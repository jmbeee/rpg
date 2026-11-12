package game.view;

import game.controller.BattleEngine;
import game.exceptions.InvalidTargetException;
import game.model.AncientKey;
import game.model.DemonKing;
import game.model.Enemy;
import game.model.GameState;
import game.model.HealthPotion;
import game.model.Hero;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/** screen used for battles. */
public class BattlePanel extends JPanel {

    private GameState state;
    private BattleEngine engine;
    private Runnable onVictory;
    private Runnable onDefeat;
    private List<JLabel> heroLabels = new ArrayList<>();
    private JLabel enemyLabel;
    private JLabel resultLabel;
    private JTextArea combatLog;
    private JButton attackButton;
    private JButton defendButton;
    private JButton abilityButton;
    private JButton itemButton;
    private BattlefieldPanel battlefield;

    public BattlePanel(GameState state, Runnable onVictory, Runnable onDefeat) {
        this.state = state;
        this.onVictory = onVictory;
        this.onDefeat = onDefeat;
        engine = new BattleEngine(state);
        setLayout(new BorderLayout());
        buildBattleInfo();
        buildActions();
        startBossEntrance();
    }

    private void buildBattleInfo() {
        Enemy enemy = engine.getEnemy();
        enemyLabel = new JLabel(getEnemyInfo(enemy), JLabel.CENTER);
        resultLabel = new JLabel("Encounter " + (state.getEncounterIndex() + 1), JLabel.CENTER);
        styleStatusLabel(enemyLabel, Color.WHITE);
        styleStatusLabel(resultLabel, new Color(255, 220, 100));

        combatLog = new JTextArea(5, 40);
        combatLog.setEditable(false);
        combatLog.setLineWrap(true);
        combatLog.setWrapStyleWord(true);
        combatLog.setBackground(new Color(25, 25, 35));
        combatLog.setForeground(Color.WHITE);
        combatLog.append("The party encountered " + enemy.getName() + ".\n");

        JPanel heroPanel = new JPanel(new GridLayout(1, state.getParty().size(), 10, 0));
        heroPanel.setBackground(new Color(20, 20, 30, 210));
        for (Hero hero : state.getParty()) {
            JLabel label = new JLabel(getHeroInfo(hero), JLabel.CENTER);
            label.setForeground(Color.WHITE);
            heroLabels.add(label);
            heroPanel.add(label);
        }

        JPanel battleInfo = new JPanel(new GridLayout(3, 1));
        battleInfo.setOpaque(false);
        battleInfo.add(heroPanel);
        battleInfo.add(enemyLabel);
        battleInfo.add(resultLabel);
        add(battleInfo, BorderLayout.NORTH);

        battlefield = new BattlefieldPanel(state.getParty(), engine.getEnemy());
        add(battlefield, BorderLayout.CENTER);
    }

    private void buildActions() {
        JPanel actions = new JPanel();
        actions.setBackground(new Color(20, 20, 30, 210));
        attackButton = new JButton("Attack");
        defendButton = new JButton("Defend");
        abilityButton = new JButton("Ability");
        itemButton = new JButton("Use Potion");

        attackButton.addActionListener(new AttackHandler());
        defendButton.addActionListener(event -> runDefendRound());
        abilityButton.addActionListener(event -> runAbilityRound());
        itemButton.addActionListener(event -> usePotion());

        actions.add(attackButton);
        actions.add(defendButton);
        actions.add(abilityButton);
        actions.add(itemButton);
        JPanel footer = new JPanel(new BorderLayout());
        footer.add(new JScrollPane(combatLog), BorderLayout.CENTER);
        footer.add(actions, BorderLayout.SOUTH);
        add(footer, BorderLayout.SOUTH);
    }

    private class AttackHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                appendMessages(engine.attackRound());
                battlefield.animateEnemyHit();
                finishRound();
            } catch (InvalidTargetException error) {
                showActionError(error);
            }
        }
    }

    private void runDefendRound() {
        try {
            appendMessages(engine.defendRound());
            finishRound();
        } catch (InvalidTargetException error) {
            showActionError(error);
        }
    }

    private void runAbilityRound() {
        try {
            appendMessages(engine.abilityRound());
            battlefield.animateEnemyHit();
            finishRound();
        } catch (InvalidTargetException error) {
            showActionError(error);
        }
    }

    private void usePotion() {
        HealthPotion potion = state.getInventory().findItem(HealthPotion.class);
        if (potion == null) {
            JOptionPane.showMessageDialog(this, "There are no Health Potions.");
            return;
        }
        Hero target = chooseLivingHero();
        if (target == null) {
            return;
        }
        int oldHealth = target.getHealth();
        potion.useOn(target);
        if (oldHealth == target.getHealth()) {
            JOptionPane.showMessageDialog(this, "That hero is already at full health.");
            return;
        }
        state.getInventory().removeItem(potion);
        combatLog.append(target.getName() + " recovered "
                + (target.getHealth() - oldHealth) + " HP.\n");
        try {
            combatLog.append(engine.enemyTurnAfterItem() + "\n");
            finishRound();
        } catch (InvalidTargetException error) {
            showActionError(error);
        }
    }

    private Hero chooseLivingHero() {
        List<Hero> livingHeroes = new ArrayList<>();
        for (Hero hero : state.getParty()) {
            if (hero.isAlive()) {
                livingHeroes.add(hero);
            }
        }
        String[] names = livingHeroes.stream().map(Hero::getName).toArray(String[]::new);
        String choice = (String) JOptionPane.showInputDialog(this, "Choose a hero",
                "Use Health Potion", JOptionPane.PLAIN_MESSAGE, null, names, names[0]);
        for (Hero hero : livingHeroes) {
            if (hero.getName().equals(choice)) {
                return hero;
            }
        }
        return null;
    }

    private void finishRound() {
        updateLabels();
        if (engine.isVictory()) {
            resultLabel.setText("Victory!");
            combatLog.append("The party won the encounter.\n");
            disableActions();
            state.finishEncounter();
            JOptionPane.showMessageDialog(this, "Encounter cleared!");
            onVictory.run();
        } else if (engine.isPartyDefeated()) {
            resultLabel.setText("You were defeated.");
            combatLog.append("The entire party was knocked out.\n");
            disableActions();
            onDefeat.run();
        }
    }

    private void appendMessages(List<String> messages) {
        for (String message : messages) {
            combatLog.append(message + "\n");
        }
        combatLog.setCaretPosition(combatLog.getDocument().getLength());
    }

    private void updateLabels() {
        for (int i = 0; i < state.getParty().size(); i++) {
            heroLabels.get(i).setText(getHeroInfo(state.getParty().get(i)));
        }
        enemyLabel.setText(getEnemyInfo(engine.getEnemy()));
    }

    private void disableActions() {
        attackButton.setEnabled(false);
        defendButton.setEnabled(false);
        abilityButton.setEnabled(false);
        itemButton.setEnabled(false);
    }

    private void showActionError(InvalidTargetException error) {
        JOptionPane.showMessageDialog(this, error.getMessage());
    }

    private void styleStatusLabel(JLabel label, Color foreground) {
        label.setForeground(foreground);
        label.setBackground(new Color(20, 20, 30, 210));
        label.setOpaque(true);
    }

    private void startBossEntrance() {
        if (!(engine.getEnemy() instanceof DemonKing)) {
            return;
        }
        disableActions();
        boolean hasKey = state.getInventory().findItem(AncientKey.class) != null;
        battlefield.startFinalEntrance(hasKey,
                () -> {
                    resultLabel.setText("The Ancient Key is glowing...");
                    combatLog.append("The Ancient Key begins to glow.\n");
                },
                () -> combatLog.append(
                        "The Ancient Key opened the Demon King's castle.\n"),
                () -> resultLabel.setText(
                        "Final Encounter - The Demon King Appears!"),
                () -> {
                    enableActions();
                    resultLabel.setText("Final Encounter");
                    combatLog.append("The Demon King stands before the party.\n");
                });
    }

    private void enableActions() {
        attackButton.setEnabled(true);
        defendButton.setEnabled(true);
        abilityButton.setEnabled(true);
        itemButton.setEnabled(true);
    }

    private String getHeroInfo(Hero hero) {
        return hero.getName() + " | HP: " + hero.getHealth() + "/"
                + hero.getMaxHealth() + " | ATK: " + hero.getAttack();
    }

    private String getEnemyInfo(Enemy enemy) {
        return enemy.getName() + " | HP: " + enemy.getHealth() + "/"
                + enemy.getMaxHealth() + " | ATK: " + enemy.getAttack();
    }
}

package game.view;

import game.controller.BattleEngine;
import game.exceptions.InvalidTargetException;
import game.model.Enemy;
import game.model.GameState;
import game.model.HealthPotion;
import game.model.Hero;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

/** screen used for battles. */
public class BattlePanel extends JPanel {

    private GameState state;
    private BattleEngine engine;
    private Runnable onVictory;
    private Runnable onDefeat;
    private BufferedImage backgroundImage;
    private BufferedImage enemyImage;
    private List<BufferedImage> heroImages = new ArrayList<>();
    private List<JLabel> heroLabels = new ArrayList<>();
    private JLabel enemyLabel;
    private JLabel resultLabel;
    private JTextArea combatLog;
    private JButton attackButton;
    private JButton defendButton;
    private JButton abilityButton;
    private JButton itemButton;
    private JPanel battlefield;
    private int enemyOffset;

    public BattlePanel(GameState state, Runnable onVictory, Runnable onDefeat) {
        this.state = state;
        this.onVictory = onVictory;
        this.onDefeat = onDefeat;
        engine = new BattleEngine(state);
        setLayout(new BorderLayout());
        loadImages(state.getParty());
        buildBattleInfo();
        buildActions();
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

        battlefield = new BattlefieldPanel();
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
                animateEnemyHit();
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
            animateEnemyHit();
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

    private void animateEnemyHit() {
        Timer timer = new Timer(60, null);
        timer.addActionListener(new ActionListener() {
            private int frame;

            @Override
            public void actionPerformed(ActionEvent event) {
                enemyOffset = frame % 2 == 0 ? -8 : 8;
                frame++;
                if (frame == 6) {
                    enemyOffset = 0;
                    timer.stop();
                }
                repaint();
            }
        });
        timer.start();
    }

    private void loadImages(List<Hero> heroes) {
        try {
            backgroundImage = ImageIO.read(new File("assets/backgrounds/forest_battle.png"));
            String enemyName = engine.getEnemy().getClass().getSimpleName()
                    .replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
            enemyImage = ImageIO.read(new File("assets/enemies/" + enemyName + ".png"));
            for (Hero hero : heroes) {
                String fileName = hero.getName().toLowerCase() + ".png";
                heroImages.add(ImageIO.read(new File("assets/heroes/" + fileName)));
            }
        } catch (IOException error) {
            JOptionPane.showMessageDialog(this, "Battle images could not be loaded.");
        }
    }

    private class BattlefieldPanel extends JPanel {

        BattlefieldPanel() {
            setPreferredSize(new Dimension(760, 330));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D graphics2d = (Graphics2D) graphics.create();
            graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            if (backgroundImage != null) {
                graphics2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            }
            int heroSize = Math.min(150, getWidth() / 5);
            for (int i = 0; i < heroImages.size(); i++) {
                int x = 25 + (i * (heroSize + 15));
                graphics2d.drawImage(heroImages.get(i), x,
                        getHeight() - heroSize - 15, heroSize, heroSize, null);
            }
            if (enemyImage != null) {
                int enemySize = Math.min(260, getHeight() - 30);
                graphics2d.drawImage(enemyImage,
                        getWidth() - enemySize - 35 + enemyOffset,
                        getHeight() - enemySize - 15, enemySize, enemySize, null);
            }
            graphics2d.dispose();
        }
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

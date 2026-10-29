package game.view;

import game.model.DemonScout;
import game.model.Hero;
import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.Comparator;
import java.util.List;
import java.util.Random;
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

    private static final Random RANDOM = new Random();
    private BufferedImage backgroundImage;
    private BufferedImage enemyImage;
    private List<BufferedImage> heroImages = new ArrayList<>();
    private int enemyOffset;

    public BattlePanel(List<Hero> heroes) {
        setLayout(new BorderLayout());
        loadImages(heroes);

        DemonScout enemy = new DemonScout();
        List<Hero> turnOrder = new ArrayList<>(heroes);
        turnOrder.sort(Comparator.comparingInt(Hero::getSpeed).reversed());
        JLabel enemyLabel = new JLabel(getEnemyInfo(enemy), JLabel.CENTER);
        JLabel resultLabel = new JLabel("Battle started", JLabel.CENTER);
        enemyLabel.setForeground(Color.WHITE);
        enemyLabel.setBackground(new Color(20, 20, 30, 210));
        enemyLabel.setOpaque(true);
        resultLabel.setForeground(new Color(255, 220, 100));
        resultLabel.setBackground(new Color(20, 20, 30, 210));
        resultLabel.setOpaque(true);
        JTextArea combatLog = new JTextArea(5, 40);
        combatLog.setEditable(false);
        combatLog.setLineWrap(true);
        combatLog.setWrapStyleWord(true);
        combatLog.setBackground(new Color(25, 25, 35));
        combatLog.setForeground(Color.WHITE);
        combatLog.append("The party encountered a Demon Scout.\n");

        JPanel heroPanel = new JPanel(new GridLayout(1, heroes.size(), 10, 0));
        heroPanel.setBackground(new Color(20, 20, 30, 210));
        List<JLabel> heroLabels = new ArrayList<>();
        for (Hero hero : heroes) {
            JLabel heroLabel = new JLabel(getPlayerInfo(hero), JLabel.CENTER);
            heroLabel.setForeground(Color.WHITE);
            heroLabels.add(heroLabel);
            heroPanel.add(heroLabel);
        }

        JPanel battleInfo = new JPanel(new GridLayout(4, 1));
        battleInfo.setOpaque(false);
        battleInfo.add(heroPanel);
        battleInfo.add(enemyLabel);
        battleInfo.add(resultLabel);
        battleInfo.add(new JScrollPane(combatLog));
        add(battleInfo, BorderLayout.CENTER);

        JPanel actions = new JPanel();
        actions.setBackground(new Color(20, 20, 30, 210));
        JButton attackButton = new JButton("Attack");
        JButton defendButton = new JButton("Defend");
        attackButton.addActionListener(event -> {
            for (Hero hero : turnOrder) {
                if (hero.isAlive() && enemy.isAlive()) {
                    int damage = hero.attack(enemy);
                    combatLog.append(hero.getName() + " dealt "
                            + damage + " damage.\n");
                }
            }
            animateEnemyHit();
            if (enemy.isAlive()) {
                Hero target = getRandomLivingHero(heroes);
                int damage = enemy.attack(target);
                combatLog.append(enemy.getName() + " dealt " + damage
                        + " damage to " + target.getName() + ".\n");
                resultLabel.setText(enemy.getName() + " attacked "
                        + target.getName() + ".");
            }
            updateHeroLabels(heroes, heroLabels);
            enemyLabel.setText(getEnemyInfo(enemy));

            if (!enemy.isAlive()) {
                resultLabel.setText("Victory!");
                combatLog.append("The party won the battle.\n");
                attackButton.setEnabled(false);
                defendButton.setEnabled(false);
            } else if (isPartyDefeated(heroes)) {
                resultLabel.setText("You were defeated.");
                combatLog.append("The party was defeated.\n");
                attackButton.setEnabled(false);
                defendButton.setEnabled(false);
            }
        });
        defendButton.addActionListener(event -> {
            Hero target = getRandomLivingHero(heroes);
            int damageTaken = enemy.attack(target, true);

            updateHeroLabels(heroes, heroLabels);
            resultLabel.setText(target.getName() + " defended and took "
                    + damageTaken + " damage.");
            combatLog.append(target.getName() + " defended and took "
                    + damageTaken + " damage.\n");

            if (isPartyDefeated(heroes)) {
                resultLabel.setText("You were defeated.");
                combatLog.append("The party was defeated.\n");
                attackButton.setEnabled(false);
                defendButton.setEnabled(false);
            }
        });
        actions.add(attackButton);
        actions.add(defendButton);
        add(actions, BorderLayout.SOUTH);
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

    private Hero getRandomLivingHero(List<Hero> heroes) {
        List<Hero> livingHeroes = new ArrayList<>();
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                livingHeroes.add(hero);
            }
        }
        return livingHeroes.get(RANDOM.nextInt(livingHeroes.size()));
    }

    private boolean isPartyDefeated(List<Hero> heroes) {
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                return false;
            }
        }
        return true;
    }

    private void updateHeroLabels(List<Hero> heroes, List<JLabel> heroLabels) {
        for (int i = 0; i < heroes.size(); i++) {
            heroLabels.get(i).setText(getPlayerInfo(heroes.get(i)));
        }
    }

    private void loadImages(List<Hero> heroes) {
        try {
            backgroundImage = ImageIO.read(new File("assets/backgrounds/forest_battle.png"));
            enemyImage = ImageIO.read(new File("assets/enemies/demon_scout.png"));
            for (Hero hero : heroes) {
                String fileName = hero.getName().toLowerCase() + ".png";
                heroImages.add(ImageIO.read(new File("assets/heroes/" + fileName)));
            }
        } catch (IOException error) {
            JOptionPane.showMessageDialog(this, "Battle images could not be loaded.");
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2d = (Graphics2D) graphics.create();
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        if (backgroundImage != null) {
            graphics2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
        for (int i = 0; i < heroImages.size(); i++) {
            graphics2d.drawImage(heroImages.get(i), 40 + (i * 120),
                    260, 110, 110, null);
        }
        if (enemyImage != null) {
            graphics2d.drawImage(enemyImage, getWidth() - 260 + enemyOffset,
                    180, 180, 180, null);
        }
        graphics2d.dispose();
    }

    private String getPlayerInfo(Hero player) {
        return player.getName() + " | HP: " + player.getHealth()
                + " | Attack: " + player.getAttack();
    }

    private String getEnemyInfo(DemonScout enemy) {
        return enemy.getName() + " | HP: " + enemy.getHealth()
                + " | Attack: " + enemy.getAttack();
    }
}

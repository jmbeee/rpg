package game.view;

import game.model.DemonScout;
import game.model.Hero;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
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

/** screen used for battles. */
public class BattlePanel extends JPanel {

    private static final Random RANDOM = new Random();
    private BufferedImage backgroundImage;
    private BufferedImage enemyImage;

    public BattlePanel(List<Hero> heroes) {
        setLayout(new BorderLayout());
        loadImages();

        DemonScout enemy = new DemonScout();
        List<Hero> turnOrder = new ArrayList<>(heroes);
        turnOrder.sort(Comparator.comparingInt(Hero::getSpeed).reversed());
        JLabel enemyLabel = new JLabel(getEnemyInfo(enemy), JLabel.CENTER);
        JLabel resultLabel = new JLabel("Battle started", JLabel.CENTER);

        JPanel heroPanel = new JPanel(new GridLayout(1, heroes.size(), 10, 0));
        heroPanel.setOpaque(false);
        List<JLabel> heroLabels = new ArrayList<>();
        for (Hero hero : heroes) {
            JLabel heroLabel = new JLabel(getPlayerInfo(hero), JLabel.CENTER);
            heroLabels.add(heroLabel);
            heroPanel.add(heroLabel);
        }

        JPanel battleInfo = new JPanel(new GridLayout(3, 1));
        battleInfo.setOpaque(false);
        battleInfo.add(heroPanel);
        battleInfo.add(enemyLabel);
        battleInfo.add(resultLabel);
        add(battleInfo, BorderLayout.CENTER);

        JPanel actions = new JPanel();
        actions.setOpaque(false);
        JButton attackButton = new JButton("Attack");
        JButton defendButton = new JButton("Defend");
        attackButton.addActionListener(event -> {
            for (Hero hero : turnOrder) {
                if (hero.isAlive() && enemy.isAlive()) {
                    hero.attack(enemy);
                }
            }
            if (enemy.isAlive()) {
                Hero target = getRandomLivingHero(heroes);
                enemy.attack(target);
                resultLabel.setText(enemy.getName() + " attacked "
                        + target.getName() + ".");
            }
            updateHeroLabels(heroes, heroLabels);
            enemyLabel.setText(getEnemyInfo(enemy));

            if (!enemy.isAlive()) {
                resultLabel.setText("Victory!");
                attackButton.setEnabled(false);
                defendButton.setEnabled(false);
            } else if (isPartyDefeated(heroes)) {
                resultLabel.setText("You were defeated.");
                attackButton.setEnabled(false);
                defendButton.setEnabled(false);
            }
        });
        defendButton.addActionListener(event -> {
            Hero target = getRandomLivingHero(heroes);
            int oldHealth = target.getHealth();
            enemy.attack(target, true);
            int damageTaken = oldHealth - target.getHealth();

            updateHeroLabels(heroes, heroLabels);
            resultLabel.setText(target.getName() + " defended and took "
                    + damageTaken + " damage.");

            if (isPartyDefeated(heroes)) {
                resultLabel.setText("You were defeated.");
                attackButton.setEnabled(false);
                defendButton.setEnabled(false);
            }
        });
        actions.add(attackButton);
        actions.add(defendButton);
        add(actions, BorderLayout.SOUTH);
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

    private void loadImages() {
        try {
            backgroundImage = ImageIO.read(new File("assets/backgrounds/forest_battle.png"));
            enemyImage = ImageIO.read(new File("assets/enemies/demon_scout.png"));
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
        if (enemyImage != null) {
            graphics2d.drawImage(enemyImage, getWidth() - 260, 180, 180, 180, null);
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

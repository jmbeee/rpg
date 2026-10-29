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
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/** screen used for battles. */
public class BattlePanel extends JPanel {

    private BufferedImage backgroundImage;
    private BufferedImage enemyImage;

    public BattlePanel(List<Hero> heroes) {
        setLayout(new BorderLayout());
        loadImages();

        Hero player = heroes.get(0);
        DemonScout enemy = new DemonScout();
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
            player.attack(enemy);
            if (enemy.isAlive()) {
                enemy.attack(player);
            }
            updateHeroLabels(heroes, heroLabels);
            enemyLabel.setText(getEnemyInfo(enemy));

            if (!enemy.isAlive()) {
                resultLabel.setText("Victory!");
                attackButton.setEnabled(false);
                defendButton.setEnabled(false);
            } else if (!player.isAlive()) {
                resultLabel.setText("You were defeated.");
                attackButton.setEnabled(false);
                defendButton.setEnabled(false);
            }
        });
        defendButton.addActionListener(event -> {
            int oldHealth = player.getHealth();
            player.takeDamage(enemy.getAttack(), true);
            int damageTaken = oldHealth - player.getHealth();

            updateHeroLabels(heroes, heroLabels);
            resultLabel.setText(player.getName() + " defended and took "
                    + damageTaken + " damage.");

            if (!player.isAlive()) {
                resultLabel.setText("You were defeated.");
                attackButton.setEnabled(false);
                defendButton.setEnabled(false);
            }
        });
        actions.add(attackButton);
        actions.add(defendButton);
        add(actions, BorderLayout.SOUTH);
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

package game.view;

import game.model.DemonScout;
import game.model.Frieren;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** screen used for battles. */
public class BattlePanel extends JPanel {

    public BattlePanel() {
        setLayout(new BorderLayout());

        Frieren player = new Frieren();
        DemonScout enemy = new DemonScout();
        JLabel playerLabel = new JLabel(getPlayerInfo(player), JLabel.CENTER);
        JLabel enemyLabel = new JLabel(getEnemyInfo(enemy), JLabel.CENTER);
        JLabel resultLabel = new JLabel("Battle started", JLabel.CENTER);

        JPanel battleInfo = new JPanel(new GridLayout(3, 1));
        battleInfo.add(playerLabel);
        battleInfo.add(enemyLabel);
        battleInfo.add(resultLabel);
        add(battleInfo, BorderLayout.CENTER);

        JPanel actions = new JPanel();
        JButton attackButton = new JButton("Attack");
        JButton defendButton = new JButton("Defend");
        attackButton.addActionListener(event -> {
            player.attack(enemy);
            if (enemy.isAlive()) {
                enemy.attack(player);
            }
            playerLabel.setText(getPlayerInfo(player));
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

            playerLabel.setText(getPlayerInfo(player));
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

    private String getPlayerInfo(Frieren player) {
        return player.getName() + " | HP: " + player.getHealth()
                + " | Attack: " + player.getAttack();
    }

    private String getEnemyInfo(DemonScout enemy) {
        return enemy.getName() + " | HP: " + enemy.getHealth()
                + " | Attack: " + enemy.getAttack();
    }
}

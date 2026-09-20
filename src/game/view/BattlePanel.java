package game.view;

import game.model.DemonScout;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** screen used for battles. */
public class BattlePanel extends JPanel {

    private static final int PLAYER_ATTACK = 20;

    public BattlePanel() {
        setLayout(new BorderLayout());

        DemonScout enemy = new DemonScout();
        JLabel enemyLabel = new JLabel(getEnemyInfo(enemy), JLabel.CENTER);
        add(enemyLabel, BorderLayout.CENTER);

        JPanel actions = new JPanel();
        JButton attackButton = new JButton("Attack");
        attackButton.addActionListener(event -> {
            enemy.takeDamage(PLAYER_ATTACK);
            enemyLabel.setText(getEnemyInfo(enemy));
        });
        actions.add(attackButton);
        actions.add(new JButton("Defend"));
        add(actions, BorderLayout.SOUTH);
    }

    private String getEnemyInfo(DemonScout enemy) {
        return enemy.getName() + " | HP: " + enemy.getHealth()
                + " | Attack: " + enemy.getAttack();
    }
}

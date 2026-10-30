package game.view;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** final screen shown after victory or defeat. */
public class EndPanel extends JPanel {

    public EndPanel(boolean victory, Runnable onMainMenu) {
        setLayout(new BorderLayout());
        String message = victory
                ? "Victory! The Demon King was defeated."
                : "Game Over - The party was defeated.";
        JLabel title = new JLabel(message, JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(28.0f));
        add(title, BorderLayout.CENTER);

        JButton menuButton = new JButton("Return to Main Menu");
        menuButton.addActionListener(event -> onMainMenu.run());
        add(menuButton, BorderLayout.SOUTH);
    }
}

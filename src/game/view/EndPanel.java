package game.view;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** final screen shown after victory or defeat. */
public class EndPanel extends JPanel {

    public EndPanel(boolean victory, Runnable onMainMenu) {
        setLayout(new BorderLayout());
        UiTheme.styleScreen(this);
        String message = victory
                ? "Victory! The Demon King was defeated."
                : "Game Over - The party was defeated.";
        JLabel title = new JLabel(message, JLabel.CENTER);
        UiTheme.styleTitle(title, 30.0f);
        title.setBorder(BorderFactory.createEmptyBorder(80, 20, 80, 20));
        add(title, BorderLayout.CENTER);

        JButton menuButton = new JButton("Return to Main Menu");
        UiTheme.styleButton(menuButton);
        menuButton.addActionListener(event -> onMainMenu.run());
        add(menuButton, BorderLayout.SOUTH);
    }
}

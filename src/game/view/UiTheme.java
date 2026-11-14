package game.view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** shared colors and simple styles used by the game screens. */
public final class UiTheme {

    public static final Color BACKGROUND = new Color(18, 20, 32);
    public static final Color PANEL = new Color(31, 35, 52);
    public static final Color CARD = new Color(43, 48, 69);
    public static final Color GOLD = new Color(238, 194, 92);
    public static final Color TEXT = new Color(242, 239, 230);
    public static final Color MUTED = new Color(183, 188, 205);
    public static final Color BUTTON = new Color(76, 89, 139);

    private UiTheme() {
    }

    /** applies the standard style to a screen. */
    public static void styleScreen(JPanel panel) {
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
    }

    /** applies the standard style to a title. */
    public static void styleTitle(JLabel label, float size) {
        label.setForeground(GOLD);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.round(size)));
    }

    /** applies the standard style to a button. */
    public static void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        button.setMargin(new java.awt.Insets(9, 18, 9, 18));
    }

    /** applies the standard style to a card. */
    public static void styleCard(JPanel panel) {
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(91, 99, 129)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
    }
}

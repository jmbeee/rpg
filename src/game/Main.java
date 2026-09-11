package game;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/** game starts here. */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("Journey to the Demon King");
            window.setSize(800, 600);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JLabel title = new JLabel("Journey to the Demon King");
            window.add(title);
            window.setVisible(true);
        });
    }
}

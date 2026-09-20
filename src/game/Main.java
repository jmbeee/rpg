package game;

import game.view.BattlePanel;
import game.view.CampPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/** game starts here. */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("Journey to the Demon King");
            window.setSize(800, 600);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ImageIcon gif = new ImageIcon("assets/backgrounds/frieren.gif");
            ImageIcon scaledGif = new ImageIcon(gif.getImage().getScaledInstance(-1, 600, Image.SCALE_REPLICATE));
            JLabel background = new JLabel(scaledGif);
            background.setLayout(new BorderLayout());

            JButton enterGame = new JButton("Enter Game");

            // move to the next page or panel
            enterGame.addActionListener(event -> {
                Runnable onBack = () -> {
                    window.setContentPane(background);
                    window.revalidate();
                    window.repaint();
                };
                Runnable onContinue = () -> {
                    Runnable onStart = () -> {
                        window.setContentPane(new BattlePanel());
                        window.revalidate();
                        window.repaint();
                    };
                    window.setContentPane(new CampPanel(onStart));
                    window.revalidate();
                    window.repaint();
                };
                window.setContentPane(new SelectionPanel(onBack, onContinue));
                window.revalidate();
            });

            JPanel buttons = new JPanel(new GridBagLayout());
            buttons.setOpaque(false);
            buttons.add(enterGame);
            background.add(buttons, BorderLayout.CENTER);
            window.setContentPane(background);
            window.setVisible(true);
        });
    }
}

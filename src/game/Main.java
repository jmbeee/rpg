package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
            enterGame.addActionListener(event ->
                JOptionPane.showMessageDialog(window, "test"));
            JPanel buttons = new JPanel(new GridBagLayout());
            buttons.setOpaque(false);
            buttons.add(enterGame);
            background.add(buttons, BorderLayout.CENTER);
            window.setContentPane(background);
            window.setVisible(true);
        });
    }
}

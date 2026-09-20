package game.view;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** screen shown before starting a battle. */
public class CampPanel extends JPanel {

    public CampPanel() {
        setLayout(new GridLayout(4, 1, 10, 10));

        add(new JLabel("Party Camp", JLabel.CENTER));
        add(new JButton("View Party"));
        add(new JButton("Inventory"));
        add(new JButton("Start Journey"));
    }
}

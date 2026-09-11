package game;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** screen for choosing heroes. */
public class SelectionPanel extends JPanel {

    public SelectionPanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Choose Your Party", JLabel.CENTER);
        add(title, BorderLayout.NORTH);
    }
}

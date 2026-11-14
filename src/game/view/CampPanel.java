package game.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** screen shown before starting a battle. */
public class CampPanel extends JPanel {

    public CampPanel(int encounterNumber, Runnable onStart,
            Runnable onParty, Runnable onInventory, Runnable onSave) {
        setLayout(new BorderLayout(20, 24));
        UiTheme.styleScreen(this);

        JLabel title = new JLabel("Party Camp", JLabel.CENTER);
        UiTheme.styleTitle(title, 30.0f);
        JLabel subtitle = new JLabel("Prepare for Encounter " + encounterNumber,
                JLabel.CENTER);
        subtitle.setForeground(UiTheme.MUTED);
        JPanel header = new JPanel(new GridLayout(2, 1, 4, 4));
        header.setOpaque(false);
        header.add(title);
        header.add(subtitle);
        add(header, BorderLayout.NORTH);

        JPanel menu = new JPanel(new GridLayout(4, 1, 12, 12));
        menu.setBackground(UiTheme.PANEL);
        menu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.CARD),
                BorderFactory.createEmptyBorder(28, 180, 28, 180)));

        JButton partyButton = new JButton("View Party");
        UiTheme.styleButton(partyButton);
        partyButton.addActionListener(event -> onParty.run());
        menu.add(partyButton);

        JButton inventoryButton = new JButton("Inventory");
        UiTheme.styleButton(inventoryButton);
        inventoryButton.addActionListener(event -> onInventory.run());
        menu.add(inventoryButton);

        JButton saveButton = new JButton("Save Game");
        UiTheme.styleButton(saveButton);
        saveButton.addActionListener(event -> onSave.run());
        menu.add(saveButton);

        JButton startButton = new JButton("Start Journey");
        UiTheme.styleButton(startButton);
        startButton.addActionListener(event -> onStart.run());
        menu.add(startButton);
        add(menu, BorderLayout.CENTER);
    }
}

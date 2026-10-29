package game.view;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** screen shown before starting a battle. */
public class CampPanel extends JPanel {

    public CampPanel(int encounterNumber, Runnable onStart,
            Runnable onInventory, Runnable onSave) {
        setLayout(new GridLayout(5, 1, 10, 10));

        add(new JLabel("Party Camp - Next Encounter: "
                + encounterNumber, JLabel.CENTER));

        JButton partyButton = new JButton("View Party");
        partyButton.addActionListener(event -> onInventory.run());
        add(partyButton);

        JButton inventoryButton = new JButton("Inventory");
        inventoryButton.addActionListener(event -> onInventory.run());
        add(inventoryButton);

        JButton saveButton = new JButton("Save Game");
        saveButton.addActionListener(event -> onSave.run());
        add(saveButton);

        JButton startButton = new JButton("Start Journey");
        startButton.addActionListener(event -> onStart.run());
        add(startButton);
    }
}

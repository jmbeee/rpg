package game.view;

import game.model.Inventory;
import game.model.Item;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** screen for viewing party items. */
public class InventoryPanel extends JPanel {

    public InventoryPanel(Inventory inventory, Runnable onBack) {
        setLayout(new BorderLayout());
        add(new JLabel("Inventory", JLabel.CENTER), BorderLayout.NORTH);

        JPanel itemList = new JPanel();
        itemList.setLayout(new BoxLayout(itemList, BoxLayout.Y_AXIS));
        for (Item item : inventory.getItems()) {
            itemList.add(new JLabel(item.getName() + " - " + item.getDescription()));
        }
        add(itemList, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(event -> onBack.run());
        add(backButton, BorderLayout.SOUTH);
    }
}

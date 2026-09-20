package game.view;

import game.model.Inventory;
import game.model.Item;
import java.awt.BorderLayout;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** screen for viewing party items. */
public class InventoryPanel extends JPanel {

    public InventoryPanel(Inventory inventory, Runnable onBack) {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Inventory", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(26.0f));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        JPanel itemList = new JPanel();
        itemList.setLayout(new BoxLayout(itemList, BoxLayout.Y_AXIS));
        itemList.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        for (Item item : inventory.getItems()) {
            JPanel itemCard = new JPanel(new BorderLayout(15, 0));
            itemCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEtchedBorder(),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));

            String fileName = item.getName().toLowerCase().replace(" ", "_") + ".png";
            ImageIcon icon = new ImageIcon("assets/items/" + fileName);
            Image scaledImage = icon.getImage().getScaledInstance(64, 64, Image.SCALE_REPLICATE);
            itemCard.add(new JLabel(new ImageIcon(scaledImage)), BorderLayout.WEST);
            itemCard.add(new JLabel("<html><b>" + item.getName() + "</b><br>"
                    + item.getDescription() + "</html>"), BorderLayout.CENTER);

            itemList.add(itemCard);
            itemList.add(Box.createVerticalStrut(10));
        }
        add(itemList, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(event -> onBack.run());
        add(backButton, BorderLayout.SOUTH);
    }
}

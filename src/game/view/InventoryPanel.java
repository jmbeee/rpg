package game.view;

import game.model.AncientKey;
import game.model.HealthPotion;
import game.model.Hero;
import game.model.Inventory;
import game.model.IronSword;
import game.model.Item;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/** screen for viewing party items. */
public class InventoryPanel extends JPanel {

    public InventoryPanel(Inventory inventory, List<Hero> heroes, Runnable onBack) {
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
                    + getItemType(item) + "<br>" + item.getDescription()
                    + "</html>"), BorderLayout.CENTER);

            if (item instanceof HealthPotion) {
                JButton useButton = new JButton("Use");
                useButton.addActionListener(event -> usePotion(
                        inventory, (HealthPotion) item, heroes, onBack));
                itemCard.add(useButton, BorderLayout.EAST);
            } else if (item instanceof IronSword) {
                JButton equipButton = new JButton("Equip");
                equipButton.addActionListener(event -> equipSword(
                        inventory, (IronSword) item, heroes, onBack));
                itemCard.add(equipButton, BorderLayout.EAST);
            }

            itemList.add(itemCard);
            itemList.add(Box.createVerticalStrut(10));
        }
        add(itemList, BorderLayout.CENTER);

        JPanel partyStatus = new JPanel(new GridLayout(heroes.size() + 1, 1, 5, 5));
        partyStatus.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 30));
        partyStatus.add(new JLabel("Party Status"));
        for (Hero hero : heroes) {
            String equipment = hero.getEquippedItem() == null
                    ? "None" : hero.getEquippedItem().getName();
            partyStatus.add(new JLabel("<html><b>" + hero.getName() + "</b>  HP: "
                    + hero.getHealth() + "/" + hero.getMaxHealth()
                    + "  Level: " + hero.getLevel()
                    + "<br>Equipment: " + equipment + "</html>"));
        }
        add(partyStatus, BorderLayout.EAST);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(event -> onBack.run());
        add(backButton, BorderLayout.SOUTH);
    }

    private void usePotion(Inventory inventory, HealthPotion potion,
            List<Hero> heroes, Runnable onBack) {
        String[] heroNames = new String[heroes.size()];
        for (int i = 0; i < heroes.size(); i++) {
            heroNames[i] = heroes.get(i).getName();
        }

        String selectedName = (String) JOptionPane.showInputDialog(this,
                "Choose a hero", "Use Health Potion",
                JOptionPane.PLAIN_MESSAGE, null, heroNames, heroNames[0]);

        for (Hero hero : heroes) {
            if (hero.getName().equals(selectedName)) {
                int oldHealth = hero.getHealth();
                potion.useOn(hero);
                if (hero.getHealth() == oldHealth) {
                    JOptionPane.showMessageDialog(this, "That hero cannot be healed.");
                    return;
                }
                inventory.removeItem(potion);
                JOptionPane.showMessageDialog(this, hero.getName() + " recovered "
                        + (hero.getHealth() - oldHealth) + " HP.");
                onBack.run();
                return;
            }
        }
    }

    private void equipSword(Inventory inventory, IronSword sword,
            List<Hero> heroes, Runnable onBack) {
        String[] heroNames = new String[heroes.size()];
        for (int i = 0; i < heroes.size(); i++) {
            heroNames[i] = heroes.get(i).getName();
        }

        String selectedName = (String) JOptionPane.showInputDialog(this,
                "Choose a hero", "Equip Iron Sword",
                JOptionPane.PLAIN_MESSAGE, null, heroNames, heroNames[0]);

        for (Hero hero : heroes) {
            if (hero.getName().equals(selectedName)) {
                if (!hero.equip(sword)) {
                    JOptionPane.showMessageDialog(this,
                            "That hero already has equipment.");
                    return;
                }
                inventory.removeItem(sword);
                JOptionPane.showMessageDialog(this,
                        hero.getName() + " equipped the Iron Sword.");
                onBack.run();
                return;
            }
        }
    }

    private String getItemType(Item item) {
        if (item instanceof HealthPotion) {
            return "Consumable";
        }
        if (item instanceof IronSword) {
            return "Equipment";
        }
        if (item instanceof AncientKey) {
            return "Key Item";
        }
        return "Item";
    }
}

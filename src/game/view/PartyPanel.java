package game.view;

import game.model.Hero;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** screen for viewing the selected heroes. */
public class PartyPanel extends JPanel {

    public PartyPanel(List<Hero> heroes, Runnable onBack) {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Your Party", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(26.0f));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        JPanel heroList = new JPanel(new GridLayout(1, heroes.size(), 15, 15));
        heroList.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (Hero hero : heroes) {
            heroList.add(createHeroCard(hero));
        }
        add(heroList, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(event -> onBack.run());
        add(backButton, BorderLayout.SOUTH);
    }

    private JPanel createHeroCard(Hero hero) {
        JPanel card = new JPanel(new BorderLayout(5, 10));
        card.setBorder(BorderFactory.createEtchedBorder());

        String fileName = hero.getName().toLowerCase() + ".png";
        ImageIcon icon = new ImageIcon("assets/heroes/" + fileName);
        Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        card.add(new JLabel(new ImageIcon(image), JLabel.CENTER), BorderLayout.NORTH);

        String equipment = hero.getEquippedItem() == null
                ? "None" : hero.getEquippedItem().getName();
        JLabel details = new JLabel("<html><div style='text-align:center'>"
                + "<h2>" + hero.getName() + "</h2>"
                + "Level: " + hero.getLevel() + "<br>"
                + "HP: " + hero.getHealth() + "/" + hero.getMaxHealth() + "<br>"
                + "Attack: " + hero.getAttack() + "<br>"
                + "Defense: " + hero.getDefense() + "<br>"
                + "Speed: " + hero.getSpeed() + "<br>"
                + "Equipment: " + equipment
                + "</div></html>", JLabel.CENTER);
        card.add(details, BorderLayout.CENTER);
        return card;
    }
}

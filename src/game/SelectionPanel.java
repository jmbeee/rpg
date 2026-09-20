package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** screen for choosing heroes. */
public class SelectionPanel extends JPanel {

    private int selectedCount = 0;
    private final int MAX_SELECTION = 3;
    private JLabel counterLabel;
    private JButton continueButton;

    public SelectionPanel(Runnable onBack, Runnable onContinue) {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Choose Your Party", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(24.0f));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        
        counterLabel = new JLabel("Selected: 0/3", JLabel.CENTER);
        counterLabel.setFont(counterLabel.getFont().deriveFont(16.0f));
        
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(counterLabel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        JPanel heroesPanel = new JPanel();
        heroesPanel.setLayout(new GridLayout(1, 4, 15, 15));
        heroesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[][] heroData = {
            {"Frieren", "Mage", "90", "28", "4", "12", "Offensive Magic", "frieren.png"},
            {"Himmel", "Swordsman", "105", "23", "6", "18", "Double Slash", "himmel.png"},
            {"Heiter", "Priest", "115", "18", "7", "10", "Heal", "heiter.png"},
            {"Eisen", "Warrior", "140", "25", "10", "8", "Heavy Strike", "eisen.png"}
        };

        List<JCheckBox> checkBoxes = new ArrayList<>();

        for (String[] data : heroData) {
            JPanel heroCard = new JPanel();
            heroCard.setLayout(new BoxLayout(heroCard, BoxLayout.Y_AXIS));
            heroCard.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

            JLabel nameLabel = new JLabel(data[0], JLabel.CENTER);
            nameLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            nameLabel.setFont(nameLabel.getFont().deriveFont(18.0f));
            nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            heroCard.add(nameLabel);
            
            java.io.File imgFile = new java.io.File("assets/heroes/" + data[7]);
            if (imgFile.exists()) {
                javax.swing.ImageIcon icon = new javax.swing.ImageIcon(imgFile.getAbsolutePath());
                java.awt.Image scaled = icon.getImage().getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new javax.swing.ImageIcon(scaled));
                imageLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
                heroCard.add(imageLabel);
            } else {
                JLabel noImage = new JLabel("(No Image)");
                noImage.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                noImage.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
                heroCard.add(noImage);
            }
            
            JLabel classLabel = new JLabel("Class: " + data[1]);
            classLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            
            JLabel hpLabel = new JLabel("HP: " + data[2]);
            hpLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            
            JLabel atkLabel = new JLabel("Attack: " + data[3]);
            atkLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            
            JLabel defLabel = new JLabel("Defense: " + data[4]);
            defLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            
            JLabel spdLabel = new JLabel("Speed: " + data[5]);
            spdLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            
            JLabel abilityLabel = new JLabel("Ability: " + data[6]);
            abilityLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            abilityLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            
            JCheckBox selectBox = new JCheckBox("Select");
            selectBox.setAlignmentX(JCheckBox.CENTER_ALIGNMENT);
            checkBoxes.add(selectBox);

            selectBox.addActionListener(e -> {
                if (selectBox.isSelected()) {
                    if (selectedCount >= MAX_SELECTION) {
                        selectBox.setSelected(false);
                    } else {
                        selectedCount++;
                    }
                } else {
                    selectedCount--;
                }
                updateStatus(checkBoxes);
            });

            heroCard.add(classLabel);
            heroCard.add(hpLabel);
            heroCard.add(atkLabel);
            heroCard.add(defLabel);
            heroCard.add(spdLabel);
            heroCard.add(abilityLabel);
            heroCard.add(selectBox);

            heroesPanel.add(heroCard);
        }

        add(heroesPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel();
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> onBack.run());
        
        continueButton = new JButton("Continue");
        continueButton.setEnabled(false);
        continueButton.addActionListener(e -> onContinue.run());
        
        bottomPanel.add(backButton);
        bottomPanel.add(continueButton);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void updateStatus(List<JCheckBox> checkBoxes) {
        counterLabel.setText("Selected: " + selectedCount + "/" + MAX_SELECTION);
        
        boolean maxReached = (selectedCount >= MAX_SELECTION);
        for (JCheckBox cb : checkBoxes) {
            if (!cb.isSelected()) {
                cb.setEnabled(!maxReached);
            }
        }
        
        continueButton.setEnabled(maxReached);
    }
}


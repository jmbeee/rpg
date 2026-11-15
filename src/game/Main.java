package game;

import game.exceptions.SaveGameException;
import game.io.SaveService;
import game.model.AncientKey;
import game.model.GameState;
import game.model.HealthPotion;
import game.model.Inventory;
import game.model.IronSword;
import game.view.BattlePanel;
import game.view.CampPanel;
import game.view.EndPanel;
import game.view.InventoryPanel;
import game.view.PartyPanel;
import game.view.UiTheme;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.util.function.Consumer;

/** game starts here. */
public class Main {

    private JFrame window;
    private SaveService saveService = new SaveService();
    private GameState currentState;

    /** Starts the Swing application on the event thread. */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().start());
    }

    private void start() {
        window = new JFrame("Journey to the Demon King");
        window.setSize(900, 650);
        window.setMinimumSize(new Dimension(850, 620));
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                handleWindowClosing();
            }
        });
        showMainMenu();
        window.setVisible(true);
    }

    private void showMainMenu() {
        currentState = null;
        JLabel background = createMenuBackground();
        JPanel buttons = new JPanel(new GridLayout(3, 1, 10, 10));
        buttons.setOpaque(false);
        buttons.setBorder(BorderFactory.createEmptyBorder(200, 330, 200, 330));

        JButton newGameButton = new JButton("New Game");
        JButton loadButton = new JButton("Load Game");
        JButton quitButton = new JButton("Quit");
        UiTheme.styleButton(newGameButton);
        UiTheme.styleButton(loadButton);
        UiTheme.styleButton(quitButton);
        newGameButton.addActionListener(event -> showSelection());
        loadButton.addActionListener(event -> loadGame());
        quitButton.addActionListener(event -> window.dispose());

        buttons.add(newGameButton);
        buttons.add(loadButton);
        buttons.add(quitButton);
        JLabel title = new JLabel("Journey to the Demon King", JLabel.CENTER);
        UiTheme.styleTitle(title, 32.0f);
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        background.add(title, BorderLayout.NORTH);
        background.add(buttons, BorderLayout.CENTER);
        showPanel(background);
    }

    private JLabel createMenuBackground() {
        ImageIcon gif = new ImageIcon("assets/backgrounds/frieren.gif");
        Image scaled = gif.getImage().getScaledInstance(-1, 650, Image.SCALE_REPLICATE);
        JLabel background = new JLabel(new ImageIcon(scaled));
        background.setLayout(new BorderLayout());
        return background;
    }

    private void showSelection() {
        SelectionPanel selection = new SelectionPanel(
                this::showMainMenu,
                heroes -> {
                    Inventory inventory = new Inventory();
                    inventory.addItem(new HealthPotion());
                    inventory.addItem(new HealthPotion());
                    inventory.addItem(new IronSword());
                    inventory.addItem(new AncientKey());
                    showCamp(new GameState(heroes, inventory));
                });
        showPanel(selection);
    }

    private void showCamp(GameState state) {
        currentState = state;
        if (state.isJourneyComplete()) {
            showEndScreen(true);
            return;
        }
        Runnable onStart = () -> showBattle(state);
        Runnable onParty = () -> showPanel(new PartyPanel(
                state.getParty(), () -> showCamp(state)));
        Runnable onInventory = () -> showPanel(new InventoryPanel(
                state.getInventory(), state.getParty(), () -> showCamp(state)));
        Runnable onSave = () -> saveGame(state);
        showPanel(new CampPanel(state.getEncounterIndex() + 1,
                onStart, onParty, onInventory, onSave));
    }

    private void showBattle(GameState state) {
        currentState = state;
        Consumer<Boolean> onVictory = proceed -> {
            if (state.isJourneyComplete()) {
                showEndScreen(true);
            } else {
                state.getParty().forEach(hero -> hero.levelUp());
                state.getInventory().addItem(new HealthPotion());
                if (proceed) {
                    showBattle(state);
                } else {
                    showCamp(state);
                }
            }
        };
        Runnable onDefeat = () -> showEndScreen(false);
        showPanel(new BattlePanel(state, onVictory, onDefeat));
    }

    private void showEndScreen(boolean victory) {
        showPanel(new EndPanel(victory, this::showMainMenu));
    }

    private boolean saveGame(GameState state) {
        Integer slot = chooseSlot("Save Game");
        if (slot == null) {
            return false;
        }
        try {
            saveService.save(slot, state);
            JOptionPane.showMessageDialog(window, "Game saved in slot " + slot + ".");
            return true;
        } catch (SaveGameException error) {
            JOptionPane.showMessageDialog(window, error.getMessage());
            return false;
        }
    }

    private void handleWindowClosing() {
        if (currentState == null) {
            window.dispose();
            return;
        }
        Object[] choices = {"Save and Exit", "Exit Without Saving", "Cancel"};
        int choice = JOptionPane.showOptionDialog(window,
                "Would you like to save before closing the game?",
                "Close Game", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
        if (choice == 0 && saveGame(currentState)) {
            window.dispose();
        } else if (choice == 1) {
            window.dispose();
        }
    }

    private void loadGame() {
        Integer slot = chooseSlot("Load Game");
        if (slot == null) {
            return;
        }
        try {
            showCamp(saveService.load(slot));
        } catch (SaveGameException error) {
            JOptionPane.showMessageDialog(window, error.getMessage());
        }
    }

    private Integer chooseSlot(String title) {
        Integer[] slots = {1, 2, 3};
        return (Integer) JOptionPane.showInputDialog(window, "Choose a slot",
                title, JOptionPane.PLAIN_MESSAGE, null, slots, slots[0]);
    }

    private void showPanel(java.awt.Container panel) {
        window.setContentPane(panel);
        window.revalidate();
        window.repaint();
    }
}

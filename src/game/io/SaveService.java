package game.io;

import game.exceptions.SaveGameException;
import game.model.AncientKey;
import game.model.Eisen;
import game.model.Frieren;
import game.model.GameSlot;
import game.model.GameState;
import game.model.HealthPotion;
import game.model.Heiter;
import game.model.Hero;
import game.model.Himmel;
import game.model.Inventory;
import game.model.IronSword;
import game.model.Item;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/** saves and loads the three game slots. */
public class SaveService {

    private static final int SLOT_COUNT = 3;
    private static final Path SAVE_DIRECTORY = Paths.get("saves");

    /** Writes binary and text data for one of three slots. */
    public void save(int slot, GameState state) throws SaveGameException {
        validateSlot(slot);
        try {
            Files.createDirectories(SAVE_DIRECTORY);
            saveBinary(slot, state);
            saveText(slot, state);
        } catch (IOException error) {
            throw new SaveGameException("The game could not be saved.", error);
        }
    }

    /** Loads binary data, falling back to the readable text save. */
    public GameState load(int slot) throws SaveGameException {
        validateSlot(slot);
        try {
            return loadBinary(slot);
        } catch (IOException | ClassNotFoundException binaryError) {
            try {
                return loadText(slot);
            } catch (IOException | RuntimeException textError) {
                throw new SaveGameException("Save slot " + slot
                        + " is missing or damaged.", textError);
            }
        }
    }

    private void saveBinary(int slot, GameState state) throws IOException {
        Path file = SAVE_DIRECTORY.resolve("slot" + slot + ".sav");
        try (ObjectOutputStream output = new ObjectOutputStream(
                Files.newOutputStream(file))) {
            output.writeObject(new GameSlot<>(slot, state));
        }
    }

    private GameState loadBinary(int slot) throws IOException, ClassNotFoundException {
        Path file = SAVE_DIRECTORY.resolve("slot" + slot + ".sav");
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(file))) {
            Object savedObject = input.readObject();
            if (!(savedObject instanceof GameSlot<?>)) {
                throw new IOException("Invalid save data.");
            }
            Object data = ((GameSlot<?>) savedObject).getData();
            if (!(data instanceof GameState)) {
                throw new IOException("Invalid game state.");
            }
            return (GameState) data;
        }
    }

    private void saveText(int slot, GameState state) throws IOException {
        Path file = SAVE_DIRECTORY.resolve("slot" + slot + ".txt");
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write("encounter=" + state.getEncounterIndex());
            writer.newLine();
            for (Hero hero : state.getParty()) {
                String equipment = hero.getEquippedItem() == null
                        ? "None" : hero.getEquippedItem().getClass().getSimpleName();
                writer.write("hero=" + hero.getClass().getSimpleName() + "|"
                        + hero.getHealth() + "|" + equipment + "|" + hero.getLevel());
                writer.newLine();
            }
            for (Item item : state.getInventory().getItems()) {
                writer.write("item=" + item.getClass().getSimpleName());
                writer.newLine();
            }
        }
    }

    private GameState loadText(int slot) throws IOException {
        Path file = SAVE_DIRECTORY.resolve("slot" + slot + ".txt");
        int encounter = 0;
        List<Hero> heroes = new ArrayList<>();
        Inventory inventory = new Inventory();

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("encounter=")) {
                    encounter = Integer.parseInt(line.substring(10));
                } else if (line.startsWith("hero=")) {
                    String[] values = line.substring(5).split("\\|");
                    Hero hero = createHero(values[0]);
                    if (values.length > 3) {
                        hero.restoreLevel(Integer.parseInt(values[3]));
                    }
                    if (values.length > 2 && values[2].equals("IronSword")) {
                        hero.equip(new IronSword());
                    }
                    hero.restoreHealth(Integer.parseInt(values[1]));
                    heroes.add(hero);
                } else if (line.startsWith("item=")) {
                    inventory.addItem(createItem(line.substring(5)));
                }
            }
        }

        if (heroes.isEmpty()) {
            throw new IOException("Save file has no party.");
        }
        GameState state = new GameState(heroes, inventory);
        state.setEncounterIndex(encounter);
        return state;
    }

    private Hero createHero(String className) throws IOException {
        switch (className) {
            case "Frieren":
                return new Frieren();
            case "Himmel":
                return new Himmel();
            case "Heiter":
                return new Heiter();
            case "Eisen":
                return new Eisen();
            default:
                throw new IOException("Unknown hero: " + className);
        }
    }

    private Item createItem(String className) throws IOException {
        switch (className) {
            case "HealthPotion":
                return new HealthPotion();
            case "IronSword":
                return new IronSword();
            case "AncientKey":
                return new AncientKey();
            default:
                throw new IOException("Unknown item: " + className);
        }
    }

    private void validateSlot(int slot) throws SaveGameException {
        if (slot < 1 || slot > SLOT_COUNT) {
            throw new SaveGameException("Choose a save slot from 1 to 3.", null);
        }
    }
}

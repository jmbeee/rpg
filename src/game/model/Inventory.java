package game.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** items shared by the party. */
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Item> items = new ArrayList<>();

    /** Adds an item to the shared inventory. */
    public void addItem(Item item) {
        items.add(item);
    }

    /** Removes an item and reports whether it was present. */
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    /** Returns a safe copy of the item list. */
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    /** Finds the first item matching a requested item class. */
    public <T extends Item> T findItem(Class<T> itemType) {
        for (Item item : items) {
            if (itemType.isInstance(item)) {
                return itemType.cast(item);
            }
        }
        return null;
    }
}

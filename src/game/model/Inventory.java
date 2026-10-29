package game.model;

import java.util.ArrayList;
import java.util.List;

/** items shared by the party. */
public class Inventory {

    private List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }
}

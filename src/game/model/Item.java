package game.model;

import java.io.Serializable;

/** base class for inventory items. */
public abstract class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String getDescription();
}

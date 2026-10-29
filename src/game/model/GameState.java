package game.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** complete campaign data used by the game and save files. */
public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Hero> party;
    private Inventory inventory;
    private int encounterIndex;

    public GameState(List<Hero> party, Inventory inventory) {
        this.party = new ArrayList<>(party);
        this.inventory = inventory;
    }

    /** Returns the selected party. */
    public List<Hero> getParty() {
        return party;
    }

    /** Returns the party's shared inventory. */
    public Inventory getInventory() {
        return inventory;
    }

    /** Returns the current encounter position. */
    public int getEncounterIndex() {
        return encounterIndex;
    }

    /** Restores the encounter position from a save. */
    public void setEncounterIndex(int encounterIndex) {
        this.encounterIndex = Math.max(0, encounterIndex);
    }

    /** Advances the campaign after a victory. */
    public void finishEncounter() {
        encounterIndex++;
    }

    /** Returns true after the final encounter. */
    public boolean isJourneyComplete() {
        return encounterIndex >= EncounterFactory.getEncounterCount();
    }
}

package game.model;

/** first enemy faced by the party. */
public class DemonScout extends Enemy {

    public DemonScout() {
        super("Demon Scout", 70, 16, 3, 11);
    }

    @Override
    public String useAbility() {
        return "Demon Scout uses sneak attack!";
    }
}

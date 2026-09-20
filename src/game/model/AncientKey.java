package game.model;

/** key item used to enter the demon castle. */
public class AncientKey extends Item {

    public AncientKey() {
        super("Ancient Key");
    }

    @Override
    public String getDescription() {
        return "Opens the Demon King's castle";
    }
}

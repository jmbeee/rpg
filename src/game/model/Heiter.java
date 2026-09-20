package game.model;

/** priest hero who supports the party. */
public class Heiter extends Hero {

    public Heiter() {
        super("Heiter", 115, 18, 7, 10);
    }

    @Override
    public String useAbility() {
        return "Heiter uses heal!";
    }
}

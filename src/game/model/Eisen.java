package game.model;

/** warrior hero with high defense. */
public class Eisen extends Hero {

    public Eisen() {
        super("Eisen", 140, 25, 10, 8);
    }

    @Override
    public String useAbility() {
        return "Eisen uses heavy strike!";
    }
}

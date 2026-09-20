package game.model;

/** mage hero with strong magic. */
public class Frieren extends Hero {

    public Frieren() {
        super("Frieren", 90, 28, 4, 12);
    }

    @Override
    public String useAbility() {
        return "Frieren uses offensive magic!";
    }
}

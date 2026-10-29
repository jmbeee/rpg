package game.model;

/** mage hero with strong magic. */
public class Frieren extends Hero {

    public Frieren() {
        super("Frieren", 90, 28, 4, 12);
    }

    @Override
    public String useAbility(GameCharacter target) {
        int damage = target.takeDamage(getAttack() + 10);
        return "Frieren used offensive magic for " + damage + " damage.";
    }
}

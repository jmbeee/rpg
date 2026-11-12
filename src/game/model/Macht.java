package game.model;

/** powerful golden demon enemy. */
public class Macht extends Enemy {

    public Macht() {
        super("Macht", 300, 39, 10, 14);
    }

    @Override
    public String useAbility(GameCharacter target) {
        int damage = target.takeDamage(getAttack() + 14);
        return "Macht used golden transmutation for " + damage + " damage.";
    }
}

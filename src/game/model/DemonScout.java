package game.model;

/** first enemy faced by the party. */
public class DemonScout extends Enemy {

    public DemonScout() {
        super("Demon Scout", 100, 19, 3, 11);
    }

    @Override
    public String useAbility(GameCharacter target) {
        int damage = target.takeDamage(getAttack() + 5);
        return "Demon Scout used sneak attack for " + damage + " damage.";
    }
}

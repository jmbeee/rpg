package game.model;

/** demon commander enemy. */
public class Aura extends Enemy {

    public Aura() {
        super("Aura", 165, 28, 7, 15);
    }

    @Override
    public String useAbility(GameCharacter target) {
        int damage = target.takeDamage(getAttack() + 11);
        return "Aura used domination magic for " + damage + " damage.";
    }
}

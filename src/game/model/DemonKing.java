package game.model;

/** final boss of the journey. */
public class DemonKing extends Enemy {

    public DemonKing() {
        super("Demon King", 300, 38, 13, 16);
    }

    @Override
    public String useAbility(GameCharacter target) {
        int damage = target.takeDamage(getAttack() + 18);
        return "Demon King used dark annihilation for " + damage + " damage.";
    }
}

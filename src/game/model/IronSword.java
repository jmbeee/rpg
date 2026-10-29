package game.model;

/** equipment that increases attack. */
public class IronSword extends Item {

    private static final int ATTACK_BONUS = 5;
    private int attackBonus;

    public IronSword() {
        super("Iron Sword");
        attackBonus = ATTACK_BONUS;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    @Override
    public String getDescription() {
        return "Adds 5 attack";
    }
}

package game.model;

/** equipment that increases attack. */
public class IronSword extends Item {

    private int attackBonus;

    public IronSword() {
        super("Iron Sword");
        attackBonus = 5;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    @Override
    public String getDescription() {
        return "Adds 5 attack";
    }
}

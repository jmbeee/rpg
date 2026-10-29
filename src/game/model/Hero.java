package game.model;

/** base class for heroes. */
public abstract class Hero extends GameCharacter {

    private Item equippedItem;

    public Hero(String name, int health, int attack, int defense, int speed) {
        super(name, health, attack, defense, speed);
    }

    public Item getEquippedItem() {
        return equippedItem;
    }

    public boolean equip(IronSword sword) {
        if (equippedItem != null) {
            return false;
        }
        equippedItem = sword;
        increaseAttack(sword.getAttackBonus());
        return true;
    }
}

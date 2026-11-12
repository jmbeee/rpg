package game.model;

/** armored demon enemy. */
public class DemonSoldier extends Enemy {

    public DemonSoldier() {
        super("Demon Soldier", 250, 27, 8, 9);
    }

    @Override
    public String useAbility(GameCharacter target) {
        int damage = target.takeDamage(getAttack() + 6);
        return "Demon Soldier used shield bash for " + damage + " damage.";
    }
}

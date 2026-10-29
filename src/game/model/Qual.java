package game.model;

/** demon mage enemy. */
public class Qual extends Enemy {

    public Qual() {
        super("Qual", 130, 25, 5, 13);
    }

    @Override
    public String useAbility(GameCharacter target) {
        int damage = target.takeDamage(getAttack() + 9);
        return "Qual used Zoltraak for " + damage + " damage.";
    }
}

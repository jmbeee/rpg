package game.model;

/** swordsman hero with fast attacks. */
public class Himmel extends Hero {

    public Himmel() {
        super("Himmel", 105, 23, 6, 18);
    }

    @Override
    public String useAbility(GameCharacter target) {
        int damage = attack(target) + attack(target);
        return "Himmel used double slash for " + damage + " damage.";
    }
}

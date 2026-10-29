package game.model;

/** priest hero who supports the party. */
public class Heiter extends Hero {

    public Heiter() {
        super("Heiter", 115, 18, 7, 10);
    }

    @Override
    public String useAbility(GameCharacter target) {
        int oldHealth = target.getHealth();
        target.heal(35);
        return "Heiter healed " + target.getName() + " for "
                + (target.getHealth() - oldHealth) + " HP.";
    }
}

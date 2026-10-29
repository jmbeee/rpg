package game.model;

/** potion that restores health. */
public class HealthPotion extends Item {

    private static final int HEALING_AMOUNT = 30;
    private int healingAmount;

    public HealthPotion() {
        super("Health Potion");
        healingAmount = HEALING_AMOUNT;
    }

    public int getHealingAmount() {
        return healingAmount;
    }

    public void useOn(GameCharacter target) {
        target.heal(healingAmount);
    }

    @Override
    public String getDescription() {
        return "Restores 30 HP";
    }
}

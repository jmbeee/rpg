package game.model;

/** potion that restores health. */
public class HealthPotion extends Item {

    private int healingAmount;

    public HealthPotion() {
        super("Health Potion");
        healingAmount = 30;
    }

    public int getHealingAmount() {
        return healingAmount;
    }

    @Override
    public String getDescription() {
        return "Restores 30 HP";
    }
}

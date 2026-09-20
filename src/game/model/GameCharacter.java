package game.model;

/** base class for game characters. */
public abstract class GameCharacter {

    private String name;
    private int health;
    private int attack;
    private int defense;
    private int speed;

    public GameCharacter(String name, int health, int attack, int defense, int speed) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void takeDamage(int damage) {
        int finalDamage = Math.max(1, damage - defense);
        health = Math.max(0, health - finalDamage);
    }
}

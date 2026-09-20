package game.model;

import java.util.Random;

/** base class for game characters. */
public abstract class GameCharacter {

    private static final Random RANDOM = new Random();
    private String name;
    private int health;
    private int maxHealth;
    private int attack;
    private int defense;
    private int speed;

    public GameCharacter(String name, int health, int attack, int defense, int speed) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
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

    public int getMaxHealth() {
        return maxHealth;
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

    public void takeDamage(int damage, boolean defending) {
        int reducedDamage = defending ? damage / 2 : damage;
        takeDamage(reducedDamage);
    }

    public void attack(GameCharacter target) {
        int randomDamage = attack + RANDOM.nextInt(5) - 2;
        target.takeDamage(randomDamage);
    }

    public abstract String useAbility();
}

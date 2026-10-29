package game.model;

import java.util.Random;

/** base class for game characters. */
public abstract class GameCharacter implements Attackable, Healable {

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

    public void increaseAttack(int amount) {
        if (amount > 0) {
            attack += amount;
        }
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

    @Override
    public void heal(int amount) {
        if (isAlive() && amount > 0) {
            health = Math.min(maxHealth, health + amount);
        }
    }

    public int takeDamage(int damage) {
        int oldHealth = health;
        int finalDamage = Math.max(1, damage - defense);
        health = Math.max(0, health - finalDamage);
        return oldHealth - health;
    }

    public int takeDamage(int damage, boolean defending) {
        int reducedDamage = defending ? damage / 2 : damage;
        return takeDamage(reducedDamage);
    }

    @Override
    public int attack(GameCharacter target) {
        int randomDamage = attack + RANDOM.nextInt(5) - 2;
        return target.takeDamage(randomDamage);
    }

    public int attack(GameCharacter target, boolean defending) {
        int randomDamage = attack + RANDOM.nextInt(5) - 2;
        return target.takeDamage(randomDamage, defending);
    }

    public abstract String useAbility();
}

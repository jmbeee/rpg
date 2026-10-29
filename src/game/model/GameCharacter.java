package game.model;

import java.io.Serializable;
import java.util.Random;

/** base class for game characters. */
public abstract class GameCharacter implements Attackable, Healable, Serializable {

    private static final long serialVersionUID = 1L;
    private static final int RANDOM_DAMAGE_RANGE = 5;
    private static final int RANDOM_DAMAGE_OFFSET = 2;

    private static final Random RANDOM = new Random();
    private String name;
    private int health;
    private int maxHealth;
    private int attack;
    private int defense;
    private int speed;
    private int level = 1;

    public GameCharacter(String name, int health, int attack, int defense, int speed) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
    }

    /** Returns the character name. */
    public String getName() {
        return name;
    }

    /** Returns current hit points. */
    public int getHealth() {
        return health;
    }

    /** Returns maximum hit points. */
    public int getMaxHealth() {
        return maxHealth;
    }

    /** Returns the current attack stat. */
    public int getAttack() {
        return attack;
    }

    /** Adds a positive amount to attack. */
    public void increaseAttack(int amount) {
        if (amount > 0) {
            attack += amount;
        }
    }

    /** Returns the defense stat. */
    public int getDefense() {
        return defense;
    }

    /** Returns the speed stat. */
    public int getSpeed() {
        return speed;
    }

    /** Returns the current level. */
    public int getLevel() {
        return level;
    }

    /** Raises the character level and base stats. */
    public void levelUp() {
        level++;
        maxHealth += 5;
        health = maxHealth;
        attack += 2;
        defense++;
    }

    /** Restores a level read from a text save. */
    public void restoreLevel(int savedLevel) {
        while (level < savedLevel) {
            levelUp();
        }
    }

    /** Returns true when the character has health remaining. */
    public boolean isAlive() {
        return health > 0;
    }

    /** Restores a saved health value within valid limits. */
    public void restoreHealth(int savedHealth) {
        health = Math.max(0, Math.min(maxHealth, savedHealth));
    }

    /** Restores health without passing maximum health. */
    @Override
    public void heal(int amount) {
        if (isAlive() && amount > 0) {
            health = Math.min(maxHealth, health + amount);
        }
    }

    /** Applies damage after defense and returns health lost. */
    public int takeDamage(int damage) {
        int oldHealth = health;
        int finalDamage = Math.max(1, damage - defense);
        health = Math.max(0, health - finalDamage);
        return oldHealth - health;
    }

    /** Applies damage with an optional defending reduction. */
    public int takeDamage(int damage, boolean defending) {
        int reducedDamage = defending ? damage / 2 : damage;
        return takeDamage(reducedDamage);
    }

    /** Performs a random normal attack and returns damage dealt. */
    @Override
    public int attack(GameCharacter target) {
        int randomDamage = attack + RANDOM.nextInt(RANDOM_DAMAGE_RANGE)
                - RANDOM_DAMAGE_OFFSET;
        return target.takeDamage(randomDamage);
    }

    /** Performs a random attack against a possibly defending target. */
    public int attack(GameCharacter target, boolean defending) {
        int randomDamage = attack + RANDOM.nextInt(RANDOM_DAMAGE_RANGE)
                - RANDOM_DAMAGE_OFFSET;
        return target.takeDamage(randomDamage, defending);
    }

    /** Uses this character's unique ability on a target. */
    public abstract String useAbility(GameCharacter target);
}

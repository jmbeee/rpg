package game.model;

/** base class for heroes. */
public abstract class Hero extends GameCharacter {

    private int level = 1;

    public Hero(String name, int health, int attack, int defense, int speed) {
        super(name, health, attack, defense, speed);
    }

    public int getLevel() {
        return level;
    }
}

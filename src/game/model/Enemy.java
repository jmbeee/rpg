package game.model;

/** base class for enemies. */
public abstract class Enemy extends GameCharacter {

    public Enemy(String name, int health, int attack, int defense, int speed) {
        super(name, health, attack, defense, speed);
    }
}

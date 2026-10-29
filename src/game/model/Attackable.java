package game.model;

/** character that can attack another character. */
public interface Attackable {

    void attack(GameCharacter target);
}

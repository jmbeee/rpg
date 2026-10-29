package game.model;

/** character that can attack another character. */
public interface Attackable {

    int attack(GameCharacter target);
}

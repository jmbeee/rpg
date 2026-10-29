package game.exceptions;

/** thrown when an action has no valid target. */
public class InvalidTargetException extends Exception {

    public InvalidTargetException(String message) {
        super(message);
    }
}

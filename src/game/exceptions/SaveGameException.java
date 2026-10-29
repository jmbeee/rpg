package game.exceptions;

/** thrown when saving or loading cannot be completed. */
public class SaveGameException extends Exception {

    public SaveGameException(String message, Throwable cause) {
        super(message, cause);
    }
}

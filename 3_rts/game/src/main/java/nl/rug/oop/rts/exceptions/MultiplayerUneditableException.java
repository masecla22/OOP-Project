package nl.rug.oop.rts.exceptions;

/**
 * This exception is thrown when a multiplayer map is edited. (It shouldn't be)
 */
public class MultiplayerUneditableException extends UnsupportedOperationException {
    /**
     * Creates a new exception.
     */
    public MultiplayerUneditableException() {
        super("Multiplayer maps cannot be edited!");
    }
}

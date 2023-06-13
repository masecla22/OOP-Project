package nl.rug.oop.rpg.game.behaviours;

/**
 * Allows objects to be copied.
 */
public interface Copyable {
    /**
     * Creates a copy of the current object.
     * @return - An exact copy of the current object
     */
    Object copy();
}

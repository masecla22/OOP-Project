package nl.rug.oop.rpg.game.save;

/**
 * An enum representing the different types of save state requests.
 * 
 * This is returned by the Game loop to the GameRunner to indicate what kind of
 * save state request the user has made.
 */
public enum SaveStateRequest {
    SAVE, LOAD, QUICKSAVE, QUICKLOAD
}

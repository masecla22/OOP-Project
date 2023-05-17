package nl.rug.oop.rpg.game.behaviours;

import nl.rug.oop.rpg.game.player.Player;

/**
 * Interface for interactable objects.
 * 
 */
public interface Interactable {
    /**
     * Interact as the player with this entity.
     * 
     * @param player - the player
     */
    void interact(Player player);
}

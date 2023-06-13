package nl.rug.oop.rpg.game.items;

import nl.rug.oop.rpg.game.player.Player;

/**
 * An item that can be consumed.
 */
public abstract class ConsumableItem extends Item {

    /**
     * Create a new consumable item.
     * 
     * @param name        the name of the item
     * @param description the description of the item
     */
    public ConsumableItem(String name, String description) {
        super(name, description);
    }

    /**
     * Consume the item.
     * 
     * @param player the player
     */
    public void conusme(Player player) {
        player.getInventory().removeItem(this);
        this.applyEffects(player);
    }

    /**
     * Apply the effects of the item to the player.
     * 
     * @param player the player
     */
    protected abstract void applyEffects(Player player);
}

package nl.rug.oop.rpg.game.items.consumables;

import nl.rug.oop.rpg.game.items.ConsumableItem;
import nl.rug.oop.rpg.game.player.Player;

/**
 * A block of cheese. Heals 100HP
 */
public class Cheese extends ConsumableItem {

    /**
     * Create a new cheese item.
     */
    public Cheese() {
        super("Cheese", "Just a regular block of cheese. Heals 100HP");
    }

    @Override
    protected void applyEffects(Player player) {
        player.setHealth(player.getHealth() + 100);
    }

}

package nl.rug.oop.rpg.game.items.consumables;

import nl.rug.oop.rpg.game.items.ConsumableItem;
import nl.rug.oop.rpg.game.player.Player;

/**
 * An apple. This item can be consumed to heal the player for 500HP.
 */
public class Apple extends ConsumableItem {

    /**
     * Constructor for an apple.
     */
    public Apple() {
        super("Apple", "Crisp and red. Heals 500HP");
    }

    @Override
    protected void applyEffects(Player player) {
        player.setHealth(player.getHealth() + 500);
    }

    @Override
    public Apple copy(){
        return new Apple();
    }
}

package nl.rug.oop.rpg.game.items;

import nl.rug.oop.rpg.game.player.Player;

public abstract class ConsumableItem extends Item {

    public ConsumableItem(String name, String description) {
        super(name, description);
    }

    protected void removeFromInventory(Player player) {

    }

    public abstract void consume(Player player);

}

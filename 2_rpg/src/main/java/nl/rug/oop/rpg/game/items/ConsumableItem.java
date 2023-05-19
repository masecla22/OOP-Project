package nl.rug.oop.rpg.game.items;

import nl.rug.oop.rpg.game.player.Player;

public abstract class ConsumableItem extends Item {

    public ConsumableItem(String name, String description) {
        super(name, description);
    }

    public void conusme(Player player) {
        player.getInventory().removeItem(this);
        this.applyEffects(player);
    }

    protected abstract void applyEffects(Player player);
}

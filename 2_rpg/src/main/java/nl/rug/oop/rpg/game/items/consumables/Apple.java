package nl.rug.oop.rpg.game.items.consumables;

import nl.rug.oop.rpg.game.items.ConsumableItem;
import nl.rug.oop.rpg.game.player.Player;

public class Apple extends ConsumableItem {

    public Apple() {
        super("Apple", "Crisp and red. Heals 500HP");
    }

    @Override
    protected void applyEffects(Player player) {
        player.setHealth(player.getHealth() + 500);
    }

}

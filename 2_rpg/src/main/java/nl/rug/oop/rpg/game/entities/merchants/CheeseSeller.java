package nl.rug.oop.rpg.game.entities.merchants;

import java.util.concurrent.ThreadLocalRandom;

import lombok.NonNull;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.entities.Merchant;
import nl.rug.oop.rpg.game.items.consumables.Cheese;
import nl.rug.oop.rpg.game.objects.Room;

/**
 * A cheese seller. This merchant will sell cheese to the player.
 */
public class CheeseSeller extends Merchant {

    /**
     * Constructor for a cheese seller.
     * This will add 10 cheeses to the inventory of the cheese seller for the player
     * to buy.
     * 
     * @param game - the game
     * @param room - the room the cheese seller is in
     */
    public CheeseSeller(@NonNull Game game, @NonNull Room room) {
        super(game, room, "He seems to have quite a bit of cheese. It's most likely stolen off the floor.",
                "Cheese seller");

        for (int i = 0; i < 10; i++) {
            addToInventory(new Cheese(), ThreadLocalRandom.current().nextInt(10, 31));
        }
    }
}

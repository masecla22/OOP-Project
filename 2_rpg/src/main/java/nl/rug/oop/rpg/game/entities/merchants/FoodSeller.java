package nl.rug.oop.rpg.game.entities.merchants;

import lombok.NonNull;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.entities.Merchant;
import nl.rug.oop.rpg.game.items.consumables.Apple;
import nl.rug.oop.rpg.game.items.consumables.Cheese;
import nl.rug.oop.rpg.game.objects.Room;

/**
 * A food seller. This merchant will sell food to the player.
 */
public class FoodSeller extends Merchant {

    /**
     * Constructor for a food seller.
     * This will add 3 apples and 1 cheese to the inventory of the food seller for
     * the player to buy.
     * 
     * @param game - the game
     * @param room - the room the food seller is in
     */
    public FoodSeller(@NonNull Game game, @NonNull Room room) {
        super(game, room, "A friendly looking fella with a lot of food on him.",
                "Food Seller");

        this.addToInventory(new Cheese(), 1);
        this.addToInventory(new Apple(), 1);
        this.addToInventory(new Apple(), 2);
        this.addToInventory(new Apple(), 3);
    }

}

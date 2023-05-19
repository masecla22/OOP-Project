package nl.rug.oop.rpg.game.entities.merchants;

import lombok.NonNull;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.entities.Merchant;
import nl.rug.oop.rpg.game.items.consumables.Apple;
import nl.rug.oop.rpg.game.items.consumables.Cheese;
import nl.rug.oop.rpg.game.objects.Room;

public class FoodSeller extends Merchant {

    public FoodSeller(@NonNull Game game, @NonNull Room room) {
        super(game, room, "A friendly looking fella with a lot of food on him.",
                "Food Seller");

        this.addToInventory(new Cheese(), 1);
        this.addToInventory(new Apple(), 1);
        this.addToInventory(new Apple(), 2);
        this.addToInventory(new Apple(), 3);
    }

}

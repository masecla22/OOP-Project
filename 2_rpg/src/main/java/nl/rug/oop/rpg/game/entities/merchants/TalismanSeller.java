package nl.rug.oop.rpg.game.entities.merchants;

import lombok.NonNull;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.entities.Merchant;
import nl.rug.oop.rpg.game.items.modifiers.SimpleTalisman;
import nl.rug.oop.rpg.game.items.modifiers.WarriorTalisman;
import nl.rug.oop.rpg.game.objects.Room;

/**
 * A merchant that sells talismans.
 */
public class TalismanSeller extends Merchant {

    /**
     * Create a new talisman seller.
     * The talisman seller will sell a simple talisman and a warrior talisman.
     * 
     * @param game the game
     * @param room the room the talisman seller is in
     */
    public TalismanSeller(@NonNull Game game, @NonNull Room room) {
        super(game, room, "A shady looking man with a couple talismans hanging around his neck",
                "Talisman Seller");

        this.addToInventory(new SimpleTalisman(), 50);
        this.addToInventory(new WarriorTalisman(), 200);
    }

    @Override
    public TalismanSeller copy() {
        TalismanSeller result = new TalismanSeller(this.getGame(), this.getRoom());
        result.setSellableItems(this.copyInventory());

        return result;
    }
}

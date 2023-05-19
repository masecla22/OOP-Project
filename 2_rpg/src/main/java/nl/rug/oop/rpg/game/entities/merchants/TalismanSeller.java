package nl.rug.oop.rpg.game.entities.merchants;

import lombok.NonNull;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.entities.Merchant;
import nl.rug.oop.rpg.game.items.modifiers.SimpleTalisman;
import nl.rug.oop.rpg.game.items.modifiers.WarriorTalisman;
import nl.rug.oop.rpg.game.objects.Room;

public class TalismanSeller extends Merchant {

    public TalismanSeller(@NonNull Game game, @NonNull Room room) {
        super(game, room, "A shady looking man with a couple talismans hanging around his neck",
                "Talisman Seller");

        this.addToInventory(new SimpleTalisman(), 50);
        this.addToInventory(new WarriorTalisman(), 200);
    }
}

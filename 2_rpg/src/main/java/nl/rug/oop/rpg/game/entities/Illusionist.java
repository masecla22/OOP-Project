package nl.rug.oop.rpg.game.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.objects.Room;
import nl.rug.oop.rpg.game.player.Player;

/**
 * Upon interaction, the illusionist clones every NPC in the room.
 */
public class Illusionist extends NPC {

    /**
     * Create Illusionist.
     *
     * @param game the game
     * @param room the room the illusionist is in
     */
    public Illusionist(@NonNull Game game, @NonNull Room room) {
        super(game, room,
                "He will clone every NPC in the room, should you choose to interact with him.",
                "Illusionist");
    }

    @Override
    public void inspect() {
        System.out.println("He will even clone all characteristics!");
    }

    @Override
    public void interact(Player player) {
        System.out.println("Poof! Now all NPCs are cloned, have fun!");
        List<NPC> toAdd = new ArrayList<NPC>();
        for (NPC current : this.getRoom().getNpcs()) {
            if (current != this) {
                toAdd.add(current.copy());
            }
        }

        this.getRoom().getNpcs().addAll(toAdd);
    }

    @Override
    public Illusionist copy() {
        return new Illusionist(this.getGame(), this.getRoom());
    }
}

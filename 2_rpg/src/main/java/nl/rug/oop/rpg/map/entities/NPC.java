package nl.rug.oop.rpg.map.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.map.behaviours.Inspectable;
import nl.rug.oop.rpg.map.behaviours.Interactable;
import nl.rug.oop.rpg.map.objects.Room;
import nl.rug.oop.rpg.player.Player;

/**
 * An NPC is a non-playable character that can be interacted with.
 */
@Data
@AllArgsConstructor
public class NPC implements Inspectable, Interactable {
    /** The game the NPC is a part of. */
    @NonNull
    private Game game;
    
    /** The room the NPC is in. */
    @NonNull
    private Room room;
    
    /** The description of the NPC. */
    @NonNull
    private String description;

    /**
     * Inspects the NPC.
     */
    @Override
    public void inspect() {
        System.out.println(description);
    }

    /**
     * Interacts with the NPC.
     * @param player - the player
     */
    @Override
    public void interact(Player player) {
        System.out.println("Watch where you walk! You almost bumped into me!");
    }
}

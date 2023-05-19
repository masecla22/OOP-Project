package nl.rug.oop.rpg.game.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import nl.rug.oop.rpg.game.behaviours.Inspectable;
import nl.rug.oop.rpg.game.behaviours.Interactable;
import nl.rug.oop.rpg.game.player.Player;

/**
 * A door is an object that can be interacted with and inspected.
 */
@Data
@AllArgsConstructor
public class Door implements Inspectable, Interactable, Serializable {
    /** Serial version ID. */
    private static final long serialVersionUID = 2140956097087l;

    /** The description of the door. */
    @NonNull
    private String description;

    /** The room the door comes from */
    private Room source;

    /** The room the door leads to. */
    private Room destination;

    /**
     * Inspects the door.
     */
    @Override
    public void inspect() {
        System.out.println(description);
    }

    /**
     * Interacts with the door.
     * 
     * @param player - the player
     */
    @Override
    public void interact(Player player) {
        if (destination == null) {
            System.out.println("The door leads into a solid brick wall.");
            System.out.println("You confidently bump your head against it and bounce back where you were. (-0.1 HP)");
            player.receiveDamage(null, 0.1);
            return;
        }

        player.setCurrentlyIn(destination);
        System.out.println("You walk through the door and find yourself in a new room.");
    }
}

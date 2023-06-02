package nl.rug.oop.rpg.game.objects;

import java.io.Serializable;

import lombok.Data;
import nl.rug.oop.rpg.game.behaviours.Inspectable;
import nl.rug.oop.rpg.game.behaviours.Interactable;
import nl.rug.oop.rpg.game.player.Player;

/**
 * A door is an object that can be interacted with and inspected.
 */
@Data
public class Door implements Inspectable, Interactable, Serializable {
    /** Serial version ID. */
    private static final long serialVersionUID = 2140956097087l;

    /** The description of the door. */
    private String description;

    /** The room the door comes from. */
    private Room source;

    /** The room the door leads to. */
    private Room destination;

    /**
     * Creates a new door. The constructor will also add the door to the source
     * room.
     * 
     * @param description - the description of the door
     * @param source      - the room the door comes from
     * @param destination - the room the door leads to
     */
    public Door(String description, Room source, Room destination) {
        this.description = description;
        this.source = source;
        this.destination = destination;

        source.addDoor(this);
    }

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

package nl.rug.oop.rpg.map.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rpg.map.behaviours.Inspectable;
import nl.rug.oop.rpg.map.behaviours.Interactable;
import nl.rug.oop.rpg.player.Player;

/**
 * A door is an object that can be interacted with and inspected.
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Door implements Inspectable, Interactable {
    /** The description of the door. */
    @NonNull
    private String description;

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
        if(destination == null){
            System.out.println("The door leads into a solid brick wall.");
            System.out.println("You confidently bump your head against it and bounce back where you were.");
            return ;
        }

        player.setCurrentlyIn(destination);
        System.out.println("You walk through the door and find yourself in a new room.");
    }
}

package nl.rug.oop.rpg.map.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rpg.map.behaviours.Inspectable;
import nl.rug.oop.rpg.map.behaviours.Interactable;
import nl.rug.oop.rpg.player.Player;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Door implements Inspectable, Interactable {
    @NonNull
    private String description;

    private Room destination;
    
    @Override
    public void inspect() {
        System.out.println(description);
    }

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

package nl.rug.oop.rpg.map.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.map.behaviours.Inspectable;
import nl.rug.oop.rpg.map.behaviours.Interactable;
import nl.rug.oop.rpg.map.objects.Room;
import nl.rug.oop.rpg.player.Player;

@Data
@AllArgsConstructor
public class NPC implements Inspectable, Interactable {
    @NonNull
    private Game game;
    
    @NonNull
    private Room room;
    
    @NonNull
    private String description;


    @Override
    public void inspect() {
        System.out.println(description);
    }

    @Override
    public void interact(Player player) {
        System.out.println("Watch where you walk! You almost bumped into me!");
    }
}

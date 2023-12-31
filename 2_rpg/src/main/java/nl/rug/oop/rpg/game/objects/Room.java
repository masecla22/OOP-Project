package nl.rug.oop.rpg.game.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rpg.game.behaviours.Inspectable;
import nl.rug.oop.rpg.game.entities.NPC;

/**
 * A room is a place in the map that can be entered and exited.
 */
@Data
@RequiredArgsConstructor
public class Room implements Inspectable, Serializable {
    /** Serial version ID. */
    private static final long serialVersionUID = 92875163129876l;

    /** The description of the room. */
    @NonNull
    private String description;

    /** The doors in the room. */
    private List<Door> doors = new ArrayList<>();

    /** The NPCs in the room. */
    private List<NPC> npcs = new ArrayList<>();

    /**
     * Inspects the room.
     */
    @Override
    public void inspect() {
        System.out.print(description);
        System.out.println(" The room has " + doors.size() + " doors and "
                + npcs.size() + " NPCs.");
    }

    /**
     * Adds a door to the room.
     * 
     * @param door - the door to add
     */
    public void addDoor(Door door) {
        doors.add(door);
    }

    /**
     * Adds multiple doors to the room.
     * 
     * @param doors - the doors to add
     */
    public void addDoors(Door... doors) {
        for (Door door : doors) {
            addDoor(door);
        }
    }

    /**
     * Adds an NPC to the room.
     * 
     * @param npc - the NPC to add
     */
    public void addNPC(NPC npc) {
        npcs.add(npc);
    }

    /**
     * Removes an NPC from the room.
     * 
     * @param npc - the NPC to remove
     */
    public void removeNPC(NPC npc) {
        npcs.remove(npc);
    }
}

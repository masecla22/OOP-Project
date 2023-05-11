package nl.rug.oop.rpg.map.objects;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rpg.map.behaviours.Inspectable;
import nl.rug.oop.rpg.map.entities.NPC;

@Data
@RequiredArgsConstructor
public class Room implements Inspectable {
    @NonNull
    private String description;

    private List<Door> doors = new ArrayList<>();
    private List<NPC> npcs = new ArrayList<>();

    @Override
    public void inspect() {
        System.out.print(description);
        System.out.println(" The room has " + doors.size() + " doors and "
                + npcs.size() + " NPCs.");
    }

    public void addDoor(Door door) {
        doors.add(door);
    }

    public void addNPC(NPC npc) {
        npcs.add(npc);
    }

    public void removeNPC(NPC npc){
        npcs.remove(npc);
    }
}

package nl.rug.oop.rpg.game.entities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.behaviours.Copyable;
import nl.rug.oop.rpg.game.behaviours.Inspectable;
import nl.rug.oop.rpg.game.behaviours.Interactable;
import nl.rug.oop.rpg.game.objects.Room;

/**
 * An NPC is a non-playable character that can be interacted with.
 */
@Data
@AllArgsConstructor
public abstract class NPC implements Inspectable, Interactable, Serializable, Copyable {
    /** Serial version ID. */
    private static final long serialVersionUID = 1398649861938l;

    /** The game the NPC is a part of. */
    @NonNull
    private Game game;

    /** The room the NPC is in. */
    @NonNull
    private Room room;

    /** The description of the NPC. */
    @NonNull
    private String description;

    /** The name of the NPC. */
    @NonNull
    private String name;

    @Override
    public abstract NPC copy();
}

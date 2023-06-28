package nl.rug.oop.rpg.game.items;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.rug.oop.rpg.game.behaviours.Copyable;

/**
 * An item that can be picked up and used.
 */
@Data
@AllArgsConstructor
public abstract class Item implements Serializable, Copyable {
    /** Serial version ID. */
    private static final long serialVersionUID = 2145629086987l;

    private String name;
    private String description;

    @Override
    public abstract Item copy();
}

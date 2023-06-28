package nl.rug.oop.rts.protocol.objects.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nl.rug.oop.rts.protocol.objects.interfaces.Selectable;
import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.events.Event;

/**
 * This class represents a node in the map.
 */
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@ToString(of = { "id", "name" })
public class Node implements Selectable {
    /** The size of a node. */
    public static final int NODE_SIZE = 64;

    private int id;

    private Point position;
    private String name;

    private transient List<Edge> edges = new ArrayList<>();

    private List<Army> armies = new ArrayList<>();
    private List<Event> events = new ArrayList<>();

    /**
     * Creates a new node.
     * 
     * @param id       The id of the node.
     * @param position The position of the node.
     * @param name     The name of the node.
     */
    public Node(int id, Point position, String name) {
        this.id = id;
        this.position = position;
        this.name = name;
    }

    /**
     * Adds an army to the node.
     * 
     * @param army - The army to add.
     */
    public void addArmy(Army army) {
        this.armies.add(army);
    }

    /**
     * Removes an army from the node.
     * 
     * @param army - The army to remove.
     */
    public void removeArmy(Army army) {
        this.armies.remove(army);
    }

    /**
     * Adds an event to the node.
     * 
     * @param event - The event to add.
     */
    public void addEvent(Event event) {
        this.events.add(event);
    }

    /**
     * Removes an event from the node.
     * 
     * @param event - The event to remove.
     */
    public void removeEvent(Event event) {
        this.events.remove(event);
    }
}

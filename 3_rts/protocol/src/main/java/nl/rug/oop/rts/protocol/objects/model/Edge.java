package nl.rug.oop.rts.protocol.objects.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.objects.interfaces.Selectable;
import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.events.Event;

/**
 * An edge is a connection between two nodes.
 */
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Edge implements Selectable {
    private int id;
    private Node pointA;
    private Node pointB;

    private List<Army> armies = new ArrayList<>();
    private List<Event> events = new ArrayList<>();

    /**
     * Create a new edge.
     * 
     * @param id     - the id
     * @param pointA - the first node
     * @param pointB - the second node
     */
    public Edge(int id, Node pointA, Node pointB) {
        this.id = id;
        this.pointA = pointA;
        this.pointB = pointB;
    }

    /**
     * Check if this edge is connected to a node.
     * 
     * @param node - the node
     * @return - true if connected, false otherwise
     */
    public boolean isConnectedTo(Node node) {
        return pointA.equals(node) || pointB.equals(node);
    }

    /**
     * Add an army to this edge.
     * 
     * @param army - the army
     */
    public void addArmy(Army army) {
        armies.add(army);
    }

    /**
     * Remove an army from this edge.
     * 
     * @param army - the army
     */
    public void removeArmy(Army army) {
        armies.remove(army);
    }

    /**
     * Add an event to this edge.
     * 
     * @param event - the event
     */
    public void addEvent(Event event) {
        events.add(event);
    }

    /**
     * Remove an event from this edge.
     * 
     * @param event - the event
     */
    public void removeEvent(Event event) {
        events.remove(event);
    }

    /**
     * Get the other node connected to this edge.
     * 
     * @param node - the node
     * @return - the other node
     */
    public Node getOtherNode(Node node) {
        if (pointA.equals(node)) {
            return pointB;
        } else if (pointB.equals(node)) {
            return pointA;
        } else {
            throw new IllegalArgumentException("Node is not connected to this edge");
        }
    }
}

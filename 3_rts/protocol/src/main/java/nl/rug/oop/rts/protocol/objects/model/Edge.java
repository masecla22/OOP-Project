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

@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Edge implements Selectable {
    private int id;
    private Node pointA;
    private Node pointB;

    public Edge(int id, Node pointA, Node pointB) {
        this.id = id;
        this.pointA = pointA;
        this.pointB = pointB;
    }

    private List<Army> armies = new ArrayList<>();
    private List<Event> events = new ArrayList<>();

    public boolean isConnectedTo(Node node) {
        return pointA.equals(node) || pointB.equals(node);
    }

    public void addArmy(Army army) {
        armies.add(army);
    }

    public void removeArmy(Army army) {
        armies.remove(army);
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void removeEvent(Event event) {
        events.remove(event);
    }

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

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

@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@ToString(of = { "id", "name" })
public class Node implements Selectable {
    public static final int NODE_SIZE = 64;

    private int id;

    private Point position;
    private String name;

    public Node(int id, Point position, String name) {
        this.id = id;
        this.position = position;
        this.name = name;
    }

    private transient List<Edge> edges = new ArrayList<>();

    private List<Army> armies = new ArrayList<>();
    private List<Event> events = new ArrayList<>();

    public void addArmy(Army army) {
        this.armies.add(army);
    }

    public void removeArmy(Army army) {
        this.armies.remove(army);
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    public void removeEvent(Event event) {
        this.events.remove(event);
    }
}

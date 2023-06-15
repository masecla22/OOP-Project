package nl.rug.oop.rts.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import nl.rug.oop.rts.interfaces.Selectable;
import nl.rug.oop.rts.model.armies.Army;
import nl.rug.oop.rts.model.events.Event;

@Data
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
@ToString(of = { "id", "name" })
public class Node implements Selectable {
    public static final int NODE_SIZE = 64;

    private final int id;

    @NonNull
    private Point position;

    @NonNull
    private String name;

    private List<Edge> edges = new ArrayList<>();

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

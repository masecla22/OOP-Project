package nl.rug.oop.rts.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import nl.rug.oop.rts.interfaces.Selectable;
import nl.rug.oop.rts.model.armies.Army;

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

    private Set<Edge> edges = new HashSet<>();

    private List<Army> armies = new ArrayList<>();

    public void addArmy(Army army) {
        this.armies.add(army);
    }

    public void removeArmy(Army army) {
        this.armies.remove(army);
    }
}

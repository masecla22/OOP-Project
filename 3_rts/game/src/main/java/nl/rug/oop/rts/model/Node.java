package nl.rug.oop.rts.model;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import nl.rug.oop.rts.interfaces.Selectable;
import nl.rug.oop.rts.model.units.Unit;

@Data
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
@ToString(of = {"id", "name"})
public class Node implements Selectable {
    public static final int NODE_SIZE = 64;

    private final int id;

    @NonNull
    private Point position;

    @NonNull
    private String name;

    private Set<Edge> edges = new HashSet<>();

    private Set<Unit> units = new HashSet<>();
}

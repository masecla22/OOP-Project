package nl.rug.oop.rts.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rts.interfaces.Selectable;

@Data
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
public class Edge implements Selectable {
    private final int id;

    @NonNull
    private Node pointA;

    @NonNull
    private Node pointB;

    public boolean isConnectedTo(Node node) {
        return pointA.equals(node) || pointB.equals(node);
    }
}

package nl.rug.oop.rts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
public class Edge {
    private final int id;

    @NonNull
    private Node pointA;

    @NonNull
    private Node pointB;

    public boolean isConnectedTo(Node node) {
        return pointA.equals(node) || pointB.equals(node);
    }
}

package nl.rug.oop.rts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class Edge {
    private int id;

    @NonNull
    private Node pointA;

    @NonNull
    private Node pointB;

    public boolean isConnectedTo(Node node) {
        return pointA.equals(node) || pointB.equals(node);
    }
}

package nl.rug.oop.rts.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

@Getter
public class Map {
    private Set<Node> nodes = new HashSet<>();
    private Set<Edge> edges = new HashSet<>();

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void removeNode(Node node) {
        nodes.remove(node);

        for (Edge edge : edges) {
            if (edge.isConnectedTo(node)) {
                edges.remove(edge);
            }
        }
    }

    public void addEdge(Edge edge) {
        edges.add(edge);

        edge.getPointA().getEdges().add(edge);
        edge.getPointB().getEdges().add(edge);
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }
}

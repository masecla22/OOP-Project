package nl.rug.oop.rts.model;

import java.util.Set;

public class Map {
    private Set<Node> nodes;
    private Set<Edge> edges;

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
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }
}

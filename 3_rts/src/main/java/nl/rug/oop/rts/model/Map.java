package nl.rug.oop.rts.model;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import nl.rug.oop.rts.observing.Observable;
import nl.rug.oop.rts.observing.Observer;

@Getter
public class Map implements Observable {
    private Point offset = new Point(0, 0);

    private Set<Node> nodes = new HashSet<>();
    private Set<Edge> edges = new HashSet<>();

    private Set<Observer> observers = new HashSet<>();
    private Node selectedNode = null;

    public void addNode(Node node) {
        nodes.add(node);
        this.update();
    }

    public void removeNode(Node node) {
        nodes.remove(node);

        edges.removeIf(edge -> edge.isConnectedTo(node));
        this.update();
    }

    public void setSelectedNode(Node selectedNode) {
        this.selectedNode = selectedNode;
        this.update();
    }

    public void setOffset(Point offset) {
        this.offset = offset;
        this.update();
    }

    public void addEdge(Edge edge) {
        edges.add(edge);

        edge.getPointA().getEdges().add(edge);
        edge.getPointB().getEdges().add(edge);

        this.update();
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);

        this.update();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public Point transformPoint(Point point) {
        Point offset = this.getOffset();
        return new Point((int) (point.x + offset.x), (int) (point.y + offset.y));
    }

    public Node getNodeAt(int x, int y) {
        for (Node node : this.getNodes()) {
            Point position = node.getPosition();
            position = transformPoint(position);

            if (x >= position.x - Node.NODE_SIZE / 2 && x <= position.x + Node.NODE_SIZE / 2
                    && y >= position.y - Node.NODE_SIZE / 2 && y <= position.y + Node.NODE_SIZE / 2) {
                return node;
            }
        }

        return null;
    }

    public int getNextNodeId() {
        int maxId = 0;

        for (Node node : this.getNodes()) {
            if (node.getId() > maxId) {
                maxId = node.getId();
            }
        }

        return maxId + 1;
    }

    public int getNextEdgeId() {
        int maxId = 0;

        for (Edge edge : this.getEdges()) {
            if (edge.getId() > maxId) {
                maxId = edge.getId();
            }
        }

        return maxId + 1;
    }
}

package nl.rug.oop.rts.model;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import lombok.Getter;
import nl.rug.oop.rts.interfaces.Selectable;
import nl.rug.oop.rts.interfaces.observing.Observable;
import nl.rug.oop.rts.interfaces.observing.Observer;

@Getter
public class Map implements Observable {
    private Point offset = new Point(0, 0);

    private Set<Node> nodes = new HashSet<>();
    private Set<Edge> edges = new HashSet<>();

    private Set<Observer> observers = new HashSet<>();

    private Selectable selection;

    private CompletableFuture<Node> addingEdge = null;

    public void addNode(Node node) {
        nodes.add(node);
        this.update();
    }

    public void removeNode(Node node) {
        nodes.remove(node);

        edges.removeIf(edge -> edge.isConnectedTo(node));
        this.update();
    }

    public void setSelection(Selectable selection) {
        this.selection = selection;
        if (this.selection == null)
            this.addingEdge = null;

        this.update();
    }

    public boolean isAddingEdge() {
        return this.addingEdge != null;
    }

    public void setOffset(Point offset) {
        this.offset = offset;
        this.update();
    }

    public void markAddingEdge() {
        this.addingEdge = new CompletableFuture<>();
        this.update();
    }

    public void unmarkAddingEdge() {
        this.addingEdge = null;
        this.update();
    }

    public void addEdge(Edge edge) {
        // Make sure no identical edges are added
        for (Edge existingEdge : edges) {
            if (existingEdge.getPointA().equals(edge.getPointA())
                    && existingEdge.getPointB().equals(edge.getPointB())) {
                return;
            }
        }

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

    public Point addOffset(Point point) {
        Point offset = this.getOffset();
        return new Point((int) (point.x + offset.x), (int) (point.y + offset.y));
    }

    public Point subtractOffset(Point point) {
        Point offset = this.getOffset();
        return new Point((int) (point.x - offset.x), (int) (point.y - offset.y));
    }

    public Selectable getSelectableAt(int x, int y) {
        // First check the nodes
        for (Node node : this.getNodes()) {
            Point position = node.getPosition();
            position = addOffset(position);

            if (x >= position.x - Node.NODE_SIZE / 2 && x <= position.x + Node.NODE_SIZE / 2
                    && y >= position.y - Node.NODE_SIZE / 2 && y <= position.y + Node.NODE_SIZE / 2) {
                return node;
            }
        }

        for (Edge edge : this.getEdges()) {
            Point pointA = edge.getPointA().getPosition();
            Point pointB = edge.getPointB().getPosition();

            pointA = addOffset(pointA);
            pointB = addOffset(pointB);

            int middleX = (pointA.x + pointB.x) / 2;
            int middleY = (pointA.y + pointB.y) / 2;

            if (x >= middleX - 10 && x <= middleX + 10 && y >= middleY - 10 && y <= middleY + 10) {
                return edge;
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

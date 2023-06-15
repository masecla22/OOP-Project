package nl.rug.oop.rts.protocol.objects.model;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import nl.rug.oop.rts.protocol.objects.interfaces.Selectable;
import nl.rug.oop.rts.protocol.objects.interfaces.observing.Observable;
import nl.rug.oop.rts.protocol.objects.interfaces.observing.Observer;

@Getter
@Setter
public class Map implements Observable {
    private Point offset = new Point(0, 0);

    private Set<Node> nodes = new HashSet<>();
    private Set<Edge> edges = new HashSet<>();

    private Set<Observer> observers = new HashSet<>();

    private Selectable selection;

    public void addNode(Node node) {
        nodes.add(node);
        this.update();
    }

    public void removeNode(Node node) {
        nodes.remove(node);

        edges.removeIf(edge -> {
            if (edge.isConnectedTo(node)) {
                edge.getPointA().getEdges().remove(edge);
                edge.getPointB().getEdges().remove(edge);
                return true;
            } else {
                return false;
            }
        });
        this.update();
    }

    public void setSelection(Selectable selection) {
        this.selection = selection;
        this.update();
    }

    public void setOffset(Point offset) {
        this.offset = offset;
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
        edge.getPointA().getEdges().remove(edge);
        edge.getPointB().getEdges().remove(edge);

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

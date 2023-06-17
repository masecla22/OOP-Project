package nl.rug.oop.rts.protocol.objects.model;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import nl.rug.oop.rts.protocol.objects.interfaces.Selectable;
import nl.rug.oop.rts.protocol.objects.interfaces.observing.Observable;
import nl.rug.oop.rts.protocol.objects.interfaces.observing.Observer;

/**
 * This class represents the map of the game.
 */
@Data
public class Map implements Observable {
    private Point offset = new Point(0, 0);

    private Set<Node> nodes = new HashSet<>();
    private Set<Edge> edges = new HashSet<>();

    private transient Set<Observer> observers = new HashSet<>();

    private transient Selectable selection;

    /**
     * Adds a node to the map.
     * 
     * @param node - The node to add.
     */
    public void addNode(Node node) {
        nodes.add(node);
        this.update();
    }

    /**
     * Removes a node from the map.
     * 
     * @param node - The node to remove.
     */
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

    /**
     * Sets the selection.
     * 
     * @param selection - The selection.
     */
    public void setSelection(Selectable selection) {
        this.selection = selection;
        this.update();
    }

    /**
     * Sets the offset.
     * 
     * @param offset - The offset.
     */
    public void setOffset(Point offset) {
        this.offset = offset;
        this.update();
    }

    /**
     * Adds an edge to the map.
     * 
     * @param edge - The edge to add.
     */
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

    /**
     * Removes an edge from the map.
     * 
     * @param edge - The edge to remove.
     */
    public void removeEdge(Edge edge) {
        edge.getPointA().getEdges().remove(edge);
        edge.getPointB().getEdges().remove(edge);

        edges.remove(edge);

        this.update();
    }

    @Override
    public void addObserver(Observer observer) {
        if (observers == null)
            observers = new HashSet<>();
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Adds the offset to a point.
     *
     * @param point - The point to update.
     * @return - The updated point.
     */
    public Point addOffset(Point point) {
        Point offset = this.getOffset();
        return new Point((int) (point.x + offset.x), (int) (point.y + offset.y));
    }

    /**
     * Subtracts the offset from a point.
     *
     * @param point - The point to update.
     * @return - The updated point.
     */
    public Point subtractOffset(Point point) {
        Point offset = this.getOffset();
        return new Point((int) (point.x - offset.x), (int) (point.y - offset.y));
    }

    /**
     * Gets the selectable at a certain position.
     * 
     * @param x - The x position.
     * @param y - The y position.
     * @return - The selectable at the position.
     */
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

    /**
     * Gets the next node id.
     * 
     * @return - The next node id.
     */
    public int getNextNodeId() {
        int maxId = 0;

        for (Node node : this.getNodes()) {
            if (node.getId() > maxId) {
                maxId = node.getId();
            }
        }

        return maxId + 1;
    }

    /**
     * Gets the next edge id.
     * 
     * @return - The next edge id.
     */
    public int getNextEdgeId() {
        int maxId = 0;

        for (Edge edge : this.getEdges()) {
            if (edge.getId() > maxId) {
                maxId = edge.getId();
            }
        }

        return maxId + 1;
    }

    /**
     * Removes all armies from the map
     */
    public void removeAllArmies() {
        for (Node node : this.getNodes()) {
            node.getArmies().clear();
        }

        for (Edge edge : this.getEdges()) {
            edge.getArmies().clear();
        }

        this.update();
    }
}

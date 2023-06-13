package nl.rug.oop.rts.controller.map;

import java.awt.Point;
import java.util.concurrent.CompletableFuture;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rts.interfaces.Selectable;
import nl.rug.oop.rts.model.Edge;
import nl.rug.oop.rts.model.Map;
import nl.rug.oop.rts.model.Node;

@RequiredArgsConstructor
public abstract class MapController {
    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private Map map;

    @Getter
    private CompletableFuture<Node> addingEdge = null;

    public abstract void removeNode(Node node);

    public abstract Node createNode(String nodeName);

    public abstract void addEdge(Node node1, Node node2);

    public abstract void removeEdge(Edge edge);

    public abstract void runSimulationStep();

    public void setSelection(Selectable selection) {
        if (selection == null) {
            this.addingEdge = null;
        }
        this.map.setSelection(selection);
    }

    public boolean isAddingEdge() {
        return this.addingEdge != null;
    }

    public void markAddingEdge() {
        this.addingEdge = new CompletableFuture<>();
        map.update();
    }

    public void unmarkAddingEdge() {
        this.addingEdge = null;
        map.update();
    }

    public void setOffset(Point offset) {
        map.setOffset(offset);
    }

}

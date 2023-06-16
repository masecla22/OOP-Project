package nl.rug.oop.rts.controller.map;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import nl.rug.oop.rts.protocol.objects.interfaces.Selectable;
import nl.rug.oop.rts.protocol.objects.model.Edge;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.objects.model.Node;
import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.armies.Faction;
import nl.rug.oop.rts.protocol.objects.model.events.Event;
import nl.rug.oop.rts.protocol.objects.model.events.EventType;
import nl.rug.oop.rts.view.map.MapView;
import nl.rug.oop.rugson.Rugson;

public abstract class MapController {
    private Rugson rugson;

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private Map map;

    public MapController(Rugson rugson, @NonNull Map map) {
        this.rugson = rugson;
        this.map = map;
    }

    @Getter
    private CompletableFuture<Node> addingEdge = null;

    public abstract void removeNode(Node node);

    public abstract Node createNode(String nodeName);

    public abstract void addEdge(Node node1, Node node2);

    public abstract void removeEdge(Edge edge);

    public abstract void addArmy(Node node, Faction faction);

    public abstract void removeArmy(Node node, Army army);

    public abstract void addEvent(Node node, EventType type);

    public abstract void removeEvent(Node node, Event event);

    public abstract void addEvent(Edge edge, EventType type);

    public abstract void removeEvent(Edge edge, Event event);

    public abstract void setNodePosition(Node node, Point position);

    public abstract Color getColorForNode(Node node);

    public abstract boolean allowEventModification();

    public abstract boolean allowNodeRenaming();

    public abstract String canPlaceArmy(Node node);

    public abstract Set<Faction> getAllowedFactions();

    public abstract boolean showUnitCost();

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
        // Bind offset between 0
        if (offset.x > 0)
            offset.x = 0;
        if (offset.y > 0)
            offset.y = 0;
        if (offset.x < -MapView.MAP_SIZE + 600)
            offset.x = -MapView.MAP_SIZE + 600;
        if (offset.y < -MapView.MAP_SIZE + 600)
            offset.y = -MapView.MAP_SIZE + 600;

        map.setOffset(offset);
    }

    public void exportToJson(File file) throws IOException {
        if (!file.getName().endsWith(".json")) {
            file = new File(file.getAbsolutePath() + ".json");
        }

        String json = rugson.toJson(map);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(json.getBytes());
        fileOutputStream.close();
    }

}

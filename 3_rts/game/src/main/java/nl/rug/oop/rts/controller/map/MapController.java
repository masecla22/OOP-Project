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

/**
 * Controller for the map.
 */
public abstract class MapController {
    private Rugson rugson;

    @Getter
    private CompletableFuture<Node> addingEdge = null;

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private Map map;

    public MapController(Rugson rugson, @NonNull Map map) {
        this.rugson = rugson;
        this.map = map;
    }

    /**
     * Remove a node from the map.
     * 
     * @param node - the node to remove
     */
    public abstract void removeNode(Node node);

    /**
     * Create a new node.
     * 
     * @param nodeName - the name of the node
     * @return - the new node
     */
    public abstract Node createNode(String nodeName);

    /**
     * Adds a new edge.
     * 
     * @param node1 - the first node
     * @param node2 - the second node
     */
    public abstract void addEdge(Node node1, Node node2);

    /**
     * Remove an edge.
     * 
     * @param edge - the edge to remove
     */
    public abstract void removeEdge(Edge edge);

    /**
     * Adds an army to a node.
     * 
     * @param node    - the node to add the army to
     * @param faction - the faction of the army
     */
    public abstract void addArmy(Node node, Faction faction);

    /**
     * Removes an army from a node.
     * 
     * @param node - the node to remove the army from
     * @param army - the army to remove
     */
    public abstract void removeArmy(Node node, Army army);

    /**
     * Adds an event to a node.
     * 
     * @param node - the node to add the event to
     * @param type - the type of the event
     */
    public abstract void addEvent(Node node, EventType type);

    /**
     * Removes an event from a node.
     * 
     * @param node  - the node to remove the event from
     * @param event - the event to remove
     */
    public abstract void removeEvent(Node node, Event event);

    /**
     * Adds an event to an edge.
     * 
     * @param edge - the edge to add the event to
     * @param type - the type of the event
     */
    public abstract void addEvent(Edge edge, EventType type);

    /**
     * Removes an event from an edge.
     * 
     * @param edge  - the edge to remove the event from
     * @param event - the event to remove
     */
    public abstract void removeEvent(Edge edge, Event event);

    /**
     * Sets the position of a node.
     * 
     * @param node     - the node to set the position of
     * @param position - the new position
     */
    public abstract void setNodePosition(Node node, Point position);

    /**
     * Sets the name of a node.
     * 
     * @param node - the node to set the name of
     * @return - the new name
     */
    public abstract Color getColorForNode(Node node);

    /**
     * Whether to allow event modification.
     * 
     * @return - whether to allow event modification
     */
    public abstract boolean allowEventModification();

    /**
     * Whether to allow node renaming.
     * 
     * @return - whether to allow node renaming
     */
    public abstract boolean allowNodeRenaming();

    /**
     * Whether the player can place an army on a node.
     * 
     * @param node - the node to place the army on
     * @return - whether the player can place an army on the node
     */
    public abstract String canPlaceArmy(Node node);

    /**
     * Get the list of allowed factions the player can place an army of.
     * 
     * @return - the list of allowed factions
     */
    public abstract Set<Faction> getAllowedFactions();

    /**
     * Whether to show the unit cost.
     * 
     * @return - whether to show the unit cost
     */
    public abstract boolean showUnitCost();

    /**
     * Whether to allow army removal.
     * 
     * @return - whether to allow army removal
     */
    public abstract boolean allowArmyRemoval();

    /**
     * Sets the selection of the map.
     * 
     * @param selection - the new selection
     */
    public void setSelection(Selectable selection) {
        if (selection == null) {
            this.addingEdge = null;
        }
        this.map.setSelection(selection);
    }

    /**
     * Whether the player is adding an edge.
     * 
     * @return - whether the player is adding an edge
     */
    public boolean isAddingEdge() {
        return this.addingEdge != null;
    }

    /**
     * Mark that the player is adding an edge.
     */
    public void markAddingEdge() {
        this.addingEdge = new CompletableFuture<>();
        map.update();
    }

    /**
     * Unmark that the player is adding an edge.
     */
    public void unmarkAddingEdge() {
        this.addingEdge = null;
        map.update();
    }

    /**
     * Sets the offset of the map.
     * 
     * @param offset - the new offset
     */
    public void setOffset(Point offset) {
        // Bind offset between 0
        if (offset.x > 0) {
            offset.x = 0;
        }
        if (offset.y > 0) {
            offset.y = 0;
        }
        if (offset.x < -MapView.MAP_SIZE + 600) {
            offset.x = -MapView.MAP_SIZE + 600;
        }
        if (offset.y < -MapView.MAP_SIZE + 600) {
            offset.y = -MapView.MAP_SIZE + 600;
        }

        map.setOffset(offset);
    }

    /**
     * Saves the map to a file.
     * 
     * @param file - the file to save to
     * @throws IOException - if an IO error occurs
     */
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

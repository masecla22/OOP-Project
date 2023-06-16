package nl.rug.oop.rts.controller.map;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import lombok.NonNull;
import nl.rug.oop.rts.protocol.objects.model.Edge;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.objects.model.Node;
import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.armies.Faction;
import nl.rug.oop.rts.protocol.objects.model.armies.Team;
import nl.rug.oop.rts.protocol.objects.model.events.Event;
import nl.rug.oop.rts.protocol.objects.model.events.EventType;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.MultiplayerGame;
import nl.rug.oop.rugson.Rugson;

public class MultiplayerMapController extends MapController {
    private Team team;
    private MultiplayerGame multiplayerGame;

    public MultiplayerMapController(MultiplayerGame multiplayerGame, Team team, Rugson rugson, @NonNull Map map) {
        super(rugson, map);
        this.multiplayerGame = multiplayerGame;
    }

    @Override
    public void addArmy(Node node, Faction faction) {
        System.out.println("Adding army!");
    }

    @Override
    public void removeArmy(Node node, Army army) {
        System.out.println("Removing army!");
    }

    @Override
    public void addEdge(Node node1, Node node2) {
        throw new UnsupportedOperationException("Multiplayer maps cannot be edited!");
    }

    @Override
    public void addEvent(Node node, EventType type) {
        throw new UnsupportedOperationException("Multiplayer maps cannot be edited!");
    }

    @Override
    public void addEvent(Edge edge, EventType type) {
        throw new UnsupportedOperationException("Multiplayer maps cannot be edited!");
    }

    @Override
    public Node createNode(String nodeName) {
        throw new UnsupportedOperationException("Multiplayer maps cannot be edited!");
    }

    @Override
    public void removeEdge(Edge edge) {
        throw new UnsupportedOperationException("Multiplayer maps cannot be edited!");
    }

    @Override
    public void removeEvent(Node node, Event event) {
        throw new UnsupportedOperationException("Multiplayer maps cannot be edited!");
    }

    @Override
    public void removeEvent(Edge edge, Event event) {
        throw new UnsupportedOperationException("Multiplayer maps cannot be edited!");
    }

    @Override
    public void removeNode(Node node) {
        throw new UnsupportedOperationException("Multiplayer maps cannot be edited!");
    }

    @Override
    public void setNodePosition(Node node, Point position) {
        // Ignore the request.
    }

    @Override
    public boolean isAddingEdge() {
        return false;
    }

    @Override
    public void markAddingEdge() {
        throw new UnsupportedOperationException("Multiplayer maps cannot be edited!");
    }

    @Override
    public void unmarkAddingEdge() {
        throw new UnsupportedOperationException("Multiplayer maps cannot be edited!");
    }

    @Override
    public void exportToJson(File file) throws IOException {
        throw new UnsupportedOperationException("Multiplayer maps cannot be exported!");
    }

    @Override
    public Color getColorForNode(Node node) {
        if (this.getMap().getSelection().equals(node)) {
            return Color.RED;
        }

        if (this.multiplayerGame.getPlayerA().getStartingNode().equals(node)) {
            if (this.team.equals(Team.TEAM_A)) {
                return Color.GREEN;
            } else {
                return Color.MAGENTA;
            }
        }

        if (this.multiplayerGame.getPlayerB().getStartingNode().equals(node)) {
            if (this.team.equals(Team.TEAM_B)) {
                return Color.GREEN;
            } else {
                return Color.MAGENTA;
            }
        }

        return null;
    }
}

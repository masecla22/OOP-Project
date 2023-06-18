package nl.rug.oop.rts.controller.map;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NonNull;
import nl.rug.oop.rts.protocol.objects.model.Edge;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.objects.model.Node;
import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.armies.Faction;
import nl.rug.oop.rts.protocol.objects.model.armies.Team;
import nl.rug.oop.rts.protocol.objects.model.events.Event;
import nl.rug.oop.rts.protocol.objects.model.events.EventType;
import nl.rug.oop.rts.protocol.objects.model.factories.UnitFactory;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.GameChange;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.GamePlayer;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.MultiplayerGame;
import nl.rug.oop.rts.protocol.packet.definitions.game.GameUpdatePacket;
import nl.rug.oop.rugson.Rugson;

public class MultiplayerMapController extends MapController {
    private Team team;
    private MultiplayerGame multiplayerGame;

    private UnitFactory unitFactory;

    @Getter
    private List<GameChange> changes = new ArrayList<>();

    public MultiplayerMapController(MultiplayerGame multiplayerGame, Team team, Rugson rugson,
            @NonNull Map map, UnitFactory unitFactory) {
        super(rugson, map);
        this.multiplayerGame = multiplayerGame;
        this.team = team;
        this.unitFactory = unitFactory;
    }

    @Override
    public void addArmy(Node node, Faction faction) {
        GamePlayer player = this.multiplayerGame.getGamePlayer(team);
        int gold = player.getGold();
        if (gold < faction.getCost()) {
            return;
        }

        Army army = unitFactory.buildArmy(faction);
        node.addArmy(army);

        this.changes.add(new GameChange(node, faction));

        player.setGold(gold - faction.getCost());

        this.getMap().update();
        this.multiplayerGame.update();
    }

    @Override
    public void removeArmy(Node node, Army army) {
        throw new UnsupportedOperationException("You cannot remove armies in MP!");
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
        if (this.getMap().getSelection() != null) {
            if (this.getMap().getSelection().equals(node)) {
                return Color.RED;
            }
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

        if (this.multiplayerGame.getGoldGeneratingNodes().contains(node)) {
            return Color.ORANGE;
        }

        return null;
    }

    @Override
    public boolean allowEventModification() {
        return false;
    }

    @Override
    public boolean allowNodeRenaming() {
        return false;
    }

    @Override
    public String canPlaceArmy(Node node) {
        if (multiplayerGame.getPlayerA().getStartingNode().equals(node) && team.equals(Team.TEAM_A))
            return null;
        if (multiplayerGame.getPlayerB().getStartingNode().equals(node) && team.equals(Team.TEAM_B))
            return null;

        List<Edge> edges = node.getEdges();
        boolean friendlies = false;
        for (Edge cr : edges) {
            if (cr.getArmies().stream().anyMatch(c -> c.getFaction().getTeam().equals(this.team))) {
                friendlies = true;
                break;
            }
        }

        if (!friendlies)
            return "No friendlies nearby!";

        if (team == Team.TEAM_A && multiplayerGame.getPlayerA().getGold() < Faction.getSmallestCost())
            return "Not enough gold!";

        if (team == Team.TEAM_B && multiplayerGame.getPlayerB().getGold() < Faction.getSmallestCost())
            return "Not enough gold!";

        if (!multiplayerGame.isMyTurn(team)) {
            return "Not your turn!";
        }

        return null;
    }

    @Override
    public Set<Faction> getAllowedFactions() {
        return Arrays.stream(Faction.values())
                .filter(c -> c.getTeam().equals(this.team))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean allowArmyRemoval() {
        return false;
    }

    @Override
    public boolean showUnitCost() {
        return true;
    }

    public void ingestMapUpdate(GameUpdatePacket packet) {
        this.changes.clear();

        // Update gold
        MultiplayerGame game = packet.getCurrentGame();

        multiplayerGame.getPlayerA().setGold(game.getPlayerA().getGold());
        multiplayerGame.getPlayerB().setGold(game.getPlayerB().getGold());
        multiplayerGame.setPlayerATurn(game.isPlayerATurn());

        multiplayerGame.getMap().setNodes(game.getMap().getNodes());
        multiplayerGame.getMap().setEdges(game.getMap().getEdges());

        multiplayerGame.getMap().update();
        multiplayerGame.update();
    }
}

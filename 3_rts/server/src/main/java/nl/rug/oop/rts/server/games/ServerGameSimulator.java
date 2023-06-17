package nl.rug.oop.rts.server.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import nl.rug.oop.rts.protocol.objects.model.Edge;
import nl.rug.oop.rts.protocol.objects.model.Node;
import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.armies.Team;
import nl.rug.oop.rts.protocol.objects.model.events.Event;
import nl.rug.oop.rts.protocol.objects.model.factories.UnitFactory;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.GameChange;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.GamePlayer;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.MultiplayerGame;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;

@AllArgsConstructor
public class ServerGameSimulator {
    private UnitFactory unitFactory;
    private MultiplayerGame game;

    public boolean canDoChanges(List<GameChange> changes, Team team) {
        int totalCost = changes.stream().mapToInt(change -> change.getFaction().getCost()).sum();

        // Make sure the team has enough gold to apply the changes
        if (game.getGamePlayer(team).getGold() < totalCost) {
            return false;
        }

        // Make sure no armies were placed on inexistent nodes
        // (use map for fast lookup)
        Map<Integer, Node> nodes = new HashMap<>();
        for (Node node : game.getMap().getNodes()) {
            nodes.put(node.getId(), node);
        }

        for (GameChange change : changes) {
            if (!nodes.keySet().contains(change.getNodeId())) {
                return false;
            }
        }

        // Make sure the player was allowed to place armies on the nodes it places them
        // on
        for (GameChange change : changes) {
            Node node = nodes.get(change.getNodeId());
            if (!canTeamPlace(node, team)) {
                return false;
            }
        }

        return true;
    }

    public void applyChanges(List<GameChange> changes, Team team) {
        // First subtract the gold from the player's balance
        GamePlayer player = game.getGamePlayer(team);
        int gold = player.getGold();
        player.setGold(gold - changes.stream().mapToInt(change -> change.getFaction().getCost()).sum());

        // Then apply the changes to the map
        Map<Integer, Node> nodes = new HashMap<>();
        for (Node node : game.getMap().getNodes()) {
            nodes.put(node.getId(), node);
        }

        for (GameChange change : changes) {
            Node node = nodes.get(change.getNodeId());
            node.addArmy(unitFactory.buildArmy(change.getFaction()));
        }

        game.update();
    }

    private boolean canTeamPlace(Node node, Team team) {
        GamePlayer player = game.getGamePlayer(team);
        if (player.getStartingNode().equals(node)) {
            return true;
        }

        List<Edge> edges = node.getEdges();
        for (Edge cr : edges) {
            if (cr.getOtherNode(node).getArmies().stream()
                    .anyMatch(c -> c.getFaction().getTeam().equals(team)))
                return true;
        }

        return false;
    }

    public void simulateStep() {
        resolveAllBattles();
        moveNodeArmies();
        resolveAllBattles();
        applyEvents();
        moveEdgeArmies();
        resolveAllBattles();
        applyEvents();

        purgeArmies();
        resetArmiesStatus();

        giveGold();

        game.update();
    }

    private void giveGold() {
        int goldForTeamA = 0;
        int goldForTeamB = 0;
        for (Node node : game.getGoldGeneratingNodes()) {
            for (Army army : node.getArmies()) {
                if (army.getFaction().getTeam().equals(Team.TEAM_A)) {
                    goldForTeamA += 50;
                } else {
                    goldForTeamB += 50;
                }
            }
        }

        game.getPlayerA().setGold(game.getPlayerA().getGold() + goldForTeamA);
        game.getPlayerB().setGold(game.getPlayerB().getGold() + goldForTeamB);
    }

    private void moveNodeArmies() {
        for (Node node : game.getMap().getNodes()) {
            List<Army> armies = node.getArmies();
            List<Edge> edges = node.getEdges();

            List<Army> toRemove = new ArrayList<>();
            for (Army cr : armies) {
                if (cr.isMoved()) {
                    continue;
                }

                Edge randomEdge = edges.get((int) (Math.random() * edges.size()));
                Node randomNode = randomEdge.getOtherNode(node);

                cr.setMovingToNextStep(randomNode);
                cr.setMoved(true);

                randomEdge.addArmy(cr);
                toRemove.add(cr);
            }

            armies.removeAll(toRemove);
        }
    }

    private void purgeArmies() {
        game.getMap().getNodes().forEach(c -> c.getArmies().removeIf(Army::isEmpty));
        game.getMap().getEdges().forEach(c -> c.getArmies().removeIf(Army::isEmpty));
    }

    private void applyEvents() {
        for (Node node : game.getMap().getNodes()) {
            applyEvents(node.getArmies(), node.getEvents());
            node.getEvents().clear();
        }

        for (Edge edge : game.getMap().getEdges()) {
            applyEvents(edge.getArmies(), edge.getEvents());
            edge.getEvents().clear();
        }
    }

    private void applyEvents(List<Army> armies, List<Event> events) {
        if (events.size() == 0 || armies.size() == 0) {
            return;
        }

        for (Army army : armies) {
            for (Event event : events) {
                event.execute(army);
            }
        }
    }

    private void resolveAllBattles() {
        for (Node node : game.getMap().getNodes()) {
            if (shouldBattleHappen(node)) {
                resolveBattle(node.getArmies());
            }
        }

        for (Edge edge : game.getMap().getEdges()) {
            if (shouldBattleHappen(edge)) {
                resolveBattle(edge.getArmies());
            }
        }
    }

    private void moveEdgeArmies() {
        for (Edge edge : game.getMap().getEdges()) {
            List<Army> armies = edge.getArmies();

            List<Army> toRemove = new ArrayList<>();
            for (Army cr : armies) {
                if (cr.isMoved()) {
                    continue;
                }

                Node nextNode = cr.getMovingToNextStep();
                if (nextNode == null)
                    nextNode = edge.getPointA();

                nextNode.addArmy(cr);

                cr.setMoved(true);
                cr.setMovingToNextStep(null);
                toRemove.add(cr);
            }

            armies.removeAll(toRemove);
        }
    }

    private void resetArmiesStatus() {
        List<Army> toChange = new ArrayList<>();
        for (Node node : game.getMap().getNodes())
            toChange.addAll(node.getArmies());
        for (Edge edge : game.getMap().getEdges())
            toChange.addAll(edge.getArmies());

        for (Army cr : toChange) {
            cr.setMoved(false);
        }
    }

    private boolean shouldBattleHappen(Node node) {
        Set<Team> teams = new HashSet<>();
        for (Army cr : node.getArmies())
            teams.add(cr.getFaction().getTeam());

        return teams.size() > 1;
    }

    private boolean shouldBattleHappen(Edge edge) {
        Set<Team> teams = new HashSet<>();
        for (Army cr : edge.getArmies())
            teams.add(cr.getFaction().getTeam());

        return teams.size() > 1;
    }

    private void resolveBattle(List<Army> armies) {
        List<Army> teamA = new ArrayList<>();
        List<Army> teamB = new ArrayList<>();

        for (Army cr : armies) {
            if (cr.getFaction().getTeam() == Team.TEAM_A) {
                teamA.add(cr);
            } else {
                teamB.add(cr);
            }
        }

        while (teamA.size() > 0 && teamB.size() > 0) {
            Army aArmy = teamA.get(0);
            Army bArmy = teamB.get(0);

            resolveSingleArmy(aArmy, bArmy);
            if (aArmy.getUnits().size() == 0)
                teamA.remove(aArmy);
            if (bArmy.getUnits().size() == 0)
                teamB.remove(bArmy);
        }

        armies.removeIf(cr -> cr.getUnits().size() == 0);
    }

    private void resolveSingleArmy(Army a, Army b) {
        List<Unit> aUnits = a.getUnits();
        List<Unit> bUnits = b.getUnits();

        while (aUnits.size() > 0 && bUnits.size() > 0) {
            Unit aUnit = aUnits.get(0);
            Unit bUnit = bUnits.get(0);

            resolveSingleUnitBattle(aUnit, bUnit);
            if (aUnit.getHealth() <= 0)
                aUnits.remove(aUnit);
            if (bUnit.getHealth() <= 0)
                bUnits.remove(bUnit);
        }
    }

    private void resolveSingleUnitBattle(Unit a, Unit b) {
        while (a.getHealth() > 0 && b.getHealth() > 0) {
            double damage = a.dealDamage(b);
            b.takeDamage(b, damage);

            if (b.getHealth() <= 0)
                break;

            damage = b.dealDamage(a);
            a.takeDamage(a, damage);

            if (a.getHealth() <= 0)
                break;
        }
    }
}

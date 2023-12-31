package nl.rug.oop.rts.controller.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import nl.rug.oop.rts.protocol.objects.model.Edge;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.objects.model.Node;
import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.armies.Team;
import nl.rug.oop.rts.protocol.objects.model.events.Event;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;

/**
 * Controller for the map simulation.
 */
@AllArgsConstructor
public class MapSimulationController {
    private Map map;

    private void update() {
        map.update();
    }

    /**
     * Simulates a full step.
     */
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
        update();
    }

    private void moveNodeArmies() {
        for (Node node : map.getNodes()) {
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
        map.getNodes().forEach(c -> c.getArmies().removeIf(Army::isEmpty));
        map.getEdges().forEach(c -> c.getArmies().removeIf(Army::isEmpty));
    }

    private void applyEvents() {
        for (Node node : map.getNodes()) {
            applyEvents(node.getArmies(), node.getEvents());
        }

        for (Edge edge : map.getEdges()) {
            applyEvents(edge.getArmies(), edge.getEvents());
        }
    }

    private void applyEvents(List<Army> armies, List<Event> events) {
        // Check if events should be applied
        if (Math.random() > 0.5) {
            return;
        }

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
        for (Node node : map.getNodes()) {
            if (shouldBattleHappen(node)) {
                resolveBattle(node.getArmies());
            }
        }

        for (Edge edge : map.getEdges()) {
            if (shouldBattleHappen(edge)) {
                resolveBattle(edge.getArmies());
            }
        }
    }

    private void moveEdgeArmies() {
        for (Edge edge : map.getEdges()) {
            List<Army> armies = edge.getArmies();

            List<Army> toRemove = new ArrayList<>();
            for (Army cr : armies) {
                if (cr.isMoved()) {
                    continue;
                }

                Node nextNode = cr.getMovingToNextStep();
                if (nextNode == null) {
                    nextNode = edge.getPointA();
                }

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
        for (Node node : map.getNodes()) {
            toChange.addAll(node.getArmies());
        }
        for (Edge edge : map.getEdges()) {
            toChange.addAll(edge.getArmies());
        }

        for (Army cr : toChange) {
            cr.setMoved(false);
        }
    }

    private boolean shouldBattleHappen(Node node) {
        Set<Team> teams = new HashSet<>();
        for (Army cr : node.getArmies()) {
            teams.add(cr.getFaction().getTeam());
        }

        return teams.size() > 1;
    }

    private boolean shouldBattleHappen(Edge edge) {
        Set<Team> teams = new HashSet<>();
        for (Army cr : edge.getArmies()) {
            teams.add(cr.getFaction().getTeam());
        }

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
            if (aArmy.getUnits().size() == 0) {
                teamA.remove(aArmy);
            }
            if (bArmy.getUnits().size() == 0) {
                teamB.remove(bArmy);
            }
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
            if (aUnit.getHealth() <= 0) {
                aUnits.remove(aUnit);
            }
            if (bUnit.getHealth() <= 0) {
                bUnits.remove(bUnit);
            }
        }
    }

    private void resolveSingleUnitBattle(Unit a, Unit b) {
        while (a.getHealth() > 0 && b.getHealth() > 0) {
            double damage = a.dealDamage(b);
            b.takeDamage(b, damage);

            if (b.getHealth() <= 0) {
                break;
            }

            damage = b.dealDamage(a);
            a.takeDamage(a, damage);

            if (a.getHealth() <= 0) {
                break;
            }
        }
    }
}

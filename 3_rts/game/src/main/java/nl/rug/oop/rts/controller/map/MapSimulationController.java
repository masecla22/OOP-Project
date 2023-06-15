package nl.rug.oop.rts.controller.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import nl.rug.oop.rts.model.Edge;
import nl.rug.oop.rts.model.Map;
import nl.rug.oop.rts.model.Node;
import nl.rug.oop.rts.model.armies.Army;
import nl.rug.oop.rts.model.armies.Team;
import nl.rug.oop.rts.model.units.Unit;

@AllArgsConstructor
public class MapSimulationController {
    private Map map;

    private void update() {
        map.update();
    }

    public void simulateStep() {
        resolveAllBattles();
        moveNodeArmies();
        resolveAllBattles();
        moveEdgeArmies();
        resolveAllBattles();

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
        for (Node node : map.getNodes())
            toChange.addAll(node.getArmies());
        for (Edge edge : map.getEdges())
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
            if (cr.getFaction().getTeam() == Team.TEAM_A)
                teamA.add(cr);
            else
                teamB.add(cr);
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

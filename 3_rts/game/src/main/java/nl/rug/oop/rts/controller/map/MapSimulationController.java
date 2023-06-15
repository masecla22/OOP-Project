package nl.rug.oop.rts.controller.map;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import nl.rug.oop.rts.model.Edge;
import nl.rug.oop.rts.model.Map;
import nl.rug.oop.rts.model.Node;
import nl.rug.oop.rts.model.armies.Army;

@AllArgsConstructor
public class MapSimulationController {
    private Map map;

    private void update() {
        map.update();
    }

    public void simulateStep() {
        moveNodeArmies();
        moveEdgeArmies();

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

}

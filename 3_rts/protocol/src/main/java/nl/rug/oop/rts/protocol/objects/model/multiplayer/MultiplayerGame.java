package nl.rug.oop.rts.protocol.objects.model.multiplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rts.protocol.objects.interfaces.observing.Observable;
import nl.rug.oop.rts.protocol.objects.interfaces.observing.Observer;
import nl.rug.oop.rts.protocol.objects.model.Edge;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.objects.model.Node;
import nl.rug.oop.rts.protocol.objects.model.armies.Team;
import nl.rug.oop.rts.protocol.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class MultiplayerGame implements Observable {
    @NonNull
    private UUID gameId;

    @NonNull
    private Map map;

    @NonNull
    private GamePlayer playerA;

    @NonNull
    private GamePlayer playerB;

    private boolean isPlayerATurn;

    private List<Node> goldGeneratingNodes = new ArrayList<>();

    private transient Set<Observer> observers = new HashSet<>();

    public void initialize() {
        this.initializeStartingPosition();
        this.initializeGoldNodes();

        this.isPlayerATurn = true;
    }

    public boolean isPartOf(User user) {
        return playerA.getUser().getName().equals(user.getName()) ||
                playerB.getUser().getName().equals(user.getName());
    }

    private void initializeStartingPosition() {
        List<Node> nodes = new ArrayList<>(map.getNodes());
        Node startA = nodes.get(ThreadLocalRandom.current().nextInt(nodes.size()));

        playerA.setStartingNode(startA);
        playerA.setTeam(Team.TEAM_A);

        Node startB = findFurthestNodeFrom(startA);

        playerB.setStartingNode(startB);
        playerB.setTeam(Team.TEAM_B);
    }

    private void initializeGoldNodes() {
        Node startA = playerA.getStartingNode();
        Node startB = playerB.getStartingNode();

        java.util.Map<Node, Integer> distancesA = getDistanceMap(startA);
        java.util.Map<Node, Integer> distancesB = getDistanceMap(startB);

        int goldGeneratingNodesCount = (int) (map.getNodes().size() / 10.0);
        if (goldGeneratingNodesCount < 2) {
            goldGeneratingNodesCount = 2;
        }

        List<Node> possibleNodes = new ArrayList<>(map.getNodes());
        possibleNodes.remove(startA);
        possibleNodes.remove(startB);

        if (possibleNodes.size() < goldGeneratingNodesCount) {
            goldGeneratingNodesCount = possibleNodes.size();
        }

        // Sort the nodes by how balanced the distance is between the two starting
        // nodes.
        // We want the nodes who are at the same distance from both starting nodes.
        possibleNodes.sort((a, b) -> {
            int distanceA = distancesA.get(a);
            int distanceB = distancesB.get(a);

            int distanceA2 = distancesA.get(b);
            int distanceB2 = distancesB.get(b);

            int distanceDiff = Math.abs(distanceA - distanceB);
            int distanceDiff2 = Math.abs(distanceA2 - distanceB2);

            return Integer.compare(distanceDiff, distanceDiff2);
        });

        for (int i = 0; i < Math.min(goldGeneratingNodesCount, possibleNodes.size()); i++)
            goldGeneratingNodes.add(possibleNodes.get(i));
    }

    private Node findFurthestNodeFrom(Node node) {
        java.util.Map<Node, Integer> distances = getDistanceMap(node);

        int maxDistance = 0;
        Node furthest = null;

        for (Entry<Node, Integer> entry : distances.entrySet()) {
            if (entry.getValue() > maxDistance) {
                maxDistance = entry.getValue();
                furthest = entry.getKey();
            }
        }

        return furthest;
    }

    private java.util.Map<Node, Integer> getDistanceMap(Node node) {
        java.util.Map<Node, Integer> distances = new HashMap<>();
        for (Node n : map.getNodes()) {
            distances.put(n, Integer.MAX_VALUE);
        }

        distances.put(node, 0);
        Queue<Node> queue = new LinkedList<>();
        queue.add(node);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            int distance = distances.get(current);
            for (Edge edge : current.getEdges()) {
                Node other = edge.getOtherNode(current);
                if (distances.get(other) > distance + 1) {
                    distances.put(other, distance + 1);
                    queue.add(other);
                }
            }
        }
        return distances;
    }

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    public boolean isMyTurn(Team team) {
        if (team == Team.TEAM_A) {
            return isPlayerATurn;
        } else {
            return !isPlayerATurn;
        }
    }

    public GamePlayer getGamePlayer(Team team) {
        if (team == Team.TEAM_A) {
            return playerA;
        } else {
            return playerB;
        }
    }

}

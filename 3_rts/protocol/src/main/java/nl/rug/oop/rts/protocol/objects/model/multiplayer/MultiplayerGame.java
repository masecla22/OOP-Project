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
import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.armies.Team;
import nl.rug.oop.rts.protocol.user.User;

/**
 * Represents an ongoing multiplayer game.
 */
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

    /**
     * Finds the furthest node from a given node.
     */
    public void initialize() {
        this.initializeStartingPosition();
        this.initializeGoldNodes();

        this.isPlayerATurn = true;
    }

    /**
     * Checks if a user is part of this game.
     * 
     * @param user - the user
     * @return - true if the user is part of this game, false otherwise.
     */
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

        for (int i = 0; i < Math.min(goldGeneratingNodesCount, possibleNodes.size()); i++) {
            goldGeneratingNodes.add(possibleNodes.get(i));
        }
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
        if (observers == null) {
            observers = new HashSet<>();
        }
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Check if it is the turn of the given team.
     * 
     * @param team - the team
     * @return - true if it is the turn of the given team, false otherwise
     */
    public boolean isMyTurn(Team team) {
        if (team == Team.TEAM_A) {
            return isPlayerATurn;
        } else {
            return !isPlayerATurn;
        }
    }

    /**
     * Gets a game player from a team.
     * 
     * @param team - the team
     * @return - the game player
     */
    public GamePlayer getGamePlayer(Team team) {
        if (team == Team.TEAM_A) {
            return playerA;
        } else {
            return playerB;
        }
    }

    /**
     * Returns the team of the given user.
     * 
     * @param user - the user
     * @return - the team of the user or null if the user is not part of the game
     */
    public Team getTeam(User user) {
        if (playerA.getUser().getName().equals(user.getName())) {
            return Team.TEAM_A;
        } else if (playerB.getUser().getName().equals(user.getName())) {
            return Team.TEAM_B;
        } else {
            return null;
        }
    }

    /**
     * Checks if the game is over and returns the winning team if it is.
     * 
     * @return - the winning team or null if the game is not over yet
     */
    public Team checkWinner() {
        Node startingNodeTeamA = getPlayerA().getStartingNode();
        // Check if the starting node has any armies from the other team
        for (Army cr : startingNodeTeamA.getArmies()) {
            if (cr.getFaction().getTeam() != Team.TEAM_A) {
                return Team.TEAM_B;
            }
        }

        Node startingNodeTeamB = getPlayerB().getStartingNode();
        // Check if the starting node has any armies from the other team
        for (Army cr : startingNodeTeamB.getArmies()) {
            if (cr.getFaction().getTeam() != Team.TEAM_B) {
                return Team.TEAM_A;
            }
        }

        return null;
    }

    /**
     * Given a player, returns the other player.
     * 
     * @param player - the player to get the other player from
     * @return - the other player
     */
    public GamePlayer getOtherPlayer(GamePlayer player) {
        if (player == playerA) {
            return playerB;
        } else {
            return playerA;
        }
    }
}

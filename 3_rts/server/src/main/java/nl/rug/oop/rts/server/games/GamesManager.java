package nl.rug.oop.rts.server.games;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.games.MultiplayerLobby;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.objects.model.armies.Team;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.GamePlayer;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.MultiplayerGame;
import nl.rug.oop.rts.protocol.packet.definitions.game.GameEndPacket;
import nl.rug.oop.rts.protocol.user.User;
import nl.rug.oop.rts.server.user.UserManager;

/**
 * Manages all the games and lobbies, as well as their creation.
 */
@RequiredArgsConstructor
public class GamesManager {
    @Getter
    private java.util.Map<UUID, MultiplayerLobby> lobbies = new HashMap<>();

    @Getter
    private java.util.Map<UUID, MultiplayerGame> games = new HashMap<>();

    @NonNull
    private UserManager userManager;

    /**
     * Gets the lobby that the user is hosting.
     * 
     * @param user - The user to get the lobby for.
     * @return - The lobby that the user is hosting, or null if the user is not
     *         hosting a lobby.
     */
    public MultiplayerLobby getByUser(User user) {
        for (MultiplayerLobby lobby : lobbies.values()) {
            if (lobby.getHost().getId() == user.getId()) {
                return lobby;
            }
        }
        return null;
    }

    /**
     * Creates a new lobby for the user.
     * 
     * @param user       - The user to create the lobby for.
     * @param lobbyName  - The name of the lobby.
     * @param map        - The map to use.
     * @param mapName    - The name of the map.
     * @param connection - The connection of the user who made it.
     * @return - The created lobby.
     */
    public MultiplayerLobby createLobby(User user, String lobbyName, Map map, String mapName,
            SocketConnection connection) {
        MultiplayerLobby lobby = new MultiplayerLobby(UUID.randomUUID(), map, mapName, lobbyName, user, connection);
        lobbies.put(lobby.getLobbyId(), lobby);

        return lobby;
    }

    /**
     * Removes the lobby from the list of lobbies.
     * 
     * @param lobbyId - The id of the lobby to remove.
     */
    public void removeLobby(UUID lobbyId) {
        lobbies.remove(lobbyId);
    }

    /**
     * Gets the lobby with the given id.
     * 
     * @param lobbyId - The id of the lobby to get.
     * @return - The lobby with the given id.
     */
    public MultiplayerLobby getLobby(UUID lobbyId) {
        return this.lobbies.get(lobbyId);
    }

    /**
     * Checks if the user can join the lobby.
     * 
     * @param lobby - The lobby to check.
     * @param user  - The user to check.
     * @return - True if the user can join the lobby, false otherwise.
     */
    public boolean canJoin(MultiplayerLobby lobby, User user) {
        // Make sure the user is not the host
        if (lobby.getHost().getId() == user.getId()) {
            return false;
        }

        // Make sure the user is not already in another lobby
        for (MultiplayerLobby otherLobby : lobbies.values()) {
            if (otherLobby.getHost().getId() == user.getId()) {
                return false;
            }
        }

        // Make sure the player is not in another game
        for (MultiplayerGame game : games.values()) {
            if (game.getPlayerA().getUser().getId() == user.getId()
                    || game.getPlayerB().getUser().getId() == user.getId()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Creates a game from the given lobby.
     * 
     * @param lobby               - The lobby to create the game from.
     * @param otherUser           - The user to play against.
     * @param otherUserConnection - The connection of the user to play against.
     * @return - The created game.
     */
    public MultiplayerGame createGame(MultiplayerLobby lobby, User otherUser, SocketConnection otherUserConnection) {
        UUID gameId = UUID.randomUUID();

        GamePlayer player = new GamePlayer(lobby.getHost(), 400, Team.TEAM_A, null, lobby.getConnection());
        GamePlayer otherPlayer = new GamePlayer(otherUser, 600, Team.TEAM_B, null, otherUserConnection);

        MultiplayerGame game = new MultiplayerGame(gameId, lobby.getMap(), player, otherPlayer);
        game.initialize();

        games.put(gameId, game);
        lobbies.remove(lobby.getLobbyId());

        return game;
    }

    /**
     * Gets the game with the given id.
     * 
     * @param gameId - The id of the game to get.
     * @return - The game with the given id.
     */
    public MultiplayerGame getGame(UUID gameId) {
        return games.get(gameId);
    }

    /**
     * Handles a finished game.
     * 
     * @param game       - The game that finished.
     * @param winner     - The winner of the game.
     * @param disconnected - Whether the game ended due to a disconnect.
     */
    public void handleFinishedGame(MultiplayerGame game, GamePlayer winner, boolean disconnected) {
        games.remove(game.getGameId());

        GamePlayer loser = game.getOtherPlayer(winner);

        User winningUser = winner.getUser();
        User losingUser = loser.getUser();

        this.userManager.updateRatings(winningUser, losingUser);

        try {
            GameEndPacket packet = new GameEndPacket(true, winningUser.getElo());
            winner.getConnection().sendPacket(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If the game end was caused due to a disconnect, no need to send the packet
        // to the other player!
        if (!disconnected) {
            try {
                GameEndPacket packet = new GameEndPacket(false, losingUser.getElo());
                loser.getConnection().sendPacket(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles a disconnect from a socket.
     * 
     * @param connection - The connection that disconnected.
     */
    public void handleDisconnect(SocketConnection connection) {
        // Wipe any lobbies containing this socket
        for (MultiplayerLobby lobby : lobbies.values()) {
            if (lobby.getConnection() == connection) {
                lobbies.remove(lobby.getLobbyId());
            }
        }

        // Close out any games in progress
        for (MultiplayerGame game : games.values()) {
            if (game.getPlayerA().getConnection() == connection) {
                handleFinishedGame(game, game.getPlayerB(), true);
            } else if (game.getPlayerB().getConnection() == connection) {
                handleFinishedGame(game, game.getPlayerA(), true);
            }
        }
    }
}

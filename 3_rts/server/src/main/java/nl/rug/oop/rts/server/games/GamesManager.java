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

@RequiredArgsConstructor
public class GamesManager {
    @Getter
    private java.util.Map<UUID, MultiplayerLobby> lobbies = new HashMap<>();

    @Getter
    private java.util.Map<UUID, MultiplayerGame> games = new HashMap<>();

    @NonNull
    private UserManager userManager;

    public MultiplayerLobby getByUser(User user) {
        for (MultiplayerLobby lobby : lobbies.values()) {
            if (lobby.getHost().getId() == user.getId()) {
                return lobby;
            }
        }
        return null;
    }

    public MultiplayerLobby createLobby(User user, String lobbyName, Map map, String mapName,
            SocketConnection connection) {
        MultiplayerLobby lobby = new MultiplayerLobby(UUID.randomUUID(), map, mapName, lobbyName, user, connection);
        lobbies.put(lobby.getLobbyId(), lobby);

        return lobby;
    }

    public void removeLobby(UUID lobbyId) {
        lobbies.remove(lobbyId);
    }

    public MultiplayerLobby getLobby(UUID lobbyId) {
        return this.lobbies.get(lobbyId);
    }

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

    public MultiplayerGame getGame(UUID gameId) {
        return games.get(gameId);
    }

    public void handleFinishedGame(MultiplayerGame game, GamePlayer winner) {
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

        try {
            GameEndPacket packet = new GameEndPacket(false, losingUser.getElo());
            loser.getConnection().sendPacket(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

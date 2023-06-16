package nl.rug.oop.rts.server.games;

import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.games.MultiplayerLobby;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.objects.model.armies.Team;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.GamePlayer;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.MultiplayerGame;
import nl.rug.oop.rts.protocol.user.User;

public class GamesManager {
    @Getter
    private java.util.Map<UUID, MultiplayerLobby> lobbies = new HashMap<>();

    @Getter
    private java.util.Map<UUID, MultiplayerGame> games = new HashMap<>();

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
    public MultiplayerGame createGame(MultiplayerLobby lobby, User otherUser, SocketConnection otherUserConnection) {
        UUID gameId = UUID.randomUUID();

        GamePlayer player = new GamePlayer(lobby.getHost(), 100, Team.TEAM_A, null, lobby.getConnection());
        GamePlayer otherPlayer = new GamePlayer(otherUser, 100, Team.TEAM_B, null, otherUserConnection);

        MultiplayerGame game = new MultiplayerGame(gameId, lobby.getMap(), player, otherPlayer);
        game.initialize();

        games.put(gameId, game);
        lobbies.remove(lobby.getLobbyId());

        return game;
    }
}

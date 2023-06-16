package nl.rug.oop.rts.server.games;

import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import nl.rug.oop.rts.protocol.games.MultiplayerLobby;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.user.User;

public class GamesManager {
    @Getter
    private java.util.Map<UUID, MultiplayerLobby> lobbies = new HashMap<>();

    public MultiplayerLobby getByUser(User user) {
        for (MultiplayerLobby lobby : lobbies.values()) {
            if (lobby.getHost().getId() == user.getId()) {
                return lobby;
            }
        }
        return null;
    }

    public MultiplayerLobby createLobby(User user, String lobbyName, Map map, String mapName) {
        MultiplayerLobby lobby = new MultiplayerLobby(UUID.randomUUID(), map, mapName, lobbyName, user);
        lobbies.put(lobby.getLobbyId(), lobby);

        return lobby;
    }

    public void removeLobby(UUID lobbyId) {
        lobbies.remove(lobbyId);
    }
}

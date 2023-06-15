package nl.rug.oop.rts.server.games;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import nl.rug.oop.rts.protocol.games.MultiplayerLobby;

public class GamesManager {
    @Getter
    private Map<UUID, MultiplayerLobby> lobbies = new HashMap<>();
}

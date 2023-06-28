package nl.rug.oop.rts.server.handlers.games.lobby;

import java.io.IOException;
import java.util.UUID;

import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyDeletionRequest;
import nl.rug.oop.rts.server.games.GamesManager;

/**
 * This class handles a lobby deletion request. This is sent by the client 
 * when the host leaves the lobby.
 */
public class LobbyDeletionRequestHandler extends PacketListener<LobbyDeletionRequest> {

    private GamesManager gamesManager;

    /**
     * Create a new LobbyDeletionRequestHandler.
     * 
     * @param gamesManager - The games manager
     */
    public LobbyDeletionRequestHandler(GamesManager gamesManager) {
        super(LobbyDeletionRequest.class);

        this.gamesManager = gamesManager;
    }

    @Override
    protected boolean handlePacket(SocketConnection connection, LobbyDeletionRequest packet) throws IOException {
        UUID lobbyId = packet.getLobbyId();
        this.gamesManager.removeLobby(lobbyId);

        return true;
    }

}

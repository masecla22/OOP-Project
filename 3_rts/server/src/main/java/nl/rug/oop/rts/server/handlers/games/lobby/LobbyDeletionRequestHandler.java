package nl.rug.oop.rts.server.handlers.games.lobby;

import java.util.UUID;

import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyDeletionRequest;
import nl.rug.oop.rts.server.games.GamesManager;

public class LobbyDeletionRequestHandler extends PacketListener<LobbyDeletionRequest> {

    private GamesManager gamesManager;

    public LobbyDeletionRequestHandler(GamesManager gamesManager) {
        super(LobbyDeletionRequest.class);

        this.gamesManager = gamesManager;
    }

    @Override
    protected boolean handlePacket(SocketConnection connection, LobbyDeletionRequest packet) throws Exception {
        UUID lobbyId = packet.getLobbyId();
        this.gamesManager.removeLobby(lobbyId);

        return true;
    }

}

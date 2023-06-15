package nl.rug.oop.rts.server.handlers.games.lobby;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.games.MultiplayerLobby;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyListingRequest;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyListingResponse;
import nl.rug.oop.rts.server.games.GamesManager;

public class LobbyListingRequestHandler extends PacketListener<LobbyListingRequest> {
    private GamesManager gamesManager;

    public LobbyListingRequestHandler(GamesManager gamesManager) {
        super(LobbyListingRequest.class);
        this.gamesManager = gamesManager;
    }

    @Override
    protected boolean handlePacket(SocketConnection connection, LobbyListingRequest packet) throws IOException {
        List<MultiplayerLobby> lobbies = new ArrayList<>(gamesManager.getLobbies().values());
        LobbyListingResponse response = new LobbyListingResponse(lobbies);

        connection.sendPacket(response);
        return true;
    }
}

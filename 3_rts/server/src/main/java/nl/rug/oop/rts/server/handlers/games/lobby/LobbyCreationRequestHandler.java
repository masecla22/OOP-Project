package nl.rug.oop.rts.server.handlers.games.lobby;

import java.io.IOException;

import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.games.MultiplayerLobby;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyCreationRequest;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyCreationResponse;
import nl.rug.oop.rts.protocol.user.User;
import nl.rug.oop.rts.server.games.GamesManager;
import nl.rug.oop.rts.server.user.UserManager;

/**
 * This class handles a lobby creation request, and sends back a response with
 * the lobby.
 */
public class LobbyCreationRequestHandler extends PacketListener<LobbyCreationRequest> {

    private GamesManager gamesManager;
    private UserManager userManager;

    /**
     * Create a new LobbyCreationRequestHandler.
     * 
     * @param gamesManager - The games manager
     * @param userManager - The user manager
     */
    public LobbyCreationRequestHandler(GamesManager gamesManager, UserManager userManager) {
        super(LobbyCreationRequest.class);

        this.gamesManager = gamesManager;
        this.userManager = userManager;
    }

    @Override
    protected boolean handlePacket(SocketConnection connection, LobbyCreationRequest packet) throws IOException {
        User user = userManager.getUser(packet.getSessionToken());

        Map map = packet.getMap();
        map.removeAllArmies();

        MultiplayerLobby lobby = this.gamesManager.getByUser(user);
        if (lobby != null) {
            connection.sendPacket(new LobbyCreationResponse(false, null));
            return true;
        }

        String mapName = packet.getMapName();
        String lobbyName = packet.getName();

        MultiplayerLobby resultingLobby = this.gamesManager.createLobby(user, lobbyName, map, mapName, connection);
        connection.sendPacket(new LobbyCreationResponse(true, resultingLobby));
        return true;
    }
}

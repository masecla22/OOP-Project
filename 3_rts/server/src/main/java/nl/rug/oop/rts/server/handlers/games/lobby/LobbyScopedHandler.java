package nl.rug.oop.rts.server.handlers.games.lobby;

import java.io.IOException;
import java.util.UUID;

import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyScopedPacket;
import nl.rug.oop.rts.protocol.user.User;
import nl.rug.oop.rts.server.games.GamesManager;
import nl.rug.oop.rts.server.user.UserManager;

/**
 * This class handles a lobby scoped packet, and makes sure the lobby id
 * matches the user's lobby id.
 */
public class LobbyScopedHandler extends PacketListener<LobbyScopedPacket> {

    private UserManager userManager;
    private GamesManager gamesManager;

    /**
     * Create a new LobbyScopedHandler.
     * 
     * @param userManager  - The user manager
     * @param gamesManager - The games manager
     */
    public LobbyScopedHandler(UserManager userManager, GamesManager gamesManager) {
        super(LobbyScopedPacket.class);

        this.userManager = userManager;
        this.gamesManager = gamesManager;
    }

    @Override
    protected boolean handlePacket(SocketConnection connection, LobbyScopedPacket packet) throws IOException {
        // Make sure the game belongs to the user authenticated
        UUID authToken = packet.getSessionToken();
        UUID lobbyId = packet.getLobbyId();

        User user = userManager.getUser(authToken);
        if (user == null) {
            return false;
        }

        if (gamesManager.getByUser(user).getLobbyId().equals(lobbyId)) {
            return true;
        }

        return false;
    }

}

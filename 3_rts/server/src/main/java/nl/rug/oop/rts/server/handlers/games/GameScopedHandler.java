package nl.rug.oop.rts.server.handlers.games;

import java.io.IOException;

import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.MultiplayerGame;
import nl.rug.oop.rts.protocol.packet.definitions.game.GameScopedPacket;
import nl.rug.oop.rts.protocol.user.User;
import nl.rug.oop.rts.server.games.GamesManager;
import nl.rug.oop.rts.server.user.UserManager;

/**
 * This handler is responsible for handling game scoped packets.
 * It will make sure that the user sending them is part of the correct game
 * and will discard the packets if there is any mismatch. That way users
 * are only able to control games they are part of.
 */
public class GameScopedHandler extends PacketListener<GameScopedPacket> {

    private UserManager userManager;
    private GamesManager gamesManager;

    /**
     * Create a new GameScopedHandler.
     * 
     * @param userManager - The user manager
     * @param gamesManager - The games manager
     */
    public GameScopedHandler(UserManager userManager, GamesManager gamesManager) {
        super(GameScopedPacket.class);

        this.userManager = userManager;
        this.gamesManager = gamesManager;
    }

    @Override
    protected boolean handlePacket(SocketConnection connection, GameScopedPacket packet) throws IOException {
        // Get the user from the connection
        User user = userManager.getUser(packet.getSessionToken());
        if (user == null) {
            return false;
        }

        // Get the game out of the packet
        MultiplayerGame game = gamesManager.getGame(packet.getGameId());
        if (game == null) {
            return false;
        }

        // Make sure the user is one of the players
        if (!game.isPartOf(user)) {
            return false;
        }

        // Allow packet to go through
        return true;
    }
}

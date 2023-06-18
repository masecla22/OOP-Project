package nl.rug.oop.rts.server.handlers.authentication;

import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.AuthenticatedPacket;
import nl.rug.oop.rts.protocol.user.User;
import nl.rug.oop.rts.server.user.UserManager;

/**
 * This class handles an authenticated packet. It will ensure the session token
 * is valid, and if it is, it will pass the packet on to the next handler.
 */
public class AuthenticatedPacketHandler extends PacketListener<AuthenticatedPacket> {
    private UserManager userManager;

    /**
     * Create a new AuthenticatedPacketHandler.
     * 
     * @param userManager - The user manager
     */
    public AuthenticatedPacketHandler(UserManager userManager) {
        super(AuthenticatedPacket.class);
        this.userManager = userManager;
    }

    @Override
    protected boolean handlePacket(SocketConnection connection, AuthenticatedPacket packet) {
        User user = userManager.getUser(packet.getSessionToken());
        if (user == null) {
            return false;
        }
        return true;
    }

}

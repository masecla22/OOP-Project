package nl.rug.oop.rts.server.handlers.authentication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.register.RegistrationRequest;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.register.RegistrationResponse;
import nl.rug.oop.rts.protocol.user.User;
import nl.rug.oop.rts.server.user.UserManager;

/**
 * This class handles a registration request. This is sent by the client when
 * the user wants to register a new account.
 */
public class RegistrationHandler extends PacketListener<RegistrationRequest> {
    private UserManager userManager;

    /**
     * Create a new RegistrationHandler.
     * 
     * @param userManager - The user manager
     */
    public RegistrationHandler(UserManager userManager) {
        super(RegistrationRequest.class);
        this.userManager = userManager;
    }

    @Override
    protected boolean handlePacket(SocketConnection connection, RegistrationRequest packet) throws IOException {
        String username = packet.getUsername();
        String password = packet.getPassword();

        try {
            User registered = this.userManager.createUser(username, password);
            UUID token = this.userManager.login(connection, registered);
            UUID refreshToken = this.userManager.createRefreshToken(registered);

            RegistrationResponse response = new RegistrationResponse(true, null, token, refreshToken);
            connection.sendPacket(response);
        } catch (IllegalArgumentException | SQLException e) {
            RegistrationResponse response = new RegistrationResponse(false, e.getMessage(), null, null);
            connection.sendPacket(response);
        }

        return true;
    }
}

package nl.rug.oop.rts.server.handlers.authentication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.login.LoginRequest;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.login.LoginResponse;
import nl.rug.oop.rts.protocol.user.User;
import nl.rug.oop.rts.server.user.UserManager;

/**
 * This class handles a login request. This is sent by the client when the user
 * wants to login.
 */
public class LoginHandler extends PacketListener<LoginRequest> {

    private UserManager userManager;

    /**
     * Create a new LoginHandler.
     * 
     * @param userManager - The user manager
     */
    public LoginHandler(UserManager userManager) {
        super(LoginRequest.class);
        this.userManager = userManager;
    }

    @Override
    protected boolean handlePacket(SocketConnection connection, LoginRequest packet) throws IOException {
        String username = packet.getUsername();
        String password = packet.getPassword();

        try {
            UUID token = this.userManager.login(username, password);
            if (token == null) {
                LoginResponse response = new LoginResponse(false, "Invalid Credentials", null, null, null);
                connection.sendPacket(response);
                return true;
            }

            User user = this.userManager.getUser(token);
            UUID refreshToken = this.userManager.createRefreshToken(user);

            LoginResponse response = new LoginResponse(true, null, token, refreshToken, user);
            connection.sendPacket(response);
        } catch (IllegalArgumentException | SQLException e) {
            LoginResponse response = new LoginResponse(false, e.getMessage(), null, null, null);
            connection.sendPacket(response);
        }

        return true;
    }

}

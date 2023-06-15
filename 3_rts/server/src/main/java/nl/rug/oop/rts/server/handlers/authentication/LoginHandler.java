package nl.rug.oop.rts.server.handlers.authentication;

import java.sql.SQLException;
import java.util.UUID;

import lombok.SneakyThrows;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.login.LoginRequest;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.login.LoginResponse;
import nl.rug.oop.rts.protocol.user.User;
import nl.rug.oop.rts.server.user.UserManager;

public class LoginHandler extends PacketListener<LoginRequest> {

    private UserManager userManager;

    public LoginHandler(UserManager userManager) {
        super(LoginRequest.class);
        this.userManager = userManager;
    }

    @Override
    @SneakyThrows
    protected boolean handlePacket(SocketConnection connection, LoginRequest packet) {
        String username = packet.getUsername();
        String password = packet.getPassword();

        try {
            UUID token = this.userManager.login(username, password);
            if (token == null) {
                LoginResponse response = new LoginResponse(false, "Invalid Credentials", null, null);
                connection.sendPacket(response);
                return true;
            }

            User user = this.userManager.getUser(token);
            LoginResponse response = new LoginResponse(true, null, token, user);
            connection.sendPacket(response);
        } catch (IllegalArgumentException | SQLException e) {
            LoginResponse response = new LoginResponse(false, e.getMessage(), null, null);
            connection.sendPacket(response);
        }

        return true;
    }

}

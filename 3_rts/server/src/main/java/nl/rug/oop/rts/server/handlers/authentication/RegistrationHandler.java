package nl.rug.oop.rts.server.handlers.authentication;

import java.util.UUID;

import lombok.SneakyThrows;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.register.RegistrationRequest;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.register.RegistrationResponse;
import nl.rug.oop.rts.protocol.user.User;
import nl.rug.oop.rts.server.user.UserManager;

public class RegistrationHandler extends PacketListener<RegistrationRequest> {
    private UserManager userManager;

    public RegistrationHandler(UserManager userManager) {
        super(RegistrationRequest.class);
        this.userManager = userManager;
    }

    @Override
    @SneakyThrows
    protected void handlePacket(SocketConnection connection, RegistrationRequest packet) {
        String username = packet.getUsername();
        String password = packet.getPassword();

        try {
            User registered = this.userManager.createUser(username, password);

            UUID token = this.userManager.login(registered);
            RegistrationResponse response = new RegistrationResponse(true, null, token);
            connection.sendPacket(response);
        } catch (IllegalArgumentException e) {
            RegistrationResponse response = new RegistrationResponse(false, e.getMessage(), null);
            connection.sendPacket(response);
        }
    }
}

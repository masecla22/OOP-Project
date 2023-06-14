package nl.rug.oop.rts.server.handlers;

import lombok.AllArgsConstructor;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.server.handlers.authentication.LoginHandler;
import nl.rug.oop.rts.server.handlers.authentication.RegistrationHandler;
import nl.rug.oop.rts.server.main.RTSServer;

@AllArgsConstructor
public class HandlerBinder {
    private RTSServer server;
    private SocketConnection connection;

    public void bind() {
        this.registerAuthenticationListeners();
    }

    private void registerAuthenticationListeners() {
        connection.addListener(new LoginHandler(server.getUserManager()));
        connection.addListener(new RegistrationHandler(server.getUserManager()));
    }
}

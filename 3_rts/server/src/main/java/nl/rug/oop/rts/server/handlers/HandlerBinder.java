package nl.rug.oop.rts.server.handlers;

import lombok.AllArgsConstructor;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.server.games.GamesManager;
import nl.rug.oop.rts.server.handlers.authentication.AuthenticatedPacketHandler;
import nl.rug.oop.rts.server.handlers.authentication.LoginHandler;
import nl.rug.oop.rts.server.handlers.authentication.RegistrationHandler;
import nl.rug.oop.rts.server.handlers.games.lobby.LobbyCreationRequestHandler;
import nl.rug.oop.rts.server.handlers.games.lobby.LobbyDeletionRequestHandler;
import nl.rug.oop.rts.server.handlers.games.lobby.LobbyListingRequestHandler;
import nl.rug.oop.rts.server.handlers.games.lobby.LobbyScopedHandler;
import nl.rug.oop.rts.server.main.RTSServer;
import nl.rug.oop.rts.server.user.UserManager;

@AllArgsConstructor
public class HandlerBinder {
    private RTSServer server;
    private SocketConnection connection;

    public void bind() {
        this.registerAuthenticationListeners();
    }

    private void registerAuthenticationListeners() {
        UserManager userManager = server.getUserManager();
        GamesManager gamesManager = server.getGamesManager();

        connection.addListener(new LoginHandler(userManager));
        connection.addListener(new RegistrationHandler(userManager));

        connection.addListener(new AuthenticatedPacketHandler(userManager));

        connection.addListener(new LobbyScopedHandler(userManager, gamesManager));
        connection.addListener(new LobbyListingRequestHandler(gamesManager));
        connection.addListener(new LobbyCreationRequestHandler(gamesManager, userManager));
        connection.addListener(new LobbyDeletionRequestHandler(gamesManager));
    }
}

package nl.rug.oop.rts.server.handlers.authentication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.tokens.TokenRefreshingRequest;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.tokens.TokenRefreshingResponse;
import nl.rug.oop.rts.protocol.user.User;
import nl.rug.oop.rts.server.user.UserManager;

/**
 * This class handles all requests for refreshing tokens.
 */
public class RefreshTokenRequestHandler extends PacketListener<TokenRefreshingRequest> {

    private UserManager userManager;

    public RefreshTokenRequestHandler(UserManager userManager) {
        super(TokenRefreshingRequest.class);
        this.userManager = userManager;
    }

    @Override
    protected boolean handlePacket(SocketConnection connection, TokenRefreshingRequest packet)
            throws IOException, SQLException {
        UUID sessionToken = userManager.login(connection, packet.getRefreshToken());
        User user = userManager.getUser(sessionToken);

        if (sessionToken == null) {
            TokenRefreshingResponse response = new TokenRefreshingResponse(false, null, null);
            connection.sendPacket(response);
            return true;
        }

        TokenRefreshingResponse response = new TokenRefreshingResponse(true, sessionToken, user);
        connection.sendPacket(response);
        return true;
    }

}

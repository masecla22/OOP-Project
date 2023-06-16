package nl.rug.oop.rts.server.handlers.games.lobby;

import java.io.IOException;

import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.games.MultiplayerLobby;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.objects.model.armies.Team;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.MultiplayerGame;
import nl.rug.oop.rts.protocol.packet.definitions.game.GameStartPacket;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyJoiningRequest;
import nl.rug.oop.rts.protocol.user.User;
import nl.rug.oop.rts.server.games.GamesManager;
import nl.rug.oop.rts.server.user.UserManager;

public class LobbyJoinRequestHandler extends PacketListener<LobbyJoiningRequest> {

    private GamesManager gamesManager;
    private UserManager userManager;

    public LobbyJoinRequestHandler(GamesManager gamesManager, UserManager userManager) {
        super(LobbyJoiningRequest.class);

        this.gamesManager = gamesManager;
        this.userManager = userManager;
    }

    @Override
    protected boolean handlePacket(SocketConnection connection, LobbyJoiningRequest packet) throws IOException {
        User user = userManager.getUser(packet.getSessionToken());
        if (user == null) {
            sendFailurePacket(connection);
            return false;
        }

        MultiplayerLobby lobby = gamesManager.getLobby(packet.getLobbyId());
        if (lobby == null) {
            sendFailurePacket(connection);
            return false;
        }

        if (gamesManager.canJoin(lobby, user)) {
            MultiplayerGame game = gamesManager.createGame(lobby, user, connection);
            game.initialize();

            // Notify both players of the game starting and which team they are in
            GameStartPacket gamePacket = new GameStartPacket(true, game, Team.TEAM_B);
            connection.sendPacket(gamePacket);

            GameStartPacket otherPacket = new GameStartPacket(true, game, Team.TEAM_A);
            game.getPlayerA().getConnection().sendPacket(otherPacket);

            return true;
        } else {
            sendFailurePacket(connection);
            return false;
        }
    }

    private void sendFailurePacket(SocketConnection connection) throws IOException {
        connection.sendPacket(new GameStartPacket(false, null, null));
    }

}

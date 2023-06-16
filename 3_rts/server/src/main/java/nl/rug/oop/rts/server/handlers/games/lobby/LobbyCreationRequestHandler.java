package nl.rug.oop.rts.server.handlers.games.lobby;

import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.games.MultiplayerLobby;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.objects.model.Edge;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.objects.model.Node;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyCreationRequest;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyCreationResponse;
import nl.rug.oop.rts.protocol.user.User;
import nl.rug.oop.rts.server.games.GamesManager;
import nl.rug.oop.rts.server.user.UserManager;

public class LobbyCreationRequestHandler extends PacketListener<LobbyCreationRequest> {

    private GamesManager gamesManager;
    private UserManager userManager;

    public LobbyCreationRequestHandler(GamesManager gamesManager, UserManager userManager) {
        super(LobbyCreationRequest.class);

        this.gamesManager = gamesManager;
        this.userManager = userManager;
    }

    @Override
    protected boolean handlePacket(SocketConnection connection, LobbyCreationRequest packet) throws Exception {
        User user = userManager.getUser(packet.getSessionToken());

        Map map = packet.getMap();
        purgeMap(map);

        MultiplayerLobby lobby = this.gamesManager.getByUser(user);
        if (lobby != null) {
            connection.sendPacket(new LobbyCreationResponse(false, null));
            return true;
        }

        String mapName = packet.getMapName();
        String lobbyName = packet.getName();

        MultiplayerLobby resultingLobby = this.gamesManager.createLobby(user, lobbyName, map, mapName);
        connection.sendPacket(new LobbyCreationResponse(true, resultingLobby));
        return true;
    }

    private void purgeMap(Map map) {
        for (Node cr : map.getNodes())
            cr.getArmies().clear();
        for (Edge edge : map.getEdges())
            edge.getArmies().clear();
    }
}

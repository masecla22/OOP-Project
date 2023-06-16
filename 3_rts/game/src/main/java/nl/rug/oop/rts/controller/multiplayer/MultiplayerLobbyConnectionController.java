package nl.rug.oop.rts.controller.multiplayer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.games.MultiplayerLobby;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyScopedPacket;

@AllArgsConstructor
public class MultiplayerLobbyConnectionController {
    @Getter
    private MultiplayerConnectionController connectionController;
    private MultiplayerLobby lobby;

    public void sendLobbyPacket(LobbyScopedPacket packet) {
        packet.setLobbyId(lobby.getLobbyId());
        connectionController.sendAuthenticatedPacket(packet);
    }

    public SocketConnection getConnection() {
        return connectionController.getConnection();
    }
}

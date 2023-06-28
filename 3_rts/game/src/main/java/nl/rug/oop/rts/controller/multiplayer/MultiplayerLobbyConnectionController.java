package nl.rug.oop.rts.controller.multiplayer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.games.MultiplayerLobby;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyScopedPacket;

/**
 * This class is used to send packets to a specific lobby.
 */
@AllArgsConstructor
public class MultiplayerLobbyConnectionController {
    @Getter
    private MultiplayerConnectionController connectionController;
    private MultiplayerLobby lobby;

    /**
     * Sends a packet to the lobby. It will automatically set the lobby id.
     * 
     * @param packet - The packet to send.
     */
    public void sendLobbyPacket(LobbyScopedPacket packet) {
        packet.setLobbyId(lobby.getLobbyId());
        connectionController.sendAuthenticatedPacket(packet);
    }

    /**
     * Gets the connection of the user.
     * 
     * @return - The connection.
     */
    public SocketConnection getConnection() {
        return connectionController.getConnection();
    }
}

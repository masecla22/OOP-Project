package nl.rug.oop.rts.protocol.packet.definitions.game.lobby;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.games.MultiplayerLobby;
import nl.rug.oop.rts.protocol.packet.Packet;

/**
 * Response to a lobby creation request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LobbyCreationResponse extends Packet {
    private boolean success;

    private MultiplayerLobby lobby;
}

package nl.rug.oop.rts.protocol.packet.definitions.game.lobby;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.games.MultiplayerLobby;
import nl.rug.oop.rts.protocol.packet.Packet;

/**
 * Response to a lobby listing request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LobbyListingResponse extends Packet {
    private List<MultiplayerLobby> lobbies;
}

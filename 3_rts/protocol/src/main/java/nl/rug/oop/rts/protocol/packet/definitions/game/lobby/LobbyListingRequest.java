package nl.rug.oop.rts.protocol.packet.definitions.game.lobby;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.AuthenticatedPacket;

/**
 * Request a list of lobbies.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LobbyListingRequest extends AuthenticatedPacket {
}

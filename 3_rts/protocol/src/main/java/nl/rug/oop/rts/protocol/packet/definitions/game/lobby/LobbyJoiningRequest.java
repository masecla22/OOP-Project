package nl.rug.oop.rts.protocol.packet.definitions.game.lobby;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.AuthenticatedPacket;

/**
 * Request to join a lobby.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LobbyJoiningRequest extends AuthenticatedPacket {
    private UUID lobbyId;
}

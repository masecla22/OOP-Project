package nl.rug.oop.rts.protocol.packet.definitions.game.lobby;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.AuthenticatedPacket;

/**
 * This packet is sent by the client to the server in the context of a lobby.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LobbyScopedPacket extends AuthenticatedPacket {
    private UUID lobbyId;

}

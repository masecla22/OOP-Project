package nl.rug.oop.rts.protocol.packet.definitions.game;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.AuthenticatedPacket;

/**
 * This packet is sent by the client to the server with data about the game.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GameScopedPacket extends AuthenticatedPacket {
    private UUID gameId;
}

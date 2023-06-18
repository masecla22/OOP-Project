package nl.rug.oop.rts.protocol.packet.definitions.game.lobby;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Request to delete a lobby.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LobbyDeletionRequest extends LobbyScopedPacket{
}

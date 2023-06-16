package nl.rug.oop.rts.protocol.packet.definitions.game.lobby;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LobbyDeletionRequest extends LobbyScopedPacket{
}

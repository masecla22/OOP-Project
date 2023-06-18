package nl.rug.oop.rts.protocol.packet.definitions.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.MultiplayerGame;
import nl.rug.oop.rts.protocol.packet.Packet;

/**
 * This packet is sent by the server to the client to update the game state.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GameUpdatePacket extends Packet {
    private MultiplayerGame currentGame;
}

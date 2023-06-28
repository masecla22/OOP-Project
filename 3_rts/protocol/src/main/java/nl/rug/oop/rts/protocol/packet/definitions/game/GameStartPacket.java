package nl.rug.oop.rts.protocol.packet.definitions.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.objects.model.armies.Team;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.MultiplayerGame;
import nl.rug.oop.rts.protocol.packet.Packet;

/**
 * This packet is sent by the server to the client to signal that the game has
 * started.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GameStartPacket extends Packet {
    private boolean success;

    private MultiplayerGame game;

    private Team team; // Used to signal to the client which team they are in
}

package nl.rug.oop.rts.protocol.packet.definitions.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.objects.model.armies.Team;
import nl.rug.oop.rts.protocol.packet.Packet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GameEndPacket extends Packet {
    private Team winner;    
}

package nl.rug.oop.rts.protocol.packet.definitions.game.changes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.packet.Packet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GameChangeListConfirm extends Packet {
    private boolean accepted;
}

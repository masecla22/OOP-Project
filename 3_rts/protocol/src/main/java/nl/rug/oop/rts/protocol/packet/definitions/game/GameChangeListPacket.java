package nl.rug.oop.rts.protocol.packet.definitions.game;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.GameChange;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GameChangeListPacket extends GameScopedPacket {
    private List<GameChange> changes;
}

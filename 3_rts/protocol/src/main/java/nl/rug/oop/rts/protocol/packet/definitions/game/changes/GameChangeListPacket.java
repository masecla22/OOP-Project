package nl.rug.oop.rts.protocol.packet.definitions.game.changes;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.GameChange;
import nl.rug.oop.rts.protocol.packet.definitions.game.GameScopedPacket;

/**
 * List of changes to the game.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GameChangeListPacket extends GameScopedPacket {
    private List<GameChange> changes;
}

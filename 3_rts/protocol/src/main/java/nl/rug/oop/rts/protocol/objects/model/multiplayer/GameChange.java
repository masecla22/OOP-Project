package nl.rug.oop.rts.protocol.objects.model.multiplayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.objects.model.Node;
import nl.rug.oop.rts.protocol.objects.model.armies.Faction;

/**
 * Represents a change in the game state.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameChange {
    private int nodeId;

    private Faction faction;

    public GameChange(Node node, Faction faction) {
        this.nodeId = node.getId();
        this.faction = faction;
    }
}

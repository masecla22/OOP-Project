package nl.rug.oop.rts.protocol.objects.model.multiplayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.objects.model.Node;
import nl.rug.oop.rts.protocol.objects.model.armies.Faction;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameChange {
    private Node node;
    private Faction factionAdded;
}

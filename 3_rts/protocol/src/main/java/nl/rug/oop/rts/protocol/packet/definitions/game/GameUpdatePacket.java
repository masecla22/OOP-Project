package nl.rug.oop.rts.protocol.packet.definitions.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.objects.model.armies.Team;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GameUpdatePacket extends GameScopedPacket {
    private Team nextTurn;

    private boolean gameOver;
    private Team winner;
}

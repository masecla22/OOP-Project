package nl.rug.oop.rts.protocol.packet.definitions.game.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.AuthenticatedPacket;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LeaderboardRequest extends AuthenticatedPacket {
}

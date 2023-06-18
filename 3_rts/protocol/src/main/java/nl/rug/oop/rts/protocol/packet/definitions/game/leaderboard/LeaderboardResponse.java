package nl.rug.oop.rts.protocol.packet.definitions.game.leaderboard;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.packet.Packet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LeaderboardResponse extends Packet {
    private List<String> usernames;
    private List<Integer> scores;
}

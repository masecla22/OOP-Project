package nl.rug.oop.rts.protocol.packet.definitions.game.leaderboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

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

    public LeaderboardResponse(List<Entry<String, Integer>> leaderboard) {
        usernames = new ArrayList<>();
        scores = new ArrayList<>();

        for (Entry<String, Integer> entry : leaderboard) {
            usernames.add(entry.getKey());
            scores.add(entry.getValue());
        }
    }
}

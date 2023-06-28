package nl.rug.oop.rts.protocol.objects.model.multiplayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.objects.model.Node;
import nl.rug.oop.rts.protocol.objects.model.armies.Team;
import nl.rug.oop.rts.protocol.user.User;

/**
 * Represents a player in a multiplayer game.
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class GamePlayer {
    @NonNull
    private User user;
    private int gold = 0;

    private Team team;
    private Node startingNode;

    private transient SocketConnection connection;
}

package nl.rug.oop.rts.protocol.games;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiplayerLobby {
    private UUID lobbyId;

    private Map map;
    private String mapName;

    private String name;

    private User host;

    private transient SocketConnection connection;
}

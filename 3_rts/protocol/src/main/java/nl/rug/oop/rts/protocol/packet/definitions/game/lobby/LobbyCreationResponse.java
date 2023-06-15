package nl.rug.oop.rts.protocol.packet.definitions.game.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.games.MultiplayerLobby;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.AuthenticatedPacket;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LobbyCreationResponse extends AuthenticatedPacket {
    private String name;
    private MultiplayerLobby lobby;
}

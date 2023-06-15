package nl.rug.oop.rts.protocol.packet.definitions.game.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.AuthenticatedPacket;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LobbyCreationRequest extends AuthenticatedPacket {
    private String name;
    private Map map;
}

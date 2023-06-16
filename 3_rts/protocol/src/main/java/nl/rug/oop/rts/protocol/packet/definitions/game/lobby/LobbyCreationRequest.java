package nl.rug.oop.rts.protocol.packet.definitions.game.lobby;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.AuthenticatedPacket;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LobbyCreationRequest extends AuthenticatedPacket {
    private String name;
    private String mapName;
    
    private Map map;
}

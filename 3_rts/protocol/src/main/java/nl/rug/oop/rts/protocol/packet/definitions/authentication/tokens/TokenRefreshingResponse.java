package nl.rug.oop.rts.protocol.packet.definitions.authentication.tokens;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.packet.Packet;
import nl.rug.oop.rts.protocol.user.User;

/**
 * This packet is used to respond to a token refreshing request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TokenRefreshingResponse extends Packet {
    private boolean success;

    private UUID token;
    private User user;
}

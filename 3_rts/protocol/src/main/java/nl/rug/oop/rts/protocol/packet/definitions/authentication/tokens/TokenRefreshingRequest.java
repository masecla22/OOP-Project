package nl.rug.oop.rts.protocol.packet.definitions.authentication.tokens;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.packet.Packet;

/**
 * This packet is used to request a new token.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TokenRefreshingRequest extends Packet {
    private UUID refreshToken;
}

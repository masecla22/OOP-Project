package nl.rug.oop.rts.protocol.packet.definitions.authentication;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.packet.Packet;

/**
 * Packet that requires authentication.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthenticatedPacket extends Packet {
    private UUID sessionToken;
}

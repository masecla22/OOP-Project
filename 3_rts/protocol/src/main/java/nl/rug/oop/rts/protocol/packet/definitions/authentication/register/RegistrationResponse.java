package nl.rug.oop.rts.protocol.packet.definitions.authentication.register;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.packet.Packet;

/**
 * Response to a registration request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RegistrationResponse extends Packet {
    private boolean success;
    private String error;

    private UUID token;
    private UUID refreshToken;
}

package nl.rug.oop.rts.protocol.packet.definitions.authentication.login;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.protocol.packet.Packet;
import nl.rug.oop.rts.protocol.user.User;

/**
 * Response to a login request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoginResponse extends Packet {
    private boolean success;
    private String error;

    private UUID token;
    private User user;
}

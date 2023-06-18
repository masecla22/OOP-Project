package nl.rug.oop.rts.protocol.packet.definitions.keepalive;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.rug.oop.rts.protocol.packet.Packet;

/**
 * This packet is sent by the client to the server and vice versa to keep the
 * connection alive. Doesn't contain any data.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KeepAlivePacket extends Packet {
}

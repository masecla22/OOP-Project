package nl.rug.oop.rts.protocol.packet.definitions.keepalive;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.rug.oop.rts.protocol.packet.Packet;

@Data
@EqualsAndHashCode(callSuper = true)
public class KeepAlivePacket extends Packet {
}

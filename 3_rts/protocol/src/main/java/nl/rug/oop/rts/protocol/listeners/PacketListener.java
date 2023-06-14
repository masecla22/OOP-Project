package nl.rug.oop.rts.protocol.listeners;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.packet.Packet;

@RequiredArgsConstructor
public abstract class PacketListener<K> {
    @Getter
    @NonNull
    private Class<?> packetClass;

    @SuppressWarnings("unchecked")
    public void onReceive(SocketConnection connection, Packet packet) {
        if (packetClass.isInstance(packet)) {
            this.handlePacket(connection, (K) packetClass.cast(packet));
        } else {
            throw new IllegalArgumentException("Packet is not of type " + packetClass.getName());
        }
    }

    protected abstract void handlePacket(SocketConnection connection, K packet);
}

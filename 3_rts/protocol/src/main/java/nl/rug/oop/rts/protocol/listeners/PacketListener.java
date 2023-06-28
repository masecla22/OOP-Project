package nl.rug.oop.rts.protocol.listeners;

import java.io.IOException;
import java.sql.SQLException;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.packet.Packet;

/**
 * A packet listener is a class that listens for a specific packet type.
 * 
 * @param <K> - The type of packet that this listener listens for.
 */
@RequiredArgsConstructor
public abstract class PacketListener<K> {
    @Getter
    @NonNull
    private Class<?> packetClass;

    /**
     * Handles a packet if it is of the correct type.
     * 
     * @param connection - The connection that received the packet.
     * @param packet - The packet that was received.
     * @return - Whether the packet should continue to be handled by other listeners.
     * @throws Exception - If the packet could not be handled.
     */
    @SuppressWarnings("unchecked")
    public boolean onReceive(SocketConnection connection, Packet packet) throws IOException, SQLException {
        if (packetClass.isInstance(packet)) {
            return this.handlePacket(connection, (K) packetClass.cast(packet));
        } else {
            throw new IllegalArgumentException("Packet is not of type " + packetClass.getName());
        }
    }

    protected abstract boolean handlePacket(SocketConnection connection, K packet) throws IOException, SQLException;
}

package nl.rug.oop.rts.protocol.listeners;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.packet.Packet;

/**
 * This is a listener that binds to a connection and await for a packet once.
 * The listener unbinds itself from the connection once a T packet is received
 * that matches all {@link #isValid(Predicate)}.
 * 
 * @param <T> - The type of packet to look for.
 */

@RequiredArgsConstructor
public class AwaitPacketOnce<T extends Packet> {
    @NonNull
    private Class<?> packetWaiting;

    private SocketConnection boundTo;

    @Getter
    private CompletableFuture<Map.Entry<SocketConnection, T>> awaiting;
    private List<Predicate<T>> isValid = new ArrayList<>();

    /**
     * Binds to the given connection waits for the packet. The binding should happen
     * before sending the packet as binding afterwards causes the possibility of
     * losing the response to the packet.
     * 
     * @param endpoint - The connection to bind to
     * @return - The listener
     */
    public AwaitPacketOnce<T> bindTo(SocketConnection endpoint) {
        this.boundTo = endpoint;
        this.awaiting = new CompletableFuture<>();
        this.boundTo.addListener(new PacketListener<T>(packetWaiting) {
            @Override
            protected void handlePacket(SocketConnection endpoint, T packet) {
                // Make sure the packet class matches
                // Theoretically, this should already be handled by the PacketListener
                // but this is a safety check
                if (packet.getClass().equals(packetWaiting)) {
                    // Make sure the packet is valid
                    if (isValid.size() == 0 || isValid.stream().allMatch(c -> c.test(packet))) {
                        // Unbind from the connection
                        boundTo.removeListener(this);
                        // Complete the future
                        awaiting.complete(new SimpleEntry<>(boundTo, packet));
                    }
                }
            }

        });
        return this;
    }

    public AwaitPacketOnce<T> isValid(Predicate<T> predicate) {
        this.isValid.add(predicate);
        return this;
    }

    public Map.Entry<SocketConnection, T> await() {
        return this.awaiting.join();
    }
}

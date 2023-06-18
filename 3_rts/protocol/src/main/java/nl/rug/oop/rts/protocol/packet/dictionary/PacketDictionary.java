package nl.rug.oop.rts.protocol.packet.dictionary;

import java.util.HashMap;
import java.util.Map;

import nl.rug.oop.rts.protocol.packet.Packet;
import nl.rug.oop.rts.protocol.packet.definitions.keepalive.KeepAlivePacket;

/**
 * This class is used to map packet classes to packet ids and vice versa.
 */
public class PacketDictionary {
    private Map<Integer, Class<? extends Packet>> packetDictionary = new HashMap<>();
    private Map<Class<? extends Packet>, Integer> packetIdDictionary = new HashMap<>();

    /**
     * Create a new packet dictionary. Will register the keep alive packet.
     */
    public PacketDictionary() {
        this.registerPacket(KeepAlivePacket.class);
    }

    /**
     * Registers the next packet id for the given packet class.
     * 
     * @param packetClass - the packet class
     */
    public void registerPacket(Class<? extends Packet> packetClass) {
        int nextPacketId = packetDictionary.size();
        this.registerPacket(nextPacketId, packetClass);
    }

    /**
     * Registers the given packet id for the given packet class.
     * 
     * @param packetId    - the packet id
     * @param packetClass - the packet class
     */
    public void registerPacket(int packetId, Class<? extends Packet> packetClass) {
        packetDictionary.put(packetId, packetClass);
        packetIdDictionary.put(packetClass, packetId);
    }

    /**
     * Returns the packet class for the given packet id.
     * 
     * @param packetId - the packet id
     * @return - the packet class
     */
    public Class<? extends Packet> getPacketClass(int packetId) {
        return packetDictionary.get(packetId);
    }

    /**
     * Returns the packet id for the given packet class.
     * 
     * @param packetClass - the packet class
     * @return - the packet id
     */
    public int getPacketId(Class<? extends Packet> packetClass) {
        Integer result = packetIdDictionary.get(packetClass);
        if (result == null) {
            throw new IllegalArgumentException("Packet class " + packetClass.getName() + " is not registered");
        }
        return result;
    }

    /**
     * Returns the packet id for the given packet object.
     * 
     * @param object - the packet object
     * @return - the packet id
     */
    public int getPacketId(Packet object) {
        return this.getPacketId(object.getClass());
    }
}

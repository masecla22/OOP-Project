package nl.rug.oop.rts.protocol.packet.dictionary;

import java.util.HashMap;
import java.util.Map;

import nl.rug.oop.rts.protocol.packet.Packet;
import nl.rug.oop.rts.protocol.packet.definitions.keepalive.KeepAlivePacket;

public class PacketDictionary {
    private Map<Integer, Class<? extends Packet>> packetDictionary = new HashMap<>();
    private Map<Class<? extends Packet>, Integer> packetIdDictionary = new HashMap<>();

    public PacketDictionary() {
        this.registerPacket(KeepAlivePacket.class);
    }

    public void registerPacket(Class<? extends Packet> packetClass) {
        int nextPacketId = packetDictionary.size();
        this.registerPacket(nextPacketId, packetClass);
    }

    public void registerPacket(int packetId, Class<? extends Packet> packetClass) {
        packetDictionary.put(packetId, packetClass);
        packetIdDictionary.put(packetClass, packetId);
    }

    public Class<? extends Packet> getPacketClass(int packetId) {
        return packetDictionary.get(packetId);
    }

    public int getPacketId(Class<? extends Packet> packetClass) {
        Integer result = packetIdDictionary.get(packetClass);
        if (result == null) {
            throw new IllegalArgumentException("Packet class " + packetClass.getName() + " is not registered");
        }
        return result;
    }

    public int getPacketId(Packet object) {
        return this.getPacketId(object.getClass());
    }
}

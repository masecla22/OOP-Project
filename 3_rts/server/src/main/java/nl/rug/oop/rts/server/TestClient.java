package nl.rug.oop.rts.server;

import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.SneakyThrows;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.packet.definitions.keepalive.KeepAlivePacket;
import nl.rug.oop.rts.protocol.packet.dictionary.PacketDictionary;
import nl.rug.oop.rts.server.TestServer.SomeExtendingPacket;
import nl.rug.oop.rts.server.TestServer.SomePacket;
import nl.rug.oop.rugson.Rugson;

public class TestClient {
    @SneakyThrows
    public static void main(String[] args) {
        System.out.println("running client");

        Socket socket = new Socket("localhost", 12345);

        Rugson rugson = new Rugson();
        PacketDictionary dictionary = new PacketDictionary();
        dictionary.registerPacket(SomePacket.class);
        dictionary.registerPacket(SomeExtendingPacket.class);

        ExecutorService service = Executors.newCachedThreadPool();

        SocketConnection connection = new SocketConnection(rugson, socket, service, dictionary);
        connection.addListener(new DebugListener());

        connection.addListener(new PacketListener<KeepAlivePacket>(KeepAlivePacket.class) {
            @Override
            protected boolean handlePacket(SocketConnection connection, KeepAlivePacket packet) {
                System.out.println("Received keep alive packet");
                return true;
            }
        });

        int i = 0;
        while (true) {
            SomeExtendingPacket pck = new SomeExtendingPacket("iuytgfiuytf");
            pck.setAge(i);
            pck.setName(UUID.randomUUID() + "");

            connection.sendPacket(pck);
            i++;

            Thread.sleep(5000);
        }
    }

    public static class DebugListener extends PacketListener<SomePacket> {
        public DebugListener() {
            super(SomePacket.class);
        }

        @Override
        protected boolean handlePacket(SocketConnection connection, SomePacket packet) {
            System.out.println("Received packet: " + packet);
            return true;
        }
    }
}

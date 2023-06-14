package nl.rug.oop.rts.server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.packet.dictionary.PacketDictionary;
import nl.rug.oop.rugson.Rugson;

@RequiredArgsConstructor
public class ConnectionManager {
    @NonNull
    private ExecutorService threadPool;
    @NonNull
    private Rugson rugson;

    @NonNull
    private PacketDictionary packetDictionary;

    private ServerSocket socket;

    private AtomicBoolean running = new AtomicBoolean(false);

    private List<Consumer<SocketConnection>> connectionHandlers = new ArrayList<>();
    private List<SocketConnection> connections = new ArrayList<>();

    public void start(int port) throws IOException {
        this.socket = new ServerSocket(port);
        this.socket.setReuseAddress(true);

        running.set(true);
        threadPool.submit(this::handleConnections);
    }

    public void addConnectionHandler(Consumer<SocketConnection> handler) {
        connectionHandlers.add(handler);
    }

    public void stop() throws IOException, InterruptedException {
        running.set(false);

        // Close all connections
        for (SocketConnection connection : connections)
            connection.closeConnection();

        socket.close();
    }

    private void handleConnections() {
        while (running.get()) {
            try {
                Socket client = socket.accept();
                this.handleConnectionOpen(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleConnectionOpen(Socket client) {
        // Wrap connection in a SocketConnection
        SocketConnection connection = new SocketConnection(rugson, client, threadPool, packetDictionary);

        // Add connection to list
        connections.add(connection);

        // Handle connection
        connectionHandlers.forEach(handler -> handler.accept(connection));
    }
}

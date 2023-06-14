package nl.rug.oop.rts.server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConnectionManager {
    @NonNull
    private ExecutorService threadPool;
    private ServerSocket socket;

    private AtomicBoolean running = new AtomicBoolean(false);

    public void start() {
        running.set(true);
        threadPool.submit(this::handleConnections);
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

    }

    public void stop() throws IOException {
        running.set(false);
        socket.close();
    }
}

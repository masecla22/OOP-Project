package nl.rug.oop.rts.server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.packet.dictionary.PacketDictionary;
import nl.rug.oop.rugson.Rugson;

/**
 * This class manages all connections to the server.
 */
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

    private KeyPair rsaKey;

    /**
     * Starts the connection manager.
     * 
     * @param port - The port to listen on
     * @throws IOException - When the socket cannot be created
     */
    public void start(int port) throws IOException {
        this.initializeRSA();

        this.socket = new ServerSocket(port);
        this.socket.setReuseAddress(true);

        running.set(true);
        threadPool.submit(this::handleConnections);

    }

    @SneakyThrows(NoSuchAlgorithmException.class)
    private void initializeRSA() {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = SecureRandom.getInstanceStrong();

        keyPairGenerator.initialize(2048, random);

        rsaKey = keyPairGenerator.generateKeyPair();
    }

    /**
     * Add a connection handler. This is called when a new connection is opened.
     * 
     * @param handler - The handler
     */
    public void addConnectionHandler(Consumer<SocketConnection> handler) {
        connectionHandlers.add(handler);
    }

    /**
     * Stop the connection manager.
     * 
     * @throws IOException          - When the socket cannot be closed
     * @throws InterruptedException - When the thread is interrupted
     */
    public void stop() throws IOException, InterruptedException {
        running.set(false);

        // Close all connections
        for (SocketConnection connection : connections) {
            connection.closeConnection();
        }

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

    @SneakyThrows
    private void handleConnectionOpen(Socket client) {
        // Wrap connection in a SocketConnection
        SocketConnection connection = new SocketConnection(rugson, client, threadPool, packetDictionary);

        try {
            serverSideEncryption(connection);
        } catch (IOException e) {
            e.printStackTrace();
            connection.closeConnection();
            return;
        }

        Thread.sleep(100);

        connection.initializeSending();

        // Add connection to list
        connections.add(connection);

        // Handle connection
        connectionHandlers.forEach(handler -> handler.accept(connection));
    }

    @SneakyThrows({ NoSuchAlgorithmException.class, NoSuchPaddingException.class, InvalidKeyException.class,
        BadPaddingException.class, IllegalBlockSizeException.class }) // Jesus christ checkstyle
    private void serverSideEncryption(SocketConnection connection) throws IOException {
        // The server sends out it's public RSA key
        // The client encrypts the AES key with the public RSA key
        // Then, the client sends the encrypted AES key to the server
        // The server decrypts the AES key with it's private RSA key
        // They now communicate exclusively encrypted with AES

        Socket socket = connection.getSocket();
        PublicKey publicKey = rsaKey.getPublic();

        // Send the public key
        connection.writeInt(socket.getOutputStream(), publicKey.getEncoded().length);
        socket.getOutputStream().write(publicKey.getEncoded());

        // Read the encrypted AES key
        byte[] encryptedData = connection.readRawKeyByte();

        // Decrypt the AES key
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, rsaKey.getPrivate());

        SecretKey aesKey = new SecretKeySpec(cipher.doFinal(encryptedData), "AES");

        // Set the AES key
        connection.setAesKey(aesKey);
    }
}

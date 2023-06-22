package nl.rug.oop.rts.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.packet.Packet;
import nl.rug.oop.rts.protocol.packet.definitions.keepalive.KeepAlivePacket;
import nl.rug.oop.rts.protocol.packet.dictionary.PacketDictionary;
import nl.rug.oop.rugson.Rugson;

/**
 * Represents a connection to a remote socket.
 */
public class SocketConnection {
    private static final int PACKET_MAX_SIZE = 1024 * 1024 * 16;

    @NonNull
    private Rugson rugson;

    @NonNull
    @Getter
    private Socket socket;

    @NonNull
    private ExecutorService executorService;

    @NonNull
    private PacketDictionary packetDictionary;

    @NonNull
    private Thread pollingThread;

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    private Map<Class<?>, List<PacketListener<?>>> packetListeners = new HashMap<>();

    private List<Consumer<SocketConnection>> closeListeners = new ArrayList<>();

    private Timer keepAliveSender = new Timer();

    @Setter
    private SecretKey aesKey;

    /**
     * Creates a new socker connection and starts polling for packets.
     * 
     * @param rugson           The Rugson instance
     * @param socket           The socket to connect to
     * @param executorService  The executor service to run the polling thread on
     * @param packetDictionary The packet dictionary to use
     */
    public SocketConnection(Rugson rugson, Socket socket, ExecutorService executorService,
            PacketDictionary packetDictionary) {
        this.rugson = rugson;
        this.socket = socket;
        this.executorService = executorService;
        this.packetDictionary = packetDictionary;

        this.isRunning.set(true);
    }

    /**
     * Initializes the polling thread and the keep-alive sender.
     */
    public void initializeSending() {
        this.pollingThread = new Thread(this::pollPackets);
        this.pollingThread.start();

        keepAliveSender.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    sendPacket(new KeepAlivePacket());
                } catch (IOException e) {
                    closeConnection();
                }
            }
        }, 100, 3000);
    }

    /**
     * Initializes a client-side AES key.
     */
    @SneakyThrows
    public void initializeAESKey() {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        this.aesKey = keyGen.generateKey();
    }

    /**
     * Establishes the encryption protocol with the server.
     * 
     * @throws IOException - When an IO error occurs
     */
    @SneakyThrows
    public void broadcastEncryptedAESKey() throws IOException {
        this.waitUntilBytesAvailable(4, 1000);

        int publicKeyLength = this.readInt(this.socket.getInputStream());

        ByteArrayOutputStream publicKeyStream = new ByteArrayOutputStream();
        readIntoBuffer(socket.getInputStream(), publicKeyStream, publicKeyLength, Instant.now().toEpochMilli(),
                4000);

        byte[] publicKeyBytes = publicKeyStream.toByteArray();
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = factory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        // Encrypt the AES key with the public key
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] encryptedKey = cipher.doFinal(this.aesKey.getEncoded());

        writeInt(this.socket.getOutputStream(), encryptedKey.length);
        this.socket.getOutputStream().write(encryptedKey);
    }

    /**
     * Adds a packet listener.
     * 
     * @param packetListener - The packet listener to add
     */
    public void addListener(PacketListener<? extends Packet> packetListener) {
        Class<?> packetClass = packetListener.getPacketClass();
        if (!this.packetListeners.containsKey(packetClass)) {
            this.packetListeners.put(packetClass, new ArrayList<>());
        }

        this.packetListeners.get(packetClass).add(packetListener);
    }

    /**
     * Removes the listener from the packet listener list.
     * 
     * @param packetListener - The packet listener to remove
     */
    public void removeListener(PacketListener<? extends Packet> packetListener) {
        Class<?> packetClass = packetListener.getPacketClass();
        if (!this.packetListeners.containsKey(packetClass)) {
            return;
        }

        this.packetListeners.get(packetClass).remove(packetListener);
        if (this.packetListeners.get(packetClass).isEmpty()) {
            this.packetListeners.remove(packetClass);
        }
    }

    /**
     * Adds a listener for when the connection closes.
     * 
     * @param closeListener - The listener to add
     */
    public void onConnectionClose(Consumer<SocketConnection> closeListener) {
        this.closeListeners.add(closeListener);
    }

    /**
     * Removes all listeners for a given class.
     * 
     * @param packetClass - The class to remove listeners for
     */
    public void removeListeners(Class<? extends Packet> packetClass) {
        this.packetListeners.remove(packetClass);
    }

    /**
     * Reads a 4-byte integer from the stream.
     * 
     * @param stream - The stream to read from
     * @return - The integer that was read
     * @throws IOException - When the integer could not be read
     */
    public int readInt(InputStream stream) throws IOException {
        byte[] bfs = new byte[4];
        stream.read(bfs);
        ByteBuffer bfr = ByteBuffer.wrap(bfs);
        bfr.order(ByteOrder.BIG_ENDIAN);
        return bfr.getInt();
    }

    /**
     * Writes a 4-byte integer to the stream.
     * 
     * @param stream - The stream to write to
     * @param i      - The integer to write
     * @throws IOException - When the integer could not be written
     */
    public void writeInt(OutputStream stream, int i) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);
        stream.write(bb.array());
    }

    /**
     * Sends a packet to the remote socket.
     * 
     * @param packet - The packet to send
     * @throws IOException - When the packet could not be sent
     */
    @SneakyThrows
    public synchronized void sendPacket(Packet packet) throws IOException {
        OutputStream stream = this.socket.getOutputStream();

        int id = this.packetDictionary.getPacketId(packet);
        String jsonString = this.rugson.toJson(packet);

        byte[] encryptedBytes = null;

        if (this.aesKey != null) {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, this.aesKey);
            encryptedBytes = cipher.doFinal(jsonString.getBytes());
        } else {
            throw new RuntimeException("AES key not initialized");
        }

        int size = encryptedBytes.length;

        writeInt(stream, id);
        writeInt(stream, size);
        stream.write(encryptedBytes);
    }

    private void pollPackets() {
        while (isRunning.get()) {
            // We're giving it 20 seconds, as client and server are supposed to
            // exchange a KeepAlive packet every 5 seconds
            try {
                Packet packet = readNextPacket(20000);
                if (packet == null) {
                    continue;
                }
                handleIncomingPacket(packet);
            } catch (IOException | InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void handleIncomingPacket(Packet packet) {
        List<Class<? extends Packet>> packetClasses = new ArrayList<>();

        Class<? extends Packet> packetClass = packet.getClass();
        while (packetClass != Packet.class) {
            packetClasses.add(packetClass);
            packetClass = (Class<? extends Packet>) packetClass.getSuperclass();
        }
        packetClasses.add(Packet.class);

        this.executorService.execute(() -> {
            passPacketThroughListeners(packet, packetClasses);
        });
    }

    private void passPacketThroughListeners(Packet packet, List<Class<? extends Packet>> packetClasses) {
        for (int i = packetClasses.size() - 1; i >= 0; i--) {
            Class<?> packetClassToCheck = packetClasses.get(i);
            if (!this.packetListeners.containsKey(packetClassToCheck)) {
                continue;
            }

            boolean shouldContinue = true;
            List<PacketListener<?>> toInvoke = new ArrayList<>(this.packetListeners.get(packetClassToCheck));
            for (PacketListener<?> packetListener : toInvoke) {
                try {
                    shouldContinue = packetListener.onReceive(this, packet);
                    if (!shouldContinue) {
                        break;
                    }
                } catch (IOException | NullPointerException | IllegalStateException | IllegalArgumentException
                        | SQLException e) {
                    e.printStackTrace();
                }
            }

            if (!shouldContinue) {
                break;
            }
        }
    }

    /**
     * Closes the connection.
     */
    @SneakyThrows
    public void closeConnection() {
        if (!this.isRunning.get()) {
            return;
        }

        this.packetListeners.values().forEach(List::clear);
        this.packetListeners.clear();

        this.closeListeners.forEach(listener -> listener.accept(this));

        this.keepAliveSender.cancel();
        this.isRunning.set(false);
        this.pollingThread.interrupt();
        this.pollingThread.join(5000);
        this.socket.close();
        System.out.println(
                "Connection closed! " + this.socket.getInetAddress().getHostAddress() + ":" + this.socket.getPort());
    }

    /**
     * Waits until a given amount of bytes is available in the input stream.
     * 
     * @param amount        - The amount of bytes to wait for
     * @param millisTimeout - The timeout in milliseconds
     * @throws IOException - When the connection was closed
     */
    public void waitUntilBytesAvailable(int amount, int millisTimeout) throws IOException {
        InputStream inputStream = this.socket.getInputStream();
        long currentTimestamp = Instant.now().toEpochMilli();

        while (inputStream.available() < amount) {
            try {
                Thread.sleep(10);
                if (Instant.now().toEpochMilli() - currentTimestamp > millisTimeout) {
                    this.closeConnection();
                    return;
                }
            } catch (InterruptedException e) {
                this.closeConnection();
                return;
            }
        }
    }

    @SneakyThrows
    private Packet readNextPacket(int millisTimeout) throws IOException, InterruptedException, TimeoutException {
        InputStream inputStream = this.socket.getInputStream();

        // We're waiting for the first 8 bytes to be available
        // (4 bytes for the packet id, 4 bytes for the packet size)
        // This should be essentially instant, but we're waiting just in case
        waitUntilBytesAvailable(8, millisTimeout);

        int id = readInt(inputStream);
        int size = readInt(inputStream);

        if (size > PACKET_MAX_SIZE) {
            throw new IOException("Packet size too large");
        }

        ByteArrayOutputStream bufferedPacket = new ByteArrayOutputStream();
        if (!readIntoBuffer(inputStream, bufferedPacket, size, Instant.now().toEpochMilli(), millisTimeout)) {
            return null;
        }

        byte[] encryptedBytes = bufferedPacket.toByteArray();

        if (this.aesKey != null) {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, this.aesKey);
            encryptedBytes = cipher.doFinal(encryptedBytes);
        } else {
            throw new RuntimeException("AES key not initialized");
        }

        String jsonString = new String(encryptedBytes);
        Class<? extends Packet> packetClass = this.packetDictionary.getPacketClass(id);

        return this.rugson.fromJson(jsonString, packetClass);
    }

    private boolean readIntoBuffer(InputStream inputStream, ByteArrayOutputStream bufferedPacket, int size,
            long currentTimestamp, int millisTimeout) throws IOException, TimeoutException {

        int prev = inputStream.available();

        int read = 0;
        int bufferSize = 1024;

        while (bufferedPacket.size() < size) {
            // Wait for data to be available
            while (inputStream.available() < size - read && inputStream.available() < bufferSize) {
                try {
                    Thread.sleep(5);
                    if (Instant.now().toEpochMilli() - currentTimestamp > millisTimeout) {
                        throw new TimeoutException();
                    }

                    if (inputStream.available() != prev) {
                        // Reset timeout as data is trickling in
                        currentTimestamp = Instant.now().toEpochMilli();
                        prev = inputStream.available();
                    }
                } catch (InterruptedException e) {
                    this.closeConnection();
                    return false;
                }
            }

            byte[] tempBuf = new byte[bufferSize];
            int tempRead = inputStream.read(tempBuf, 0, Math.min(bufferSize, size - bufferedPacket.size()));
            read += tempRead;

            bufferedPacket.write(tempBuf, 0, tempRead);
        }

        return true;
    }

    /**
     * Reads a key byte array from the input stream.
     * 
     * @return - The key byte array
     * @throws IOException - When the connection was closed
     */
    public byte[] readRawKeyByte() throws IOException {
        waitUntilBytesAvailable(4, 1000);
        InputStream inputStream = this.socket.getInputStream();

        int size = readInt(inputStream);

        ByteArrayOutputStream bufferedPacket = new ByteArrayOutputStream();
        try {
            readIntoBuffer(inputStream, bufferedPacket, size, Instant.now().toEpochMilli(), 1000);
        } catch (TimeoutException e) {
            return null;
        }

        return bufferedPacket.toByteArray();
    }
}
package nl.rug.oop.rts.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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

import lombok.Getter;
import lombok.NonNull;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.packet.Packet;
import nl.rug.oop.rts.protocol.packet.definitions.keepalive.KeepAlivePacket;
import nl.rug.oop.rts.protocol.packet.dictionary.PacketDictionary;
import nl.rug.oop.rugson.Rugson;

public class SocketConnection {
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

    private Timer keepAliveSender = new Timer();

    public SocketConnection(Rugson rugson, Socket socket, ExecutorService executorService,
            PacketDictionary packetDictionary) {
        this.rugson = rugson;
        this.socket = socket;
        this.executorService = executorService;
        this.packetDictionary = packetDictionary;

        this.isRunning.set(true);

        this.pollingThread = new Thread(this::pollPackets);
        this.pollingThread.start();

        keepAliveSender.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    sendPacket(new KeepAlivePacket());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 100, 3000);
    }

    public void addListener(PacketListener<? extends Packet> packetListener) {
        Class<?> packetClass = packetListener.getPacketClass();
        if (!this.packetListeners.containsKey(packetClass)) {
            this.packetListeners.put(packetClass, new ArrayList<>());
        }

        this.packetListeners.get(packetClass).add(packetListener);
    }

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

    private int readInt(InputStream stream) throws IOException {
        byte[] bfs = new byte[4];
        stream.read(bfs);
        ByteBuffer bfr = ByteBuffer.wrap(bfs);
        bfr.order(ByteOrder.BIG_ENDIAN);
        return bfr.getInt();
    }

    private void writeInt(OutputStream stream, int i) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);
        stream.write(bb.array());
    }

    public synchronized void sendPacket(Packet packet) throws IOException {
        OutputStream stream = this.socket.getOutputStream();

        int id = this.packetDictionary.getPacketId(packet);
        String jsonString = this.rugson.toJson(packet);
        int size = jsonString.getBytes().length;

        writeInt(stream, id);
        writeInt(stream, size);
        stream.write(jsonString.getBytes());
    }

    public void pollPackets() {
        while (isRunning.get()) {
            // We're giving it 10 seconds, as client and server are supposed to
            // exchange a KeepAlive packet every 5 seconds
            try {
                Packet packet = readNextPacket(10000);
                if (packet == null)
                    continue;
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
            for (int i = packetClasses.size() - 1; i >= 0; i--) {
                Class<?> packetClassToCheck = packetClasses.get(i);
                if (!this.packetListeners.containsKey(packetClassToCheck)) {
                    continue;
                }

                boolean shouldContinue = true;
                for (PacketListener<?> packetListener : this.packetListeners.get(packetClassToCheck)) {
                    shouldContinue = packetListener.onReceive(this, packet);
                    if (!shouldContinue)
                        break;
                }

                if (!shouldContinue)
                    break;
            }
        });
    }

    public void closeConnection() throws IOException, InterruptedException {
        this.keepAliveSender.cancel();
        this.isRunning.set(false);
        this.pollingThread.interrupt();
        this.pollingThread.join(5000);
        this.socket.close();
    }

    private Packet readNextPacket(int millisTimeout) throws IOException, InterruptedException, TimeoutException {
        int read = 0;
        int bufferSize = 1024;

        InputStream inputStream = this.socket.getInputStream();
        long currentTimestamp = Instant.now().toEpochMilli();

        // We're waiting for the first 8 bytes to be available
        // (4 bytes for the packet id, 4 bytes for the packet size)
        // This should be essentially instant, but we're waiting just in case
        while (inputStream.available() < 8) {
            Thread.sleep(10);
            if (Instant.now().toEpochMilli() - currentTimestamp > millisTimeout) {
                throw new TimeoutException();
            }
        }

        int id = readInt(inputStream);
        int size = readInt(inputStream);

        ByteArrayOutputStream bufferedPacket = new ByteArrayOutputStream();
        while (bufferedPacket.size() < size) {
            // Wait for data to be available
            while (inputStream.available() < size - read &&
                    inputStream.available() < bufferSize) {
                Thread.sleep(5);
                if (Instant.now().toEpochMilli() - currentTimestamp > millisTimeout) {
                    throw new TimeoutException();
                }
            }

            byte[] tempBuf = new byte[bufferSize];
            int tempRead = inputStream.read(tempBuf, 0, Math.min(bufferSize, size - bufferedPacket.size()));
            read += tempRead;

            bufferedPacket.write(tempBuf);
        }

        String jsonString = bufferedPacket.toString();
        Class<? extends Packet> packetClass = this.packetDictionary.getPacketClass(id);

        return this.rugson.fromJson(jsonString, packetClass);
    }
}

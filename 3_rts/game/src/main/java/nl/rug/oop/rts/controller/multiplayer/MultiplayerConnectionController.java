package nl.rug.oop.rts.controller.multiplayer;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import nl.rug.oop.rts.controller.settings.SettingsController;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.listeners.AwaitPacketOnce;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.objects.model.events.EventFactory;
import nl.rug.oop.rts.protocol.objects.model.factories.UnitFactory;
import nl.rug.oop.rts.protocol.packet.Packet;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.AuthenticatedPacket;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.login.LoginRequest;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.login.LoginResponse;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.register.RegistrationRequest;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.register.RegistrationResponse;
import nl.rug.oop.rts.protocol.packet.dictionary.PacketDictionary;
import nl.rug.oop.rts.protocol.packet.dictionary.RTSPacketDictionary;
import nl.rug.oop.rts.protocol.user.User;
import nl.rug.oop.rugson.Rugson;

@RequiredArgsConstructor
public class MultiplayerConnectionController {
    @Getter
    private SocketConnection connection;

    @NonNull
    private SettingsController settingsController;

    @NonNull
    private Rugson rugson;

    @NonNull
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    @NonNull
    private UnitFactory unitFactory;

    @NonNull
    private EventFactory eventFactory;

    @Getter
    private UUID authToken = null;

    @Getter
    private User user = null;

    public boolean openConnection() throws IOException {
        if (connection != null) {
            if (!connection.getSocket().isConnected())
                connection = null;
            else
                return true;
        }

        try {
            String centralServer = settingsController.getSettings().getCentralServer();
            int port = 7779;
            if (centralServer.contains(":")) {
                String[] split = centralServer.split(":");
                centralServer = split[0];
                port = Integer.parseInt(split[1]);
            }

            Socket socket = new Socket(centralServer, port);
            this.connection = new SocketConnection(rugson, socket, threadPool, getPacketDictionary());
            this.connection.addListener(new PacketListener<>(Packet.class) {
                @Override
                protected boolean handlePacket(SocketConnection connection, Packet packet) {
                    System.out.println("Received packet: " + packet);
                    return true;
                }
            });

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            this.connection = null;
            return false;
        }

    }

    @SneakyThrows
    public void closeConnection() {
        if (connection != null)
            this.connection.closeConnection();

        this.threadPool.shutdown();
        this.threadPool.awaitTermination(5, TimeUnit.SECONDS);
    }

    private PacketDictionary getPacketDictionary() {
        return new RTSPacketDictionary();
    }

    public CompletableFuture<Boolean> ensureLogin() throws IOException {
        if (this.connection == null)
            return CompletableFuture.completedFuture(false);
        if (this.authToken != null)
            return CompletableFuture.completedFuture(true);

        String username = settingsController.getSettings().getUsername();
        String password = settingsController.getSettings().getPassword();

        AwaitPacketOnce<Packet> result = new AwaitPacketOnce<>(LoginResponse.class).bindTo(connection);
        this.connection.sendPacket(new LoginRequest(username, password));

        return result.getAwaiting().thenApply(c -> {
            LoginResponse response = (LoginResponse) c.getValue();
            if (!response.isSuccess())
                return false;

            if (!response.getUser().getName().equals(username))
                return false;

            this.authToken = response.getToken();
            this.user = response.getUser();

            return true;
        });
    }

    public CompletableFuture<RegistrationResponse> attemptRegister(String username, String password)
            throws IOException {
        if (this.connection == null)
            return CompletableFuture.completedFuture(new RegistrationResponse(false, "Not connected to server.", null));
        if (this.authToken != null)
            return CompletableFuture.completedFuture(new RegistrationResponse(true, "Already logged in.", null));

        AwaitPacketOnce<Packet> result = new AwaitPacketOnce<>(RegistrationResponse.class).bindTo(connection);
        this.connection.sendPacket(new RegistrationRequest(username, password));

        return result.getAwaiting().thenApply(c -> {
            RegistrationResponse response = (RegistrationResponse) c.getValue();
            if (!response.isSuccess())
                return response;

            this.authToken = response.getToken();
            this.user = new User(-1, username, null, 1000);
            return response;
        });
    }

    public void sendPacket(Packet packet) {
        if (this.connection == null)
            return;
        this.threadPool.execute(() -> actuallySendPacket(packet));
    }

    @SneakyThrows
    private void actuallySendPacket(Packet packet) {
        this.connection.sendPacket(packet);
    }

    public void sendAuthenticatedPacket(AuthenticatedPacket packet) {
        if (this.connection == null)
            throw new IllegalStateException("Not connected to server.");
        if (this.authToken == null)
            throw new IllegalStateException("Not logged in.");

        packet.setSessionToken(authToken);
        sendPacket(packet);
    }
}

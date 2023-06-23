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
import nl.rug.oop.rts.Game;
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
import nl.rug.oop.rts.protocol.packet.definitions.authentication.tokens.TokenRefreshingRequest;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.tokens.TokenRefreshingResponse;
import nl.rug.oop.rts.protocol.packet.definitions.game.GameStartPacket;
import nl.rug.oop.rts.protocol.packet.dictionary.PacketDictionary;
import nl.rug.oop.rts.protocol.packet.dictionary.RTSPacketDictionary;
import nl.rug.oop.rts.protocol.user.User;
import nl.rug.oop.rts.view.multiplayer.MultiplayerGameView;
import nl.rug.oop.rugson.Rugson;

/**
 * This class manages the connection to the central server.
 */
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

    @NonNull
    private Game game;

    @Getter
    private UUID authToken = null;

    @Getter
    private User user = null;

    /**
     * Starts the connection to the central server.
     * 
     * @return - Whether the connection was successful
     * @throws IOException - When the connection cannot be opened
     */
    public boolean openConnection() throws IOException {
        if (connection != null) {
            if (!connection.getSocket().isConnected()) {
                connection = null;
            } else {
                return true;
            }
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
            this.connection.initializeAESKey();
            this.connection.broadcastEncryptedAESKey();
            this.connection.initializeSending();

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

    /**
     * Closes the connection to the central server.
     */
    @SneakyThrows
    public void closeConnection() {
        if (connection != null) {
            this.connection.closeConnection();
        }

        this.threadPool.shutdown();
        this.threadPool.awaitTermination(5, TimeUnit.SECONDS);
    }

    private PacketDictionary getPacketDictionary() {
        return new RTSPacketDictionary();
    }

    /**
     * Attempts to login to the central server.
     * 
     * @return - A future that will be completed when the login is complete
     * @throws IOException - When the connection is not open
     */
    public CompletableFuture<Boolean> validateRefreshToken() throws IOException {
        if (this.connection == null) {
            return CompletableFuture.completedFuture(false);
        }
        if (this.authToken != null) {
            return CompletableFuture.completedFuture(true);
        }

        UUID refreshToken = settingsController.getSettings().getRefreshToken();
        if (refreshToken == null) {
            return CompletableFuture.completedFuture(false);
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        AwaitPacketOnce<Packet> result = new AwaitPacketOnce<>(TokenRefreshingResponse.class).bindTo(connection);
        this.connection.sendPacket(new TokenRefreshingRequest(refreshToken));

        return result.getAwaiting().thenApply(c -> {
            TokenRefreshingResponse response = (TokenRefreshingResponse) c.getValue();
            if (!response.isSuccess()) {
                return false;
            }

            this.authToken = response.getToken();
            this.user = response.getUser();

            return true;
        });
    }

    /**
     * Attempts to login to the central server.
     * 
     * @param username - The username to login with
     * @param password - The password to login with
     * @return - A future that will be completed when the login is complete
     * @throws IOException - When the connection is not open
     */
    public CompletableFuture<Boolean> attemptLogin(String username, String password) throws IOException {
        if (this.connection == null) {
            return CompletableFuture.completedFuture(false);
        }
        if (this.authToken != null) {
            return CompletableFuture.completedFuture(true);
        }

        AwaitPacketOnce<Packet> result = new AwaitPacketOnce<>(LoginResponse.class).bindTo(connection);
        this.connection.sendPacket(new LoginRequest(username, password));

        return result.getAwaiting().thenApply(c -> {
            LoginResponse request = (LoginResponse) c.getValue();
            if (!request.isSuccess()) {
                return false;
            }

            this.authToken = request.getToken();
            this.user = request.getUser();

            this.settingsController.setRefreshToken(request.getRefreshToken());
            this.settingsController.save();

            return true;
        });
    }

    /**
     * Attempts to register to the central server.
     * 
     * @param username - The username to register with
     * @param password - The password to register with
     * @return - A future that will be completed when the registration is complete
     * @throws IOException - When the connection is not open
     */
    public CompletableFuture<RegistrationResponse> attemptRegister(String username, String password)
            throws IOException {
        if (this.connection == null) {
            return CompletableFuture
                    .completedFuture(new RegistrationResponse(false, "Not connected to server.", null, null));
        }
        if (this.authToken != null) {
            return CompletableFuture.completedFuture(new RegistrationResponse(true, "Already logged in.",
                    null, null));
        }

        AwaitPacketOnce<Packet> result = new AwaitPacketOnce<>(RegistrationResponse.class).bindTo(connection);
        this.connection.sendPacket(new RegistrationRequest(username, password));

        return result.getAwaiting().thenApply(c -> {
            RegistrationResponse response = (RegistrationResponse) c.getValue();
            if (!response.isSuccess()) {
                return response;
            }

            this.authToken = response.getToken();
            this.user = new User(-1, username, null, 1000);
            return response;
        });
    }

    /**
     * Sends a packet to the central server.
     * 
     * @param packet - The packet to send
     */
    public void sendPacket(Packet packet) {
        if (this.connection == null) {
            return;
        }

        this.threadPool.execute(() -> actuallySendPacket(packet));
    }

    /**
     * Sends a packet to the central server synchronously.
     * 
     * @param packet - The packet to send
     */
    public void sendPacketSyncronously(Packet packet) {
        if (this.connection == null) {
            return;
        }

        actuallySendPacket(packet);
    }

    @SneakyThrows
    private void actuallySendPacket(Packet packet) {
        this.connection.sendPacket(packet);
    }

    /**
     * Sends a packet to the central server, but only if the user is logged in.
     * Automatically sets the session ID inside of the packet.
     * 
     * @param packet - The packet to send
     */
    public void sendAuthenticatedPacket(AuthenticatedPacket packet) {
        if (this.connection == null) {
            throw new IllegalStateException("Not connected to server.");
        }
        if (this.authToken == null) {
            throw new IllegalStateException("Not logged in.");
        }

        packet.setSessionToken(authToken);
        sendPacket(packet);
    }

    /**
     * Sends a packet to the central server, but only if the user is logged in.
     * 
     * @param packet - The packet to send
     */
    public void handleGameStartPacket(GameStartPacket packet) {
        if (!packet.isSuccess()) {
            throw new IllegalStateException("GameStartPacket was not successful");
        }

        MultiplayerGameView view = new MultiplayerGameView(game, packet.getGame(),
                this, packet.getTeam(), unitFactory);
        game.handleView(view);
    }
}

package nl.rug.oop.rts.controller.multiplayer;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rts.controller.settings.SettingsController;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.listeners.AwaitPacketOnce;
import nl.rug.oop.rts.protocol.objects.model.events.EventFactory;
import nl.rug.oop.rts.protocol.objects.model.factories.UnitFactory;
import nl.rug.oop.rts.protocol.packet.Packet;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.login.LoginRequest;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.login.LoginResponse;
import nl.rug.oop.rts.protocol.packet.dictionary.PacketDictionary;
import nl.rug.oop.rts.protocol.packet.dictionary.RTSPacketDictionary;
import nl.rug.oop.rugson.Rugson;

@RequiredArgsConstructor
public class MultiplayerConnectionController {
    private SocketConnection connection;

    @NonNull
    private SettingsController settingsController;

    @NonNull
    private Rugson rugson;

    @NonNull
    private ExecutorService threadPool;

    @NonNull
    private UnitFactory unitFactory;

    @NonNull
    private EventFactory eventFactory;

    private UUID authToken = null;

    public void openConnection() throws IOException {
        if (connection != null) {
            if (!connection.getSocket().isConnected())
                connection = null;
            else
                return;
        }

        String centralServer = settingsController.getSettings().getCentralServer();
        int port = 7779;
        if (centralServer.contains(":")) {
            String[] split = centralServer.split(":");
            centralServer = split[0];
            port = Integer.parseInt(split[1]);
        }

        Socket socket = new Socket(centralServer, port);
        this.connection = new SocketConnection(rugson, socket, threadPool, getPacketDictionary());
    }

    private PacketDictionary getPacketDictionary() {
        return new RTSPacketDictionary();
    }

    public CompletableFuture<Boolean> ensureLogin() throws IOException {
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
            return true;
        });
    }
}

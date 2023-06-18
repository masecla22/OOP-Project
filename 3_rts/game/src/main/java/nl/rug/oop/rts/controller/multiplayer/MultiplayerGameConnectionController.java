package nl.rug.oop.rts.controller.multiplayer;

import java.io.IOException;

import javax.swing.JOptionPane;

import lombok.AllArgsConstructor;
import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.controller.map.MultiplayerMapController;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.listeners.AwaitPacketOnce;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.MultiplayerGame;
import nl.rug.oop.rts.protocol.packet.Packet;
import nl.rug.oop.rts.protocol.packet.definitions.game.GameEndPacket;
import nl.rug.oop.rts.protocol.packet.definitions.game.GameScopedPacket;
import nl.rug.oop.rts.protocol.packet.definitions.game.GameUpdatePacket;
import nl.rug.oop.rts.protocol.packet.definitions.game.changes.GameChangeListConfirm;
import nl.rug.oop.rts.protocol.packet.definitions.game.changes.GameChangeListPacket;
import nl.rug.oop.rts.view.multiplayer.MultiplayerView;

@AllArgsConstructor
public class MultiplayerGameConnectionController {

    private MultiplayerMapController mapController;
    private MultiplayerConnectionController connectionController;
    private MultiplayerGame multiGame;
    private Game game;

    public void sendGamePacket(GameScopedPacket packet) {
        packet.setGameId(multiGame.getGameId());
        this.connectionController.sendAuthenticatedPacket(packet);
    }

    public void commitChanges() {
        GameChangeListPacket packet = new GameChangeListPacket(mapController.getChanges());

        AwaitPacketOnce<Packet> awaiter = new AwaitPacketOnce<>(GameChangeListConfirm.class)
                .bindTo(connectionController.getConnection());

        sendGamePacket(packet);

        awaiter.getAwaiting().thenAccept(c -> {
            mapController.getChanges().clear();
            // game.setPlayerATurn(!game.isPlayerATurn());
            multiGame.update();
        });
    }

    public void bindGameChangeListener() {
        connectionController.getConnection().addListener(new PacketListener<GameUpdatePacket>(GameUpdatePacket.class) {
            @Override
            protected boolean handlePacket(SocketConnection connection, GameUpdatePacket packet) throws IOException {
                System.out.println("Received game update packet");
                mapController.ingestMapUpdate(packet);
                return true;
            }
        });
    }

    public void bindGameEndListener() {
        connectionController.getConnection().addListener(new PacketListener<GameEndPacket>(GameEndPacket.class) {
            @Override
            protected boolean handlePacket(SocketConnection connection, GameEndPacket packet) throws IOException {
                showGameEnding(packet.isWinner(), packet.getNewElo());
                return true;
            }
        });
    }

    public void unbindGameChangeListener() {
        connectionController.getConnection().removeListeners(GameUpdatePacket.class);
    }

    public void unbindGameEndListener() {
        connectionController.getConnection().removeListeners(GameEndPacket.class);
    }

    public void showGameEnding(boolean won, int elo) {
        JOptionPane.showMessageDialog(null,
                "Game ended. You " + (won ? "won" : "lost") + "! You now have " + elo + " ELO");
        game.handleBackUpTo(MultiplayerView.class);
    }
}

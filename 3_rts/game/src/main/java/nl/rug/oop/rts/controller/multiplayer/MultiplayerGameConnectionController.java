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

/**
 * This class manages the connection to the central server during a game.
 */
@AllArgsConstructor
public class MultiplayerGameConnectionController {

    private MultiplayerMapController mapController;
    private MultiplayerConnectionController connectionController;
    private MultiplayerGame multiGame;
    private Game game;

    /**
     * Sends a packet to the server. It will automatically set the game id.
     * 
     * @param packet - the packet to send
     */
    public void sendGamePacket(GameScopedPacket packet) {
        packet.setGameId(multiGame.getGameId());
        this.connectionController.sendAuthenticatedPacket(packet);
    }

    /**
     * Commits the changes to the server.
     */
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

    /**
     * Binds the game change listener.
     */
    public void bindGameChangeListener() {
        connectionController.getConnection().addListener(new PacketListener<GameUpdatePacket>(GameUpdatePacket.class) {
            @Override
            protected boolean handlePacket(SocketConnection connection, GameUpdatePacket packet) throws IOException {
                mapController.ingestMapUpdate(packet);
                return true;
            }
        });
    }

    /**
     * Binds the game end listener.
     */
    public void bindGameEndListener() {
        connectionController.getConnection().addListener(new PacketListener<GameEndPacket>(GameEndPacket.class) {
            @Override
            protected boolean handlePacket(SocketConnection connection, GameEndPacket packet) throws IOException {
                showGameEnding(packet.isWinner(), packet.getNewElo());
                return true;
            }
        });
    }

    /**
     * Unbinds the game change listener.
     */
    public void unbindGameChangeListener() {
        connectionController.getConnection().removeListeners(GameUpdatePacket.class);
    }

    /**
     * Unbinds the game end listener.
     */
    public void unbindGameEndListener() {
        connectionController.getConnection().removeListeners(GameEndPacket.class);
    }

    /**
     * Shows the game ending dialog.
     * 
     * @param won - whether the player won or not
     * @param elo - the new elo of the player
     */
    public void showGameEnding(boolean won, int elo) {
        JOptionPane.showMessageDialog(null,
                "Game ended. You " + (won ? "won" : "lost") + "! You now have " + elo + " ELO");
        game.handleBackUpTo(MultiplayerView.class);
    }
}

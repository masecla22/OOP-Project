package nl.rug.oop.rts.view.multiplayer.lobby;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.controller.multiplayer.MultiplayerConnectionController;
import nl.rug.oop.rts.controller.multiplayer.MultiplayerLobbyConnectionController;
import nl.rug.oop.rts.protocol.games.MultiplayerLobby;
import nl.rug.oop.rts.protocol.listeners.AwaitPacketOnce;
import nl.rug.oop.rts.protocol.packet.Packet;
import nl.rug.oop.rts.protocol.packet.definitions.game.GameStartPacket;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyDeletionRequest;
import nl.rug.oop.rts.view.View;

/**
 * Represents a view for the lobby waiting screen.
 */
public class LobbyWaitingView extends View {
    private Game game;
    private MultiplayerLobby lobby;
    private MultiplayerLobbyConnectionController connectionController;
    
    private JLabel lobbyNameLabel;
    private JLabel mapNameLabel;
    private JLabel numEdgesLabel;
    private JLabel numNodesLabel;

    /**
     * Constructs a LobbyWaitingView object.
     *
     * @param game                 The game object.
     * @param lobby                The multiplayer lobby.
     * @param connectionController The multiplayer connection controller.
     */
    public LobbyWaitingView(Game game, MultiplayerLobby lobby,
            MultiplayerConnectionController connectionController) {
        this.game = game;
        this.lobby = lobby;
        this.connectionController = new MultiplayerLobbyConnectionController(connectionController, lobby);

        buildContentPane();
        bindGameStartPacketListener();
    }

    private void buildContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());

        addBack(contentPane);

        lobbyNameLabel = new JLabel("Lobby Name: " + lobby.getName());
        mapNameLabel = new JLabel("Map Name: " + lobby.getMapName());
        numEdgesLabel = new JLabel("Number of Edges: " + lobby.getMap().getEdges().size());
        numNodesLabel = new JLabel("Number of Nodes: " + lobby.getMap().getNodes().size());

        JPanel statistics = new JPanel();
        statistics.setLayout(new GridLayout(5, 1, 20, 0));

        statistics.add(lobbyNameLabel);
        statistics.add(mapNameLabel);
        statistics.add(numEdgesLabel);
        statistics.add(numNodesLabel);
        statistics.add(new JLabel("Waiting for another player..."));

        // Add the labels to the content pane
        contentPane.add(statistics, BorderLayout.CENTER);

        this.add(contentPane);
    }

    private void addBack(JPanel contentPane) {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to go back?", "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Send LobbyDeletionRequest
                connectionController.sendLobbyPacket(new LobbyDeletionRequest());

                // Leave the lobby
                game.handleBack();
            }
        });

        backButton.setPreferredSize(new Dimension(200, 50));
        contentPane.add(backButton, BorderLayout.PAGE_START);
    }

    private void bindGameStartPacketListener() {
        AwaitPacketOnce<Packet> packetListener = new AwaitPacketOnce<>(GameStartPacket.class)
                .bindTo(connectionController.getConnection());
        packetListener.getAwaiting()
                .thenApply(c -> (GameStartPacket) c.getValue())
                .thenAccept(this::handleGameStartPacket);
    }

    private void handleGameStartPacket(GameStartPacket packetEntry) {
    }
}

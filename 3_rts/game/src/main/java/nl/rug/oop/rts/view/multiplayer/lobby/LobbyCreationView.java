package nl.rug.oop.rts.view.multiplayer.lobby;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.controller.multiplayer.MultiplayerConnectionController;
import nl.rug.oop.rts.protocol.games.MultiplayerLobby;
import nl.rug.oop.rts.protocol.listeners.AwaitPacketOnce;
import nl.rug.oop.rts.protocol.objects.interfaces.observing.Observer;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.packet.Packet;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyCreationRequest;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyCreationResponse;
import nl.rug.oop.rts.util.PredefinedMapLoader;
import nl.rug.oop.rts.util.file.MapSaveJsonFilter;
import nl.rug.oop.rts.view.View;
import nl.rug.oop.rugson.Rugson;

public class LobbyCreationView extends View implements Observer {
    private Game game;
    private MultiplayerConnectionController connectionController;
    private Rugson rugson;

    private PredefinedMapLoader mapLoader;

    private Map selectedMap = null;
    private String selectedMapName = null;

    private JLabel mapName = new JLabel();
    private JTextField lobbyNameField = new JTextField();

    public LobbyCreationView(Game game, MultiplayerConnectionController connectionController, Rugson rugson) {
        this.game = game;
        this.connectionController = connectionController;
        this.rugson = rugson;

        this.mapLoader = new PredefinedMapLoader(rugson);

        this.setMap("star", this.mapLoader.getMap("star"));

        this.setLayout(new BorderLayout());

        JPanel lobbyOptions = new JPanel();

        this.add(lobbyOptions, BorderLayout.CENTER);
        addLobbyProperties();
        addBack();
        addCreateButton();
    }

    private void addLobbyProperties() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        panel.add(new JLabel("Map: "));
        panel.add(this.mapName);

        JComboBox<String> mapSelector = new JComboBox<>(mapLoader.getPredefinedMapNames().toArray(new String[0]));
        mapSelector.addActionListener(e -> {
            String mapName = (String) mapSelector.getSelectedItem();
            Map map = mapLoader.getMap(mapName);
            setMap(mapName, map);
        });

        JButton customMapButton = new JButton("Custom Map");
        customMapButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            // only json files
            fileChooser.setFileFilter(new MapSaveJsonFilter());
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setMultiSelectionEnabled(false);

            fileChooser.addActionListener(this::handleMapSelection);
            fileChooser.showOpenDialog(this);
        });

        panel.add(new JLabel("Lobby name: "));
        panel.add(this.lobbyNameField);

        panel.add(mapSelector);
        panel.add(customMapButton);

        this.add(panel, BorderLayout.CENTER);
    }

    private void addCreateButton() {
        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> {
            String lobbyName = this.lobbyNameField.getText();
            if (lobbyName == null || lobbyName.isEmpty()) {
                return;
            }

            String mapName = this.selectedMapName;
            if (mapName == null || mapName.isEmpty()) {
                return;
            }

            this.createLobby(lobbyName, mapName, this.selectedMap);
        });

        createButton.setPreferredSize(new Dimension(200, 50));
        this.add(createButton, BorderLayout.PAGE_END);
    }

    private void handleMapSelection(ActionEvent event) {
        try {
            JFileChooser fileChooser = (JFileChooser) event.getSource();
            if (event.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                String mapName = fileChooser.getSelectedFile().getName();

                FileInputStream stream = new FileInputStream(fileChooser.getSelectedFile());
                Map map = rugson.fromJson(stream, Map.class);
                setMap(mapName, map);
                stream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.selectedMapName = "Something went wrong";
            this.selectedMap = null;
        }
    }

    private void setMap(String mapName, Map map) {
        this.selectedMapName = mapName;
        this.selectedMap = map;

        this.mapName.setText(mapName);
    }

    private void addBack() {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            this.game.handleBack();
        });

        backButton.setPreferredSize(new Dimension(200, 50));
        this.add(backButton, BorderLayout.PAGE_START);
    }

    private void createLobby(String name, String mapName, Map map) {
        // Cleanup armies
        // Will also be done on the server for anti-cheat reasons
        // But we do it here too for performance reasons
        map.removeAllArmies();

        AwaitPacketOnce<Packet> awaiter = new AwaitPacketOnce<>(LobbyCreationResponse.class)
                .bindTo(this.connectionController.getConnection());
        this.connectionController.sendAuthenticatedPacket(new LobbyCreationRequest(name, mapName, map));

        awaiter.getAwaiting().thenAccept(c -> {
            LobbyCreationResponse response = (LobbyCreationResponse) c.getValue();

            MultiplayerLobby lobby = response.getLobby();
            LobbyWaitingView view = new LobbyWaitingView(game, lobby, connectionController);
            game.handleView(view);
        });
    }
}

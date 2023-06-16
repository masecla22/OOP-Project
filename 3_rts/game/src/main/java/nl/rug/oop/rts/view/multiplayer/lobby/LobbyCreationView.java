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

import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.controller.multiplayer.MultiplayerConnectionController;
import nl.rug.oop.rts.protocol.objects.interfaces.observing.Observer;
import nl.rug.oop.rts.protocol.objects.model.Map;
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

    private JLabel mapName;

    public LobbyCreationView(Game game, MultiplayerConnectionController connectionController, Rugson rugson) {
        this.game = game;
        this.connectionController = connectionController;
        this.rugson = rugson;

        this.mapLoader = new PredefinedMapLoader(rugson);

        this.setLayout(new BorderLayout());

        JPanel lobbyOptions = new JPanel();

        this.add(lobbyOptions, BorderLayout.CENTER);
        addMapSelector();
        addBack();
    }

    private void addMapSelector() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));

        JComboBox<String> mapSelector = new JComboBox<>(mapLoader.getPredefinedMapNames().toArray(new String[0]));
        mapSelector.addActionListener(e -> {
            String mapName = (String) mapSelector.getSelectedItem();
            this.selectedMapName = mapName;
            this.selectedMap = mapLoader.getMap(mapName);
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
        });
    }

    private void handleMapSelection(ActionEvent event) {
        try {
            JFileChooser fileChooser = (JFileChooser) event.getSource();
            if (event.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                String mapName = fileChooser.getSelectedFile().getName();

                this.selectedMapName = mapName;

                FileInputStream stream = new FileInputStream(fileChooser.getSelectedFile());
                this.selectedMap = rugson.fromJson(stream, Map.class);
                stream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.selectedMapName = "Something went wrong";
            this.selectedMap = null;
        }
    }

    private void addBack() {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            this.game.handleBack();
        });

        backButton.setPreferredSize(new Dimension(200, 50));
        this.add(backButton, BorderLayout.PAGE_START);
    }
}

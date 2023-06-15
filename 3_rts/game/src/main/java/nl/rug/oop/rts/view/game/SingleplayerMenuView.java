package nl.rug.oop.rts.view.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.controller.map.MapController;
import nl.rug.oop.rts.controller.map.MapSimulationController;
import nl.rug.oop.rts.controller.map.SinglePlayerMapController;
import nl.rug.oop.rts.protocol.adapters.EventTypeAdapter;
import nl.rug.oop.rts.protocol.adapters.GameMapTypeAdapter;
import nl.rug.oop.rts.protocol.adapters.UnitTypeAdapter;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.objects.model.events.Event;
import nl.rug.oop.rts.protocol.objects.model.events.EventFactory;
import nl.rug.oop.rts.protocol.objects.model.factories.UnitFactory;
import nl.rug.oop.rts.protocol.objects.model.factories.singleplayer.SinglePlayerUnitFactory;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.view.View;
import nl.rug.oop.rugson.Rugson;
import nl.rug.oop.rugson.RugsonBuilder;

public class SingleplayerMenuView extends View {
    private Game game;

    private UnitFactory spUnitFactory;
    private EventFactory spEventFactory;
    private Rugson rugson;

    public SingleplayerMenuView(Game game) {
        super();
        this.game = game;

        this.spUnitFactory = new SinglePlayerUnitFactory(-1);
        this.spEventFactory = new EventFactory(spUnitFactory);
        this.initializeRugson();

        this.setLayout(new BorderLayout());

        JPanel options = new JPanel();
        options.setLayout(new GridLayout(2, 1));

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> handleNewGame());
        newGameButton.setPreferredSize(new Dimension(200, 100));
        options.add(newGameButton);

        JButton loadGameButton = new JButton("Load Game");
        loadGameButton.addActionListener(e -> handleLoadGame());
        loadGameButton.setPreferredSize(new Dimension(200, 100));
        options.add(loadGameButton);

        this.add(options, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            this.game.handleBack();
        });

        backButton.setPreferredSize(new Dimension(200, 50));
        this.add(backButton, BorderLayout.PAGE_END);
    }

    private void initializeRugson() {
        this.rugson = new RugsonBuilder()
                .setPrettyPrint(true)
                .addTypeAdapter(Unit.class, new UnitTypeAdapter())
                .addTypeAdapter(Event.class, new EventTypeAdapter(spEventFactory))
                .addTypeAdapter(Map.class, new GameMapTypeAdapter())
                .build();
    }

    private void loadMap(Map map) {
        MapController spMapController = new SinglePlayerMapController(rugson, spUnitFactory, spEventFactory, map);
        MapSimulationController simulationController = new MapSimulationController(map);

        GameView view = new GameView(game, map, spMapController, simulationController);
        game.handleView(view);
    }

    private void handleNewGame() {
        loadMap(new Map());
    }

    private void loadGame(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        Map map = rugson.fromJson(inputStream, Map.class);
        loadMap(map);
    }

    private void handleLoadGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Game");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.addActionListener(e -> {
            if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                File file = fileChooser.getSelectedFile();
                try {
                    loadGame(file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        fileChooser.showOpenDialog(this);
    }
}

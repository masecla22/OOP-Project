package nl.rug.oop.rts.view.multiplayer;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.controller.map.MultiplayerMapController;
import nl.rug.oop.rts.controller.multiplayer.MultiplayerConnectionController;
import nl.rug.oop.rts.controller.multiplayer.MultiplayerGameConnectionController;
import nl.rug.oop.rts.protocol.objects.interfaces.observing.Observer;
import nl.rug.oop.rts.protocol.objects.model.armies.Team;
import nl.rug.oop.rts.protocol.objects.model.factories.UnitFactory;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.MultiplayerGame;
import nl.rug.oop.rts.view.View;
import nl.rug.oop.rts.view.game.SidePanelView;
import nl.rug.oop.rts.view.map.MapView;

public class MultiplayerGameView extends View implements Observer {
    private Game game;

    private MultiplayerGame multiGame;
    private Team team;

    private MapView mapView;
    private MultiplayerMapController mapController;
    private MultiplayerGameConnectionController connectionController;

    public MultiplayerGameView(Game game, MultiplayerGame multiGame,
            MultiplayerConnectionController connectionController, Team team, UnitFactory unitFactory) {
        this.game = game;
        this.multiGame = multiGame;
        this.team = team;

        this.multiGame.addObserver(this);
        this.multiGame.getMap().addObserver(this);

        this.mapController = new MultiplayerMapController(multiGame, team, null, multiGame.getMap(), unitFactory);

        this.connectionController = new MultiplayerGameConnectionController(mapController,
                connectionController, multiGame);

        this.setLayout(new BorderLayout());

        this.buildTopBar();

        this.mapView = new MapView(multiGame.getMap(), this.mapController);

        this.add(this.mapView, BorderLayout.CENTER);
        this.add(new SidePanelView(this.multiGame.getMap(), mapController), BorderLayout.LINE_START);

        this.connectionController.bindGameChangeListener();
    }

    @Override
    public void onClose() {
        this.connectionController.unbindGameChangeListener();
    }

    private JPanel topBar = null;

    private void buildTopBar() {
        if (topBar == null) {
            topBar = new JPanel();
            this.add(topBar, BorderLayout.PAGE_START);
        }

        topBar.removeAll();

        if (!this.multiGame.isMyTurn(team)) {
            topBar.add(new JLabel(
                    "Waiting for other player... Gold: " + this.multiGame.getGamePlayer(team).getGold()));
            return;
        }

        // Add back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            this.handleBack();
        });

        // Add all buttons to the top bar
        topBar.add(backButton);

        JButton markAsReady = new JButton("Finish turn");
        markAsReady.addActionListener(e -> {
            markAsReady.setEnabled(false);
            markAsReady.setText("Loading...");
            this.connectionController.commitChanges();
        });

        int gold = this.multiGame.getGamePlayer(team).getGold();
        topBar.add(new JLabel("Gold: " + gold));

        topBar.add(markAsReady);
        this.add(topBar, BorderLayout.PAGE_START);
    }

    @Override
    public void update() {
        System.out.println("UPDATE CALLED");
        System.out.println(this.multiGame.getMap());

        this.buildTopBar();

        this.revalidate();
        this.repaint();
    }

    private void handleBack() {

    }

}

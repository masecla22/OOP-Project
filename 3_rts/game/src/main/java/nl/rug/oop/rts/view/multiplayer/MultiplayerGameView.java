package nl.rug.oop.rts.view.multiplayer;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.controller.map.MapController;
import nl.rug.oop.rts.controller.map.MultiplayerMapController;
import nl.rug.oop.rts.protocol.objects.interfaces.observing.Observer;
import nl.rug.oop.rts.protocol.objects.model.armies.Team;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.MultiplayerGame;
import nl.rug.oop.rts.view.View;
import nl.rug.oop.rts.view.game.SidePanelView;
import nl.rug.oop.rts.view.map.MapView;

public class MultiplayerGameView extends View implements Observer {
    private Game game;

    private MultiplayerGame multiGame;
    private Team team;

    private MapView mapView;
    private MapController mapController;

    public MultiplayerGameView(Game game, MultiplayerGame multiGame, Team team) {
        this.game = game;
        this.multiGame = multiGame;
        this.team = team;

        this.multiGame.addObserver(this);

        this.setLayout(new BorderLayout());
        this.add(buildTopBar(), BorderLayout.PAGE_START);

        this.mapController = new MultiplayerMapController(multiGame, team, null, multiGame.getMap());
        this.mapView = new MapView(multiGame.getMap(), this.mapController);

        this.add(this.mapView, BorderLayout.CENTER);
        this.add(new SidePanelView(this.multiGame.getMap(), mapController), BorderLayout.LINE_START);
    }

    private JPanel buildTopBar() {
        JPanel topBar = new JPanel();

        // Add back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            this.handleBack();
        });

        // Add all buttons to the top bar
        topBar.add(backButton);

        JButton markAsReady = new JButton("Finish turn");
        markAsReady.addActionListener(e -> {

        });
        topBar.add(markAsReady);

        return topBar;
    }

    private void handleBack() {

    }

}

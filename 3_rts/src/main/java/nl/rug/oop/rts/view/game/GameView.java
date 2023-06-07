package nl.rug.oop.rts.view.game;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import lombok.AccessLevel;
import lombok.Getter;
import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.model.Map;
import nl.rug.oop.rts.view.map.MapView;

@Getter(AccessLevel.PROTECTED)
public class GameView extends JPanel {
    private Game game;
    private Map map;

    public GameView(Game game, Map map) {
        super();
        this.game = game;
        this.map = map;

        this.setLayout(new BorderLayout());

        this.add(buildTopBar(), BorderLayout.PAGE_START);

        MapView mapView = new MapView(game, map);
        this.add(mapView, BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel topBar = new JPanel();

        // Add back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            this.game.handleBack();
        });

        // Add edge button
        JButton addEdgeButton = new JButton("Add Edge");

        // Remove edge button
        JButton removeEdgeButton = new JButton("Remove Edge");

        // Add node button
        JButton addNodeButton = new JButton("Add Node");

        // Remove node button
        JButton removeNodeButton = new JButton("Remove Node");

        // Add all buttons to the top bar
        topBar.add(backButton);

        topBar.add(addEdgeButton);
        topBar.add(removeEdgeButton);
        topBar.add(addNodeButton);
        topBar.add(removeNodeButton);

        return topBar;
    }
}

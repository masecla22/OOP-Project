package nl.rug.oop.rts.view.game;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import lombok.AccessLevel;
import lombok.Getter;
import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.model.Map;
import nl.rug.oop.rts.observing.Observer;
import nl.rug.oop.rts.view.map.MapView;

@Getter(AccessLevel.PROTECTED)
public class GameView extends JPanel implements Observer {
    private Game game;
    private Map map;

    public GameView(Game game, Map map) {
        super();
        this.game = game;
        this.map = map;

        this.setLayout(new BorderLayout());

        this.map.addObserver(this);
        this.add(buildTopBar(), BorderLayout.PAGE_START);

        this.map.addObserver(this);

        MapView mapView = new MapView(game, map);
        this.add(mapView, BorderLayout.CENTER);
    }

    @Override
    public void update() {
        if (this.map.getSelectedNode() == null) {
            this.removeEdgeButton.setEnabled(false);
        } else {
            this.removeEdgeButton.setEnabled(true);
        }

        this.repaint();
    }

    private JButton addEdgeButton = new JButton("Add Edge");
    private JButton removeEdgeButton = new JButton("Remove Edge");
    private JButton addNodeButton = new JButton("Add Node");
    private JButton removeNodeButton = new JButton("Remove Node");

    private JPanel buildTopBar() {
        JPanel topBar = new JPanel();

        // Add back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            this.game.handleBack();
        });

        // Add all buttons to the top bar
        topBar.add(backButton);

        topBar.add(addEdgeButton);
        topBar.add(removeEdgeButton);

        topBar.add(addNodeButton);
        topBar.add(removeNodeButton);

        removeNodeButton.addActionListener(e -> {
            this.map.removeNode(this.map.getSelectedNode());
        });

        removeEdgeButton.setEnabled(false);

        return topBar;
    }
}

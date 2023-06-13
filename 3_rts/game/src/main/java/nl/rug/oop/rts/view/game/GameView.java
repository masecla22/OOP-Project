package nl.rug.oop.rts.view.game;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import lombok.AccessLevel;
import lombok.Getter;
import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.controller.map.MapController;
import nl.rug.oop.rts.interfaces.observing.Observer;
import nl.rug.oop.rts.model.Edge;
import nl.rug.oop.rts.model.Map;
import nl.rug.oop.rts.model.Node;
import nl.rug.oop.rts.view.map.MapView;

@Getter(AccessLevel.PROTECTED)
public class GameView extends JPanel implements Observer {
    private Game game;
    private Map map;

    private MapController mapController;

    public GameView(Game game, Map map, MapController mapController) {
        super();
        this.game = game;
        this.map = map;
        this.mapController = mapController;

        this.setLayout(new BorderLayout());
        this.add(buildTopBar(), BorderLayout.PAGE_START);

        this.map.addObserver(this);

        MapView mapView = new MapView(map, mapController);
        this.add(mapView, BorderLayout.CENTER);
        this.add(new SidePanelView(map), BorderLayout.LINE_START);
    }

    @Override
    public void update() {
        if (this.map.getSelection() == null || !(this.map.getSelection() instanceof Node)) {
            this.removeNodeButton.setEnabled(false);
            this.addEdgeButton.setEnabled(false);
        } else {
            this.removeNodeButton.setEnabled(true);
            this.addEdgeButton.setEnabled(true);
        }

        if (this.map.getSelection() == null || !(this.map.getSelection() instanceof Edge)) {
            this.removeEdgeButton.setEnabled(false);
        } else {
            this.removeEdgeButton.setEnabled(true);
        }

        if (!this.mapController.isAddingEdge())
            this.addEdgeButton.setText("Add Edge");

        this.repaint();
    }

    private JButton addEdgeButton = new JButton("Add Edge");
    private JButton removeEdgeButton = new JButton("Remove Edge");
    private JButton addNodeButton = new JButton("Add Node");
    private JButton removeNodeButton = new JButton("Remove Node");

    private JButton addArmyButton = new JButton("Add Army");

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
        addEdgeButton.addActionListener(e -> {
            this.mapController.markAddingEdge();
            this.mapController.getAddingEdge().thenAccept(endNode -> {
                Node nodeA = (Node) this.getMap().getSelection();
                Node nodeB = endNode;

                if (!nodeA.equals(nodeB)) {
                    if (nodeA != null && nodeB != null) {
                        this.mapController.addEdge(nodeA, nodeB);
                    }
                }

                this.mapController.unmarkAddingEdge();
                this.mapController.setSelection(null);
            });

            addEdgeButton.setText("Select a second node...");
        });

        addEdgeButton.setEnabled(false);

        topBar.add(removeEdgeButton);

        topBar.add(addNodeButton);
        addNodeButton.addActionListener(e -> {
            this.mapController.createNode(JOptionPane.showInputDialog("What is the name of the node?"));
        });

        topBar.add(removeNodeButton);

        removeNodeButton.setEnabled(false);
        removeNodeButton.addActionListener(e -> {
            if (this.map.getSelection() instanceof Node selectedNode)
                this.mapController.removeNode(selectedNode);
            this.mapController.setSelection(null);
        });

        removeEdgeButton.setEnabled(false);
        removeEdgeButton.addActionListener(e -> {
            if (this.map.getSelection() instanceof Edge selectedEdge)
                this.mapController.removeEdge(selectedEdge);
            this.map.setSelection(null);
        });

        /**topBar.add(addArmyButton);
        addArmyButton.addActionListener(e -> {
            // this.map.addArmy();
        });*/

        return topBar;
    }
}

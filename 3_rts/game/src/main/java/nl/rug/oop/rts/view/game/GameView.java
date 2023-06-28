package nl.rug.oop.rts.view.game;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import lombok.AccessLevel;
import lombok.Getter;
import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.controller.map.MapController;
import nl.rug.oop.rts.controller.map.MapSimulationController;
import nl.rug.oop.rts.protocol.objects.interfaces.observing.Observer;
import nl.rug.oop.rts.protocol.objects.model.Edge;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.objects.model.Node;
import nl.rug.oop.rts.view.View;
import nl.rug.oop.rts.view.map.MapView;

/**
 * This class is responsible for presenting the game to the user.
 */
@Getter(AccessLevel.PROTECTED)
public class GameView extends View implements Observer {
    private Game game;
    private Map map;
    private MapController mapController;
    private MapSimulationController simulationController;

    private JButton addEdgeButton = new JButton("Add Edge");
    private JButton removeEdgeButton = new JButton("Remove Edge");
    private JButton addNodeButton = new JButton("Add Node");
    private JButton removeNodeButton = new JButton("Remove Node");

    /**
     * Constructor for the game view.
     * 
     * @param game                 - the game
     * @param map                  - the map
     * @param mapController        - the map controller
     * @param simulationController - the simulation controller
     */
    public GameView(Game game, Map map, MapController mapController, MapSimulationController simulationController) {
        this.game = game;

        this.map = map;
        this.mapController = mapController;
        this.simulationController = simulationController;

        this.setLayout(new BorderLayout());
        this.add(buildTopBar(), BorderLayout.PAGE_START);

        this.map.addObserver(this);

        MapView mapView = new MapView(map, mapController);
        this.add(mapView, BorderLayout.CENTER);
        this.add(new SidePanelView(map, mapController), BorderLayout.LINE_START);
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

        if (!this.mapController.isAddingEdge()) {
            this.addEdgeButton.setText("Add Edge");
        }

        this.repaint();
    }

    private JPanel buildTopBar() {
        JPanel topBar = new JPanel();

        addBackButton(topBar);
        addCreateEdgeButton(topBar);
        addCreateNodeButton(topBar);
        addRemoveNodeButton(topBar);
        addRemoveEdgeButton(topBar);
        addSimulateStepButton(topBar);
        addExportToJsonButton(topBar);

        return topBar;
    }

    private void addRemoveEdgeButton(JPanel topBar) {
        removeEdgeButton.addActionListener(e -> {
            if (this.map.getSelection() instanceof Edge selectedEdge) {
                this.mapController.removeEdge(selectedEdge);
            }
            this.map.setSelection(null);
        });
        removeEdgeButton.setEnabled(false);
        topBar.add(removeEdgeButton);
    }

    private void addRemoveNodeButton(JPanel topBar) {
        removeNodeButton.addActionListener(e -> {
            if (this.map.getSelection() instanceof Node selectedNode) {
                this.mapController.removeNode(selectedNode);
            }
            this.mapController.setSelection(null);
        });
        removeNodeButton.setEnabled(false);
        topBar.add(removeNodeButton);
    }

    private void addSimulateStepButton(JPanel topBar) {
        JButton simulateStep = new JButton("Simulate Step");
        simulateStep.addActionListener(e -> {
            this.simulationController.simulateStep();
        });
        topBar.add(simulateStep);
    }

    private void addExportToJsonButton(JPanel topBar) {
        JButton exportToJson = new JButton("Export to JSON");
        exportToJson.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");
            fileChooser.addActionListener((selected) -> {
                if (selected.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                    try {
                        this.mapController.exportToJson(fileChooser.getSelectedFile());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            fileChooser.showOpenDialog(this);
        });
        topBar.add(exportToJson);
    }

    private void addCreateNodeButton(JPanel topBar) {
        addNodeButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("What is the name of the node?");
            if (name != null) {
                this.mapController.createNode(name);
            }
        });
        topBar.add(addNodeButton);
    }

    private void addCreateEdgeButton(JPanel topBar) {
        addEdgeButton.addActionListener(e -> handleAddEdge());
        addEdgeButton.setEnabled(false);

        topBar.add(addEdgeButton);
    }

    private void handleAddEdge() {
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
    }

    private void addBackButton(JPanel topBar) {
        // Add back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            this.game.handleBack();
        });
        // Add all buttons to the top bar
        topBar.add(backButton);
    }
}

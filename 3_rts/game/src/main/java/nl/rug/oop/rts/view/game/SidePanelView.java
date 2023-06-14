package nl.rug.oop.rts.view.game;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.interfaces.Selectable;
import nl.rug.oop.rts.interfaces.observing.Observer;
import nl.rug.oop.rts.model.Edge;
import nl.rug.oop.rts.model.Map;
import nl.rug.oop.rts.model.Node;

public class SidePanelView extends JPanel implements Observer {
    private Map map;
    private Game game;

    private Selectable showingOptionsFor;

    public SidePanelView(Map map) {
        super();
        this.map = map;
        this.map.addObserver(this);

        // It looks awful without this
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(200, 0));

        // Set the initial state
        this.showNoNodeSelected(true);
    }

    @Override
    public void update() {
        if (this.map.getSelection() == null)
            this.showNoNodeSelected(false);
        else if (this.map.getSelection() instanceof Node)
            this.showNodeOptions();
        else if (this.map.getSelection() instanceof Edge)
            this.showEdgeOptions();

        Observer.super.update();
    }

    private void showNodeOptions() {
        if (showingOptionsFor != null && showingOptionsFor.equals(this.map.getSelection()))
            return;

        showingOptionsFor = this.map.getSelection();

        this.removeAll();

        Node selectedNode = (Node) showingOptionsFor;

        JPanel nodeOptions = new JPanel();
        nodeOptions.setLayout(new GridLayout(4, 1, 1, 2));
        this.add(nodeOptions);

        JLabel node = new JLabel("Node:", SwingConstants.CENTER);
        node.setPreferredSize(new Dimension(50, 30));
        nodeOptions.add(node);

        JTextField nodeName = new JTextField(selectedNode.getName(), 10);
        nodeName.setPreferredSize(new Dimension(50, 30));
        nodeOptions.add(nodeName);

        // add buttons for adding/removing armies on/from selected node

        /**addNodeButton.addActionListener(e -> {
            this.mapController.createNode(JOptionPane.showInputDialog("What is the name of the node?"));
        });*/

        JButton addArmy = new JButton("Add army");

        nodeOptions.add(addArmy);


        String[] options = {"Men", "Elves", "Dwarves", "Mordor", "Isengard"};
        addArmy.addActionListener(e -> {
            this.game.handleAddArmy(JOptionPane.showOptionDialog(null,
                    "Which army would you like to add?",
                    "Select a faction",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null, options, null));
        });

        JButton removeArmy = new JButton("Remove army");
        removeArmy.setPreferredSize(new Dimension(50, 30));
        removeArmy.addActionListener(e -> {
            // this.game.handleRemoveArmy();
        });
        nodeOptions.add(removeArmy);

        nodeName.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                selectedNode.setName(nodeName.getText());
                map.update();
                nodeName.requestFocusInWindow();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                selectedNode.setName(nodeName.getText());
                map.update();
                nodeName.requestFocusInWindow();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                selectedNode.setName(nodeName.getText());
                map.update();
                nodeName.requestFocusInWindow();
            }
        });

    }

    private void showEdgeOptions() {
        if (showingOptionsFor != null && showingOptionsFor.equals(this.map.getSelection()))
            return;

        showingOptionsFor = this.map.getSelection();
        this.removeAll();

        Edge selectedEdge = (Edge) showingOptionsFor;

        this.add(new JLabel("Edge connects \n"));
        this.add(new JLabel(selectedEdge.getPointA().getName() + " - " + selectedEdge.getPointB().getName()));
    }

    private void showNoNodeSelected(boolean ignoreSelection) {
        if (!ignoreSelection && showingOptionsFor == null)
            return;

        showingOptionsFor = null;
        this.removeAll();

        this.add(new JLabel("Nothing selected right now!"));
    }
}
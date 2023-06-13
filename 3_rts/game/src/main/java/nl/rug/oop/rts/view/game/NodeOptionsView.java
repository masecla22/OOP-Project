package nl.rug.oop.rts.view.game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.interfaces.observing.Observer;
import nl.rug.oop.rts.model.Edge;
import nl.rug.oop.rts.model.Map;
import nl.rug.oop.rts.model.Node;

import java.awt.*;

public class NodeOptionsView extends JPanel implements Observer {
    private Map map;
    private Game game;

    private Node nodeShowingOptionsFor = null;

    public NodeOptionsView(Map map) {
        super();
        this.map = map;
        this.map.addObserver(this);

        // It looks awful without this
        setBorder(new EmptyBorder(10, 10, 10, 10));
        this.add(new JLabel("No node selected right now!"));
    }

    @Override
    public void update() {
        if (this.map.getSelection() == null)
            this.showNoNodeSelected();
        else if (this.map.getSelection() instanceof Node)
            this.showNodeOptions();
        else if (this.map.getSelection() instanceof Edge)
            this.showEdgeOptions();

        Observer.super.update();
    }

    private void showNodeOptions() {
        if (nodeShowingOptionsFor != null && nodeShowingOptionsFor.equals(this.map.getSelection()))
            return;

        nodeShowingOptionsFor = (Node) this.map.getSelection();

        this.removeAll();

        Node selectedNode = (Node) this.map.getSelection();

        JPanel nodeOptions = new JPanel();
        nodeOptions.setLayout(new GridLayout(4, 1, 1, 2));
        this.add(nodeOptions);

        JLabel node = new JLabel("Node:", SwingConstants.CENTER);
        node.setPreferredSize(new Dimension(50, 30));
        nodeOptions.add(node);

        JTextField nodeName = new JTextField(selectedNode.getName(), 10);
        nodeName.setPreferredSize(new Dimension(50, 30));
        nodeOptions.add(nodeName);

        //add buttons for adding/removing armies on/from selected node

        JButton addArmy = new JButton("Add army");
        /**addArmy.setPreferredSize(new Dimension(50, 30));
        String[] armies = { "Men", "Elves", "Dwarves", "Mordor", "Isengard" };
        JComboBox<String> armiesDropDown = new JComboBox<>(armies);*/

        nodeOptions.add(addArmy);
        //nodeOptions.add(armiesDropDown);

        addArmy.addActionListener(e -> {
            this.game.handleAddArmy(nodeOptions);
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
        if (nodeShowingOptionsFor != null)
            nodeShowingOptionsFor = null;

        this.removeAll();

        Edge selectedEdge = (Edge) this.map.getSelection();

        this.add(new JLabel("Edge connects \n"));
        this.add(new JLabel(selectedEdge.getPointA().getName() + " - " + selectedEdge.getPointB().getName()));
    }

    private void showNoNodeSelected() {
        if (nodeShowingOptionsFor == null)
            return;

        nodeShowingOptionsFor = null;
        this.removeAll();

        this.add(new JLabel("No node selected right now!"));
    }
}

package nl.rug.oop.rts.view.game;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import nl.rug.oop.rts.controller.map.MapController;
import nl.rug.oop.rts.interfaces.Selectable;
import nl.rug.oop.rts.interfaces.observing.Observer;
import nl.rug.oop.rts.model.Edge;
import nl.rug.oop.rts.model.Map;
import nl.rug.oop.rts.model.Node;
import nl.rug.oop.rts.model.armies.Army;
import nl.rug.oop.rts.model.armies.Faction;
import nl.rug.oop.rts.model.events.Event;
import nl.rug.oop.rts.model.events.EventType;

public class SidePanelView extends JPanel implements Observer {
    private Map map;

    private Selectable showingOptionsFor;
    private MapController mapController;

    public SidePanelView(Map map, MapController mapController) {
        super();
        this.map = map;
        this.map.addObserver(this);

        this.mapController = mapController;

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
        nodeOptions.setLayout(new GridLayout(6, 1, 1, 10));
        this.add(nodeOptions);

        JLabel node = new JLabel("Node:", SwingConstants.CENTER);
        node.setPreferredSize(new Dimension(50, 30));
        nodeOptions.add(node);

        JTextField nodeName = new JTextField(selectedNode.getName(), 10);
        nodeName.setPreferredSize(new Dimension(50, 30));
        nodeOptions.add(nodeName);

        // add buttons for adding/removing armies on/from selected node
        JButton addArmy = new JButton("Add army");

        nodeOptions.add(addArmy);

        addArmy.addActionListener(e -> {
            int pickedOption = JOptionPane.showOptionDialog(null,
                    "Which army would you like to add?",
                    "Select a faction",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null, Faction.values(), null);

            if (pickedOption != JOptionPane.CLOSED_OPTION) {
                Faction faction = Faction.values()[pickedOption];
                this.mapController.addArmy(selectedNode, faction);
            }
        });

        JButton addEvent = new JButton("Add event");
        addEvent.setPreferredSize(new Dimension(50, 30));
        addEvent.addActionListener(e -> {
            EventType pickedOption = (EventType) JOptionPane.showInputDialog(null,
                    "Which event would you like to add?",
                    "Select an event",
                    JOptionPane.QUESTION_MESSAGE,
                    null, EventType.values(), null);

            if (pickedOption != null) {
                this.mapController.addEvent(selectedNode, pickedOption);
            }
        });
        nodeOptions.add(addEvent);

        JButton removeArmy = new JButton("Remove army");
        removeArmy.setPreferredSize(new Dimension(50, 30));
        removeArmy.addActionListener(e -> {
            Army army = (Army) JOptionPane.showInputDialog(null,
                    "Which army would you like to remove?",
                    "Select an army",
                    JOptionPane.QUESTION_MESSAGE,
                    null, selectedNode.getArmies().toArray(), null);

            if (army != null)
                this.mapController.removeArmy(selectedNode, army);
        });
        nodeOptions.add(removeArmy);

        JButton removeEvent = new JButton("Remove event");
        removeEvent.setPreferredSize(new Dimension(50, 30));
        removeEvent.addActionListener(e -> {
            Event event = (Event) JOptionPane.showInputDialog(null,
                    "Which event would you like to remove?",
                    "Select an event",
                    JOptionPane.QUESTION_MESSAGE,
                    null, selectedNode.getEvents().toArray(), null);

            if (event != null)
                this.mapController.removeEvent(selectedNode, event);
        });

        nodeOptions.add(removeEvent);

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

        JPanel edgeOptions = new JPanel();
        edgeOptions.setLayout(new GridLayout(4, 1, 1, 10));
        edgeOptions.setPreferredSize(new Dimension(140, 200));
        this.add(edgeOptions);

        Edge selectedEdge = (Edge) showingOptionsFor;

        edgeOptions.add(new JLabel("Edge connects \n"));
        edgeOptions.add(new JLabel(selectedEdge.getPointA().getName() + " - " + selectedEdge.getPointB().getName()));

        JButton addEvent = new JButton("Add event");
        addEvent.setPreferredSize(new Dimension(50, 30));
        addEvent.addActionListener(e -> {
            EventType pickedOption = (EventType) JOptionPane.showInputDialog(null,
                    "Which event would you like to add?",
                    "Select an event",
                    JOptionPane.QUESTION_MESSAGE,
                    null, EventType.values(), null);

            if (pickedOption != null) {
                this.mapController.addEvent(selectedEdge, pickedOption);
            }
        });
        edgeOptions.add(addEvent);

        JButton removeEvent = new JButton("Remove event");
        removeEvent.setPreferredSize(new Dimension(50, 30));
        removeEvent.addActionListener(e -> {
            Event event = (Event) JOptionPane.showInputDialog(null,
                    "Which event would you like to remove?",
                    "Select an event",
                    JOptionPane.QUESTION_MESSAGE,
                    null, selectedEdge.getEvents().toArray(), null);

            if (event != null)
                this.mapController.removeEvent(selectedEdge, event);
        });
        edgeOptions.add(removeEvent);
    }

    private void showNoNodeSelected(boolean ignoreSelection) {
        if (!ignoreSelection && showingOptionsFor == null)
            return;

        showingOptionsFor = null;
        this.removeAll();

        this.add(new JLabel("Nothing selected right now!"));
    }
}

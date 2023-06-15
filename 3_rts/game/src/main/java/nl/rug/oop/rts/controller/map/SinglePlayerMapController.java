package nl.rug.oop.rts.controller.map;

import java.awt.Point;

import nl.rug.oop.rts.protocol.objects.model.Edge;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.objects.model.Node;
import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.armies.Faction;
import nl.rug.oop.rts.protocol.objects.model.events.Event;
import nl.rug.oop.rts.protocol.objects.model.events.EventFactory;
import nl.rug.oop.rts.protocol.objects.model.events.EventType;
import nl.rug.oop.rts.protocol.objects.model.units.UnitFactory;

public class SinglePlayerMapController extends MapController {

    private UnitFactory unitFactory;
    private EventFactory eventFactory;

    public SinglePlayerMapController(UnitFactory unitFactory, EventFactory eventFactory, Map map) {
        super(map);
        this.unitFactory = unitFactory;
        this.eventFactory = eventFactory;
    }

    @Override
    public void addEdge(Node node1, Node node2) {
        int nextEdgeId = this.getMap().getNextEdgeId();
        Edge edge = new Edge(nextEdgeId, node1, node2);
        this.getMap().addEdge(edge);
    }

    @Override
    public Node createNode(String nodeName) {
        int availableId = this.getMap().getNextNodeId();

        Point offset = this.getMap().getOffset();
        Point point = new Point(-offset.x + 100, -offset.y + 100);

        Node node = new Node(availableId, point, nodeName);
        this.getMap().addNode(node);

        return node;
    }

    @Override
    public void removeEdge(Edge edge) {
        this.getMap().removeEdge(edge);
    }

    @Override
    public void removeNode(Node node) {
        this.getMap().removeNode(node);
    }

    @Override
    public void addArmy(Node node, Faction faction) {
        Army army = unitFactory.buildArmy(faction);
        node.addArmy(army);
        this.getMap().update();
    }

    @Override
    public void removeArmy(Node node, Army army) {
        node.removeArmy(army);
        this.getMap().update();
    }

    @Override
    public void addEvent(Node node, EventType type) {
        Event event = eventFactory.build(type);
        node.addEvent(event);
    }

    @Override
    public void addEvent(Edge edge, EventType type) {
        Event event = eventFactory.build(type);
        edge.addEvent(event);
    }

    @Override
    public void removeEvent(Node node, Event event) {
        node.removeEvent(event);
    }

    @Override
    public void removeEvent(Edge edge, Event event) {
        edge.removeEvent(event);
    }

}

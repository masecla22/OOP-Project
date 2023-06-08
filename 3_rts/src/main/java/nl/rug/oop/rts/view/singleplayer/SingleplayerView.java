package nl.rug.oop.rts.view.singleplayer;

import java.awt.Point;

import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.model.Edge;
import nl.rug.oop.rts.model.Map;
import nl.rug.oop.rts.model.Node;
import nl.rug.oop.rts.view.game.GameView;

public class SingleplayerView extends GameView {
    public SingleplayerView(Game game, Map map) {
        super(game, map);
    }

    @Override
    public void addEdge(Node node1, Node node2) {
        int availableId = this.getMap().getNextEdgeId();

        Edge edge = new Edge(availableId, node1, node2);
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

}

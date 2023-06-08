package nl.rug.oop.rts.view.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JPanel;

import nl.rug.oop.rts.controller.mouse.MapMouseHandler;
import nl.rug.oop.rts.interfaces.observing.Observer;
import nl.rug.oop.rts.model.Edge;
import nl.rug.oop.rts.model.Map;
import nl.rug.oop.rts.model.Node;
import nl.rug.oop.rts.util.TextureLoader;

public class MapView extends JPanel implements Observer {
    private Map map;

    public MapView(Map map) {
        super();
        this.map = map;

        this.map.addObserver(this);

        MapMouseHandler mapMouseHandler = new MapMouseHandler(this.map);
        this.addMouseListener(mapMouseHandler);
        this.addMouseMotionListener(mapMouseHandler);
    }

    @Override
    public void paint(Graphics g) {
        if (!(g instanceof Graphics2D)) {
            throw new RuntimeException("Graphics object is not a Graphics2D object!");
        }

        // Draw background
        g.drawImage(TextureLoader.getInstance().getTexture("mapTexture", this.getWidth(), this.getHeight()), 0, 0,
                null);

        Graphics2D g2d = (Graphics2D) g;

        // Draw all the edges
        map.getEdges().forEach((c) -> renderEdge(g2d, c));

        // Draw the fake edge (from a selected node to the mouse)
        if (map.isAddingEdge()) {
            Point position = this.getMousePosition();

            if (position != null) {
                // Create a fake node which represents the mouse (it won't be rendered)
                Point fakePosition = map.subtractOffset(position);
                Node fakeNode = new Node(-1, fakePosition, "Fake Node");

                // Draw the edge between the fake node and the selected node
                Edge fakeEdge = new Edge(-1, fakeNode, (Node) map.getSelection());
                renderEdge(g2d, fakeEdge);
            }
        }

        map.getNodes().forEach((c) -> renderNode(g2d, c));
    }

    private void renderNode(Graphics2D g, Node node) {
        if (map.getSelection() instanceof Node selectedNode && node.equals(selectedNode)) {
            Color toUse = Color.RED;
            if (map.isAddingEdge()) {
                toUse = Color.BLUE;
            }

            g.setColor(toUse);
            g.setStroke(new BasicStroke(3));
            Point position = node.getPosition();
            position = map.addOffset(position);

            int actualSize = Node.NODE_SIZE + 10;
            g.drawRect(position.x - actualSize / 2, position.y - actualSize / 2, actualSize, actualSize);
        }

        Point position = node.getPosition();
        position = map.addOffset(position);

        Image image = TextureLoader.getInstance().getTexture("factionMordor", Node.NODE_SIZE, Node.NODE_SIZE);
        g.drawImage(image, position.x - Node.NODE_SIZE / 2, position.y - Node.NODE_SIZE / 2, null);

        g.setColor(Color.BLACK);

        int textWidth = g.getFontMetrics().stringWidth(node.getName());
        int textHeight = g.getFontMetrics().getHeight();

        g.drawString(node.getName(), position.x - textWidth / 2, position.y - Node.NODE_SIZE / 2 - textHeight);
    }

    private void renderEdge(Graphics2D g, Edge edge) {
        Point pointA = edge.getPointA().getPosition();
        Point pointB = edge.getPointB().getPosition();

        pointA = map.addOffset(pointA);
        pointB = map.addOffset(pointB);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[] { 9 }, 0));

        g.drawLine(pointA.x, pointA.y, pointB.x, pointB.y);

        // Draw a little circle in the middle
        int handleSize = 10;

        g.fillOval((pointA.x + pointB.x - (int) (handleSize * 1.4)) / 2,
                (pointA.y + pointB.y - (int) (handleSize * 1.4)) / 2,
                (int) (handleSize * 1.4), (int) (handleSize * 1.4));
        g.setColor(Color.WHITE);

        if (this.map.getSelection() instanceof Edge selectedEdge && edge.equals(selectedEdge)) {
            g.setColor(Color.RED);
        }

        g.fillOval((pointA.x + pointB.x - handleSize) / 2, (pointA.y + pointB.y - handleSize) / 2, handleSize,
                handleSize);
    }
}

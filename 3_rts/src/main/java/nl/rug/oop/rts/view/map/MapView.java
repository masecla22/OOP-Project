package nl.rug.oop.rts.view.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JPanel;

import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.controller.mouse.MapMouseHandler;
import nl.rug.oop.rts.model.Edge;
import nl.rug.oop.rts.model.Map;
import nl.rug.oop.rts.model.Node;
import nl.rug.oop.rts.observing.Observer;
import nl.rug.oop.rts.util.TextureLoader;

public class MapView extends JPanel implements Observer {
    private Game game;

    private Map map;

    public MapView(Game game, Map map) {
        super();
        this.game = game;
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

        // Draw all the nodes
        map.getEdges().forEach((c) -> renderEdges(g2d, c));
        map.getNodes().forEach((c) -> renderNode(g2d, c));
    }

    private void renderNode(Graphics2D g, Node node) {
        if (node.equals(map.getSelectedNode())) {
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(3));
            Point position = node.getPosition();
            position = map.transformPoint(position);

            int actualSize = Node.NODE_SIZE + 10;
            g.drawRect(position.x - actualSize / 2, position.y - actualSize / 2, actualSize,
                    actualSize);
        }

        Point position = node.getPosition();
        position = map.transformPoint(position);

        Image image = TextureLoader.getInstance().getTexture("factionMordor", Node.NODE_SIZE, Node.NODE_SIZE);
        g.drawImage(image, position.x - Node.NODE_SIZE / 2, position.y - Node.NODE_SIZE / 2, null);

        g.setColor(Color.BLACK);

        int textWidth = g.getFontMetrics().stringWidth(node.getName());
        int textHeight = g.getFontMetrics().getHeight();

        g.drawString(node.getName(), position.x - textWidth / 2, position.y - Node.NODE_SIZE / 2 - textHeight);
    }

    private void renderEdges(Graphics2D g, Edge edge) {
        Point pointA = edge.getPointA().getPosition();
        Point pointB = edge.getPointB().getPosition();

        pointA = map.transformPoint(pointA);
        pointB = map.transformPoint(pointB);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[] { 9 }, 0));

        g.drawLine(pointA.x, pointA.y, pointB.x, pointB.y);
    }
}

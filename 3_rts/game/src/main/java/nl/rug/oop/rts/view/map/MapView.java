package nl.rug.oop.rts.view.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import javax.swing.JPanel;

import nl.rug.oop.rts.controller.map.MapController;
import nl.rug.oop.rts.controller.mouse.MapMouseHandler;
import nl.rug.oop.rts.interfaces.observing.Observer;
import nl.rug.oop.rts.model.Edge;
import nl.rug.oop.rts.model.Map;
import nl.rug.oop.rts.model.Node;
import nl.rug.oop.rts.model.armies.Army;
import nl.rug.oop.rts.model.armies.Faction;
import nl.rug.oop.rts.model.armies.Team;
import nl.rug.oop.rts.util.TextureLoader;

public class MapView extends JPanel implements Observer {

    public static final int MAP_SIZE = 2000;

    private Map map;
    private MapController mapController;

    public MapView(Map map, MapController mapController) {
        super();
        this.map = map;
        this.mapController = mapController;

        this.map.addObserver(this);

        MapMouseHandler mapMouseHandler = new MapMouseHandler(this.map, mapController);
        this.addMouseListener(mapMouseHandler);
        this.addMouseMotionListener(mapMouseHandler);
    }

    @Override
    public void paint(Graphics g) {
        if (!(g instanceof Graphics2D)) {
            throw new RuntimeException("Graphics object is not a Graphics2D object!");
        }

        Point offset = map.getOffset();

        // Draw background
        g.drawImage(TextureLoader.getInstance().getTexture("mapTexture", MAP_SIZE, MAP_SIZE),
                offset.x, offset.y, null);

        Graphics2D g2d = (Graphics2D) g;

        // Draw all the edges
        map.getEdges().forEach((c) -> renderEdge(g2d, c));

        // Draw the fake edge (from a selected node to the mouse)
        if (mapController.isAddingEdge()) {
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

    private boolean shouldRenderNode(Node node) {
        Point position = node.getPosition();
        position = map.addOffset(position);

        Point viewportCenter = new Point(this.getWidth() / 2, this.getHeight() / 2);

        return viewportCenter.distanceSq(position) < 250000;
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

        Point center = new Point((pointA.x + pointB.x) / 2, (pointA.y + pointB.y) / 2);

        renderArmies(g, center, edge.getArmies());
    }

    private void renderNode(Graphics2D g, Node node) {
        if (!shouldRenderNode(node)) {
            return;
        }

        if (map.getSelection() instanceof Node selectedNode && node.equals(selectedNode)) {
            Color toUse = Color.RED;
            if (mapController.isAddingEdge()) {
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

        Image image = TextureLoader.getInstance().getTexture("node3", Node.NODE_SIZE, Node.NODE_SIZE);
        g.drawImage(image, position.x - Node.NODE_SIZE / 2, position.y - Node.NODE_SIZE / 2, null);

        g.setColor(Color.BLACK);

        int textWidth = g.getFontMetrics().stringWidth(node.getName());
        int textHeight = g.getFontMetrics().getHeight();

        g.drawString(node.getName(), position.x - textWidth / 2, position.y - Node.NODE_SIZE / 2 - textHeight);

        renderArmies(g, position, node.getArmies());
    }

    private void renderArmies(Graphics2D g, Point position, List<Army> allArmies) {
        List<Army> teamA = allArmies.stream().filter((c) -> c.getFaction().getTeam().equals(Team.TEAM_A)).toList();
        List<Army> teamB = allArmies.stream().filter((c) -> c.getFaction().getTeam().equals(Team.TEAM_B)).toList();

        double emptySpace = Math.PI / 5;
        double halfPi = Math.PI / 2;

        renderArmiesAt(g, position, teamA, halfPi - emptySpace, -halfPi + emptySpace);
        renderArmiesAt(g, position, teamB, halfPi + emptySpace, Math.PI + halfPi - emptySpace);
    }

    private void renderArmiesAt(Graphics2D g, Point position, List<Army> army, double beginAngle, double endAngle) {
        double angle = endAngle - beginAngle;
        double angleStep = angle / (army.size() + 1);

        int radiusToDrawAt = 45;

        for (int i = 0; i < army.size(); i++) {
            double currentAngle = beginAngle + angleStep * (i + 1);

            int x = (int) (Math.cos(currentAngle) * radiusToDrawAt) + position.x - Node.NODE_SIZE / 4;
            int y = (int) (Math.sin(currentAngle) * radiusToDrawAt) + position.y - Node.NODE_SIZE / 4;

            renderArmy(g, army.get(i), x, y);
        }
    }

    private void renderArmy(Graphics2D g, Army army, int x, int y) {
        int armyTextureSize = 32;

        Faction faction = army.getFaction();
        Image image = TextureLoader.getInstance().getTexture(faction.getTexture(), armyTextureSize, armyTextureSize);

        if (faction.getTeam().equals(Team.TEAM_A)) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLUE);
        }

        g.fillRect(x - 2, y - 2, armyTextureSize + 4, armyTextureSize + 4);
        g.drawImage(image, x, y, null);

        g.setColor(Color.BLACK);

        String armyString = army.toString();

        int textWidth = g.getFontMetrics().stringWidth(armyString);
        int textHeight = g.getFontMetrics().getHeight();

        // If team a, place text on right of image
        int textPosX = x;
        int textPosY = y + armyTextureSize / 2 + textHeight / 4;

        if (faction.getTeam().equals(Team.TEAM_A)) {
            textPosX += armyTextureSize + 6;
        } else {
            textPosX -= textWidth + 6;
        }

        g.drawString(armyString, textPosX, textPosY);
    }
}

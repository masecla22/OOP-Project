package nl.rug.oop.rts.controller.mouse;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rts.model.Map;

@RequiredArgsConstructor
public class MapMouseHandler extends MouseAdapter {
    @NonNull
    private Map map;

    private Point draggedPoint = null;

    @Override
    public void mousePressed(MouseEvent e) {
        map.setSelectedNode(map.getNodeAt(e.getX(), e.getY()));

        draggedPoint = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point delta = new Point(e.getX() - draggedPoint.x, e.getY() - draggedPoint.y);

        // If selected node is null, we drag map
        if (map.getSelectedNode() == null) {
            map.setOffset(new Point(map.getOffset().x + delta.x, map.getOffset().y + delta.y));
        } else {
            map.getSelectedNode().setPosition(new Point(
                    map.getSelectedNode().getPosition().x + delta.x,
                    map.getSelectedNode().getPosition().y + delta.y));
            map.update();
        }

        draggedPoint = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        draggedPoint = null;
    }

}

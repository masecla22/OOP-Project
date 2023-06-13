package nl.rug.oop.rts.controller.mouse;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rts.controller.map.MapController;
import nl.rug.oop.rts.interfaces.Selectable;
import nl.rug.oop.rts.model.Map;
import nl.rug.oop.rts.model.Node;

@RequiredArgsConstructor
public class MapMouseHandler extends MouseAdapter {
    @NonNull
    private Map map;
    
    @NonNull
    private MapController mapController;

    private Point draggedPoint = null;

    @Override
    public void mousePressed(MouseEvent e) {
        draggedPoint = e.getPoint();

        Selectable selectable = map.getSelectableAt(e.getX(), e.getY());
        if (selectable instanceof Node node) {
            if (mapController.isAddingEdge()) {
                mapController.getAddingEdge().complete(node);
                return;
            }
        }

        mapController.setSelection(map.getSelectableAt(e.getX(), e.getY()));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (mapController.isAddingEdge()) {
            map.update();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point delta = new Point(e.getX() - draggedPoint.x, e.getY() - draggedPoint.y);

        // If selected node is null, we drag map
        if (map.getSelection() == null) {
            mapController.setOffset(new Point(map.getOffset().x + delta.x, map.getOffset().y + delta.y));
        } else {
            Selectable selection = map.getSelection();

            if (selection instanceof Node node) {
                node.setPosition(new Point(node.getPosition().x + delta.x, node.getPosition().y + delta.y));
                map.update();
            }
        }

        draggedPoint = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        draggedPoint = null;
    }

}

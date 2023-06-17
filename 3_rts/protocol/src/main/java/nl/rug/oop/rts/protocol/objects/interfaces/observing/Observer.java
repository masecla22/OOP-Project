package nl.rug.oop.rts.protocol.objects.interfaces.observing;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Interface for observer objects.
 */
public interface Observer {
    /**
     * Update the observer.
     */
    default void update() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::update);
            return;
        }

        if ((this instanceof JPanel)) {
            ((JPanel) this).revalidate();
            ((JPanel) this).repaint();
        }
    }
}

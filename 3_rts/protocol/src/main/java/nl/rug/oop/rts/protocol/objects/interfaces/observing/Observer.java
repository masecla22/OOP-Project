package nl.rug.oop.rts.protocol.objects.interfaces.observing;

import javax.swing.JPanel;

/**
 * Interface for observer objects.
 */
public interface Observer {
    /**
     * Update the observer.
     */
    default void update() {
        if ((this instanceof JPanel)) {
            ((JPanel) this).revalidate();
            ((JPanel) this).repaint();
        }
    }
}

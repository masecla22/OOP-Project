package nl.rug.oop.rts.observing;

import javax.swing.JPanel;

public interface Observer {
    public default void update() {
        if ((this instanceof JPanel)) {
            ((JPanel) this).repaint();
        }
    }
}

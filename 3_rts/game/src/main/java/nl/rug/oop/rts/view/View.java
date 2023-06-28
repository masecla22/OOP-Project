package nl.rug.oop.rts.view;

import javax.swing.JPanel;

/**
 * Abstract class for all views.
 */
public abstract class View extends JPanel {
    /**
     * Called when the view is closed.
     */
    public void onClose() {
    }

    /**
     * Called when the view is opened.
     */
    public void onOpen() {
    }
}

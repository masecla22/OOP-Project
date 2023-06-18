package nl.rug.oop.rts;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import nl.rug.oop.rts.controller.settings.SettingsController;
import nl.rug.oop.rts.view.MainMenuView;
import nl.rug.oop.rts.view.View;
import nl.rug.oop.rts.view.settings.SettingsView;

/**
 * This class is responsible for the game itself.
 */
public class Game {
    private JFrame frame;

    private List<View> accessedViews = new ArrayList<>();

    private SettingsController settingsController;

    /**
     * Handles the initialization of the game.
     */
    public void initialize() {
        this.settingsController = new SettingsController();

        this.frame = new JFrame("RTS Game");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);

        this.frame.setSize(800, 600);
        this.frame.setLocationRelativeTo(null);

        this.handleView(new MainMenuView(this, settingsController));

        this.frame.setVisible(true);
    }

    /**
     * Handles the quitting of the game.
     */
    public void handleQuitting() {
        this.frame.dispose();
        System.exit(0);
    }

    /**
     * Handles the back button.
     */
    public void handleBack() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::handleBack);
            return;
        }

        if (this.accessedViews.size() > 0) {
            View view = this.accessedViews.remove(this.accessedViews.size() - 1);
            view.onClose();
        }

        View lastView = this.accessedViews.get(this.accessedViews.size() - 1);
        this.frame.setContentPane(lastView);
        lastView.onOpen();

        this.frame.revalidate();
        this.frame.repaint();
    }

    /**
     * Handles going back up to a certain view.
     *
     * @param view - the class of the view to go back up to
     */
    public void handleBackUpTo(Class<? extends View> view) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> handleBackUpTo(view));
            return;
        }

        while (this.accessedViews.size() > 0) {
            View lastView = this.accessedViews.get(this.accessedViews.size() - 1);
            if (lastView.getClass() == view) {
                break;
            }

            lastView.onClose();
            this.accessedViews.remove(this.accessedViews.size() - 1);
        }

        View lastView = this.accessedViews.get(this.accessedViews.size() - 1);
        this.frame.setContentPane(lastView);
        lastView.onOpen();

        this.frame.revalidate();
        this.frame.repaint();
    }

    /**
     * Handles opening a view.
     * 
     * @param panel - the view to open
     */
    public void handleView(View panel) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> handleView(panel));
            return;
        }

        this.accessedViews.add(panel);
        this.frame.setContentPane(panel);

        panel.onOpen();
        this.frame.revalidate();
        this.frame.repaint();
    }

    /**
     * Handles opening the settings.
     */
    public void openSettings() {
        handleView(new SettingsView(this, this.settingsController));
    }
}

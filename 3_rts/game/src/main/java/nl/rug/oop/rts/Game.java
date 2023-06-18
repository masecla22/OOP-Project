package nl.rug.oop.rts;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import nl.rug.oop.rts.controller.settings.SettingsController;
import nl.rug.oop.rts.view.MainMenuClass;
import nl.rug.oop.rts.view.View;
import nl.rug.oop.rts.view.settings.SettingsView;

public class Game {
    private JFrame frame;

    private List<View> accessedViews = new ArrayList<>();

    private SettingsController settingsController;

    /**
     * something
     */
    public void initialize() {
        this.settingsController = new SettingsController();

        this.frame = new JFrame("RTS Game");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);

        this.frame.setSize(800, 600);
        this.frame.setLocationRelativeTo(null);

        this.handleView(new MainMenuClass(this, settingsController));

        this.frame.setVisible(true);
    }

    public void handleQuitting() {
        this.frame.dispose();
        System.exit(0);
    }

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

    public void openSettings() {
        handleView(new SettingsView(this, this.settingsController));
    }
}

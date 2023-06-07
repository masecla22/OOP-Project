package nl.rug.oop.rts;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import nl.rug.oop.rts.view.MainMenuClass;
import nl.rug.oop.rts.view.settings.SettingsView;

public class Game {
    private JFrame frame;

    public void initialize() {
        this.frame = new JFrame("RTS Game");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);

        this.frame.setSize(800, 600);
        this.frame.setLocationRelativeTo(null);

        this.handleView(new MainMenuClass(this));

        this.frame.setVisible(true);
    }

    private List<JPanel> accessedViews = new ArrayList<>();

    public void handleQuitting() {
        this.frame.dispose();

        // Handle additional cleanup here

        System.exit(0);
    }

    public void handleBack() {
        if (this.accessedViews.size() > 0) {
            this.accessedViews.remove(this.accessedViews.size() - 1);
        }

        JPanel lastView = this.accessedViews.get(this.accessedViews.size() - 1);
        this.frame.setContentPane(lastView);

        this.frame.revalidate();
        this.frame.repaint();
    }

    public void handleView(JPanel panel) {
        this.accessedViews.add(panel);
        this.frame.setContentPane(panel);

        this.frame.revalidate();
        this.frame.repaint();
    }

    public void openSettings() {
        handleView(new SettingsView(this));
    }
}

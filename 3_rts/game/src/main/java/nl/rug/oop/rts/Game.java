package nl.rug.oop.rts;

import java.awt.Point;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import nl.rug.oop.rts.controller.map.MapController;
import nl.rug.oop.rts.controller.map.MapSimulationController;
import nl.rug.oop.rts.controller.map.SinglePlayerMapController;
import nl.rug.oop.rts.controller.settings.SettingsController;
import nl.rug.oop.rts.model.Edge;
import nl.rug.oop.rts.model.Map;
import nl.rug.oop.rts.model.Node;
import nl.rug.oop.rts.model.events.EventFactory;
import nl.rug.oop.rts.model.units.UnitFactory;
import nl.rug.oop.rts.view.MainMenuClass;
import nl.rug.oop.rts.view.game.GameView;
import nl.rug.oop.rts.view.game.LoginView;
import nl.rug.oop.rts.view.game.MultiplayerView;
import nl.rug.oop.rts.view.game.RegisterView;
import nl.rug.oop.rts.view.settings.SettingsView;
import nl.rug.oop.rugson.Rugson;
import nl.rug.oop.rugson.RugsonBuilder;

public class Game {
    private JFrame frame;
    private Rugson rugson;

    private List<JPanel> accessedViews = new ArrayList<>();

    /**
     * something
     */
    public void initialize() {
        this.initializeRugson();

        this.frame = new JFrame("RTS Game");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);

        this.frame.setSize(800, 600);
        this.frame.setLocationRelativeTo(null);

        this.handleView(new MainMenuClass(this));

        this.frame.setVisible(true);
    }

    private void initializeRugson() {
        this.rugson = new RugsonBuilder().build();
    }

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

    public void handleSingleplayer() {
        Node a = new Node(0, new Point(100, 100), "bruh");
        Node b = new Node(1, new Point(200, 200), "bruh");
        Node c = new Node(2, new Point(150, 300), "veoj");
        Edge ab = new Edge(0, a, b);
        Edge bc = new Edge(1, b, c);
        Edge ca = new Edge(2, c, a);
        Map map = new Map();
        map.addNode(a);
        map.addNode(b);
        map.addNode(c);
        map.addEdge(ab);
        map.addEdge(bc);
        map.addEdge(ca);

        UnitFactory unitFactory = new UnitFactory((int) Instant.now().toEpochMilli());
        EventFactory factory = new EventFactory();

        MapController spMapController = new SinglePlayerMapController(unitFactory, factory, map);
        MapSimulationController simulationController = new MapSimulationController(map);

        GameView view = new GameView(this, map, spMapController, simulationController);
        this.handleView(view);
    }

    public void openSettings() {
        handleView(new SettingsView(this, new SettingsController(this.rugson)));
    }

    public void handleMultiplayer() {
        handleView(new MultiplayerView(this));
    }

    public void handleLogin() {
        handleView(new LoginView(this));
    }

    public void handleRegister() {
        handleView(new RegisterView(this));
    }
}

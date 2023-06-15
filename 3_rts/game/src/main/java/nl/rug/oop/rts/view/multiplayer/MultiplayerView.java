package nl.rug.oop.rts.view.multiplayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.controller.multiplayer.MultiplayerConnectionController;
import nl.rug.oop.rts.controller.settings.SettingsController;
import nl.rug.oop.rts.protocol.adapters.EventTypeAdapter;
import nl.rug.oop.rts.protocol.adapters.GameMapTypeAdapter;
import nl.rug.oop.rts.protocol.adapters.UnitTypeAdapter;
import nl.rug.oop.rts.protocol.objects.interfaces.observing.Observer;
import nl.rug.oop.rts.protocol.objects.model.events.Event;
import nl.rug.oop.rts.protocol.objects.model.events.EventFactory;
import nl.rug.oop.rts.protocol.objects.model.factories.UnitFactory;
import nl.rug.oop.rts.protocol.objects.model.factories.singleplayer.MultiPlayerUnitFactory;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.view.View;
import nl.rug.oop.rugson.Rugson;
import nl.rug.oop.rugson.RugsonBuilder;

public class MultiplayerView extends View implements Observer {
    private Game game;

    private Rugson rugson;
    private MultiplayerConnectionController connectionController;
    private SettingsController settingsController;

    private UnitFactory unitFactory;
    private EventFactory eventFactory;

    public MultiplayerView(Game game, SettingsController settingsController) {
        this.initializeRugson();

        this.settingsController = settingsController;

        this.unitFactory = new MultiPlayerUnitFactory();
        this.eventFactory = new EventFactory(unitFactory);

        this.game = game;

        this.connectionController = new MultiplayerConnectionController(settingsController, rugson, unitFactory,
                eventFactory);

        addLoading();
    }

    @Override
    public void onClose() {
        this.connectionController.closeConnection();
    }

    @Override
    public void onOpen() {
        System.out.println("attemping login");
        this.attemptLogin();
    }

    private void attemptLogin() {
        try {
            if (!this.connectionController.openConnection()) {
                SwingUtilities.invokeLater(this::addSomethingWrong);
                return;
            }

            this.connectionController.ensureLogin().thenAccept(c -> {
                if (c) {
                    System.out.println("LOGIN SUCCESS " + this.connectionController.getUser().getName());
                } else {
                    SwingUtilities.invokeLater(this::addLoginRegister);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(this::addSomethingWrong);
            return;
        }
    }

    private void addLoading() {
        JLabel loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
        this.add(loadingLabel, BorderLayout.CENTER);
        addBackButton();
        this.update();
    }

    private void addSomethingWrong() {
        this.removeAll();
        this.setLayout(new BorderLayout());
        JLabel somethingWrongLabel = new JLabel("Something went wrong, please try again", SwingConstants.CENTER);
        this.add(somethingWrongLabel, BorderLayout.CENTER);
        addBackButton();
        this.update();
    }

    private void addLoginRegister() {
        this.removeAll();
        this.setLayout(new BorderLayout());
        JPanel userOptions = new JPanel();
        userOptions.setSize(10, 10);
        userOptions.setLayout(new GridLayout(4, 2, 10, 20));

        this.add(userOptions, BorderLayout.CENTER);
        addLayout(userOptions);
        addLoginButton(userOptions);
        addRegisterButton(userOptions);

        addBackButton();
        this.update();
    }

    private void addLayout(JPanel userOptions) {
        JLabel layoutLabel1 = new JLabel();
        JLabel layoutLabel2 = new JLabel();
        userOptions.add(layoutLabel1);
        userOptions.add(layoutLabel2);
    }

    private void addBackButton() {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            this.game.handleBack();
        });

        backButton.setPreferredSize(new Dimension(200, 50));
        this.add(backButton, BorderLayout.PAGE_START);

    }

    private void addLoginButton(JPanel userOptions) {
        JLabel loginLabel = new JLabel("Already have an account?", SwingConstants.CENTER);
        JButton loginButton = new JButton("Log In");
        loginButton.setPreferredSize(new Dimension(50, 26));
        loginButton.addActionListener(e -> {
            handleLogin();
        });
        userOptions.add(loginLabel);
        userOptions.add(loginButton);
    }

    private void addRegisterButton(JPanel userOptions) {
        JLabel registerLabel = new JLabel("New to the game?", SwingConstants.CENTER);
        JButton registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(50, 26));
        registerButton.addActionListener(e -> {
            handleRegister();
        });
        userOptions.add(registerLabel);
        userOptions.add(registerButton);
    }

    private void initializeRugson() {
        this.rugson = new RugsonBuilder()
                .setPrettyPrint(false)
                .addTypeAdapter(Unit.class, new UnitTypeAdapter())
                .addTypeAdapter(Event.class, new EventTypeAdapter(eventFactory))
                .addTypeAdapter(Map.class, new GameMapTypeAdapter())
                .build();
    }

    private void handleRegister() {
        this.game.handleView(new RegisterView(game, settingsController, connectionController));
    }

    private void handleLogin() {
        this.game.handleView(new LoginView(game, settingsController, connectionController));
    }

}

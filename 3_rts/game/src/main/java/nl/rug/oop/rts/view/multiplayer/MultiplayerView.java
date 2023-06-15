package nl.rug.oop.rts.view.multiplayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.controller.multiplayer.MultiplayerConnectionController;
import nl.rug.oop.rts.controller.settings.SettingsController;
import nl.rug.oop.rts.protocol.adapters.EventTypeAdapter;
import nl.rug.oop.rts.protocol.adapters.GameMapTypeAdapter;
import nl.rug.oop.rts.protocol.adapters.UnitTypeAdapter;
import nl.rug.oop.rts.protocol.games.MultiplayerLobby;
import nl.rug.oop.rts.protocol.listeners.AwaitPacketOnce;
import nl.rug.oop.rts.protocol.objects.interfaces.observing.Observer;
import nl.rug.oop.rts.protocol.objects.model.events.Event;
import nl.rug.oop.rts.protocol.objects.model.events.EventFactory;
import nl.rug.oop.rts.protocol.objects.model.factories.UnitFactory;
import nl.rug.oop.rts.protocol.objects.model.factories.singleplayer.MultiPlayerUnitFactory;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.packet.Packet;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyListingRequest;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyListingResponse;
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

    private List<MultiplayerLobby> knownLobbies = new ArrayList<>();

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
                    SwingUtilities.invokeLater(this::handleLobbyView);
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

    private void handleLobbyView() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        JPanel lobbyPanel = new JPanel();
        lobbyPanel.setLayout(new BorderLayout());
        lobbyPanel.add(new JLabel("Logged in as " + this.connectionController.getUser().getName(),
                SwingConstants.CENTER), BorderLayout.PAGE_START);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            requestGames().thenAccept((v) -> SwingUtilities.invokeLater(this::handleLobbyView));
        });

        getLobbyList();

        this.addBackButton();
        this.update();
    }

    private JPanel getLobbyList() {
        JPanel lobbyList = new JPanel();
        lobbyList.setLayout(new BorderLayout());

        String[] columnNames = { "Name", "Creator", "Map", "" };
        Object[][] data = new Object[this.knownLobbies.size()][4];
        for (int i = 0; i < this.knownLobbies.size(); i++) {
            MultiplayerLobby lobby = this.knownLobbies.get(i);
            data[i][0] = lobby.getName();
            data[i][1] = lobby.getHost().getName();
            data[i][2] = lobby.getMapName();

            JButton button = new JButton("Join");
            data[i][3] = button;
            button.addActionListener(e -> {
                this.handleJoinLobby(lobby.getLobbyId());
            });
        }

        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        lobbyList.add(scrollPane, BorderLayout.CENTER);

        return lobbyList;
    }

    private void handleJoinLobby(UUID lobbyId) {
        // TODO:
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

        userOptions.add(new JLabel());
        userOptions.add(new JLabel());

        addLoginButton(userOptions);
        addRegisterButton(userOptions);

        addBackButton();
        this.update();
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

    private CompletableFuture<Void> requestGames() {
        AwaitPacketOnce<Packet> awaitPacketOnce = new AwaitPacketOnce<>(LobbyListingResponse.class)
                .bindTo(connectionController.getConnection());
        this.connectionController.sendAuthenticatedPacket(new LobbyListingRequest());

        return awaitPacketOnce.getAwaiting().thenAccept(c -> {
            LobbyListingResponse response = (LobbyListingResponse) c;
            this.knownLobbies = response.getLobbies();
        });
    }

}

package nl.rug.oop.rts.view;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.controller.settings.SettingsController;
import nl.rug.oop.rts.view.game.SingleplayerMenuView;
import nl.rug.oop.rts.view.multiplayer.MultiplayerView;

public class MainMenuClass extends JPanel implements View {
    private Game game;
    private SettingsController settingsController;

    public MainMenuClass(Game game, SettingsController settingsController) {
        super();
        this.game = game;
        this.settingsController = settingsController;

        GridLayout layout = new GridLayout(5, 0, 0, 20);

        this.setLayout(layout);

        this.addMainTitle(layout);
        this.addButtons(layout);
    }

    private void addMainTitle(GridLayout layout) {
        // Add a large centered label

        JLabel mainTitle = new JLabel("RTS Game");
        mainTitle.setHorizontalAlignment(JLabel.CENTER);
        mainTitle.setFont(mainTitle.getFont().deriveFont(64f));

        this.add(mainTitle);
    }

    private void addButtons(GridLayout layout) {

        // Singleplayer
        JButton singleplayerButton = new JButton("Singleplayer");
        singleplayerButton.addActionListener(e -> {
            handleSingleplayer();
        });

        // Multiplayer
        JButton multiplayerButton = new JButton("Multiplayer");
        multiplayerButton.addActionListener(e -> {
            handleMultiplayer();
        });

        // Settings
        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(e -> {
            this.game.openSettings();
        });

        // Quit
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> {
            this.game.handleQuitting();
        });

        this.add(singleplayerButton);
        this.add(multiplayerButton);
        this.add(settingsButton);
        this.add(quitButton);
    }

    private void handleSingleplayer() {
        SingleplayerMenuView menuView = new SingleplayerMenuView(game);
        game.handleView(menuView);
    }

    private void handleMultiplayer() {
        MultiplayerView multiplayerView = new MultiplayerView(game, this.settingsController);
        game.handleView(multiplayerView);
    }

}

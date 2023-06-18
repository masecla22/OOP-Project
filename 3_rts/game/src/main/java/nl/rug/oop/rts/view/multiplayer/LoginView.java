package nl.rug.oop.rts.view.multiplayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.controller.multiplayer.MultiplayerConnectionController;
import nl.rug.oop.rts.controller.settings.SettingsController;
import nl.rug.oop.rts.protocol.objects.interfaces.observing.Observer;
import nl.rug.oop.rts.view.View;

/**
 * This class is responsible for presenting the login menu to the user.
 */
public class LoginView extends View implements Observer {
    private Game game;

    private SettingsController settingsController;
    private MultiplayerConnectionController connectionController;

    private JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
    private JLabel errorDescriptionLabel = new JLabel("", SwingConstants.CENTER);
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginView(Game game, SettingsController settingsController,
            MultiplayerConnectionController connectionController) {
        this.game = game;
        this.settingsController = settingsController;
        this.connectionController = connectionController;

        this.setLayout(new BorderLayout());
        JPanel loginOptions = new JPanel();
        loginOptions.setSize(10, 10);
        loginOptions.setLayout(new GridLayout(4, 2, 10, 20));

        this.add(loginOptions, BorderLayout.CENTER);

        addBackButton();
        addLayout(loginOptions);
        addUsername(loginOptions);
        addPassword(loginOptions);
        addLoginButton();
    }

    private void addBackButton() {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            this.game.handleBack();
        });

        backButton.setPreferredSize(new Dimension(200, 50));
        this.add(backButton, BorderLayout.PAGE_START);
    }

    private void addLayout(JPanel loginOptions) {
        loginOptions.add(errorLabel);
        loginOptions.add(errorDescriptionLabel);
    }

    private void addUsername(JPanel loginOptions) {
        // this adds a label and a text field
        JLabel usernameLabel = new JLabel("Username:", SwingConstants.CENTER);
        usernameField = new JTextField();

        loginOptions.add(usernameLabel);
        loginOptions.add(usernameField);
    }

    private void addPassword(JPanel loginOptions) {
        // this adds a label and a text field
        JLabel passwordLabel = new JLabel("Password:", SwingConstants.CENTER);
        passwordField = new JPasswordField();

        loginOptions.add(passwordLabel);
        loginOptions.add(passwordField);
    }

    private void addLoginButton() {
        JButton startButton = new JButton("Login");
        startButton.addActionListener(e -> {
            String username = this.usernameField.getText();
            String password = new String(this.passwordField.getPassword());

            this.settingsController.setUsername(username);
            this.settingsController.setPassword(password);
            this.settingsController.save();

            this.attemptLogin();
        });

        startButton.setPreferredSize(new Dimension(200, 50));
        this.add(startButton, BorderLayout.PAGE_END);
    }

    private void attemptLogin() {
        try {
            this.connectionController.ensureLogin().thenAccept(c -> {
                if (c) {
                    SwingUtilities.invokeLater(() -> game.handleBack());
                } else {
                    SwingUtilities.invokeLater(() -> {
                        errorLabel.setText("Error:");
                        errorDescriptionLabel.setText("Invalid username or password");
                        update();
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                errorLabel.setText("Error:");
                errorDescriptionLabel.setText(e.getMessage());
                update();
            });
            return;
        }
    }

}

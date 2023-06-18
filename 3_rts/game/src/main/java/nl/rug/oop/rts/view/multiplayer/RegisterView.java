package nl.rug.oop.rts.view.multiplayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.controller.multiplayer.MultiplayerConnectionController;
import nl.rug.oop.rts.controller.settings.SettingsController;
import nl.rug.oop.rts.protocol.objects.interfaces.observing.Observer;
import nl.rug.oop.rts.view.View;

/**
 * This class is responsible for presenting the register menu to the user.
 */
public class RegisterView extends View implements Observer {
    private Game game;

    private SettingsController settingsController;
    private MultiplayerConnectionController connectionController;

    private JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
    private JLabel errorDescriptionLabel = new JLabel("", SwingConstants.CENTER);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField checkPasswordField;

    /**
     * Constructor for the register view.
     * 
     * @param game                 - the game
     * @param settingsController   - the settings controller
     * @param connectionController - the connection controller
     */
    public RegisterView(Game game, SettingsController settingsController,
            MultiplayerConnectionController connectionController) {
        this.game = game;
        this.settingsController = settingsController;
        this.connectionController = connectionController;

        this.setLayout(new BorderLayout());
        JPanel registerOptions = new JPanel();
        registerOptions.setSize(10, 10);
        registerOptions.setLayout(new GridLayout(5, 2, 10, 20));

        this.add(registerOptions, BorderLayout.CENTER);

        addBackButton();

        registerOptions.add(errorLabel);
        registerOptions.add(errorDescriptionLabel);

        addUsername(registerOptions);
        addPassword(registerOptions);
        checkPassword(registerOptions);
        addSubmitButton();
    }

    private void addBackButton() {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            this.game.handleBack();
        });

        backButton.setPreferredSize(new Dimension(200, 50));
        this.add(backButton, BorderLayout.PAGE_START);

    }

    private void setError(String error) {
        errorLabel.setText("Error");
        errorDescriptionLabel.setText(error);
        this.update();
    }

    private void addUsername(JPanel registerOptions) {
        // this adds a label and a text field
        JLabel usernameLabel = new JLabel("Username:", SwingConstants.CENTER);
        usernameField = new JTextField();

        registerOptions.add(usernameLabel);
        registerOptions.add(usernameField);
    }

    private void addPassword(JPanel registerOptions) {
        // this adds a label and a text field
        JLabel passwordLabel = new JLabel("Password:", SwingConstants.CENTER);
        passwordField = new JPasswordField();

        registerOptions.add(passwordLabel);
        registerOptions.add(passwordField);
    }

    private void checkPassword(JPanel registerOptions) {
        // this adds a label and a text field
        JLabel checkPasswordLabel = new JLabel("Verify Password:", SwingConstants.CENTER);
        checkPasswordField = new JPasswordField();

        registerOptions.add(checkPasswordLabel);
        registerOptions.add(checkPasswordField);
    }

    private void addSubmitButton() {
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            attemptRegister();
        });

        submitButton.setPreferredSize(new Dimension(200, 50));
        this.add(submitButton, BorderLayout.PAGE_END);
    }

    private void attemptRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String checkPassword = new String(checkPasswordField.getPassword());

        if (username == null || username.length() == 0 || password.length() == 0 || checkPassword.length() == 0) {
            setError("Please fill in all fields");
            return;
        }

        if (!password.equals(checkPassword)) {
            setError("Passwords do not match");
            return;
        }

        try {
            this.connectionController.attemptRegister(username, password).thenAccept(c -> {
                if (c.isSuccess()) {
                    this.settingsController.setUsername(username);
                    this.settingsController.setPassword(password);

                    this.game.handleBack();
                } else {
                    setError(c.getError());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            setError(e.getMessage());
            return;
        }

    }
}

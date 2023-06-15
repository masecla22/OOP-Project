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

import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.view.View;

public class RegisterView extends View {
    private Game game;

    public RegisterView(Game game) {
        this.game = game;
        this.setLayout(new BorderLayout());
        JPanel registerOptions = new JPanel();
        registerOptions.setSize(10, 10);
        registerOptions.setLayout(new GridLayout(5, 2, 10, 20));

        this.add(registerOptions, BorderLayout.CENTER);

        addBackButton();
        addLayout(registerOptions);
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

    private void addLayout(JPanel registerOptions) {
        JLabel layoutLabel1 = new JLabel();
        JLabel layoutLabel2 = new JLabel();
        registerOptions.add(layoutLabel1);
        registerOptions.add(layoutLabel2);
    }

    private void addUsername(JPanel registerOptions) {
        // this adds a label and a text field
        JLabel usernameLabel = new JLabel("Username:", SwingConstants.CENTER);
        JTextField usernameField = new JTextField();

        registerOptions.add(usernameLabel);
        registerOptions.add(usernameField);
    }

    private void addPassword(JPanel registerOptions) {
        // this adds a label and a text field
        JLabel passwordLabel = new JLabel("Password:", SwingConstants.CENTER);
        JPasswordField passwordField = new JPasswordField();

        registerOptions.add(passwordLabel);
        registerOptions.add(passwordField);
    }

    private void checkPassword(JPanel registerOptions) {
        // this adds a label and a text field
        JLabel checkPasswordLabel = new JLabel("Verify Password:", SwingConstants.CENTER);
        JPasswordField checkPasswordField = new JPasswordField();

        registerOptions.add(checkPasswordLabel);
        registerOptions.add(checkPasswordField);
    }

    private void addSubmitButton() {
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            // this.game.handleSubmit();
        });

        submitButton.setPreferredSize(new Dimension(200, 50));
        this.add(submitButton, BorderLayout.PAGE_END);

    }
}

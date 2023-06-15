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

public class LoginView extends View {
    private Game game;

    public LoginView(Game game) {
        this.game = game;
        this.setLayout(new BorderLayout());
        JPanel loginOptions = new JPanel();
        loginOptions.setSize(10, 10);
        loginOptions.setLayout(new GridLayout(4, 2, 10, 20));

        this.add(loginOptions, BorderLayout.CENTER);

        addBackButton();
        addLayout(loginOptions);
        addUsername(loginOptions);
        addPassword(loginOptions);
        addStartButton();
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
        JLabel layoutLabel1 = new JLabel();
        JLabel layoutLabel2 = new JLabel();
        loginOptions.add(layoutLabel1);
        loginOptions.add(layoutLabel2);
    }

    private void addUsername(JPanel loginOptions) {
        // this adds a label and a text field
        JLabel usernameLabel = new JLabel("Username:", SwingConstants.CENTER);
        JTextField usernameField = new JTextField();

        loginOptions.add(usernameLabel);
        loginOptions.add(usernameField);
    }

    private void addPassword(JPanel loginOptions) {
        // this adds a label and a text field
        JLabel passwordLabel = new JLabel("Password:", SwingConstants.CENTER);
        JPasswordField passwordField = new JPasswordField();

        loginOptions.add(passwordLabel);
        loginOptions.add(passwordField);
    }

    private void addStartButton() {
        JButton startButton = new JButton("Start game!");
        startButton.addActionListener(e -> {
            // this.game.handleStart();
        });

        startButton.setPreferredSize(new Dimension(200, 50));
        this.add(startButton, BorderLayout.PAGE_END);

    }
}

package nl.rug.oop.rts.view.game;

import nl.rug.oop.rts.Game;

import javax.swing.*;
import java.awt.*;


public class MultiplayerView extends JPanel {

    private Game game;

    public MultiplayerView(Game game) {
        this.game = game;
        this.setLayout(new BorderLayout());
        JPanel userOptions = new JPanel();
        userOptions.setSize(10, 10);
        userOptions.setLayout(new GridLayout(4, 2, 10, 20));

        /**addTitle();*/

        this.add(userOptions, BorderLayout.CENTER);
        addLayout(userOptions);
        addLoginButton(userOptions);
        addRegisterButton(userOptions);

        addBackButton();
    }

    /**private void addTitle() {
        JLabel titleLabel = new JLabel ("User options", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(64f));
        this.add(titleLabel, BorderLayout.PAGE_START);
    }*/

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

    private void addLoginButton (JPanel userOptions) {
        JLabel loginLabel = new JLabel("Already have an account?", SwingConstants.CENTER);
        JButton loginButton = new JButton("Log In");
        loginButton.setPreferredSize(new Dimension(50, 26));
        loginButton.addActionListener(e -> {
            this.game.handleLogin();
        });
        userOptions.add(loginLabel);
        userOptions.add(loginButton);
    }

    private void addRegisterButton(JPanel userOptions) {
        JLabel registerLabel = new JLabel("New to the game?", SwingConstants.CENTER);
        JButton registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(50, 26));
        registerButton.addActionListener(e -> {
            this.game.handleRegister();
        });
        userOptions.add(registerLabel);
        userOptions.add(registerButton);
    }

}
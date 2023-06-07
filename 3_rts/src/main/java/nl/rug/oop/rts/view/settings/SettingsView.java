package nl.rug.oop.rts.view.settings;

import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nl.rug.oop.rts.Game;

public class SettingsView extends JPanel {

    private Game game;

    public SettingsView(Game game) {
        this.game = game;

        this.setLayout(null);

        JPanel actualOptions = new JPanel();
        actualOptions.setLayout(new GridLayout(3, 2, 0, 20));

        Insets insets = this.getInsets();

        this.add(actualOptions);
        actualOptions.setBounds(insets.left + 20, insets.top + 40, 760, 480);

        addUsernameOption(actualOptions);
        addBackButton();
    }

    private void addBackButton() {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            this.game.handleBack();
        });

        this.add(backButton);
        backButton.setBounds(680, 20, 100, 40);
    }

    private void addUsernameOption(JPanel optionsPanel) {
        // this adds a label and a text field
        JLabel usernameLabel = new JLabel("Username: ");
        JTextField usernameField = new JTextField();

        optionsPanel.add(usernameLabel);
        optionsPanel.add(usernameField);
    }

}

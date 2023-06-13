package nl.rug.oop.rts.view.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import nl.rug.oop.rts.Game;

public class SettingsView extends JPanel {

    private Game game;

    public SettingsView(Game game) {
        this.game = game;
        this.setLayout(new BorderLayout());
        JPanel actualOptions = new JPanel();
        actualOptions.setLayout(new GridLayout(4, 2, 10, 10));

        addBackButton();
        addUsernameOption(actualOptions);
        addThemeDropDown(actualOptions);
        addCentralServer(actualOptions);
        addSaveButton();

        this.add(actualOptions);
    }

    private void addBackButton() {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            this.game.handleBack();
        });

        backButton.setPreferredSize(new Dimension(200, 50));
        this.add(backButton, BorderLayout.PAGE_START);

    } 

    private void addSaveButton() {
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // this.game.handleSave();
        });

        saveButton.setPreferredSize(new Dimension(200, 50));
        this.add(saveButton, BorderLayout.PAGE_END);

    }

    private void addUsernameOption(JPanel actualOptions) {
        // this adds a label and a text field
        JLabel usernameLabel = new JLabel("Username:", SwingConstants.CENTER);
        JTextField usernameField = new JTextField("Username");

        actualOptions.add(usernameLabel);
        actualOptions.add(usernameField);
    }

    private void addThemeDropDown(JPanel actualOptions) {
        String[] themes = { "Theme 1", "Theme 2", "Theme 3" };

        JLabel themeLabel = new JLabel("Theme:", SwingConstants.CENTER);
        JComboBox<String> themeDropDown = new JComboBox<>(themes);

        actualOptions.add(themeLabel);
        actualOptions.add(themeDropDown);
    }

    private void addCentralServer(JPanel actualOptions) {
        // this adds a label and a text field
        JLabel serverLabel = new JLabel("Central Server:", SwingConstants.CENTER);
        JTextField serverField = new JTextField("masecla.dev");

        actualOptions.add(serverLabel);
        actualOptions.add(serverField);
    }

}

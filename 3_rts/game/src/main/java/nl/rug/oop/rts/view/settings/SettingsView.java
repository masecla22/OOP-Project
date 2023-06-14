package nl.rug.oop.rts.view.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
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
        actualOptions.setLayout(new GridLayout(4, 2, 10, 20));

        addBackButton();
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

    private void addCentralServer(JPanel actualOptions) {
        // this adds a label and a text field
        JLabel serverLabel = new JLabel("Central Server:", SwingConstants.CENTER);
        JTextField serverField = new JTextField("masecla.dev");

        actualOptions.add(serverLabel);
        actualOptions.add(serverField);
    }

}

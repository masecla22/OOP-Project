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
import nl.rug.oop.rts.controller.settings.SettingsController;
import nl.rug.oop.rts.view.View;

/**
 * This class is responsible for presenting the settings menu to the user.
 */
public class SettingsView extends View {
    private Game game;

    private SettingsController controller;

    private JTextField centralServer;

    /**
     * Constructor for the settings view.
     * 
     * @param game       - the game
     * @param controller - the settings controller
     */
    public SettingsView(Game game, SettingsController controller) {
        this.game = game;
        this.controller = controller;

        this.setLayout(new BorderLayout());
        JPanel actualOptions = new JPanel();
        actualOptions.setLayout(new GridLayout(3, 2, 10, 20));

        addBackButton();
        addLayout(actualOptions);
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
            this.controller.setCentralServer(centralServer.getText());
            this.controller.save();
        });

        saveButton.setPreferredSize(new Dimension(200, 50));
        this.add(saveButton, BorderLayout.PAGE_END);
    }

    private void addCentralServer(JPanel actualOptions) {
        JLabel serverLabel = new JLabel("Central Server:", SwingConstants.CENTER);
        centralServer = new JTextField(this.controller.getSettings().getCentralServer());

        actualOptions.add(serverLabel);
        actualOptions.add(centralServer);
    }

    private void addLayout(JPanel actualOptions) {
        JLabel layoutLabel1 = new JLabel();
        JLabel layoutLabel2 = new JLabel();
        actualOptions.add(layoutLabel1);
        actualOptions.add(layoutLabel2);
    }

}

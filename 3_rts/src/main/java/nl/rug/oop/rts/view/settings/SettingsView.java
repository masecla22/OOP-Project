package nl.rug.oop.rts.view.settings;



import java.awt.*;

import javax.swing.*;

import nl.rug.oop.rts.Game;

public class SettingsView extends JPanel {

    private Game game;

    public SettingsView(Game game) {
        this.game = game;
        this.setLayout(new BorderLayout());
        JPanel actualOptions = new JPanel();
        actualOptions.setLayout(new GridBagLayout());

        Insets insets = this.getInsets();

        this.add(actualOptions);

        addBackButton();
        addUsernameOption(actualOptions);
        addThemeDropDown(actualOptions);
        addCentralServer(actualOptions);
        addSaveButton();
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
            //this.game.handleSave();
        });

        saveButton.setPreferredSize(new Dimension(200, 50));
        this.add(saveButton, BorderLayout.PAGE_END);

    }

    private void addUsernameOption(JPanel actualOptions) {
        // this adds a label and a text field
        JLabel usernameLabel = new JLabel("Username:", SwingConstants.CENTER);
        JTextField usernameField = new JTextField();

        usernameLabel.setPreferredSize(new Dimension(50, 50));
        usernameField.setPreferredSize(new Dimension(50, 50));

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;

        actualOptions.add(usernameLabel, c);
        c.gridx = 2;
        actualOptions.add(usernameField, c);

    }

    private void addThemeDropDown (JPanel actualOptions) {

        JButton themeButton = new JButton("Theme");

        //this adds a drop-down menu which shows the options for changing the theme
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem menuItemCreateTheme1 = new JMenuItem("Theme 1");
        popupMenu.add(menuItemCreateTheme1);

        JMenuItem menuItemCreateTheme2 = new JMenuItem("Theme 2");
        popupMenu.add(menuItemCreateTheme2);

        JMenuItem menuItemCreateTheme3 = new JMenuItem("Theme 3");
        popupMenu.add(menuItemCreateTheme3);

        themeButton.setPreferredSize(new Dimension(500, 50));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 1;
        c.gridy = 2;

        actualOptions.add(themeButton, c);

    }

    private void addCentralServer (JPanel actualOptions) {
        // this adds a label and a text field
        JLabel serverLabel = new JLabel("Central Server:", SwingConstants.CENTER);
        JTextField serverField = new JTextField("masecla.dev");

        serverLabel.setPreferredSize(new Dimension(50, 50));
        serverField.setPreferredSize(new Dimension(50, 50));

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;

        actualOptions.add(serverLabel);

        c.gridx = 2;
        actualOptions.add(serverField);


    }


}

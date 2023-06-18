package nl.rug.oop.rts.controller.settings;

import java.io.File;

import nl.rug.oop.rts.protocol.objects.model.settings.GameSettings;
import nl.rug.oop.rugson.Rugson;

/**
 * This class is responsible for managing the settings of the game.
 */
public class SettingsController {
    private GameSettings settings;

    public SettingsController() {
        this.settings = GameSettings.loadConfiguration(new Rugson(), new File("game.json"));
    }

    public GameSettings getSettings() {
        return settings;
    }

    public void save() {
        settings.save();
    }

    public void setCentralServer(String centralServer) {
        settings.setCentralServer(centralServer);
    }

    public void setUsername(String username) {
        settings.setUsername(username);
    }

    public void setPassword(String password) {
        settings.setPassword(password);
    }
}

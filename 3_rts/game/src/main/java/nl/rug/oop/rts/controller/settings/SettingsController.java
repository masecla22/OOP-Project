package nl.rug.oop.rts.controller.settings;

import java.io.File;

import nl.rug.oop.rts.protocol.objects.model.settings.GameSettings;
import nl.rug.oop.rugson.Rugson;

public class SettingsController {
    private GameSettings settings;

    public SettingsController(Rugson rugson) {
        this.settings = GameSettings.loadConfiguration(rugson, new File("game.json"));
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
}

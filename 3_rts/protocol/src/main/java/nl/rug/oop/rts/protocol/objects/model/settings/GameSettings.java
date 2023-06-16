package nl.rug.oop.rts.protocol.objects.model.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.rug.oop.rugson.Rugson;

/**
 * This class represents the game settings. It is used to store different
 * aspects of the game.
 */
@Data
@NoArgsConstructor
public class GameSettings {
    private transient Rugson rugson;
    private transient File file;

    private String centralServer = "masecla.dev";

    private String username;
    private String password;

    /**
     * Loads the configuration from the given file. If the file does not exist, it
     * will be created
     * with default values.
     * 
     * @param rugson - Rugson instance
     * @param file   - File to load from
     * @return The loaded configuration
     */
    public static GameSettings loadConfiguration(Rugson rugson, File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();

                // Build a config with default values
                GameSettings config = new GameSettings();

                config.setRugson(rugson);
                config.setFile(file);

                config.save();
                return config;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try (FileInputStream stream = new FileInputStream(file)) {
            GameSettings config = rugson.fromJson(stream, GameSettings.class);

            config.setFile(file);
            config.setRugson(rugson);

            return config;
        } catch (IOException | NullPointerException | IllegalStateException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Saves the config to the file that it was built to originally. Throws to its
     * logger if something breaks down.
     */
    public void save() {
        try (FileOutputStream stream = new FileOutputStream(file)) {
            String json = this.rugson.toJson(this);
            stream.write(json.getBytes());
        } catch (IOException | NullPointerException | IllegalStateException e) {
            e.printStackTrace();
        }
    }
}

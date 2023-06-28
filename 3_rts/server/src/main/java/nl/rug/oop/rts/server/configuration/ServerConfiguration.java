package nl.rug.oop.rts.server.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.rug.oop.rts.server.configuration.sql.SQLConfiguration;
import nl.rug.oop.rugson.Rugson;

/**
 * This class represents the server configuration.
 */
@Data
@NoArgsConstructor
public class ServerConfiguration {
    private transient Rugson rugson;
    private transient Logger logger;
    private transient File file;

    private int port = 7779;
    private SQLConfiguration sql = new SQLConfiguration();

    /**
     * Loads the configuration from the given file. If the file does not exist, it
     * will be created with default values. Throws to the logger if something goes
     * wrong.
     * 
     * @param rugson - The Rugson instance to use for loading the config
     * @param file   - The file to load the config from
     * @param logger - The logger to throw to if something goes wrong
     * @return - The loaded configuration
     */
    public static ServerConfiguration loadConfiguration(Rugson rugson, File file, Logger logger) {
        try {
            if (!file.exists()) {
                file.createNewFile();

                // Build a config with default values
                ServerConfiguration config = new ServerConfiguration();

                config.setRugson(rugson);
                config.setLogger(logger);
                config.setFile(file);

                config.save();
                return config;
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.toString(), e);
            return null;
        }

        try (FileInputStream stream = new FileInputStream(file)) {
            ServerConfiguration config = rugson.fromJson(stream, ServerConfiguration.class);

            config.setLogger(logger);
            config.setFile(file);
            config.setRugson(rugson);

            return config;
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.toString(), e);
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
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }
}

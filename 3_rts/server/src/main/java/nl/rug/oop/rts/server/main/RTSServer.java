package nl.rug.oop.rts.server.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import lombok.SneakyThrows;
import nl.rug.oop.rts.protocol.packet.dictionary.RTSPacketDictionary;
import nl.rug.oop.rts.server.configuration.ServerConfiguration;
import nl.rug.oop.rts.server.connection.ConnectionManager;
import nl.rug.oop.rts.server.logging.CustomFormatter;
import nl.rug.oop.rts.server.logging.FileLogHandler;
import nl.rug.oop.rts.server.logging.LoggingOutputStream;
import nl.rug.oop.rts.server.user.UserManager;
import nl.rug.oop.rugson.Rugson;
import nl.rug.oop.rugson.RugsonBuilder;

public class RTSServer {
    private ExecutorService threadPool;
    private Logger logger;

    private ConnectionManager connectionManager;

    private ServerConfiguration configuration;
    private Rugson rugson;

    private Connection connection;
    private UserManager userManager;

    public RTSServer() {
        this.threadPool = Executors.newCachedThreadPool();
        this.logger = Logger.getLogger("RTS-Server");
    }

    public void start() throws IOException {
        this.setupLogging();

        logger.info("--- Starting RTS-Server ---");
        logger.info("             - Kindly brought to you by Team 48 <3");
        logger.info("");

        this.setupConfiguration();
        this.setupRugson();

        this.setupSQLConnection();
        this.setupUserManager();

        this.setupConnectionManager();
    }

    private void setupConfiguration() {
        this.rugson = new RugsonBuilder().setPrettyPrint(false).build();

        // We want the configuration Rugson to print pretty so the config can be edited
        // by hand
        Rugson configurationRugson = new RugsonBuilder().setPrettyPrint(true).build();

        // Load the configuration
        this.configuration = ServerConfiguration.loadConfiguration(configurationRugson, new File("config.json"),
                logger);

        logger.info("Loaded and setup configuration for the server.");
    }

    private void setupRugson() {
        this.rugson = new RugsonBuilder().setPrettyPrint(false).build();
        System.out.println("Setup Rugson instance!");
    }

    private void setupConnectionManager() throws IOException {
        RTSPacketDictionary packetDictionary = new RTSPacketDictionary();

        this.connectionManager = new ConnectionManager(threadPool, rugson, packetDictionary);
        this.connectionManager.start(this.configuration.getPort());

        logger.info("Connection manager started on port: " + this.configuration.getPort() + ".");
    }

    @SneakyThrows
    private void setupSQLConnection() {
        this.connection = this.configuration.getSql().openConnection();
        logger.info("SQL connection established.");
    }

    private void setupUserManager() {
        this.userManager = new UserManager(this.connection);
        logger.info("User manager setup.");
    }

    private void setupLogging() {
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);

        ConsoleHandler consoleHandler = new ConsoleHandler() {
            @Override
            public void publish(LogRecord record) {
                if (record == null)
                    return;
                super.publish(record);
            }
        };

        consoleHandler.setFormatter(new CustomFormatter());
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);

        FileLogHandler fileHandler = new FileLogHandler();
        fileHandler.setFormatter(new CustomFormatter());
        fileHandler.setLevel(Level.ALL);
        logger.addHandler(fileHandler);

        // Redirect stderr and stdout to logger
        System.setOut(new PrintStream(new LoggingOutputStream(logger, Level.INFO)));
        System.setErr(new PrintStream(new LoggingOutputStream(logger, Level.SEVERE)));
    }

}

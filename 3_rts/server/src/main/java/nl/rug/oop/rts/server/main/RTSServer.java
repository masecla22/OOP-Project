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

import lombok.Getter;
import lombok.SneakyThrows;
import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.adapters.EventTypeAdapter;
import nl.rug.oop.rts.protocol.adapters.GameMapTypeAdapter;
import nl.rug.oop.rts.protocol.adapters.UnitTypeAdapter;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.objects.model.Map;
import nl.rug.oop.rts.protocol.objects.model.events.Event;
import nl.rug.oop.rts.protocol.objects.model.events.EventFactory;
import nl.rug.oop.rts.protocol.objects.model.factories.UnitFactory;
import nl.rug.oop.rts.protocol.objects.model.factories.singleplayer.MultiPlayerUnitFactory;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.packet.Packet;
import nl.rug.oop.rts.protocol.packet.dictionary.RTSPacketDictionary;
import nl.rug.oop.rts.server.configuration.ServerConfiguration;
import nl.rug.oop.rts.server.connection.ConnectionManager;
import nl.rug.oop.rts.server.games.GamesManager;
import nl.rug.oop.rts.server.handlers.HandlerBinder;
import nl.rug.oop.rts.server.logging.CustomFormatter;
import nl.rug.oop.rts.server.logging.FileLogHandler;
import nl.rug.oop.rts.server.logging.LoggingOutputStream;
import nl.rug.oop.rts.server.user.UserManager;
import nl.rug.oop.rugson.Rugson;
import nl.rug.oop.rugson.RugsonBuilder;

@Getter
public class RTSServer {
    private ExecutorService threadPool;
    private Logger logger;

    private ConnectionManager connectionManager;

    private ServerConfiguration configuration;
    private Rugson rugson;

    private Connection connection;
    private UserManager userManager;

    private GamesManager gamesManager;

    private UnitFactory unitFactory;
    private EventFactory eventFactory;

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
        this.setupFactories();

        this.setupRugson();

        this.setupSQLConnection();
        this.setupUserManager();

        this.setupGameManager();

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

    private void setupFactories() {
        this.unitFactory = new MultiPlayerUnitFactory();
        this.eventFactory = new EventFactory(this.unitFactory);

        logger.info("Setup factories!");
    }

    private void setupRugson() {
        this.rugson = new RugsonBuilder()
                .setPrettyPrint(true)
                .addTypeAdapter(Unit.class, new UnitTypeAdapter())
                .addTypeAdapter(Event.class, new EventTypeAdapter(eventFactory))
                .addTypeAdapter(Map.class, new GameMapTypeAdapter())
                .build();
        logger.info("Setup Rugson instance!");
    }

    private void setupGameManager() {
        this.gamesManager = new GamesManager();
        System.out.println("Setup GameManager instance!");
    }

    private void setupConnectionManager() throws IOException {
        RTSPacketDictionary packetDictionary = new RTSPacketDictionary();

        this.connectionManager = new ConnectionManager(threadPool, rugson, packetDictionary);
        this.connectionManager.start(this.configuration.getPort());

        this.connectionManager.addConnectionHandler((connection) -> {
            // Register all listeners on the connection
            System.out.println("New connection! " + connection.getSocket().getInetAddress().getHostAddress() + ":"
                    + connection.getSocket().getPort());
            connection.addListener(new PacketListener<>(Packet.class) {
                @Override
                protected boolean handlePacket(SocketConnection connection, Packet packet) {
                    System.out.println("Received packet: " + packet);
                    return true;
                }
            });

            HandlerBinder binder = new HandlerBinder(this, connection);
            binder.bind();
        });

        logger.info("Connection manager started on port: " + this.configuration.getPort() + ".");
    }

    @SneakyThrows
    private void setupSQLConnection() {
        this.connection = this.configuration.getSql().openConnection();
        logger.info("SQL connection established.");
    }

    @SneakyThrows
    private void setupUserManager() {
        this.userManager = new UserManager(this.connection);
        this.userManager.initialize();
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

package nl.rug.oop.rts.server.main;

import java.io.IOException;

/**
 * This class initializes, then, starts the server.
 */
public class ServerStarter {
    /**
     * Start the server.
     * 
     * @param args - The arguments
     * @throws IOException - If something goes wrong while opening the server,
     *                     this exception is thrown
     */
    public static void main(String[] args) throws IOException {
        RTSServer server = new RTSServer();
        server.start();
    }
}

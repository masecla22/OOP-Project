package nl.rug.oop.rts.server.main;

import java.io.IOException;

public class ServerStarter {
    public static void main(String[] args) throws IOException {
        RTSServer server = new RTSServer();
        server.start();
    }
}

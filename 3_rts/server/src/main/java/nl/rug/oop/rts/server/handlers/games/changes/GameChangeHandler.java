package nl.rug.oop.rts.server.handlers.games.changes;

import java.io.IOException;

import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.objects.model.armies.Team;
import nl.rug.oop.rts.protocol.objects.model.factories.UnitFactory;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.MultiplayerGame;
import nl.rug.oop.rts.protocol.packet.definitions.game.changes.GameChangeListConfirm;
import nl.rug.oop.rts.protocol.packet.definitions.game.changes.GameChangeListPacket;
import nl.rug.oop.rts.protocol.user.User;
import nl.rug.oop.rts.server.games.GamesManager;
import nl.rug.oop.rts.server.games.ServerGameSimulator;
import nl.rug.oop.rts.server.user.UserManager;

public class GameChangeHandler extends PacketListener<GameChangeListPacket> {

    private UnitFactory unitFactory;
    private UserManager userManager;
    private GamesManager gamesManager;

    public GameChangeHandler(UserManager userManager, GamesManager gamesManager) {
        super(GameChangeListPacket.class);

        this.userManager = userManager;
        this.gamesManager = gamesManager;
    }

    @Override
    protected boolean handlePacket(SocketConnection connection, GameChangeListPacket packet) throws Exception {
        // Get the user from the connection
        User user = userManager.getUser(packet.getSessionToken());
        if (user == null) {
            sendFailurePacket(connection);
            return false;
        }

        // Get the game out of the packet
        MultiplayerGame game = gamesManager.getGame(packet.getGameId());
        if (game == null) {
            sendFailurePacket(connection);
            return false;
        }

        // Setup a disposable ServerGameSimulator
        ServerGameSimulator simulator = new ServerGameSimulator(unitFactory, game);
        Team team = game.getTeam(user);
        if (team == null) {
            sendFailurePacket(connection);
            return false;
        }

        if (!simulator.canDoChanges(packet.getChanges(), team)) {
            sendFailurePacket(connection);
            return false;
        }

        simulator.applyChanges(packet.getChanges(), team);

        simulator.simulateStep();

        return true;
    }

    private void sendFailurePacket(SocketConnection socketConnection) throws IOException {
        GameChangeListConfirm confirm = new GameChangeListConfirm(false);
        socketConnection.sendPacket(confirm);
    }

}

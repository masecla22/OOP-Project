package nl.rug.oop.rts.server.handlers.games.changes;

import java.io.IOException;

import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.objects.model.armies.Team;
import nl.rug.oop.rts.protocol.objects.model.factories.UnitFactory;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.GamePlayer;
import nl.rug.oop.rts.protocol.objects.model.multiplayer.MultiplayerGame;
import nl.rug.oop.rts.protocol.packet.definitions.game.GameUpdatePacket;
import nl.rug.oop.rts.protocol.packet.definitions.game.changes.GameChangeListConfirm;
import nl.rug.oop.rts.protocol.packet.definitions.game.changes.GameChangeListPacket;
import nl.rug.oop.rts.protocol.user.User;
import nl.rug.oop.rts.server.games.GamesManager;
import nl.rug.oop.rts.server.games.ServerGameSimulator;
import nl.rug.oop.rts.server.user.UserManager;

/**
 * This handler is responsible for handling game change packets.
 * It will also run the simulation of the game and will send out updates.
 */
public class GameChangeHandler extends PacketListener<GameChangeListPacket> {

    private UserManager userManager;
    private GamesManager gamesManager;
    private UnitFactory unitFactory;

    /**
     * Create a new GameChangeHandler.
     * 
     * @param userManager  - The user manager
     * @param gamesManager - The games manager
     * @param unitFactory  - The unit factory
     */
    public GameChangeHandler(UserManager userManager, GamesManager gamesManager, UnitFactory unitFactory) {
        super(GameChangeListPacket.class);

        this.userManager = userManager;
        this.gamesManager = gamesManager;
        this.unitFactory = unitFactory;
    }

    @Override
    protected boolean handlePacket(SocketConnection connection, GameChangeListPacket packet) throws IOException {
        User user = userManager.getUser(packet.getSessionToken());
        if (user == null) {
            sendFailurePacket(connection);
            return false;
        }
        MultiplayerGame game = gamesManager.getGame(packet.getGameId());
        if (game == null) {
            sendFailurePacket(connection);
            return false;
        }

        Team team = game.isPlayerATurn() ? game.getPlayerA().getTeam() : game.getPlayerB().getTeam();
        GamePlayer player = game.isPlayerATurn() ? game.getPlayerA() : game.getPlayerB();
        if (!player.getUser().equals(user)) {
            sendFailurePacket(connection);
            return false;
        }

        // Setup a disposable ServerGameSimulator
        ServerGameSimulator simulator = new ServerGameSimulator(unitFactory, game);
        if (!simulator.canDoChanges(packet.getChanges(), team)) {
            sendFailurePacket(connection);
            return false;
        }

        simulator.applyChanges(packet.getChanges(), team);
        simulator.simulateStep();

        GameChangeListConfirm confirm = new GameChangeListConfirm(true);
        connection.sendPacket(confirm);
        sendGameUpdates(game);

        return true;
    }

    private void sendFailurePacket(SocketConnection socketConnection) throws IOException {
        GameChangeListConfirm confirm = new GameChangeListConfirm(false);
        socketConnection.sendPacket(confirm);
    }

    private void sendGameUpdates(MultiplayerGame game) {
        GameUpdatePacket packet = new GameUpdatePacket(game);

        try {
            game.getPlayerA().getConnection().sendPacket(packet);
            game.getPlayerB().getConnection().sendPacket(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Team possibleWinner = game.checkWinner();
        if (possibleWinner != null) {
            GamePlayer winner = possibleWinner == Team.TEAM_A ? game.getPlayerA() : game.getPlayerB();
            this.gamesManager.handleFinishedGame(game, winner, false);
        }
    }

}

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

public class GameChangeHandler extends PacketListener<GameChangeListPacket> {

    private UserManager userManager;
    private GamesManager gamesManager;
    private UnitFactory unitFactory;

    public GameChangeHandler(UserManager userManager, GamesManager gamesManager, UnitFactory unitFactory) {
        super(GameChangeListPacket.class);

        this.userManager = userManager;
        this.gamesManager = gamesManager;
        this.unitFactory = unitFactory;
    }

    @Override
    protected boolean handlePacket(SocketConnection connection, GameChangeListPacket packet) throws IOException {
        try {
            // Get the user from the connection
            User user = userManager.getUser(packet.getSessionToken());
            if (user == null) {
                System.out.println("No user found");
                sendFailurePacket(connection);
                return false;
            }

            // Get the game out of the packet
            MultiplayerGame game = gamesManager.getGame(packet.getGameId());
            if (game == null) {
                System.out.println("No game found");
                sendFailurePacket(connection);
                return false;
            }

            // Setup a disposable ServerGameSimulator
            ServerGameSimulator simulator = new ServerGameSimulator(unitFactory, game);
            Team team = game.isPlayerATurn() ? game.getPlayerA().getTeam() : game.getPlayerB().getTeam();

            // Make sure the packet is from the correct team
            GamePlayer player = game.isPlayerATurn() ? game.getPlayerA() : game.getPlayerB();
            if (!player.getUser().equals(user)) {
                System.out.println("Wrong user");
                sendFailurePacket(connection);
                return false;
            }

            if (!simulator.canDoChanges(packet.getChanges(), team)) {
                System.out.println("Can't do changes");
                sendFailurePacket(connection);
                return false;
            }

            simulator.applyChanges(packet.getChanges(), team);
            simulator.simulateStep();

            GameChangeListConfirm confirm = new GameChangeListConfirm(true);
            connection.sendPacket(confirm);

            sendGameUpdates(game);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void sendFailurePacket(SocketConnection socketConnection) throws IOException {
        GameChangeListConfirm confirm = new GameChangeListConfirm(false);
        socketConnection.sendPacket(confirm);
    }

    private void sendGameUpdates(MultiplayerGame game) {
        System.out.println("Sending game updates");
        GameUpdatePacket packet = new GameUpdatePacket(game);
        
        try {
            System.out.println("Sending game updates");
            game.getPlayerA().getConnection().sendPacket(packet);
            System.out.println("Sending game updates");
            game.getPlayerB().getConnection().sendPacket(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Team possibleWinner = game.checkWinner();
        if (possibleWinner != null) {
            this.gamesManager.handleFinishedGame(game,
                    possibleWinner == Team.TEAM_A ? game.getPlayerA() : game.getPlayerB());
        }
    }

}

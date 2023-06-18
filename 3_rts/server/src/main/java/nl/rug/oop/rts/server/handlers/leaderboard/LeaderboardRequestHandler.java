package nl.rug.oop.rts.server.handlers.leaderboard;

import java.util.List;
import java.util.Map.Entry;

import nl.rug.oop.rts.protocol.SocketConnection;
import nl.rug.oop.rts.protocol.listeners.PacketListener;
import nl.rug.oop.rts.protocol.packet.definitions.game.leaderboard.LeaderboardRequest;
import nl.rug.oop.rts.protocol.packet.definitions.game.leaderboard.LeaderboardResponse;
import nl.rug.oop.rts.server.user.UserManager;

public class LeaderboardRequestHandler extends PacketListener<LeaderboardRequest> {
    private UserManager userManager;

    public LeaderboardRequestHandler(UserManager userManager) {
        super(LeaderboardRequest.class);

        this.userManager = userManager;
    }

    @Override
    protected boolean handlePacket(SocketConnection connection, LeaderboardRequest packet) throws Exception {
        List<Entry<String, Integer>> leaderboard = userManager.getLeaderboard();

        LeaderboardResponse response = new LeaderboardResponse(leaderboard);
        connection.sendPacket(response);

        return true;
    }

}

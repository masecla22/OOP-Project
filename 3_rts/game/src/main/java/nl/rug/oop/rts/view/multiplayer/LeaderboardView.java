package nl.rug.oop.rts.view.multiplayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import nl.rug.oop.rts.Game;
import nl.rug.oop.rts.controller.multiplayer.MultiplayerConnectionController;
import nl.rug.oop.rts.protocol.listeners.AwaitPacketOnce;
import nl.rug.oop.rts.protocol.objects.interfaces.observing.Observer;
import nl.rug.oop.rts.protocol.packet.Packet;
import nl.rug.oop.rts.protocol.packet.definitions.game.leaderboard.LeaderboardRequest;
import nl.rug.oop.rts.protocol.packet.definitions.game.leaderboard.LeaderboardResponse;
import nl.rug.oop.rts.view.View;

public class LeaderboardView extends View implements Observer {
    private Game game;
    private MultiplayerConnectionController connectionController;

    private JPanel userPanel;

    public LeaderboardView(Game game, MultiplayerConnectionController connectionController) {
        this.game = game;
        this.connectionController = connectionController;

        addLoading();
        requestLeaderboard();
    }

    private void requestLeaderboard() {
        AwaitPacketOnce<Packet> awaitPacketOnce = new AwaitPacketOnce<>(LeaderboardResponse.class)
                .bindTo(connectionController.getConnection());

        connectionController.sendAuthenticatedPacket(new LeaderboardRequest());

        awaitPacketOnce.getAwaiting().whenComplete((c, err) -> {
            if (err != null) {
                err.printStackTrace();
                addSomethingWrong();
            }

            LeaderboardResponse response = (LeaderboardResponse) c.getValue();
            parseLeaderboard(response);
        });
    }

    private void addLoading() {
        JLabel loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
        this.add(loadingLabel, BorderLayout.CENTER);
        addBackButton();
        this.update();
    }

    private void addSomethingWrong() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::addSomethingWrong);
            return;
        }

        this.removeAll();

        this.setLayout(new BorderLayout());

        JLabel somethingWrongLabel = new JLabel("Something went wrong, please try again", SwingConstants.CENTER);

        this.add(somethingWrongLabel, BorderLayout.CENTER);
        addBackButton();
        this.update();
    }

    private void addBackButton() {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            this.game.handleBack();
        });

        backButton.setPreferredSize(new Dimension(200, 50));
        this.add(backButton, BorderLayout.PAGE_END);
    }

    private void parseLeaderboard(LeaderboardResponse response) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> parseLeaderboard(response));
            return;
        }

        if (this.userPanel == null) {
            this.userPanel = new JPanel();

            this.removeAll();
            this.setLayout(new BorderLayout());

            this.add(this.userPanel, BorderLayout.CENTER);
            this.addBackButton();
        }

        int size = response.getUsernames().size();

        this.userPanel.removeAll();
        this.userPanel.setLayout(new GridLayout(size, 3));

        for (int i = 0; i < size; i++) {
            JLabel rankLabel = new JLabel(Integer.toString(i + 1), SwingConstants.CENTER);
            JLabel usernameLabel = new JLabel(response.getUsernames().get(i), SwingConstants.CENTER);
            JLabel scoreLabel = new JLabel(Integer.toString(response.getScores().get(i)), SwingConstants.CENTER);

            this.userPanel.add(rankLabel);
            this.userPanel.add(usernameLabel);
            this.userPanel.add(scoreLabel);
        }

        this.update();
    }
}

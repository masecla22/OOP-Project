package nl.rug.oop.rts.protocol.packet.dictionary;

import nl.rug.oop.rts.protocol.packet.definitions.authentication.login.LoginRequest;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.login.LoginResponse;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.register.RegistrationRequest;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.register.RegistrationResponse;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.tokens.TokenRefreshingRequest;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.tokens.TokenRefreshingResponse;
import nl.rug.oop.rts.protocol.packet.definitions.game.GameEndPacket;
import nl.rug.oop.rts.protocol.packet.definitions.game.GameStartPacket;
import nl.rug.oop.rts.protocol.packet.definitions.game.GameUpdatePacket;
import nl.rug.oop.rts.protocol.packet.definitions.game.changes.GameChangeListConfirm;
import nl.rug.oop.rts.protocol.packet.definitions.game.changes.GameChangeListPacket;
import nl.rug.oop.rts.protocol.packet.definitions.game.leaderboard.LeaderboardRequest;
import nl.rug.oop.rts.protocol.packet.definitions.game.leaderboard.LeaderboardResponse;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyCreationRequest;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyCreationResponse;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyDeletionRequest;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyJoiningRequest;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyListingRequest;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyListingResponse;

/**
 * Dictionary for the RTS protocol.
 */
public class RTSPacketDictionary extends PacketDictionary {

    /**
     * Create a new dictionary and registers all packets.
     */
    public RTSPacketDictionary() {
        // Register packets here
        this.registerPacket(LoginRequest.class);
        this.registerPacket(LoginResponse.class);
        this.registerPacket(RegistrationRequest.class);
        this.registerPacket(RegistrationResponse.class);
        this.registerPacket(TokenRefreshingRequest.class);
        this.registerPacket(TokenRefreshingResponse.class);

        // Game lobby packets here
        this.registerPacket(LobbyCreationRequest.class);
        this.registerPacket(LobbyCreationResponse.class);
        this.registerPacket(LobbyDeletionRequest.class);
        this.registerPacket(LobbyListingRequest.class);
        this.registerPacket(LobbyListingResponse.class);
        this.registerPacket(LobbyJoiningRequest.class);

        // Game packets here
        this.registerPacket(GameStartPacket.class);
        this.registerPacket(GameChangeListPacket.class);
        this.registerPacket(GameChangeListConfirm.class);
        this.registerPacket(GameEndPacket.class);
        this.registerPacket(GameUpdatePacket.class);

        // Leaderboard packets here
        this.registerPacket(LeaderboardRequest.class);
        this.registerPacket(LeaderboardResponse.class);
    }

}

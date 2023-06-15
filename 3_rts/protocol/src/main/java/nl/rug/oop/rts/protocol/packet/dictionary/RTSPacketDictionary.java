package nl.rug.oop.rts.protocol.packet.dictionary;

import nl.rug.oop.rts.protocol.packet.definitions.authentication.login.LoginRequest;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.login.LoginResponse;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.register.RegistrationRequest;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.register.RegistrationResponse;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyCreationRequest;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyCreationResponse;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyDeletionRequest;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyListingRequest;
import nl.rug.oop.rts.protocol.packet.definitions.game.lobby.LobbyListingResponse;

public class RTSPacketDictionary extends PacketDictionary {
    public RTSPacketDictionary() {
        super();

        // Register packets here
        this.registerPacket(LoginRequest.class);
        this.registerPacket(LoginResponse.class);
        this.registerPacket(RegistrationRequest.class);
        this.registerPacket(RegistrationResponse.class);

        // Game lobby packets here
        this.registerPacket(LobbyCreationRequest.class);
        this.registerPacket(LobbyCreationResponse.class);
        this.registerPacket(LobbyDeletionRequest.class);
        this.registerPacket(LobbyListingRequest.class);
        this.registerPacket(LobbyListingResponse.class);
    }

}

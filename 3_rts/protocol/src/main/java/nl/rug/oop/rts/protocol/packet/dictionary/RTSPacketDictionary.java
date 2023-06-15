package nl.rug.oop.rts.protocol.packet.dictionary;

import nl.rug.oop.rts.protocol.packet.definitions.authentication.login.LoginRequest;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.login.LoginResponse;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.register.RegistrationRequest;
import nl.rug.oop.rts.protocol.packet.definitions.authentication.register.RegistrationResponse;

public class RTSPacketDictionary extends PacketDictionary {
    public RTSPacketDictionary() {
        super();

        // Register packets here
        this.registerPacket(LoginRequest.class);
        this.registerPacket(LoginResponse.class);
        this.registerPacket(RegistrationRequest.class);
        this.registerPacket(RegistrationResponse.class);
    }
}

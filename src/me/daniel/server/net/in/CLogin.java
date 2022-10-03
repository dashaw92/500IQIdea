package me.daniel.server.net.in;

import me.daniel.server.wrapper.McInputStream;

import java.io.ByteArrayInputStream;

public final class CLogin {

    public final int protocolVersion;
    public final String ign;

    public CLogin(McInputStream is) {
        protocolVersion = is.readIntMc();
        ign = is.readString16();
    }

}

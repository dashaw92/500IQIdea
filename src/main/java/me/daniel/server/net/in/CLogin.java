package me.daniel.server.net.in;

import me.daniel.server.wrapper.McInputStream;

import java.io.ByteArrayInputStream;

public record CLogin(int protocolVersion, String ign) {

    public CLogin(McInputStream is) {
        this(is.readIntMc(), is.readString16());
    }

}

package me.daniel.server.net;

public class SPacketChat extends Packet {

    public SPacketChat(String msg) {
        super(0x03);
        data.add(msg);
    }

}
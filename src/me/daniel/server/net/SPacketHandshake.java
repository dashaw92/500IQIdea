package me.daniel.server.net;

public class SPacketHandshake extends Packet {

    public SPacketHandshake() {
        super(0x02);
        data.add("-");
    }

}
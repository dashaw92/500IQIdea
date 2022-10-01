package me.daniel.server.net;

public class SPacketKeepalive extends Packet {

    public SPacketKeepalive(int id) {
        super(0x00);
        data.add(id);
    }

}
package me.daniel.server.net;

public class SPacketPlayerLookAndPosition extends Packet {

    public SPacketPlayerLookAndPosition() {
        super(0x0D);
        data.add(10D);
        data.add(62D);
        data.add(60D);
        data.add(100D);
        data.add(0.5F);
        data.add(0.5F);
        data.add(false);
//		bytes = new byte[] { (byte) 0x0d, (byte) 0xc0, (byte) 0x38, (byte) 0x28, (byte) 0x00, (byte) 0x00, (byte) 0x00,
//				(byte) 0x00, (byte) 0x00, (byte) 0x40, (byte) 0x4f, (byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00,
//				(byte) 0x00, (byte) 0x00, (byte) 0x40, (byte) 0x50, (byte) 0x27, (byte) 0xae, (byte) 0x14, (byte) 0x80,
//				(byte) 0x00, (byte) 0x00, (byte) 0x40, (byte) 0x76, (byte) 0xbe, (byte) 0x80, (byte) 0x00, (byte) 0x00,
//				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc0, (byte) 0x5d, (byte) 0xb8, (byte) 0x91, (byte) 0x41,
//				(byte) 0x8d, (byte) 0x7a, (byte) 0x11, (byte) 0x00 };
    }

}
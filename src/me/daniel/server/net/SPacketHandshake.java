package me.daniel.server.net;

public class SPacketHandshake extends Packet {

	public SPacketHandshake() {
		bytes = new byte[] { 
				0x02, 0x00, 0x01, 0x00, 0x2d 
		};
	}

}
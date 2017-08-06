package me.daniel.server.net;

import java.nio.ByteBuffer;

public class SPacketKeepalive extends Packet {

	public SPacketKeepalive(int id) {
		bytes = ByteBuffer.allocate(4).putInt(id).array();
	}
	
}
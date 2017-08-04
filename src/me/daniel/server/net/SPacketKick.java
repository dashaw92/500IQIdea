package me.daniel.server.net;

import java.util.ArrayList;
import java.util.List;

public class SPacketKick extends Packet {
	
	public SPacketKick(String msg) {
		if(msg == null || msg.trim().isEmpty()) {
			msg = "Kicked from the server.";
		}
		bytes = serialize(msg);
	}
	
	public byte[] serialize(String msg) {
		List<Byte> data = new ArrayList<Byte>();
		data.add((byte) 0xFF);
		data.add((byte) 0x00);
		data.add((byte) msg.length());
		data.add((byte) 0x00);
		for (char c : msg.toCharArray()) {
			data.add((byte) c);
			data.add((byte) 0x00);
		}
		byte[] packet = new byte[data.size()];
		for (int i = 0; i < data.size(); i++) {
			packet[i] = data.get(i);
		}
		return packet;
	}
	
}
package me.daniel.server.net;

public class SPacketKick extends Packet {
	
	public SPacketKick(String msg) {
		if(msg == null || msg.trim().isEmpty()) {
			msg = "Kicked from the server.";
		}
		bytes = Packet.pad((byte)0xFF, true, msg);
	}
}
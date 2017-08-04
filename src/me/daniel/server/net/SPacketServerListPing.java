package me.daniel.server.net;

public class SPacketServerListPing extends SPacketKick {

	public SPacketServerListPing(String motd, int users, int slots) {
		//So annoying that super has to be the first call in a subclass constructor.
		super(String.format("%s§%d§%d", (motd == null || motd.isEmpty())? "Not A Minecraft Server" : motd, users, slots));
	}	
}
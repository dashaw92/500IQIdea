package me.daniel.server.net;

public class SPacketServerListPing extends SPacketKick {

	public SPacketServerListPing(String motd, int users, int slots) {
		//So annoying that super has to be the first call in a subclass constructor.
		super("%s\u00A7%d\u00A7%d".formatted (
				(motd == null || motd.isEmpty())? "Not A Minecraft Server" : motd,
				users,
				slots
		));
	}	
}
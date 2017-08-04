package me.daniel.server;

import java.util.ArrayList;
import java.util.List;

import me.daniel.server.net.SPacketHandshake;
import me.daniel.server.net.SPacketKick;
import me.daniel.server.net.SPacketLogin;
import me.daniel.server.net.SPacketPlayerLookAndPosition;
import me.daniel.server.net.SPacketServerListPing;
import me.daniel.server.wrapper.Player;

public class PlayerThread implements Runnable {

	private static List<PlayerThread> players = new ArrayList<>();
	private Player player;
	private Thread thread;
	
	public PlayerThread(Player player) {
		this.player = player;
		thread = new Thread(this);
		players.add(this);
		thread.start();
	}
	
	public static List<PlayerThread> getPlayers() {
		return players;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public static void stop() {
		for(PlayerThread pt : players) {
			pt.disconnect();
		}
		players.clear();
	}
	
	@SuppressWarnings("deprecation")
	public void disconnect() {
		thread.stop();
		player.close();
	}
	
	@Override
	public void run() {
		while(player.isConnected()) {
			switch(player.read()) {
				case 0x02:
					if(ServerPlugin.server.getUsers() >= ServerPlugin.server.getSlots()) {
						player.send(new SPacketKick("Server is full."));
						disconnect();
						break;
					}
					player.send(new SPacketHandshake());
					player.read();
					player.send(new SPacketLogin());
					player.read();
					player.send(new SPacketPlayerLookAndPosition());
					ServerPlugin.server.setUsers(ServerPlugin.server.getUsers()+1);
					break;
				case 0xFE: //Need to figure out why my server reads this as 0xFD. 0xFD here doesn't let client login.
					player.send(new SPacketServerListPing(ServerPlugin.server.getMotd(), ServerPlugin.server.getUsers(), ServerPlugin.server.getSlots()));
					disconnect();
					break;
				case 0xFF:
					ServerPlugin.server.setUsers(ServerPlugin.server.getUsers()-1);
					disconnect();
					break;
			}
		}
	}
	
}

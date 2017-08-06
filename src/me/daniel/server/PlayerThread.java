package me.daniel.server;

import java.util.ArrayList;
import java.util.List;

import me.daniel.server.net.SPacketChat;
import me.daniel.server.net.SPacketHandshake;
import me.daniel.server.net.SPacketKeepalive;
import me.daniel.server.net.SPacketKick;
import me.daniel.server.net.SPacketLogin;
import me.daniel.server.net.SPacketPlayerLookAndPosition;
import me.daniel.server.net.SPacketServerListPing;
import me.daniel.server.wrapper.Player;

public class PlayerThread implements Runnable {

	private static List<PlayerThread> players = new ArrayList<>();
	private Player player;
	private Thread thread;
	private boolean connected = false; //Is the player already in play state? @53
	private long keepalive;
	
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
			//Once every 5 seconds
			if(connected && System.currentTimeMillis() - keepalive > 5000) {
				player.send(new SPacketKeepalive(2)); //TODO: Implement random id here
				keepalive = System.currentTimeMillis();
			}
			switch(player.read()) {
				case 0x00:
					byte[] read = new byte[4];
					for(int i = 0; i < 4; i++) {
						read[i] = (byte)player.read();
					}
					break;
				case 0x02:
					if(connected) break; //TODO maybe not needed anymore due to DataInputStream vs InputStreamReader?
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
					connected = true;
					break;
				case 0x03:
					System.out.println("Chat packet, read: " + player.read());
					player.send(new SPacketChat("Testing :D"));
					break;
				case 0xFE:
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

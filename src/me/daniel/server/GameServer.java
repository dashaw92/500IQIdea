package me.daniel.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import me.daniel.server.net.SPacketKick;
import me.daniel.server.wrapper.Player;

public class GameServer {

	private ServerSocket socket;
	private String motd;
	private int users, slots;
	private int port;

	public GameServer(int port, String motd, int users, int slots) {
		this.port = port;
		this.motd = (motd == null)? "Not A Minecraft Server" : motd;
		this.users = users;
		this.slots = slots;
	}
	
	public void start() throws IOException {
		System.out.println("Starting my server on port " + port + ".");
		socket = new ServerSocket(port);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try { 
					while(true) {
						Socket client = socket.accept();
						new PlayerThread(new Player(client));
					}
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
			
		}).start();
	}
	
	public void stop() {
		for(PlayerThread pt : PlayerThread.getPlayers()) {
			pt.getPlayer().send(new SPacketKick("Server closed."));
		}
		PlayerThread.stop();
		users = 0;
		try {
			socket.close();
		} catch (IOException e) {
		}
	}
	
	public String getMotd() {
		return motd;
	}
	
	public void setUsers(int i) {
		users = i;
	}
	
	public int getUsers() {
		return users;
	}
	
	public int getSlots() {
		return slots;
	}
}
package me.daniel.server;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

public class ServerPlugin extends JavaPlugin {
	
	protected static GameServer server;
	
	@Override
	public void onEnable() {
		loadConfig();
		server = new GameServer(25065, "test!", 0, 10);
		try {
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		server.stop();
	}
	
	private void loadConfig() {
		//TODO
	}
	
}
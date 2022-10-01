package me.daniel.server;

import me.daniel.server.net.*;
import me.daniel.server.wrapper.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerThread implements Runnable {

    private static final List<PlayerThread> players = new ArrayList<>();
    private final Player player;
    private final Thread thread;
    private boolean connected = false; //Is the player already in play state? @53
    private long w_keepAlive, r_keepAlive;

    public PlayerThread(Player player) {
        this.player = player;
        thread = new Thread(this);
        players.add(this);
        thread.start();
        r_keepAlive = System.currentTimeMillis() + 1000;
    }

    public static List<PlayerThread> getPlayers() {
        return players;
    }

    public static void stop() {
        for (PlayerThread pt : players) {
            pt.disconnect();
        }
        players.clear();
    }

    public Player getPlayer() {
        return player;
    }

    @SuppressWarnings("deprecation")
    public void disconnect() {
        ServerPlugin.server.setUsers(ServerPlugin.server.getUsers() - 1);
        connected = false;
        thread.stop();
        player.close();
    }

    @Override
    public void run() {
        while (player.isConnected()) {
            //Once every 5 seconds
            if (connected && System.currentTimeMillis() - w_keepAlive > 20) {
                if (System.currentTimeMillis() - r_keepAlive > (20 * 500)) {
                    System.out.println("Player lost connection");
                    disconnect();
                    return;
                }

                player.send(new SPacketKeepalive(2)); //TODO: Implement random id here
                w_keepAlive = System.currentTimeMillis();
            }
            var inByte = player.read();
//			System.out.printf("Packet read: %02Xd", inByte);
            switch (inByte) {
                case 0x00 -> {
                    byte[] read = new byte[4];
                    for (int i = 0; i < 4; i++) {
                        read[i] = (byte) player.read();
                    }
                    r_keepAlive = System.currentTimeMillis();
                }
                case 0x02 -> {
                    if (connected) break; //TODO maybe not needed anymore due to DataInputStream vs InputStreamReader?
                    if (ServerPlugin.server.getUsers() >= ServerPlugin.server.getSlots()) {
                        player.send(new SPacketKick("Server is full."));
                        disconnect();
                        break;
                    }
                    player.send(new SPacketHandshake());
                    player.read();
                    player.send(new SPacketLogin());
                    player.read();
                    player.send(new SPacketPlayerLookAndPosition());
                    ServerPlugin.server.setUsers(ServerPlugin.server.getUsers() + 1);
                    connected = true;
                }
                case 0x03 -> {
                    System.out.println("Chat packet, read: " + player.read());
                    player.send(new SPacketChat("Testing :D"));
                }
                case 0xFE -> {
                    player.send(new SPacketServerListPing(ServerPlugin.server.getMotd(), ServerPlugin.server.getUsers(), ServerPlugin.server.getSlots()));
                    disconnect();
                }
                case 0xFF -> {
                    disconnect();
                }
            }
        }
    }

}

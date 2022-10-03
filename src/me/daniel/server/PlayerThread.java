package me.daniel.server;

import me.daniel.server.net.in.CLogin;
import me.daniel.server.net.out.*;
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

    public void disconnect() {
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
//			System.out.println("Packet read: %02X".formatted(inByte));
            switch (inByte) {
                case 0x00 -> {
                    int id = player.is.readIntMc();
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
                    String ign = player.is.readString16();
                    System.out.println("Player %s is connecting!".formatted(ign));
                    player.send(new SPacketLogin());
                    player.read();
//                    var login = new CLogin(player.is);
//                    System.out.println("Player %s logged in with protocol version %d!".formatted(login.ign, login.protocolVersion));
                    player.send(new SPacketPlayerLookAndPosition());
                    connected = true;
                }
                case 0x03 -> {
                    System.out.println("Chat packet, read: " + player.is.readString16());
                    player.send(new SPacketChat("Testing :D"));
                }
                case 0xFE -> {
                    player.send(new SPacketServerListPing(ServerPlugin.server.getMotd(), ServerPlugin.server.getUsers(), ServerPlugin.server.getSlots()));
                    disconnect();
                }
                case 0xFF -> disconnect();
            }
        }
    }

}

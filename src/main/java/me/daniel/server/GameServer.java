package me.daniel.server;

import me.daniel.server.net.Packet;
import me.daniel.server.net.out.SPacketKick;
import me.daniel.server.wrapper.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class GameServer {

    private ServerSocket socket;
    private final String motd;
    private int users;
	private final int slots;
    private final int port;

    public GameServer(int port, String motd, int users, int slots) {
        this.port = port;
        this.motd = (motd == null) ? "Not A Minecraft Server" : motd;
        this.users = users;
        this.slots = slots;
    }

    public void start() throws IOException {
        System.out.println("Starting my server on port " + port + ".");
        socket = new ServerSocket(port);
        new Thread(() -> {
            try {
                while (true) {
                    Socket client = socket.accept();
                    client.setTcpNoDelay(true);
                    new PlayerThread(new Player(client));
                }
            } catch (SocketException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stop() {
        for (PlayerThread pt : PlayerThread.getPlayers()) {
            pt.getPlayer().send(new SPacketKick("Server closed."));
        }
        PlayerThread.stop();
        users = 0;
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }

    public void broadcast(Packet p) {
        for (PlayerThread pt : PlayerThread.getPlayers()) {
            pt.getPlayer().send(p);
        }
    }

    public String getMotd() {
        return motd;
    }

    public int getUsers() {
        return users;
    }

    public int getSlots() {
        return slots;
    }
}
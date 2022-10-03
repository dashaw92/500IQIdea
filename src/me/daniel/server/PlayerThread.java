package me.daniel.server;

import me.daniel.server.net.in.CLogin;
import me.daniel.server.net.out.*;
import me.daniel.server.wrapper.Player;
import me.daniel.server.wrapper.Slot;

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
//            if (connected && System.currentTimeMillis() - w_keepAlive > 20) {
//                if (System.currentTimeMillis() - r_keepAlive > (20 * 500)) {
//                    System.out.println("Player lost connection");
//                    disconnect();
//                    return;
//                }
//
//                player.send(new SPacketKeepalive(2)); //TODO: Implement random id here
//                w_keepAlive = System.currentTimeMillis();
//            }
            var inByte = player.read();
//			System.out.println("Packet read: %02X".formatted(inByte));
            switch (inByte) {
                case 0x00 -> {
                    int id = player.is.readIntMc();
                    r_keepAlive = System.currentTimeMillis();
                }
                case 0x01 -> {
                    var login = new CLogin(player.is);
                    System.out.println("Player %s logged in with protocol version %d!".formatted(login.ign, login.protocolVersion));
                    player.send(new SPacketLogin());
                    player.send(new SPacketPlayerLookAndPosition());
                    player.os.writeByteMc((byte) 0x03);
                    player.os.writeString16("Hello!");
                    connected = true;
                }
                case 0x02 -> {
                    if (connected) break; //TODO maybe not needed anymore due to DataInputStream vs InputStreamReader?
                    if (ServerPlugin.server.getUsers() >= ServerPlugin.server.getSlots()) {
                        player.send(new SPacketKick("Server is full."));
                        disconnect();
                        break;
                    }
                    String ign = player.is.readString16();
                    System.out.println("Player %s is connecting!".formatted(ign));
                    player.send(new SPacketHandshake());
                }
                case 0x03 -> {
                    System.out.println("Chat packet, read: " + player.is.readString16());
                    player.send(new SPacketChat("Testing :D"));
                }
                case 0x07 -> {
                    int eid = player.is.readIntMc();
                    int target = player.is.readIntMc();
                    boolean leftClick = player.is.readBoolMc();
                }
                case 0x09 -> {
                    byte dim = player.is.readByteMc();
                    byte difficulty = player.is.readByteMc();
                    byte mode = player.is.readByteMc();
                    short height = player.is.readShortMc();
                    long seed = player.is.readLongMc();
                }
                case 0x0A -> {
                    boolean onGround = player.is.readBoolMc();
                }
                case 0x0B -> {
                    double x = player.is.readDoubleMc();
                    double y = player.is.readDoubleMc();
                    double stance = player.is.readDoubleMc();
                    double z = player.is.readDoubleMc();
                    boolean onGround = player.is.readBoolMc();
                }
                case 0x0C -> {
                    float yaw = player.is.readFloatMc();
                    float pitch = player.is.readFloatMc();
                    boolean onGround = player.is.readBoolMc();
                }
                case 0x0D -> {
                    double x = player.is.readDoubleMc();
                    double y = player.is.readDoubleMc();
                    double stance = player.is.readDoubleMc();
                    double z = player.is.readDoubleMc();
                    float yaw = player.is.readFloatMc();
                    float pitch = player.is.readFloatMc();
                    boolean onGround = player.is.readBoolMc();
                }
                case 0x0E -> {
                    byte status = player.is.readByteMc();
                    int x = player.is.readIntMc();
                    byte y = player.is.readByteMc();
                    int z = player.is.readIntMc();
                    byte face = player.is.readByteMc();
                }
                case 0x0F -> {
                    int x = player.is.readIntMc();
                    int y = player.is.readIntMc();
                    int z = player.is.readIntMc();
                    byte direction = player.is.readByteMc();
                    Slot slot = player.is.readSlot();
                }
                case 0x10 -> {
                    short newSlot = player.is.readShortMc();
                }
                case 0x11 -> {
                    int eid = player.is.readIntMc();
                    byte useBed = player.is.readByteMc();
                    int x = player.is.readIntMc();
                    byte y = player.is.readByteMc();
                    int z = player.is.readIntMc();
                }
                case 0x12 -> {
                    int eid = player.is.readIntMc();
                    byte animation = player.is.readByteMc();
                }
                case 0x13 -> {
                    int eid = player.is.readIntMc();
                    byte actionId = player.is.readByteMc();
                    switch(actionId) {
                        case 1 -> {} //crouching
                        case 2 -> {} //uncrouch
                        case 3 -> {} //leave bed
                        case 4 -> {} //start sprinting
                        case 5 -> {} //stop sprinting
                    }
                }
                case 0x15 -> {
                    int eid = player.is.readIntMc();
                    short itemId = player.is.readShortMc();
                    byte count = player.is.readByteMc();
                    short damage = player.is.readShortMc();
                    int x = player.is.readIntMc();
                    int y = player.is.readIntMc();
                    int z = player.is.readIntMc();
                    byte rotation = player.is.readByteMc();
                    byte pitch = player.is.readByteMc();
                    byte roll = player.is.readByteMc();
                }
                case 0x1C -> {
                    int eid = player.is.readIntMc();
                    short velX = player.is.readShortMc();
                    short velY = player.is.readShortMc();
                    short velZ = player.is.readShortMc();
                }
                case 0x27 -> {
                    int eid = player.is.readIntMc();
                    int vid = player.is.readIntMc();
                }
                case 0x66 -> {
                    byte wid = player.is.readByteMc();
                    short slot = player.is.readShortMc();
                    byte rightClick = player.is.readByteMc();
                    short actionNumber = player.is.readShortMc();
                    boolean shiftClick = player.is.readBoolMc();
                    Slot clickedItem = player.is.readSlot();
                }
                case 0x6A -> {
                    byte wid = player.is.readByteMc();
                    short actionNumber = player.is.readShortMc();
                    boolean accepted = player.is.readBoolMc();
                }
                case 0x6C -> {
                    byte wid = player.is.readByteMc();
                    byte enchant = player.is.readByteMc();
                }
                case 0x82 -> {
                    int x = player.is.readIntMc();
                    int y = player.is.readIntMc();
                    int z = player.is.readIntMc();
                    String line1 = player.is.readString16();
                    String line2 = player.is.readString16();
                    String line3 = player.is.readString16();
                    String line4 = player.is.readString16();
                }
                case 0xFE -> {
                    player.send(new SPacketServerListPing(ServerPlugin.server.getMotd(), ServerPlugin.server.getUsers(), ServerPlugin.server.getSlots()));
                    disconnect();
                }
                case 0xFF -> {
                    String reason = player.is.readString16();
                    disconnect();
                    System.out.println("Player left with reason: %s".formatted(reason));
                }
            }
        }
    }

}

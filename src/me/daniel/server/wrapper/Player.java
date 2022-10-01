package me.daniel.server.wrapper;

import me.daniel.server.net.Packet;
import me.daniel.server.net.SPacketKeepalive;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.HexFormat;

public class Player {
    private final Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

    public Player(Socket socket) {
        this.socket = socket;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Packet p) {
        if (!(p instanceof SPacketKeepalive)) {
            var hex = HexFormat.of().withUpperCase().withDelimiter(", ");
            System.out.println("Sending " + p.getClass().getSimpleName() + ": [" + hex.formatHex(p.getBytes()) + "]");
        }
        send(p.getBytes());
    }

    public void send(byte[] b) {
        try {
            dos.write(b);
//			Thread.sleep(10); //Force the packets to be sent as their own rather than mushed together. F*ck you tcp stack >;(
            dos.flush();
        } catch (SocketException e) {
        } catch (IOException e) {
        }
//		  catch (InterruptedException e) {}
    }

    public int read() {
        try {
            /*
             * The tale of a man and his dream (August 5th, 2017 ~ 10:05 PM EST/EDT):
             * One day, a young man by the name dashaw92 set out to write a minecraft server
             * in a bukkit plugin. After about an hour of banging away at his keyboard, he
             * built his plugin for the first time. He FTPed it to his minecraft server, and
             * loaded up minecraft. Once he saw his idea would work, he then refactored his
             * monolith. About two hours later, he sat back to admire his creation.
             * just kidding he got screwed over by InputStreamReader hahaa
             * Turns out that the InputStreamReader class isn't meant to read raw byte data,
             * like the stuff sent in the minecraft protocol (:
             */
            return dis.read();
            /* For memorial reasons:
             * int read = ((byte)isr.read) & 0xFF; //isr instanceof InputStreamReader on socket.getInputStream()
             * return read;
             */
        } catch (IOException e) {
            return Integer.MIN_VALUE;
        }
    }
}
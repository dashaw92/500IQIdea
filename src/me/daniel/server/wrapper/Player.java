package me.daniel.server.wrapper;

import me.daniel.server.net.Packet;
import me.daniel.server.net.out.SPacketKeepalive;

import java.io.*;
import java.net.Socket;
import java.util.HexFormat;

public class Player {
    private final Socket socket;
    public final McOutputStream os;
    public final McInputStream is;

    public Player(Socket socket) {
        this.socket = socket;

        OutputStream _os;
        InputStream _is;
        try {
            _os = socket.getOutputStream();
            _is = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Cannot get player I/O streams from socket!");
        }

        this.os = new McOutputStream(_os);
        this.is = new McInputStream(_is);
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
            os.write(b);
        } catch (IOException ignored) {
        }
    }

    @Deprecated
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
            return is.read();
            /* For memorial reasons:
             * int read = ((byte)isr.read) & 0xFF; //isr instanceof InputStreamReader on socket.getInputStream()
             * return read;
             */
        } catch (IOException e) {
            return Integer.MIN_VALUE;
        }
    }
}
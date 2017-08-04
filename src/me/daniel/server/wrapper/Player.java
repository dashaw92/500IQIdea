package me.daniel.server.wrapper;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import me.daniel.server.net.Packet;

public class Player {
	private Socket socket;
	private DataOutputStream dos;
	private InputStreamReader isr;
	
	public Player(Socket socket) {
		this.socket = socket;
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			isr = new InputStreamReader(socket.getInputStream());
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
		send(p.getBytes());
	}
	
	public void send(byte[] b) {
		try {
			dos.write(b);
			dos.flush();
		} catch (IOException e) {
		}
	}
	
	public int read() {
		try {
			return ((byte)isr.read()) & 0xFF;
		} catch(IOException e) {
			return -1;
		}
	}
}
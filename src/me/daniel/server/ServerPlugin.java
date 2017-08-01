package me.daniel.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public class ServerPlugin extends JavaPlugin implements Runnable {

	private Thread thread;
	private ServerSocket socket;
	private boolean running = true;
	
	@Override
	public void onDisable() {
		running = false;
		try {
			socket.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEnable() {
		System.out.println("Starting my server on port 25065.");
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		socket = null;
		try {
			socket = new ServerSocket(25065);
			while(running) {
				System.out.println("Waiting for client.");
				Socket client = socket.accept();
				System.out.println("Serving a client.");
				
				OutputStream os = client.getOutputStream();
				DataOutputStream dos = new DataOutputStream(os);
				InputStream is = client.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				int read = ((byte)isr.read()) & 0xFF;
				System.out.println("Read from client: " + read);
				switch(read) {
					case 0x02: //Client sent login packet
						System.out.println("They're trying to login");
						/*
						 * Since my goal is to just emulate a server, not BE a server, we'll just ignore all the client's demands >x)
						 */
						dos.write(new byte[] { //Handshake S -> C
								0x02, 0x00, 0x01, 0x00, 0x2d
						});
						isr.read(); //Handshake C -> S
						dos.write(new byte[] { //An official packet I copied from a real server with wireshark. Login request S -> C
								(byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xb2,
								(byte)0x00, (byte)0x00, (byte)0x56, (byte)0x99, (byte)0x7e,
								(byte)0x92, (byte)0x55, (byte)0xdc, (byte)0xe4, (byte)0xd6,
								(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
								(byte)0x01, (byte)0x80, (byte)0x14, (byte)0x06, (byte)0x00,
								(byte)0x00, (byte)0x00, (byte)0x10, (byte)0x00, (byte)0x00,
								(byte)0x00, (byte)0x40, (byte)0x00, (byte)0x00, (byte)0x01,
								(byte)0x00, (byte)0x04, (byte)0x00, (byte)0x00, (byte)0x00,
								(byte)0x00, (byte)0x00, (byte)0x06, (byte)0xa4, (byte)0x3a
						});
						isr.read(); //I tried flushing my output stream and it didn't seem to work, this does.
						dos.write(new byte[] { //Send player location.
								(byte)0x0d, (byte)0xc0, (byte)0x38, (byte)0x28, (byte)0x00, 
								(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x40, 
								(byte)0x4f, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, 
								(byte)0x00, (byte)0x00, (byte)0x40, (byte)0x50, (byte)0x27,
								(byte)0xae, (byte)0x14, (byte)0x80, (byte)0x00, (byte)0x00, 
								(byte)0x40, (byte)0x76, (byte)0xbe, (byte)0x80, (byte)0x00,
								(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xc0, 
								(byte)0x5d, (byte)0xb8, (byte)0x91, (byte)0x41, (byte)0x8d,
								(byte)0x7a, (byte)0x11, (byte)0x00
						});
						isr.read();
						/*
						 * The client will reach an end of stream eventually.
						 */
						client.setSoTimeout(5000);
						loop: while(client.isConnected()) {
							switch(isr.read() & 0xFF) {
								case 0xFF: //Handle client disconnect and free up our single thread :D
									client.close();
									break loop;
							}
						}
						break;
					case 0xFD: //no idea why the server reads it as 0xFD, wireshark even shows it as 0xFE...
						System.out.println("Server list ping");
						dos.write(SLPpack("Not A Minecraft Server", 313, 37));
						break;
				}
				
				dos.close();
				isr.close();
				System.out.println("Ended communication with a client.");
			}
			socket.close();
		} catch (IOException e) {}
		System.out.println("My server closed.");
	}
	
	
	private byte[] SLPpack(String desc, int users, int slots) {
		String concat = String.format("%s§%d§%d",desc, users, slots);
		List<Byte> bytes = new ArrayList<Byte>();
		bytes.add((byte)0xFF);
		bytes.add((byte)0x00);
		bytes.add((byte)concat.length());
		bytes.add((byte)0x00);
		for(char c : concat.toCharArray()) {
			bytes.add((byte)c);
			bytes.add((byte)0x00);
		}
		byte[] packet = new byte[bytes.size()];
		for(int i = 0; i < bytes.size(); i++) {
			packet[i] = bytes.get(i);
		}
		return packet;
	}
}
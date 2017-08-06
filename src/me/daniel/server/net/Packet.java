package me.daniel.server.net;

import java.nio.ByteBuffer;

public abstract class Packet {
	
	protected byte[] bytes;
	
	public byte[] getBytes() {
		return bytes;
	}
	
	//Helper function for subclasses
	public static byte[] pad(byte id, boolean length, String msg) {
		// This packet is (1 byte for id, 1 byte for length if true, and msg.length) * 2
		// for every 0x00
		byte[] arr = new byte[(1 + msg.length() + (length ? 1 : 0)) * 2];
		arr[0] = id;
		int index = 2;
		if(length) {
			index = 4;
			arr[2] = (byte)msg.length();
		}
		for (int i = 0; i < msg.length(); i++) {
			arr[index + (i * 2)] = (byte)msg.charAt(i);
		}
		return arr;
	}
	
	public static int decode(byte[] b) {
		ByteBuffer bb = ByteBuffer.wrap(b);
        return bb.getInt();
	}
}
package me.daniel.server.net;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Packet {

    private final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    private final DataOutputStream stream = new DataOutputStream(bytes);
    protected List<Object> data = new ArrayList<>();
    protected byte id;
    public Packet(int id) {
        this.id = (byte) id;
    }

    public byte[] getBytes() {
        try {
            stream.writeByte(id);

            for (var obj : data) {
                if (obj instanceof Byte byt) {
                    stream.writeByte(byt);
                } else if (obj instanceof Short shrt) {
                    stream.writeShort(shrt);
                } else if (obj instanceof Integer num) {
                    stream.writeInt(num);
                } else if (obj instanceof Long lng) {
                    stream.writeLong(lng);
                } else if (obj instanceof Float fl) {
                    stream.writeFloat(fl);
                } else if (obj instanceof Double dbl) {
                    stream.writeDouble(dbl);
                } else if (obj instanceof String str) {
                    writeString16(str);
                } else if (obj instanceof Boolean bool) {
                    stream.writeBoolean(bool);
                } else if (obj instanceof byte[] arr) {
                    stream.write(arr);
                } else {
                    if (obj == null) continue;
                    System.err.println("Unknown object type " + obj.getClass().getSimpleName());
                }
            }

            stream.close();
        } catch (IOException ignored) {
        }
        return bytes.toByteArray();
    }

    private void writeString16(String str) {
        try {
            stream.writeShort(str.length());
            stream.writeChars(str);
        } catch (IOException e) {
            System.err.println("Failed to write string16 " + str);
        }
    }

}
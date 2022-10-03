package me.daniel.server.net;

import me.daniel.server.wrapper.McOutputStream;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class Packet {

    private final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    private final McOutputStream stream = new McOutputStream(bytes);
    protected List<Object> data = new ArrayList<>();
    protected byte id;
    public Packet(int id) {
        this.id = (byte) id;
    }

    public byte[] getBytes() {
        stream.writeByteMc(id);

        for (var obj : data) {
            if (obj instanceof Byte byt) {
                stream.writeByteMc(byt);
            } else if (obj instanceof Short shrt) {
                stream.writeShortMc(shrt);
            } else if (obj instanceof Integer num) {
                stream.writeIntMc(num);
            } else if (obj instanceof Long lng) {
                stream.writeLongMc(lng);
            } else if (obj instanceof Float fl) {
                stream.writeFloatMc(fl);
            } else if (obj instanceof Double dbl) {
                stream.writeDoubleMc(dbl);
            } else if (obj instanceof String str) {
                stream.writeString16(str);
            } else if (obj instanceof Boolean bool) {
                stream.writeBoolMc(bool);
            } else if (obj instanceof byte[] arr) {
                stream.writeMc(arr);
            } else {
                if (obj == null) continue;
                System.err.println("Unknown object type " + obj.getClass().getSimpleName());
            }
        }

        stream.close();
        return bytes.toByteArray();
    }

}
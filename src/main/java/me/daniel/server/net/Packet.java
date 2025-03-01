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
            switch(obj) {
                case Byte byt -> stream.writeByteMc(byt);
                case Short shrt -> stream.writeShortMc(shrt);
                case Integer num -> stream.writeIntMc(num);
                case Long lng -> stream.writeLongMc(lng);
                case Float fl -> stream.writeFloatMc(fl);
                case Double dbl -> stream.writeDoubleMc(dbl);
                case String str -> stream.writeString16(str);
                case Boolean bool -> stream.writeBoolMc(bool);
                case byte[] arr -> stream.writeMc(arr);
                default -> {
                    System.err.println("Unknown object type " + obj.getClass().getSimpleName());
                }
            }
        }

        stream.close();
        return bytes.toByteArray();
    }

}
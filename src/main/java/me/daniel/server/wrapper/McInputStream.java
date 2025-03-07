package me.daniel.server.wrapper;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public final class McInputStream extends DataInputStream {

    /**
     * Creates a DataInputStream that uses the specified
     * underlying InputStream.
     *
     * @param in the specified input stream
     */
    public McInputStream(InputStream in) {
        super(in);
    }

    public void close() {
        try {
            super.close();
        } catch(IOException ex) {
            System.err.println("Failed to close input stream.");
            ex.printStackTrace();
        }
    }

    public Slot readSlot() {
        try {
            short id = readShort();
            if(id == -1) return null;
            short count = readShort();
            short damage = readShort();

            var item = new Slot(id, count, damage);
            if(!item.canAcceptEnchants()) return item;

            short nbtLen = readShort();
            if(nbtLen == -1) return item;

            byte[] nbt = new byte[nbtLen];
            int read = read(nbt);
            if(read != nbtLen) {
                System.err.println("Did not read expected amount of NBT data from slot?? Got %d, expected %d.".formatted(read, nbtLen));
                return item;
            }

            try(GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(nbt))) {
                
            }
        } catch(IOException ex) {
            System.err.println("Failed to read slot from input stream.");
            ex.printStackTrace();
            return null;
        }
        return null;
    }

    public String readString16() {
        try {
            short len = readShort();
            char[] buf = new char[len];
            for (int i = 0; i < buf.length; i++) {
                buf[i] = readChar();
            }

            return new String(buf);
        } catch(IOException ex) {
            System.err.println("Failed to read string16 from input stream.");
            ex.printStackTrace();
            return null;
        }
    }

    public int readUByteMC() {
        try {
            return readUnsignedByte();
        } catch (IOException ex) {
            System.err.println("Failed to read ubyte from input stream.");
            ex.printStackTrace();
            return -1;
        }
    }

    public byte readByteMc() {
        try {
            return readByte();
        } catch(IOException ex) {
            System.err.println("Failed to read byte from input stream.");
            ex.printStackTrace();
            return -1;
        }
    }

    public short readShortMc() {
        try {
            return readShort();
        } catch(IOException ex) {
            System.err.println("Failed to read short from input stream.");
            ex.printStackTrace();
            return -1;
        }
    }

    public int readIntMc() {
        try {
            return readInt();
        } catch(IOException ex) {
            System.err.println("Failed to read int from input stream.");
            ex.printStackTrace();
            return -1;
        }
    }

    public long readLongMc() {
        try {
            return readLong();
        } catch(IOException ex) {
            System.err.println("Failed to read long from input stream.");
            ex.printStackTrace();
            return -1;
        }
    }

    public float readFloatMc() {
        try {
            return readFloat();
        } catch(IOException ex) {
            System.err.println("Failed to read float from input stream.");
            ex.printStackTrace();
            return -1F;
        }
    }

    public double readDoubleMc() {
        try {
            return readDouble();
        } catch(IOException ex) {
            System.err.println("Failed to read double from input stream.");
            ex.printStackTrace();
            return -1d;
        }
    }

    public boolean readBoolMc() {
        try {
            return readBoolean();
        } catch (IOException ex) {
            System.err.println("Failed to read bool from input stream.");
            ex.printStackTrace();
            return false;
        }
    }
}

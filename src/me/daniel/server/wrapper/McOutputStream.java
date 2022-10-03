package me.daniel.server.wrapper;

import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class McOutputStream extends DataOutputStream {


    /**
     * Creates a new data output stream to write data to the specified
     * underlying output stream. The counter {@code written} is
     * set to zero.
     *
     * @param out the underlying output stream, to be saved for later
     *            use.
     * @see FilterOutputStream#out
     */
    public McOutputStream(OutputStream out) {
        super(out);
    }

    public void close() {
        try {
            super.close();
        } catch(IOException ex) {
            System.err.println("Failed to close output stream.");
            ex.printStackTrace();
        }
    }

    public void writeMc(byte[] b) {
        try {
            write(b);
        } catch(IOException ex) {
            System.err.println("Failed to write byte[] to output stream.");
            ex.printStackTrace();
        }
    }

    public void writeString16(String string) {
        try {
            writeShort(string.length());
            writeChars(string);
        } catch(IOException ex) {
            System.err.println("Failed to write string16 to output stream.");
            ex.printStackTrace();
        }
    }

    public void writeByteMc(byte b) {
        try {
            writeByte(b);
        } catch(IOException ex) {
            System.err.println("Failed to write byte to output stream.");
            ex.printStackTrace();
        }
    }

    public void writeShortMc(short sh) {
        try {
            writeShort(sh);
        } catch(IOException ex) {
            System.err.println("Failed to write short to output stream.");
            ex.printStackTrace();
        }
    }

    public void writeIntMc(int i) {
        try {
            writeInt(i);
        } catch(IOException ex) {
            System.err.println("Failed to write short to output stream.");
            ex.printStackTrace();
        }
    }

    public void writeLongMc(long l) {
        try {
            writeLong(l);
        } catch(IOException ex) {
            System.err.println("Failed to write long to output stream.");
            ex.printStackTrace();
        }
    }

    public void writeFloatMc(float f) {
        try {
            writeFloat(f);
        } catch(IOException ex) {
            System.err.println("Failed to write float to output stream.");
            ex.printStackTrace();
        }
    }

    public void writeDoubleMc(double d) {
        try {
            writeDouble(d);
        } catch(IOException ex) {
            System.err.println("Failed to write double to output stream.");
            ex.printStackTrace();
        }
    }

    public void writeBoolMc(boolean bool) {
        try {
            writeBoolean(bool);
        } catch (IOException ex) {
            System.err.println("Failed to write bool to output stream.");
            ex.printStackTrace();
        }
    }
}

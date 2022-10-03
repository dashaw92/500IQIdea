String readString(DataInputStream dis) throws IOException {
    short len = dis.readShort();
    char[] buf = new char[len];
    for(int i = 0; i < buf.length; i++) {
        buf[i] = dis.readChar();
    }

    return new String(buf);
}

var goodPacket = new byte[] {
    (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xa0, (byte)0x00, (byte)0x00, (byte)0x96, (byte)0xf2, 
    (byte)0xad, (byte)0x2b, (byte)0x2a, (byte)0x26, (byte)0x81, (byte)0xcb, (byte)0x00, (byte)0x00, 
    (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x01, (byte)0x80, (byte)0x14, (byte)0x06, (byte)0xff, 
    (byte)0xff, (byte)0xfe, (byte)0x0e, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x40, (byte)0xff, 
    (byte)0xff, (byte)0xff, (byte)0xa9, (byte)0x04, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
    (byte)0x00, (byte)0x03, (byte)0x9b, (byte)0x89
};


var test = new byte[] {
    (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xA0, (byte)0x00, (byte)0x00, (byte)0x96, (byte)0xF2, 
    (byte)0xAD, (byte)0x2B, (byte)0x2A, (byte)0x26, (byte)0x81, (byte)0xCB, (byte)0x00, (byte)0x00, 
    (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
    (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x80, (byte)0x00, (byte)0x00, 
    (byte)0x00, (byte)0x14
};

var badPacket = new byte[] {
    (byte)0x00, (byte)0x00, (byte)0x03, (byte)0xE8, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
    (byte)0x01, (byte)0x1F, (byte)0x71, (byte)0xFB, (byte)0x09, (byte)0x22, (byte)0x00, (byte)0x00, 
    (byte)0x00, (byte)0x01, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x80, 
    (byte)0x0A
};

record Login(int eid, String empty, long seed, int mode, byte dimension, byte difficulty, int height, byte maxPlayers) {}

Login readLogin(byte[] input) throws IOException {
    var bis = new ByteArrayInputStream(input);
    var dis = new DataInputStream(bis);

    int eid = dis.readInt();
    String empty = readString(dis);
    long seed = dis.readLong();
    int mode = dis.readInt();
    byte dimension = dis.readByte();
    byte difficulty = dis.readByte();
    int height = dis.readByte() & 0xFF;
    byte maxPlayers = dis.readByte();

    return new Login(eid, empty, seed, mode, dimension, difficulty, height, maxPlayers);
}

var goodLogin = readLogin(goodPacket);
var badLogin = readLogin(badPacket);
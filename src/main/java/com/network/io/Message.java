package com.network.io;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Message implements IAdvanceIOMessage {

    public byte command;
    private ByteArrayOutputStream os;
    private DataOutputStream dos;
    private ByteArrayInputStream is;
    private DataInputStream dis;

    public Message(int command) {
        this((byte) command);
    }

    public Message(byte command) {
        this.command = command;
        this.os = new ByteArrayOutputStream();
        this.dos = new DataOutputStream(this.os);
    }

    public Message(byte command, byte[] data) {
        this.command = command;
        this.is = new ByteArrayInputStream(data);
        this.dis = new DataInputStream(this.is);
    }

    public DataOutputStream writer() {
        return this.dos;
    }

    public DataInputStream reader() {
        return this.dis;
    }

    public byte[] getData() {
        return this.os.toByteArray();
    }

    public void cleanup() {
        try {
            if (this.is != null) {
                this.is.close();
            }
            if (this.os != null) {
                this.os.close();
            }
            if (this.dis != null) {
                this.dis.close();
            }
            if (this.dos != null) {
                this.dos.close();
            }
        } catch (Exception var2) {
        }
    }

    public void dispose() {
        this.cleanup();
        this.dis = null;
        this.is = null;
        this.dos = null;
        this.os = null;
    }

    public int read() throws IOException {
        return this.reader().read();
    }

    public int read(byte[] b) throws IOException {
        return this.reader().read(b);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return this.reader().read(b, off, len);
    }

    public boolean readBoolean() throws IOException {
        return this.reader().readBoolean();
    }

    public byte readByte() throws IOException {
        return this.reader().readByte();
    }

    public short readShort() throws IOException {
        return this.reader().readShort();
    }

    public int readInt() throws IOException {
        return this.reader().readInt();
    }

    public long readLong() throws IOException {
        return this.reader().readLong();
    }

    public float readFloat() throws IOException {
        return this.reader().readFloat();
    }

    public double readDouble() throws IOException {
        return this.reader().readDouble();
    }

    public char readChar() throws IOException {
        return this.reader().readChar();
    }

    public String readUTF() throws IOException {
        return this.reader().readUTF();
    }

    public void readFully(byte[] b) throws IOException {
        this.reader().readFully(b);
    }

    public void readFully(byte[] b, int off, int len) throws IOException {
        this.reader().readFully(b, off, len);
    }

    public int readUnsignedByte() throws IOException {
        return this.reader().readUnsignedByte();
    }

    public int readUnsignedShort() throws IOException {
        return this.reader().readUnsignedShort();
    }

    public void write(byte[] b) throws IOException {
        this.writer().write(b);
    }

    public void write(int b) throws IOException {
        this.writer().write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        this.writer().write(b, off, len);
    }

    public void writeBoolean(boolean v) throws IOException {
        this.writer().writeBoolean(v);
    }

    public void writeByte(int v) throws IOException {
        this.writer().writeByte(v);
    }

    public void writeBytes(String s) throws IOException {
        this.writer().writeBytes(s);
    }

    public void writeChar(int v) throws IOException {
        this.writer().writeChar(v);
    }

    public void writeChars(String s) throws IOException {
        this.writer().writeChars(s);
    }

    public void writeDouble(double v) throws IOException {
        this.writer().writeDouble(v);
    }

    public void writeFloat(float v) throws IOException {
        this.writer().writeFloat(v);
    }

    public void writeInt(int v) throws IOException {
        this.writer().writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        this.writer().writeLong(v);
    }

    public void writeShort(int v) throws IOException {
        this.writer().writeShort(v);
    }

    public void writeUTF(String str) throws IOException {
        this.writer().writeUTF(str);
    }

    public BufferedImage readImage() throws IOException {
        int size = this.readInt();
        byte[] dataImage = new byte[size];
        this.read(dataImage);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(dataImage));
        return image;
    }

    public void writeImage(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        byte[] dataImage = baos.toByteArray();
        this.writeInt(dataImage.length);
        this.write(dataImage);
    }

}


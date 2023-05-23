package com.network.example;

import com.network.handler.IMessageSendCollect;
import com.network.io.Message;
import com.network.session.ISession;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class MessageSendCollect implements IMessageSendCollect {

    private int curR = 0;
    private int curW = 0;

    public MessageSendCollect() {
    }

    public Message readMessage(ISession session, DataInputStream dis) throws Exception {
        byte cmd = dis.readByte();
        if (session.sentKey()) {
            cmd = this.readKey(session, cmd);
        }
        int size;
        if (session.sentKey()) {
            byte b1 = dis.readByte();
            byte b2 = dis.readByte();
            size = (this.readKey(session, b1) & 255) << 8 | this.readKey(session, b2) & 255;
        } else {
            size = dis.readUnsignedShort();
        }
        byte[] data = new byte[size];
        int len = 0;
        int byteRead = 0;
        while (len != -1 && byteRead < size) {
            len = dis.read(data, byteRead, size - byteRead);
            if (len > 0) {
                byteRead += len;
            }
        }
        if (session.sentKey()) {
            for (int i = 0; i < data.length; ++i) {
                data[i] = this.readKey(session, data[i]);
            }
        }
        return new Message(cmd, data);
    }

    public byte readKey(ISession session, byte b) {
        byte i = (byte) (session.getKey()[this.curR++] & 255 ^ b & 255);
        if (this.curR >= session.getKey().length) {
            this.curR %= session.getKey().length;
        }
        return i;
    }

    public void doSendMessage(ISession session, DataOutputStream dos, Message msg) throws Exception {
        try {
            byte[] data = msg.getData();
            if (session.sentKey()) {
                byte b = this.writeKey(session, msg.command);
                dos.writeByte(b);
            } else {
                dos.writeByte(msg.command);
            }
            if (data == null) {
                dos.writeShort(0);
            } else {
                int size = data.length;
                byte byte1;
                byte byte2;
                if (msg.command != -32 && msg.command != -66 && msg.command != -74 && msg.command != 11 && msg.command != -67 && msg.command != -87 && msg.command != 66) {
                    if (session.sentKey()) {
                        byte1 = this.writeKey(session, (byte) (size >> 8));
                        dos.writeByte(byte1);
                        byte2 = this.writeKey(session, (byte) (size & 255));
                        dos.writeByte(byte2);
                    } else {
                        dos.writeShort(size);
                    }
                } else {
                    byte1 = this.writeKey(session, (byte) size);
                    dos.writeByte(byte1 - 128);
                    byte2 = this.writeKey(session, (byte) (size >> 8));
                    dos.writeByte(byte2 - 128);
                    byte b3 = this.writeKey(session, (byte) (size >> 16));
                    dos.writeByte(b3 - 128);
                }
                if (session.sentKey()) {
                    for (int i = 0; i < data.length; ++i) {
                        data[i] = this.writeKey(session, data[i]);
                    }
                }
                dos.write(data);
            }
            dos.flush();
            msg.cleanup();
        } catch (Exception var9) {
        }
    }

    public byte writeKey(ISession session, byte b) {
        byte i = (byte) (session.getKey()[this.curW++] & 255 ^ b & 255);
        if (this.curW >= session.getKey().length) {
            this.curW %= session.getKey().length;
        }
        return i;
    }

}


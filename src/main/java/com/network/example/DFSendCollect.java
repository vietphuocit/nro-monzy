package com.network.example;

import com.network.handler.IMessageSendCollect;
import com.network.io.Message;
import com.network.session.ISession;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class DFSendCollect implements IMessageSendCollect {

    public DFSendCollect() {
    }

    public Message readMessage(ISession session, DataInputStream dis) throws Exception {
        byte cmd = dis.readByte();
        int size = dis.readInt();
        byte[] data = new byte[size];
        int len = 0;
        int byteRead = 0;
        while (len != -1 && byteRead < size) {
            len = dis.read(data, byteRead, size - byteRead);
            if (len > 0) {
                byteRead += len;
            }
        }
        return new Message(cmd, data);
    }

    public byte readKey(ISession session, byte b) {
        return -1;
    }

    public void doSendMessage(ISession session, DataOutputStream dos, Message msg) throws Exception {
        try {
            byte[] data = msg.getData();
            dos.writeByte(msg.command);
            if (data != null) {
                dos.writeInt(data.length);
                dos.write(data);
            } else {
                dos.writeInt(0);
            }
            dos.flush();
        } catch (Exception var5) {
        }
    }

    public byte writeKey(ISession session, byte b) {
        return -1;
    }

}


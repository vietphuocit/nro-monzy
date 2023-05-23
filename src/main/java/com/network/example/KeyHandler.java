package com.network.example;

import com.network.CommandMessage;
import com.network.handler.IKeySessionHandler;
import com.network.io.Message;
import com.network.session.ISession;

public class KeyHandler implements IKeySessionHandler {

    public KeyHandler() {
    }

    public void sendKey(ISession session) {
        Message msg = new Message(CommandMessage.REQUEST_KEY);
        try {
            byte[] KEYS = session.getKey();
            msg.writer().writeByte(KEYS.length);
            msg.writer().writeByte(KEYS[0]);
            for (int i = 1; i < KEYS.length; ++i) {
                msg.writer().writeByte(KEYS[i] ^ KEYS[i - 1]);
            }
            session.doSendMessage(msg);
            msg.cleanup();
            session.setSentKey(true);
        } catch (Exception var5) {
        }
    }

    public void setKey(ISession session, Message message) throws Exception {
        try {
            byte b = message.reader().readByte();
            byte[] KEYS = new byte[b];
            int j;
            for (j = 0; j < b; ++j) {
                KEYS[j] = message.reader().readByte();
            }
            for (j = 0; j < KEYS.length - 1; ++j) {
                KEYS[j + 1] ^= KEYS[j];
            }
            session.setKey(KEYS);
            session.setSentKey(true);
        } catch (Exception var6) {
        }
    }

}


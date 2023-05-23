package com.network.example;

import com.network.handler.IMessageHandler;
import com.network.io.Message;
import com.network.session.ISession;

public class MessageHandler implements IMessageHandler {

    public MessageHandler() {
    }

    public void onMessage(ISession session, Message msg) throws Exception {
        System.out.println(msg.reader().readUTF());
        msg.cleanup();
    }

}


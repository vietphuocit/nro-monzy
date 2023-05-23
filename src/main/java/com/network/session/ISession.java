package com.network.session;

import com.network.handler.IKeySessionHandler;
import com.network.handler.IMessageHandler;
import com.network.handler.IMessageSendCollect;
import com.network.io.Message;

public interface ISession extends IKey {

    TypeSession getTypeSession();

    ISession setSendCollect(IMessageSendCollect var1);

    ISession setMessageHandler(IMessageHandler var1);

    ISession setKeyHandler(IKeySessionHandler var1);

    ISession startSend();

    ISession startCollect();

    ISession start();

    ISession setReconnect(boolean var1);

    void initThreadSession();

    void reconnect();

    String getIP();

    boolean isConnected();

    long getID();

    void sendMessage(Message var1);

    void doSendMessage(Message var1) throws Exception;

    void disconnect();

    void dispose();

    int getNumMessages();

}

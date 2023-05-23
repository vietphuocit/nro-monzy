package com.network.handler;

import com.network.io.Message;
import com.network.session.ISession;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface IMessageSendCollect {

    Message readMessage(ISession var1, DataInputStream var2) throws Exception;

    byte readKey(ISession var1, byte var2);

    void doSendMessage(ISession var1, DataOutputStream var2, Message var3) throws Exception;

    byte writeKey(ISession var1, byte var2);

}


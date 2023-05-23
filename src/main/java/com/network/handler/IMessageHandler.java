package com.network.handler;

import com.network.io.Message;
import com.network.session.ISession;

public interface IMessageHandler {

    void onMessage(ISession var1, Message var2) throws Exception;

}
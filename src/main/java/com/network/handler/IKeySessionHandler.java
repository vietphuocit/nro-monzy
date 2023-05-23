package com.network.handler;

import com.network.io.Message;
import com.network.session.ISession;

public interface IKeySessionHandler {

    void sendKey(ISession var1);

    void setKey(ISession var1, Message var2) throws Exception;

}

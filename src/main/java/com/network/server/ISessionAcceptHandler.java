package com.network.server;

import com.network.session.ISession;

public interface ISessionAcceptHandler {

    void sessionInit(ISession var1);

    void sessionDisconnect(ISession var1);

}

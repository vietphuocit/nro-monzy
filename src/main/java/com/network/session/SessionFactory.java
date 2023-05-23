package com.network.session;

import java.net.Socket;

public class SessionFactory {

    private static SessionFactory I;

    public SessionFactory() {
    }

    public static SessionFactory gI() {
        if (I == null) {
            I = new SessionFactory();
        }
        return I;
    }

    public ISession cloneSession(Class clazz, Socket socket) throws Exception {
        return (ISession) clazz.getConstructor(Socket.class).newInstance(socket);
    }

}

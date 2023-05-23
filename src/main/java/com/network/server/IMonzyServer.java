package com.network.server;

public interface IMonzyServer extends Runnable {

    IMonzyServer init();

    IMonzyServer start(int var1) throws Exception;

    IMonzyServer setAcceptHandler(ISessionAcceptHandler var1);

    IMonzyServer close();

    IMonzyServer dispose();

    IMonzyServer randomKey(boolean var1);

    IMonzyServer setDoSomeThingWhenClose(IServerClose var1);

    IMonzyServer setTypeSessioClone(Class var1) throws Exception;

    ISessionAcceptHandler getAcceptHandler() throws Exception;

    boolean isRandomKey();

    void stopConnect();

}

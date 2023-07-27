package com.network.server;

public interface IMonzyServer extends Runnable {

    IMonzyServer init();

    IMonzyServer start(int var1) throws Exception;

    IMonzyServer close();

    IMonzyServer dispose();

    IMonzyServer randomKey(boolean var1);

    IMonzyServer setDoSomeThingWhenClose(IServerClose var1);

    IMonzyServer setTypeSessioClone(Class var1) throws Exception;

    ISessionAcceptHandler getAcceptHandler() throws Exception;

    IMonzyServer setAcceptHandler(ISessionAcceptHandler var1);

    boolean isRandomKey();

    void stopConnect();

}

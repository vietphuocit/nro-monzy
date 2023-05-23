package com.network.server;

import com.network.session.ISession;
import com.network.session.Session;
import com.network.session.SessionFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MonzyServer implements IMonzyServer {

    private static MonzyServer I;
    private int port = -1;
    private ServerSocket serverListen;
    private Class sessionClone = Session.class;
    private boolean start;
    private boolean randomKey;
    private IServerClose serverClose;
    private ISessionAcceptHandler acceptHandler;
    private Thread loopServer;

    public static MonzyServer gI() {
        if (I == null) {
            I = new MonzyServer();
        }
        return I;
    }

    private MonzyServer() {
    }

    public IMonzyServer init() {
        this.loopServer = new Thread(this);
        return this;
    }

    public IMonzyServer start(int port) throws Exception {
        if (port < 0) {
            throw new Exception("Vui lòng khởi tạo port server!");
        } else if (this.acceptHandler == null) {
            throw new Exception("AcceptHandler chưa được khởi tạo!");
        } else if (!ISession.class.isAssignableFrom(this.sessionClone)) {
            throw new Exception("Type session clone không hợp lệ!");
        } else {
            try {
                this.port = port;
                this.serverListen = new ServerSocket(port);
            } catch (IOException var3) {
                System.out.println("Lỗi khởi tạo server tại port " + port);
                System.exit(0);
            }
            this.start = true;
            this.loopServer.start();
            System.out.println("Server Girlkun đang chạy tại port " + this.port);
            return this;
        }
    }

    public IMonzyServer close() {
        this.start = false;
        if (this.serverListen != null) {
            try {
                this.serverListen.close();
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }
        if (this.serverClose != null) {
            this.serverClose.serverClose();
        }
        System.out.println("Server Girlkun đã đóng!");
        return this;
    }

    public IMonzyServer dispose() {
        this.acceptHandler = null;
        this.loopServer = null;
        this.serverListen = null;
        return this;
    }

    public IMonzyServer setAcceptHandler(ISessionAcceptHandler handler) {
        this.acceptHandler = handler;
        return this;
    }

    public void run() {
        while (this.start) {
            try {
                Socket socket = this.serverListen.accept();
                ISession session = SessionFactory.gI().cloneSession(this.sessionClone, socket);
                this.acceptHandler.sessionInit(session);
                MonzySessionManager.gI().putSession(session);
            } catch (IOException var3) {
                var3.printStackTrace();
            } catch (Exception var4) {
                Logger.getLogger(MonzyServer.class.getName()).log(Level.SEVERE, null, var4);
            }
        }
    }

    public IMonzyServer setDoSomeThingWhenClose(IServerClose serverClose) {
        this.serverClose = serverClose;
        return this;
    }

    public IMonzyServer randomKey(boolean isRandom) {
        this.randomKey = isRandom;
        return this;
    }

    public boolean isRandomKey() {
        return this.randomKey;
    }

    public IMonzyServer setTypeSessioClone(Class clazz) throws Exception {
        this.sessionClone = clazz;
        return this;
    }

    public ISessionAcceptHandler getAcceptHandler() throws Exception {
        if (this.acceptHandler == null) {
            throw new Exception("AcceptHandler chưa được khởi tạo!");
        } else {
            return this.acceptHandler;
        }
    }

    public void stopConnect() {
        this.start = false;
    }

}


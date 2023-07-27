package com.network.session;

import com.network.handler.IKeySessionHandler;
import com.network.handler.IMessageHandler;
import com.network.handler.IMessageSendCollect;
import com.network.io.Collector;
import com.network.io.Message;
import com.network.io.Sender;
import com.network.server.MonzyServer;
import com.network.server.MonzySessionManager;
import com.network.util.StringUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Session implements ISession {

    private static ISession I;
    private static int ID_INIT;
    public TypeSession typeSession;
    public int id;
    private byte[] KEYS = "Girlkun75".getBytes();
    private boolean sentKey;
    private Socket socket;
    private boolean connected;
    private boolean reconnect;
    private Sender sender;
    private Collector collector;
    private Thread tSender;
    private Thread tCollector;
    private IKeySessionHandler keyHandler;
    private String ip;
    private String host;
    private int port;

    public Session(String host, int port) throws IOException {
        this.id = 752002;
        this.socket = new Socket(host, port);
        this.socket.setSendBufferSize(1048576);
        this.socket.setReceiveBufferSize(1048576);
        this.typeSession = TypeSession.CLIENT;
        this.connected = true;
        this.host = host;
        this.port = port;
        this.initThreadSession();
    }

    public Session(Socket socket) {
        this.id = ID_INIT++;
        this.typeSession = TypeSession.SERVER;
        this.socket = socket;
        try {
            this.socket.setSendBufferSize(1048576);
            this.socket.setReceiveBufferSize(1048576);
        } catch (Exception var3) {
        }
        this.connected = true;
        this.ip = ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().toString().replace("/", "");
        this.initThreadSession();
    }

    public static ISession gI() throws Exception {
        if (I == null) {
            throw new Exception("Instance chưa được khởi tạo!");
        } else {
            return I;
        }
    }

    public static ISession initInstance(String host, int port) throws Exception {
        if (I != null) {
            throw new Exception("Instance đã được khởi tạo!");
        } else {
            I = new Session(host, port);
            return I;
        }
    }

    public void sendMessage(Message msg) {
        if (this.isConnected() && this.sender.getNumMessages() < 200) {
            this.sender.sendMessage(msg);
        }
    }

    public ISession setSendCollect(IMessageSendCollect collect) {
        this.sender.setSend(collect);
        this.collector.setCollect(collect);
        return this;
    }

    public ISession setMessageHandler(IMessageHandler handler) {
        this.collector.setMessageHandler(handler);
        return this;
    }

    public ISession setKeyHandler(IKeySessionHandler handler) {
        this.keyHandler = handler;
        return this;
    }

    public ISession startSend() {
        this.tSender.start();
        return this;
    }

    public ISession startCollect() {
        this.tCollector.start();
        return this;
    }

    public String getIP() {
        return this.ip;
    }

    public long getID() {
        return this.id;
    }

    public void disconnect() {
        this.connected = false;
        this.sentKey = false;
        if (this.sender != null) {
            this.sender.close();
        }
        if (this.collector != null) {
            this.collector.close();
        }
        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException var2) {
            }
        }
        if (this.reconnect) {
            this.reconnect();
        } else {
            this.dispose();
        }
    }

    public void dispose() {
        if (this.sender != null) {
            this.sender.dispose();
        }
        if (this.collector != null) {
            this.collector.dispose();
        }
        this.socket = null;
        this.sender = null;
        this.collector = null;
        this.tSender = null;
        this.tCollector = null;
        this.ip = null;
        MonzySessionManager.gI().removeSession(this);
    }

    public void sendKey() throws Exception {
        if (this.keyHandler == null) {
            throw new Exception("Key handler chưa được khởi tạo!");
        } else {
            if (MonzyServer.gI().isRandomKey()) {
                this.KEYS = StringUtil.randomText(7).getBytes();
            }
            this.keyHandler.sendKey(this);
        }
    }

    public boolean sentKey() {
        return this.sentKey;
    }

    public void setSentKey(boolean sent) {
        this.sentKey = sent;
    }

    public void doSendMessage(Message msg) throws Exception {
        this.sender.doSendMessage(msg);
    }

    public ISession start() {
        this.tSender.start();
        this.tCollector.start();
        return this;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public byte[] getKey() {
        return this.KEYS;
    }

    public void setKey(Message message) throws Exception {
        if (this.keyHandler == null) {
            throw new Exception("Key handler chưa được khởi tạo!");
        } else {
            this.keyHandler.setKey(this, message);
        }
    }

    public void setKey(byte[] key) {
        this.KEYS = key;
    }

    public TypeSession getTypeSession() {
        return this.typeSession;
    }

    public ISession setReconnect(boolean b) {
        this.reconnect = b;
        return this;
    }

    public int getNumMessages() {
        return this.isConnected() ? this.sender.getNumMessages() : -1;
    }

    public void reconnect() {
        if (this.typeSession == TypeSession.CLIENT && !this.isConnected()) {
            try {
                this.socket = new Socket(this.host, this.port);
                this.connected = true;
                this.initThreadSession();
                this.start();
            } catch (Exception var4) {
                try {
                    Thread.sleep(1000L);
                    this.reconnect();
                } catch (Exception var3) {
                    var3.printStackTrace();
                }
            }
        }
    }

    public void initThreadSession() {
        this.tSender = new Thread(this.sender != null ? this.sender.setSocket(this.socket) : (this.sender = new Sender(this, this.socket)));
        this.tCollector = new Thread(this.collector != null ? this.collector.setSocket(this.socket) : (this.collector = new Collector(this, this.socket)));
    }

}

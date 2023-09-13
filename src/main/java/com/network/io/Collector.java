package com.network.io;

import com.monzy.utils.Logger;
import com.network.CommandMessage;
import com.network.handler.IMessageHandler;
import com.network.handler.IMessageSendCollect;
import com.network.server.MonzyServer;
import com.network.session.ISession;
import com.network.session.TypeSession;

import java.io.DataInputStream;
import java.net.Socket;

public class Collector implements Runnable {

    private ISession session;
    private DataInputStream dis;
    private IMessageSendCollect collect;
    private IMessageHandler messageHandler;

    public Collector(ISession session, Socket socket) {
        this.session = session;
        this.setSocket(socket);
    }

    public Collector setSocket(Socket socket) {
        try {
            this.dis = new DataInputStream(socket.getInputStream());
        } catch (Exception var3) {
        }
        return this;
    }

    public void run() {
        try {
            while (true) {
                if (this.session.isConnected()) {
                    Message msg = this.collect.readMessage(this.session, this.dis);
                    if (msg.command == CommandMessage.REQUEST_KEY) {
                        if (this.session.getTypeSession() == TypeSession.SERVER) {
                            this.session.sendKey();
                        } else {
                            this.session.setKey(msg);
                        }
                    } else {
                        this.messageHandler.onMessage(this.session, msg);
                    }
                    msg.cleanup();
                }
                Thread.sleep(1L);
            }
        } catch (Exception var4) {
            try {
                MonzyServer.gI().getAcceptHandler().sessionDisconnect(this.session);
            } catch (Exception var3) {
            }
            if (this.session != null) {
                Logger.log(Logger.RED, "Mất kết nối với session " + this.session.getIP() + "...");
                this.session.disconnect();
            }
        }
    }

    public void setCollect(IMessageSendCollect collect) {
        this.collect = collect;
    }

    public void setMessageHandler(IMessageHandler handler) {
        this.messageHandler = handler;
    }

    public void close() {
        if (this.dis != null) {
            try {
                this.dis.close();
            } catch (Exception var2) {
            }
        }
    }

    public void dispose() {
        this.session = null;
        this.dis = null;
        this.collect = null;
    }

}
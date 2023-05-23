package com.network.io;

import com.network.handler.IMessageSendCollect;
import com.network.session.ISession;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Sender implements Runnable {

    private ISession session;
    private ArrayList<Message> messages;
    private DataOutputStream dos;
    private IMessageSendCollect sendCollect;

    public Sender(ISession session, Socket socket) {
        try {
            this.session = session;
            this.messages = new ArrayList();
            this.setSocket(socket);
        } catch (Exception var4) {
        }
    }

    public Sender setSocket(Socket socket) {
        try {
            this.dos = new DataOutputStream(socket.getOutputStream());
        } catch (Exception var3) {
        }
        return this;
    }

    public void run() {
        while (this.session != null && this.session.isConnected()) {
            try {
                Message message;
                for (; this.messages.size() > 0; message = null) {
                    message = this.messages.remove(0);
                    if (message != null) {
                        this.doSendMessage(message);
                    }
                }
                Thread.sleep(1L);
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }
    }

    public synchronized void doSendMessage(Message message) throws Exception {
        this.sendCollect.doSendMessage(this.session, this.dos, message);
    }

    public synchronized void sendMessage(Message msg) {
        if (this.session.isConnected()) {
            this.messages.add(msg);
        }
    }

    public void setSend(IMessageSendCollect sendCollect) {
        this.sendCollect = sendCollect;
    }

    public int getNumMessages() {
        return this.messages != null ? this.messages.size() : -1;
    }

    public void close() {
        if (this.messages != null) {
            this.messages.clear();
        }
        if (this.dos != null) {
            try {
                this.dos.close();
            } catch (Exception var2) {
            }
        }
    }

    public void dispose() {
        this.session = null;
        this.messages = null;
        this.sendCollect = null;
        this.dos = null;
    }

}

package com.monzy.server.io;

import com.monzy.data.DataGame;
import com.network.example.KeyHandler;
import com.network.session.ISession;

public class MyKeyHandler extends KeyHandler {

    @Override
    public void sendKey(ISession session) {
        super.sendKey(session);
        DataGame.sendVersionRes(session);
    }

}























package com.network.session;

import com.network.io.Message;

public interface IKey {

    void sendKey() throws Exception;

    void setKey(Message var1) throws Exception;

    void setKey(byte[] var1);

    byte[] getKey();

    boolean sentKey();

    void setSentKey(boolean var1);

}

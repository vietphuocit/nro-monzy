package com.monzy.services;

import com.monzy.server.Manager;
import com.monzy.utils.Logger;

public class TopService implements Runnable {

    @Override
    public void run() {
        while (true) {
            try {
                Manager.TOP_NV = Manager.readTop(Manager.QUERY_TOP_NV);
                Manager.TOP_SM = Manager.readTop(Manager.QUERY_TOP_SM);
                Manager.TOP_NAP = Manager.readTop(Manager.QUERY_TOP_NAP);
                Thread.sleep(1000 * 60 * Manager.TIME_READ_TOP);
            } catch (Exception ignored) {
                Logger.error("Lỗi đọc top");
            }
        }
    }

}

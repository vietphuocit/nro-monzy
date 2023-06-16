package com.monzy.services;

import com.database.Database;
import com.monzy.server.Manager;
import com.monzy.utils.Logger;

import java.sql.Connection;

public class TopService implements Runnable {

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000 * 60 * Manager.TIME_READ_TOP);
                try (Connection con = Database.getConnection()) {
                    Manager.TOP_NV = Manager.readTop(Manager.QUERY_TOP_NV, con);
                    Manager.TOP_SM = Manager.readTop(Manager.QUERY_TOP_SM, con);
                    Manager.TOP_NAP = Manager.readTop(Manager.QUERY_TOP_NAP, con);
                } catch (Exception ignored) {
                    Logger.error("Lỗi đọc top");
                }
            } catch (Exception ignored) {
            }
        }
    }

}

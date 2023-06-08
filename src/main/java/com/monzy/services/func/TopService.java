package com.monzy.services.func;

import com.database.Database;
import com.monzy.server.Manager;
import com.monzy.utils.Logger;
import com.monzy.utils.TimeUtil;
import com.monzy.utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Date;

public class TopService implements Runnable {

    private static TopService i;

    public static TopService gI() {
        if (i == null) {
            i = new TopService();
        }
        return i;
    }

    @Override
    public void run() {
        while (true) {
            try {
                try (Connection con = Database.getConnection()) {
                    Manager.TOP_NV = Manager.readTop(Manager.QUERY_TOP_NV, con);
                    Manager.TOP_SM = Manager.readTop(Manager.QUERY_TOP_SM, con);
                    Manager.TOP_NAP = Manager.readTop(Manager.QUERY_TOP_NAP, con);
                } catch (Exception ignored) {
                    Logger.error("Lỗi đọc top");
                }
                Thread.sleep(1000 * 60 * Manager.TIME_READ_TOP);
            } catch (Exception ignored) {
            }
        }
    }

}

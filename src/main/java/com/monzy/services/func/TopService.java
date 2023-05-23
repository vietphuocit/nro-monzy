package com.monzy.services.func;

import com.database.Database;
import com.monzy.server.Manager;
import com.monzy.utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TopService implements Runnable {

    private static TopService i;

    public static TopService gI() {
        if (i == null) {
            i = new TopService();
        }
        return i;
    }

    public static String getTopNap() {
        StringBuffer sb = new StringBuffer();
        String SELECT_TOP_POWER = "SELECT username, tongnap FROM account ORDER BY tongnap DESC LIMIT 10;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = Database.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_POWER, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
            conn.setAutoCommit(false);
            rs = ps.executeQuery();
            byte i = 1;
            while (rs.next()) {
                sb.append(i).append(".").append(rs.getString("username")).append(": ").append(rs.getString("tongnap")).append("\b");
                i++;
            }
            conn.close();
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (Manager.timeRealTop + (30 * 60 * 1000) < System.currentTimeMillis()) {
                    Manager.timeRealTop = System.currentTimeMillis();
                    try (Connection con = Database.getConnection()) {
                        Manager.topNV = Manager.realTop(Manager.queryTopNV, con);
                        Manager.topSM = Manager.realTop(Manager.queryTopSM, con);
                        Manager.topSK = Manager.realTop(Manager.queryTopSK, con);
                        Manager.topPVP = Manager.realTop(Manager.queryTopPVP, con);
                        Manager.topNHS = Manager.realTop(Manager.queryTopNHS, con);
                        Manager.topNHS = Manager.realTop(Manager.queryTopNAP, con);
                    } catch (Exception ignored) {
                        Logger.error("Lỗi đọc top");
                    }
                }
                Thread.sleep(1000);
            } catch (Exception ignored) {
            }
        }
    }

}

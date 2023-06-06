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
            ps = conn.prepareStatement(SELECT_TOP_POWER, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
                try (Connection con = Database.getConnection()) {
                    System.out.println("Đã làm mới TOP");
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

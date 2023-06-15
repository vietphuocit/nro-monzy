package com.monzy.services;

import com.database.Database;
import com.google.gson.Gson;
import com.monzy.jdbc.daos.PlayerDAO;
import com.monzy.models.player.Player;
import com.monzy.models.transaction.TranHisMsg;
import com.monzy.server.Client;
import com.monzy.server.Manager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class TransactionMomoService implements Runnable {

    private static TransactionMomoService I;

    public TransactionMomoService() {
    }

    public static TransactionMomoService gI() {
        if (TransactionMomoService.I == null) {
            TransactionMomoService.I = new TransactionMomoService();
        }
        return TransactionMomoService.I;
    }

    @Override
    public void run() {
        while (true) {
            Connection con = null;
            PreparedStatement ps = null;
            try {
                con = Database.getConnection();
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("https://api.web2m.com/historyapimomo/2483de084fc82418cd0164-94e6-cf39-8749-06aa22051626")
                        .get()
                        .build();
                Response response = client.newCall(request).execute();
                String jsonString = response.body().string();
                Object obj = JSONValue.parse(jsonString);
                JSONObject jsonObject = (JSONObject) obj;
                Gson gson = new Gson();
                for (Object o : ((JSONArray) ((JSONObject) jsonObject.get("momoMsg")).get("tranList"))) {
                    TranHisMsg tranHisMsg = gson.fromJson(o.toString(), TranHisMsg.class);
                    ps = con.prepareStatement("select * from tran_his where tran_id = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ps.setLong(1, tranHisMsg.tranId);
                    if (tranHisMsg.io == 1 && !ps.executeQuery().next() && tranHisMsg.comment.indexOf(' ') != -1) {
                        String command = tranHisMsg.comment.split(" ")[0];
                        String playerName = tranHisMsg.comment.split(" ")[1];
                        if (command.toLowerCase().equals("mtv")) {
                            Player playerMTV = Client.gI().getPlayer(playerName);
                            if (playerMTV != null && tranHisMsg.amount >= 20000) {
                                playerMTV.getSession().actived = true;
                                ps = con.prepareStatement("update account set active = ? where id = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                                ps.setInt(1, 1);
                                ps.setInt(2, playerMTV.getSession().userId);
                                ps.executeUpdate();
                                Service.gI().sendThongBao(playerMTV, "Đã mở thành viên!");
                                playerMTV.inventory.ruby += 20000;
                                PlayerService.gI().sendInfoHpMpMoney(playerMTV);
                                ps = con.prepareStatement("insert into tran_his(tran_id, comment, amount, command, player_id) values(?,?,?,?,?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                                ps.setLong(1, tranHisMsg.tranId);
                                ps.setString(2, tranHisMsg.comment);
                                ps.setInt(3, tranHisMsg.amount);
                                ps.setString(4, command);
                                ps.setLong(5, playerMTV.id);
                                ps.executeUpdate();
                            }
                        } else if (command.toLowerCase().equals("nap")) {
                            Player playerNap = Client.gI().getPlayer(playerName);
                            int vnd = tranHisMsg.amount;
                            if (playerNap != null && tranHisMsg.amount >= 10000) {
                                ps = con.prepareStatement("update account set vnd = (vnd + ?), active = ? where id = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                                ps.setInt(1, vnd);
                                ps.setInt(2, playerNap.getSession().actived ? 1 : 0);
                                ps.setInt(3, playerNap.getSession().userId);
                                ps.executeUpdate();
                                playerNap.getSession().vnd += vnd;
                                ps = con.prepareStatement("update account set tongnap = (tongnap + ?), active = ? where id = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                                ps.setInt(1, vnd);
                                ps.setInt(2, playerNap.getSession().actived ? 1 : 0);
                                ps.setInt(3, playerNap.getSession().userId);
                                ps.executeUpdate();
                                playerNap.getSession().tongnap += vnd;
                                Service.gI().sendThongBao(playerNap, "Bạn nhận được " + vnd + " vnd. Đến Santa để kiểm tra số dư!");

                                ps = con.prepareStatement("insert into tran_his(tran_id, comment, amount, command, player_id) values(?,?,?,?,?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                                ps.setLong(1, tranHisMsg.tranId);
                                ps.setString(2, tranHisMsg.comment);
                                ps.setInt(3, tranHisMsg.amount);
                                ps.setString(4, command);
                                ps.setLong(5, playerNap.id);
                                ps.executeUpdate();
                            }
                        }
                    }
                }
                if (con != null)
                    con.close();
                if (ps != null)
                    ps.close();
                Thread.sleep(1000 * 60);
            } catch (Exception e) {
                try {
                    if (con != null)
                        con.close();
                    if (ps != null)
                        ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                System.out.println(e.getMessage());
            }
        }
    }

}
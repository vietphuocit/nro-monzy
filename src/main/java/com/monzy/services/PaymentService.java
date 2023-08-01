package com.monzy.services;

import com.database.Database;
import com.monzy.jdbc.daos.PlayerDAO;
import com.monzy.models.item.Item;
import com.monzy.models.payment.TransactionHistory;
import com.monzy.models.player.Player;
import com.monzy.server.Client;
import com.monzy.server.Manager;
import com.monzy.utils.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PaymentService implements Runnable {

  public static PaymentService i;
  private static long lastTimeUpdate;
  private final String TOKEN_MOMO = "737f922623d9dd2dc9f0d7-fdd3-335c-0b14-2cd27c84c0ea";
  private final String TOKEN_MB_BANK = "08CEBDCF-A1EA-F5B5-5F77-F77C46F3C607";

  public static PaymentService gI() {
    if (i == null) {
      return new PaymentService();
    }
    return i;
  }

  @Override
  public void run() {
    while (true) {
      try {
        // Kiểm tra nếu đã trôi qua 30 giây kể từ lần cuối cùng thực hiện
        if (System.currentTimeMillis() - lastTimeUpdate >= 30 * 1000) {
          // Momo
          for (Object o : (JSONArray) getListTransactionHistoryMomo()) {
            processTransactionHistory(convertMomo(o));
          }
          // MBBank
          for (Object o : (JSONArray) getListTransactionHistoryMBBank()) {
            processTransactionHistory(convertMBBank(o));
          }
          // Cập nhật thời gian thực hiện cuối cùng
          lastTimeUpdate = System.currentTimeMillis();
        }
        // Tạm dừng 1 giây trước khi kiểm tra lại
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      } catch (Exception e) {
        Logger.error("\nLỗi nạp tự động: " + e.getMessage());
      }
    }
  }

  public TransactionHistory convertMomo(Object transaction) {
    JSONObject json = (JSONObject) transaction;
    String transactionID = json.get("tranId").toString();
    int amount = Integer.parseInt(json.get("amount").toString());
    String description = json.get("comment").toString();
    String type = (Integer.parseInt(json.get("io").toString()) > 0) ? "IN" : "OUT";
    return new TransactionHistory(transactionID, amount, description, type);
  }

  public TransactionHistory convertMBBank(Object transaction) {
    JSONObject json = (JSONObject) transaction;
    String transactionID = json.get("transactionID").toString();
    int amount = Integer.parseInt(json.get("amount").toString());
    String description = json.get("description").toString();
    String type = json.get("type").toString();
    return new TransactionHistory(transactionID, amount, description, type);
  }

  public Object getListTransactionHistoryMomo() {
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    Request request =
        new Request.Builder()
            .url("https://api.web2m.com/historyapimomo/" + TOKEN_MOMO)
            .get()
            .build();
    try (Response response = client.newCall(request).execute()) {
      String responseString = response.body().string();
      return ((JSONObject) ((JSONObject) JSONValue.parse(responseString)).get("momoMsg"))
          .get("tranList");
    } catch (IOException e) {
      Logger.error("\nLỗi lấy lịch sử giao dịch Momo: " + e.getMessage());
      return null;
    }
  }

  public Object getListTransactionHistoryMBBank() {
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    Request request =
        new Request.Builder()
            .url(
                "https://api.web2m.com/historyapimbv3/Vietphuocit2019@/0583217667/" + TOKEN_MB_BANK)
            .get()
            .build();
    try (Response response = client.newCall(request).execute()) {
      String responseString = response.body().string();
      return ((JSONObject) JSONValue.parse(responseString)).get("transactions");
    } catch (IOException e) {
      Logger.error("\nLỗi lấy lịch sử giao dịch MBBank: " + e.getMessage());
      return null;
    }
  }

  public void processTransactionHistory(TransactionHistory transactionHistory) {
    if (transactionHistory.getType().equals("IN")
        && !isExistTranID(transactionHistory.getTransactionID())) {
      if (transactionHistory.getDescription().contains("mtv")) {
        Player playerMTV =
            Client.gI().getPlayer(extractPlayerName(transactionHistory.getDescription(), "mtv"));
        if (playerMTV != null && transactionHistory.getAmount() >= 20000) {
          playerMTV.getSession().actived = true;
          PlayerDAO.activedUser(playerMTV);
          Service.gI().sendThongBao(playerMTV, "Đã mở thành viên!");
          playerMTV.inventory.ruby += 20000;
          PlayerService.gI().sendInfoHpMpMoney(playerMTV);
          insertTranHis(transactionHistory, "mtv", playerMTV);
        }
      } else if (transactionHistory.getDescription().contains("nap")) {
        Player playerNap =
            Client.gI().getPlayer(extractPlayerName(transactionHistory.getDescription(), "nap"));
        int vnd = transactionHistory.getAmount();
        if (playerNap != null && vnd >= 10000) {
          PlayerDAO.addVND(playerNap, vnd * Manager.RATE_PAY);
          PlayerDAO.addTongNap(playerNap, vnd);
          // event
          //                    playerNap.event += vnd / 1000;
          Item traiDua = new Item((short) 694);
          traiDua.quantity = vnd / 1000;
          InventoryService.gI().addItemBag(playerNap, traiDua);
          Item veTangNgoc = new Item((short) 718);
          veTangNgoc.quantity = vnd / 100000;
          InventoryService.gI().addItemBag(playerNap, veTangNgoc);
          InventoryService.gI().sendItemBags(playerNap);
          Service.gI()
              .sendThongBao(
                  playerNap, "Bạn nhận được " + vnd + " vnd. Đến Santa để kiểm tra số dư!");
          insertTranHis(transactionHistory, "nap", playerNap);
        }
      }
    }
  }

  public boolean isExistTranID(String tranId) {
    try (Connection con = Database.getConnection()) {
      PreparedStatement ps =
          con.prepareStatement(
              "select * from tran_his where tran_id = ?",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      ps.setString(1, tranId);
      return ps.executeQuery().next();
    } catch (Exception e) {
      Logger.error("\nLỗi kiểm tra transaction id: " + e.getMessage());
      return false;
    }
  }

  public void insertTranHis(TransactionHistory transactionHistory, String command, Player player) {
    try (Connection con = Database.getConnection()) {
      PreparedStatement ps =
          con.prepareStatement(
              "insert into tran_his(tran_id, description, amount, command, player_id) values(?,?,?,?,?)",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      ps.setString(1, transactionHistory.getTransactionID());
      ps.setString(2, transactionHistory.getDescription());
      ps.setInt(3, transactionHistory.getAmount());
      ps.setString(4, command);
      ps.setLong(5, player.id);
      ps.executeUpdate();
      ps.close();
    } catch (Exception e) {
      Logger.error("\nLỗi thêm lịch sử giao dịch: " + e.getMessage());
    }
  }

  private String extractPlayerName(String description, String keyword) {
    int keywordIndex = description.indexOf(keyword);
    return description
        .toLowerCase()
        .replaceAll("[!@#$%^&*(){}\\[\\]|;:\"'<,>.?/]+", " ")
        .substring(keywordIndex)
        .split(" ")[1];
  }
}

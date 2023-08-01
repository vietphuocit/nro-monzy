package com.monzy.services;

import com.monzy.jdbc.daos.PlayerDAO;
import com.monzy.models.player.Player;
import com.monzy.utils.Logger;
import com.monzy.utils.Util;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import okhttp3.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class NapThe {

  private static NapThe I;

  public static NapThe gI() {
    if (NapThe.I == null) {
      NapThe.I = new NapThe();
    }
    return NapThe.I;
  }

  public static void callbackAPI() {
    try {
      HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);
      server.createContext("/callback", new InfoHandler());
      server.setExecutor(null);
      server.start();
      System.out.println("Server started on port 8080");
    } catch (Exception e) {
      Logger.error("callbackAPI: " + e.getMessage() + '\n');
    }
  }

  public static final void SendCard(
      Player player, String telco, String amount, String serial, String code) {
    String partnerId = "1079293661";
    String partnerKey = "bc6a0cf61ea9028d34b10ca2b1db7f1e";
    String api = MD5Hash(partnerKey + code + serial);
    String requestID = String.valueOf(System.currentTimeMillis() + Util.nextInt(1000, 9999));
    try {
      OkHttpClient client = new OkHttpClient().newBuilder().build();
      RequestBody body =
          new MultipartBody.Builder()
              .setType(MultipartBody.FORM)
              .addFormDataPart("telco", telco)
              .addFormDataPart("code", code)
              .addFormDataPart("serial", serial)
              .addFormDataPart("amount", amount)
              .addFormDataPart("request_id", requestID)
              .addFormDataPart("partner_id", partnerId)
              .addFormDataPart("sign", api)
              .addFormDataPart("command", "charging")
              .build();
      Request request =
          new Request.Builder()
              .url("https://thesieure.com/chargingws/v2")
              .post(body)
              .addHeader("Content-Type", "application/json")
              .build();
      Response response = client.newCall(request).execute();
      String jsonString = response.body().string();
      Object obj = JSONValue.parse(jsonString);
      JSONObject jsonObject = (JSONObject) obj;
      long name = (long) jsonObject.get("status");
      //
      if (name == 99) {
        PlayerDAO.LogNapTien(player.getSession().uu, amount, serial, code, requestID);
        Service.gI()
            .sendThongBaoOK(
                player,
                "Gửi thẻ thành công \n"
                    + "Seri :"
                    + serial
                    + "\n Mã thẻ :"
                    + code
                    + "\n Mệnh giá : "
                    + amount
                    + "\n"
                    + "Thời gian : "
                    + java.time.LocalDate.now()
                    + " "
                    + java.time.LocalTime.now()
                    + "\n"
                    + "Vui lòng thoát game để update lại số tiền");
      }
      if (name == 1) {
        PlayerDAO.LogNapTien(player.getSession().uu, amount, serial, code, requestID);
        Service.gI()
            .sendThongBaoOK(
                player,
                "Gửi thẻ thành công \n"
                    + "Seri :"
                    + serial
                    + "\n Mã thẻ :"
                    + code
                    + "\n Mệnh giá : "
                    + amount
                    + "\n"
                    + "Thời gian : "
                    + java.time.LocalDate.now()
                    + " "
                    + java.time.LocalTime.now()
                    + "\n"
                    + "Vui lòng thoát game để update lại số tiền");
      } else if (name == 2) {
        Service.gI()
            .sendThongBao(
                player,
                "Nạp thành công nhưng sai mệnh giá.\nCon sẽ ko dc cộng tiền lần sau ông khóa mẹ acc con cho chừa nhé");
      } else if (name == 3) {
        Service.gI().sendThongBao(player, "Bạn đã nhập sai giá trị, hãy nhập đúng nhóe :3");
      } else if (name == 4) {
        Service.gI().sendThongBao(player, "Hệ thống nạp bảo trì rồi con");
      } else if (name == 100) {
        Service.gI().sendThongBao(player, "Sai seri và mã pin rồi con ơi");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String MD5Hash(String input) {
    try {
      MessageDigest md = java.security.MessageDigest.getInstance("MD5");
      byte[] array = md.digest(input.getBytes());
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < array.length; ++i) {
        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100), 1, 3);
      }
      return sb.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  static class InfoHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
      if ("POST".equalsIgnoreCase(httpExchange.getRequestMethod())) {
        InputStreamReader isr =
            new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
          requestBody.append(line);
        }
        br.close();
        isr.close();
        String response = requestBody.toString();
        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
      }
    }
  }
}

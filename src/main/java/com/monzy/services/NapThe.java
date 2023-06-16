package com.monzy.services;

import com.google.gson.Gson;
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
            HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0",8080), 0);
            server.createContext("/info", new InfoHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("Server started on port 8080");
        } catch (Exception e) {
            Logger.error("callbackAPI: " + e.getMessage() + '\n');
        }
    }

    static class InfoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            if ("POST".equalsIgnoreCase(httpExchange.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
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
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    public static final void SendCard(Player p, String loaiThe, String menhGia, String soSeri, String maPin) {
        String partnerId = "72461046463"; //0086879143
        String partnerKey = "16502d49bf5e949c3f27238c2a762115"; //edc3a8086e2db06925438495b0cf88df
        String api = MD5Hash(partnerKey + maPin + soSeri);
        int requestID = Util.nextInt(100000000, 999999999);
        String t = String.valueOf(requestID);
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("telco", loaiThe)
                    .addFormDataPart("code", maPin)
                    .addFormDataPart("serial", soSeri)
                    .addFormDataPart("amount", menhGia)
                    .addFormDataPart("request_id", t)
                    .addFormDataPart("partner_id", partnerId)
                    .addFormDataPart("sign", api)
                    .addFormDataPart("command", "charging")
                    .build();
            Request request = new Request.Builder()
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
                PlayerDAO.LogNapTIen(p.getSession().uu, menhGia, soSeri, maPin, t);
                Service.gI().sendThongBaoOK(p, "Gửi thẻ thành công \n"
                        + "Seri :" + soSeri + "\n Mã thẻ :" + maPin + "\n Mệnh giá : " + menhGia + "\n"
                        + "Thời gia : " + java.time.LocalDate.now() + " " + java.time.LocalTime.now() + "\n"
                        + "Vui lòng thoát game để update lại số tiền");
            }
            if (name == 1) {
                PlayerDAO.LogNapTIen(p.getSession().uu, menhGia, soSeri, maPin, t);
                Service.gI().sendThongBaoOK(p, "Gửi thẻ thành công \n"
                        + "Seri :" + soSeri + "\n Mã thẻ :" + maPin + "\n Mệnh giá : " + menhGia + "\n"
                        + "Thời gia : " + java.time.LocalDate.now() + " " + java.time.LocalTime.now() + "\n"
                        + "Vui lòng thoát game để update lại số tiền");
            } else if (name == 2) {
                Service.gI().sendThongBao(p, "nạp thành công nhưng sai mệnh giá.con sẽ ko dc cộng tiền \n lần sau ông khóa mẹ acc con cho chừa nhé");
            } else if (name == 3) {
                Service.gI().sendThongBao(p, "Bạn đã nhập sai giá trị, hãy nhập đúng nhóe :3");
            } else if (name == 4) {
                Service.gI().sendThongBao(p, "Hệ thống nạp bảo trì rồi con");
            } else if (name == 100) {
                Service.gI().sendThongBao(p, "Sai seri và mã ping ồi con ơi");
            }
            System.out.println(name + "\n" + menhGia + soSeri + "\n" + maPin);
        } catch (Exception e) {
            e.printStackTrace();
            //Logger.error("lỗi ở nạp thẻ mất ồi");
        }
    }

    public static String MD5Hash(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
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

}






















package com.monzy.services;

import com.monzy.utils.Logger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

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
      Logger.logException(NapThe.class, e);
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

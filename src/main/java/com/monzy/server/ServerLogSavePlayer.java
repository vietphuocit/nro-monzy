package com.monzy.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerLogSavePlayer implements Runnable {

  private static ServerLogSavePlayer i;
  private final List<String> list;
  private BufferedWriter bw;

  private ServerLogSavePlayer() {
    this.list = new ArrayList<>();
    try {
      this.bw =
          new BufferedWriter(
              new FileWriter("log/log_save_player" + System.currentTimeMillis() + ".sql", true));
      new Thread(this, "Log file save player").start();
    } catch (IOException e) {
      Logger.getLogger(ServerLogSavePlayer.class.getName()).log(Level.SEVERE, null, e);
    }
  }

  public static ServerLogSavePlayer gI() {
    if (i == null) {
      i = new ServerLogSavePlayer();
    }
    return i;
  }

  @Override
  public void run() {
    while (true) {
      while (!this.list.isEmpty()) {
        String text = this.list.remove(0);
        try {
          bw.write(text.substring(text.indexOf(":") + 2) + "\n");
          bw.flush();
        } catch (IOException e) {
          Logger.getLogger(ServerLogSavePlayer.class.getName()).log(Level.SEVERE, null, e);
        }
      }
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        // Bỏ qua
      }
    }
  }

  public void add(String text) {
    list.add(text);
  }
}

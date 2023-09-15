package com.monzy.server;

import com.monzy.services.Service;
import com.monzy.utils.Logger;

public class Maintenance extends Thread {

  public static boolean isRunning = false;
  private static Maintenance i;
  private int seconds;

  private Maintenance() {}

  public static Maintenance gI() {
    if (i == null) {
      i = new Maintenance();
    }
    return i;
  }

  public void start(int seconds) {
    if (!isRunning) {
      isRunning = true;
      this.seconds = seconds;
      this.start();
    }
  }

  @Override
  public void run() {
    while (this.seconds > 0) {
      this.seconds--;
      Service.gI()
          .sendThongBaoAllPlayer(
              "Hệ thống sẽ bảo trì sau "
                  + seconds
                  + " giây nữa, vui lòng thoát game để tránh mất vật phẩm");
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        // Bỏ qua
      }
    }
    Logger.error("BEGIN MAINTENANCE...............................\n");
    ServerManager.gI().close();
  }
}

package com.monzy.models.matches;

import com.monzy.models.map.nguhanhson.nguhs;
import com.monzy.server.ServerManager;
import com.monzy.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class GiaiNgoaiHang implements Runnable {

  private static GiaiNgoaiHang I;
  private final List<Long> subscribers;

  public GiaiNgoaiHang() {
    this.subscribers = new ArrayList<>();
  }

  public static GiaiNgoaiHang gI() {
    if (GiaiNgoaiHang.I == null) {
      GiaiNgoaiHang.I = new GiaiNgoaiHang();
      new Thread(GiaiNgoaiHang.I, "Update giải đấu ngoại hạng").start();
    }
    return GiaiNgoaiHang.I;
  }

  @Override
  public void run() {
    this.update();
  }

  private void update() {
    while (ServerManager.isRunning) {
      try {
        Thread.sleep(1000);
      } catch (Exception e) {
        Logger.logException(GiaiNgoaiHang.class, e);
      }
    }
  }
}

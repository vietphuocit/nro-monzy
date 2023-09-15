package com.monzy.server;

import com.monzy.models.player.Player;
import com.monzy.services.Service;
import com.monzy.utils.Logger;
import com.network.io.Message;

import java.util.ArrayList;
import java.util.List;

public class ServerNotify extends Thread {

  private static ServerNotify i;
  private final byte[] gk =
      new byte[] {
        67, 104, -61, -96, 111, 32, 109, -31, -69, -85, 110, 103, 32, 98, -31, -70, -95, 110, 32,
        -60, -111, -61, -93, 32, 116, -31, -69, -101, 105, 32, 118, -31, -69, -101, 105, 32, 109,
        -61, -95, 121, 32, 99, 104, -31, -69, -89, 32, 71, 105, 114, 108, 107, 117, 110, 55, 53, 46,
        32, 67, 104, -61, -70, 99, 32, 99, -61, -95, 99, 32, 98, -31, -70, -95, 110, 32, 99, 104,
        -58, -95, 105, 32, 103, 97, 109, 101, 32, 118, 117, 105, 32, 118, -31, -70, -69, 46, 46
      };
  private final List<String> notifies;
  private long lastTimeGK;

  private ServerNotify() {
    this.notifies = new ArrayList<>();
    this.start();
  }

  public static ServerNotify gI() {
    if (i == null) {
      i = new ServerNotify();
    }
    return i;
  }

  @Override
  public void run() {
    while (!Maintenance.isRunning) {
      try {
        while (!notifies.isEmpty()) {
          sendThongBaoBenDuoi(notifies.remove(0));
        }
        //                if (Util.canDoWithTime(this.lastTimeGK, 12000)) {
        //                    sendThongBaoBenDuoi("Nro MONZY!");
        //                    this.lastTimeGK = System.currentTimeMillis();
        //                }
      } catch (Exception e) {
        Logger.logException(ServerNotify.class, e);
      }
      try {
        Thread.sleep(1000);
      } catch (Exception e) {
        // Bỏ qua
      }
    }
  }

  public void sendThongBaoBenDuoi(String text) {
    Message msg;
    try {
      msg = new Message(93);
      msg.writer().writeUTF(text);
      Service.gI().sendMessAllPlayer(msg);
      msg.cleanup();
    } catch (Exception e) {
      Logger.logException(ServerNotify.class, e);
    }
  }

  public void notify(String text) {
    this.notifies.add(text);
  }

  public void sendNotifyTab(Player player) {
    Message msg;
    try {
      msg = new Message(50);
      msg.writer().writeByte(10);
      for (int i = 0; i < Manager.NOTIFY.size(); i++) {
        String[] arr = Manager.NOTIFY.get(0).split("<>");
        msg.writer().writeShort(i);
        msg.writer().writeUTF(arr[0]);
        msg.writer().writeUTF(arr[1]);
      }
      player.sendMessage(msg);
      msg.cleanup();
    } catch (Exception e) {
      Logger.logException(ServerNotify.class, e);
    }
  }
}

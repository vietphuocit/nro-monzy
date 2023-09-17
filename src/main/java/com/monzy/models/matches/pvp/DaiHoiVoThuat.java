package com.monzy.models.matches.pvp;

import com.monzy.models.player.Player;
import com.monzy.server.Manager;
import com.monzy.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

public class DaiHoiVoThuat implements Runnable {

  private static DaiHoiVoThuat instance;
  public ArrayList<Player> listReg = new ArrayList<>();
  public ArrayList<Player> listPlayerWait = new ArrayList<>();
  public String cup;
  public String[] time;
  public int gem;
  public int gold;
  public int min_start;
  public int min_start_temp;
  public int min_limit;
  public int round = 1;
  public int hour;
  public int minute;
  public int second;

  public static DaiHoiVoThuat gI() {
    if (instance == null) {
      instance = new DaiHoiVoThuat();
    }
    return instance;
  }

  public DaiHoiVoThuat getDaiHoiNow() {
    for (DaiHoiVoThuat daiHoiVoThuat : Manager.LIST_DHVT) {
      if (daiHoiVoThuat != null && Util.contains(daiHoiVoThuat.time, String.valueOf(hour))) {
        return daiHoiVoThuat;
      }
    }
    return null;
  }

  public String info() {
    if (Manager.LIST_DHVT.isEmpty()) {
      return "Không có giải đấu nào được tổ chức\b";
    }
    StringBuilder lichThiDau = new StringBuilder("Lịch thi đấu trong ngày\b");
    StringBuilder lePhiThiDau = new StringBuilder("Lệ phí đăng ký thi đấu\b");
    for (DaiHoiVoThuat daiHoiVoThuat : Manager.LIST_DHVT) {
      lichThiDau.append("Giải ").append(daiHoiVoThuat.cup).append(": ").append(Arrays.toString(daiHoiVoThuat.time).replace("[", "").replace("]", "")).append("h\b");
      if (daiHoiVoThuat.gold > 0) {
        lePhiThiDau.append("Giải ").append(daiHoiVoThuat.cup).append(": ").append(Util.powerToString(daiHoiVoThuat.gold)).append(" vàng\b");
      } else if (daiHoiVoThuat.gem > 0) {
        lePhiThiDau.append("Giải ").append(daiHoiVoThuat.cup).append(": ").append(Util.powerToString(daiHoiVoThuat.gem)).append(" ngọc\b");
      }
    }
    return lichThiDau + "\n" + lePhiThiDau;
  }

  @Override
  public void run() {
    while (true) {
      Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
      try {
        second = calendar.get(Calendar.SECOND);
        minute = calendar.get(Calendar.MINUTE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        DaiHoiVoThuatService.gI(getDaiHoiNow()).update();
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        // Bỏ qua
      }
    }
  }
}

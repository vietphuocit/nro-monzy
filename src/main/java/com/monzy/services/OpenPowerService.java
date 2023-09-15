package com.monzy.services;

import com.monzy.models.player.NPoint;
import com.monzy.models.player.Player;

public class OpenPowerService {

  public static final int COST_SPEED_OPEN_LIMIT_POWER = 500000000;
  private static OpenPowerService i;

  private OpenPowerService() {}

  public static OpenPowerService gI() {
    if (i == null) {
      i = new OpenPowerService();
    }
    return i;
  }

  public boolean openPowerBasic(Player player) {
    if (player.nPoint.limitPower >= NPoint.MAX_LIMIT) {
      Service.gI().sendThongBao(player, "Sức mạnh của bạn đã đạt tới mức tối đa");
      return false;
    }
    if (player.itemTime.isOpenPower || !player.nPoint.canOpenPower()) {
      Service.gI().sendThongBao(player, "Sức mạnh của bạn không đủ để thực hiện");
      return false;
    }
    player.itemTime.isOpenPower = true;
    player.itemTime.lastTimeOpenPower = System.currentTimeMillis();
    ItemTimeService.gI().sendAllItemTime(player);
    return true;
  }

  public boolean openPowerSpeed(Player player) {
    String msg = player.isPet ? "đệ tử" : "bạn";
    if (player.nPoint.limitPower >= NPoint.MAX_LIMIT) {
      Service.gI().sendThongBao(player, "Sức mạnh của " + msg + " đã đạt tới mức tối đa");
      return false;
    }
    if (!player.nPoint.canOpenPower()) {
      Service.gI().sendThongBao(player, "Sức mạnh của " + msg + " không đủ để thực hiện");
      return false;
    }
    player.nPoint.limitPower++;
    player.itemTime.isOpenPower = false;
    Service.gI().sendThongBao(player, "Giới hạn sức mạnh của " + msg + " đã được tăng lên 1 bậc");
    return true;
  }
}

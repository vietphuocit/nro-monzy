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
    if (!player.itemTime.isOpenPower && player.nPoint.canOpenPower()) {
      player.itemTime.isOpenPower = true;
      player.itemTime.lastTimeOpenPower = System.currentTimeMillis();
      ItemTimeService.gI().sendAllItemTime(player);
      return true;
    } else {
      Service.gI().sendThongBao(player, "Sức mạnh của bạn không đủ để thực hiện");
      return false;
    }
  }

  public boolean openPowerSpeed(Player player) {
    if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
      player.nPoint.limitPower++;
      String message =
          player.isPet
              ? "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc"
              : "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc";
      Service.gI().sendThongBao(player, message);
      return true;
    } else {
      String message =
          player.isPet
              ? "Sức mạnh của đệ tử đã đạt tới mức tối đa"
              : "Sức mạnh của bạn đã đạt tới mức tối đa";
      Service.gI().sendThongBao(player, message);
      return false;
    }
  }
}

package com.monzy.services;

import com.monzy.models.map.ItemMap;
import com.monzy.models.player.Player;
import com.monzy.utils.Logger;
import com.monzy.utils.Util;
import com.network.io.Message;

public class ItemMapService {

  private static ItemMapService i;

  public static ItemMapService gI() {
    if (i == null) {
      i = new ItemMapService();
    }
    return i;
  }

  public void pickItem(Player player, int itemMapId, boolean isThuHut) {
    if (isThuHut || Util.canDoWithTime(player.iDMark.getLastTimePickItem(), 1000)) {
      player.zone.pickItem(player, itemMapId);
      player.iDMark.setLastTimePickItem(System.currentTimeMillis());
    }
  }

  // xóa item map và gửi item map biến mất
  public void removeItemMapAndSendClient(ItemMap itemMap) {
    sendItemMapDisappear(itemMap);
    removeItemMap(itemMap);
  }

  public void sendItemMapDisappear(ItemMap itemMap) {
    Message msg;
    try {
      msg = new Message(-21);
      msg.writer().writeShort(itemMap.itemMapId);
      Service.gI().sendMessAllPlayerInMap(itemMap.zone, msg);
      msg.cleanup();
    } catch (Exception e) {
      Logger.logException(ItemMapService.class, e);
    }
  }

  public void removeItemMap(ItemMap itemMap) {
    itemMap.zone.removeItemMap(itemMap);
    itemMap.dispose();
  }

  public boolean isBlackBall(int tempId) {
    return tempId >= 372 && tempId <= 378;
  }

  public boolean isNamecBall(int tempId) {
    return tempId >= 353 && tempId <= 360;
  }
}

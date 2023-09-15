package com.monzy.services;

import com.monzy.models.map.Zone;
import com.monzy.models.player.Player;
import com.monzy.utils.Logger;
import com.network.io.Message;

public class EffectMapService {

  private static EffectMapService i;

  private EffectMapService() {}

  public static EffectMapService gI() {
    if (i == null) {
      i = new EffectMapService();
    }
    return i;
  }

  public void sendEffectMapToPlayer(
      Player player, int id, int layer, int loop, int x, int y, int delay) {
    Message msg;
    try {
      msg = new Message(113);
      msg.writer().writeByte(id);
      msg.writer().writeByte(layer);
      msg.writer().writeByte(id);
      msg.writer().writeShort(x);
      msg.writer().writeShort(y);
      msg.writer().writeShort(delay);
      player.sendMessage(msg);
      msg.cleanup();
    } catch (Exception e) {
      Logger.logException(EffectMapService.class, e);
    }
  }

  public void sendEffectMapToAllInMap(
      Zone zone, int id, int layer, int loop, int x, int y, int delay) {
    Message msg;
    try {
      msg = new Message(113);
      msg.writer().writeByte(loop);
      msg.writer().writeByte(layer);
      msg.writer().writeByte(id);
      msg.writer().writeShort(x);
      msg.writer().writeShort(y);
      msg.writer().writeShort(delay);
      Service.gI().sendMessAllPlayerInMap(zone, msg);
      msg.cleanup();
    } catch (Exception e) {
      Logger.logException(EffectMapService.class, e);
    }
  }
}

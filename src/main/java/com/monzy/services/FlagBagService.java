package com.monzy.services;

import com.monzy.models.Template.FlagBag;
import com.monzy.models.player.Player;
import com.monzy.server.Manager;
import com.monzy.utils.Logger;
import com.network.io.Message;

import java.util.ArrayList;
import java.util.List;

public class FlagBagService {

  private static FlagBagService i;
  private final List<FlagBag> flagClan = new ArrayList<>();

  public static FlagBagService gI() {
    if (i == null) {
      i = new FlagBagService();
    }
    return i;
  }

  public void sendIconFlagChoose(Player player, int id) {
    FlagBag fb = getFlagBag(id);
    if (fb != null) {
      Message msg;
      try {
        msg = new Message(-62);
        msg.writer().writeByte(fb.id);
        msg.writer().writeByte(1);
        msg.writer().writeShort(fb.iconId);
        player.sendMessage(msg);
        msg.cleanup();
      } catch (Exception e) {
        Logger.logException(FlagBagService.class, e);
      }
    }
  }

  public void sendIconEffectFlag(Player player, int id) {
    FlagBag fb = getFlagBag(id);
    if (fb != null) {
      Message msg;
      try {
        msg = new Message(-63);
        msg.writer().writeByte(fb.id);
        msg.writer().writeByte(fb.iconEffect.length);
        for (Short iconId : fb.iconEffect) {
          msg.writer().writeShort(iconId);
        }
        player.sendMessage(msg);
        msg.cleanup();
      } catch (Exception e) {
        Logger.logException(FlagBagService.class, e);
      }
    }
  }

  public void sendListFlagClan(Player pl) {
    List<FlagBag> list = getFlagsForChooseClan();
    Message msg;
    try {
      msg = new Message(-46);
      msg.writer().writeByte(1); // type
      msg.writer().writeByte(list.size());
      for (FlagBag fb : list) {
        msg.writer().writeByte(fb.id);
        msg.writer().writeUTF(fb.name);
        msg.writer().writeInt(fb.gold);
        msg.writer().writeInt(fb.gem);
      }
      pl.sendMessage(msg);
      msg.cleanup();
    } catch (Exception e) {
      Logger.logException(FlagBagService.class, e);
    }
  }

  public FlagBag getFlagBag(int id) {
    for (FlagBag fb : Manager.FLAGS_BAGS) {
      if (fb.id == id) {
        return fb;
      }
    }
    return null;
  }

  public List<FlagBag> getFlagsForChooseClan() {
    if (flagClan.isEmpty()) {
      int[] flagsId = {
        0, 8, 7, 6, 5, 4, 3, 2, 1, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 27, 26, 25, 24, 23, 36,
        32, 33, 34, 35, 19, 22, 21, 20, 29, 37, 38, 69, 70, 71, 77, 78, 79
        //                    ,56,57,58,59,60,61,62,63,64,65,66,67,68
      };
      for (int i = 0; i < flagsId.length; i++) {
        flagClan.add(getFlagBag(flagsId[i]));
      }
    }
    return flagClan;
  }
}

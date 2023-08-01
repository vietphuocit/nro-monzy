/*
 */
package com.monzy.services.func;

import com.monzy.models.item.Item;
import com.monzy.models.player.Player;
import com.monzy.services.ChatGlobalService;
import com.monzy.services.InventoryService;
import com.monzy.services.ItemService;
import com.monzy.services.Service;
import com.monzy.utils.Util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChonAiDay implements Runnable {

  private static ChonAiDay instance;
  public int goldNormar;
  public int goldVip;
  public long lastTimeEnd;
  public List<Player> PlayersNormar = new ArrayList<>();
  public List<Player> PlayersVIP = new ArrayList<>();

  public static ChonAiDay gI() {
    if (instance == null) {
      instance = new ChonAiDay();
    }
    return instance;
  }

  public void addPlayerVIP(Player pl) {
    if (!PlayersVIP.equals(pl)) {
      PlayersVIP.add(pl);
    }
  }

  public void addPlayerNormar(Player pl) {
    if (!PlayersNormar.equals(pl)) {
      PlayersNormar.add(pl);
    }
  }

  public void removePlayerVIP(Player pl) {
    if (PlayersVIP.equals(pl)) {
      PlayersVIP.remove(pl);
    }
  }

  public void removePlayerNormar(Player pl) {
    if (PlayersNormar.equals(pl)) {
      PlayersNormar.remove(pl);
    }
  }

  @Override
  public void run() {
    while (true) {
      try {
        if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) <= 0) {
          List<Player> listN = new ArrayList<>();
          ChonAiDay.gI().PlayersNormar.stream()
              .filter(p -> p != null && p.goldNormar != 0)
              .sorted(
                  Comparator.comparing(
                      p -> Math.ceil(((double) p.goldNormar / ChonAiDay.gI().goldNormar) * 100),
                      Comparator.reverseOrder()))
              .forEach(cl -> listN.add(cl));
          if (listN.size() > 5) {
            Player pl = listN.get(Util.nextInt(0, 5));
            if (pl != null) {
              ChatGlobalService.gI().chat(pl, pl.name + " đã chiến thắng Chọn ai đây giải thường");
              int goldC = ChonAiDay.gI().goldNormar * 80 / 100;
              Service.gI()
                  .sendThongBao(
                      pl, "Chúc mừng bạn đã dành chiến thắng và nhận được " + goldC + " thỏi vàng");
              Item it = ItemService.gI().createNewItem((short) 457, goldC);
              InventoryService.gI().addItemBag(pl, it);
              InventoryService.gI().sendItemBags(pl);
            }
          } else if (!listN.isEmpty()) {
            Player pl = listN.get(Util.nextInt(0, listN.size() - 1));
            if (pl != null) {
              ChatGlobalService.gI().chat(pl, pl.name + " đã chiến thắng Chọn ai đây giải thường");
              int goldC = ChonAiDay.gI().goldNormar * 80 / 100;
              Service.gI()
                  .sendThongBao(
                      pl, "Chúc mừng bạn đã dành chiến thắng và nhận được " + goldC + " thỏi vàng");
              Item it = ItemService.gI().createNewItem((short) 457, goldC);
              InventoryService.gI().addItemBag(pl, it);
              InventoryService.gI().sendItemBags(pl);
            }
          }
          listN.clear();
          ChonAiDay.gI().PlayersVIP.stream()
              .filter(p -> p != null && p.goldVIP != 0)
              .sorted(
                  Comparator.comparing(
                      p -> Math.ceil(((double) p.goldVIP / ChonAiDay.gI().goldVip) * 100),
                      Comparator.reverseOrder()))
              .forEach(cl -> listN.add(cl));
          if (listN.size() > 5) {
            Player pl = listN.get(Util.nextInt(0, 5));
            if (pl != null) {
              ChatGlobalService.gI().chat(pl, pl.name + " đã chiến thắng Chọn ai đây giải VIP");
              int goldC = ChonAiDay.gI().goldVip * 90 / 100;
              Service.gI()
                  .sendThongBao(
                      pl, "Chúc mừng bạn đã dành chiến thắng và nhận được " + goldC + " thỏi vàng");
              Item it = ItemService.gI().createNewItem((short) 457, goldC);
              InventoryService.gI().addItemBag(pl, it);
              InventoryService.gI().sendItemBags(pl);
            }
          } else if (!listN.isEmpty()) {
            Player pl = listN.get(Util.nextInt(0, listN.size() - 1));
            if (pl != null) {
              ChatGlobalService.gI().chat(pl, pl.name + " đã chiến thắng Chọn ai đây giải VIP");
              int goldC = ChonAiDay.gI().goldVip * 90 / 100;
              Service.gI()
                  .sendThongBao(
                      pl, "Chúc mừng bạn đã dành chiến thắng và nhận được " + goldC + " thỏi vàng");
              Item it = ItemService.gI().createNewItem((short) 457, goldC);
              InventoryService.gI().addItemBag(pl, it);
              InventoryService.gI().sendItemBags(pl);
            }
          }
          for (int i = 0; i < ChonAiDay.gI().PlayersNormar.size(); i++) {
            Player pl = ChonAiDay.gI().PlayersNormar.get(i);
            if (pl != null) {
              pl.goldVIP = 0;
              pl.goldNormar = 0;
            }
          }
          for (int i = 0; i < ChonAiDay.gI().PlayersVIP.size(); i++) {
            Player pl = ChonAiDay.gI().PlayersVIP.get(i);
            if (pl != null) {
              pl.goldVIP = 0;
              pl.goldNormar = 0;
            }
          }
          ChonAiDay.gI().goldNormar = 0;
          ChonAiDay.gI().goldVip = 0;
          ChonAiDay.gI().PlayersNormar.clear();
          ChonAiDay.gI().PlayersVIP.clear();
          ChonAiDay.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
        }
        Thread.sleep(1000);
      } catch (Exception e) {
      }
    }
  }
}

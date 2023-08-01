package com.monzy.services;

import com.monzy.models.item.Item;
import com.monzy.models.map.bdkb.BanDoKhoBau;
import com.monzy.models.player.Player;

import java.util.ArrayList;
import java.util.List;

public class BanDoKhoBauService {

  private static BanDoKhoBauService i;
  public static final List<BanDoKhoBau> LIST_BAN_DO_KHO_BAU;
  public static final long POWER_CAN_GO_TO_DBKB = 0;
  public static final int MAX_AVAILABLE = 10;
  public static final int TIME_BAN_DO_KHO_BAU = 1800000;

  private BanDoKhoBauService() {}

  static {
    LIST_BAN_DO_KHO_BAU = new ArrayList<>();
    for (int i = 0; i < MAX_AVAILABLE; i++) {
      LIST_BAN_DO_KHO_BAU.add(new BanDoKhoBau(i));
    }
  }

  public static BanDoKhoBauService gI() {
    if (i == null) {
      i = new BanDoKhoBauService();
    }
    return i;
  }

  public void openBanDoKhoBau(Player player, byte level) {
    if (level < 1 || level > 110) {
      Service.gI().sendThongBao(player, "Hãy chọn cấp độ từ 1 đến 110");
      return;
    }
    if (player.clan == null) {
      Service.gI().sendThongBao(player, "Hãy vào bang hội");
      return;
    }
    if (player.clan.banDoKhoBau != null) {
      Service.gI().sendThongBao(player, "Có thành viên đã mở bản đồ kho báu");
      return;
    }
    Item item = InventoryService.gI().findItemBag(player, 611);
    if (item == null || item.quantity < 1) {
      Service.gI().sendThongBao(player, "Yêu cầu có bản đồ kho báu");
      return;
    }
    BanDoKhoBau banDoKhoBau = null;
    for (BanDoKhoBau bdkb : LIST_BAN_DO_KHO_BAU) {
      if (!bdkb.isOpened) {
        banDoKhoBau = bdkb;
        break;
      }
    }
    if (banDoKhoBau != null) {
      InventoryService.gI().subQuantityItemsBag(player, item, 1);
      InventoryService.gI().sendItemBags(player);
      banDoKhoBau.openBanDoKhoBau(player, player.clan, level);
      //            try {
      //              long bossDamage = (20 * level);
      //              long bossMaxHealth = (2 * level);
      //              bossDamage = Math.min(bossDamage, 200000000L);
      //              bossMaxHealth = Math.min(bossMaxHealth, 2000000000L);
      //              TrungUyXanhLo boss =
      //                  new TrungUyXanhLo(
      //                      player.clan.BanDoKhoBau.getMapById(137),
      //                      player.clan.BanDoKhoBau.level,
      //                      (int) bossDamage,
      //                      (int) bossMaxHealth);
      //            } catch (Exception exception) {
      //              Logger.logException(BanDoKhoBauService.class, exception, "Error
      // initializing boss");
      //            }
    } else {
      Service.gI().sendThongBao(player, "Bản đồ kho báu đã đầy, vui lòng quay lại sau");
    }
  }
}

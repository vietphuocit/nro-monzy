package com.monzy.services;

import com.monzy.giftcode.GiftCode;
import com.monzy.giftcode.GiftCodeManager;
import com.monzy.models.player.Player;

public class GiftService implements Runnable {

  private static GiftService i;
  private static long lastTimeUpdate;

  public GiftService() {}

  public static GiftService gI() {
    if (i == null) {
      i = new GiftService();
    }
    return i;
  }

  public void giftCode(Player player, String code) {
    GiftCode giftcode = GiftCodeManager.gI().checkUseGiftCode((int) player.id, code);
    // if(!Maintenance.gI().canUseCode){Service.gI().sendThongBao(player, "Không thể thực hiện lúc
    // này ");return;}
    if (giftcode == null) {
      Service.gI().sendThongBao(player, "Code đã được sử dụng, hoặc không tồn tại!");
    } else if (giftcode.timeCode()) {
      Service.gI().sendThongBao(player, "Code đã hết hạn");
    } else {
      InventoryService.gI().addItemGiftCodeToPlayer(player, giftcode);
    }
  }

  @Override
  public void run() {
    while (true) {
      // Kiểm tra nếu đã trôi qua 1 phút kể từ lần cuối cùng thực hiện
      if (System.currentTimeMillis() - lastTimeUpdate >= 60 * 1000) {
        // Thực hiện đoạn mã ở đây
        GiftCodeManager.gI().saveGiftCode();
        GiftCodeManager.gI().init();
        // Cập nhật thời gian thực hiện cuối cùng
        lastTimeUpdate = System.currentTimeMillis();
      }
      // Tạm dừng 1 giây trước khi kiểm tra lại
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        // Bỏ qua
      }
    }
  }
}

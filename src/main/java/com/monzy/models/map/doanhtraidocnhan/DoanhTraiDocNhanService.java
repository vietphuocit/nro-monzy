package com.monzy.models.map.doanhtraidocnhan;

import com.monzy.models.player.Player;
import com.monzy.services.ChangeMapService;
import com.monzy.services.Service;
import java.util.ArrayList;
import java.util.List;

public class DoanhTraiDocNhanService {

  private static DoanhTraiDocNhanService i;
  public static final List<DoanhTraiDocNhan> DOANH_TRAI_DOC_NHAN_LIST;
  // bang hội đủ số người mới đc mở
  public static final int N_PLAYER_IN_CLAN = 0;
  // số người đứng cùng khu
  public static final int N_PLAYER_IN_MAP = 0;
  public static final int MAX_AVAILABLE = 10;
  public static final int TIME_DOANH_TRAI = 1800000;

  static {
    DOANH_TRAI_DOC_NHAN_LIST = new ArrayList<>();
    for (int i = 0; i < MAX_AVAILABLE; i++) {
      DOANH_TRAI_DOC_NHAN_LIST.add(new DoanhTraiDocNhan(i));
    }
  }

  public static DoanhTraiDocNhanService gI() {
    if (i == null) {
      i = new DoanhTraiDocNhanService();
    }
    return i;
  }

  public void openDoanhTraiDocNhan(Player player) {
    if (player.clan.doanhTraiDocNhan != null) {
      ChangeMapService.gI().changeMapInYard(player, 53, -1, 60);
      return;
    }
    DoanhTraiDocNhan doanhTraiDocNhan = null;
    for (DoanhTraiDocNhan dtdn : DOANH_TRAI_DOC_NHAN_LIST) {
      if (!dtdn.isOpened()) {
        doanhTraiDocNhan = dtdn;
        break;
      }
    }
    if (doanhTraiDocNhan == null) {
      Service.gI().sendThongBao(player, "Doanh trại độc nhãn đã đầy, vui lòng quay lại sau");
      return;
    }
    doanhTraiDocNhan.openDoanhTrai(player);
  }
}

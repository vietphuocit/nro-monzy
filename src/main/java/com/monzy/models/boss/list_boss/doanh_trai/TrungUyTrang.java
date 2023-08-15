package com.monzy.models.boss.list_boss.doanh_trai;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossManager;
import com.monzy.models.boss.BossesData;
import com.monzy.models.map.Zone;

public class TrungUyTrang extends Boss {

  public TrungUyTrang(Zone zone) throws Exception {
    super(BossID.TRUNG_UY_TRANG, BossesData.TRUNG_UY_TRANG);
    this.zoneFinal = zone;
  }

  @Override
  public void leaveMap() {
    super.leaveMap();
    BossManager.gI().removeBoss(this);
    this.dispose();
  }
}

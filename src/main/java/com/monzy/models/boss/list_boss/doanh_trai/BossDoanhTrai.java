package com.monzy.models.boss.list_boss.doanh_trai;

import com.monzy.models.map.doanhtraidocnhan.DoanhTraiDocNhan;

public class BossDoanhTrai {

  public BossDoanhTrai(DoanhTraiDocNhan doanhTraiDocNhan) throws Exception {
    new TrungUyTrang(doanhTraiDocNhan.getMapById(59));
  }
}

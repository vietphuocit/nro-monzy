package com.monzy.models.boss.list_boss.doanh_trai;

import com.monzy.models.boss.BossID;
import com.monzy.models.map.doanhtraidocnhan.DoanhTraiDocNhan;

public class BossDoanhTrai {

  public BossDoanhTrai(DoanhTraiDocNhan doanhTraiDocNhan, int dame, int hp) throws Exception {
    new TrungUyTrang(doanhTraiDocNhan.getMapById(59), dame, hp);
    new TrungUyXanhLo(doanhTraiDocNhan.getMapById(62), dame, hp);
    new TrungUyThep(doanhTraiDocNhan.getMapById(55), dame, hp);
    new NinjaAoTim(doanhTraiDocNhan.getMapById(54), dame, hp);
    new RobotVeSi(BossID.ROBOT_VE_SI, doanhTraiDocNhan.getMapById(57), dame, hp);
    new RobotVeSi(BossID.ROBOT_VE_SI_1, doanhTraiDocNhan.getMapById(57), dame, hp);
    new RobotVeSi(BossID.ROBOT_VE_SI_2, doanhTraiDocNhan.getMapById(57), dame, hp);
    new RobotVeSi(BossID.ROBOT_VE_SI_3, doanhTraiDocNhan.getMapById(57), dame, hp);
  }
}

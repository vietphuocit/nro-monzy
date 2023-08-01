package com.monzy.models.boss.list_boss.doanh_trai;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossData;
import com.monzy.models.map.ItemMap;
import com.monzy.models.map.doanhtrai.DoanhTrai;
import com.monzy.models.player.Player;
import com.monzy.services.Service;
import com.monzy.utils.Util;

public class BossDoanhTrai extends Boss {

  private final DoanhTrai doanhTrai;
  private int xHp;
  private int xDame;

  public BossDoanhTrai(DoanhTrai doanhTrai, int xHp, int xDame, int id, BossData... data)
      throws Exception {
    super(id, data);
    this.doanhTrai = doanhTrai;
  }

  @Override
  public void reward(Player plKill) {
    if (Util.isTrue(100, 100)) {
      ItemMap it =
          new ItemMap(
              this.zone,
              19,
              1,
              this.location.x,
              this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24),
              plKill.id);
      Service.gI().dropItemMap(this.zone, it);
    }
  }

  @Override
  public void initBase() {
    if (this.doanhTrai.getClan() == null) {
      return;
    }
    BossData data = this.data[this.currentLevel];
    this.name = String.format(data.getName(), Util.nextInt(0, 100));
    this.gender = data.getGender();
    this.nPoint.mpg = 7_5_2002;
    long totalDame = 0;
    long totalHp = 0;
    for (Player pl : this.doanhTrai.getClan().membersInGame) {
      totalDame += pl.nPoint.dame;
      totalHp += pl.nPoint.hpMax;
    }
    this.nPoint.hpg = (int) (totalDame * xHp);
    this.nPoint.dameg = (int) (totalHp / xDame);
    this.nPoint.calPoint();
    this.initSkill();
    this.resetBase();
  }
}

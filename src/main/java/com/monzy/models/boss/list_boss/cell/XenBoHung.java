package com.monzy.models.boss.list_boss.cell;

import com.monzy.consts.ConstPlayer;
import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;
import com.monzy.services.PlayerService;
import com.monzy.services.Service;
import com.monzy.utils.Util;

public class XenBoHung extends Boss {

  private long lastTimeHapThu;
  private int timeHapThu;

  public XenBoHung() throws Exception {
    super(
        BossID.XEN_BO_HUNG,
        BossesData.XEN_BO_HUNG_1,
        BossesData.XEN_BO_HUNG_2,
        BossesData.XEN_BO_HUNG_3);
  }

  @Override
  public void reward(Player plKill) {
    rewardItem(plKill, 16);
    super.reward(plKill);
  }

  @Override
  public void active() {
    if (this.typePk == ConstPlayer.NON_PK) {
      this.changeToTypePK();
    }
    this.hapThu();
    this.attack();
  }

  private void hapThu() {
    if (!Util.canDoWithTime(this.lastTimeHapThu, this.timeHapThu) || !Util.isTrue(1, 100)) {
      return;
    }
    Player pl = this.zone.getRandomPlayerInMap();
    if (pl == null || pl.isDie()) {
      return;
    }
    //        ChangeMapService.gI().changeMapYardrat(this, this.zone, pl.location.x, pl.location.y);
    this.nPoint.dameg += pl.nPoint.dame;
    this.nPoint.hpg += (pl.nPoint.hp * 2 / 100);
    this.nPoint.critg++;
    this.nPoint.calPoint();
    PlayerService.gI().hoiPhuc(this, pl.nPoint.hp, 0);
    pl.injured(null, pl.nPoint.hpMax, true, false);
    Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " hấp thu!");
    this.chat(
        2,
        "Ui cha cha, kinh dị quá. " + pl.name + " vừa bị tên " + this.name + " nuốt chửng kìa!!!");
    this.chat("Haha, ngọt lắm đấy " + pl.name + "..");
    this.lastTimeHapThu = System.currentTimeMillis();
    this.timeHapThu = Util.nextInt(30000, 50000);
  }
}

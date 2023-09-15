package com.monzy.models.boss.list_boss;

import com.monzy.consts.ConstPlayer;
import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;
import com.monzy.models.map.ItemMap;
import com.monzy.models.player.Player;
import com.monzy.services.Service;
import com.monzy.utils.Util;

public class AnTrom extends Boss {

  public AnTrom() throws Exception {
    super(BossID.AN_TROM, BossesData.AN_TROM);
  }

  @Override
  public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
    return super.injured(plAtt, 1, piercing, isMobAttack);
  }

  @Override
  public void reward(Player plKill) {
    Service.gI().dropItemMap(this.zone, new ItemMap(this.zone, 457, Util.nextInt(1, 3), plKill.location.x, plKill.location.y, plKill.id));
  }

  @Override
  public void attack() {
    if (Util.canDoWithTime(this.lastTimeAttack, 500) && this.typePk == ConstPlayer.PK_ALL) {
      this.lastTimeAttack = System.currentTimeMillis();
      moveTo(Util.isTrue(50, 100) ? 84 : 660, 336);
    }
  }
}

package com.monzy.models.boss.list_boss.doraemon;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;

public class Xuka extends Boss {

  public Xuka() throws Exception {
    super(BossID.XUKA, BossesData.XUKA);
  }

  @Override
  public void reward(Player plKill) {
    rewardItem(plKill, 16);
    super.reward(plKill);
  }
}

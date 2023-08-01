package com.monzy.models.boss.list_boss.android;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;

public class Poc extends Boss {

  public Poc() throws Exception {
    super(BossID.POC, BossesData.POC);
  }

  @Override
  public void reward(Player plKill) {
    super.reward(plKill);
  }
}

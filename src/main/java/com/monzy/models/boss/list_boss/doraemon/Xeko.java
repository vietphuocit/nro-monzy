package com.monzy.models.boss.list_boss.doraemon;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossManager;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;

public class Xeko extends Boss {

  public Xeko() throws Exception {
    super(BossID.XEKO, BossesData.XEKO);
  }

  @Override
  public void reward(Player plKill) {
    rewardItem(plKill, 16);
    super.reward(plKill);
  }

  @Override
  public boolean chatS() {
    Boss nobita =
        BossManager.gI().getBosses().stream()
            .filter(boss -> boss.id == BossID.NOBITA)
            .findFirst()
            .get();
    return super.chatS() && nobita.isDie();
  }
}

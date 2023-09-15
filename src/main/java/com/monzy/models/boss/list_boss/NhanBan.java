package com.monzy.models.boss.list_boss;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossData;
import com.monzy.models.boss.BossManager;
import com.monzy.models.map.Zone;
import com.monzy.models.player.Player;

public class NhanBan extends Boss {
  public Player real;

  public NhanBan(int bossID, BossData bossData, Zone zone, Player player) throws Exception {
    super(bossID, bossData);
    this.zone = zone;
    this.real = player;
  }

  @Override
  public void reward(Player plKill) {
    // Vật phẩm rơi khi diệt boss nhân bản
//    ItemMap it =
//        new ItemMap(
//            this.zone,
//            Util.nextInt(1099, 1103),
//            Util.nextInt(3, 4),
//            plKill.location.x,
//            plKill.location.y,
//            plKill.id);
//    Service.gI().dropItemMap(this.zone, it);
  }

  @Override
  public void update() {
    super.update();
    if(real.zone != this.zone) {
      leaveMap();
    }
  }

  @Override
  public void leaveMap() {
    super.leaveMap();
    BossManager.gI().removeBoss(this);
    this.dispose();
  }
}

package com.monzy.models.boss.list_boss.doanh_trai;

import com.monzy.consts.ConstPlayer;
import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossData;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossManager;
import com.monzy.models.map.ItemMap;
import com.monzy.models.map.Zone;
import com.monzy.models.player.Player;
import com.monzy.models.skill.Skill;
import com.monzy.services.Service;
import com.monzy.utils.Util;

public class TrungUyThep extends Boss {
  public TrungUyThep(Zone zone, int dame, int hp) throws Exception {
    super(
        BossID.TRUNG_UY_THEP,
        new BossData(
            "Trung Uý Thép", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[] {129, 130, 131, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            dame, // dame
            new int[] {hp}, // hp
            new int[] {55}, // map join
            new int[][] {{Skill.KAMEJOKO, 7, 1000}, {Skill.LIEN_HOAN, 7, 1000}},
            new String[] {}, // text chat 1
            new String[] {}, // text chat 2
            new String[] {}, // text chat 3
            86400));
    this.zoneFinal = zone;
  }

  @Override
  public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
    if (plAtt != null) {
      switch (plAtt.playerSkill.skillSelect.template.id) {
        case Skill.KAMEJOKO:
        case Skill.MASENKO:
        case Skill.ANTOMIC:
          return super.injured(plAtt, damage, piercing, isMobAttack);
      }
    }
    this.chat("Xí hụt");
    return 0;
  }

  @Override
  public void leaveMap() {
    super.leaveMap();
    BossManager.gI().removeBoss(this);
    this.dispose();
  }

  @Override
  public void reward(Player plKill) {
    super.reward(plKill);
    int idNgocRong = Util.nextInt(17, 20);
    Service.gI().dropItemMap(this.zone, new ItemMap(this.zone, idNgocRong, 1, plKill.location.x, plKill.location.y, plKill.id));
  }
}

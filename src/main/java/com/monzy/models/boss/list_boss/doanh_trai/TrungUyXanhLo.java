package com.monzy.models.boss.list_boss.doanh_trai;

import com.monzy.consts.ConstPlayer;
import com.monzy.models.boss.*;
import com.monzy.models.map.Zone;
import com.monzy.models.skill.Skill;

public class TrungUyXanhLo extends Boss {
  public TrungUyXanhLo(Zone zone, int dame, int hp) throws Exception {
    super(
        BossID.TRUNG_UY_XANH_LO,
        new BossData(
            "Trung Uý Xanh Lơ", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[] {135, 136, 137, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            dame, // dame
            new int[] {hp}, // hp
            new int[] {62}, // map join
            new int[][] {{Skill.KAMEJOKO, 7, 1000}, {Skill.LIEN_HOAN, 7, 1000}},
            new String[] {}, // text chat 1
            new String[] {}, // text chat 2
            new String[] {}, // text chat 3
            86400));
    this.zoneFinal = zone;
  }

  @Override
  public void leaveMap() {
    super.leaveMap();
    BossManager.gI().removeBoss(this);
    this.dispose();
  }
}

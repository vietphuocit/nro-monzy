package com.monzy.models.boss.list_boss.doanh_trai;

import com.monzy.consts.ConstPlayer;
import com.monzy.models.boss.*;
import com.monzy.models.map.Zone;
import com.monzy.models.skill.Skill;

public class NinjaAoTimClone extends Boss {
  public NinjaAoTimClone(int id, Zone zone, int dame, int hp) throws Exception {
    super(
        id,
        new BossData(
            "Ninja Áo Tím", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[] {123, 124, 125, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            dame, // dame
            new int[] {hp}, // hp
            new int[] {57}, // map join
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

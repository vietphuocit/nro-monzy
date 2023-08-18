package com.monzy.models.boss.list_boss.doanh_trai;

import com.monzy.consts.ConstPlayer;
import com.monzy.models.boss.*;
import com.monzy.models.map.Zone;
import com.monzy.models.player.Player;
import com.monzy.models.skill.Skill;

public class NinjaAoTim extends Boss {

  public boolean isCallClone;

  public NinjaAoTim(Zone zone, int dame, int hp) throws Exception {
    super(
        BossID.NINJA_AO_TIM,
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
  public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
    if (!this.isCallClone && this.nPoint.hp < this.nPoint.hpg * 0.5f) {
      try {
        new NinjaAoTimClone(BossID.NINJA_AO_TIM_CLONE_1, zone, this.nPoint.dame / 2, this.nPoint.hp);
        new NinjaAoTimClone(BossID.NINJA_AO_TIM_CLONE_2, zone, this.nPoint.dame / 2, this.nPoint.hp);
        new NinjaAoTimClone(BossID.NINJA_AO_TIM_CLONE_3, zone, this.nPoint.dame / 2, this.nPoint.hp);
        new NinjaAoTimClone(BossID.NINJA_AO_TIM_CLONE_4, zone, this.nPoint.dame / 2, this.nPoint.hp);
        this.isCallClone = true;
      } catch (Exception e) {
      }
    }
    return super.injured(plAtt, damage, piercing, isMobAttack);
  }

  @Override
  public void leaveMap() {
    super.leaveMap();
    BossManager.gI().removeBoss(this);
    this.dispose();
  }
}

package com.monzy.models.boss.list_boss.FideBack;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;

public class FideRobot extends Boss {

  public FideRobot() throws Exception {
    super(BossID.FIDE_ROBOT, BossesData.FIDE_ROBOT);
  }
  //    @Override
  //    public void reward(Player plKill) {
  //        int[] itemDos = new int[]{555, 557, 559, 556, 558, 560, 562, 564, 566, 563, 565, 567};
  //        int[] NRs = new int[]{16, 1230};
  //        int randomDo = new Random().nextInt(itemDos.length);
  //        int randomNR = new Random().nextInt(NRs.length);
  //        if (Util.isTrue(15, 100)) {
  //            if (Util.isTrue(1, 5)) {
  //                Service.gI().dropItemMap(this.zone, Util.randomClothesGod(zone, 1230, 1,
  // this.location.x, this.location.y, plKill.id));
  //                return;
  //            }
  //            Service.gI().dropItemMap(this.zone, Util.randomClothesGod(zone, itemDos[randomDo],
  // 1, this.location.x, this.location.y, plKill.id));
  //        } else {
  //            Service.gI().dropItemMap(this.zone, new ItemMap(zone, NRs[randomNR], 1,
  // this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
  //        }
  //    }

  @Override
  public void active() {
    this.attack();
  }
  //    @Override
  //    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
  //        if (plAtt != null) {
  //            switch (plAtt.playerSkill.skillSelect.template.id) {
  //                case Skill.KAMEJOKO:
  //                case Skill.MASENKO:
  //                case Skill.ANTOMIC:
  //                    int hpHoi = (int) ((long) damage * 80 / 100);
  //                    PlayerService.gI().hoiPhuc(this, hpHoi, 0);
  //                    if (Util.isTrue(1, 5)) {
  //                        this.chat("Hahaha,Các ngươi nghĩ sao vậy?");
  //                    }
  //                    return 0;
  //            }
  //        }
  //        return super.injured(plAtt, damage, piercing, isMobAttack);
  //    }
}
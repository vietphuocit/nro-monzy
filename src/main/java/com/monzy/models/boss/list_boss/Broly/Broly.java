//package com.monzy.models.boss.list_boss.Broly;
//
//import com.monzy.models.boss.Boss;
//import com.monzy.models.boss.BossID;
//import com.monzy.models.boss.BossStatus;
//import com.monzy.models.boss.BossesData;
//import com.monzy.models.player.Player;
//import com.monzy.services.EffectSkillService;
//import com.monzy.utils.Util;
//
//public class Broly extends Boss {
//
//  private long st;
//  //    @Override
//  //    public void reward(Player plKill) {
//  //        int[] itemDos = new int[]{1115, 1116, 1117, 1118, 1119};
//  //        int[] NRs = new int[]{17, 18};
//  //        int randomDo = new Random().nextInt(itemDos.length);
//  //        int randomNR = new Random().nextInt(NRs.length);
//  //        if (Util.isTrue(15, 100)) {
//  //            if (Util.isTrue(1, 50)) {
//  //                Service.gI().dropItemMap(this.zone, Util.randomClothesGod(zone, 1230, 1,
//  // this.location.x, this.location.y, plKill.id));
//  //                return;
//  //            }
//  //            Service.gI().dropItemMap(this.zone, Util.randomClothesGod(zone, itemDos[randomDo],
//  // 1, this.location.x, this.location.y, plKill.id));
//  //        } else {
//  //            Service.gI().dropItemMap(this.zone, new ItemMap(zone, NRs[randomNR], 1,
//  // this.location.x, this.location.y, plKill.id));
//  //        }
//  //    }
//
//  public Broly() throws Exception {
//    super(BossID.BROLY, BossesData.BROLY_1, BossesData.BROLY_2, BossesData.BROLY_3);
//  }
//
//  @Override
//  public void active() {
//    super.active(); // To change body of generated methods, choose Tools | Templates.
//    if (Util.canDoWithTime(st, 900000)) {
//      this.changeStatus(BossStatus.LEAVE_MAP);
//    }
//  }
//
//  @Override
//  public void joinMap() {
//    super.joinMap(); // To change body of generated methods, choose Tools | Templates.
//    st = System.currentTimeMillis();
//  }
//
//  @Override
//  public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
//    if (!this.isDie()) {
//      if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
//        this.chat("Xí hụt");
//        return 0;
//      }
//      damage = this.nPoint.subDameInjureWithDef(damage / 2);
//      if (!piercing && effectSkill.isShielding) {
//        if (damage > nPoint.hpMax) {
//          EffectSkillService.gI().breakShield(this);
//        }
//        damage = damage / 2;
//      }
//      this.nPoint.subHP(damage);
//      if (isDie()) {
//        this.setDie(plAtt);
//        die(plAtt);
//      }
//      return damage;
//    } else {
//      return 0;
//    }
//  }
//}

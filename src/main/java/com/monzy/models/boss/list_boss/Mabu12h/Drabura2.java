//package com.monzy.models.boss.list_boss.Mabu12h;
//
//import com.monzy.models.boss.Boss;
//import com.monzy.models.boss.BossesData;
//import com.monzy.models.player.Player;
//import com.monzy.services.EffectSkillService;
//import com.monzy.utils.Util;
//
//public class Drabura2 extends Boss {
//
//  public Drabura2() throws Exception {
//    super(Util.randomBossId(), BossesData.DRABURA_2);
//  }
//  //    @Override
//  //    public void reward(Player plKill) {
//  //        byte randomDo = (byte) new Random().nextInt(Manager.ID_CLOTHES_GOD.length - 1);
//  //        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
//  //        byte randomc12 = (byte) new Random().nextInt(Manager.itemDC12.length - 1);
//  //        if (Util.isTrue(1, 130)) {
//  //            if (Util.isTrue(1, 50)) {
//  //                Service.gI().dropItemMap(this.zone, Util.randomClothesGod(zone, 561, 1,
//  // this.location.x, this.location.y, plKill.id));
//  //                return;
//  //            }
//  //            Service.gI().dropItemMap(this.zone, Util.randomClothesGod(zone,
//  // Manager.ID_CLOTHES_GOD[randomDo], 1, this.location.x, this.location.y, plKill.id));
//  //        } else if (Util.isTrue(50, 100)) {
//  //            Service.gI().dropItemMap(this.zone, new ItemMap(Util.RandomCSDoThuong(zone,
//  // Manager.itemDC12[randomc12], 1, this.location.x, this.location.y, plKill.id)));
//  //            return;
//  //        } else {
//  //            Service.gI().dropItemMap(this.zone, new ItemMap(zone,
//  // Manager.itemIds_NR_SB[randomNR], 1, this.location.x, this.location.y, plKill.id));
//  //        }
//  //        plKill.fightMabu.changePoint((byte) 20);
//  //    }
//
//  @Override
//  public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
//    if (!this.isDie()) {
//      if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1)) {
//        this.chat("Xí hụt");
//        return 0;
//      }
//      damage = this.nPoint.subDameInjureWithDef(damage);
//      if (!piercing && effectSkill.isShielding) {
//        if (damage > nPoint.hpMax) {
//          EffectSkillService.gI().breakShield(this);
//        }
//        damage = 1;
//      }
//      if (damage >= 1000000) {
//        damage = 1000000;
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

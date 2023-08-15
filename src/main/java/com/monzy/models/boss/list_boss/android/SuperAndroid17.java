//package com.monzy.models.boss.list_boss.android;
//
//import com.monzy.models.boss.Boss;
//import com.monzy.models.boss.BossID;
//import com.monzy.models.boss.BossManager;
//import com.monzy.models.boss.BossesData;
//import com.monzy.models.map.ItemMap;
//import com.monzy.models.player.Player;
//import com.monzy.services.EffectSkillService;
//import com.monzy.services.Service;
//import com.monzy.utils.Util;
//
//public class SuperAndroid17 extends Boss {
//
//  private long st;
//
//  public SuperAndroid17() throws Exception {
//    super(BossID.SUPER_ANDROID_17, BossesData.SUPER_ANDROID_17);
//    this.nPoint.defg = (short) (this.nPoint.hpg / 1000);
//    if (this.nPoint.defg < 0) {
//      this.nPoint.defg = (short) -this.nPoint.defg;
//    }
//  }
//
//  @Override
//  public void reward(Player plKill) {
//    if (Util.isTrue(15, 100)) {
//      ItemMap it =
//          new ItemMap(
//              this.zone,
//              1230,
//              1,
//              this.location.x,
//              this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24),
//              plKill.id);
//      Service.gI().dropItemMap(this.zone, it);
//    }
//  }
//
//  @Override
//  public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
//    if (!this.isDie()) {
//      if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
//        this.chat("Xí hụt");
//        return 0;
//      }
//      damage = this.nPoint.subDameInjureWithDef(damage / 1);
//      if (!piercing && effectSkill.isShielding) {
//        if (damage > nPoint.hpMax) {
//          EffectSkillService.gI().breakShield(this);
//        }
//        damage = damage / 1;
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
//
//  @Override
//  public void active() {
//    super.active();
//  }
//
//  @Override
//  public void joinMap() {
//    super.joinMap(); // To change body of generated methods, choose Tools | Templates.
//    st = System.currentTimeMillis();
//  }
//
//  @Override
//  public void leaveMap() {
//    super.leaveMap();
//    BossManager.gI().removeBoss(this);
//    this.dispose();
//  }
//}

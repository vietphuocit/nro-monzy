package com.monzy.models.boss.list_boss.HuyDiet;

import com.monzy.consts.ConstPlayer;
import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;
import com.monzy.services.EffectSkillService;
import com.monzy.services.PlayerService;
import com.monzy.services.Service;
import com.monzy.utils.Util;

public class Champa extends Boss {

  private long lasttimehakai;
  private int timehakai;

  public Champa() throws Exception {
    super(Util.randomBossId(), BossesData.THAN_HUY_DIET_CHAMPA);
  }
  //    @Override
  //    public void reward(Player plKill) {
  //        byte randomDo = (byte) new Random().nextInt(Manager.ID_CLOTHES_GOD.length - 1);
  //        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
  //        ItemMap itemMap;
  //        if (Util.isTrue(5, 100)) {
  //            if (Util.isTrue(1, 50)) {
  //                itemMap = Util.randomClothesGod(zone, 1142, 1, this.location.x, this.location.y,
  // plKill.id);
  //            } else {
  //                itemMap = Util.randomClothesGod(zone, Manager.ID_CLOTHES_GOD[randomDo], 1,
  // this.location.x, this.location.y, plKill.id);
  //            }
  //        } else {
  //            itemMap = Util.randomClothesGod(zone, Manager.itemIds_NR_SB[randomNR], 1,
  // this.location.x, this.location.y, plKill.id);
  //        }
  //        itemMap.options.add(new Item.ItemOption(30, 1));
  //        Service.gI().dropItemMap(this.zone, itemMap);
  //    }

  @Override
  public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
    if (!this.isDie()) {
      if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1)) {
        this.chat("Xí hụt");
        return 0;
      }
      damage = this.nPoint.subDameInjureWithDef(damage / 3);
      if (!piercing && effectSkill.isShielding) {
        if (damage > nPoint.hpMax) {
          EffectSkillService.gI().breakShield(this);
        }
        damage = 1;
      }
      this.nPoint.subHP(damage);
      if (isDie()) {
        this.setDie(plAtt);
        die(plAtt);
      }
      return damage;
    } else {
      return 0;
    }
  }

  @Override
  public void active() {
    if (this.typePk == ConstPlayer.NON_PK) {
      this.changeToTypePK();
    }
    this.huydiet();
    this.attack();
    //        super.active(); //To change body of generated methods, choose Tools | Templates.
    //        if (Util.canDoWithTime(st, 1000000)) {
    //            this.changeStatus(BossStatus.LEAVE_MAP);
  }
  //    @Override
  //    public void joinMap() {
  //        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
  //        st = System.currentTimeMillis();
  //    }
  //    private long st;

  private void huydiet() {
    if (!Util.canDoWithTime(this.lasttimehakai, this.timehakai) || !Util.isTrue(1, 100)) {
      return;
    }
    Player pl = this.zone.getRandomPlayerInMap();
    if (pl == null || pl.isDie()) {
      return;
    }
    this.nPoint.dameg += (pl.nPoint.dame * 5 / 100);
    this.nPoint.hpg += (pl.nPoint.hp * 2 / 100);
    this.nPoint.critg++;
    this.nPoint.calPoint();
    PlayerService.gI().hoiPhuc(this, pl.nPoint.hp, 0);
    pl.injured(null, pl.nPoint.hpMax, true, false);
    Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " cho bay màu");
    this.chat(
        2,
        "Hắn ta mạnh quá,coi chừng "
            + pl.name
            + ",tên "
            + this.name
            + " hắn không giống như những kẻ thù trước đây");
    this.chat("Thật là yếu ớt " + pl.name);
    this.lasttimehakai = System.currentTimeMillis();
    this.timehakai = Util.nextInt(20000, 30000);
  }
}

package com.monzy.models.mob;

import com.monzy.models.map.Zone;
import com.monzy.models.player.Player;
import com.monzy.services.Service;
import com.monzy.utils.SkillUtil;
import com.monzy.utils.Util;
import com.network.io.Message;

public final class MobMe extends Mob {

  private final long lastTimeSpawn;
  private final int timeSurvive;
  private Player player;

  public MobMe(Player player) {
    super();
    this.player = player;
    this.id = (int) player.id;
    int level = player.playerSkill.getSkillbyId(12).point;
    this.tempId = SkillUtil.getTempMobMe(level);
    this.point.maxHp = SkillUtil.getHPMobMe(player.nPoint.hpMax, level);
    this.point.dame = SkillUtil.getHPMobMe(player.nPoint.getDameAttack(false), level);
    if (this.player.setClothes.pikkoroDaimao == 5) {
      this.point.dame *= 2;
    }
    this.point.hp = this.point.maxHp;
    this.zone = player.zone;
    this.lastTimeSpawn = System.currentTimeMillis();
    this.timeSurvive = SkillUtil.getTimeSurviveMobMe(level);
    spawn();
  }

  @Override
  public void update() {
    if (Util.canDoWithTime(lastTimeSpawn, timeSurvive)
        && this.player.setClothes.pikkoroDaimao != 5) {
      this.mobMeDie();
      this.dispose();
    }
  }

  public void attack(Player pl, Mob mob) {
    Message msg;
    try {
      if (pl != null) {
        if (pl.nPoint.hp > this.point.dame && pl.nPoint.hp > pl.nPoint.hpMax * 0.05) {
          int dameHit = pl.injured(null, this.point.dame, true, true);
          msg = new Message(-95);
          msg.writer().writeByte(2);
          msg.writer().writeInt(this.id);
          msg.writer().writeInt((int) pl.id);
          msg.writer().writeInt(dameHit);
          msg.writer().writeInt(pl.nPoint.hp);
          Service.gI().sendMessAllPlayerInMap(this.zone, msg);
          msg.cleanup();
        }
      }
      if (mob != null) {
        if (mob.point.gethp() > this.point.dame) {
          long tnsm = mob.getTiemNangForPlayer(this.player, this.point.dame);
          msg = new Message(-95);
          msg.writer().writeByte(3);
          msg.writer().writeInt(this.id);
          msg.writer().writeInt(mob.id);
          mob.point.sethp(mob.point.gethp() - this.point.dame);
          msg.writer().writeInt(mob.point.gethp());
          msg.writer().writeInt(this.point.dame);
          Service.gI().sendMessAllPlayerInMap(this.zone, msg);
          msg.cleanup();
          Service.gI().addSMTN(player, (byte) 2, tnsm, true);
        }
      }
    } catch (Exception e) {
    }
  }

  // tạo mobme
  public void spawn() {
    Message msg;
    try {
      msg = new Message(-95);
      msg.writer().writeByte(0); // type
      msg.writer().writeInt((int) player.id);
      msg.writer().writeShort(this.tempId);
      msg.writer().writeInt(this.point.hp); // hp mob
      Service.gI().sendMessAllPlayerInMap(this.zone, msg);
      msg.cleanup();
    } catch (Exception e) {
    }
  }

  public void goToMap(Zone zone) {
    if (zone != null) {
      this.removeMobInMap();
      this.zone = zone;
    }
  }

  // xóa mobme khỏi map
  private void removeMobInMap() {
    Message msg;
    try {
      msg = new Message(-95);
      msg.writer().writeByte(7); // type
      msg.writer().writeInt((int) player.id);
      Service.gI().sendMessAllPlayerInMap(this.zone, msg);
      msg.cleanup();
    } catch (Exception e) {
    }
  }

  public void mobMeDie() {
    Message msg;
    try {
      msg = new Message(-95);
      msg.writer().writeByte(6); // type
      msg.writer().writeInt((int) player.id);
      Service.gI().sendMessAllPlayerInMap(this.zone, msg);
      msg.cleanup();
    } catch (Exception e) {
    }
  }

  public void dispose() {
    player.mobMe = null;
    this.player = null;
  }
}

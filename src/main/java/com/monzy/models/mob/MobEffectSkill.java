package com.monzy.models.mob;

import com.monzy.models.map.nguhanhson.nguhs;
import com.monzy.services.Service;
import com.monzy.utils.Logger;
import com.monzy.utils.Util;
import com.network.io.Message;

public class MobEffectSkill {

  private final Mob mob;
  public long lastTimeStun;
  public int timeStun;
  public boolean isStun;
  public boolean isThoiMien;
  public long lastTimeThoiMien;
  public int timeThoiMien;
  public boolean isBlindDCTT;
  public long lastTimeBlindDCTT;
  public int timeBlindDCTT;
  public boolean isAnTroi;
  public long lastTimeAnTroi;
  public int timeAnTroi;
  public boolean isSocola;
  private long lastTimeSocola;
  private int timeSocola;

  public MobEffectSkill(Mob mob) {
    this.mob = mob;
  }

  public void update() {
    if (isStun && (Util.canDoWithTime(lastTimeStun, timeStun) || mob.isDie())) {
      removeStun();
    }
    if (isThoiMien && (Util.canDoWithTime(lastTimeThoiMien, timeThoiMien) || mob.isDie())) {
      removeThoiMien();
    }
    if (isBlindDCTT && (Util.canDoWithTime(lastTimeBlindDCTT, timeBlindDCTT)) || mob.isDie()) {
      removeBlindDCTT();
    }
    if (isSocola && (Util.canDoWithTime(lastTimeSocola, timeSocola) || mob.isDie())) {
      removeSocola();
    }
    if (isAnTroi && (Util.canDoWithTime(lastTimeAnTroi, timeAnTroi) || mob.isDie())) {
      removeAnTroi();
    }
  }

  public boolean isHaveEffectSkill() {
    return isAnTroi || isBlindDCTT || isStun || isThoiMien;
  }

  public void startStun(long lastTimeStartBlind, int timeBlind) {
    this.lastTimeStun = lastTimeStartBlind;
    this.timeStun = timeBlind;
    isStun = true;
  }

  private void removeStun() {
    isStun = false;
    Message msg;
    try {
      msg = new Message(-124);
      msg.writer().writeByte(0);
      msg.writer().writeByte(1);
      msg.writer().writeByte(40);
      msg.writer().writeByte(mob.id);
      Service.gI().sendMessAllPlayerInMap(mob.zone, msg);
      msg.cleanup();
    } catch (Exception e) {
      Logger.logException(MobEffectSkill.class, e);
    }
  }

  public void setThoiMien(long lastTimeThoiMien, int timeThoiMien) {
    this.isThoiMien = true;
    this.lastTimeThoiMien = lastTimeThoiMien;
    this.timeThoiMien = timeThoiMien;
  }

  public void removeThoiMien() {
    this.isThoiMien = false;
    Message msg;
    try {
      msg = new Message(-124);
      msg.writer().writeByte(0); // b5
      msg.writer().writeByte(1); // b6
      msg.writer().writeByte(41); // num6
      msg.writer().writeByte(mob.id); // b7
      Service.gI().sendMessAllPlayerInMap(mob.zone, msg);
      msg.cleanup();
    } catch (Exception e) {
      Logger.logException(MobEffectSkill.class, e);
    }
  }

  public void setStartBlindDCTT(long lastTimeBlindDCTT, int timeBlindDCTT) {
    this.isBlindDCTT = true;
    this.lastTimeBlindDCTT = lastTimeBlindDCTT;
    this.timeBlindDCTT = timeBlindDCTT;
  }

  public void removeBlindDCTT() {
    this.isBlindDCTT = false;
    Message msg;
    try {
      msg = new Message(-124);
      msg.writer().writeByte(0);
      msg.writer().writeByte(1);
      msg.writer().writeByte(40);
      msg.writer().writeByte(mob.id);
      Service.gI().sendMessAllPlayerInMap(mob.zone, msg);
      msg.cleanup();
    } catch (Exception e) {
      Logger.logException(MobEffectSkill.class, e);
    }
  }

  public void setTroi(long lastTimeAnTroi, int timeAnTroi) {
    this.lastTimeAnTroi = lastTimeAnTroi;
    this.timeAnTroi = timeAnTroi;
    this.isAnTroi = true;
  }

  public void removeAnTroi() {
    isAnTroi = false;
    Message msg;
    try {
      msg = new Message(-124);
      msg.writer().writeByte(0); // b4
      msg.writer().writeByte(1); // b5
      msg.writer().writeByte(32); // num8
      msg.writer().writeByte(mob.id); // b6
      Service.gI().sendMessAllPlayerInMap(mob.zone, msg);
      msg.cleanup();
    } catch (Exception e) {
      Logger.logException(MobEffectSkill.class, e);
    }
  }

  public void removeSocola() {
    Message msg;
    this.isSocola = false;
    try {
      msg = new Message(-112);
      msg.writer().writeByte(0);
      msg.writer().writeByte(mob.id);
      Service.gI().sendMessAllPlayerInMap(mob.zone, msg);
      msg.cleanup();
    } catch (Exception e) {
      Logger.logException(MobEffectSkill.class, e);
    }
  }

  public void setSocola(long lastTimeSocola, int timeSocola) {
    this.lastTimeSocola = lastTimeSocola;
    this.timeSocola = timeSocola;
    this.isSocola = true;
  }
}

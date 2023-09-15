package com.monzy.models.skill;

import com.monzy.models.player.Player;
import com.monzy.models.shop.ShopServiceNew;
import com.monzy.services.Service;
import com.monzy.utils.Logger;
import com.network.io.Message;

import java.util.ArrayList;
import java.util.List;

public class PlayerSkill {

  public List<Skill> skills;
  public Skill skillSelect;
  public boolean prepareQCKK;
  public boolean prepareTuSat;
  public boolean prepareLaze;
  public long lastTimePrepareQCKK;
  public long lastTimePrepareTuSat;
  public long lastTimePrepareLaze;
  public byte[] skillShortCut = new byte[10];
  private Player player;

  public PlayerSkill(Player player) {
    this.player = player;
    skills = new ArrayList<>();
  }

  public Skill getSkillbyId(int id) {
    for (Skill skill : skills) {
      if (skill.template.id == id) {
        return skill;
      }
    }
    return null;
  }

  public void sendSkillShortCut() {
    Message msg;
    try {
      msg = Service.gI().messageSubCommand((byte) 61);
      msg.writer().writeUTF("KSkill");
      msg.writer().writeInt(skillShortCut.length);
      msg.writer().write(skillShortCut);
      player.sendMessage(msg);
      msg.cleanup();
      msg = Service.gI().messageSubCommand((byte) 61);
      msg.writer().writeUTF("OSkill");
      msg.writer().writeInt(skillShortCut.length);
      msg.writer().write(skillShortCut);
      player.sendMessage(msg);
      msg.cleanup();
    } catch (Exception e) {
      Logger.logException(PlayerSkill.class, e);
    }
  }

  public byte getSizeSkill() {
    byte size = 0;
    for (Skill skill : skills) {
      if (skill.skillId != -1) {
        size++;
      }
    }
    return size;
  }

  public void dispose() {
    if (this.skillSelect != null) {
      this.skillSelect.dispose();
    }
    if (this.skills != null) {
      for (Skill skill : this.skills) {
        skill.dispose();
      }
      this.skills.clear();
    }
    this.player = null;
    this.skillSelect = null;
    this.skills = null;
  }
}

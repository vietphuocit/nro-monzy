package com.monzy.models.boss.list_boss.android;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;
import com.monzy.models.skill.Skill;
import com.monzy.services.PlayerService;
import com.monzy.utils.Util;

public class Android19 extends Boss {

  public Android19() throws Exception {
    super(BossID.ANDROID_19, BossesData.ANDROID_19);
  }

  @Override
  public void reward(Player plKill) {
    super.reward(plKill);
  }

  @Override
  public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
    if (plAtt != null) {
      switch (plAtt.playerSkill.skillSelect.template.id) {
        case Skill.KAMEJOKO:
        case Skill.MASENKO:
        case Skill.ANTOMIC:
          int hpHoi = (int) ((long) damage * 80 / 100);
          PlayerService.gI().hoiPhuc(this, hpHoi, 0);
          if (Util.isTrue(1, 1)) {
            this.chat("Hấp thụ.. các ngươi nghĩ sao vậy?");
          }
          return 0;
      }
    }
    return super.injured(plAtt, damage, piercing, isMobAttack);
  }
}

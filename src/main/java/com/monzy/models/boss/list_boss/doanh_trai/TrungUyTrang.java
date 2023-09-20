package com.monzy.models.boss.list_boss.doanh_trai;

import com.monzy.consts.ConstPlayer;
import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossData;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossManager;
import com.monzy.models.map.ItemMap;
import com.monzy.models.map.Zone;
import com.monzy.models.player.Player;
import com.monzy.models.skill.Skill;
import com.monzy.services.Service;
import com.monzy.services.SkillService;
import com.monzy.utils.Logger;
import com.monzy.utils.Util;

public class TrungUyTrang extends Boss {

  public TrungUyTrang(Zone zone, int dame, int hp) throws Exception {
    super(
        BossID.TRUNG_UY_TRANG,
        new BossData(
            "Trung Uý Trắng", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[] {141, 142, 143, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            dame,
            new int[] {hp}, // hp
            new int[] {59}, // map join
            new int[][] {{Skill.KAMEJOKO, 7, 1000}, {Skill.LIEN_HOAN, 7, 1000}},
            new String[] {}, // text chat 1
            new String[] {}, // text chat 2
            new String[] {}, // text chat 3
            86400));
    this.zoneFinal = zone;
  }

  @Override
  public void attack() {
    if (Util.canDoWithTime(this.lastTimeAttack, 333) && this.typePk == ConstPlayer.PK_ALL) {
      this.lastTimeAttack = System.currentTimeMillis();
      try {
        Player pl =
            this.zone.getPlayers().stream()
                .filter(player -> player.location.x > 740 && player.location.x < 1080)
                .skip((int) (this.zone.getPlayers().size() * Math.random()))
                .findFirst()
                .orElse(null);
        if (pl == null || pl.isDie() || pl.isNewPet) {
          this.moveTo(910, 384);
          return;
        }
        this.playerSkill.skillSelect =
            this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
        this.moveToPlayer(pl);
        SkillService.gI().useSkill(this, pl, null, null);
      } catch (Exception e) {
        Logger.logException(TrungUyTrang.class, e);
      }
    }
  }

  @Override
  public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
    if (zone.mobs.stream().anyMatch(mob -> mob.tempId == 22 && !mob.isDie())) {
      this.chat("Xí hụt");
      return 0;
    }
    return super.injured(plAtt, damage, piercing, isMobAttack);
  }

  @Override
  public void leaveMap() {
    super.leaveMap();
    BossManager.gI().removeBoss(this);
    this.dispose();
  }

	@Override
	public void reward(Player plKill) {
		super.reward(plKill);
		int idNgocRong = Util.nextInt(17, 20);
		Service.gI().dropItemMap(this.zone, new ItemMap(this.zone, idNgocRong, 1, plKill.location.x, plKill.location.y, plKill.id));
	}
}

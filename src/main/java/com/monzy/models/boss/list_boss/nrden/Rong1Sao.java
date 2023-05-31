package com.monzy.models.boss.list_boss.nrden;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossesData;
import com.monzy.models.map.ItemMap;
import com.monzy.models.player.Player;
import com.monzy.services.EffectSkillService;
import com.monzy.services.Service;
import com.monzy.utils.Util;

public class Rong1Sao extends Boss {

    public Rong1Sao() throws Exception {
        super(Util.randomBossId(), BossesData.Rong_1Sao);
    }

    @Override
    public void reward(Player plKill) {
        ItemMap it = new ItemMap(this.zone, 372, 1, this.location.x, this.location.y, -1);
        Service.gI().dropItemMap(this.zone, it);
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage / 7);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage / 4;
            }
            if (!piercing && effectSkill.useTroi) {
                EffectSkillService.gI().removeUseTroi(this);
            }
            if (!piercing && effectSkill.isStun) {
                EffectSkillService.gI().removeStun(this);
            }
            if (!piercing && effectSkill.isThoiMien) {
                EffectSkillService.gI().removeThoiMien(this);
            }
            if (!piercing && effectSkill.isBlindDCTT) {
                EffectSkillService.gI().removeBlindDCTT(this);
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

}



package com.monzy.models.boss.list_boss.black;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossStatus;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;
import com.monzy.services.EffectSkillService;
import com.monzy.utils.Util;

public class SuperBlack2 extends Boss {

    private long st;
//    @Override
//    public void reward(Player plKill) {
//        byte randomDo = (byte) new Random().nextInt(Manager.ID_CLOTHES_GOD.length - 1);
//        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
//        int[] itemDos = new int[]{233, 237, 241, 245, 249, 253, 257, 261, 265, 269, 273, 277, 281};
//        int randomc12 = new Random().nextInt(itemDos.length);
//        if (Util.isTrue(BossManager.ratioReward, 100)) {
//            if (Util.isTrue(1, 5)) {
//                Service.gI().dropItemMap(this.zone, Util.randomClothesGod(zone, 561, 1, this.location.x, this.location.y, plKill.id));
//                return;
//            }
//            Service.gI().dropItemMap(this.zone, Util.randomClothesGod(zone, Manager.ID_CLOTHES_GOD[randomDo], 1, this.location.x, this.location.y, plKill.id));
//        } else if (Util.isTrue(2, 5)) {
//            Service.gI().dropItemMap(this.zone, Util.RandomCSDoThuong(zone, itemDos[randomc12], 1, this.location.x, this.location.y, plKill.id));
//            return;
//        } else {
//            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, this.location.y, plKill.id));
//        }
//    }

    public SuperBlack2() throws Exception {
        super(Util.randomBossId(), BossesData.SUPER_BLACK_GOKU_2);
    }

    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDef(damage / 2);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage;
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
//    @Override
//    public void moveTo(int x, int y) {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.moveTo(x, y);
//    }
//
//    @Override
//    public void reward(Player plKill) {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.reward(plKill);
//    }
//
//    @Override
//    protected void notifyJoinMap() {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.notifyJoinMap();
//    }
}























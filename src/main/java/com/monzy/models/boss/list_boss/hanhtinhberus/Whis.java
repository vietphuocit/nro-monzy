package com.monzy.models.boss.list_boss.hanhtinhberus;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossManager;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;

public class Whis extends Boss {

    public Whis() throws Exception {
        super(BossID.WISH, BossesData.WISH);
    }

    @Override
    public void reward(Player plKill) {
        rewardItem(plKill, 1070, 1069, 1068, 1067, 1066);
        rewardItem(plKill, 859, 956, 1142);
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.effectSkill.isShielding) {
            return 0;
        } else {
            this.nPoint.subHP(1);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return 1;
        }
    }

    @Override
    public boolean chatS() {
        Boss berus = BossManager.gI().getBosses().stream().filter(boss -> boss.id == BossID.BILL).findFirst().get();
        return super.chatS() && berus.isDie();
    }

}

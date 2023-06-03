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
    public boolean chatS() {
        Boss berus = BossManager.gI().getBosses().stream().filter(boss -> boss.id == BossID.BILL).findFirst().get();
        return super.chatS() && berus.isDie();
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.effectSkill.isShielding) {
            this.chat("Xí hụt");
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void reward(Player plKill) {
        rewardItem(plKill, 1066, 1067, 1068, 1069, 1070, 1142);
    }

}

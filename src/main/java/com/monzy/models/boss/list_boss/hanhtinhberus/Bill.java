package com.monzy.models.boss.list_boss.hanhtinhberus;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;

public class Bill extends Boss {

    public Bill() throws Exception {
        super(BossID.BILL, BossesData.BILL);
    }

    @Override
    public void reward(Player plKill) {
        rewardManhThienSu(plKill);
        rewardItem(plKill, 859, 956, 1142);
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        return super.injured(plAtt, damage / 2, piercing, isMobAttack);
    }

}

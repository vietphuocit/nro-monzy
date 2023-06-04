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
        rewardItem(plKill, 1069, 1070, 1066, 1067, 1068, 1142);
    }

}

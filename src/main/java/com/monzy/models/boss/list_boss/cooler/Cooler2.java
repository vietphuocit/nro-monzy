package com.monzy.models.boss.list_boss.cooler;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossManager;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;

public class Cooler2 extends Boss {

    public Cooler2() throws Exception {
        super(BossID.COOLER_2, BossesData.COOLER_2);
    }

    @Override
    public void reward(Player plKill) {
        super.reward(plKill);
        rewardDTL(plKill);
    }

    @Override
    public boolean chatS() {
        Boss cooler = BossManager.gI().getBosses().stream().filter(boss -> boss.id == BossID.COOLER).findFirst().get();
        return super.chatS() && cooler.isDie();
    }

}

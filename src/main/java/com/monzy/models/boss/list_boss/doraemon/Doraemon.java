package com.monzy.models.boss.list_boss.doraemon;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossManager;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;

public class Doraemon extends Boss {

    public Doraemon() throws Exception {
        super(BossID.DORAEMON, BossesData.DORAEMON);
    }

    @Override
    public void reward(Player plKill) {
        rewardItem(plKill, 16);
        super.reward(plKill);
    }

    @Override
    public boolean chatS() {
        Boss chaien = BossManager.gI().getBosses().stream().filter(boss -> boss.id == BossID.CHAIEN).findFirst().get();
        return super.chatS() && chaien.isDie();
    }

}
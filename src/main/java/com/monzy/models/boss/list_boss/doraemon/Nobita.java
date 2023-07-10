package com.monzy.models.boss.list_boss.doraemon;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossManager;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;
import com.monzy.services.TaskService;

public class Nobita extends Boss {

    public Nobita() throws Exception {
        super(BossID.NOBITA, BossesData.NOBITA);
    }

    @Override
    public void reward(Player plKill) {
        rewardItem(plKill, 16);
        super.reward(plKill);
    }

    @Override
    public boolean chatS() {
        Boss doraemon = BossManager.gI().getBosses().stream().filter(boss -> boss.id == BossID.DORAEMON).findFirst().get();
        return super.chatS() && doraemon.isDie();
    }

}























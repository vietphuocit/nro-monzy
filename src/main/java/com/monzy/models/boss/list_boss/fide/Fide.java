package com.monzy.models.boss.list_boss.fide;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossStatus;
import com.monzy.models.boss.BossesData;
import com.monzy.models.map.ItemMap;
import com.monzy.models.player.Player;
import com.monzy.services.Service;
import com.monzy.services.TaskService;
import com.monzy.utils.Util;

public class Fide extends Boss {

    public Fide() throws Exception {
        super(BossID.FIDE, BossesData.FIDE_DAI_CA_1, BossesData.FIDE_DAI_CA_2, BossesData.FIDE_DAI_CA_3);
    }

    @Override
    public void reward(Player plKill) {
        super.rewardItem(plKill, 16);
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

}























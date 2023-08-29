package com.monzy.models.boss.list_boss.dhvt;

import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;

public class ODo extends BossDHVT {

    public ODo(Player player) throws Exception {
        super(BossID.O_DO, BossesData.O_DO);
        this.playerAtt = player;
    }

}

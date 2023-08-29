package com.monzy.models.boss.list_boss.dhvt;

import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;

public class ChanXu extends BossDHVT {

    public ChanXu(Player player) throws Exception {
        super(BossID.CHAN_XU, BossesData.CHAN_XU);
        this.playerAtt = player;
    }

}
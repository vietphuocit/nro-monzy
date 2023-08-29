package com.monzy.models.boss.list_boss.dhvt;

import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;

public class ChaPa extends BossDHVT {

    public ChaPa(Player player) throws Exception {
        super(BossID.CHA_PA, BossesData.CHA_PA);
        this.playerAtt = player;
    }

}
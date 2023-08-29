package com.monzy.models.boss.list_boss.dhvt;

import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;

public class LiuLiu extends BossDHVT {

    public LiuLiu(Player player) throws Exception {
        super(BossID.LIU_LIU, BossesData.LIU_LIU);
        this.playerAtt = player;
    }

}
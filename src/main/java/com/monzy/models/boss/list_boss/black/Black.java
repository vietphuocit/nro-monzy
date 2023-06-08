package com.monzy.models.boss.list_boss.black;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;
import com.monzy.models.item.Item;
import com.monzy.models.map.ItemMap;
import com.monzy.models.player.Player;
import com.monzy.server.Manager;
import com.monzy.services.ItemService;
import com.monzy.services.Service;
import com.monzy.utils.Util;

import java.util.Arrays;
import java.util.stream.Stream;

public class Black extends Boss {

    public Black() throws Exception {
        super(BossID.BLACK, BossesData.BLACK_GOKU, BossesData.SUPER_BLACK_GOKU);
    }

    @Override
    public void reward(Player plKill) {
        rewardDTL(plKill);
        rewardItem(plKill, 1142, 16);
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        return super.injured(plAtt, damage / 2, piercing, isMobAttack);
    }

}























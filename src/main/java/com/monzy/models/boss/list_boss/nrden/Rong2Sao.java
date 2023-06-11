package com.monzy.models.boss.list_boss.nrden;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossesData;
import com.monzy.models.map.ItemMap;
import com.monzy.models.player.Player;
import com.monzy.services.Service;
import com.monzy.utils.Util;

public class Rong2Sao extends Boss {

    public Rong2Sao() throws Exception {
        super(Util.randomBossId(), BossesData.Rong_2Sao);
    }

    @Override
    public void reward(Player plKill) {
        ItemMap it = new ItemMap(this.zone, 373, 1, this.location.x, this.location.y, -1);
        Service.gI().dropItemMap(this.zone, it);
    }

}



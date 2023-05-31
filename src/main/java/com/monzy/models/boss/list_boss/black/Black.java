package com.monzy.models.boss.list_boss.black;

import com.monzy.models.boss.*;
import com.monzy.models.map.ItemMap;
import com.monzy.models.player.Player;
import com.monzy.server.Manager;
import com.monzy.services.EffectSkillService;
import com.monzy.services.Service;
import com.monzy.utils.Util;

import java.util.Random;
import java.util.stream.Stream;

public class Black extends Boss {

    public Black() throws Exception {
        super(BossID.BLACK, BossesData.BLACK_GOKU, BossesData.SUPER_BLACK_GOKU);
    }

    @Override
    public void reward(Player plKill) {
        int trangBi = Stream.of(555, 557, 559, 556, 558, 560, 563, 565, 567).skip((int) (9 * Math.random())).findFirst().get();
        if (Util.isTrue(30, 100)) {
            Service.gI().dropItemMap(this.zone, Util.randomAWJThan(zone, trangBi, 1, this.location.x, this.location.y, plKill.id));
        }
        rewardItem(plKill, 1142, 15, 16);
    }

}























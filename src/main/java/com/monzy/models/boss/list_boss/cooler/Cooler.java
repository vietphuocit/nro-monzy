package com.monzy.models.boss.list_boss.cooler;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;
import com.monzy.services.Service;
import com.monzy.utils.Util;

import java.util.stream.Stream;

public class Cooler extends Boss {

    public Cooler() throws Exception {
        super(BossID.COOLER, BossesData.COOLER_1, BossesData.COOLER_2);
    }

    @Override
    public void reward(Player plKill) {
        int trangBi = Stream.of(233, 237, 241, 245, 249, 253, 257, 261, 265, 269, 273, 277, 281).skip((int) (13 * Math.random())).findFirst().get();
        if (Util.isTrue(30, 100)) {
            Service.gI().dropItemMap(this.zone, Util.RandomCSDoThuong(zone, trangBi, 1, this.location.x, this.location.y, plKill.id));
        }
        rewardItem(plKill, 1142, 16);
    }

}

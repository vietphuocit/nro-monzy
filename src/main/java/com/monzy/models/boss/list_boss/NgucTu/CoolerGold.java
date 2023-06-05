/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monzy.models.boss.list_boss.NgucTu;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossManager;
import com.monzy.models.boss.BossesData;

/**
 * @Stole By Arriety
 */
public class CoolerGold extends Boss {

    public CoolerGold() throws Exception {
        super(BossID.COOLER_GOLD, BossesData.COOLER_GOLD);
    }
//    @Override
//    public void reward(Player plKill) {
//        int[] itemDos = new int[]{1142, 1142, 1117, 1142, 1142};
//        int[] NRs = new int[]{17, 16};
//        int randomDo = new Random().nextInt(itemDos.length);
//        int randomNR = new Random().nextInt(NRs.length);
//        if (Util.isTrue(15, 100)) {
//            if (Util.isTrue(1, 50)) {
//                Service.gI().dropItemMap(this.zone, Util.randomClothesGod(zone, 561, 1, this.location.x, this.location.y, plKill.id));
//                return;
//            }
//            Service.gI().dropItemMap(this.zone, Util.randomClothesGod(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
//        } else if (Util.isTrue(50, 100)) {
//            Service.gI().dropItemMap(this.zone, new ItemMap(zone, NRs[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
//        }
//    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        super.dispose();
    }

}

package com.monzy.models.boss.list_boss;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossData;
import com.monzy.models.boss.BossManager;
import com.monzy.models.map.ItemMap;
import com.monzy.models.map.Zone;
import com.monzy.models.player.Player;
import com.monzy.services.Service;
import com.monzy.utils.Util;

/**
 * @Stole By Arriety
 */
public class NhanBan extends Boss {

    public NhanBan(int bossID, BossData bossData, Zone zone) throws Exception {
        super(bossID, bossData);
        this.zone = zone;
    }

    @Override
    public void reward(Player plKill) {
        //vật phẩm rơi khi diệt boss nhân bản
        ItemMap it = new ItemMap(this.zone, Util.nextInt(1099, 1103), Util.nextInt(3, 4), this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                this.location.y - 24), plKill.id);
        Service.gI().dropItemMap(this.zone, it);
    }

    @Override
    public void active() {
        super.active();
    }

    @Override
    public void joinMap() {
        super.joinMap();
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }

}

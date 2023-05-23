package com.monzy.models.boss.list_boss.android;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;
import com.monzy.models.map.ItemMap;
import com.monzy.models.player.Player;
import com.monzy.services.PlayerService;
import com.monzy.services.Service;
import com.monzy.services.TaskService;
import com.monzy.utils.Util;

public class Android15 extends Boss {

    public boolean callApk13;

    public Android15() throws Exception {
        super(BossID.ANDROID_15, BossesData.ANDROID_15);
    }

    @Override
    public void reward(Player plKill) {
        int[] itemRan = new int[]{1230, 382, 383, 384, 1142};
        int itemId = itemRan[2];
        if (Util.isTrue(15, 100)) {
            ItemMap it = new ItemMap(this.zone, itemId, 17, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    protected void resetBase() {
        super.resetBase();
        this.callApk13 = false;
    }

    @Override
    public void active() {
        this.attack();
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.callApk13 && damage >= this.nPoint.hp) {
            if (this.parentBoss != null) {
                ((Android14) this.parentBoss).callApk13();
            }
            return 0;
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    public void recoverHP() {
        PlayerService.gI().hoiPhuc(this, this.nPoint.hpMax, 0);
    }

}
/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */

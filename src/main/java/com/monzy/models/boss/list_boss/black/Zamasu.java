package com.monzy.models.boss.list_boss.black;

import com.monzy.consts.ConstTask;
import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;
import com.monzy.models.map.ItemMap;
import com.monzy.models.player.Player;
import com.monzy.services.Service;
import com.monzy.services.TaskService;

public class Zamasu extends Boss {

    public Zamasu() throws Exception {
        super(BossID.ZAMASU, BossesData.ZAMASU, BossesData.THAN_ZAMASU);
    }

    @Override
    public void reward(Player plKill) {
        rewardDTL(plKill);
        rewardItem(plKill, 1142, 16);
        if (TaskService.gI().isCurrentTask(plKill, ConstTask.TASK_31_2) && this.name.equals("Zamasu"))
            Service.gI().dropItemMap(this.zone, new ItemMap(this.zone, 874, 1, plKill.location.x, plKill.location.y, plKill.id));
        else if (TaskService.gI().isCurrentTask(plKill, ConstTask.TASK_31_3) && this.name.equals("Thần Zamasu"))
            Service.gI().dropItemMap(this.zone, new ItemMap(this.zone, 725, 1, plKill.location.x, plKill.location.y, plKill.id));
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        return super.injured(plAtt, damage / 2, piercing, isMobAttack);
    }

}























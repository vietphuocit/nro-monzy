package com.monzy.models.boss.list_boss.black;

import com.monzy.consts.ConstTask;
import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;
import com.monzy.models.map.ItemMap;
import com.monzy.models.player.Player;
import com.monzy.services.Service;
import com.monzy.services.TaskService;

public class Black extends Boss {

    public Black() throws Exception {
        super(BossID.BLACK, BossesData.BLACK_GOKU, BossesData.SUPER_BLACK_GOKU);
    }

    @Override
    public void reward(Player plKill) {
        super.reward(plKill);
        rewardDTL(plKill);
        if (TaskService.gI().isCurrentTask(plKill, ConstTask.TASK_31_0) && this.name.equals("Black Goku"))
            Service.gI().dropItemMap(this.zone, new ItemMap(this.zone, 992, 1, plKill.location.x, plKill.location.y, plKill.id));
        else if (TaskService.gI().isCurrentTask(plKill, ConstTask.TASK_31_1) && this.name.equals("Super Black Goku"))
            Service.gI().dropItemMap(this.zone, new ItemMap(this.zone, 865, 1, plKill.location.x, plKill.location.y, plKill.id));
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        return super.injured(plAtt, damage / 2, piercing, isMobAttack);
    }

}























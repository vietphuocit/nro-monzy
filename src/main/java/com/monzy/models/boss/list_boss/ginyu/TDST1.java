package com.monzy.models.boss.list_boss.ginyu;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossStatus;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;
import com.monzy.services.Service;
import com.monzy.utils.Util;

public class TDST1 extends Boss {

    public TDST1() throws Exception {
        super(BossID.TDST_1, BossesData.SO_4_1, BossesData.SO_3_1, BossesData.SO_2_1, BossesData.SO_1_1, BossesData.TIEU_DOI_TRUONG_1);
    }

    @Override
    public void moveTo(int x, int y) {
        if (this.currentLevel == 1) {
            return;
        }
        super.moveTo(x, y);
    }

    @Override
    public void reward(Player plKill) {
        plKill.inventory.ruby += 50;
        Service.gI().sendMoney(plKill);
        Service.gI().sendThongBao(plKill, "Bạn vừa nhận được 50 hồng ngọc ");
    }

    @Override
    protected void notifyJoinMap() {
        if (this.currentLevel == 1) {
            return;
        }
        super.notifyJoinMap();
    }

    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 300000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    private long st;

}
/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */

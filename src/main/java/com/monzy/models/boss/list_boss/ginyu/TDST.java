package com.monzy.models.boss.list_boss.ginyu;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossStatus;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;
import com.monzy.utils.Util;

public class TDST extends Boss {

    public TDST() throws Exception {
        super(BossID.TDST, BossesData.SO_4, BossesData.SO_3, BossesData.SO_2, BossesData.SO_1, BossesData.TIEU_DOI_TRUONG);
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
        super.reward(plKill);
        if (this.currentLevel == 1) {
            return;
        }
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

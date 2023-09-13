package com.monzy.models.boss.list_boss.dhvt;

import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossManager;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;
import com.monzy.services.EffectSkillService;
import com.monzy.utils.Logger;
import com.monzy.utils.Util;

public class ThienXinHang extends BossDHVT {

    private long lastTimePhanThan = System.currentTimeMillis();

    public ThienXinHang(Player player) throws Exception {
        super(BossID.THIEN_XIN_HANG, BossesData.THIEN_XIN_HANG);
        this.playerAtt = player;
    }

    @Override
    public void attack() {
        super.attack();
        try {
            EffectSkillService.gI().removeStun(this);
            if (Util.canDoWithTime(lastTimePhanThan, 30000)) {
                lastTimePhanThan = System.currentTimeMillis();
                phanThan();
            }
        } catch (Exception e) {
            Logger.logException(ThienXinHang.class, e);
        }
    }

    private void phanThan() {
        try {
            new ThienXinHangClone(BossID.THIEN_XIN_HANG_CLONE, playerAtt);
            new ThienXinHangClone(BossID.THIEN_XIN_HANG_CLONE1, playerAtt);
            new ThienXinHangClone(BossID.THIEN_XIN_HANG_CLONE2, playerAtt);
            new ThienXinHangClone(BossID.THIEN_XIN_HANG_CLONE3, playerAtt);
        } catch (Exception e) {
            Logger.logException(ThienXinHang.class, e);
        }
    }

}

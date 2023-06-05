package com.monzy.models.boss.list_boss.tdst;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;

public class TDST extends Boss {

    public TDST() throws Exception {
        super(BossID.TDST, BossesData.SO_4, BossesData.SO_3, BossesData.SO_2, BossesData.SO_1, BossesData.TIEU_DOI_TRUONG);
    }

}

package com.monzy.models.player;

import com.monzy.services.MapService;
import com.monzy.services.Service;

public class FightMabu {

    public final byte POINT_MAX = 20;
    private final Player player;
    public int pointMabu = 0;

    public FightMabu(Player player) {
        this.player = player;
    }

    public void changePoint(byte pointAdd) {
        if (MapService.gI().isMapMaBu(player.zone.map.mapId)) {
            pointMabu += pointAdd;
            if (pointMabu >= POINT_MAX) {
                Service.gI().sendThongBao(player, "Bạn đã đủ điểm lên tầng tiếp theo");
            }
        }
    }

    public void clear() {
        this.pointMabu = 0;
    }

}

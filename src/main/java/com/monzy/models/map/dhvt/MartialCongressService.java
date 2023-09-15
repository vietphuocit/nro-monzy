package com.monzy.models.map.dhvt;

import com.monzy.models.map.Zone;
import com.monzy.models.npc.NpcManager;
import com.monzy.models.player.Player;
import com.monzy.services.ChangeMapService;
import com.monzy.services.MapService;
import com.monzy.services.Service;
import com.monzy.utils.Logger;
import com.network.io.Message;

public class MartialCongressService {

    private static MartialCongressService i;

    public static MartialCongressService gI() {
        if (i == null) {
            i = new MartialCongressService();
        }
        return i;
    }

    public void startChallenge(Player player) {
        Zone zone = getMapChalllenge(129);
        if (zone != null) {
            ChangeMapService.gI().changeMap(player, zone, player.location.x, 360);
            setTimeout(() -> {
                MartialCongress mc = new MartialCongress();
                mc.setPlayer(player);
                mc.setNpc(NpcManager.getNpcsByMapPlayer(player).get(0));
                mc.toTheNextRound();
                MartialCongressManager.gI().add(mc);
                Service.gI().sendThongBao(player, "Số thứ tự của ngươi là 1\n chuẩn bị thi đấu nhé");
            }, 500);
        }
    }

    public static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                Logger.logException(MartialCongressService.class, e);
            }
        }).start();
    }

    public void moveFast(Player pl, int x, int y) {
        Message msg;
        try {
            msg = new Message(58);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeInt((int) pl.id);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(MartialCongressService.class, e);
        }
    }

    public void sendTypePK(Player player, Player boss) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 35);
            msg.writer().writeInt((int) boss.id);
            msg.writer().writeByte(3);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(MartialCongressService.class, e);
        }
    }

    public Zone getMapChalllenge(int mapId) {
        Zone map = MapService.gI().getMapWithRandZone(mapId);
        if (map.getBosses().isEmpty()) {
            return map;
        }
        return null;
    }

}

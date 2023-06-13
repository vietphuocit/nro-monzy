package com.monzy.models.npc.specialnpc;

import com.monzy.models.player.Player;
import com.monzy.services.PetService;
import com.monzy.services.Service;
import com.monzy.services.ChangeMapService;
import com.monzy.utils.Logger;
import com.monzy.utils.Util;
import com.network.io.Message;

public class BillEgg {

    //    private static final long DEFAULT_TIME_DONE = 7776000000L;
    private static final long DEFAULT_TIME_DONE = 86400000L;
    private Player player;
    public long lastTimeCreate;
    public long timeDone;
    private final short id = 50;

    public BillEgg(Player player, long lastTimeCreate, long timeDone) {
        this.player = player;
        this.lastTimeCreate = lastTimeCreate;
        this.timeDone = timeDone;
    }

    public static void createBillEgg(Player player) {
        player.billEgg = new BillEgg(player, System.currentTimeMillis(), DEFAULT_TIME_DONE);
    }

    public void sendBillEgg() {
        Message msg;
        try {
            msg = new Message(-122);
            msg.writer().writeShort(this.id);
            msg.writer().writeByte(1);
            msg.writer().writeShort(15073);
            msg.writer().writeByte(0);
            msg.writer().writeInt(this.getSecondDone());
            this.player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(BillEgg.class, e);
        }
    }

    public int getSecondDone() {
        int seconds = (int) ((lastTimeCreate + timeDone - System.currentTimeMillis()) / 1000);
        return seconds > 0 ? seconds : 0;
    }

    public void openEgg(int gender) {
        if (this.player.pet != null) {
            try {
                destroyEgg();
                Thread.sleep(4000);
                if (this.player.pet == null) {
                    PetService.gI().createBerusPet(this.player, gender);
                } else {
                    PetService.gI().changeBerusPet(this.player, gender);
                }
                ChangeMapService.gI().changeMapInYard(this.player, this.player.gender * 7, -1, Util.nextInt(300, 500));
                player.billEgg = null;
            } catch (Exception e) {
            }
        } else {
            Service.gI().sendThongBao(player, "Yêu cầu phải có đệ tử");
        }
    }

    public void destroyEgg() {
        try {
            Message msg = new Message(-117);
            msg.writer().writeByte(101);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
        this.player.billEgg = null;
    }

    public void subTimeDone(int d, int h, int m, int s) {
        this.timeDone -= ((d * 24 * 60 * 60 * 1000) + (h * 60 * 60 * 1000) + (m * 60 * 1000) + (s * 1000));
        this.sendBillEgg();
    }

    public void dispose() {
        this.player = null;
    }

}

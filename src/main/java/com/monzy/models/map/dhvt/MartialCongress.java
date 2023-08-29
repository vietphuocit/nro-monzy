package com.monzy.models.map.dhvt;

import com.monzy.consts.ConstPlayer;
import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossStatus;
import com.monzy.models.boss.list_boss.dhvt.*;
import com.monzy.models.npc.Npc;
import com.monzy.models.player.Player;
import com.monzy.services.*;
import lombok.Data;

@Data
public class MartialCongress {

    private Player player;
    private Boss boss;
    private Npc npc;
    private int time;
    private int round;
    private int timeWait;

    public void update() {
        if (time > 0) {
            time--;
            if (player.isDie()) {
                die();
                return;
            }
            if (player.location != null && !player.isDie() && player != null && player.zone != null) {
                if (boss.isDie()) {
                    round++;
                    boss.leaveMap();
                    toTheNextRound();
                }
                if (player.location.y > 264) {
                    leave();
                }
            } else {
                endChallenge();
                if (boss != null) {
                    boss.leaveMap();
                }
                MartialCongressManager.gI().remove(this);
            }
        } else {
            timeOut();
        }
        if (timeWait > 0) {
            switch (timeWait) {
                case 10:
                    npc.npcChat("Trận đấu giữa " + player.name + " VS " + boss.name + " sắp diễn ra");
                    ready();
                    break;
                case 8:
                    npc.npcChat("Xin quý vị khán giả cho 1 tràng pháo tay để cổ vũ cho 2 đối thủ nào");
                    break;
                case 4:
                    npc.npcChat("Mọi người ngồi sau hãy ổn định chỗ ngồi, trận đấu sẽ bắt đầu sau 3 giây nữa");
                    break;
                case 2:
                    npc.npcChat("Trận đấu bắt đầu");
                    break;
                case 1:
                    Service.gI().chat(player, "Ok");
                    Service.gI().chat(boss, "Ok");
                    break;
            }
            timeWait--;
        }
    }

    public void ready() {
        EffectSkillService.gI().startStun(boss, System.currentTimeMillis(), 10000);
        EffectSkillService.gI().startStun(player, System.currentTimeMillis(), 10000);
        ItemTimeService.gI().sendItemTime(player, 3779, 10000 / 1000);
        MartialCongressService.setTimeout(() -> {
            if (boss.effectSkill != null) {
                EffectSkillService.gI().removeStun(boss);
            }
            MartialCongressService.gI().sendTypePK(player, boss);
            PlayerService.gI().changeAndSendTypePK(this.player, ConstPlayer.PK_PVP);
            boss.changeStatus(BossStatus.ACTIVE);
        }, 10000);
    }

    public void toTheNextRound() {
        try {
            PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
            Boss boss;
            switch (round) {
                case 0:
                    boss = new SoiHecQuyn(player);
                    break;
                case 1:
                    boss = new ODo(player);
                    break;
                case 2:
                    boss = new Xinbato(player);
                    break;
                case 3:
                    boss = new ChaPa(player);
                    break;
                case 4:
                    boss = new PonPut(player);
                    break;
                case 5:
                    boss = new ChanXu(player);
                    break;
                case 6:
                    boss = new TauPayPay(player);
                    break;
                case 7:
                    boss = new Yamcha(player);
                    break;
                case 8:
                    boss = new JackyChun(player);
                    break;
                case 9:
                    boss = new ThienXinHang(player);
                    break;
                case 10:
                    boss = new LiuLiu(player);
                    break;
                default:
                    champion();
                    return;
            }
            MartialCongressService.gI().moveFast(player, 335, 264);
            setTimeWait(11);
            setBoss(boss);
            setTime(185);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (round > 0 && round < 11) {
//            bss.joinMap();
//        }
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setTimeWait(int timeWait) {
        this.timeWait = timeWait;
    }

    private void die() {
        Service.gI().sendThongBao(player, "Bạn bị xử thua vì chết queo");
        if (player.zone != null) {
            endChallenge();
        }
    }

    private void timeOut() {
        Service.gI().sendThongBao(player, "Bạn bị xử thua vì hết thời gian");
        endChallenge();
    }

    private void champion() {
        Service.gI().sendThongBao(player, "Chúc mừng " + player.name + " vừa đoạt giải vô địch");
        endChallenge();
    }

    public void leave() {
        setTime(0);
        EffectSkillService.gI().removeStun(player);
        Service.gI().sendThongBao(player, "Bạn bị xử thua vì rời khỏi võ đài");
        endChallenge();
    }

    private void reward() {
        if (player.levelWoodChest < round) {
            player.levelWoodChest = round;
        }
    }

    public void endChallenge() {
        reward();
        if (player.zone != null) {
            PlayerService.gI().hoiSinh(player);
        }
        PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
        if (player != null && player.zone != null && player.zone.map.mapId == 129) {
            MartialCongressService.setTimeout(() -> {
                ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
            }, 500);
        }
        if (boss != null) {
            boss.leaveMap();
        }
        MartialCongressManager.gI().remove(this);
    }

}

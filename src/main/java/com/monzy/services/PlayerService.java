package com.monzy.services;

import com.database.Database;
import com.monzy.models.player.Player;
import com.monzy.server.Client;
import com.monzy.utils.Logger;
import com.monzy.utils.Util;
import com.network.io.Message;

public class PlayerService {

    private static final int COST_GOLD_HOI_SINH = 10000000;
    private static final int COST_GEM_HOI_SINH = 10;
    private static final int COST_GOLD_HOI_SINH_NRSD = 20000000;
    private static final int COST_GOLD_HOI_SINH_PVP = 200000000;
    private static PlayerService i;

    public PlayerService() {
    }

    public static PlayerService gI() {
        if (i == null) {
            i = new PlayerService();
        }
        return i;
    }

    public void sendTNSM(Player player, byte type, long param) {
        if (param > 0) {
            Message msg;
            try {
                msg = new Message(-3);
                msg.writer().writeByte(type);// 0 là cộng sm, 1 cộng tn, 2 là cộng cả 2
                msg.writer().writeInt((int) param);// số tn cần cộng
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    public void sendMessageAllPlayer(Message msg) {
        for (Player pl : Client.gI().getPlayers()) {
            if (pl != null) {
                pl.sendMessage(msg);
            }
        }
        msg.cleanup();
    }

    public void sendMessageIgnore(Player plIgnore, Message msg) {
        for (Player pl : Client.gI().getPlayers()) {
            if (pl != null && !pl.equals(plIgnore)) {
                pl.sendMessage(msg);
            }
        }
        msg.cleanup();
    }

    public void sendInfoHp(Player player) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 5);
            msg.writer().writeInt(player.nPoint.hp);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(PlayerService.class, e);
        }
    }

    public void sendInfoMp(Player player) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 6);
            msg.writer().writeInt(player.nPoint.mp);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(PlayerService.class, e);
        }
    }

    public void sendInfoHpMp(Player player) {
        sendInfoHp(player);
        sendInfoMp(player);
    }

    public void hoiPhuc(Player player, int hp, int mp) {
        if (!player.isDie()) {
            player.nPoint.addHp(hp);
            player.nPoint.addMp(mp);
            Service.gI().Send_Info_NV(player);
            if (!player.isPet && !player.isNewPet) {
                PlayerService.gI().sendInfoHpMp(player);
            }
        }
    }

    public void sendInfoHpMpMoney(Player player) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 4);
            try {
                if (player.getSession().version >= 214) {
                    msg.writer().writeLong(player.inventory.gold);
                } else {
                    msg.writer().writeInt((int) player.inventory.gold);
                }
            } catch (Exception e) {
                msg.writer().writeInt((int) player.inventory.gold);
            }
            msg.writer().writeInt(player.inventory.gem);//luong
            msg.writer().writeInt(player.nPoint.hp);//chp
            msg.writer().writeInt(player.nPoint.mp);//cmp
            msg.writer().writeInt(player.inventory.ruby);//ruby
            player.sendMessage(msg);
        } catch (Exception e) {
            Logger.logException(PlayerService.class, e);
        }
    }

    public void playerMove(Player player, int x, int y) {
        if (player.zone == null) {
            return;
        }
        if (!player.isDie()) {
            if (player.effectSkill.isCharging) {
                EffectSkillService.gI().stopCharge(player);
            }
            if (player.effectSkill.useTroi) {
                EffectSkillService.gI().removeUseTroi(player);
            }
            player.location.x = x;
            player.location.y = y;
            player.location.lastTimeplayerMove = System.currentTimeMillis();
//            switch (player.zone.map.mapId) {
//                case 85:
//                case 86:
//                case 87:
//                case 88:
//                case 89:
//                case 90:
//                case 91:
//                    if (!player.isBoss && !player.isPet) {
//                        if (x < 24 || x > player.zone.map.mapWidth - 24 || y < 0 || y > player.zone.map.mapHeight - 24) {
//                            if (MapService.gI().getWaypointPlayerIn(player) == null && !MapService.gI().isMapBlackBallWar(player.zone.map.mapId)) {
//                                ChangeMapService.gI().changeMap(player, 21 + player.gender, 0, 200, 336);
//                                return;
//                            }
//                        }
//                        int yTop = player.zone.map.yPhysicInTop(player.location.x, player.location.y);
//                        if (yTop >= player.zone.map.mapHeight - 24) {
//                            ChangeMapService.gI().changeMap(player, 21 + player.gender, 0, 200, 336);
//                            return;
//                        }
//                    }
//                    break;
//            }
            if (player.pet != null) {
                player.pet.followMaster();
            }
            if (player.newpet != null) {
                player.newpet.followMaster();
            }
            MapService.gI().sendPlayerMove(player);
            TaskService.gI().checkDoneTaskGoToMap(player, player.zone);
        }
    }

    public void sendCurrentStamina(Player player) {
        Message msg;
        try {
            msg = new Message(-68);
            msg.writer().writeShort(player.nPoint.stamina);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(PlayerService.class, e);
        }
    }

    public void sendMaxStamina(Player player) {
        Message msg;
        try {
            msg = new Message(-69);
            msg.writer().writeShort(player.nPoint.maxStamina);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(PlayerService.class, e);
        }
    }

    public void changeAndSendTypePK(Player player, int type) {
        changeTypePK(player, type);
        sendTypePk(player);
    }

    public void changeTypePK(Player player, int type) {
        player.typePk = (byte) type;
    }

    public void sendTypePk(Player player) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 35);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeByte(player.typePk);
            Service.gI().sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void quaVip1(Player playerVip1) {
        try {
            Database.executeUpdate("update account set vip1 = 1 where id = ? and username = ?", playerVip1.getSession().userId, playerVip1.getSession().uu);
        } catch (Exception e) {
        }
        Service.getInstance().sendThongBao(playerVip1, "Bạn là Vip1, quà đã được đặt trong hành trang!, cảm ơn bạn đã đóng góp");
        playerVip1.iDMark.setVip1(true);
    }

    public void quaVip2(Player playerVip2) {
        try {
            Database.executeUpdate("update account set vip2 = 1 where id = ? and username = ?", playerVip2.getSession().userId, playerVip2.getSession().uu);
        } catch (Exception e) {
        }
        Service.getInstance().sendThongBao(playerVip2, "Bạn là Vip2, quà đã được đặt trong hành trang!, cảm ơn bạn đã đóng góp");
        playerVip2.iDMark.setVip2(true);
    }

    public void quaVip3(Player playerVip3) {
        try {
            Database.executeUpdate("update account set vip3 = 1 where id = ? and username = ?", playerVip3.getSession().userId, playerVip3.getSession().uu);
        } catch (Exception e) {
        }
        Service.getInstance().sendThongBao(playerVip3, "Bạn là Vip3, quà đã được đặt trong hành trang!, cảm ơn bạn đã đóng góp");
        playerVip3.iDMark.setVip3(true);
    }

    public void quaVip4(Player playerVip4) {
        try {
            Database.executeUpdate("update account set vip4 = 1 where id = ? and username = ?", playerVip4.getSession().userId, playerVip4.getSession().uu);
        } catch (Exception e) {
        }
        Service.getInstance().sendThongBao(playerVip4, "Bạn là Vip4, quà đã được đặt trong hành trang!, cảm ơn bạn đã đóng góp");
        playerVip4.iDMark.setVip4(true);
    }

    public void quaVip5(Player playerVip5) {
        try {
            Database.executeUpdate("update account set vip5 = 1 where id = ? and username = ?", playerVip5.getSession().userId, playerVip5.getSession().uu);
        } catch (Exception e) {
        }
        Service.getInstance().sendThongBao(playerVip5, "Bạn là Vip5, quà đã được đặt trong hành trang!, cảm ơn bạn đã đóng góp");
        playerVip5.iDMark.setVip5(true);
    }

    public void banPlayer(Player playerBaned) {
        try {
            Database.executeUpdate("update account set ban = 1 where id = ? and username = ?", playerBaned.getSession().userId, playerBaned.getSession().uu);
        } catch (Exception e) {
        }
        Service.gI().sendThongBao(playerBaned, "Tài khoản của bạn đã bị khóa\nGame sẽ mất kết nối sau 5 giây...");
        playerBaned.iDMark.setLastTimeBan(System.currentTimeMillis());
        playerBaned.iDMark.setBan(true);
    }

    public void hoiSinh(Player player) {
        if (player.isDie()) {
            boolean canHs = false;
            if (MapService.gI().isMapBlackBallWar(player.zone.map.mapId)) {
                if (player.inventory.gold >= COST_GOLD_HOI_SINH_NRSD) {
                    player.inventory.gold -= COST_GOLD_HOI_SINH_NRSD;
                    canHs = true;
                } else {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện, còn thiếu " + Util.numberToMoney(COST_GOLD_HOI_SINH_NRSD - player.inventory.gold) + " vàng");
                    return;
                }
            } else if (MapService.gI().isMapBanDoKhoBau(player.zone.map.mapId) || MapService.gI().isNguHS(player.zone.map.mapId)) {
                Service.getInstance().sendThongBao(player, "Không thể hồi sinh trong khu vực này");
                return;
            } else if (MapService.gI().isMapPVP(player.zone.map.mapId)) {
                if (player.inventory.gold >= COST_GOLD_HOI_SINH_PVP) {
                    player.inventory.gold -= COST_GOLD_HOI_SINH_PVP;
                    canHs = true;
                } else {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện, còn thiếu " + Util.numberToMoney(COST_GOLD_HOI_SINH_PVP - player.inventory.gold) + " vàng");
                    return;
                }
            } else {
                if (player.inventory.gem >= COST_GEM_HOI_SINH) {
                    player.inventory.gem -= COST_GEM_HOI_SINH;
                    canHs = true;
                } else {
                    Service.gI().sendThongBao(player, "?");
                    return;
                }
            }
            if (canHs) {
                Service.gI().sendMoney(player);
                Service.gI().hsChar(player, player.nPoint.hpMax, player.nPoint.mpMax);
            }
        }
    }

    public void hoiSinhMaBu(Player player) {
        if (player.isDie()) {
            boolean canHs = false;
            if (MapService.gI().isMapMaBu(player.zone.map.mapId)) {
                if (player.inventory.gold >= COST_GOLD_HOI_SINH_NRSD) {
                    player.inventory.gold -= COST_GOLD_HOI_SINH_NRSD;
                    canHs = true;
                } else {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện, còn thiếu " + Util.numberToMoney(COST_GOLD_HOI_SINH_NRSD - player.inventory.gold) + " vàng");
                    return;
                }
            } else {
                if (player.inventory.gold >= COST_GOLD_HOI_SINH) {
                    player.inventory.gold -= COST_GOLD_HOI_SINH;
                    canHs = true;
                } else {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện, còn thiếu " + Util.numberToMoney(COST_GOLD_HOI_SINH - player.inventory.gold) + " vàng");
                    return;
                }
            }
            if (canHs) {
                Service.gI().sendMoney(player);
                Service.gI().hsChar(player, player.nPoint.hpMax, player.nPoint.mpMax);
            }
        }
    }

}

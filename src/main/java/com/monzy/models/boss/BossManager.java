package com.monzy.models.boss;

import com.monzy.models.boss.list_boss.AnTrom;
import com.monzy.models.boss.list_boss.black.Black;
import com.monzy.models.boss.list_boss.cooler.Cooler;
import com.monzy.models.boss.list_boss.android.*;
import com.monzy.models.boss.list_boss.cell.*;
import com.monzy.models.boss.list_boss.doraemon.*;
import com.monzy.models.boss.list_boss.fide.Fide;
import com.monzy.models.boss.list_boss.nappa.Kuku;
import com.monzy.models.boss.list_boss.nappa.MapDauDinh;
import com.monzy.models.boss.list_boss.nappa.Rambo;
import com.monzy.models.boss.list_boss.nrden.*;
import com.monzy.models.boss.list_boss.tdst.TDST;
import com.monzy.models.boss.list_boss.tdst.TDST1;
import com.monzy.models.player.Player;
import com.monzy.server.ServerManager;
import com.monzy.services.ItemMapService;
import com.network.io.Message;

import java.util.ArrayList;
import java.util.List;

public class BossManager implements Runnable {

    private static BossManager I;
    public static final byte ratioReward = 2;

    public static BossManager gI() {
        if (BossManager.I == null) {
            BossManager.I = new BossManager();
        }
        return BossManager.I;
    }

    private BossManager() {
        this.bosses = new ArrayList<>();
    }

    private boolean loadedBoss;
    private final List<Boss> bosses;

    public void addBoss(Boss boss) {
        this.bosses.add(boss);
    }

    public void removeBoss(Boss boss) {
        this.bosses.remove(boss);
    }

    public void loadBoss() {
        if (this.loadedBoss) {
            return;
        }
        try {
/**
 this.createBoss(BossID.KAMIRIN);
 this.createBoss(BossID.KAMILOC);
 this.createBoss(BossID.KAMI_SOOME);
 this.createBoss(BossID.CUMBERBLACK);
 this.createBoss(BossID.CUMBERYELLOW);
 this.createBoss(BossID.SUPER_XEN);
 this.createBoss(BossID.SONGOKU_TA_AC);
 this.createBoss(BossID.CUMBER);
 this.createBoss(BossID.COOLER_GOLD);
 this.createBoss(BossID.THIEN_SU_VADOS);
 this.createBoss(BossID.THIEN_SU_WHIS);
 this.createBoss(BossID.THIEN_SU_VADOS);
 this.createBoss(BossID.THIEN_SU_WHIS);
 this.createBoss(BossID.THIEN_SU_VADOS);
 this.createBoss(BossID.THIEN_SU_WHIS);
 this.createBoss(BossID.THIEN_SU_VADOS);
 this.createBoss(BossID.THIEN_SU_WHIS);
 this.createBoss(BossID.ZAMASZIN);
 this.createBoss(BossID.BLACK2);
 this.createBoss(BossID.ZAMASMAX);
 this.createBoss(BossID.BLACK);
 this.createBoss(BossID.BLACK3);
 */
            this.createBoss(BossID.AN_TROM);
            this.createBoss(BossID.AN_TROM);
            this.createBoss(BossID.AN_TROM);
            this.createBoss(BossID.TDST_1);
            this.createBoss(BossID.BROLY);
            this.createBoss(BossID.KUKU);
            this.createBoss(BossID.MAP_DAU_DINH);
            this.createBoss(BossID.RAMBO);
            this.createBoss(BossID.TDST);
            this.createBoss(BossID.FIDE);
            this.createBoss(BossID.ANDROID_14);
            this.createBoss(BossID.KING_KONG);
            this.createBoss(BossID.DR_KORE);
            this.createBoss(BossID.XEN_BO_HUNG);
            this.createBoss(BossID.XEN_CON);
            this.createBoss(BossID.SIEU_BO_HUNG);
            this.createBoss(BossID.XUKA);
            this.createBoss(BossID.COOLER);
            this.createBoss(BossID.BLACK);
            this.createBoss(BossID.BLACK);
            this.createBoss(BossID.BLACK);
//            this.createBoss(BossID.MABU);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.loadedBoss = true;
        new Thread(BossManager.I, "Update boss").start();
    }

    public Boss createBoss(int bossID) {
        try {
            switch (bossID) {
                case BossID.Rong_1Sao:
                    return new Rong1Sao();
                case BossID.Rong_2Sao:
                    return new Rong2Sao();
                case BossID.Rong_3Sao:
                    return new Rong3Sao();
                case BossID.Rong_4Sao:
                    return new Rong4Sao();
                case BossID.Rong_5Sao:
                    return new Rong5Sao();
                case BossID.Rong_6Sao:
                    return new Rong6Sao();
                case BossID.Rong_7Sao:
                    return new Rong7Sao();
                case BossID.AN_TROM:
                    return new AnTrom();
                case BossID.TDST_1:
                    return new TDST1();
                case BossID.KUKU:
                    return new Kuku();
                case BossID.MAP_DAU_DINH:
                    return new MapDauDinh();
                case BossID.RAMBO:
                    return new Rambo();
                case BossID.TDST:
                    return new TDST();
                case BossID.FIDE:
                    return new Fide();
                case BossID.ANDROID_13:
                    return new Android13();
                case BossID.ANDROID_14:
                    return new Android14();
                case BossID.ANDROID_15:
                    return new Android15();
                case BossID.PIC:
                    return new Pic();
                case BossID.POC:
                    return new Poc();
                case BossID.KING_KONG:
                    return new KingKong();
                case BossID.ANDROID_19:
                    return new Android19();
                case BossID.DR_KORE:
                    return new DrKore();
                case BossID.XEN_BO_HUNG:
                    return new XenBoHung();
                case BossID.XEN_CON:
                    return new XenCon();
                case BossID.XEN_CON_1:
                    return new XenCon1();
                case BossID.XEN_CON_2:
                    return new XenCon2();
                case BossID.XEN_CON_3:
                    return new XenCon3();
                case BossID.XEN_CON_4:
                    return new XenCon4();
                case BossID.SIEU_BO_HUNG:
                    return new SieuBoHung();
                case BossID.XUKA:
                    return new Xuka();
                case BossID.NOBITA:
                    return new Nobita();
                case BossID.XEKO:
                    return new Xeko();
                case BossID.CHAIEN:
                    return new Chaien();
                case BossID.DORAEMON:
                    return new Doraemon();
                case BossID.COOLER:
                    return new Cooler();
                case BossID.BLACK:
                    return new Black();
                /**
                 case BossID.CUMBERYELLOW:
                 return new cumberYellow();
                 case BossID.CUMBERBLACK:
                 return new cumberBlack();
                 case BossID.KAMIRIN:
                 return new kamiRin();
                 case BossID.KAMILOC:
                 return new kamiLoc();
                 case BossID.KAMI_SOOME:
                 return new kamiSooMe();
                 case BossID.DRABURA:
                 return new Drabura();
                 case BossID.DRABURA_2:
                 return new Drabura2();
                 case BossID.BUI_BUI:
                 return new BuiBui();
                 case BossID.BUI_BUI_2:
                 return new BuiBui2();
                 case BossID.YA_CON:
                 return new Yacon();
                 case BossID.MABU_12H:
                 return new MabuBoss();
                 case BossID.SUPER_XEN:
                 return new SuperXen();
                 case BossID.SUPER_ANDROID_17:
                 return new SuperAndroid17();
                 case BossID.TRUNG_UY_TRANG:
                 return new TrungUyTrang();
                 case BossID.VUA_COLD:
                 return new Kingcold();
                 case BossID.FIDE_ROBOT:
                 return new FideRobot();
                 case BossID.COOLER:
                 return new Cooler();
                 case BossID.ZAMASMAX:
                 return new ZamasMax();
                 case BossID.ZAMASZIN:
                 return new ZamasKaio();
                 case BossID.BLACK2:
                 return new SuperBlack2();
                 case BossID.BLACK1:
                 return new BlackGokuTL();
                 case BossID.BLACK:
                 return new Black();
                 case BossID.BLACK3:
                 return new BlackGokuBase();
                 case BossID.MABU:
                 return new Mabu();
                 case BossID.COOLER_GOLD:
                 return new CoolerGold();
                 case BossID.CUMBER:
                 return new Cumber();
                 case BossID.THAN_HUY_DIET_CHAMPA:
                 return new Champa();
                 case BossID.THIEN_SU_VADOS:
                 return new Vados();
                 case BossID.THAN_HUY_DIET:
                 return new ThanHuyDiet();
                 case BossID.THIEN_SU_WHIS:
                 return new ThienSuWhis();
                 case BossID.SONGOKU_TA_AC:
                 return new SongokuTaAc();
                 case BossID.BROLY:
                 return new Broly();
                 */
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public boolean existBossOnPlayer(Player player) {
        return player.zone.getBosses().size() > 0;
    }

    public void showListBoss(Player player) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Boss");
            msg.writer().writeByte(((Long) bosses.stream().filter(boss -> boss.zone != null).count()).byteValue());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (boss.zone == null)
                    continue;
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                msg.writer().writeUTF("Sống");
                if (player.isAdmin()) {
                    msg.writer().writeUTF(boss.zone.map.mapName + " (" + boss.zone.map.mapId + ") - khu " + boss.zone.zoneId + "");
                } else {
                    msg.writer().writeUTF(boss.zone.map.mapName + " (" + boss.zone.map.mapId + ") - khu ?");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void callBoss(Player player, int mapId) {
        try {
            if (BossManager.gI().existBossOnPlayer(player) ||
                    player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id)) ||
                    player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                return;
            }
            Boss k = null;
            switch (mapId) {
                case 85:
                    k = BossManager.gI().createBoss(BossID.Rong_1Sao);
                    break;
                case 86:
                    k = BossManager.gI().createBoss(BossID.Rong_2Sao);
                    break;
                case 87:
                    k = BossManager.gI().createBoss(BossID.Rong_3Sao);
                    break;
                case 88:
                    k = BossManager.gI().createBoss(BossID.Rong_4Sao);
                    break;
                case 89:
                    k = BossManager.gI().createBoss(BossID.Rong_5Sao);
                    break;
                case 90:
                    k = BossManager.gI().createBoss(BossID.Rong_6Sao);
                    break;
                case 91:
                    k = BossManager.gI().createBoss(BossID.Rong_7Sao);
                    break;
            }
            if (k != null) {
                k.currentLevel = 0;
                k.joinMapByZone(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boss getBossById(int bossId) {
        return BossManager.gI().bosses.stream().filter(boss -> boss.id == bossId && !boss.isDie()).findFirst().orElse(null);
    }

    public List<Boss> getBosses() {
        return bosses;
    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            try {
                long st = System.currentTimeMillis();
                for (Boss boss : this.bosses) {
                    boss.update();
                }
                Thread.sleep(150 - (System.currentTimeMillis() - st));
            } catch (Exception ignored) {
            }
        }
    }

}

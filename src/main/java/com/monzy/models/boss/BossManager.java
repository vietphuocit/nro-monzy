package com.monzy.models.boss;

import com.monzy.models.boss.list_boss.AnTrom;
import com.monzy.models.boss.list_boss.Mabu;
import com.monzy.models.boss.list_boss.android.*;
import com.monzy.models.boss.list_boss.black.Black;
import com.monzy.models.boss.list_boss.black.Zamasu;
import com.monzy.models.boss.list_boss.cell.*;
import com.monzy.models.boss.list_boss.cooler.Cooler;
import com.monzy.models.boss.list_boss.cooler.Cooler2;
import com.monzy.models.boss.list_boss.doraemon.*;
import com.monzy.models.boss.list_boss.fide.Fide;
import com.monzy.models.boss.list_boss.fide.FideGold;
import com.monzy.models.boss.list_boss.hanhtinhberus.Bill;
import com.monzy.models.boss.list_boss.hanhtinhberus.Whis;
import com.monzy.models.boss.list_boss.nappa.Kuku;
import com.monzy.models.boss.list_boss.nappa.MapDauDinh;
import com.monzy.models.boss.list_boss.nappa.Rambo;
import com.monzy.models.boss.list_boss.nrden.*;
import com.monzy.models.boss.list_boss.tdst.TDST;
import com.monzy.models.boss.list_boss.tdst.TDST1;
import com.monzy.models.player.Player;
import com.monzy.server.ServerManager;
import com.monzy.services.ItemMapService;
import com.monzy.utils.Logger;
import com.network.io.Message;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BossManager implements Runnable {

  public static final byte ratioReward = 2;
  private static BossManager I;
  @Getter
  private final List<Boss> bosses;
  private boolean loadedBoss;

  private BossManager() {
    this.bosses = new ArrayList<>();
  }

  public static BossManager gI() {
    if (BossManager.I == null) {
      BossManager.I = new BossManager();
    }
    return BossManager.I;
  }

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
      this.createBoss(BossID.ZAMASU);
      this.createBoss(BossID.ZAMASU);
      this.createBoss(BossID.ZAMASU);
//      this.createBoss(BossID.BILL);
//      this.createBoss(BossID.MABU);
      this.createBoss(BossID.FIDE_GOLD);
      this.createBoss(BossID.FIDE_GOLD);
      this.createBoss(BossID.FIDE_GOLD);
    } catch (Exception e) {
      Logger.logException(BossManager.class, e);
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
        case BossID.COOLER_2:
          return new Cooler2();
        case BossID.BLACK:
          return new Black();
        case BossID.BILL:
          return new Bill();
        case BossID.WISH:
          return new Whis();
        case BossID.MABU:
          return new Mabu();
        case BossID.ZAMASU:
          return new Zamasu();
        case BossID.FIDE_GOLD:
          return new FideGold();
        default:
          return null;
      }
    } catch (Exception e) {
      return null;
    }
  }

  public boolean existBossOnPlayer(Player player) {
    return !player.zone.getBosses().isEmpty();
  }

  public void showListBoss(Player player) {
    Message msg;
    try {
      List<Boss> bossList =
          bosses.stream().filter(boss -> !boss.isPersonalBoss(boss.data[0].getMapJoin()[0])).collect(Collectors.toList());

      msg = new Message(-96);
      msg.writer().writeByte(0);
      msg.writer().writeUTF("Boss");
      msg.writer().writeByte(bossList.size());
      for (int i = 0; i < bossList.size(); i++) {
        Boss boss = bossList.get(i);
        msg.writer().writeInt(i + 1);
        msg.writer().writeInt((int) boss.id);
        msg.writer().writeShort(boss.data[0].getOutfit()[0]);
        if (player.session.version > 214) {
          msg.writer().writeShort(-1);
        }
        msg.writer().writeShort(boss.data[0].getOutfit()[1]);
        msg.writer().writeShort(boss.data[0].getOutfit()[2]);
        msg.writer().writeUTF(boss.data[0].getName());
        msg.writer()
            .writeUTF(
                boss.isDie()
                    ? "Hồi sinh sau: " + Math.max(0, boss.getTimeRespawn()) + " phút"
                    : "Sống");
        if (player.isAdmin()) {
          msg.writer()
              .writeUTF(
                  boss.isDie()
                      ? ""
                      : boss.zone.map.mapName
                          + " ("
                          + boss.zone.map.mapId
                          + ") - khu "
                          + boss.zone.zoneId
                          + "");
        } else {
          msg.writer()
              .writeUTF(
                  boss.isDie()
                      ? ""
                      : boss.zone.map.mapName + " (" + boss.zone.map.mapId + ") - khu ?");
        }
      }
      player.sendMessage(msg);
      msg.cleanup();
    } catch (Exception e) {
      Logger.logException(BossManager.class, e);
    }
  }

  public synchronized void callBoss(Player player, int mapId) {
    try {
      if (BossManager.gI().existBossOnPlayer(player)
          || player.zone.items.stream()
              .anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
          || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
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
      Logger.logException(BossManager.class, e);
    }
  }

  public Boss getBossById(int bossId) {
    return BossManager.gI().bosses.stream()
        .filter(boss -> boss.id == bossId && !boss.isDie())
        .findFirst()
        .orElse(null);
  }

  @Override
  public void run() {
    while (ServerManager.isRunning) {
      try {
        for (Boss boss : this.bosses) {
          boss.update();
        }
        Thread.sleep(100);
      } catch (Exception e) {
        // Bỏ qua
      }
    }
  }
}

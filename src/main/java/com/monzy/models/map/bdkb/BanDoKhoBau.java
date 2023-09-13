package com.monzy.models.map.bdkb;

import com.monzy.models.boss.list_boss.dhvt.ThienXinHang;
import com.monzy.models.clan.Clan;
import com.monzy.models.map.TrapMap;
import com.monzy.models.map.Zone;
import com.monzy.models.mob.Mob;
import com.monzy.models.player.Player;
import com.monzy.services.*;
import com.monzy.utils.Logger;
import com.monzy.utils.Util;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class BanDoKhoBau implements Runnable {

  private int id;
  private int level;
  private List<Zone> zones;

  private Clan clan;
  private Player player;
  private boolean isOpened;
  private boolean running;
  private long lastTimeUpdate;

  public BanDoKhoBau(int id) {
    this.id = id;
    this.zones = new ArrayList<>();
    this.running = true;
    new Thread(this, "Bản đồ kho báu " + id).start();
  }

  @Override
  public void run() {
    while (running) {
      if (Util.canDoWithTime(lastTimeUpdate, 1000)) {
        update();
        lastTimeUpdate = System.currentTimeMillis();
      }
      try {
        Thread.sleep(1000);
      } catch (Exception e) {
        Logger.logException(BanDoKhoBau.class, e);
      }
    }
  }

  public void update() {
    if (this.isOpened) {
      if (Util.canDoWithTime(this.clan.lastTimeOpenBanDo, BanDoKhoBauService.TIME_BAN_DO_KHO_BAU)) {
        this.finish();
      }
    }
  }

  public void finish() {
    for (Player player : this.clan.membersInGame) {
      if (player.isDie()) PlayerService.gI().hoiSinh(player);
      if (MapService.gI().isMapBanDoKhoBau(player.zone.map.mapId)) {
        Service.gI().sendThongBao(player, "Hang Kho Báu Đã Sập Bạn Đang Được Đưa Ra Ngoài");
        ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1038);
      }
    }
    this.level = 0;
    this.clan.banDoKhoBau = null;
    this.clan = null;
    this.player = null;
    this.isOpened = false;
  }

  public void openBanDoKhoBau(Player player, Clan clan, byte level) {
    this.level = level;
    this.clan = clan;
    this.isOpened = true;
    this.clan.lastTimeOpenBanDo = System.currentTimeMillis();
    this.clan.banDoKhoBau = this;

    resetBanDo();
    ChangeMapService.gI().goToDBKB(player);
    sendTextBanDoKhoBau();
  }

  private void resetBanDo() {
    for (Zone zone : zones) {
      for (TrapMap trap : zone.trapMaps) {
        trap.dame = this.level * 10000;
      }
    }
    for (Zone zone : zones) {
      for (Mob m : zone.mobs) {
        Mob.initMobBanDoKhoBau(m, this.level);
        Mob.hoiSinhMob(m);
      }
    }
  }

  public Zone getMapById(int mapId) {
    for (Zone zone : zones) {
      if (zone.map.mapId == mapId) {
        return zone;
      }
    }
    return null;
  }

  public static void addZone(int idBanDo, Zone zone) {
    BanDoKhoBauService.LIST_BAN_DO_KHO_BAU.get(idBanDo).zones.add(zone);
  }

  private void sendTextBanDoKhoBau() {
    for (Player pl : this.clan.membersInGame) {
      ItemTimeService.gI().sendTextBanDoKhoBau(pl);
    }
  }
}

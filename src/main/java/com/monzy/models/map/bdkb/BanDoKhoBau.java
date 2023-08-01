package com.monzy.models.map.bdkb;

import com.monzy.models.clan.Clan;
import com.monzy.models.map.TrapMap;
import com.monzy.models.map.Zone;
import com.monzy.models.mob.Mob;
import com.monzy.models.player.Player;
import com.monzy.services.*;
import com.monzy.utils.Util;
import java.util.ArrayList;
import java.util.List;

public class BanDoKhoBau implements Runnable {

  public int id;
  public byte level;
  public final List<Zone> zones;

  public Clan clan;
  public boolean isOpened;
  private long lastTimeOpen;
  private boolean running;
  private long lastTimeUpdate;

  public BanDoKhoBau(int id) {
    this.id = id;
    this.zones = new ArrayList<>();
    this.running = true;
    new Thread(this, "Ban do kho bau").start();
  }

  @Override
  public void run() {
    while (running) {
      try {
        Thread.sleep(10000);
        if (Util.canDoWithTime(lastTimeUpdate, 10000)) {
          update();
          lastTimeUpdate = System.currentTimeMillis();
        }
      } catch (Exception ignored) {
      }
    }
  }

  public void update() {
    for (BanDoKhoBau bando : BanDoKhoBauService.LIST_BAN_DO_KHO_BAU) {
      if (bando.isOpened) {
        if (Util.canDoWithTime(lastTimeOpen, BanDoKhoBauService.TIME_BAN_DO_KHO_BAU)) {
          this.finish();
        }
      }
    }
  }

  public void openBanDoKhoBau(Player plOpen, Clan clan, byte level) {
    this.level = level;
    this.lastTimeOpen = System.currentTimeMillis();
    this.isOpened = true;
    this.clan = clan;
    this.clan.timeOpenBanDoKhoBau = this.lastTimeOpen;
    this.clan.playerOpenBanDoKhoBau = plOpen;
    this.clan.banDoKhoBau = this;

    resetBanDo();
    ChangeMapService.gI().goToDBKB(plOpen);
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

  public void finish() {
    List<Player> playerInBDKB = new ArrayList<>();
    for (Zone zone : zones) {
      List<Player> players = zone.getPlayers();
      for (Player pl : players) {
        playerInBDKB.add(pl);
        kickOutOfBDKB(pl);
      }
    }
    for (Player pl : playerInBDKB) {
      ChangeMapService.gI().changeMapBySpaceShip(pl, 5, -1, 64);
    }

    this.clan.banDoKhoBau = null;
    this.clan = null;
    this.isOpened = false;
  }

  private void kickOutOfBDKB(Player player) {
    if (MapService.gI().isMapBanDoKhoBau(player.zone.map.mapId)) {
      Service.gI().sendThongBao(player, "Hang Kho Báu Đã Sập Bạn Đang Được Đưa Ra Ngoài");
      ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1038);
      running = false;
      this.clan.banDoKhoBau = null;
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

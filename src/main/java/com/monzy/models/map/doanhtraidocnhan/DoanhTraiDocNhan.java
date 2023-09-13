package com.monzy.models.map.doanhtraidocnhan;

import com.monzy.models.boss.list_boss.doanh_trai.BossDoanhTrai;
import com.monzy.models.clan.Clan;
import com.monzy.models.map.Zone;
import com.monzy.models.mob.Mob;
import com.monzy.models.player.Player;
import com.monzy.services.*;
import com.monzy.utils.Logger;
import com.monzy.utils.Util;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Data;

@Data
public class DoanhTraiDocNhan implements Runnable {

  private int id;
  private List<Zone> zones;

  private Clan clan;
  private Player player;
  private boolean isOpened;
  private boolean running;
  private long lastTimeUpdate;

  public DoanhTraiDocNhan(int id) {
    this.id = id;
    this.zones = new ArrayList<>();
    this.running = true;
    new Thread(this, "Doanh trại độc nhãn " + id).start();
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
        Logger.logException(DoanhTraiDocNhan.class, e);
      }
    }
  }

  public void update() {
    if (this.isOpened) {
      if (Util.canDoWithTime(this.clan.lastTimeOpenDTDN, DoanhTraiDocNhanService.TIME_DOANH_TRAI)) {
        this.finish();
      }
    }
  }

  public void finish() {
    for (Player player : this.clan.membersInGame) {
      if (player.isDie()) PlayerService.gI().hoiSinh(player);
      if (MapService.gI().isMapDoanhTrai(player.zone.map.mapId)) {
        Service.gI().sendThongBao(player, "Kết thúc doanh trại");
        ChangeMapService.gI().changeMapBySpaceShip(player, 21 + player.gender, -1, 500);
      }
    }
    this.clan.doanhTraiDocNhan = null;
    this.clan = null;
    this.player = null;
    this.isOpened = false;
  }

  public void openDoanhTrai(Player player) {
    this.clan = player.clan;
    this.isOpened = true;
    this.clan.lastTimeOpenDTDN = System.currentTimeMillis();
    this.clan.doanhTraiDocNhan = this;

    resetDoanhTrai();
    for (Player pl : player.clan.membersInGame) {
      if (pl == null || pl.zone == null || !player.zone.equals(pl.zone)) {
        continue;
      }
      ChangeMapService.gI().changeMapInYard(pl, 53, -1, 60);
    }
    sendTextDoanhTrai();
  }

  private void resetDoanhTrai() {
    int maxDame = this.clan.membersInGame.stream().max(Comparator.comparingInt(Player::getHPMax)).get().getSDMax();
    int maxHp = this.clan.membersInGame.stream().max(Comparator.comparingInt(Player::getHPMax)).get().getHPMax();
    // Hồi sinh quái
    for (Zone zone : this.zones) {
      for (Mob mob : zone.mobs) {
        mob.point.dame = maxHp / 20;
        mob.point.maxHp = maxDame * 20;
        mob.hoiSinh();
      }
    }
    // Hủy item map
    for (Zone zone : this.zones) {
      zone.items.clear();
    }
    // Hồi sinh boss
    try {
      new BossDoanhTrai(this, maxHp / 10, maxDame * 40);
    } catch (Exception e) {
      Logger.logException(DoanhTraiDocNhan.class, e);
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
    DoanhTraiDocNhanService.DOANH_TRAI_DOC_NHAN_LIST.get(idBanDo).zones.add(zone);
  }

  private void sendTextDoanhTrai() {
    for (Player pl : this.clan.membersInGame) {
      ItemTimeService.gI().sendTextDoanhTrai(pl);
    }
  }
}

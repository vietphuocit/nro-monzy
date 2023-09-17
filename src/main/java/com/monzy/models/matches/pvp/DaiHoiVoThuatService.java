package com.monzy.models.matches.pvp;

import com.monzy.models.map.Map;
import com.monzy.models.map.Zone;
import com.monzy.models.player.Player;
import com.monzy.server.Manager;
import com.monzy.services.ChangeMapService;
import com.monzy.services.Service;
import com.monzy.utils.Util;

public class DaiHoiVoThuatService {

  private static DaiHoiVoThuatService instance;
  public DaiHoiVoThuat daihoi;

  public DaiHoiVoThuatService(DaiHoiVoThuat dh) {
    daihoi = dh;
  }

  public static DaiHoiVoThuatService gI(DaiHoiVoThuat daiHoiVoThuat) {
    if (instance == null) {
      instance = new DaiHoiVoThuatService(daiHoiVoThuat);
    }
    return instance;
  }

  public void update() {
    if (daihoi == null) {
      return;
    }
    int countNull = 0;
    int maxZone = 0;
    if (Util.contains(daihoi.time, String.valueOf(DaiHoiVoThuat.gI().hour)) && daihoi.round != 1) {
      Map map = Manager.MAPS.get(51);
      if (map != null) {
        maxZone = map.zones.size();
        for (Zone zones : map.zones) {
          if (zones != null && zones.getPlayers().size() <= 0) {
            countNull++;
          }
        }
        if (countNull >= maxZone) {
          daihoi.listReg.addAll(daihoi.listPlayerWait);
          daihoi.listPlayerWait.clear();
        }
      }
    }
    daihoi.listReg.stream()
        .filter((player) -> (player != null && player.zone.map.mapId != 52 && player.zone.map.mapId != 51 && DaiHoiVoThuat.gI().minute >= daihoi.min_start_temp))
        .peek((player) -> Service.gI().sendThongBao(player, "Bạn đã bị tước quyền thi đấu do không có mặt kịp giờ")).forEachOrdered((player) -> daihoi.listReg.remove(player));
    if (DaiHoiVoThuat.gI().second == 0 && DaiHoiVoThuat.gI().minute < daihoi.min_start_temp) {
      if (daihoi.listReg.size() > 1) {
        Service.gI().sendThongBao(daihoi.listReg,
            "Vòng " + daihoi.round + " sẽ bắt đầu sau " + (daihoi.min_start_temp - DaiHoiVoThuat.gI().minute) + " phút nữa");
      }
    } else if (DaiHoiVoThuat.gI().minute >= daihoi.min_start) {
      if (daihoi.listReg.size() > 1) {
        if (daihoi.listReg.size() % 2 == 0) {
          if (DaiHoiVoThuat.gI().minute >= daihoi.min_start_temp) {
            MatchDHVT();
          }
        } else {
          Player player = daihoi.listReg.get(Util.nextInt(0, daihoi.listReg.size() - 1));
          if (player != null) {
            daihoi.listPlayerWait.add(player);
            daihoi.listReg.remove(player);
            Service.gI().sendThongBao(player, "Chúc mừng bạn đã may mắn lọt vào vòng trong");
          }
          MatchDHVT();
        }
      } else if (daihoi.listReg.size() == 1
          && daihoi.listPlayerWait.isEmpty()
          && countNull >= maxZone) {
        Service.gI().sendThongBao(daihoi.listReg.get(0), "Bạn đã vô địch giải " + daihoi.cup);
        daihoi.round = 1;
        daihoi.listReg.clear();
        daihoi.listPlayerWait.clear();
        daihoi.min_start_temp = daihoi.min_start;
      }
    }
  }

  public void MatchDHVT() {
    int countMatch = daihoi.listReg.size() / 2;
    for (int i = 0; i < countMatch; i++) {
      Map map = Manager.MAPS.get(51);
      Zone z = null;
      if (map != null) {
        for (Zone zones : map.zones) {
          if (zones != null && zones.getHumanoids().isEmpty()) {
            z = zones;
          }
        }
      }
      Player pl1 = daihoi.listReg.get(Util.nextInt(0, daihoi.listReg.size() - 1));
      if (pl1 != null && pl1.isPl() && pl1.zone.map.mapId == 52) {
        pl1.isWin = false;
        ChangeMapService.gI().changeMap(pl1, z, 385, 312);
        pl1.nPoint.setFullHpMp();
        Service.gI().point(pl1);
        daihoi.listReg.remove(pl1);
      } else {
        daihoi.listReg.remove(pl1);
      }
      Player pl2 = daihoi.listReg.get(Util.nextInt(0, daihoi.listReg.size() - 1));
      if (pl2 != null && pl2.isPl() && pl2.zone.map.mapId == 52) {
        pl2.isWin = false;
        ChangeMapService.gI().changeMap(pl2, z, 385, 312);
        pl2.nPoint.setFullHpMp();
        Service.gI().point(pl2);
        daihoi.listReg.remove(pl2);
      } else {
        daihoi.listReg.remove(pl2);
      }
      PVPDaiHoi thachDau =
          new PVPDaiHoi(pl1, pl2, daihoi.gold, daihoi, System.currentTimeMillis());
      // đoạn này kéo 2 thằng lên sàn này
    }
    daihoi.round += 1;
    daihoi.min_start_temp += 2;
  }

  public boolean CanReg(Player pl) {
    return daihoi != null
        && pl.isPl() && Util.contains(daihoi.time, String.valueOf(DaiHoiVoThuat.gI().hour)) && DaiHoiVoThuat.gI().minute <= daihoi.min_limit
        && !playerExist((int) pl.id);
  }

  public String Giai(Player pl) {
    if (daihoi != null && playerExist((int) pl.id)) {
      return "Đại hội võ thuật sẽ bắt đầu sau " + (daihoi.min_start_temp - DaiHoiVoThuat.gI().minute)
          + " phút nữa";
    } else if (daihoi != null && Util.contains(daihoi.time, String.valueOf(DaiHoiVoThuat.gI().hour)) && DaiHoiVoThuat.gI().minute <= daihoi.min_limit) {
      return "Chào mừng bạn đến với đại hội võ thuật\nGiải " + daihoi.cup
          + " đang có "
          + daihoi.listReg.size()
          + " người đăng ký thi đấu";
    }
    return "Đã hết thời gian đăng ký vui lòng đợi đến giải đấu sau";
  }

  public boolean playerExist(int id) {
    if (daihoi != null) {
      for (int i = 0; i < daihoi.listReg.size(); i++) {
        Player pl = daihoi.listReg.get(i);
        if (pl != null && pl.isPl() && pl.id == id) {
          return true;
        }
      }
    }
    return false;
  }

  public void removePlayer(Player pl) {
    if (daihoi != null) {
      daihoi.listReg.remove(pl);
    }
  }

  public void removePlayerWait(Player pl) {
    if (daihoi != null) {
      daihoi.listPlayerWait.remove(pl);
    }
  }

  public void register(Player player) {
    if (daihoi == null) {
      return;
    }
    boolean isReg = false;
    if (daihoi.gem > 0) {
      if (player.inventory.gem >= daihoi.gem) {
        player.inventory.gem -= daihoi.gem;
        isReg = true;
      } else {
        Service.gI().sendThongBao(player, "Bạn Không Đủ Ngọc Để Đăng Ký");
      }
    } else if (daihoi.gold > 0) {
      if (player.inventory.gold >= daihoi.gold) {
        player.inventory.gold -= daihoi.gold;
        isReg = true;
      } else {
        Service.gI().sendThongBao(player, "Bạn Không Đủ Vàng Để Đăng Ký");
      }
    } else {
      Service.gI().sendThongBao(player, "Bạn Không Thể Đăng Ký Giải Đấu Này");
    }
    if (isReg) {
      if (player.isPl()) {
        daihoi.listReg.add(player);
        Service.gI().sendMoney(player);
        Service.gI().sendThongBao(player, "Bạn đã đăng ký thành công!Vui lòng không rời khỏi đại hội võ thuật để tránh bị tước quyền thi đấu!!");
      }
    }
  }
}

package com.monzy.models.npc;

import com.monzy.consts.ConstMap;
import com.monzy.consts.ConstNpc;
import com.monzy.consts.ConstPlayer;
import com.monzy.consts.ConstTask;
import com.monzy.giftcode.GiftCodeManager;
import com.monzy.kygui.ShopKyGuiService;
import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossData;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossManager;
import com.monzy.models.boss.list_boss.NhanBan;
import com.monzy.models.clan.Clan;
import com.monzy.models.item.Item;
import com.monzy.models.map.MapMaBu.MapMaBu;
import com.monzy.models.map.Zone;
import com.monzy.models.map.bdkb.BanDoKhoBauService;
import com.monzy.models.map.blackball.BlackBallWar;
import com.monzy.models.map.dhvt.MartialCongressService;
import com.monzy.models.map.doanhtraidocnhan.DoanhTraiDocNhanService;
import com.monzy.models.map.nguhanhson.nguhs;
import com.monzy.models.matches.PVPService;
import com.monzy.models.matches.pvp.DaiHoiVoThuat;
import com.monzy.models.matches.pvp.DaiHoiVoThuatService;
import com.monzy.models.player.NPoint;
import com.monzy.models.player.Player;
import com.monzy.models.shop.ShopServiceNew;
import com.monzy.models.skill.Skill;
import com.monzy.server.Client;
import com.monzy.server.Maintenance;
import com.monzy.server.Manager;
import com.monzy.services.*;
import com.monzy.services.func.Input;
import com.monzy.services.func.LuckyRound;
import com.monzy.services.func.SummonDragon;
import com.monzy.utils.Logger;
import com.monzy.utils.TimeUtil;
import com.monzy.utils.Util;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.monzy.services.func.SummonDragon.*;

public class NpcFactory {
  // playerid - object
  public static final java.util.Map<Long, Object> PLAYER_ID_OBJECT = new HashMap<>();
  private static final int COST_HD = 50000000;

  private NpcFactory() {}

  private static Npc ongGohan_ongMoori_ongParagus(
      int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
            this.createOtherMenu(
                player,
                ConstNpc.BASE_MENU,
                "Nạp Lần Đầu Đi Con!",
                "Đổi mật khẩu",
                "Nhận ngọc xanh",
                "Nhận đệ tử",
                "GiftCode");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (player.iDMark.isBaseMenu()) {
            switch (select) {
              case 0:
                Input.gI().createFormChangePassword(player);
                return;
              case 1:
                if (player.inventory.gem > 1000000) {
                  this.npcChat(player, "Sài có hết không =))");
                  return;
                }
                player.inventory.gem += 1000000;
                Service.gI().sendMoney(player);
                this.npcChat(player, "Vừa bank 1 triệu vô tài khoản đó =))");
                return;
              case 2:
                if (player.pet != null) {
                  this.npcChat(player, "Nuôi nổi 2 đứa không =))");
                  return;
                }
                PetService.gI().createNormalPet(player);
                this.npcChat(player, "Học hành thật tốt nhé, tạm biệt con :(");
                return;
              case 3:
                Input.gI().createFormGiftCode(player);
            }
          }
        }
      }
    };
  }

  // chua fix
  private static Npc trungLinhThu(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (this.mapId == 5) {
            this.createOtherMenu(
                player,
                ConstNpc.BASE_MENU,
                "Đổi Trứng Linh thú cần:\b|7|X99 Hồn Linh Thú + 1 Tỷ vàng",
                "Đổi Trứng\nLinh thú",
                "Nâng Chiến Linh",
                "Đổi chỉ số ẩn\nChiến Linh",
                "Từ chối");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == 5) {
            if (player.iDMark.isBaseMenu()) {
              switch (select) {
                case 0:
                  {
                    Item honLinhThu = null;
                    try {
                      honLinhThu = InventoryService.gI().findItemBag(player, 2029);
                    } catch (Exception e) {
                      Logger.logException(NpcFactory.class, e);
                    }
                    if (honLinhThu == null || honLinhThu.quantity < 99) {
                      this.npcChat(player, "Bạn không đủ 99 Hồn Linh thú");
                    } else if (player.inventory.gold < 1_000_000_000) {
                      this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                    } else if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                      this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                    } else {
                      player.inventory.gold -= 1_000_000_000;
                      InventoryService.gI().subQuantityItemsBag(player, honLinhThu, 99);
                      Service.gI().sendMoney(player);
                      Item trungLinhThu = ItemService.gI().createNewItem((short) 2028);
                      InventoryService.gI().addItemBag(player, trungLinhThu);
                      InventoryService.gI().sendItemBags(player);
                      this.npcChat(player, "Bạn nhận được 1 Trứng Linh thú");
                    }
                    break;
                  }
                case 1:
                  CombineService.gI().openTabCombine(player, CombineService.NANG_CHIEN_LINH);
                  break;
                case 2:
                  CombineService.gI()
                      .openTabCombine(player, CombineService.DOI_CHI_SO_AN_CHIEN_LINH);
                  break;
              }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
              switch (player.combine.typeCombine) {
                case CombineService.NANG_CHIEN_LINH:
                case CombineService.DOI_CHI_SO_AN_CHIEN_LINH:
                  if (select == 0) {
                    CombineService.gI().startCombine(player);
                  }
                  break;
              }
            }
          }
        }
      }
    };
  }

  private static Npc kyGui(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
            this.createOtherMenu(
                player,
                ConstNpc.BASE_MENU,
                "Cửa hàng chúng tôi chuyên mua bán hàng hiệu, hàng độc, cảm ơn bạn đã ghé thăm.",
                "Mua bán\nKý gửi",
                "Từ chối");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (player.iDMark.isBaseMenu()) {
	          if (select == 0) {
		          ShopKyGuiService.gI().openShopKyGui(player);
	          }
          }
        }
      }
    };
  }

  private static Npc potage(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (this.mapId == 140) {
            this.createOtherMenu(
                player,
                ConstNpc.BASE_MENU,
                "Đa vũ trụ song song \b|7|Con muốn gọi con trong đa vũ trụ \b|1|Với giá 200tr vàng không?",
                "Gọi Boss\nNhân Bản");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player) && this.mapId == 140 && player.iDMark.isBaseMenu()) {
          if (select == 0) {
            Boss oldBossClone =
                BossManager.gI().getBossById(Util.createIdBossClone((int) player.id));
            if (oldBossClone != null) {
              this.npcChat(
                  player,
                  "Nhà ngươi hãy tiêu diệt Boss lúc trước gọi ra đã, con boss đó đang ở khu "
                      + oldBossClone.zone.zoneId);
              return;
            }
            if (player.inventory.gold < 200_000_000) {
              this.npcChat(player, "Nhà ngươi không đủ 200.000.000 vàng");
              return;
            }
            List<Skill> skillList =
                player.playerSkill.skills.stream()
                    .filter(skill -> skill.point > 0)
                    .collect(Collectors.toList());
            int[][] skillTemp =
                skillList.stream()
                    .filter(skill -> skill.point > 0)
                    .map(skill -> new int[] {skill.template.id, skill.point, skill.coolDown})
                    .toArray(int[][]::new);
            BossData bossDataClone =
                new BossData(
                    "Nhân Bản " + player.name,
                    player.gender,
                    new short[] {
                      player.getHead(),
                      player.getBody(),
                      player.getLeg(),
                      player.getFlagBag(),
                      player.idAura,
                      player.getEffFront()
                    },
                    player.nPoint.dame,
                    new int[] {player.nPoint.hpMax},
                    new int[] {140},
                    skillTemp,
                    new String[] {"|-1|Vào mà húp"}, // text chat 1
                    new String[] {}, // text chat 2
                    new String[] {"|-1|Hãy đợi đó T.T"}, // text chat 3
                    0);
            try {
              new NhanBan(Util.createIdBossClone((int) player.id), bossDataClone, player.zone, player);
            } catch (Exception e) {
              Logger.logException(NpcFactory.class, e);
            }
            // trừ vàng khi gọi boss
            player.inventory.gold -= 200_000_000;
            Service.gI().sendMoney(player);
          }
        }
      }
    };
  }

  private static Npc quyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {

      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
            this.createOtherMenu(
                player,
                ConstNpc.BASE_MENU,
                "Chào con, ta rất vui khi gặp con\n Con muốn làm gì nào ?",
                "Nói chuyện",
                "Bảng\nXếp Hạng");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (player.iDMark.isBaseMenu()) {
            switch (select) {
              case 0:
                {
                  this.createOtherMenu(
                      player,
                      ConstNpc.QUY_LAO_KAME,
                      "Chào con, ta rất vui khi gặp con\n Con muốn làm gì nào ?",
                      "Về khu\nvực bang",
                      "Giải tán\nBang hội",
                      "Kho Báu\ndưới biển");
                  return;
                }
              case 1:
                {
                  this.npcChat(player, "Có gì đâu mà xem :3");
                  return;
                }
            }
            return;
          }
          if (player.iDMark.getIndexMenu() == ConstNpc.QUY_LAO_KAME) {
            switch (select) {
              case 0:
                {
                  this.npcChat(player, "Chức Năng Đang Được Update!");
                  return;
                  //                  if (player.session.player.nPoint.power < 80_000_000_000L)
                  // {
                  //                    return;
                  //                  }
                  //                  ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1,
                  // 432);
                  //                  return;
                }
              case 1:
                {
                  Clan clan = player.clan;
                  if (clan == null || clan.getClanMember((int) player.id) == null) {
                    Service.gI().sendThongBao(player, "Bạn đã có bang hội đâu!!!");
                    return;
                  }
                  if (clan.members.size() > 1) {
                    Service.gI().sendThongBao(player, "Bang phải còn một người");
                    return;
                  }
                  if (!clan.isLeader(player)) {
                    Service.gI().sendThongBao(player, "Phải là bang chủ");
                    return;
                  }
                  NpcService.gI()
                      .createMenuConMeo(
                          player,
                          ConstNpc.CONFIRM_DISSOLUTION_CLAN,
                          -1,
                          "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
                          "Đồng ý",
                          "Từ chối!");

                  return;
                }
              case 2:
                {
                  if (player.clan == null) {
                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                    return;
                  }
                  if (player.clan.banDoKhoBau == null) {
                    this.createOtherMenu(
                        player,
                        ConstNpc.MENU_OPEN_DBKB,
                        "Đây là bản đồ kho báu x4 tnsm\nCác con cứ yên tâm lên đường\n"
                            + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                        "Chọn\ncấp độ",
                        "Từ chối");
                    return;
                  }
                  this.createOtherMenu(
                      player,
                      ConstNpc.MENU_OPENED_DBKB,
                      "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                          + player.clan.banDoKhoBau.getLevel()
                          + "\nCon có muốn đi theo không?",
                      "Đồng ý",
                      "Từ chối");
                  return;
                }
            }
            return;
          }
          if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
            if (select == 0) {
              if (player.nPoint.power < BanDoKhoBauService.POWER_HAS_GO_TO_DBKB) {
                this.npcChat(
                    player,
                    "Sức mạnh của con phải ít nhất phải đạt "
                        + Util.numberToMoney(BanDoKhoBauService.POWER_HAS_GO_TO_DBKB));
                return;
              }
              ChangeMapService.gI().goToDBKB(player);
              return;
            }
          }
          if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
            if (select == 0) {
              if (player.nPoint.power < BanDoKhoBauService.POWER_HAS_GO_TO_DBKB) {
                this.npcChat(
                    player,
                    "Sức mạnh của con phải ít nhất phải đạt "
                        + Util.numberToMoney(BanDoKhoBauService.POWER_HAS_GO_TO_DBKB));
                return;
              }
              Input.gI().createFormChooseLevelBDKB(player);
              return;
            }
          }
          if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
            if (select == 0) {
              BanDoKhoBauService.gI()
                  .openBanDoKhoBau(
                      player, Byte.parseByte(String.valueOf(PLAYER_ID_OBJECT.get(player.id))));
            }
          }
        }
      }
    };
  }

  public static Npc truongLaoGuru(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
            super.openBaseMenu(player);
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        canOpenNpc(player);
      }
    };
  }

  public static Npc vuaVegeta(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
            super.openBaseMenu(player);
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        canOpenNpc(player);
      }
    };
  }

  public static Npc bulmaQK(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
            this.createOtherMenu(
                player, ConstNpc.BASE_MENU, "Cậu cần trang bị gì cứ đến chỗ tôi nhé", "Cửa\nhàng");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (player.iDMark.isBaseMenu()) {
            if (select == 0) { // Shop
              if (player.gender != ConstPlayer.TRAI_DAT) {
                this.createOtherMenu(
                    player,
                    ConstNpc.IGNORE_MENU,
                    "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất",
                    "Đóng");
                return;
              }
              ShopServiceNew.gI().openShop(player, "BUNMA", true);
            }
          }
        }
      }
    };
  }

  public static Npc dende(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
            if (player.idNRNM == -1) {
              this.createOtherMenu(
                  player, ConstNpc.BASE_MENU, "Anh cần trang bị gì cứ đến chỗ em nhé", "Cửa\nhàng");
              return;
            }
            if (player.zone.map.mapId == 7) {
              this.createOtherMenu(
                  player,
                  1,
                  "Ồ, ngọc rồng namếc, bạn thật là may mắn\nnếu tìm đủ 7 viên sẽ được Rồng Thiêng Namếc ban cho điều ước",
                  "Hướng dẫn\nGọi Rồng",
                  "Gọi rồng",
                  "Từ chối");
            }
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (player.iDMark.isBaseMenu()) {
            if (select == 0) { // Shop
              if (player.gender == ConstPlayer.NAMEC) {
                ShopServiceNew.gI().openShop(player, "DENDE", true);
              } else {
                this.createOtherMenu(
                    player,
                    ConstNpc.IGNORE_MENU,
                    "Xin lỗi anh, em chỉ bán đồ cho dân tộc Namếc",
                    "Đóng");
              }
            }
            return;
          }
          if (player.iDMark.getIndexMenu() == 1
              && player.zone.map.mapId == 7
              && player.idNRNM != -1) {
            if (player.idNRNM != 353) {
              Service.gI().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
              return;
            }
            NgocRongNamecService.gI().tOpenNrNamec = System.currentTimeMillis() + 86400000;
            NgocRongNamecService.gI().firstNrNamec = true;
            NgocRongNamecService.gI().timeNrNamec = 0;
            NgocRongNamecService.gI().doneDragonNamec();
            NgocRongNamecService.gI().initNgocRongNamec((byte) 1);
            NgocRongNamecService.gI().reInitNrNamec(86399000);
            SummonDragon.gI().summonNamec(player);
          }
        }
      }
    };
  }

  public static Npc appule(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
            this.createOtherMenu(
                player, ConstNpc.BASE_MENU, "Ngươi cần trang bị gì cứ đến chỗ ta nhé", "Cửa\nhàng");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (player.iDMark.isBaseMenu()) {
            if (select == 0) { // Shop
              if (player.gender == ConstPlayer.XAYDA) {
                ShopServiceNew.gI().openShop(player, "APPULE", true);
              } else {
                this.createOtherMenu(
                    player,
                    ConstNpc.IGNORE_MENU,
                    "Về hành tinh hạ đẳng của ngươi mà mua đồ cùi nhé. Tại đây ta chỉ bán đồ cho người Xayda thôi",
                    "Đóng");
              }
            }
          }
        }
      }
    };
  }

  public static Npc drDrief(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player pl) {
        if (canOpenNpc(pl)) {
          if (this.mapId == 84) {
            this.createOtherMenu(
                pl,
                ConstNpc.BASE_MENU,
                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                pl.gender == ConstPlayer.TRAI_DAT
                    ? "Đến\nTrái Đất"
                    : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
            return;
          }
          if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
            if (pl.playerTask.taskMain.id == 7) {
              NpcService.gI()
                  .createTutorial(
                      pl,
                      this.avartar,
                      "Hãy lên đường cứu đứa bé nhà tôi\nChắc bây giờ nó đang sợ hãi lắm rồi");
              return;
            }
            this.createOtherMenu(
                pl,
                ConstNpc.BASE_MENU,
                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                "Đến\nNamếc",
                "Đến\nXayda",
                "Siêu thị");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == 84) {
            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
            return;
          }
          if (player.iDMark.isBaseMenu()) {
            switch (select) {
              case 0:
                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                break;
              case 1:
                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                break;
              case 2:
                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                break;
            }
          }
        }
      }
    };
  }

  public static Npc cargo(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player pl) {
        if (canOpenNpc(pl)) {
          if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
            if (pl.playerTask.taskMain.id == 7) {
              NpcService.gI()
                  .createTutorial(
                      pl,
                      this.avartar,
                      "Hãy lên đường cứu đứa bé nhà tôi\n" + "Chắc bây giờ nó đang sợ hãi lắm rồi");
              return;
            }
            this.createOtherMenu(
                pl,
                ConstNpc.BASE_MENU,
                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                "Đến\nTrái Đất",
                "Đến\nXayda",
                "Siêu thị");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (player.iDMark.isBaseMenu()) {
            switch (select) {
              case 0:
                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                break;
              case 1:
                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                break;
              case 2:
                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                break;
            }
          }
        }
      }
    };
  }

  public static Npc cui(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      private final int COST_FIND_BOSS = 50000000;

      @Override
      public void openBaseMenu(Player pl) {
        if (canOpenNpc(pl)) {
          if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
            if (pl.playerTask.taskMain.id == 7) {
              NpcService.gI()
                  .createTutorial(
                      pl,
                      this.avartar,
                      "Hãy lên đường cứu đứa bé nhà tôi\n" + "Chắc bây giờ nó đang sợ hãi lắm rồi");
              return;
            }
            if (this.mapId == 19) {
              int taskId = TaskService.gI().getIdTask(pl);
              switch (taskId) {
                case ConstTask.TASK_19_0:
                  this.createOtherMenu(
                      pl,
                      ConstNpc.MENU_FIND_KUKU,
                      "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                      "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                      "Đến Cold",
                      "Đến\nNappa",
                      "Từ chối");
                  return;
                case ConstTask.TASK_19_1:
                  this.createOtherMenu(
                      pl,
                      ConstNpc.MENU_FIND_MAP_DAU_DINH,
                      "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                      "Đến chỗ\nMập đầu đinh\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                      "Đến Cold",
                      "Đến\nNappa",
                      "Từ chối");
                  return;
                case ConstTask.TASK_19_2:
                  this.createOtherMenu(
                      pl,
                      ConstNpc.MENU_FIND_RAMBO,
                      "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                      "Đến chỗ\nRambo\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                      "Đến Cold",
                      "Đến\nNappa",
                      "Từ chối");
                  return;
                default:
                  this.createOtherMenu(
                      pl,
                      ConstNpc.BASE_MENU,
                      "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                      "Đến Cold",
                      "Đến\nNappa",
                      "Từ chối");
                  return;
              }
            }
            if (this.mapId == 68) {
              this.createOtherMenu(
                  pl, ConstNpc.BASE_MENU, "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
              return;
            }
            this.createOtherMenu(
                pl,
                ConstNpc.BASE_MENU,
                "Tàu vũ trụ Xayda sử dụng công nghệ mới nhất, "
                    + "có thể đưa ngươi đi bất kỳ đâu, chỉ cần trả tiền là được.",
                "Đến\nTrái Đất",
                "Đến\nNamếc",
                "Siêu thị");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == 26) {
            if (player.iDMark.isBaseMenu()) {
              switch (select) {
                case 0:
                  ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                  return;
                case 1:
                  ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                  return;
                case 2:
                  ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                  return;
              }
            }
          }
          if (this.mapId == 19) {
            if (player.iDMark.isBaseMenu()) {
              switch (select) {
                case 0:
                  if (player.session.player.nPoint.power >= 80_000_000_000L) {
                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                  }
                  this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                  return;
                case 1:
                  ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                  return;
              }
              return;
            }
            if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
              switch (select) {
                case 0:
                  Boss kuku = BossManager.gI().getBossById(BossID.KUKU);
                  if (kuku == null || kuku.isDie()) {
                    Service.gI().sendThongBao(player, "Chết rồi ba...");
                    return;
                  }
                  if (player.inventory.gold < COST_FIND_BOSS) {
                    Service.gI().sendThongBao(player, "Không đủ vàng");
                    return;
                  }
                  Zone zone =
                      MapService.gI().getMapCanJoin(player, kuku.zone.map.mapId, kuku.zone.zoneId);
                  if (zone == null || zone.getNumOfPlayers() >= zone.maxPlayer) {
                    Service.gI().sendThongBao(player, "Khu vực này đã đầy");
                    return;
                  }
                  ChangeMapService.gI()
                      .changeMap(player, kuku.zone, kuku.location.x, kuku.location.y);
                  player.inventory.gold -= COST_FIND_BOSS;
                  Service.gI().sendMoney(player);
                  return;
                case 1:
                  ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                  return;
                case 2:
                  ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                  return;
              }
              return;
            }
            if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_MAP_DAU_DINH) {
              switch (select) {
                case 0:
                  Boss mapDauDinh = BossManager.gI().getBossById(BossID.MAP_DAU_DINH);
                  if (mapDauDinh == null || mapDauDinh.isDie()) {
                    Service.gI().sendThongBao(player, "Chết rồi ba...");
                    return;
                  }
                  if (player.inventory.gold < COST_FIND_BOSS) {
                    Service.gI().sendThongBao(player, "Không đủ vàng");
                    return;
                  }
                  Zone zone =
                      MapService.gI()
                          .getMapCanJoin(player, mapDauDinh.zone.map.mapId, mapDauDinh.zone.zoneId);
                  if (zone == null || zone.getNumOfPlayers() >= zone.maxPlayer) {
                    Service.gI().sendThongBao(player, "Khu vực này đã đầy");
                    return;
                  }
                  ChangeMapService.gI()
                      .changeMap(
                          player, mapDauDinh.zone, mapDauDinh.location.x, mapDauDinh.location.y);
                  player.inventory.gold -= COST_FIND_BOSS;
                  Service.gI().sendMoney(player);
                  return;
                case 1:
                  ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                  return;
                case 2:
                  ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                  return;
              }
              return;
            }
            if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_RAMBO) {
              switch (select) {
                case 0:
                  Boss rambo = BossManager.gI().getBossById(BossID.RAMBO);
                  if (rambo == null || rambo.isDie()) {
                    Service.gI().sendThongBao(player, "Chết rồi ba...");
                    return;
                  }
                  if (player.inventory.gold < COST_FIND_BOSS) {
                    Service.gI().sendThongBao(player, "Không đủ vàng");
                    return;
                  }
                  Zone zone =
                      MapService.gI()
                          .getMapCanJoin(player, rambo.zone.map.mapId, rambo.zone.zoneId);
                  if (zone == null || zone.getNumOfPlayers() >= zone.maxPlayer) {
                    Service.gI().sendThongBao(player, "Khu vực này đã đầy");
                    return;
                  }
                  ChangeMapService.gI()
                      .changeMap(player, rambo.zone, rambo.location.x, rambo.location.y);
                  player.inventory.gold -= COST_FIND_BOSS;
                  Service.gI().sendMoney(player);
                  return;
                case 1:
                  ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                  return;
                case 2:
                  ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                  return;
              }
              return;
            }
          }
          if (this.mapId == 68) {
            if (player.iDMark.isBaseMenu()) {
              if (select == 0) {
                ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
              }
            }
          }
        }
      }
    };
  }

  public static Npc santa(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          createOtherMenu(
              player,
              ConstNpc.BASE_MENU,
              "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
              "Cửa hàng",
              "Đổi Tiền",
              "Shop Hồng Ngọc");
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
            if (player.iDMark.isBaseMenu()) {
              switch (select) {
                case 0: // shop
                  ShopServiceNew.gI().openShop(player, "SANTA", false);
                  break;
                case 1:
                  this.createOtherMenu(
                      player,
                      ConstNpc.QUY_DOI,
                      "|7|Số tiền của bạn còn : "
                          + player.session.vnd
                          + "\nTỉ lệ quy đổi là 1-1"
                          + "\nMuốn quy đổi không",
                      "Quy đổi\nHồng ngọc",
                      "Quy Đổi\nThỏi vàng");
                  break;
                case 2: // shop
                  ShopServiceNew.gI().openShop(player, "SHOP_NGU_SAC", false);
                  break;
              }
              return;
            }
            if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI) {
              switch (select) {
                case 0:
                  Input.gI().createFormQDHN(player);
                  break;
                case 1:
                  Input.gI().createFormQDTV(player);
                  break;
              }
            }
          }
        }
      }
    };
  }

  public static Npc thoDaiKa(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          createOtherMenu(
              player,
              ConstNpc.BASE_MENU,
              "Đưa cho ta Hồng Ngọc hoặc Thỏi Vàng và ngươi sẽ mua đc oto\nĐây không phải chẵn lẻ tài xỉu đâu=)))",
              "Tài\n hồng ngọc",
              "Xỉu\n hồng ngọc",
              "Tài\n thỏi vàng",
              "Xỉu\n thỏi vàng");
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (!player.session.actived) {
            this.npcChat(player, "Mở thành viên mới chơi được bạn ơi :)");
            return;
          }
          if (this.mapId == 5) {
            if (player.iDMark.isBaseMenu()) {
              switch (select) {
                case 0:
                  Input.gI().TAI(player);
                  break;
                case 1:
                  Input.gI().XIU(player);
                  break;
                case 2:
                  Input.gI().TAITV(player);
                  break;
                case 3:
                  Input.gI().XIUTV(player);
                  break;
              }
            }
          }
        }
      }
    };
  }

  public static Npc uron(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player pl) {
        if (canOpenNpc(pl)) {
          ShopServiceNew.gI().openShop(pl, "URON", false);
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        canOpenNpc(player);
      }
    };
  }

  public static Npc baHatMit(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (this.mapId == 5) {
            this.createOtherMenu(
                player,
                ConstNpc.BASE_MENU,
                "Ngươi tìm ta có việc gì?",
                "Ép sao\ntrang bị",
                "Pha lê\nhóa\ntrang bị");
            return;
          }
          if (this.mapId == 121) {
            this.createOtherMenu(
                player, ConstNpc.BASE_MENU, "Ngươi tìm ta có việc gì?", "Về\nđảo rùa");
            return;
          }
          this.createOtherMenu(
              player,
              ConstNpc.BASE_MENU,
              "Ngươi tìm ta có việc gì?",
              "Cửa hàng\nBùa",
              "Nâng cấp\nVật phẩm",
              "Nâng cấp\nBông tai\nPorata",
              "Mở chỉ số\nBông tai\nPorata",
              "Nhập\nNgọc Rồng",
              "Nâng cấp\n Đồ\n Hủy Diệt",
              "Nâng Cấp \nĐồ Thiên Sứ");
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == 5) {
            if (player.iDMark.isBaseMenu()) {
              switch (select) {
                case 0:
                  CombineService.gI().openTabCombine(player, CombineService.EP_SAO_TRANG_BI);
                  return;
                case 1:
                  CombineService.gI().openTabCombine(player, CombineService.PHA_LE_HOA_TRANG_BI);
                  return;
              }
            }
            if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
              switch (player.combine.typeCombine) {
                case CombineService.EP_SAO_TRANG_BI:
                case CombineService.PHA_LE_HOA_TRANG_BI:
                  if (select == 0) {
                    CombineService.gI().startCombine(player);
                  }
              }
            }
          }
          if (this.mapId == 112) {
            if (player.iDMark.isBaseMenu()) {
              if (select == 0) {
                ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
              }
            }
          }
          if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44 || this.mapId == 84) {
            if (player.iDMark.isBaseMenu()) {
              switch (select) {
                case 0:
                  // shop bùa
                  createOtherMenu(
                      player,
                      ConstNpc.MENU_OPTION_SHOP_BUA,
                      "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                          + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                      "Bùa\n1 giờ",
                      "Bùa\n8 giờ",
                      "Bùa\n1 tháng",
                      "Đóng");
                  return;
                case 1:
                  CombineService.gI().openTabCombine(player, CombineService.NANG_CAP_VAT_PHAM);
                  return;
                case 2: // NANG_CAP_BONG_TAI
                  this.npcChat(player, "Chức Năng Đang Được Update!");
                  return;
                  //                  CombineService.gI().openTabCombine(player,
                  // CombineService.NANG_CAP_BONG_TAI);
                  //                  break;
                case 3: // MO_CHI_SO_BONG_TAI
                  this.npcChat(player, "Chức Năng Đang Được Update!");
                  return;
                  //                  CombineService.gI().openTabCombine(player,
                  // CombineService.MO_CHI_SO_BONG_TAI);
                  //                  break;
                case 4: // NHAP_NGOC_RONG
                  CombineService.gI().openTabCombine(player, CombineService.NHAP_NGOC_RONG);
                  break;
                case 5: // NANG_CAP_DO_HD
                  this.npcChat(player, "Chức Năng Đang Được Update!");
                  return;
                  //                  CombineService.gI().openTabCombine(player,
                  // CombineService.NANG_CAP_DO_HD);
                  //                  break;
                case 6: // NANG_CAP_DO_TS
                  this.npcChat(player, "Chức Năng Đang Được Update!");
                  return;
                  //                  CombineService.gI().openTabCombine(player,
                  // CombineService.NANG_CAP_DO_TS);
                  //                  break;
              }
            }
            if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
              switch (select) {
                case 0:
                  ShopServiceNew.gI().openShop(player, "BUA_1H", true);
                  return;
                case 1:
                  ShopServiceNew.gI().openShop(player, "BUA_8H", true);
                  return;
                case 2:
                  ShopServiceNew.gI().openShop(player, "BUA_1M", true);
                  return;
              }
            }
            if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
              switch (player.combine.typeCombine) {
                case CombineService.NANG_CAP_VAT_PHAM:
                case CombineService.NANG_CAP_BONG_TAI:
                case CombineService.MO_CHI_SO_BONG_TAI:
                case CombineService.NHAP_NGOC_RONG:
                case CombineService.NANG_CAP_DO_HD:
                case CombineService.NANG_CAP_DO_TS:
                  if (select == 0) {
                    CombineService.gI().startCombine(player);
                  }
                  return;
              }
            }
          }
        }
      }
    };
  }

  public static Npc ruongDo(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          InventoryService.gI().sendItemBox(player);
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        canOpenNpc(player);
      }
    };
  }

  public static Npc duongtank(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (mapId == 0) {
            nguhs.gI().setTimeJoinnguhs();
            if (player.nPoint.power >= 1_500_000L && player.nPoint.power <= 20_000_000_000L) {
              this.createOtherMenu(
                  player,
                  0,
                  "Ngũ Hành Sơn x10 Tnsm\nHỗ trợ cho Ae từ 1tr5 đến 20tỷ sức mạnh\n250 hồng ngọc 1 lần vào",
                  "Đồng ý",
                  "Từ chối");
              return;
            }
            this.createOtherMenu(
                player,
                ConstNpc.IGNORE_MENU,
                "Sức mạnh bạn không đủ điều kiện\nChỉ hỗ trợ cho Ae 1tr5 đến 20tỷ sức mạnh.",
                "Không");
          }
          if (mapId == 123) {
            this.createOtherMenu(player, 0, "Bạn Muốn Quay Trở Lại Làng Aru?", "Đồng ý", "Từ chối");
          }
          if (mapId == 122) {
            this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Đi thỉnh kinh cùng tao không :))");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (mapId == 0) {
            switch (select) {
              case 0:
                break;
              case 1:
                if (player.nPoint.power < 1_500_000L || player.nPoint.power > 20_000_000_000L) {
                  Service.gI().sendThongBao(player, "Sức mạnh bạn không đủ điều kiện!");
                  return;
                }
                if (player.inventory.ruby < 250) {
                  Service.gI().sendThongBao(player, "Phí vào là 250 hồng ngọc 1 lần");
                  return;
                }
                player.inventory.ruby -= 250;
                PlayerService.gI().sendInfoHpMpMoney(player);
                ChangeMapService.gI().changeMapInYard(player, 123, -1, -1);
                break;
            }
          }
          if (mapId == 123) {
            if (select == 0) {
              ChangeMapService.gI().changeMapInYard(player, 0, -1, 469);
            }
          }
          //          if (mapId == 122) {
          //            if (select == 0) {
          //              if (player.event >= 500) {
          //                player.event -= 500;
          //                Item item = ItemService.gI().createNewItem((short) (711));
          //                item.itemOptions.add(new Item.ItemOption(5, 10));
          //                item.itemOptions.add(new Item.ItemOption(5, 10));
          //                item.itemOptions.add(new Item.ItemOption(5, 20));
          //                item.itemOptions.add(new Item.ItemOption(207, 0));
          //                item.itemOptions.add(new Item.ItemOption(33, 0));
          //                //
          //                InventoryService.gI().addItemBag(player, item);
          //                Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Vật Phẩm Thành Công
          // !");
          //              } else {
          //                Service.gI()
          //                    .sendThongBao(
          //                        player, "Không đủ điểm, bạn còn " + (500 - player.pointPvp) + "
          // điểm nữa");
          //              }
          //            }
          //          }
        }
      }
    };
  }

  public static Npc dauThan(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          player.magicTree.openMenuTree();
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          switch (player.iDMark.getIndexMenu()) {
            case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
              switch (select) {
                case 0:
                  player.magicTree.harvestPea();
                  return;
                case 1:
                  if (player.magicTree.level == 10) {
                    player.magicTree.fastRespawnPea();
                    return;
                  }
                  player.magicTree.showConfirmUpgradeMagicTree();
                  return;
                case 2:
                  player.magicTree.fastRespawnPea();
                  return;
              }
              return;
            case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
              switch (select) {
                case 0:
                  player.magicTree.harvestPea();
                  return;
                case 1:
                  player.magicTree.showConfirmUpgradeMagicTree();
                  return;
              }
              return;
            case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
              switch (select) {
                case 0:
                  player.magicTree.upgradeMagicTree();
                  return;
              }
              return;
            case ConstNpc.MAGIC_TREE_UPGRADE:
              switch (select) {
                case 0:
                  player.magicTree.fastUpgradeMagicTree();
                  return;
                case 1:
                  player.magicTree.showConfirmUnuppgradeMagicTree();
                  return;
              }
              return;
            case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
              switch (select) {
                case 0:
                  player.magicTree.unupgradeMagicTree();
                  return;
              }
              return;
          }
        }
      }
    };
  }

  public static Npc calick(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
        if (TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
          Service.gI().hideWaitDialog(player);
          Service.gI().sendThongBao(player, "Không thể thực hiện");
          return;
        }
        if (this.mapId != player.zone.map.mapId) {
          Service.gI().sendThongBao(player, "Calích đã rời khỏi map!");
          Service.gI().hideWaitDialog(player);
          return;
        }
        if (this.mapId == 102) {
          this.createOtherMenu(
              player,
              ConstNpc.BASE_MENU,
              "Chào chú, cháu có thể giúp gì?",
              "Kể\nChuyện",
              "Quay về\nQuá khứ");
          return;
        }
        this.createOtherMenu(
            player,
            ConstNpc.BASE_MENU,
            "Chào chú, cháu có thể giúp gì?",
            "Kể\nChuyện",
            "Đi đến\nTương lai",
            "Từ chối");
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (this.mapId == 102) {
          if (player.iDMark.isBaseMenu()) {
            switch (select) {
              case 0:
                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                return;
              case 1:
                ChangeMapService.gI().goToQuaKhu(player);
                return;
            }
          }
          return;
        }
        if (player.iDMark.isBaseMenu()) {
          switch (select) {
            case 0:
              NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
              return;
            case 1:
              if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_20_0) {
                ChangeMapService.gI().goToTuongLai(player);
              }
              return;
            default:
              Service.gI().sendThongBao(player, "Không thể thực hiện");
          }
        }
      }
    };
  }

  public static Npc jaco(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
            this.createOtherMenu(
                player,
                ConstNpc.BASE_MENU,
                "Gô Tên, Calich và Monaka đang gặp chuyện ở hành tinh Potaufeu \n Hãy đến đó ngay",
                "Đến \nPotaufeu");
          }
          if (this.mapId == 139) {
            this.createOtherMenu(
                player, ConstNpc.BASE_MENU, "Người muốn trở về?", "Quay về", "Từ chối");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
            if (player.session.player.nPoint.power < 800_000_000L) {
              this.npcChat(player, "Bạn chưa đủ 800tr sức mạnh để vào!");
              return;
            }
            ChangeMapService.gI().goToPotaufeu(player);
          }
          if (this.mapId == 139) {
            if (player.iDMark.isBaseMenu()) {
              // về trạm vũ trụ
              if (select == 0) {
                ChangeMapService.gI().changeMapBySpaceShip(player, 24 + player.gender, -1, -1);
              }
            }
          }
        }
      }
    };
  }

  public static Npc npcLyTieuNuong54(
      int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        this.openBaseMenu(player);
        //        createOtherMenu(
        //            player,
        //            0,
        //            "Trò chơi Chọn ai đây đang được diễn ra, nếu bạn tin tưởng mình đang tràn đầy
        // may mắn thì có thể tham gia thử",
        //            "Thể lệ",
        //            "Chọn\nThỏi vàng");
      }

      @Override
      public void confirmMenu(Player pl, int select) {
        //        if (canOpenNpc(pl)) {
        //          String time =
        //              ((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + "
        // giây";
        //          if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
        //            ChonAiDay.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
        //          }
        //          if (pl.iDMark.getIndexMenu() == 0) {
        //            if (select == 0) {
        //              createOtherMenu(
        //                  pl,
        //                  ConstNpc.IGNORE_MENU,
        //                  "Thời gian giữa các giải là 5 phút\nKhi hết giờ, hệ thống sẽ ngẫu nhiên
        // chọn ra 1 người may mắn.\nLưu ý: Số thỏi vàng nhận được sẽ bị nhà cái lụm đi 5%!Trong quá
        // trình diễn ra khi đặt cược nếu thoát game mọi phần đặt đều sẽ bị hủy",
        //                  "Ok");
        //            } else if (select == 1) {
        //              createOtherMenu(
        //                  pl,
        //                  1,
        //                  "Tổng giải thường: "
        //                      + ChonAiDay.gI().goldNormar
        //                      + " thỏi vàng, cơ hội trúng của bạn là: "
        //                      + pl.percentGold(0)
        //                      + "%\nTổng giải VIP: "
        //                      + ChonAiDay.gI().goldVip
        //                      + " thỏi vàng, cơ hội trúng của bạn là: "
        //                      + pl.percentGold(1)
        //                      + "%\nSố thỏi vàng đặt thường: "
        //                      + pl.goldNormar
        //                      + "\nSố thỏi vàng đặt VIP: "
        //                      + pl.goldVIP
        //                      + "\n Thời gian còn lại: "
        //                      + time,
        //                  "Cập nhập",
        //                  "Thường\n20 thỏi\nvàng",
        //                  "VIP\n200 thỏi\nvàng",
        //                  "Đóng");
        //            }
        //          } else if (pl.iDMark.getIndexMenu() == 1) {
        //            if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0) {
        //              switch (select) {
        //                case 0:
        //                  createOtherMenu(
        //                      pl,
        //                      1,
        //                      "Tổng giải thường: "
        //                          + ChonAiDay.gI().goldNormar
        //                          + " thỏi vàng, cơ hội trúng của bạn là: "
        //                          + pl.percentGold(0)
        //                          + "%\nTổng giải VIP: "
        //                          + ChonAiDay.gI().goldVip
        //                          + " thỏi vàng, cơ hội trúng của bạn là: "
        //                          + pl.percentGold(1)
        //                          + "%\nSố thỏi vàng đặt thường: "
        //                          + pl.goldNormar
        //                          + "\nSố thỏi vàng đặt VIP: "
        //                          + pl.goldVIP
        //                          + "\n Thời gian còn lại: "
        //                          + time,
        //                      "Cập nhập",
        //                      "Thường\n20 thỏi\nvàng",
        //                      "VIP\n200 thỏi\nvàng",
        //                      "Đóng");
        //                  break;
        //                case 1:
        //                  {
        //                    try {
        //                      if (InventoryService.gI().findItemBag(pl, 457).isNotNullItem()
        //                          && InventoryService.gI().findItemBag(pl, 457).quantity >= 20) {
        //                        InventoryService.gI()
        //                            .subQuantityItemsBag(
        //                                pl, InventoryService.gI().findItemBag(pl, 457), 20);
        //                        InventoryService.gI().sendItemBags(pl);
        //                        pl.goldNormar += 20;
        //                        ChonAiDay.gI().goldNormar += 20;
        //                        ChonAiDay.gI().addPlayerNormar(pl);
        //                        createOtherMenu(
        //                            pl,
        //                            1,
        //                            "Tổng giải thường: "
        //                                + ChonAiDay.gI().goldNormar
        //                                + " thỏi vàng, cơ hội trúng của bạn là: "
        //                                + pl.percentGold(0)
        //                                + "%\nTổng giải VIP: "
        //                                + ChonAiDay.gI().goldVip
        //                                + " thỏi vàng, cơ hội trúng của bạn là: "
        //                                + pl.percentGold(1)
        //                                + "%\nSố thỏi vàng đặt thường: "
        //                                + pl.goldNormar
        //                                + "\nSố thỏi vàng đặt VIP: "
        //                                + pl.goldVIP
        //                                + "\n Thời gian còn lại: "
        //                                + time,
        //                            "Cập nhập",
        //                            "Thường\n20 thỏi\nvàng",
        //                            "VIP\n200 thỏi\nvàng",
        //                            "Đóng");
        //                      } else {
        //                        Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");
        //                      }
        //                    } catch (Exception ex) {
        //                      java.util.logging.Logger.getLogger(NpcFactory.class.getName())
        //                          .log(Level.SEVERE, null, ex);
        //                    }
        //                  }
        //                  break;
        //                case 2:
        //                  {
        //                    try {
        //                      if (InventoryService.gI().findItemBag(pl, 457).isNotNullItem()
        //                          && InventoryService.gI().findItemBag(pl, 457).quantity >= 200) {
        //                        InventoryService.gI()
        //                            .subQuantityItemsBag(
        //                                pl, InventoryService.gI().findItemBag(pl, 457), 200);
        //                        InventoryService.gI().sendItemBags(pl);
        //                        pl.goldVIP += 200;
        //                        ChonAiDay.gI().goldVip += 200;
        //                        ChonAiDay.gI().addPlayerVIP(pl);
        //                        createOtherMenu(
        //                            pl,
        //                            1,
        //                            "Tổng giải thường: "
        //                                + ChonAiDay.gI().goldNormar
        //                                + " thỏi vàng, cơ hội trúng của bạn là: "
        //                                + pl.percentGold(0)
        //                                + "%\nTổng giải VIP: "
        //                                + ChonAiDay.gI().goldVip
        //                                + " thỏi vàng, cơ hội trúng của bạn là: "
        //                                + pl.percentGold(1)
        //                                + "%\nSố thỏi vàng đặt thường: "
        //                                + pl.goldNormar
        //                                + "\nSố thỏi vàng đặt VIP: "
        //                                + pl.goldVIP
        //                                + "\n Thời gian còn lại: "
        //                                + time,
        //                            "Cập nhập",
        //                            "Thường\n20 thỏi\nvàng",
        //                            "VIP\n200 thỏi\nvàng",
        //                            "Đóng");
        //                      } else {
        //                        Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");
        //                      }
        //                    } catch (Exception ex) {
        //                      //
        //                      //
        // java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null,
        // ex);
        //                    }
        //                  }
        //                  break;
        //              }
        //            }
        //          }
        //        }
      }
    };
  }

  public static Npc thuongDe(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (this.mapId == 45) {
            this.createOtherMenu(
                player, ConstNpc.BASE_MENU, "Con muốn làm gì nào", "Đến Kaio", "Quay số\nmay mắn");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == 45) {
            if (player.iDMark.isBaseMenu()) {
              switch (select) {
                case 0:
                  ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                  return;
                case 1:
                  this.createOtherMenu(
                      player,
                      ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                      "Con muốn làm gì nào?",
                      "Quay bằng\nvàng",
                      "Quay bằng\n hồng ngọc",
                      "Rương phụ\n("
                          + (player.inventory.itemsBoxCrackBall.size()
                              - InventoryService.gI()
                                  .getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                          + " món)",
                      "Xóa hết\ntrong rương",
                      "Đóng");
              }
            }
            if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
              switch (select) {
                case 0:
                  LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GOLD);
                  return;
                case 1:
                  LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_RUBY);
                  return;
                case 2:
                  ShopServiceNew.gI().openShop(player, "ITEMS_LUCKY_ROUND", true);
                  return;
                case 3:
                  NpcService.gI()
                      .createMenuConMeo(
                          player,
                          ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND,
                          this.avartar,
                          "Con có chắc muốn xóa hết vật phẩm trong rương phụ?\n"
                              + "Sau khi xóa sẽ không thể khôi phục!",
                          "Đồng ý",
                          "Hủy bỏ");
                  break;
              }
            }
          }
        }
      }
    };
  }

  public static Npc thanVuTru(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (this.mapId == 48) {
            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Con muốn làm gì nào", "Di chuyển");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == 48) {
            if (player.iDMark.isBaseMenu()) {
              if (select == 0) {
                this.createOtherMenu(
                    player,
                    ConstNpc.MENU_DI_CHUYEN,
                    "Con muốn đi đâu?",
                    "Về\nthần điện",
                    "Thánh địa\nKaio",
                    "Con\nđường\nrắn độc",
                    "Từ chối");
              }
            }
            if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
              switch (select) {
                case 0:
                  ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                  break;
                case 1:
                  ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                  break;
                case 2: // con đường rắn độc
                  this.npcChat(player, "Chức Năng Đang Được Update!");
                  break;
              }
            }
          }
        }
      }
    };
  }

  public static Npc kibit(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (this.mapId == 50) {
            this.createOtherMenu(
                player,
                ConstNpc.BASE_MENU,
                "Ta có thể giúp gì cho ngươi ?",
                "Đến\nKaio",
                "Từ chối");
          }
          if (this.mapId == 114) {
            this.createOtherMenu(
                player, ConstNpc.IGNORE_MENU, "Ta có thể giúp gì cho ngươi ?", "Từ chối");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == 50) {
            if (player.iDMark.isBaseMenu()) {
              if (select == 0) {
                ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
              }
            }
          }
        }
      }
    };
  }

  // chưa fix
  public static Npc osin(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (this.mapId == 50) {
            this.createOtherMenu(
                player,
                ConstNpc.BASE_MENU,
                "Ta có thể giúp gì cho ngươi ?",
                "Đến\nKaio",
                "Đến\nhành tinh\nBill",
                "Từ chối");
          }
          if (this.mapId == 154) {
            this.createOtherMenu(
                player,
                ConstNpc.BASE_MENU,
                "Ta có thể giúp gì cho ngươi ?",
                "Về thánh địa",
                "Đến\nhành tinh\nngục tù",
                "Từ chối");
          }
          if (this.mapId == 155) {
            this.createOtherMenu(
                player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?", "Quay về", "Từ chối");
          }
          if (this.mapId == 52) {
            try {
              MapMaBu.gI().setTimeJoinMapMaBu();
              if (this.mapId == 52) {
                long now = System.currentTimeMillis();
                if (now > MapMaBu.TIME_OPEN_MABU && now < MapMaBu.TIME_CLOSE_MABU) {
                  this.createOtherMenu(
                      player,
                      ConstNpc.MENU_OPEN_MMB,
                      "Đại chiến Ma Bư đã mở, " + "ngươi có muốn tham gia không?",
                      "Hướng dẫn\nthêm",
                      "Tham gia",
                      "Từ chối");
                } else {
                  this.createOtherMenu(
                      player,
                      ConstNpc.MENU_NOT_OPEN_MMB,
                      "Ta có thể giúp gì cho ngươi?",
                      "Hướng dẫn",
                      "Từ chối");
                }
              }
            } catch (Exception e) {
              Logger.logException(NpcFactory.class, e);
            }
          } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
            if (player.fightMabu.pointMabu >= player.fightMabu.POINT_MAX) {
              this.createOtherMenu(
                  player,
                  ConstNpc.GO_UPSTAIRS_MENU,
                  "Ta có thể giúp gì cho ngươi ?",
                  "Lên Tầng!",
                  "Quay về",
                  "Từ chối");
            } else {
              this.createOtherMenu(
                  player,
                  ConstNpc.BASE_MENU,
                  "Ta có thể giúp gì cho ngươi ?",
                  "Quay về",
                  "Từ chối");
            }
          } else if (this.mapId == 120) {
            this.createOtherMenu(
                player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?", "Quay về", "Từ chối");
          } else {
            super.openBaseMenu(player);
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == 50) {
            if (player.iDMark.isBaseMenu()) {
              switch (select) {
                case 0:
                  ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                  break;
                case 1:
                  ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                  break;
              }
            }
          } else if (this.mapId == 154) {
            if (player.iDMark.isBaseMenu()) {
              switch (select) {
                case 0:
                  ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                  break;
                case 1:
                  ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                  break;
              }
            }
          } else if (this.mapId == 155) {
            if (player.iDMark.isBaseMenu()) {
              if (select == 0) {
                ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
              }
            }
          } else if (this.mapId == 52) {
            switch (player.iDMark.getIndexMenu()) {
              case ConstNpc.MENU_REWARD_MMB:
                break;
              case ConstNpc.MENU_OPEN_MMB:
                if (select == 0) {
                  NpcService.gI()
                      .createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                } else if (select == 1) {
                  //                                    if (!player.session.actived) {
                  //                                        Service.gI().sendThongBao(player, "Vui
                  // lòng kích hoạt tài khoản để sử dụng chức năng này");
                  //                                    } else
                  ChangeMapService.gI().changeMap(player, 114, -1, 318, 336);
                }
                break;
              case ConstNpc.MENU_NOT_OPEN_BDW:
                if (select == 0) {
                  NpcService.gI()
                      .createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                }
                break;
            }
          } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
            if (player.iDMark.getIndexMenu() == ConstNpc.GO_UPSTAIRS_MENU) {
              if (select == 0) {
                player.fightMabu.clear();
                ChangeMapService.gI()
                    .changeMap(
                        player, this.map.mapIdNextMabu((short) this.mapId), -1, this.cx, this.cy);
              } else if (select == 1) {
                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
              }
            } else {
              if (select == 0) {
                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
              }
            }
          } else if (this.mapId == 120) {
            if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
              if (select == 0) {
                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
              }
            }
          }
        }
      }
    };
  }

  public static Npc linhCanh(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (player.clan == null) {
            this.createOtherMenu(
                player,
                ConstNpc.IGNORE_MENU,
                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai",
                "Đóng");
            return;
          }
          if (player.clan.getMembers().size() < DoanhTraiDocNhanService.N_PLAYER_IN_CLAN) {
            this.createOtherMenu(
                player,
                ConstNpc.IGNORE_MENU,
                "Bang hội phải có ít nhất "
                    + DoanhTraiDocNhanService.N_PLAYER_IN_CLAN
                    + " thành viên mới có thể mở",
                "Đóng");
            return;
          }
          if (player.clan.doanhTraiDocNhan != null) {
            createOtherMenu(
                player,
                ConstNpc.MENU_JOIN_DOANH_TRAI,
                "Bang hội của ngươi đang đánh trại độc nhãn\n"
                    + "Thời gian còn lại là "
                    + TimeUtil.getSecondLeft(
                        player.clan.lastTimeOpenDTDN,
                        DoanhTraiDocNhanService.TIME_DOANH_TRAI / 1000)
                    + ". Ngươi có muốn tham gia không?",
                "Tham gia",
                "Không",
                "Hướng\ndẫn\nthêm");
            return;
          }
          int nPlSameClan = 0;
          for (Player pl : player.zone.getPlayers()) {
            if (!pl.equals(player)
                && pl.clan != null
                && pl.clan.equals(player.clan)
                && pl.location.x >= 1285
                && pl.location.x <= 1645) {
              nPlSameClan++;
            }
          }
          if (nPlSameClan < DoanhTraiDocNhanService.N_PLAYER_IN_MAP) {
            createOtherMenu(
                player,
                ConstNpc.IGNORE_MENU,
                "Ngươi phải có ít nhất "
                    + DoanhTraiDocNhanService.N_PLAYER_IN_MAP
                    + " đồng đội cùng bang đứng gần mới có thể\nvào\n"
                    + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
                    + "Hahaha.",
                "OK",
                "Hướng\ndẫn\nthêm");
            return;
          }
          if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
            createOtherMenu(
                player,
                ConstNpc.IGNORE_MENU,
                "Doanh trại chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
                "OK",
                "Hướng\ndẫn\nthêm");
            return;
          }
          if (!player.clan.hasGoneOpenDoanhTrai()) {
            createOtherMenu(
                player,
                ConstNpc.IGNORE_MENU,
                "Bang hội của ngươi đã đi trại lúc "
                    + TimeUtil.formatTime(player.clan.lastTimeOpenDTDN, "HH:mm:ss")
                    + " hôm nay. Hẹn ngươi quay lại vào ngày mai",
                "OK",
                "Hướng\ndẫn\nthêm");
            return;
          }
          createOtherMenu(
              player,
              ConstNpc.MENU_JOIN_DOANH_TRAI,
              "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                  + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
              "Vào\n(miễn phí)",
              "Không",
              "Hướng\ndẫn\nthêm");
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          switch (player.iDMark.getIndexMenu()) {
            case ConstNpc.MENU_JOIN_DOANH_TRAI:
              if (select == 0) {
                DoanhTraiDocNhanService.gI().openDoanhTraiDocNhan(player);
              } else if (select == 2) {
                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
              }
              break;
            case ConstNpc.IGNORE_MENU:
              if (select == 1) {
                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
              }
              break;
          }
        }
      }
    };
  }

  public static Npc quaTrung(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      private final int COST_AP_TRUNG_NHANH = 1000000000;

      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (this.mapId == (21 + player.gender)) {
            player.mabuEgg.sendMabuEgg();
            if (player.mabuEgg.getSecondDone() != 0) {
              this.createOtherMenu(
                  player,
                  ConstNpc.CAN_NOT_OPEN_EGG,
                  "Bư bư bư...",
                  "Hủy bỏ\ntrứng",
                  "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng",
                  "Đóng");
              return;
            }
            this.createOtherMenu(
                player, ConstNpc.CAN_OPEN_EGG, "Bư bư bư...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
          }
          if (this.mapId == 154) {
            player.billEgg.sendBillEgg();
            if (player.billEgg.getSecondDone() != 0) {
              this.createOtherMenu(
                  player,
                  ConstNpc.CAN_NOT_OPEN_EGG,
                  "Bư bư bư...",
                  "Hủy bỏ\ntrứng",
                  "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng",
                  "Đóng");
              return;
            }
            this.createOtherMenu(
                player, ConstNpc.CAN_OPEN_EGG, "Bư bư bư...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == (21 + player.gender)) {
            switch (player.iDMark.getIndexMenu()) {
              case ConstNpc.CAN_NOT_OPEN_EGG:
                if (select == 0) {
                  this.createOtherMenu(
                      player,
                      ConstNpc.CONFIRM_DESTROY_EGG,
                      "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?",
                      "Đồng ý",
                      "Từ chối");
                } else if (select == 1) {
                  if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                    player.inventory.gold -= COST_AP_TRUNG_NHANH;
                    player.mabuEgg.timeDone = 0;
                    Service.gI().sendMoney(player);
                    player.mabuEgg.sendMabuEgg();
                  } else {
                    Service.gI()
                        .sendThongBao(
                            player,
                            "Bạn không đủ vàng để thực hiện, còn thiếu "
                                + Util.numberToMoney((COST_AP_TRUNG_NHANH - player.inventory.gold))
                                + " vàng");
                  }
                }
                break;
              case ConstNpc.CAN_OPEN_EGG:
                switch (select) {
                  case 0:
                    this.createOtherMenu(
                        player,
                        ConstNpc.CONFIRM_OPEN_EGG,
                        "Bạn có chắc chắn cho trứng nở?\n"
                            + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư",
                        "Đệ mabư\nTrái Đất",
                        "Đệ mabư\nNamếc",
                        "Đệ mabư\nXayda",
                        "Từ chối");
                    break;
                  case 1:
                    this.createOtherMenu(
                        player,
                        ConstNpc.CONFIRM_DESTROY_EGG,
                        "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?",
                        "Đồng ý",
                        "Từ chối");
                    break;
                }
                break;
              case ConstNpc.CONFIRM_OPEN_EGG:
                switch (select) {
                  case 0:
                    player.mabuEgg.openEgg(ConstPlayer.TRAI_DAT);
                    break;
                  case 1:
                    player.mabuEgg.openEgg(ConstPlayer.NAMEC);
                    break;
                  case 2:
                    player.mabuEgg.openEgg(ConstPlayer.XAYDA);
                    break;
                  default:
                    break;
                }
                break;
              case ConstNpc.CONFIRM_DESTROY_EGG:
                if (select == 0) {
                  player.mabuEgg.destroyEgg();
                }
                break;
            }
          }
          if (this.mapId == 154) {
            switch (player.iDMark.getIndexMenu()) {
              case ConstNpc.CAN_NOT_OPEN_BILL:
                if (select == 0) {
                  this.createOtherMenu(
                      player,
                      ConstNpc.CONFIRM_DESTROY_BILL,
                      "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?",
                      "Đồng ý",
                      "Từ chối");
                } else if (select == 1) {
                  if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                    player.inventory.gold -= COST_AP_TRUNG_NHANH;
                    player.billEgg.timeDone = 0;
                    Service.gI().sendMoney(player);
                    player.billEgg.sendBillEgg();
                  } else {
                    Service.gI()
                        .sendThongBao(
                            player,
                            "Bạn không đủ vàng để thực hiện, còn thiếu "
                                + Util.numberToMoney((COST_AP_TRUNG_NHANH - player.inventory.gold))
                                + " vàng");
                  }
                }
                break;
              case ConstNpc.CAN_OPEN_EGG:
                switch (select) {
                  case 0:
                    this.createOtherMenu(
                        player,
                        ConstNpc.CONFIRM_OPEN_BILL,
                        "Bạn có chắc chắn cho trứng nở?\n"
                            + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư",
                        "Đệ mabư\nTrái Đất",
                        "Đệ mabư\nNamếc",
                        "Đệ mabư\nXayda",
                        "Từ chối");
                    break;
                  case 1:
                    this.createOtherMenu(
                        player,
                        ConstNpc.CONFIRM_DESTROY_BILL,
                        "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?",
                        "Đồng ý",
                        "Từ chối");
                    break;
                }
                break;
              case ConstNpc.CONFIRM_OPEN_BILL:
                switch (select) {
                  case 0:
                    player.billEgg.openEgg(ConstPlayer.TRAI_DAT);
                    break;
                  case 1:
                    player.billEgg.openEgg(ConstPlayer.NAMEC);
                    break;
                  case 2:
                    player.billEgg.openEgg(ConstPlayer.XAYDA);
                    break;
                  default:
                    break;
                }
                break;
              case ConstNpc.CONFIRM_DESTROY_BILL:
                if (select == 0) {
                  player.billEgg.destroyEgg();
                }
                break;
            }
          }
        }
      }
    };
  }

  public static Npc quocVuong(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        this.createOtherMenu(
            player,
            ConstNpc.BASE_MENU,
            "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?",
            "Bản thân",
            "Đệ tử",
            "Từ chối");
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (player.iDMark.isBaseMenu()) {
            switch (select) {
              case 0:
                if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                  this.createOtherMenu(
                      player,
                      ConstNpc.OPEN_POWER_MYSEFT,
                      "Ta sẽ truyền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên " + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                      "Nâng\ngiới hạn\nsức mạnh", "Nâng ngay", "Đóng");
                  return;
                }
                this.createOtherMenu(
                    player, ConstNpc.IGNORE_MENU, "Sức mạnh của con đã đạt tới giới hạn", "Đóng");
                return;
              case 1:
                if (player.pet == null) {
                  Service.gI().sendThongBao(player, "Không thể thực hiện");
                  return;
                }
                if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                  this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                      "Ta sẽ truyền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên " + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                      "Nâng ngay", "Đóng");
                  return;
                }
                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Sức mạnh của đệ con đã đạt tới giới hạn",
                    "Đóng");
                return;
            }
          }
          if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
            switch (select) {
              case 0:
                OpenPowerService.gI().openPowerBasic(player);
                break;
              case 1:
                if (player.inventory.ruby < 1000) {
                  Service.gI().sendThongBao(player, "Bạn không đủ hồng ngọc để mở");
                  return;
                }
                if (OpenPowerService.gI().openPowerSpeed(player)) {
                  player.inventory.ruby -= 1000;
                  Service.gI().sendMoney(player);
                }
                return;
            }
          }
          if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
            if (select == 0) {
              if (player.inventory.ruby < 1000) {
                Service.gI().sendThongBao(player, "Bạn không đủ hồng ngọc để mở");
                return;
              }
              if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                player.inventory.ruby -= 1000;
                Service.gI().sendMoney(player);
              }
            }
          }
        }
      }
    };
  }

  public static Npc bulmaTL(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (this.mapId == 102) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
              this.createOtherMenu(
                  player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?", "Cửa hàng", "Đóng");
            }
          }
          if (this.mapId == 5) {
            this.createOtherMenu(
                player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == 102) {
            if (player.iDMark.isBaseMenu()) {
              if (select == 0) {
                ShopServiceNew.gI().openShop(player, "BUNMA_FUTURE", true);
              }
            }
          }
          if (this.mapId == 5) {
            if (player.iDMark.isBaseMenu()) {
              if (select == 0) {
                ShopServiceNew.gI().openShop(player, "BUNMA_LINHTHU", true);
              }
            }
          }
        }
      }
    };
  }

  public static Npc rongOmega(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          BlackBallWar.gI().setTime();
          if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
            try {
              long now = System.currentTimeMillis();
              if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                this.createOtherMenu(
                    player,
                    ConstNpc.MENU_OPEN_BDW,
                    "Đường đến với ngọc rồng sao đen đã mở, " + "ngươi có muốn tham gia không?",
                    "Hướng dẫn\nthêm",
                    "Tham gia",
                    "Từ chối");
              } else {
                String[] optionRewards = new String[7];
                int index = 0;
                for (int i = 0; i < 7; i++) {
                  if (player.rewardBlackBall.timeOutOfDateReward[i] > System.currentTimeMillis()) {
                    String quantily =
                        player.rewardBlackBall.quantilyBlackBall[i] > 1
                            ? "x" + player.rewardBlackBall.quantilyBlackBall[i] + " "
                            : "";
                    optionRewards[index] = quantily + (i + 1) + " sao";
                    index++;
                  }
                }
                if (index != 0) {
                  String[] options = new String[index + 1];
                  if (index >= 0) System.arraycopy(optionRewards, 0, options, 0, index);
                  options[options.length - 1] = "Từ chối";
                  this.createOtherMenu(
                      player,
                      ConstNpc.MENU_REWARD_BDW,
                      "Ngươi có một vài phần thưởng ngọc " + "rồng sao đen đây!",
                      options);
                } else {
                  this.createOtherMenu(
                      player,
                      ConstNpc.MENU_NOT_OPEN_BDW,
                      "Ta có thể giúp gì cho ngươi?",
                      "Hướng dẫn",
                      "Từ chối");
                }
              }
            } catch (Exception e) {
              Logger.logException(NpcFactory.class, e);
            }
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          switch (player.iDMark.getIndexMenu()) {
            case ConstNpc.MENU_REWARD_BDW:
              player.rewardBlackBall.getRewardSelect((byte) select);
              break;
            case ConstNpc.MENU_OPEN_BDW:
              if (select == 0) {
                NpcService.gI()
                    .createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
              } else if (select == 1) {
                //                                if (!player.session.actived) {
                //                                    Service.gI().sendThongBao(player, "Vui lòng
                // kích hoạt tài khoản để sử dụng chức năng này");
                //
                //                                } else
                player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                ChangeMapService.gI().openChangeMapTab(player);
              }
              break;
            case ConstNpc.MENU_NOT_OPEN_BDW:
              if (select == 0) {
                NpcService.gI()
                    .createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
              }
              break;
          }
        }
      }
    };
  }

  public static Npc rong1_to_7s(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (player.iDMark.isHoldBlackBall()) {
            this.createOtherMenu(
                player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?", "Phù hộ", "Từ chối");
          } else {
            if (BossManager.gI().existBossOnPlayer(player)
                || player.zone.items.stream()
                    .anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
              this.createOtherMenu(
                  player,
                  ConstNpc.MENU_OPTION_GO_HOME,
                  "Ta có thể giúp gì cho ngươi?",
                  "Về nhà",
                  "Từ chối");
            } else {
              this.createOtherMenu(
                  player,
                  ConstNpc.MENU_OPTION_GO_HOME,
                  "Ta có thể giúp gì cho ngươi?",
                  "Về nhà",
                  "Từ chối",
                  "Gọi BOSS");
            }
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
            if (select == 0) {
              this.createOtherMenu(
                  player,
                  ConstNpc.MENU_OPTION_PHU_HP,
                  "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                  "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " vàng",
                  "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " vàng",
                  "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " vàng",
                  "Từ chối");
            }
          } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
            if (select == 0) {
              ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
            } else if (select == 2) {
              BossManager.gI().callBoss(player, mapId);
            } else if (select == 1) {
              this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
            }
          } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
            if (player.effectSkin.xHPKI > 1) {
              Service.gI().sendThongBao(player, "Bạn đã được phù hộ rồi!");
              return;
            }
            switch (select) {
              case 0:
                BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                break;
              case 1:
                BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                break;
              case 2:
                BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                break;
              case 3:
                this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                break;
            }
          }
        }
      }
    };
  }

  public static Npc npcThienSu64(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        switch (this.mapId) {
          case 0:
          case 7:
          case 14:
            {
              this.createOtherMenu(
                  player,
                  ConstNpc.BASE_MENU,
                  "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                      + "\n 3. chi phí vào cổng  50 triệu vàng",
                  "Tới ngay",
                  "Từ chối");
              break;
            }
          case 201:
            {
              this.createOtherMenu(
                  player,
                  ConstNpc.BASE_MENU,
                  "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được",
                  "Trốn về",
                  "Ở lại");
              break;
            }
          case 48:
            {
              this.createOtherMenu(
                  player,
                  ConstNpc.BASE_MENU,
                  "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!",
                  "Hướng Dẫn",
                  "Đổi SKH VIP",
                  "Từ Chối");
              break;
            }
        }
      }

      // if (player.inventory.gold < 500000000) {
      //                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền
      // rồi\nẢo ít thôi con", "Đóng");
      //                return;
      //            }
      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (player.iDMark.isBaseMenu()) {
            switch (this.mapId) {
              case 0:
              case 7:
              case 14:
                {
                  this.npcChat(player, "Chức Năng Đang Được Update!");
//                  if (select == 0) {
//                    if (player.session.player.nPoint.power >= 80000000000L
//                        && player.inventory.gold > COST_HD) {
//                      player.inventory.gold -= COST_HD;
//                      Service.gI().sendMoney(player);
//                      ChangeMapService.gI().changeMapBySpaceShip(player, 201, -1, 54);
//                    } else {
//                      this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
//                    }
//                  }
                  break;
                }
              case 201:
                {
                  this.npcChat(player, "Chức Năng Đang Được Update!");
//                  if (select == 0) {
//                    ChangeMapService.gI().changeMapBySpaceShip(player, 7 * player.gender, -1, 450);
//                  }
                  break;
                }
              case 48:
                {
                  this.npcChat(player, "Chức Năng Đang Được Update!");
//                  if (select == 0) {
//                    NpcService.gI()
//                        .createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOI_SKH_VIP);
//                  }
//                  if (select == 1) {
//                    CombineService.gI().openTabCombine(player, CombineService.NANG_CAP_SKH_VIP);
//                  }
                }
            }
          } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_SKH_VIP) {
            if (select == 0) {
              CombineService.gI().startCombine(player);
            }
          }
        }
      }
    };
  }

  public static Npc bill(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          createOtherMenu(
              player,
              ConstNpc.BASE_MENU,
              "Ngươi chỉ cần mang Mảnh Chiến Lực đến đây\n Ta sẽ giúp ngươi có được những trang bị\n xịn nhất của ta!",
              "Shop Bill",
              "Đóng");
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == 48) {
            if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
              if (select == 0) {
                createOtherMenu(
                    player,
                    ConstNpc.IGNORE_MENU,
                    "Ngươi đang có: " + player.inventory.coupon + " điểm",
                    "Đóng");
                return;
              }
              if (select == 1) {
                ShopServiceNew.gI().openShop(player, "BILL", false);
              }
            }
          }
        }
      }
    };
  }

  public static Npc boMong(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (this.mapId == 47 || this.mapId == 84) {
            this.createOtherMenu(
                player,
                ConstNpc.BASE_MENU,
                "|2|Hông nàm mà đoài cóa ăn thì chỉ cóa ăn \b|7|dau buoi an cut!",
                "Nhiệm vụ\nhàng ngày",
                "Từ chối");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == 47 || this.mapId == 84) {
            if (player.iDMark.isBaseMenu()) {
              if (select == 0) {
                if (player.playerTask.sideTask.template != null) {
                  String npcSay =
                      "Nhiệm vụ hiện tại: "
                          + player.playerTask.sideTask.getName()
                          + " ("
                          + player.playerTask.sideTask.getLevel()
                          + ")"
                          + "\nHiện tại đã hoàn thành: "
                          + player.playerTask.sideTask.count
                          + "/"
                          + player.playerTask.sideTask.maxCount
                          + " ("
                          + player.playerTask.sideTask.getPercentProcess()
                          + "%)\nSố nhiệm vụ còn lại trong ngày: "
                          + player.playerTask.sideTask.leftTask
                          + "/"
                          + ConstTask.MAX_SIDE_TASK;
                  this.createOtherMenu(
                      player,
                      ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                      npcSay,
                      "Trả nhiệm\nvụ",
                      "Hủy nhiệm\nvụ");
                } else {
                  this.createOtherMenu(
                      player,
                      ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                      "Tôi có vài nhiệm vụ theo cấp bậc, " + "sức cậu có thể làm được cái nào?",
                      "Dễ",
                      "Bình thường",
                      "Khó",
                      "Siêu khó",
                      "Địa ngục",
                      "Từ chối");
                }
              }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
              switch (select) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                  TaskService.gI().changeSideTask(player, (byte) select);
                  break;
              }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
              switch (select) {
                case 0:
                  TaskService.gI().paySideTask(player);
                  break;
                case 1:
                  TaskService.gI().removeSideTask(player);
                  break;
              }
            }
          }
        }
      }
    };
  }

  public static Npc karin(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (this.mapId == 46) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
              this.createOtherMenu(
                  player,
                  ConstNpc.BASE_MENU,
                  "|0|Monzy"
                      + "\n|1|Monzy"
                      + "\n|2|Monzy"
                      + "\n|3|Monzy"
                      + "\n|4|Monzy"
                      + "\n|5|Monzy"
                      + "\n|6|Monzy"
                      + "\n|7|Monzy"
                      + "\n|8|Monzy"
                      + "\n|9|Monzy"
                      + "\n|10|Monzy"
                      + "\n|11|Monzy"
                      + "\n|12|Monzy"
                      + "\n|13|Monzy"
                      + "\n|14|Monzy"
                      + "\n|15|Monzy",
                  "Cửa hàng",
                  "Đóng");
            }
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == 46) {
            if (player.iDMark.isBaseMenu()) {
              if (select == 0) {
                ShopServiceNew.gI().openShop(player, "KARIN", true);
              }
            }
          } else if (this.mapId == 5) {
            if (player.iDMark.isBaseMenu()) {
              if (select == 0) {
                ShopServiceNew.gI().openShop(player, "BUNMA_LINHTHU", true);
              }
            }
          }
        }
      }
    };
  }

  public static Npc vados(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          createOtherMenu(
              player,
              ConstNpc.BASE_MENU,
              "|2|Ta Vừa Hắc Mắp Xêm Được Tóp Của Toàn Server\b|7|Người Muốn Xem Tóp Gì?",
              "TOP Sức Mạnh",
              "TOP Nhiệm Vụ",
              "TOP NẠP",
              "Đóng");
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == 5) {
            if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
              if (select == 0) {
                Service.gI().showListTop(player, Manager.TOP_SM);
                return;
              }
              if (select == 1) {
                Service.gI().showListTop(player, Manager.TOP_NV);
                return;
              }
              if (select == 2) {
                Service.gI().showListTop(player, Manager.TOP_NAP);
                //
                // Service.getInstance().sendThongBaoOK(player, TopService.getTopNap());
              }
            }
          }
        }
      }
    };
  }

  public static Npc gokuSSJ_1(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (this.mapId == 80) {
            this.createOtherMenu(
                player,
                ConstNpc.BASE_MENU,
                "Xin chào, tôi có thể giúp gì cho cậu?",
                "Tới hành tinh\nYardart",
                "Từ chối");
            return;
          }
          if (this.mapId == 131) {
            this.createOtherMenu(
                player,
                ConstNpc.BASE_MENU,
                "Xin chào, tôi có thể giúp gì cho cậu?",
                "Quay về",
                "Từ chối");
            return;
          }
          super.openBaseMenu(player);
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
            if (this.mapId == 131) {
              if (select == 0) {
                ChangeMapService.gI().changeMapBySpaceShip(player, 80, -1, 870);
              }
            }
          }
        }
      }
    };
  }

  public static Npc mavuong(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (this.mapId == 153) {
            this.createOtherMenu(
                player,
                ConstNpc.BASE_MENU,
                "Xin chào, tôi có thể giúp gì cho cậu?",
                "Tây thánh địa",
                "Từ chối");
          } else if (this.mapId == 156) {
            this.createOtherMenu(
                player, ConstNpc.BASE_MENU, "Người muốn trở về?", "Quay về", "Từ chối");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == 153) {
            if (player.iDMark.isBaseMenu()) {
              if (select == 0) {
                // đến tay thanh dia
                ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 360);
              }
            }
          } else if (this.mapId == 156) {
            if (player.iDMark.isBaseMenu()) {
              // về lanh dia bang hoi
              if (select == 0) {
                ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
              }
            }
          }
        }
      }
    };
  }

  public static Npc gokuSSJ_2(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          try {
            Item biKiep = InventoryService.gI().findItem(player.inventory.itemsBag, 590);
            if (biKiep != null) {
              this.createOtherMenu(
                  player,
                  ConstNpc.BASE_MENU,
                  "Bạn đang có "
                      + biKiep.quantity
                      + " bí kiếp.\n"
                      + "Hãy kiếm đủ 10000 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart",
                  "Học dịch\nchuyển",
                  "Đóng");
            }
          } catch (Exception e) {
            Logger.logException(NpcFactory.class, e);
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          try {
            Item biKiep = InventoryService.gI().findItem(player.inventory.itemsBag, 590);
            if (biKiep != null) {
              if (biKiep.quantity >= 10000 && InventoryService.gI().getCountEmptyBag(player) > 0) {
                Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
                yardart.itemOptions.add(new Item.ItemOption(47, 400));
                yardart.itemOptions.add(new Item.ItemOption(108, 10));
                InventoryService.gI().addItemBag(player, yardart);
                InventoryService.gI().subQuantityItemsBag(player, biKiep, 10000);
                InventoryService.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được trang phục tộc Yardart");
              }
            }
          } catch (Exception e) {
            Logger.logException(NpcFactory.class, e);
          }
        }
      }
    };
  }

  public static Npc khidaumoi(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (this.mapId == 14) {
          this.createOtherMenu(
              player,
              ConstNpc.BASE_MENU,
              "Bạn muốn nâng cấp khỉ ư?",
              "Nâng cấp\nkhỉ",
              "Shop của Khỉ",
              "Từ chối");
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        this.npcChat(player, "Chức Năng Đang Được Update!");
        return;
        //        if (canOpenNpc(player)) {
        //          if (this.mapId == 14) {
        //            if (player.iDMark.isBaseMenu()) {
        //              switch (select) {
        //                case 0:
        //                  this.createOtherMenu(
        //                      player,
        //                      1,
        //                      "|7|Cần Khỉ Lv1,2,3,4,5,6,7 để nâng cấp lên ct khỉ cấp cao
        // hơn\b|2|Mỗi lần nâng cấp tiếp thì mỗi cấp cần thêm 10 đá ngũ sắc",
        //                      "Nâng cấp",
        //                      "Từ chối");
        //                  break;
        //                case 1: // shop
        //                  ShopServiceNew.gI().openShop(player, "KHI", false);
        //                  break;
        //              }
        //            } else if (player.iDMark.getIndexMenu() == 1) {
        //              switch (select) {
        //                case 0:
        //                  CombineService.gI().openTabCombine(player, CombineService.NANG_CAP_KHI);
        //                  break;
        //                case 1:
        //                  break;
        //              }
        //            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_KHI) {
        //              if (player.conbine.typeCombine == CombineService.NANG_CAP_KHI) {
        //                if (select == 0) {
        //                  CombineService.gI().startCombine(player);
        //                }
        //              }
        //            }
        //          }
        //        }
      }
    };
  }

  public static Npc GhiDanh(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      String[] menuselect = new String[]{};

      @Override
      public void openBaseMenu(Player pl) {
        if (canOpenNpc(pl)) {
          if (this.mapId == 52) {
            createOtherMenu(pl, 0, DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).Giai(pl), "Thông tin\nChi tiết", DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).CanReg(pl) ? "Đăng ký" : "OK", "Đại Hội\nVõ Thuật\nLần thứ\n23");
          } else if (this.mapId == 129) {
            int goldChallenge = pl.goldChallenge;
            if (pl.levelWoodChest == 0) {
              menuselect = new String[]{"Hướng\ndẫn\nthêm", "Thi đấu\n" + Util.numberToMoney(goldChallenge) + " vàng", "Về\nĐại Hội\nVõ Thuật"};
            } else {
              menuselect = new String[]{"Hướng\ndẫn\nthêm", "Thi đấu\n" + Util.numberToMoney(goldChallenge) + " vàng", "Nhận thưởng\nRương cấp\n" + pl.levelWoodChest, "Về\nĐại Hội\nVõ Thuật"};
            }
            this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào", menuselect, "Từ chối");
          } else {
            super.openBaseMenu(pl);
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          if (this.mapId == 52) {
            switch (select) {
              case 0:
                Service.gI().sendPopUpMultiLine(player, tempId, avartar, DaiHoiVoThuat.gI().Info());
                break;
              case 1:
                if (DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).CanReg(player)) {
                  DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).Reg(player);
                }
                break;
              case 2:
                ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                break;
            }
          } else if (this.mapId == 129) {
            int goldchallenge = player.goldChallenge;
            if (player.levelWoodChest == 0) {
              switch (select) {
                case 0:
                  NpcService.gI().createTutorial(player, this.avartar, ConstNpc.NPC_DHVT23);
                  break;
                case 1:
                  if (!InventoryService.gI().findItemWoodChest(player)) {
                    if (player.inventory.gold >= goldchallenge) {
                      MartialCongressService.gI().startChallenge(player);
                      player.inventory.gold -= (goldchallenge);
                      PlayerService.gI().sendInfoHpMpMoney(player);
                      player.goldChallenge += 2000000;
                    } else {
                      Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                    }
                  } else {
                    Service.gI().sendThongBao(player, "Hãy mở rương báu vật trước");
                  }
                  break;
                case 2:
                  ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                  break;
              }
            } else {
              switch (select) {
                case 0:
                  NpcService.gI().createTutorial(player, this.avartar, ConstNpc.NPC_DHVT23);
                  break;
                case 1:
                  if (InventoryService.gI().findItemWoodChest(player)) {
                    if (player.inventory.gold >= goldchallenge) {
                      MartialCongressService.gI().startChallenge(player);
                      player.inventory.gold -= (goldchallenge);
                      PlayerService.gI().sendInfoHpMpMoney(player);
                      player.goldChallenge += 2000000;
                    } else {
                      Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                    }
                  } else {
                    Service.gI().sendThongBao(player, "Hãy mở rương báu vật trước");
                  }
                  break;
                case 2:
                  if (!player.receivedWoodChest) {
                    if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                      Item it = ItemService.gI().createNewItem((short) 570);
                      it.itemOptions.add(new Item.ItemOption(72, player.levelWoodChest));
                      it.itemOptions.add(new Item.ItemOption(30, 0));
                      it.createTime = System.currentTimeMillis();
                      InventoryService.gI().addItemBag(player, it);
                      InventoryService.gI().sendItemBags(player);
                      player.receivedWoodChest = true;
                      player.levelWoodChest = 0;
                      Service.gI().sendThongBao(player, "Bạn nhận được rương gỗ");
                    } else {
                      this.npcChat(player, "Hành trang đã đầy");
                    }
                  } else {
                    Service.gI().sendThongBao(player, "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                  }
                  break;
                case 3:
                  ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                  break;
              }
            }
          }
        }
      }
    };
  }

  public static Npc unknow(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (this.mapId == 5) {
            this.createOtherMenu(
                player,
                0,
                "Éc éc Bạn muốn gì ở tôi :3?",
                "Đến Võ đài Unknow",
                "Đổi Rương Đồng Vàng",
                "Hướng Dẫn Sự Kiện");
          }
          if (this.mapId == 112) {
            this.createOtherMenu(
                player,
                0,
                "Bạn đang còn : " + player.pointPvp + " điểm PvP Point",
                "Về đảo Kame",
                "Đổi Cải trang sự kiên",
                "Top PVP");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        this.npcChat(player, "Chức Năng Đang Được Update!");
        return;
        //        if (canOpenNpc(player)) {
        //          if (this.mapId == 5) {
        //            if (player.iDMark.getIndexMenu() == 0) { //
        //              switch (select) {
        //                case 0:
        //                  if (player.session.player.nPoint.power >= 10000000000L) {
        //                    ChangeMapService.gI().changeMapBySpaceShip(player, 112, -1, 495);
        //                    Service.gI().changeFlag(player, Util.nextInt(8));
        //                  } else {
        //                    this.npcChat(player, "Bạn cần 10 tỷ sức mạnh mới có thể vào");
        //                  }
        //                  break; // qua vo dai
        //                case 1:
        //                  Input.gI().createFormTradeRuongDongVang(player);
        //                  break;
        //                case 2:
        //                  NpcService.gI().createTutorial(player, this.avartar,
        // ConstNpc.HUONG_DAN_SK);
        //                  break;
        //              }
        //            }
        //          }
        //          if (this.mapId == 112) {
        //            if (player.iDMark.getIndexMenu() == 0) { //
        //              switch (select) {
        //                case 0:
        //                  ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 319);
        //                  break; // ve dao kame
        //                case 1: //
        //                  this.createOtherMenu(
        //                      player,
        //                      1,
        //                      "Bạn có muốn đổi 500 điểm PVP lấy \n|6|Cải trang Mèo Kid Lân với tất
        // cả chỉ số là 80%\n ",
        //                      "Ok",
        //                      "Không");
        //                  // bat menu doi item
        //                  break;
        //                case 2: //
        //                  //                                    Service.gI().showListTop(player,
        //                  // Manager.topPVP);
        //                  // mo top pvp
        //                  break;
        //              }
        //            }
        //            if (player.iDMark.getIndexMenu() == 1) { // action doi item
        //              if (select == 0) { // trade
        //                if (player.pointPvp >= 500) {
        //                  player.pointPvp -= 500;
        //                  Item item = ItemService.gI().createNewItem((short) (1104));
        //                  item.itemOptions.add(new Item.ItemOption(49, 30));
        //                  item.itemOptions.add(new Item.ItemOption(77, 15));
        //                  item.itemOptions.add(new Item.ItemOption(103, 20));
        //                  item.itemOptions.add(new Item.ItemOption(207, 0));
        //                  item.itemOptions.add(new Item.ItemOption(33, 0));
        //                  //
        //                  InventoryService.gI().addItemBag(player, item);
        //                  Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Cải Trang Thành
        // Công !");
        //                } else {
        //                  Service.gI()
        //                      .sendThongBao(
        //                          player, "Không đủ điểm bạn còn " + (500 - player.pointPvp) + "
        // Điểm nữa");
        //                }
        //              }
        //            }
        //          }
        //        }
      }
    };
  }

  public static Npc monaito(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (this.mapId == 7) {
            this.createOtherMenu(
                player, 0, "Chào bạn tôi sẽ đưa bạn đến hành tinh Cereal?", "Đồng ý", "Từ chối");
          }
          if (this.mapId == 170) {
            this.createOtherMenu(player, 0, "Ta ở đây để đưa con về", "Về Làng Mori", "Từ chối");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        this.npcChat(player, "Chức Năng Đang Được Update!");
        return;
//        if (canOpenNpc(player)) {
//          if (this.mapId == 7) {
//            if (player.iDMark.getIndexMenu() == 0) { //
//              if (select == 0) {
//                ChangeMapService.gI().changeMapBySpaceShip(player, 170, -1, 264);
//                // den hanh tinh cereal
//              }
//            }
//          }
//          if (this.mapId == 170) {
//            if (player.iDMark.getIndexMenu() == 0) { //
//              if (select == 0) {
//                ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 432);
//                // quay ve
//              }
//            }
//          }
//        }
      }
    };
  }

  public static Npc granala(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          if (this.mapId == 171) {
            this.createOtherMenu(
                player,
                0,
                "Ngươi!\n Hãy cầm đủ 7 viên ngọc rồng \n Monaito đến đây gặp ta ta sẽ ban cho ngươi\n 1 điều ước ",
                "Gọi rồng",
                "Từ chối");
          }
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        this.npcChat(player, "Chức Năng Đang Được Update!");
        return;
//        if (canOpenNpc(player)) {
//          if (this.mapId == 171) {
//            if (player.iDMark.getIndexMenu() == 0) { //
//              if (select == 0) {
//                this.npcChat(player, "Chức Năng Đang Được Update!");
//                // goi rong
//              }
//            }
//          }
//        }
      }
    };
  }

  public static Npc event(int mapId, int status, int cx, int cy, int tempId, int avatar) {
    return new Npc(mapId, status, cx, cy, tempId, avatar) {
      @Override
      public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
          this.createOtherMenu(
              player,
              ConstNpc.MENU_EVENT,
              ConstNpc.HUONG_DAN_SK_HE,
              "Hướng dẫn\nnạp tự động",
              "Hướng dẫn\nmở thành viên",
              "Đổi thức ăn",
              "Đổi Pet",
              "Đổi đeo lưng");
        }
      }

      @Override
      public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
          switch (select) {
            case 0:
              NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_NAP_TU_DONG);
              break;
            case 1:
              NpcService.gI()
                  .createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MO_THANH_VIEN);
              break;
            default:
              this.npcChat(player, "Chức Năng Đang Được Update!");
//            case 2:
//              {
//                Item traiDua = InventoryService.gI().findItemBag(player, 694);
//                if (traiDua == null) {
//                  Service.gI().sendThongBao(player, "Thiếu nguyên liệu rồi.");
//                  return;
//                }
//                InventoryService.gI().subQuantityItemsBag(player, traiDua, 1);
//                Item meal = ItemService.gI().createNewItem((short) Util.nextInt(880, 882));
//                InventoryService.gI().addItemBag(player, meal);
//                InventoryService.gI().sendItemBags(player);
//                Service.gI().sendThongBao(player, "Bạn đã nhận được 1 " + meal.template.name + ".");
//                break;
//              }
//            case 3:
//              {
//                Item conCua = InventoryService.gI().findItemBag(player, 697);
//                Item saoBien = InventoryService.gI().findItemBag(player, 698);
//                if (conCua == null
//                    || saoBien == null
//                    || conCua.quantity < 10
//                    || saoBien.quantity < 10) {
//                  Service.gI().sendThongBao(player, "Thiếu nguyên liệu rồi.");
//                  return;
//                }
//                InventoryService.gI().subQuantityItemsBag(player, conCua, 10);
//                InventoryService.gI().subQuantityItemsBag(player, saoBien, 10);
//                Item pet = ItemService.gI().randomItemEvent((short) 1273);
//                InventoryService.gI().addItemBag(player, pet);
//                InventoryService.gI().sendItemBags(player);
//                Service.gI()
//                    .sendThongBao(player, "Bạn đã nhận được Pet: " + pet.template.name + ".");
//                break;
//              }
//            case 4:
//              {
//                Item voSo = InventoryService.gI().findItemBag(player, 695);
//                Item voOc = InventoryService.gI().findItemBag(player, 696);
//                Item saoBien = InventoryService.gI().findItemBag(player, 698);
//                if (voSo == null
//                    || voOc == null
//                    || saoBien == null
//                    || voSo.quantity < 99
//                    || voOc.quantity < 99
//                    || saoBien.quantity < 10) {
//                  Service.gI().sendThongBao(player, "Thiếu nguyên liệu rồi.");
//                  return;
//                }
//                InventoryService.gI().subQuantityItemsBag(player, voSo, 99);
//                InventoryService.gI().subQuantityItemsBag(player, voOc, 99);
//                InventoryService.gI().subQuantityItemsBag(player, saoBien, 10);
//                short[] idsDeoLung = {994, 996, 997};
//                Item deoLung = ItemService.gI().randomItemEvent(idsDeoLung[Util.nextInt(0, 2)]);
//                InventoryService.gI().addItemBag(player, deoLung);
//                InventoryService.gI().sendItemBags(player);
//                Service.gI()
//                    .sendThongBao(
//                        player, "Bạn đã nhận được Đeo lưng: " + deoLung.template.name + ".");
//                break;
//              }
          }
        }
      }
    };
  }
  //    Service.gI().showListTop(player, Manager.topNV);

  public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId) {
    int avatar = Manager.NPC_TEMPLATES.get(tempId).avatar;
    try {
      switch (tempId) {
        case ConstNpc.UNKOWN:
          return unknow(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.GHI_DANH:
          return GhiDanh(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.TRUNG_LINH_THU:
          return trungLinhThu(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.POTAGE:
          return potage(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.QUY_LAO_KAME:
          return quyLaoKame(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.THO_DAI_CA:
          return thoDaiKa(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.TRUONG_LAO_GURU:
          return truongLaoGuru(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.VUA_VEGETA:
          return vuaVegeta(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.ONG_GOHAN:
        case ConstNpc.ONG_MOORI:
        case ConstNpc.ONG_PARAGUS:
          return ongGohan_ongMoori_ongParagus(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.BUNMA:
          return bulmaQK(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.DENDE:
          return dende(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.APPULE:
          return appule(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.DR_DRIEF:
          return drDrief(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.CARGO:
          return cargo(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.CUI:
          return cui(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.SANTA:
          return santa(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.URON:
          return uron(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.BA_HAT_MIT:
          return baHatMit(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.RUONG_DO:
          return ruongDo(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.DAU_THAN:
          return dauThan(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.CALICK:
          return calick(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.JACO:
          return jaco(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.THUONG_DE:
          return thuongDe(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.CUA_HANG_KY_GUI:
          return kyGui(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.Granola:
          return granala(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.GIUMA_DAU_BO:
          return mavuong(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.Monaito:
          return monaito(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.VADOS:
          return vados(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.KHI_DAU_MOI:
          return khidaumoi(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.THAN_VU_TRU:
          return thanVuTru(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.KIBIT:
          return kibit(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.OSIN:
          return osin(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.LY_TIEU_NUONG:
          return npcLyTieuNuong54(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.LINH_CANH:
          return linhCanh(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.QUA_TRUNG:
          return quaTrung(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.QUOC_VUONG:
          return quocVuong(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.BUNMA_TL:
          return bulmaTL(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.RONG_OMEGA:
          return rongOmega(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.RONG_1S:
        case ConstNpc.RONG_2S:
        case ConstNpc.RONG_3S:
        case ConstNpc.RONG_4S:
        case ConstNpc.RONG_5S:
        case ConstNpc.RONG_6S:
        case ConstNpc.RONG_7S:
          return rong1_to_7s(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.NPC_64:
          return npcThienSu64(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.BILL:
          return bill(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.BO_MONG:
          return boMong(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.THAN_MEO_KARIN:
          return karin(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.GOKU_SSJ:
          return gokuSSJ_1(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.GOKU_SSJ_:
          return gokuSSJ_2(mapId, status, cx, cy, tempId, avatar);
        case ConstNpc.DUONG_TANG:
          return duongtank(mapId, status, cx, cy, tempId, avatar);
        case 78:
          return event(mapId, status, cx, cy, tempId, avatar);
        default:
          return new Npc(mapId, status, cx, cy, tempId, avatar) {
            @Override
            public void openBaseMenu(Player player) {
              if (canOpenNpc(player)) {
                super.openBaseMenu(player);
              }
            }

            @Override
            public void confirmMenu(Player player, int select) {
              canOpenNpc(
                  player); //                                ShopService.gI().openShopNormal(player,
              // this, ConstNpc.SHOP_BUNMA_TL_0, 0, player.gender);
            }
          };
      }
    } catch (Exception e) {
      Logger.logException(NpcFactory.class, e, "Lỗi load npc");
      return null;
    }
  }

  // girlbeo-mark
  public static void createNpcRongThieng() {
    Npc npc =
        new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
          @Override
          public void confirmMenu(Player player, int select) {
            switch (player.iDMark.getIndexMenu()) {
              case ConstNpc.IGNORE_MENU:
                break;
              case ConstNpc.SHENRON_CONFIRM:
                if (select == 0) {
                  SummonDragon.gI().confirmWish();
                } else if (select == 1) {
                  SummonDragon.gI().reOpenShenronWishes(player);
                }
                break;
              case ConstNpc.SHENRON_1_1:
                if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1
                    && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                  NpcService.gI()
                      .createMenuRongThieng(
                          player, ConstNpc.SHENRON_1_2, SHENRON_SAY, SHENRON_1_STAR_WISHES_2);
                  break;
                }
              case ConstNpc.SHENRON_1_2:
                if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2
                    && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                  NpcService.gI()
                      .createMenuRongThieng(
                          player, ConstNpc.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                  break;
                }
              default:
                SummonDragon.gI()
                    .showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                break;
            }
          }
        };
  }

  public static void createNpcConMeo() {
    Npc npc =
        new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
          @Override
          public void confirmMenu(Player player, int select) {
            switch (player.iDMark.getIndexMenu()) {
              case ConstNpc.IGNORE_MENU:
                break;
              case ConstNpc.MAKE_MATCH_PVP:
                // Thách đấu
                if (Maintenance.isRunning) {
                  break;
                }
                PVPService.gI().sendInvitePVP(player, (byte) select);
                break;
              case ConstNpc.MAKE_FRIEND:
                // Kết bạn
                if (select == 0) {
                  Object playerId = PLAYER_ID_OBJECT.get(player.id);
                  if (playerId != null) {
                    FriendAndEnemyService.gI()
                        .acceptMakeFriend(player, Integer.parseInt(String.valueOf(playerId)));
                  }
                }
                break;
              case ConstNpc.REVENGE:
                // Trả thù
                if (select == 0) {
                  PVPService.gI().acceptRevenge(player);
                }
                break;
              case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                // Hướng dẫn gọi rồng thần
                if (select == 0) {
                  NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                }
                break;
              case ConstNpc.SUMMON_SHENRON:
                // Gọi rồng
                if (select == 0) {
                  NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                } else if (select == 1) {
                  SummonDragon.gI().summonShenron(player);
                }
                break;
              case ConstNpc.MENU_OPTION_USE_ITEM1105:
                // Hộp đồ thiên sứ
                if (select == 0) {
                  IntrinsicService.gI().setTSTD(player);
                } else if (select == 1) {
                  IntrinsicService.gI().setTSNM(player);
                } else if (select == 2) {
                  IntrinsicService.gI().setTSXD(player);
                }
                break;
              case ConstNpc.MENU_OPTION_USE_ITEM2000:
              case ConstNpc.MENU_OPTION_USE_ITEM2001:
              case ConstNpc.MENU_OPTION_USE_ITEM2002:
                // Hòm tiếp tế -> Set KH
                ItemService.gI().sendDKH(player, player.iDMark.getIndexMenu(), select);
                break;
              case ConstNpc.MENU_OPTION_USE_ITEM2003:
              case ConstNpc.MENU_OPTION_USE_ITEM2004:
              case ConstNpc.MENU_OPTION_USE_ITEM2005:
                // Rương hủy diệt -> Set HD
                ItemService.gI().sendDHD(player, player.iDMark.getIndexMenu(), select);
                break;
              case ConstNpc.INTRINSIC:
                // Nội tại
                if (select == 0) {
                  IntrinsicService.gI().showAllIntrinsic(player);
                } else if (select == 1) {
                  IntrinsicService.gI().showConfirmOpen(player);
                } else if (select == 2) {
                  IntrinsicService.gI().showConfirmOpenVip(player);
                }
                break;
              case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                // Mở nội tại thường
                if (select == 0) {
                  IntrinsicService.gI().open(player);
                }
                break;
              case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                // Mở nội tại VIP
                if (select == 0) {
                  IntrinsicService.gI().openVip(player);
                }
                break;
              case ConstNpc.CONFIRM_LEAVE_CLAN:
                if (select == 0) {
                  ClanService.gI().leaveClan(player);
                }
                break;
              case ConstNpc.CONFIRM_NHUONG_PC:
                if (select == 0) {
                  ClanService.gI().phongPc(player, (int) PLAYER_ID_OBJECT.get(player.id));
                }
                break;
              case ConstNpc.BAN_PLAYER:
                if (select == 0) {
                  PlayerService.gI().banPlayer((Player) PLAYER_ID_OBJECT.get(player.id));
                  Service.gI()
                      .sendThongBao(
                          player,
                          "Ban người chơi "
                              + ((Player) PLAYER_ID_OBJECT.get(player.id)).name
                              + " thành công");
                }
                break;
              case ConstNpc.BUFF_PET:
                if (select == 0) {
                  Player pl = (Player) PLAYER_ID_OBJECT.get(player.id);
                  if (pl.pet == null) {
                    PetService.gI().createNormalPet(pl);
                    Service.gI()
                        .sendThongBao(
                            player,
                            "Phát đệ tử cho "
                                + ((Player) PLAYER_ID_OBJECT.get(player.id)).name
                                + " thành công");
                  }
                }
                break;
              case ConstNpc.UP_TOP_ITEM:
                break;
              case ConstNpc.MENU_ADMIN:
                switch (select) {
                  case 0:
                    if (player.isAdmin()) {
                      Maintenance.gI().start(15);
                    }
                    break;
                  case 1:
                    Input.gI().createFormFindPlayer(player);
                    break;
                  case 2:
                    GiftCodeManager.gI().checkInformationGiftCode(player);
                    break;
                  case 3:
                    Input.gI().createFormNapForAdmin(player);
                    break;
                  case 4:
                    Input.gI().createFormMTV(player);
                    break;
                  case 5:
                    Input.gI().createFormSendRuby(player);
                    break;
                }
                break;
              case ConstNpc.MENU_MOD:
                if (select == 0) {
                  Input.gI().createFormMTV(player);
                }
                break;
              case ConstNpc.MENU_SKH_THIEN_SU_TD:
                switch (select) {
                  case 0:
                    ItemService.gI().sendSetTSKichHoat(player, 127);
                    break;
                  case 1:
                    ItemService.gI().sendSetTSKichHoat(player, 128);
                    break;
                  case 2:
                    ItemService.gI().sendSetTSKichHoat(player, 129);
                    break;
                }
                break;
              case ConstNpc.MENU_SKH_THIEN_SU_NM:
                switch (select) {
                  case 0:
                    ItemService.gI().sendSetTSKichHoat(player, 130);
                    break;
                  case 1:
                    ItemService.gI().sendSetTSKichHoat(player, 131);
                    break;
                  case 2:
                    ItemService.gI().sendSetTSKichHoat(player, 132);
                    break;
                }
                break;
              case ConstNpc.MENU_SKH_THIEN_SU_XD:
                switch (select) {
                  case 0:
                    ItemService.gI().sendSetTSKichHoat(player, 133);
                    break;
                  case 1:
                    ItemService.gI().sendSetTSKichHoat(player, 134);
                    break;
                  case 2:
                    ItemService.gI().sendSetTSKichHoat(player, 135);
                    break;
                }
                break;
              case ConstNpc.CONFIRM_DISSOLUTION_CLAN:
                if (select == 0) {
                  Clan clan = player.clan;
                  clan.deleteDB(clan.id);
                  Manager.CLANS.remove(clan);
                  player.clan = null;
                  player.clanMember = null;
                  Service.gI().sendFlagBag(player);
                  ClanService.gI().sendMyClan(player);
                  ClanService.gI().sendClanId(player);
                  Service.gI().sendThongBao(player, "Đã giải tán bang hội.");
                }
                break;
                //                                    case ConstNpc.CONFIRM_ACTIVE:
                //                                        switch (select) {
                //                                            case 0:
                //                                                if (player.session.goldBar >=
                // 20) {
                //                                                    player.session.actived =
                // true;
                //                                                    if
                // (PlayerDAO.subGoldBar(player, 20)) {
                //
                // Service.gI().sendThongBao(player, "Đã mở
                //                 thành viên thành công!");
                //                                                        break;
                //                                                    } else {
                //                                                        this.npcChat(player, "Lỗi
                // vui lòng báo
                //                 admin...");
                //                                                    }
                //                                                }
                //                                                Service.gI().sendThongBao(player,
                // "Bạn không có
                //                 vàng\n Vui lòng MONZY để nạp thỏi vàng");
                //                                                break;
                //                                        }
                //                                        break;
              case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                if (select == 0) {
                  for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                    player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                  }
                  player.inventory.itemsBoxCrackBall.clear();
                  Service.gI().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                }
                break;
              case ConstNpc.MENU_FIND_PLAYER:
                Player p = (Player) PLAYER_ID_OBJECT.get(player.id);
                if (p != null) {
                  switch (select) {
                    case 0:
                      if (p.zone != null) {
                        ChangeMapService.gI()
                            .changeMapYardrat(player, p.zone, p.location.x, p.location.y);
                      }
                      break;
                    case 1:
                      if (p.zone != null) {
                        ChangeMapService.gI()
                            .changeMap(p, player.zone, player.location.x, player.location.y);
                      }
                      break;
                    case 2:
                      Input.gI().createFormChangeName(player, p);
                      break;
                    case 3:
                      String[] selects = new String[] {"Đồng ý", "Hủy"};
                      NpcService.gI()
                          .createMenuConMeo(
                              player,
                              ConstNpc.BAN_PLAYER,
                              -1,
                              "Bạn có chắc chắn muốn ban " + p.name,
                              selects,
                              p);
                      break;
                    case 4:
                      Service.gI().sendThongBao(player, "Kik người chơi " + p.name + " thành công");
                      Client.gI().getPlayers().remove(p);
                      Client.gI().kickSession(p.session);
                      break;
                  }
                }
                break;
                //                    case ConstNpc.MENU_EVENT:
                //                        switch (select) {
                //                            case 0:
                //                                Service.gI().sendThongBaoOK(player, "Điểm sự kiện:
                // " + player.inventory.event + " ngon ngon...");
                //                                break;
                //                            case 1:
                //                                Service.gI().showListTop(player, Manager.topSK);
                //                                break;
                //                            case 2:
                //                                Service.gI().sendThongBao(player, "Sự kiện đã kết
                // thúc...");
                //                                NpcService.gI().createMenuConMeo(player,
                // ConstNpc.MENU_GIAO_BONG, -1, "Người muốn giao bao nhiêu bông...",
                //                                        "100 bông", "1000 bông", "10000 bông");
                //                                break;
                //                            case 3:
                //                                Service.gI().sendThongBao(player, "Sự kiện đã kết
                // thúc...");
                //                                NpcService.gI().createMenuConMeo(player,
                // ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN, -1, "Con có thực sự muốn đổi thưởng?\nPhải
                // giao cho ta 3000 điểm sự kiện đấy... ",
                //                                        "Đồng ý", "Từ chối");
                //                                break;
                //                        }
                //                        break;
                //                    case ConstNpc.MENU_GIAO_BONG:
                //                        ItemService.gI().giaobong(player, (int)
                // Util.tinhLuyThua(10, select + 2));
                //                        break;
                //                    case ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN:
                //                        if (select == 0) {
                //                            ItemService.gI().openBoxVip(player);
                //                        }
                //                        break;
              case ConstNpc.CONFIRM_TELE_NAMEC:
                if (select == 0) {
                  NgocRongNamecService.gI().teleportToNrNamec(player);
                  player.inventory.subGemAndRuby(50);
                  Service.gI().sendMoney(player);
                }
                break;
            }
          }
        };
  }
}

package com.monzy.services.func;

import com.monzy.card.Card;
import com.monzy.card.RadarCard;
import com.monzy.card.RadarService;
import com.monzy.consts.ConstMap;
import com.monzy.consts.ConstNpc;
import com.monzy.consts.ConstPlayer;
import com.monzy.models.item.Item;
import com.monzy.models.item.Item.ItemOption;
import com.monzy.models.map.Zone;
import com.monzy.models.player.Inventory;
import com.monzy.models.player.Player;
import com.monzy.models.skill.Skill;
import com.monzy.server.ServerNotify;
import com.monzy.server.io.MySession;
import com.monzy.services.*;
import com.monzy.utils.Logger;
import com.monzy.utils.SkillUtil;
import com.monzy.utils.TimeUtil;
import com.monzy.utils.Util;
import com.network.io.Message;

import java.util.Date;

public class UseItem {

  private static final int ITEM_BOX_TO_BODY_OR_BAG = 0;
  private static final int ITEM_BAG_TO_BOX = 1;
  private static final int ITEM_BODY_TO_BOX = 3;
  private static final int ITEM_BAG_TO_BODY = 4;
  private static final int ITEM_BODY_TO_BAG = 5;
  private static final int ITEM_BAG_TO_PET_BODY = 6;
  private static final int ITEM_BODY_PET_TO_BAG = 7;
  private static final int HP_BUFF = 100000;
  private static final int MP_BUFF = 100000;
  private static final int SD_BUFF = 5000;
  private static final byte DO_USE_ITEM = 0;
  private static final byte DO_THROW_ITEM = 1;
  private static final byte ACCEPT_THROW_ITEM = 2;
  private static final byte ACCEPT_USE_ITEM = 3;
  private static UseItem instance;

  private UseItem() {}

  public static UseItem gI() {
    if (instance == null) {
      instance = new UseItem();
    }
    return instance;
  }

  public void getItem(MySession session, Message msg) {
    Player player = session.player;
    TransactionService.gI().cancelTrade(player);
    try {
      int type = msg.reader().readByte();
      int index = msg.reader().readByte();
      if (index == -1) {
        return;
      }
      switch (type) {
        case ITEM_BOX_TO_BODY_OR_BAG:
          InventoryService.gI().itemBoxToBodyOrBag(player, index);
          TaskService.gI().checkDoneTaskGetItemBox(player);
          break;
        case ITEM_BAG_TO_BOX:
          InventoryService.gI().itemBagToBox(player, index);
          break;
        case ITEM_BODY_TO_BOX:
          InventoryService.gI().itemBodyToBox(player, index);
          break;
        case ITEM_BAG_TO_BODY:
          InventoryService.gI().itemBagToBody(player, index);
          break;
        case ITEM_BODY_TO_BAG:
          InventoryService.gI().itemBodyToBag(player, index);
          break;
        case ITEM_BAG_TO_PET_BODY:
          InventoryService.gI().itemBagToPetBody(player, index);
          break;
        case ITEM_BODY_PET_TO_BAG:
          InventoryService.gI().itemPetBodyToBag(player, index);
          break;
      }
      player.setClothes.setup();
      if (player.pet != null) {
        player.pet.setClothes.setup();
      }
      player.setClanMember();
      Service.gI().point(player);
    } catch (Exception e) {
      Logger.logException(UseItem.class, e);
    }
  }

  public void testItem(Player player, Message _msg) {
    TransactionService.gI().cancelTrade(player);
    Message msg;
    try {
      byte type = _msg.reader().readByte();
      int where = _msg.reader().readByte();
      int index = _msg.reader().readByte();
    } catch (Exception e) {
      Logger.logException(UseItem.class, e);
    }
  }

  public void doItem(Player player, Message _msg) {
    TransactionService.gI().cancelTrade(player);
    Message msg;
    byte type;
    try {
      type = _msg.reader().readByte();
      int where = _msg.reader().readByte();
      int index = _msg.reader().readByte();
      //            System.out.println(type + " " + where + " " + index);
      switch (type) {
        case DO_USE_ITEM:
          if (player != null && player.inventory != null) {
            if (index != -1) {
              Item item = player.inventory.itemsBag.get(index);
              if (item.isNotNullItem()) {
                if (item.template.type == 7) {
                  msg = new Message(-43);
                  msg.writer().writeByte(type);
                  msg.writer().writeByte(where);
                  msg.writer().writeByte(index);
                  msg.writer()
                      .writeUTF(
                          "Bạn chắc chắn học "
                              + player.inventory.itemsBag.get(index).template.name
                              + "?");
                  player.sendMessage(msg);
                } else {
                  UseItem.gI().useItem(player, item, index);
                }
              }
            } else {
              this.eatPea(player);
            }
          }
          break;
        case DO_THROW_ITEM:
          if (!(player.zone.map.mapId == 21
              || player.zone.map.mapId == 22
              || player.zone.map.mapId == 23)) {
            Item item = null;
            if (where == 0) {
              item = player.inventory.itemsBody.get(index);
            } else {
              item = player.inventory.itemsBag.get(index);
            }
            msg = new Message(-43);
            msg.writer().writeByte(type);
            msg.writer().writeByte(where);
            msg.writer().writeByte(index);
            msg.writer().writeUTF("Bạn chắc chắn muốn vứt " + item.template.name + "?");
            player.sendMessage(msg);
          } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
          }
          break;
        case ACCEPT_THROW_ITEM:
          InventoryService.gI().throwItem(player, where, index);
          Service.gI().point(player);
          InventoryService.gI().sendItemBags(player);
          break;
        case ACCEPT_USE_ITEM:
          UseItem.gI().useItem(player, player.inventory.itemsBag.get(index), index);
          break;
      }
    } catch (Exception e) {
      Logger.logException(UseItem.class, e);
    }
  }

  private void useItem(Player pl, Item item, int indexBag) {
    if (item.template.strRequire <= pl.nPoint.power) {
      switch (item.template.type) {
        case 21:
          if (pl.newpet != null) {
            ChangeMapService.gI().exitMap(pl.newpet);
            pl.newpet.dispose();
            pl.newpet = null;
          }
          InventoryService.gI().itemBagToBody(pl, indexBag);
          PetService.Pet2(pl, item.template.head, item.template.body, item.template.leg);
          Service.gI().point(pl);
          break;
        case 7: // sách học, nâng skill
          learnSkill(pl, item);
          break;
        case 33:
          UseCard(pl, item);
          break;
        case 6: // đậu thần
          this.eatPea(pl);
          break;
        case 12: // ngọc rồng các loại
          controllerCallRongThan(pl, item);
          break;
        case 23: // thú cưỡi mới
        case 24: // thú cưỡi cũ
          InventoryService.gI().itemBagToBody(pl, indexBag);
          break;
        case 11: // item bag
          InventoryService.gI().itemBagToBody(pl, indexBag);
          Service.gI().sendFlagBag(pl);
          break;
        case 72:
        case 73:
          {
            InventoryService.gI().itemBagToBody(pl, indexBag);
            Service.gI().sendPetFollow(pl, (short) (item.template.iconID - 1));
            break;
          }
        default:
          switch (item.template.id) {
              //                        case 992:
              //                            pl.type = 1;
              //                            pl.maxTime = 5;
              //                            Service.gI().Transport(pl);
              //                            break;
            case 1132:
              SkillService.gI().learnSkillSpecial(pl, Skill.SUPER_KAME);
              InventoryService.gI().subQuantityItemsBag(pl, item, 1);
              InventoryService.gI().sendItemBags(pl);
              break;
            case 1133:
              SkillService.gI().learnSkillSpecial(pl, Skill.MA_PHONG_BA);
              InventoryService.gI().subQuantityItemsBag(pl, item, 1);
              InventoryService.gI().sendItemBags(pl);
              break;
            case 1134:
              InventoryService.gI().subQuantityItemsBag(pl, item, 1);
              InventoryService.gI().sendItemBags(pl);
              SkillService.gI().learnSkillSpecial(pl, Skill.LIEN_HOAN_CHUONG);
              break;
            case 361: {
              if (pl.idNRNM != -1) {
                Service.gI().sendThongBao(pl, "Không thể thực hiện");
                return;
              }
              pl.idGo = (short) Util.nextInt(0, 6);
              NpcService.gI()
                  .createMenuConMeo(
                      pl,
                      ConstNpc.CONFIRM_TELE_NAMEC,
                      -1,
                      "1 Sao ("
                          + NgocRongNamecService.gI().getDis(pl, 0, (short) 353)
                          + " m)\n2 Sao ("
                          + NgocRongNamecService.gI().getDis(pl, 1, (short) 354)
                          + " m)\n3 Sao ("
                          + NgocRongNamecService.gI().getDis(pl, 2, (short) 355)
                          + " m)\n4 Sao ("
                          + NgocRongNamecService.gI().getDis(pl, 3, (short) 356)
                          + " m)\n5 Sao ("
                          + NgocRongNamecService.gI().getDis(pl, 4, (short) 357)
                          + " m)\n6 Sao ("
                          + NgocRongNamecService.gI().getDis(pl, 5, (short) 358)
                          + " m)\n7 Sao ("
                          + NgocRongNamecService.gI().getDis(pl, 6, (short) 359)
                          + " m)",
                      "Đến ngay\nViên " + (pl.idGo + 1) + " Sao\n50 ngọc",
                      "Kết thức");
              InventoryService.gI().subQuantityItemsBag(pl, item, 1);
              InventoryService.gI().sendItemBags(pl);
              break;
            }
            case 211: // nho tím
            case 212: // nho xanh
              eatGrapes(pl, item);
              break;
            case 1105: // hop qua skh, item 2002 xd
              UseItem.gI().ItemTS(pl, item);
              break;
            case 342: // vệ tinh
            case 343:
            case 344:
            case 345:
              if (pl.zone.items.stream()
                      .filter(it -> it != null && it.itemTemplate.type == 22)
                      .count()
                  < 5) {
                Service.gI().DropVeTinh(pl, item, pl.zone, pl.location.x, pl.location.y);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
              } else {
                Service.gI().sendThongBao(pl, "Đặt ít thôi con");
              }
              break;
            case 380: // cskb
              openCSKB(pl, item);
              break;
            case 381: // cuồng nộ
            case 382: // bổ huyết
            case 383: // bổ khí
            case 384: // giáp xên
            case 385: // ẩn danh
            case 379: // máy dò capsule
            case 2037: // máy dò cosmos
            case 663: // bánh pudding
            case 664: // xúc xíc
            case 665: // kem dâu
            case 666: // mì ly
            case 667: // sushi
            case 880:
            case 881:
            case 882:
            case 1099:
            case 1100:
            case 1101:
            case 1102:
            case 1103:
              useItemTime(pl, item);
              break;
            case 521: // tdlt
              useTDLT(pl, item);
              break;
            case 454: // bông tai
              UseItem.gI().usePorata(pl);
              break;
//            case 1152: // tăng 5k sđ
//              UseItem.gI().usesdbuff(pl);
//              break;
//            case 1153:
//              UseItem.gI().usehpbuff(pl);
//              break;
//            case 1154:
//              UseItem.gI().usekibuff(pl);
//              break;
            case 193: // gói 10 viên capsule
              InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            case 194: // capsule đặc biệt
              openCapsuleUI(pl);
              break;
            case 401: // đổi đệ tử
              changePet(pl, item);
              break;
            case 1108: // đổi đệ berus
              changePetBerus(pl, item);
              break;
            case 722: // đổi đệ tử
              changePetPic(pl, item);
              break;
            case 402: // sách nâng chiêu 1 đệ tử
            case 403: // sách nâng chiêu 2 đệ tử
            case 404: // sách nâng chiêu 3 đệ tử
            case 759: // sách nâng chiêu 4 đệ tử
              upSkillPet(pl, item);
              break;
            case 921: // bông tai c2
              UseItem.gI().usePorata2(pl);
              break;
            case 2000: // hop qua skh, item 2000 td
            case 2001: // hop qua skh, item 2001 nm
            case 2002: // hop qua skh, item 2002 xd
              UseItem.gI().ItemSKH(pl, item);
              break;
            case 2003: // hop qua skh, item 2003 td
            case 2004: // hop qua skh, item 2004 nm
            case 2005: // hop qua skh, item 2005 xd
              UseItem.gI().ItemDHD(pl, item);
              break;
            case 736:
              ItemService.gI().OpenItem736(pl, item);
              break;
            case 457:
              useThoiVang(pl);
              break;
            case 987:
              Service.gI().sendThongBao(pl, "Bảo vệ trang bị không bị rớt cấp"); // đá bảo vệ
              break;
            case 2006:
              Input.gI().createFormChangeNameByItem(pl);
              break;
            case 1131:
              openManhTS(pl, item);
            case 2027:
            case 2028:
              {
                if (InventoryService.gI().getCountEmptyBag(pl) == 0) {
                  Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                } else {
                  InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                  Item linhThu = ItemService.gI().createNewItem((short) Util.nextInt(2019, 2026));
                  linhThu.itemOptions.add(new Item.ItemOption(50, 10));
                  linhThu.itemOptions.add(new Item.ItemOption(77, 5));
                  linhThu.itemOptions.add(new Item.ItemOption(103, 5));
                  linhThu.itemOptions.add(new Item.ItemOption(95, 3));
                  linhThu.itemOptions.add(new Item.ItemOption(96, 3));
                  InventoryService.gI().addItemBag(pl, linhThu);
                  InventoryService.gI().sendItemBags(pl);
                  Service.gI()
                      .sendThongBao(
                          pl, "Chúc mừng bạn nhận được Linh thú " + linhThu.template.name);
                }
                break;
              }
            case 718:
              Input.gI().createFormSendRuby(pl);
              break;
            case 570:
              openWoodChest(pl, item);
              break;
            case 1135: {
              if (InventoryService.gI().getCountEmptyBag(pl) == 0) {
                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
              } else {
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                Item skinHit =
                    ItemService.gI().createNewItem((short) 884);
                skinHit.itemOptions.add(new Item.ItemOption(5,
                    Util.isTrue(95, 100) ? Util.nextInt(50, 90) : Util.nextInt(91, 100)));
                skinHit.itemOptions.add(new Item.ItemOption(77, 10));
                skinHit.itemOptions.add(new Item.ItemOption(103, 10));
                InventoryService.gI().addItemBag(pl, skinHit);
                InventoryService.gI().sendItemBags(pl);
                Service.gI()
                    .sendThongBao(
                        pl,
                        "Chúc mừng bạn nhận được Linh thú " + skinHit.template.name);
              }
              break;
            }
            case 2046: {
              Item itemDTL = ItemService.gI().randomDLT(pl.gender);
              itemDTL.itemOptions.add(new ItemOption(30, 0));
              InventoryService.gI().addItemBag(pl, itemDTL);
              InventoryService.gI().subQuantityItemsBag(pl, InventoryService.gI().findItemBag(pl, 2046), 1);
              InventoryService.gI().sendItemBags(pl);
              Service.gI().sendThongBao(pl, "Bạn vừa nhận được " + itemDTL.template.name);
              break;
            }
            case 2047: {
              Item itemDHD = ItemService.gI().randomDHD(pl.gender);
              itemDHD.itemOptions.add(new ItemOption(30, 0));
              InventoryService.gI().addItemBag(pl, itemDHD);
              InventoryService.gI().subQuantityItemsBag(pl, InventoryService.gI().findItemBag(pl, 2047), 1);
              InventoryService.gI().sendItemBags(pl);
              Service.gI().sendThongBao(pl, "Bạn vừa nhận được " + itemDHD.template.name);
              break;
            }
            case 2048:
            case 2049:
            case 2050:
            case 2051: {
              int top = item.template.id - 2047;
              Item gayQuyLao = ItemService.gI().createNewItem((short) 1992);
              gayQuyLao.itemOptions.add(new ItemOption(49, 12));
              gayQuyLao.itemOptions.add(new ItemOption(77, 12));
              gayQuyLao.itemOptions.add(new ItemOption(103, 12));
              gayQuyLao.itemOptions.add(new ItemOption(97, 20));
              gayQuyLao.itemOptions.add(new ItemOption(30, 0));
              if (top > 3) {
                gayQuyLao.itemOptions.add(new ItemOption(93, 90));
              }
              InventoryService.gI().addItemBag(pl, gayQuyLao);
              InventoryService.gI().subQuantityItemsBag(pl, item, 1);
              InventoryService.gI().sendItemBags(pl);
              int hongNgoc = top == 1 ? 500000 : top == 2 ? 300000 : top == 3 ? 200000 : 100000;
              pl.inventory.ruby += hongNgoc;
              Service.gI().sendMoney(pl);
              Service.gI().sendThongBao(pl,
                  "Bạn vừa nhận được " + gayQuyLao.template.name + " và " + hongNgoc + " hồng ngọc");
              break;
            }
            case 2052:
            case 2053:
            case 2054:
            case 2055: {
              int top = item.template.id - 2051;
              Item gayNhuY = ItemService.gI().createNewItem((short) 920);
              gayNhuY.itemOptions.add(new ItemOption(49, 12));
              gayNhuY.itemOptions.add(new ItemOption(77, 12));
              gayNhuY.itemOptions.add(new ItemOption(103, 12));
              gayNhuY.itemOptions.add(new ItemOption(14, 10));
              gayNhuY.itemOptions.add(new ItemOption(89, 0));
              gayNhuY.itemOptions.add(new ItemOption(30, 0));
              if (top > 3) {
                gayNhuY.itemOptions.add(new ItemOption(93, 90));
              }
              int qtyThoiVang = top == 1 ? 2000 : top == 2 ? 1200 : top == 3 ? 800 : 400;
              Item thoiVang = ItemService.gI().createNewItem((short) 457, qtyThoiVang);
              InventoryService.gI().addItemBag(pl, gayNhuY);
              InventoryService.gI().addItemBag(pl, thoiVang);
              InventoryService.gI().subQuantityItemsBag(pl, item, 1);
              InventoryService.gI().sendItemBags(pl);
              Service.gI().sendThongBao(pl,
                  "Bạn vừa nhận được " + gayNhuY.template.name + " và " + qtyThoiVang + " thỏi vàng");
              break;
            }
            case 2056:
            case 2057:
            case 2058:
            case 2059: {
              int top = item.template.id - 2055;
              Item petMinion = ItemService.gI().createNewItem((short) 1416);
              petMinion.itemOptions.add(new ItemOption(49, 12));
              petMinion.itemOptions.add(new ItemOption(77, 12));
              petMinion.itemOptions.add(new ItemOption(103, 12));
              petMinion.itemOptions.add(new ItemOption(116, 0));
              petMinion.itemOptions.add(new ItemOption(30, 0));
              if (top > 3) {
                petMinion.itemOptions.add(new ItemOption(93, 90));
              }
              int qtyRuongThanLinh = top == 1 ? 10 : top == 2 ? 7 : top == 3 ? 5 : 3;
              Item ruongThanLinh = ItemService.gI().createNewItem((short) 2046, qtyRuongThanLinh);
              ruongThanLinh.itemOptions.add(new ItemOption(30, 0));
              InventoryService.gI().addItemBag(pl, petMinion);
              InventoryService.gI().addItemBag(pl, ruongThanLinh);
              InventoryService.gI().subQuantityItemsBag(pl, item, 1);
              InventoryService.gI().sendItemBags(pl);
              Service.gI().sendThongBao(pl,
                  "Bạn vừa nhận được " + petMinion.template.name + " và " + qtyRuongThanLinh + " " + ruongThanLinh.template.name);
              break;
            }
          }
          break;
      }
      InventoryService.gI().sendItemBags(pl);
    } else {
      Service.gI().sendThongBaoOK(pl, "Sức mạnh không đủ yêu cầu");
    }
  }

  public void UseCard(Player pl, Item item) {
    RadarCard radarTemplate =
        RadarService.gI().RADAR_TEMPLATE.stream()
            .filter(c -> c.Id == item.template.id)
            .findFirst()
            .orElse(null);
    if (radarTemplate == null) return;
    if (radarTemplate.Require != -1) {
      RadarCard radarRequireTemplate =
          RadarService.gI().RADAR_TEMPLATE.stream()
              .filter(r -> r.Id == radarTemplate.Require)
              .findFirst()
              .orElse(null);
      if (radarRequireTemplate == null) return;
      Card cardRequire =
          pl.cards.stream().filter(r -> r.id == radarRequireTemplate.Id).findFirst().orElse(null);
      if (cardRequire == null || cardRequire.level < radarTemplate.RequireLevel) {
        Service.gI()
            .sendThongBao(
                pl,
                "Bạn cần sưu tầm "
                    + radarRequireTemplate.Name
                    + " ở cấp độ "
                    + radarTemplate.RequireLevel
                    + " mới có thể sử dụng thẻ này");
        return;
      }
    }
    Card card = pl.cards.stream().filter(r -> r.id == item.template.id).findFirst().orElse(null);
    if (card == null) {
      Card newCard =
          new Card(item.template.id, (byte) 1, radarTemplate.Max, (byte) -1, radarTemplate.Options);
      if (pl.cards.add(newCard)) {
        RadarService.gI().RadarSetAmount(pl, newCard.id, newCard.amount, newCard.maxAmount);
        RadarService.gI().RadarSetLevel(pl, newCard.id, newCard.level);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBags(pl);
      }
    } else {
      if (card.level >= 2) {
        Service.gI().sendThongBao(pl, "Thẻ này đã đạt cấp tối đa");
        return;
      }
      card.amount++;
      if (card.amount >= card.maxAmount) {
        card.amount = 0;
        if (card.level == -1) {
          card.level = 1;
        } else {
          card.level++;
        }
        Service.gI().point(pl);
      }
      RadarService.gI().RadarSetAmount(pl, card.id, card.amount, card.maxAmount);
      RadarService.gI().RadarSetLevel(pl, card.id, card.level);
      InventoryService.gI().subQuantityItemsBag(pl, item, 1);
      InventoryService.gI().sendItemBags(pl);
    }
  }

  private void useItemChangeFlagBag(Player player, Item item) {
    switch (item.template.id) {
      case 994: // vỏ ốc
        break;
      case 995: // cây kem
        break;
      case 996: // cá heo
        break;
      case 997: // con diều
        break;
      case 998: // diều rồng
        break;
      case 999: // mèo mun
        if (!player.effectFlagBag.useMeoMun) {
          player.effectFlagBag.reset();
          player.effectFlagBag.useMeoMun = !player.effectFlagBag.useMeoMun;
        } else {
          player.effectFlagBag.reset();
        }
        break;
      case 1000: // xiên cá
        if (!player.effectFlagBag.useXienCa) {
          player.effectFlagBag.reset();
          player.effectFlagBag.useXienCa = !player.effectFlagBag.useXienCa;
        } else {
          player.effectFlagBag.reset();
        }
        break;
      case 1001: // phóng heo
        if (!player.effectFlagBag.usePhongHeo) {
          player.effectFlagBag.reset();
          player.effectFlagBag.usePhongHeo = !player.effectFlagBag.usePhongHeo;
        } else {
          player.effectFlagBag.reset();
        }
        break;
    }
    Service.gI().point(player);
    Service.gI().sendFlagBag(player);
  }

  private void changePet(Player player, Item item) {
    if (player.pet != null) {
      boolean isWear = false;
      for (Item itemWear : player.pet.inventory.itemsBody) {
        if (itemWear.isNotNullItem()) {
          isWear = true;
        }
      }
      if (isWear) {
        Service.gI().sendThongBao(player, "Tháo hết trang bị của đệ tử");
        return;
      }
      int gender = player.pet.gender + 1;
      if (gender > 2) {
        gender = 0;
      }
      PetService.gI().changeNormalPet(player, gender);
      InventoryService.gI().subQuantityItemsBag(player, item, 1);
    } else {
      Service.gI().sendThongBao(player, "Không thể thực hiện");
    }
  }

  private void changePetBerus(Player player, Item item) {
    if (player.pet != null) {
      int gender = player.pet.gender;
      PetService.gI().changeBerusPet(player, gender);
      InventoryService.gI().subQuantityItemsBag(player, item, 1);
    } else {
      Service.gI().sendThongBao(player, "Không thể thực hiện");
    }
  }

  private void changePetPic(Player player, Item item) {
    if (player.pet != null) {
      int gender = player.pet.gender;
      PetService.gI().changePicPet(player, gender);
      InventoryService.gI().subQuantityItemsBag(player, item, 1);
    } else {
      Service.gI().sendThongBao(player, "Không thể thực hiện");
    }
  }

  private void openPhieuCaiTrangHaiTac(Player pl, Item item) {
    if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
      Item ct = ItemService.gI().createNewItem((short) Util.nextInt(618, 626));
      ct.itemOptions.add(new ItemOption(147, 3));
      ct.itemOptions.add(new ItemOption(77, 3));
      ct.itemOptions.add(new ItemOption(103, 3));
      ct.itemOptions.add(new ItemOption(149, 0));
      if (item.template.id == 2006) {
        ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
      } else if (item.template.id == 2007) {
        ct.itemOptions.add(new ItemOption(93, Util.nextInt(7, 30)));
      }
      InventoryService.gI().addItemBag(pl, ct);
      InventoryService.gI().subQuantityItemsBag(pl, item, 1);
      InventoryService.gI().sendItemBags(pl);
      CombineService.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
    } else {
      Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
    }
  }

  private void eatGrapes(Player pl, Item item) {
    int percentCurrentStatima = pl.nPoint.stamina * 100 / pl.nPoint.maxStamina;
    if (percentCurrentStatima > 50) {
      Service.gI().sendThongBao(pl, "Thể lực vẫn còn trên 50%");
      return;
    } else if (item.template.id == 211) {
      pl.nPoint.stamina = pl.nPoint.maxStamina;
      Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 100%");
    } else if (item.template.id == 212) {
      pl.nPoint.stamina += (pl.nPoint.maxStamina * 20 / 100);
      Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 20%");
    }
    InventoryService.gI().subQuantityItemsBag(pl, item, 1);
    InventoryService.gI().sendItemBags(pl);
    PlayerService.gI().sendCurrentStamina(pl);
  }

  private void openCSKB(Player pl, Item item) {
    if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
      short[] temp = {13, 14, 16, 190, 381, 382, 383, 384, 385};
      int[][] gold = {{1000000, 2000000}};
      byte index = (byte) Util.nextInt(0, temp.length - 1);
      short[] icon = new short[2];
      icon[0] = item.template.iconID;
      if (index <= 3) {
        pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
        if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
          pl.inventory.gold = Inventory.LIMIT_GOLD;
        }
        PlayerService.gI().sendInfoHpMpMoney(pl);
        icon[1] = 930;
      } else {
        Item it = ItemService.gI().createNewItem(temp[index]);
        it.itemOptions.add(new ItemOption(73, 0));
        InventoryService.gI().addItemBag(pl, it);
        icon[1] = it.template.iconID;
      }
      InventoryService.gI().subQuantityItemsBag(pl, item, 1);
      InventoryService.gI().sendItemBags(pl);
      CombineService.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
    } else {
      Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
    }
  }

  private void useItemHopQuaTanThu(Player pl, Item item) {
    if (InventoryService.gI().getCountEmptyBag(pl) > 2) {
      Item tv = ItemService.gI().createNewItem((short) 457);
      tv.quantity = 50;
      Item nr = ItemService.gI().createNewItem((short) 16);
      nr.quantity = 50;
      InventoryService.gI().subQuantityItemsBag(pl, item, 1);
      InventoryService.gI().addItemBag(pl, tv);
      InventoryService.gI().addItemBag(pl, nr);
      InventoryService.gI().sendItemBags(pl);
      Service.gI().sendMoney(pl);
    } else {
      Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
    }
  }

  private void useItemTime(Player pl, Item item) {
    switch (item.template.id) {
      case 382: // bổ huyết
        pl.itemTime.lastTimeBoHuyet = System.currentTimeMillis();
        pl.itemTime.isUseBoHuyet = true;
        break;
      case 383: // bổ khí
        pl.itemTime.lastTimeBoKhi = System.currentTimeMillis();
        pl.itemTime.isUseBoKhi = true;
        break;
      case 384: // giáp xên
        pl.itemTime.lastTimeGiapXen = System.currentTimeMillis();
        pl.itemTime.isUseGiapXen = true;
        break;
      case 381: // cuồng nộ
        pl.itemTime.lastTimeCuongNo = System.currentTimeMillis();
        pl.itemTime.isUseCuongNo = true;
        Service.gI().point(pl);
        break;
      case 385: // ẩn danh
        pl.itemTime.lastTimeAnDanh = System.currentTimeMillis();
        pl.itemTime.isUseAnDanh = true;
        break;
      case 379: // máy dò capsule
        pl.itemTime.lastTimeUseMayDo = System.currentTimeMillis();
        pl.itemTime.isUseMayDo = true;
        break;
      case 1099: // cn
        pl.itemTime.lastTimeCuongNo2 = System.currentTimeMillis();
        pl.itemTime.isUseCuongNo2 = true;
        Service.gI().point(pl);
        break;
      case 1100: // bo huyet
        pl.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis();
        pl.itemTime.isUseBoHuyet2 = true;
        break;
      case 1102: // bo khi
        pl.itemTime.lastTimeBoKhi2 = System.currentTimeMillis();
        pl.itemTime.isUseBoKhi2 = true;
        break;
      case 1101: // xbh
        pl.itemTime.lastTimeGiapXen2 = System.currentTimeMillis();
        pl.itemTime.isUseGiapXen2 = true;
        break;
      case 1103: // an danh
        pl.itemTime.lastTimeAnDanh2 = System.currentTimeMillis();
        pl.itemTime.isUseAnDanh2 = true;
        break;
      case 663: // bánh pudding
      case 664: // xúc xíc
      case 665: // kem dâu
      case 666: // mì ly
      case 667: // sushi
      case 880:
      case 881:
      case 882:
        pl.itemTime.lastTimeEatMeal = System.currentTimeMillis();
        pl.itemTime.isEatMeal = true;
        ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconMeal);
        pl.itemTime.iconMeal = item.template.iconID;
        break;
      case 2037: // máy dò đồ
        pl.itemTime.lastTimeUseMayDo2 = System.currentTimeMillis();
        pl.itemTime.isUseMayDo2 = true;
        break;
    }
    Service.gI().point(pl);
    ItemTimeService.gI().sendAllItemTime(pl);
    InventoryService.gI().subQuantityItemsBag(pl, item, 1);
    InventoryService.gI().sendItemBags(pl);
  }

  private void controllerCallRongThan(Player pl, Item item) {
    int tempId = item.template.id;
    if (tempId >= SummonDragon.NGOC_RONG_1_SAO && tempId <= SummonDragon.NGOC_RONG_7_SAO) {
      switch (tempId) {
        case SummonDragon.NGOC_RONG_1_SAO:
        case SummonDragon.NGOC_RONG_2_SAO:
        case SummonDragon.NGOC_RONG_3_SAO:
          SummonDragon.gI().openMenuSummonShenron(pl, (byte) (tempId - 13));
          break;
        default:
          NpcService.gI()
              .createMenuConMeo(
                  pl,
                  ConstNpc.TUTORIAL_SUMMON_DRAGON,
                  -1,
                  "Bạn chỉ có thể gọi rồng từ ngọc 3 sao, 2 sao, 1 sao",
                  "Hướng\ndẫn thêm\n(mới)",
                  "OK");
          break;
      }
    }
  }

  private void learnSkill(Player pl, Item item) {
    Message msg;
    try {
      if (item.template.gender == pl.gender || item.template.gender == 3) {
        String[] subName = item.template.name.split("");
        byte level = Byte.parseByte(subName[subName.length - 1]);
        Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
        if (curSkill.point == 7) {
          Service.gI().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
        } else {
          if (curSkill.point == 0) {
            if (level == 1) {
              curSkill =
                  SkillUtil.createSkill(
                      SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
              SkillUtil.setSkill(pl, curSkill);
              InventoryService.gI().subQuantityItemsBag(pl, item, 1);
              msg = Service.gI().messageSubCommand((byte) 23);
              msg.writer().writeShort(curSkill.skillId);
              pl.sendMessage(msg);
              msg.cleanup();
            } else {
              Skill skillNeed =
                  SkillUtil.createSkill(
                      SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
              Service.gI()
                  .sendThongBao(
                      pl,
                      "Vui lòng học "
                          + skillNeed.template.name
                          + " cấp "
                          + skillNeed.point
                          + " trước!");
            }
          } else {
            if (curSkill.point + 1 == level) {
              curSkill =
                  SkillUtil.createSkill(
                      SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
              // System.out.println(curSkill.template.name + " - " + curSkill.point);
              SkillUtil.setSkill(pl, curSkill);
              InventoryService.gI().subQuantityItemsBag(pl, item, 1);
              msg = Service.gI().messageSubCommand((byte) 62);
              msg.writer().writeShort(curSkill.skillId);
              pl.sendMessage(msg);
              msg.cleanup();
            } else {
              Service.gI()
                  .sendThongBao(
                      pl,
                      "Vui lòng học "
                          + curSkill.template.name
                          + " cấp "
                          + (curSkill.point + 1)
                          + " trước!");
            }
          }
          InventoryService.gI().sendItemBags(pl);
        }
      } else {
        Service.gI().sendThongBao(pl, "Không thể thực hiện");
      }
    } catch (Exception e) {
      Logger.logException(UseItem.class, e);
    }
  }

  private void useTDLT(Player pl, Item item) {
    if (pl.itemTime.isUseTDLT) {
      ItemTimeService.gI().turnOffTDLT(pl, item);
    } else {
      ItemTimeService.gI().turnOnTDLT(pl, item);
    }
  }

  private void usePorata(Player pl) {
    if (pl.pet == null
        || pl.fusion.typeFusion == 4
        || pl.fusion.typeFusion == 8
        || pl.fusion.typeFusion == 10
        || pl.fusion.typeFusion == 12
        || pl.fusion.typeFusion == 14) {
      Service.gI().sendThongBao(pl, "Không thể thực hiện");
    } else {
      if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
        pl.pet.fusion(true);
      } else {
        pl.pet.unFusion();
      }
    }
  }

  private void usePorata2(Player pl) {
    if (pl.pet == null
        || pl.fusion.typeFusion == 4
        || pl.fusion.typeFusion == 6
        || pl.fusion.typeFusion == 10
        || pl.fusion.typeFusion == 12
        || pl.fusion.typeFusion == 14) {
      Service.gI().sendThongBao(pl, "Không thể thực hiện");
    } else {
      if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
        pl.pet.fusion2(true);
      } else {
        pl.pet.unFusion();
      }
    }
  }

  private void usePorata3(Player pl) {
    if (pl.pet == null
        || pl.fusion.typeFusion == 4
        || pl.fusion.typeFusion == 6
        || pl.fusion.typeFusion == 8
        || pl.fusion.typeFusion == 12
        || pl.fusion.typeFusion == 14) {
      Service.gI().sendThongBao(pl, "Không thể thực hiện");
    } else {
      if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
        pl.pet.fusion3(true);
      } else {
        pl.pet.unFusion();
      }
    }
  }

  private void usePorata4(Player pl) {
    if (pl.pet == null
        || pl.fusion.typeFusion == 4
        || pl.fusion.typeFusion == 6
        || pl.fusion.typeFusion == 8
        || pl.fusion.typeFusion == 10
        || pl.fusion.typeFusion == 14) {
      Service.gI().sendThongBao(pl, "Không thể thực hiện");
    } else {
      if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
        pl.pet.fusion4(true);
      } else {
        pl.pet.unFusion();
      }
    }
  }

  private void usePorata5(Player pl) {
    if (pl.pet == null
        || pl.fusion.typeFusion == 4
        || pl.fusion.typeFusion == 6
        || pl.fusion.typeFusion == 8
        || pl.fusion.typeFusion == 10
        || pl.fusion.typeFusion == 12) {
      Service.gI().sendThongBao(pl, "Không thể thực hiện");
    } else {
      if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
        pl.pet.fusion5(true);
      } else {
        pl.pet.unFusion();
      }
    }
  }

  private void usehpbuff(Player pl) {
    Item hpbuff = null;
    for (Item item : pl.inventory.itemsBag) {
      if (item.isNotNullItem() && item.template.id == 1153) {
        hpbuff = item;
        break;
      }
    }
    if (hpbuff != null) {
      pl.nPoint.hpg += HP_BUFF;
      InventoryService.gI().subQuantityItemsBag(pl, hpbuff, 1);
      InventoryService.gI().sendItemBags(pl);
      Service.gI().sendThongBaoOK(pl, "HP Gốc của bạn đã tăng  " + HP_BUFF);
      Service.gI().point(pl);
    }
  }

  private void usesdbuff(Player pl) {
    Item sdbuff = null;
    for (Item item : pl.inventory.itemsBag) {
      if (item.isNotNullItem() && item.template.id == 1152) {
        sdbuff = item;
        break;
      }
    }
    if (sdbuff != null) {
      pl.nPoint.dameg += SD_BUFF;
      InventoryService.gI().subQuantityItemsBag(pl, sdbuff, 1);
      InventoryService.gI().sendItemBags(pl);
      Service.gI().sendThongBaoOK(pl, "SD Gốc của bạn đã tăng  " + SD_BUFF);
      Service.gI().point(pl);
    }
  }

  private void usekibuff(Player pl) {
    Item kibuff = null;
    for (Item item : pl.inventory.itemsBag) {
      if (item.isNotNullItem() && item.template.id == 1154) {
        kibuff = item;
        break;
      }
    }
    if (kibuff != null) {
      pl.nPoint.mpg += MP_BUFF;
      InventoryService.gI().subQuantityItemsBag(pl, kibuff, 1);
      InventoryService.gI().sendItemBags(pl);
      Service.gI().sendThongBaoOK(pl, "KI Gốc của bạn đã tăng  " + MP_BUFF);
      Service.gI().point(pl);
    }
  }

  public void useThoiVang(Player player) {
    Item tv = null;
    for (Item item : player.inventory.itemsBag) {
      if (item.isNotNullItem() && item.template.id == 457) {
        tv = item;
        break;
      }
    }
    if (tv != null) {
      if (player.inventory.gold <= Inventory.LIMIT_GOLD - 500000000) {
        InventoryService.gI().subQuantityItemsBag(player, tv, 1);
        player.inventory.gold += 500000000;
        PlayerService.gI().sendInfoHpMpMoney(player);
        InventoryService.gI().sendItemBags(player);
      } else {
        Service.gI()
            .sendThongBao(
                player,
                "không được vượt quá " + Util.numberToMoney(Inventory.LIMIT_GOLD) + " vàng");
      }
    }
  }

  public void useruondongvang(Player player) {
    try {
      if (InventoryService.gI().getCountEmptyBag(player) <= 1) {
        Service.gI().sendThongBao(player, "Bạn phải có ít nhất 2 ô trống hành trang");
        return;
      }
      short[] icon = new short[2];
      Item ruongdongvang = null;
      for (Item item : player.inventory.itemsBag) {
        if (item.isNotNullItem() && item.template.id == 1230) {
          ruongdongvang = item;
          break;
        }
      }
      if (ruongdongvang != null) {
        int rd = Util.nextInt(0, 100);
        int rac = 70;
        int ruby = 5;
        int tv = 15;
        int ct = 10;
        Item item = randomRac();
        if (rd <= rac) {
          item = randomRac();
        } else if (rd <= rac + ruby) {
          item = hongngocrdv();
        } else if (rd <= rac + ruby + tv) {
          item = thoivangrdv();
        } else if (rd <= rac + ruby + tv + ct) {
          item = caitrangrdv(true);
        }
        icon[0] = ruongdongvang.template.iconID;
        icon[1] = item.template.iconID;
        InventoryService.gI().subQuantityItemsBag(player, ruongdongvang, 1);
        InventoryService.gI().addItemBag(player, item);
        InventoryService.gI().sendItemBags(player);
        Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
        CombineService.gI().sendEffectOpenItem(player, icon[0], icon[1]);
      }
    } catch (Exception e) {
      Logger.logException(UseItem.class, e);
    }
  }

  public void doiDiemSukien(Player player) {
    if (InventoryService.gI().getCountEmptyBag(player) <= 1) {
      Service.gI().sendThongBao(player, "Bạn phải có ít nhất 2 ô trống hành trang");
      return;
    }
    if (player.event < 5) {
      Service.gI().sendThongBao(player, "Hết điểm rồi phên");
      return;
    }
    player.event -= 5;
    if (Util.isTrue(10, 100)) {
      sendItemEvent((short) 1281, player);
    } else if (Util.isTrue(10, 100)) {
      sendItemEvent((short) 995, player);
    } else {
      int hongNgoc = Util.nextInt(500, 5000);
      player.inventory.ruby += hongNgoc;
      PlayerService.gI().sendInfoHpMpMoney(player);
      Service.gI().sendThongBao(player, "Bạn đã nhận được " + hongNgoc + " hồng ngọc");
    }
  }

  public Item sendItemEvent(short idItem, Player player) {
    Item item = ItemService.gI().createNewItem(idItem);
    item.itemOptions.add(new Item.ItemOption(147, 15)); // sd 50%
    item.itemOptions.add(new Item.ItemOption(77, 15)); // hp 50%
    item.itemOptions.add(new Item.ItemOption(103, 15)); // ki 50%
    if (Util.isTrue(99, 100)) { // tỉ lệ ra hsd
      item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(10) + 3)); // hsd
    } else {
      ServerNotify.gI()
          .sendThongBaoBenDuoi(
              "Chúc mừng người chơi "
                  + player.name
                  + " nhận được "
                  + item.template.name
                  + " vĩnh viễn");
    }
    InventoryService.gI().addItemBag(player, item);
    InventoryService.gI().sendItemBags(player);
    Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
    return item;
  }

  public Item randomRac() {
    short[] racs = {20, 19, 18, 17, 16};
    Item item = ItemService.gI().createNewItem(racs[Util.nextInt(racs.length - 1)], 1);
    return item;
  }

  public Item caitrangrdv(boolean rating) {
    Item item = ItemService.gI().createNewItem((short) 2043);
    item.itemOptions.add(new Item.ItemOption(147, 20)); // sd 50%
    item.itemOptions.add(new Item.ItemOption(77, 20)); // hp 50%
    item.itemOptions.add(new Item.ItemOption(103, 20)); // ki 50%
    if (Util.isTrue(50, 100)) {
      item.itemOptions.add(new Item.ItemOption(101, 100)); // smtn + 500%
    } else {
      item.itemOptions.add(new Item.ItemOption(106, 0)); // k ảnh hưởng bới cái lạnh
    }
    if (Util.isTrue(995, 1000) && rating) { // tỉ lệ ra hsd
      item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(30) + 3)); // hsd
    }
    return item;
  }

  public Item hongngocrdv() {
    Item item = ItemService.gI().createNewItem((short) 861);
    item.quantity = Util.nextInt(100, 150);
    return item;
  }

  public Item thoivangrdv() {
    Item item = ItemService.gI().createNewItem((short) 457);
    item.quantity = Util.nextInt(5, 10);
    return item;
  }

  private void openCapsuleUI(Player pl) {
    pl.iDMark.setTypeChangeMap(ConstMap.CHANGE_CAPSULE);
    ChangeMapService.gI().openChangeMapTab(pl);
  }

  public void choseMapCapsule(Player pl, int index) {
    int zoneId = -1;
    Zone zoneChose = pl.mapCapsule.get(index);
    // Kiểm tra số lượng người trong khu
    if (zoneChose.getNumOfPlayers() > 25
        || MapService.gI().isMapDoanhTrai(zoneChose.map.mapId)
        || MapService.gI().isMapMaBu(zoneChose.map.mapId)
        || MapService.gI().isMapHuyDiet(zoneChose.map.mapId)
        || MapService.gI().isMapBanDoKhoBau(zoneChose.map.mapId)
        || MapService.gI().isNguHS(zoneChose.map.mapId)) {
      Service.gI().sendThongBao(pl, "Hiện tại không thể vào được khu!");
      return;
    }
    if (index != 0
        || zoneChose.map.mapId == 21
        || zoneChose.map.mapId == 22
        || zoneChose.map.mapId == 23) {
      pl.mapBeforeCapsule = pl.zone;
    } else {
      zoneId = pl.mapBeforeCapsule != null ? pl.mapBeforeCapsule.zoneId : -1;
      pl.mapBeforeCapsule = null;
    }
    ChangeMapService.gI().changeMapBySpaceShip(pl, pl.mapCapsule.get(index).map.mapId, zoneId, -1);
  }

  public void eatPea(Player player) {

    Item pea = null;
    for (Item item : player.inventory.itemsBag) {
      if (item.isNotNullItem() && item.template.type == 6) {
        pea = item;
        break;
      }
    }
    if (pea != null) {
      int hpKiHoiPhuc = 0;
      int lvPea = Integer.parseInt(pea.template.name.split("cấp ")[1]);
      for (Item.ItemOption io : pea.itemOptions) {
        if (io.optionTemplate.id == 2) {
          hpKiHoiPhuc = io.param * 1000;
          break;
        }
        if (io.optionTemplate.id == 48) {
          hpKiHoiPhuc = io.param;
          break;
        }
      }
      player.nPoint.setHp(player.nPoint.hp + hpKiHoiPhuc);
      player.nPoint.setMp(player.nPoint.mp + hpKiHoiPhuc);
      PlayerService.gI().sendInfoHpMp(player);
      Service.gI().sendInfoPlayerEatPea(player);
      if (player.pet != null && player.zone.equals(player.pet.zone) && !player.pet.isDie()) {
        player.pet.nPoint.stamina += (short) (100 * lvPea);
        if (player.pet.nPoint.stamina > player.pet.nPoint.maxStamina) {
          player.pet.nPoint.stamina = player.pet.nPoint.maxStamina;
        }
        player.pet.nPoint.setHp(player.pet.nPoint.hp + hpKiHoiPhuc);
        player.pet.nPoint.setMp(player.pet.nPoint.mp + hpKiHoiPhuc);
        Service.gI().sendInfoPlayerEatPea(player.pet);
        Service.gI().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã cho con đậu thần");
      }
      InventoryService.gI().subQuantityItemsBag(player, pea, 1);
      InventoryService.gI().sendItemBags(player);
    }
  }

  private void hopquat1nap(Player pl) {
    try {
      if (InventoryService.gI().getCountEmptyBag(pl) <= 1) {
        Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 2 ô trống hành trang");
        return;
      }
      Item hopquat1nap = null;
      for (Item item : pl.inventory.itemsBag) {
        if (item.isNotNullItem() && item.template.id == 1259) {
          hopquat1nap = item;
          break;
        }
      }
      if (hopquat1nap != null) {
        Item gang = ItemService.gI().createNewItem((short) 1255);
        Item DL = ItemService.gI().createNewItem((short) 1249);
        Item tv = ItemService.gI().createNewItem((short) 457);
        Item hn = ItemService.gI().createNewItem((short) 861);
        Item LT = ItemService.gI().createNewItem((short) 1246);
        tv.quantity = 150;
        hn.quantity = 20000;
        gang.itemOptions.add(new ItemOption(190, 0));
        gang.itemOptions.add(new ItemOption(21, 20));
        gang.itemOptions.add(new ItemOption(147, Util.nextInt(55, 65)));
        gang.itemOptions.add(new Item.ItemOption(77, Util.nextInt(70, 85)));
        gang.itemOptions.add(new Item.ItemOption(103, Util.nextInt(70, 85)));
        gang.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 150)));
        gang.itemOptions.add(new Item.ItemOption(211, 0));
        gang.itemOptions.add(new Item.ItemOption(30, 0));
        DL.itemOptions.add(new ItemOption(190, 0));
        DL.itemOptions.add(new ItemOption(21, 20));
        DL.itemOptions.add(new ItemOption(147, Util.nextInt(55, 65)));
        DL.itemOptions.add(new Item.ItemOption(77, Util.nextInt(70, 85)));
        DL.itemOptions.add(new Item.ItemOption(103, Util.nextInt(70, 85)));
        DL.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 150)));
        DL.itemOptions.add(new Item.ItemOption(211, 0));
        DL.itemOptions.add(new Item.ItemOption(30, 0));
        LT.itemOptions.add(new ItemOption(190, 0));
        LT.itemOptions.add(new ItemOption(21, 20));
        LT.itemOptions.add(new ItemOption(147, Util.nextInt(55, 65)));
        LT.itemOptions.add(new Item.ItemOption(77, Util.nextInt(70, 85)));
        LT.itemOptions.add(new Item.ItemOption(103, Util.nextInt(70, 85)));
        LT.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 150)));
        LT.itemOptions.add(new Item.ItemOption(211, 0));
        LT.itemOptions.add(new Item.ItemOption(30, 0));
        InventoryService.gI().subQuantityItemsBag(pl, hopquat1nap, 1);
        InventoryService.gI().addItemBag(pl, gang);
        InventoryService.gI().addItemBag(pl, tv);
        InventoryService.gI().addItemBag(pl, hn);
        InventoryService.gI().addItemBag(pl, DL);
        InventoryService.gI().addItemBag(pl, LT);
        InventoryService.gI().sendItemBags(pl);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + gang.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + tv.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + hn.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + DL.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + LT.template.name);
      }
    } catch (Exception e) {
      Logger.logException(UseItem.class, e);
    }
  }

  private void hopquat2nap(Player pl) {
    try {
      if (InventoryService.gI().getCountEmptyBag(pl) <= 1) {
        Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 2 ô trống hành trang");
        return;
      }
      Item hopquat2nap = null;
      for (Item item : pl.inventory.itemsBag) {
        if (item.isNotNullItem() && item.template.id == 1264) {
          hopquat2nap = item;
          break;
        }
      }
      if (hopquat2nap != null) {
        Item gang = ItemService.gI().createNewItem((short) 1256);
        Item DL = ItemService.gI().createNewItem((short) 1245);
        Item tv = ItemService.gI().createNewItem((short) 457);
        Item hn = ItemService.gI().createNewItem((short) 861);
        Item LT = ItemService.gI().createNewItem((short) 1250);
        tv.quantity = 100;
        hn.quantity = 10000;
        gang.itemOptions.add(new ItemOption(190, 0));
        gang.itemOptions.add(new ItemOption(21, 20));
        gang.itemOptions.add(new ItemOption(147, Util.nextInt(40, 55)));
        gang.itemOptions.add(new Item.ItemOption(77, Util.nextInt(50, 70)));
        gang.itemOptions.add(new Item.ItemOption(103, Util.nextInt(65, 70)));
        gang.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 90)));
        gang.itemOptions.add(new Item.ItemOption(211, 0));
        gang.itemOptions.add(new Item.ItemOption(30, 0));
        DL.itemOptions.add(new ItemOption(190, 0));
        DL.itemOptions.add(new ItemOption(21, 20));
        DL.itemOptions.add(new ItemOption(147, Util.nextInt(40, 55)));
        DL.itemOptions.add(new Item.ItemOption(77, Util.nextInt(60, 70)));
        DL.itemOptions.add(new Item.ItemOption(103, Util.nextInt(65, 70)));
        DL.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 90)));
        DL.itemOptions.add(new Item.ItemOption(211, 0));
        DL.itemOptions.add(new Item.ItemOption(30, 0));
        LT.itemOptions.add(new ItemOption(190, 0));
        LT.itemOptions.add(new ItemOption(21, 20));
        LT.itemOptions.add(new ItemOption(147, Util.nextInt(40, 55)));
        LT.itemOptions.add(new Item.ItemOption(77, Util.nextInt(60, 70)));
        LT.itemOptions.add(new Item.ItemOption(103, Util.nextInt(65, 70)));
        LT.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 90)));
        LT.itemOptions.add(new Item.ItemOption(211, 0));
        LT.itemOptions.add(new Item.ItemOption(30, 0));
        InventoryService.gI().subQuantityItemsBag(pl, hopquat2nap, 1);
        InventoryService.gI().addItemBag(pl, gang);
        InventoryService.gI().addItemBag(pl, tv);
        InventoryService.gI().addItemBag(pl, hn);
        InventoryService.gI().addItemBag(pl, DL);
        InventoryService.gI().addItemBag(pl, LT);
        InventoryService.gI().sendItemBags(pl);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + gang.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + tv.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + hn.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + DL.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + LT.template.name);
      }
    } catch (Exception e) {
      Logger.logException(UseItem.class, e);
    }
  }

  private void hopquat3nap(Player pl) {
    try {
      if (InventoryService.gI().getCountEmptyBag(pl) <= 1) {
        Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 2 ô trống hành trang");
        return;
      }
      Item hopquat3nap = null;
      for (Item item : pl.inventory.itemsBag) {
        if (item.isNotNullItem() && item.template.id == 1265) {
          hopquat3nap = item;
          break;
        }
      }
      if (hopquat3nap != null) {
        Item gang = ItemService.gI().createNewItem((short) 1257);
        Item DL = ItemService.gI().createNewItem((short) 1244);
        Item tv = ItemService.gI().createNewItem((short) 457);
        Item hn = ItemService.gI().createNewItem((short) 861);
        Item LT = ItemService.gI().createNewItem((short) 1251);
        tv.quantity = 70;
        hn.quantity = 5000;
        gang.itemOptions.add(new ItemOption(190, 0));
        gang.itemOptions.add(new ItemOption(21, 20));
        gang.itemOptions.add(new ItemOption(147, Util.nextInt(30, 40)));
        gang.itemOptions.add(new Item.ItemOption(77, Util.nextInt(50, 65)));
        gang.itemOptions.add(new Item.ItemOption(103, Util.nextInt(50, 65)));
        gang.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 100)));
        gang.itemOptions.add(new Item.ItemOption(211, 0));
        gang.itemOptions.add(new Item.ItemOption(30, 0));
        DL.itemOptions.add(new ItemOption(190, 0));
        DL.itemOptions.add(new ItemOption(21, 20));
        DL.itemOptions.add(new ItemOption(147, Util.nextInt(30, 40)));
        DL.itemOptions.add(new Item.ItemOption(77, Util.nextInt(50, 65)));
        DL.itemOptions.add(new Item.ItemOption(103, Util.nextInt(50, 65)));
        DL.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 100)));
        DL.itemOptions.add(new Item.ItemOption(211, 0));
        DL.itemOptions.add(new Item.ItemOption(30, 0));
        LT.itemOptions.add(new ItemOption(190, 0));
        LT.itemOptions.add(new ItemOption(21, 20));
        LT.itemOptions.add(new ItemOption(147, Util.nextInt(30, 40)));
        LT.itemOptions.add(new Item.ItemOption(77, Util.nextInt(50, 65)));
        LT.itemOptions.add(new Item.ItemOption(103, Util.nextInt(50, 65)));
        LT.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 100)));
        LT.itemOptions.add(new Item.ItemOption(211, 0));
        LT.itemOptions.add(new Item.ItemOption(30, 0));
        InventoryService.gI().subQuantityItemsBag(pl, hopquat3nap, 1);
        InventoryService.gI().addItemBag(pl, gang);
        InventoryService.gI().addItemBag(pl, tv);
        InventoryService.gI().addItemBag(pl, hn);
        InventoryService.gI().addItemBag(pl, DL);
        InventoryService.gI().addItemBag(pl, LT);
        InventoryService.gI().sendItemBags(pl);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + gang.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + tv.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + hn.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + DL.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + LT.template.name);
      }
    } catch (Exception e) {
      Logger.logException(UseItem.class, e);
    }
  }

  private void hopquat1SM(Player pl) {
    try {
      if (InventoryService.gI().getCountEmptyBag(pl) <= 1) {
        Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 2 ô trống hành trang");
        return;
      }
      Item hopquat1SM = null;
      for (Item item : pl.inventory.itemsBag) {
        if (item.isNotNullItem() && item.template.id == 1266) {
          hopquat1SM = item;
          break;
        }
      }
      if (hopquat1SM != null) {
        Item gang = ItemService.gI().createNewItem((short) 1210);
        Item DL = ItemService.gI().createNewItem((short) 1249);
        Item tv = ItemService.gI().createNewItem((short) 457);
        Item hn = ItemService.gI().createNewItem((short) 861);
        Item LT = ItemService.gI().createNewItem((short) 1246);
        tv.quantity = 150;
        hn.quantity = 20000;
        gang.itemOptions.add(new ItemOption(190, 0));
        gang.itemOptions.add(new ItemOption(21, 20));
        gang.itemOptions.add(new ItemOption(147, Util.nextInt(55, 65)));
        gang.itemOptions.add(new Item.ItemOption(77, Util.nextInt(70, 85)));
        gang.itemOptions.add(new Item.ItemOption(103, Util.nextInt(70, 85)));
        gang.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 150)));
        gang.itemOptions.add(new Item.ItemOption(211, 0));
        gang.itemOptions.add(new Item.ItemOption(30, 0));
        DL.itemOptions.add(new ItemOption(190, 0));
        DL.itemOptions.add(new ItemOption(21, 20));
        DL.itemOptions.add(new ItemOption(147, Util.nextInt(55, 65)));
        DL.itemOptions.add(new Item.ItemOption(77, Util.nextInt(70, 85)));
        DL.itemOptions.add(new Item.ItemOption(103, Util.nextInt(70, 85)));
        DL.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 150)));
        DL.itemOptions.add(new Item.ItemOption(211, 0));
        DL.itemOptions.add(new Item.ItemOption(30, 0));
        LT.itemOptions.add(new ItemOption(190, 0));
        LT.itemOptions.add(new ItemOption(21, 20));
        LT.itemOptions.add(new ItemOption(147, Util.nextInt(55, 65)));
        LT.itemOptions.add(new Item.ItemOption(77, Util.nextInt(70, 85)));
        LT.itemOptions.add(new Item.ItemOption(103, Util.nextInt(70, 85)));
        LT.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 150)));
        LT.itemOptions.add(new Item.ItemOption(211, 0));
        LT.itemOptions.add(new Item.ItemOption(30, 0));
        InventoryService.gI().subQuantityItemsBag(pl, hopquat1SM, 1);
        InventoryService.gI().addItemBag(pl, gang);
        InventoryService.gI().addItemBag(pl, tv);
        InventoryService.gI().addItemBag(pl, hn);
        InventoryService.gI().addItemBag(pl, DL);
        InventoryService.gI().addItemBag(pl, LT);
        InventoryService.gI().sendItemBags(pl);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + gang.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + tv.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + hn.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + DL.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + LT.template.name);
      }
    } catch (Exception e) {
      Logger.logException(UseItem.class, e);
    }
  }

  private void hopquat2SM(Player pl) {
    try {
      if (InventoryService.gI().getCountEmptyBag(pl) <= 1) {
        Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 2 ô trống hành trang");
        return;
      }
      Item hopquat2SM = null;
      for (Item item : pl.inventory.itemsBag) {
        if (item.isNotNullItem() && item.template.id == 1267) {
          hopquat2SM = item;
          break;
        }
      }
      if (hopquat2SM != null) {
        Item gang = ItemService.gI().createNewItem((short) 1209);
        Item DL = ItemService.gI().createNewItem((short) 1245);
        Item tv = ItemService.gI().createNewItem((short) 457);
        Item hn = ItemService.gI().createNewItem((short) 861);
        Item LT = ItemService.gI().createNewItem((short) 1250);
        tv.quantity = 100;
        hn.quantity = 10000;
        gang.itemOptions.add(new ItemOption(190, 0));
        gang.itemOptions.add(new ItemOption(21, 20));
        gang.itemOptions.add(new ItemOption(147, Util.nextInt(40, 55)));
        gang.itemOptions.add(new Item.ItemOption(77, Util.nextInt(50, 70)));
        gang.itemOptions.add(new Item.ItemOption(103, Util.nextInt(65, 70)));
        gang.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 90)));
        gang.itemOptions.add(new Item.ItemOption(211, 0));
        gang.itemOptions.add(new Item.ItemOption(30, 0));
        DL.itemOptions.add(new ItemOption(190, 0));
        DL.itemOptions.add(new ItemOption(21, 20));
        DL.itemOptions.add(new ItemOption(147, Util.nextInt(40, 55)));
        DL.itemOptions.add(new Item.ItemOption(77, Util.nextInt(60, 70)));
        DL.itemOptions.add(new Item.ItemOption(103, Util.nextInt(65, 70)));
        DL.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 90)));
        DL.itemOptions.add(new Item.ItemOption(211, 0));
        DL.itemOptions.add(new Item.ItemOption(30, 0));
        LT.itemOptions.add(new ItemOption(190, 0));
        LT.itemOptions.add(new ItemOption(21, 20));
        LT.itemOptions.add(new ItemOption(147, Util.nextInt(40, 55)));
        LT.itemOptions.add(new Item.ItemOption(77, Util.nextInt(60, 70)));
        LT.itemOptions.add(new Item.ItemOption(103, Util.nextInt(65, 70)));
        LT.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 90)));
        LT.itemOptions.add(new Item.ItemOption(211, 0));
        LT.itemOptions.add(new Item.ItemOption(30, 0));
        InventoryService.gI().subQuantityItemsBag(pl, hopquat2SM, 1);
        InventoryService.gI().addItemBag(pl, gang);
        InventoryService.gI().addItemBag(pl, tv);
        InventoryService.gI().addItemBag(pl, hn);
        InventoryService.gI().addItemBag(pl, DL);
        InventoryService.gI().addItemBag(pl, LT);
        InventoryService.gI().sendItemBags(pl);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + gang.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + tv.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + hn.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + DL.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + LT.template.name);
      }
    } catch (Exception e) {
      Logger.logException(UseItem.class, e);
    }
  }

  private void hopquat3SM(Player pl) {
    try {
      if (InventoryService.gI().getCountEmptyBag(pl) <= 1) {
        Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 2 ô trống hành trang");
        return;
      }
      Item hopquat3SM = null;
      for (Item item : pl.inventory.itemsBag) {
        if (item.isNotNullItem() && item.template.id == 1268) {
          hopquat3SM = item;
          break;
        }
      }
      if (hopquat3SM != null) {
        Item gang = ItemService.gI().createNewItem((short) 1208);
        Item DL = ItemService.gI().createNewItem((short) 1244);
        Item tv = ItemService.gI().createNewItem((short) 457);
        Item hn = ItemService.gI().createNewItem((short) 861);
        Item LT = ItemService.gI().createNewItem((short) 1251);
        tv.quantity = 70;
        hn.quantity = 5000;
        gang.itemOptions.add(new ItemOption(190, 0));
        gang.itemOptions.add(new ItemOption(21, 20));
        gang.itemOptions.add(new ItemOption(147, Util.nextInt(30, 40)));
        gang.itemOptions.add(new Item.ItemOption(77, Util.nextInt(50, 65)));
        gang.itemOptions.add(new Item.ItemOption(103, Util.nextInt(50, 65)));
        gang.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 100)));
        gang.itemOptions.add(new Item.ItemOption(211, 0));
        gang.itemOptions.add(new Item.ItemOption(30, 0));
        DL.itemOptions.add(new ItemOption(190, 0));
        DL.itemOptions.add(new ItemOption(21, 20));
        DL.itemOptions.add(new ItemOption(147, Util.nextInt(30, 40)));
        DL.itemOptions.add(new Item.ItemOption(77, Util.nextInt(50, 65)));
        DL.itemOptions.add(new Item.ItemOption(103, Util.nextInt(50, 65)));
        DL.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 100)));
        DL.itemOptions.add(new Item.ItemOption(211, 0));
        DL.itemOptions.add(new Item.ItemOption(30, 0));
        LT.itemOptions.add(new ItemOption(190, 0));
        LT.itemOptions.add(new ItemOption(21, 20));
        LT.itemOptions.add(new ItemOption(147, Util.nextInt(30, 40)));
        LT.itemOptions.add(new Item.ItemOption(77, Util.nextInt(50, 65)));
        LT.itemOptions.add(new Item.ItemOption(103, Util.nextInt(50, 65)));
        LT.itemOptions.add(new Item.ItemOption(101, Util.nextInt(80, 100)));
        LT.itemOptions.add(new Item.ItemOption(211, 0));
        LT.itemOptions.add(new Item.ItemOption(30, 0));
        InventoryService.gI().subQuantityItemsBag(pl, hopquat3SM, 1);
        InventoryService.gI().addItemBag(pl, gang);
        InventoryService.gI().addItemBag(pl, tv);
        InventoryService.gI().addItemBag(pl, hn);
        InventoryService.gI().addItemBag(pl, DL);
        InventoryService.gI().addItemBag(pl, LT);
        InventoryService.gI().sendItemBags(pl);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + gang.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + tv.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + hn.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + DL.template.name);
        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + LT.template.name);
      }
    } catch (Exception e) {
      Logger.logException(UseItem.class, e);
    }
  }

  private void upSkillPet(Player pl, Item item) {
    if (pl.pet == null) {
      Service.gI().sendThongBao(pl, "Không thể thực hiện");
      return;
    }
    try {
      switch (item.template.id) {
        case 402: // skill 1
          if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 0)) {
            Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
          } else {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
          }
          break;
        case 403: // skill 2
          if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 1)) {
            Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
          } else {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
          }
          break;
        case 404: // skill 3
          if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 2)) {
            Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
          } else {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
          }
          break;
        case 759: // skill 4
          if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 3)) {
            Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
          } else {
            Service.gI().sendThongBao(pl, "Không thể thực hiện");
          }
          break;
      }
    } catch (Exception e) {
      Service.gI().sendThongBao(pl, "Không thể thực hiện");
    }
  }

  private void ItemSKH(Player pl, Item item) { // hop qua skh
    NpcService.gI()
        .createMenuConMeo(
            pl,
            item.template.id,
            -1,
            "Hãy chọn một món quà",
            "Áo",
            "Quần",
            "Găng",
            "Giày",
            "Rada",
            "Từ Chối");
  }

  private void ItemDHD(Player pl, Item item) { // hop qua do huy diet
    NpcService.gI()
        .createMenuConMeo(
            pl,
            item.template.id,
            -1,
            "Hãy chọn một món quà",
            "Áo",
            "Quần",
            "Găng",
            "Giày",
            "Rada",
            "Từ Chối");
  }

  private void ItemTS(Player pl, Item item) { // hop qua do huy diet
    NpcService.gI()
        .createMenuConMeo(
            pl,
            item.template.id,
            -1,
            "Chọn hành tinh của mày đi",
            "Set trái đất",
            "Set namec",
            "Set xayda",
            "Từ chổi");
  }

  private void openManhTS(Player player, Item item) {
    if (InventoryService.gI().getCountEmptyBag(player) > 0) {
      short[] possibleItems = {1066, 1067, 1068, 1069, 1070};
      byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
      short[] icon = new short[2];
      icon[0] = item.template.iconID;
      Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
      newItem.itemOptions.add(new ItemOption(73, 0));
      newItem.quantity = (short) Util.nextInt(1, 99);
      InventoryService.gI().addItemBag(player, newItem);
      icon[1] = newItem.template.iconID;
      InventoryService.gI().subQuantityItemsBag(player, item, 1);
      InventoryService.gI().sendItemBags(player);
      CombineService.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    } else {
      Service.gI().sendThongBao(player, "Hàng trang đã đầy");
    }
  }

  private void openWoodChest(Player pl, Item item) {
    int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
    if (time != 0) {
      Item itemReward;
      int param = item.itemOptions.size();
      int gold;
      int[] listItem = {441, 442, 443, 444, 445, 446, 447, 220, 221, 222, 223, 224, 225};
      int[] listClothesReward;
      int[] listItemReward;
      String text = "Bạn nhận được\n";
      if (param < 8) {
        gold = 100000 * param;
        listClothesReward = new int[]{randClothes(param)};
        listItemReward = Util.pickNRandInArr(listItem, 3);
      } else if (param < 10) {
        gold = 250000 * param;
        listClothesReward = new int[]{randClothes(param), randClothes(param)};
        listItemReward = Util.pickNRandInArr(listItem, 4);
      } else {
        gold = 500000 * param;
        listClothesReward = new int[]{randClothes(param), randClothes(param), randClothes(param)};
        listItemReward = Util.pickNRandInArr(listItem, 5);
        int ruby = Util.nextInt(1, 5);
        pl.inventory.ruby += ruby;
        pl.textRuongGo.add(text + "|1| " + ruby + " Hồng Ngọc");
      }
      for (int i : listClothesReward) {
        itemReward = ItemService.gI().createNewItem((short) i);
        RewardService.gI().initBaseOptionClothes(itemReward.template.id, itemReward.template.type, itemReward.itemOptions);
        RewardService.gI().initStarOption(itemReward, new RewardService.RatioStar[]{new RewardService.RatioStar((byte) 1, 1, 2), new RewardService.RatioStar((byte) 2, 1, 3), new RewardService.RatioStar((byte) 3, 1, 4), new RewardService.RatioStar((byte) 4, 1, 5),});
        InventoryService.gI().addItemBag(pl, itemReward);
        pl.textRuongGo.add(text + itemReward.getInfoItem());
      }
      for (int i : listItemReward) {
        itemReward = ItemService.gI().createNewItem((short) i);
        RewardService.gI().initBaseOptionSaoPhaLe(itemReward);
        itemReward.quantity = Util.nextInt(1, 5);
        InventoryService.gI().addItemBag(pl, itemReward);
        pl.textRuongGo.add(text + itemReward.getInfoItem());
      }
      if (param == 11) {
        itemReward = ItemService.gI().createNewItem((short) 0);
        itemReward.quantity = Util.nextInt(1, 3);
        InventoryService.gI().addItemBag(pl, itemReward);
        pl.textRuongGo.add(text + itemReward.getInfoItem());
      }
      NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1, "Bạn nhận được\n|1|+" + Util.numberToMoney(gold) + " vàng", "OK [" + pl.textRuongGo.size() + "]");
      InventoryService.gI().subQuantityItemsBag(pl, item, 1);
      pl.inventory.addGold(gold);
      InventoryService.gI().sendItemBags(pl);
      PlayerService.gI().sendInfoHpMpMoney(pl);
    } else {
      Service.gI().sendThongBao(pl, "Vui lòng đợi 24h");
    }
  }

  private int randClothes(int level) {
    return LIST_ITEM_CLOTHES[Util.nextInt(0, 2)][Util.nextInt(0, 4)][level - 1];
  }

  public static final int[][][] LIST_ITEM_CLOTHES = {
      // áo , quần , găng ,giày,rada
      //td -> nm -> xd
      {{0, 33, 3, 34, 136, 137, 138, 139, 230, 231, 232, 233, 555}, {6, 35, 9, 36, 140, 141, 142, 143, 242, 243, 244, 245, 556}, {21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257, 562}, {27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269, 563}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}}, {{1, 41, 4, 42, 152, 153, 154, 155, 234, 235, 236, 237, 557}, {7, 43, 10, 44, 156, 157, 158, 159, 246, 247, 248, 249, 558}, {22, 46, 25, 45, 160, 161, 162, 163, 258, 259, 260, 261, 564}, {28, 47, 31, 48, 164, 165, 166, 167, 270, 271, 272, 273, 565}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}}, {{2, 49, 5, 50, 168, 169, 170, 171, 238, 239, 240, 241, 559}, {8, 51, 11, 52, 172, 173, 174, 175, 250, 251, 252, 253, 560}, {23, 53, 26, 54, 176, 177, 178, 179, 262, 263, 264, 265, 566}, {29, 55, 32, 56, 180, 181, 182, 183, 274, 275, 276, 277, 567}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}}};

}

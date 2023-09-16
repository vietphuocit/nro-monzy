package com.monzy.services.func;

import com.database.Database;
import com.monzy.consts.ConstNpc;
import com.monzy.jdbc.daos.PlayerDAO;
import com.monzy.models.item.Item;
import com.monzy.models.map.Zone;
import com.monzy.models.npc.Npc;
import com.monzy.models.npc.NpcManager;
import com.monzy.models.payment.TransactionHistory;
import com.monzy.models.player.Player;
import com.monzy.server.Client;
import com.monzy.server.Manager;
import com.monzy.services.*;
import com.monzy.utils.Logger;
import com.monzy.utils.Util;
import com.network.io.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Input {

  public static final int CHANGE_PASSWORD = 500;
  public static final int GIFT_CODE = 501;
  public static final int FIND_PLAYER = 502;
  public static final int CHANGE_NAME = 503;
  public static final int CHOOSE_LEVEL_BDKB = 504;
  public static final int NAP_THE = 505;
  public static final int CHANGE_NAME_BY_ITEM = 506;
  public static final int GIVE_IT = 507;
  public static final int VND_TO_HONG_NGOC = 508;
  public static final int VND_TO_THOI_VANG = 509;
  public static final int NAP = 516;
  public static final int MTV = 517;
  public static final int SEND_RUBY = 518;
  public static final int TAIHN = 510;
  public static final int XIUHN = 511;
  public static final int TAITV = 512;
  public static final int XIUTV = 513;
  public static final int DOI_RUONG_DONG_VANG = 515;
  public static final byte NUMERIC = 0;
  public static final byte ANY = 1;
  public static final byte PASSWORD = 2;
  private static final Map<Integer, Object> PLAYER_ID_OBJECT = new HashMap<>();
  public static String LOAI_THE;
  public static String MENH_GIA;
  private static Input intance;

  private Input() {}

  public static Input gI() {
    if (intance == null) {
      intance = new Input();
    }
    return intance;
  }

  public void doInput(Player player, Message msg) {
    Item thoiVangInBag = InventoryService.gI().findItem(player.inventory.itemsBag, 457);
    try {
      String[] text = new String[msg.reader().readByte()];
      for (int i = 0; i < text.length; i++) {
        text[i] = msg.reader().readUTF();
      }
      switch (player.iDMark.getTypeInput()) {
        case GIVE_IT:
          String name = text[0];
          int id = Integer.parseInt(text[1]);
          int q = Integer.parseInt(text[2]);
          if (Client.gI().getPlayer(name) != null) {
            Item item = ItemService.gI().createNewItem(((short) id));
            item.quantity = q;
            InventoryService.gI().addItemBag(Client.gI().getPlayer(name), item);
            InventoryService.gI().sendItemBags(Client.gI().getPlayer(name));
            Service.gI()
                .sendThongBao(
                    Client.gI().getPlayer(name),
                    "Nhận " + item.template.name + " từ " + player.name);
          } else {
            Service.gI().sendThongBao(player, "Không online");
          }
          break;
        case CHANGE_PASSWORD:
          Service.gI().changePassword(player, text[0], text[1], text[2]);
          break;
        case GIFT_CODE:
          GiftService.gI().giftCode(player, text[0]);
          break;
        case NAP:
          Player playerNap = Client.gI().getPlayer(text[0]);
          int vnd = Integer.parseInt(text[1]);
          if (playerNap != null) {
            TransactionHistory transactionHistory =
                new TransactionHistory(
                    String.valueOf(System.currentTimeMillis() + Util.nextInt(1000, 9999)),
                    vnd,
                    "nap " + playerNap.name,
                    "IN");
            PaymentService.gI().insertTranHis(transactionHistory, "nap", playerNap);
            PlayerDAO.addVND(playerNap, vnd * Manager.RATE_PAY);
            PlayerDAO.addTongNap(playerNap, vnd);
            // event
            //                        playerNap.event += vnd / 1000;
//            Item traiDua = new Item((short) 694);
//            traiDua.quantity = vnd / 1000;
//            InventoryService.gI().addItemBag(playerNap, traiDua);
            Item veTangNgoc = new Item((short) 718);
            veTangNgoc.quantity = vnd / 100000;
            InventoryService.gI().addItemBag(playerNap, veTangNgoc);
            InventoryService.gI().sendItemBags(playerNap);
            Service.gI().sendThongBao(player, "Đã nạp cho " + playerNap.name + " " + vnd + " vnd");
            Service.gI()
                .sendThongBao(
                    playerNap, "Bạn nhận được " + vnd + " vnd. Đến Santa để kiểm tra số dư!");
          } else {
            Service.gI().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline!");
          }
          break;
        case MTV:
          Player playerMTV = Client.gI().getPlayer(text[0]);
          if (playerMTV != null) {
            if (playerMTV.session.actived) {
              Service.gI().sendThongBao(player, playerMTV.name + " đã mở thành viên rồi!");
              return;
            }
            playerMTV.session.actived = true;
            PlayerDAO.activedUser(playerMTV);
            TransactionHistory transactionHistory =
                new TransactionHistory(
                    String.valueOf(System.currentTimeMillis() + Util.nextInt(1000, 9999)),
                    20000,
                    player.name + " mtv " + playerMTV.name,
                    "IN");
            PaymentService.gI().insertTranHis(transactionHistory, "mvt", playerMTV);
            Service.gI().sendThongBao(player, "Đã mở thành viên cho " + playerMTV.name + "!");
            Service.gI().sendThongBao(playerMTV, "Đã mở thành viên!");
            playerMTV.inventory.ruby += 20000;
            PlayerService.gI().sendInfoHpMpMoney(playerMTV);
          } else {
            Service.gI().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline!");
          }
          break;
        case SEND_RUBY:
          Player playerNHN = Client.gI().getPlayer(text[0]);
          int hongNgoc = Integer.parseInt(text[1]);
          if (playerNHN == null) {
            Service.gI().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline!");
            return;
          }
          if (player.inventory.ruby < hongNgoc) {
            Service.gI().sendThongBao(player, "Không đủ hồng ngọc để tặng");
            return;
          }
          Item veTangHongNgoc = InventoryService.gI().findItem(player.inventory.itemsBag, 718);
          if (veTangHongNgoc == null) {
            Service.gI().sendThongBao(player, "Không tìm thấy Vé tặng hồng ngọc");
            return;
          }
          player.inventory.ruby -= hongNgoc;
          InventoryService.gI().subQuantityItemsBag(player, veTangHongNgoc, 1);
          PlayerService.gI().sendInfoHpMpMoney(player);
          InventoryService.gI().sendItemBags(player);
          playerNHN.inventory.ruby += hongNgoc;
          PlayerService.gI().sendInfoHpMpMoney(playerNHN);
          Service.gI()
              .sendThongBao(
                  player, "Đã tặng cho " + playerNHN.name + " " + hongNgoc + " hồng ngọc");
          Service.gI().sendThongBao(playerNHN, "Bạn nhận được " + hongNgoc + " hồng ngọc!");
          break;
        case FIND_PLAYER:
          Player pl = Client.gI().getPlayer(text[0]);
          if (pl != null) {
            NpcService.gI()
                .createMenuConMeo(
                    player,
                    ConstNpc.MENU_FIND_PLAYER,
                    -1,
                    "Ngài muốn..?",
                    new String[] {
                      "Đi tới\n" + pl.name, "Gọi " + pl.name + "\ntới đây", "Đổi tên", "Ban", "Kick"
                    },
                    pl);
          } else {
            Service.gI().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline!");
          }
          break;
        case CHANGE_NAME:
          Player plChanged = (Player) PLAYER_ID_OBJECT.get((int) player.id);
          if (plChanged != null) {
            if (Objects.requireNonNull(
                    Database.executeQuery("select * from player where name = ?", text[0]))
                .next()) {
              Service.gI().sendThongBao(player, "Tên nhân vật đã tồn tại");
            } else {
              plChanged.name = text[0];
              Database.executeUpdate(
                  "update player set name = ? where id = ?", plChanged.name, plChanged.id);
              Service.gI().player(plChanged);
              Service.gI().sendCaiTrang(plChanged);
              Service.gI().sendFlagBag(plChanged);
              Zone zone = plChanged.zone;
              ChangeMapService.gI()
                  .changeMap(plChanged, zone, plChanged.location.x, plChanged.location.y);
              Service.gI()
                  .sendThongBao(
                      plChanged, "Chúc mừng bạn đã có cái tên mới đẹp đẽ hơn tên ban đầu");
              Service.gI().sendThongBao(player, "Đổi tên người chơi thành công");
            }
          }
          break;
        case CHANGE_NAME_BY_ITEM:
          Item theDoiTen = InventoryService.gI().findItem(player.inventory.itemsBag, 2006);
          if (theDoiTen == null) {
            Service.gI().sendThongBaoOK(player.session, "Không tìm thấy thẻ đổi tên");
            return;
          }
          if (text[0].length() > 10) {
            Service.gI().sendThongBaoOK(player.session, "Tên nhân vật tối đa 10 ký tự");
            return;
          }
          if (Objects.requireNonNull(Database.executeQuery("select * from player where name = ?", text[0])).next()) {
            Service.gI().sendThongBaoOK(player.session, "Tên nhân vật đã tồn tại");
            return;
          }
          if (!text[0].matches("^[a-z0-9]+$")) {
            Service.gI().sendThongBaoOK(player.session, "Tên nhân vật chỉ được chứa kí tự chữ cái thường.");
            return;
          }
          InventoryService.gI().subQuantityItemsBag(player, theDoiTen, 1);
          player.name = text[0];
          Database.executeUpdate("update player set name = ? where id = ?", player.name, player.id);
          Service.gI().player(player);
          Service.gI().sendCaiTrang(player);
          Service.gI().sendFlagBag(player);
          Zone zone = player.zone;
          ChangeMapService.gI().changeMap(player, zone, player.location.x, player.location.y);
          Service.gI().sendThongBao(player, "Chúc mừng bạn đã có cái tên mới đẹp đẽ hơn tên ban đầu");
          break;
        case TAIHN:
          int taiHongNgoc = Integer.parseInt(text[0]);
          if (player.inventory.ruby < taiHongNgoc) {
            Service.gI().sendThongBao(player, "Không đủ hồng ngọc kìa cu!!");
            return;
          }
          if (taiHongNgoc > 500000) {
            Service.gI().sendThongBao(player, "Tối đa 500000 Hồng Ngọc!!");
            return;
          }
          if (taiHongNgoc <= 1000) {
            Service.gI().sendThongBao(player, "Ít nhất 1000 hồng ngọc!!");
            return;
          }
          player.inventory.ruby -= taiHongNgoc;
          Service.gI().sendMoney(player);
          Thread threadTaiHN =
              new Thread(
                  () -> {
                    int timeSeconds = 10;
                    Service.gI()
                        .sendThongBao(player, "Chờ " + timeSeconds + " giây để biết kết quả.");
                    while (timeSeconds > 0) {
                      timeSeconds--;
                      try {
                        Thread.sleep(1000);
                      } catch (InterruptedException e) {
                        // Bỏ qua
                      }
                    }
                    int x = diceRoller(true);
                    int y = diceRoller(true, x);
                    int z = diceRoller(true, x, y);
                    int tong = (x + y + z);
                    if ((x + y + z) > 10 && (x + y + z) <= 18) {
                      player.inventory.ruby += taiHongNgoc * 1.8;
                      Service.gI().sendMoney(player);
                      Service.gI()
                          .sendThongBaoOK(
                              player,
                              "Kết quả"
                                  + "\nSố hệ thống quay ra : "
                                  + x
                                  + " "
                                  + y
                                  + " "
                                  + z
                                  + "\nTổng là : "
                                  + tong
                                  + "\nBạn đã cược : "
                                  + taiHongNgoc
                                  + " Hồng Ngọc vào Tài"
                                  + "\nKết quả : Tài"
                                  + "\n\nVề bờ");
                    } else if (3 <= (x + y + z) && (x + y + z) <= 10) {
                      Service.gI()
                          .sendThongBaoOK(
                              player,
                              "Kết quả"
                                  + "\nSố hệ thống quay ra là :"
                                  + " "
                                  + x
                                  + " "
                                  + y
                                  + " "
                                  + z
                                  + "\nTổng là : "
                                  + tong
                                  + "\nBạn đã cược : "
                                  + taiHongNgoc
                                  + " Hồng Ngọc vào Tài"
                                  + "\nKết quả : Xỉu"
                                  + "\nCòn cái nịt.");
                    }
                  });
          threadTaiHN.start();
          break;
        case XIUHN:
          int xiuHongNgoc = Integer.parseInt(text[0]);
          if (player.inventory.ruby < xiuHongNgoc) {
            Service.gI().sendThongBao(player, "Không đủ hồng ngọc kìa cu!!");
            return;
          }
          if (xiuHongNgoc > 500000) {
            Service.gI().sendThongBao(player, "Tối đa 500000 Hồng Ngọc!!");
            return;
          }
          if (xiuHongNgoc <= 1000) {
            Service.gI().sendThongBao(player, "Ít nhất 1000 hồng ngọc!!");
            return;
          }
          player.inventory.ruby -= xiuHongNgoc;
          Service.gI().sendMoney(player);
          Thread threadXiuHN =
              new Thread(
                  () -> {
                    int timeSeconds = 10;
                    Service.gI()
                        .sendThongBao(player, "Chờ " + timeSeconds + " giây để biết kết quả.");
                    while (timeSeconds > 0) {
                      timeSeconds--;
                      try {
                        Thread.sleep(1000);
                      } catch (InterruptedException e) {
                        // Bỏ qua
                      }
                    }
                    int x = diceRoller(false);
                    int y = diceRoller(false, x);
                    int z = diceRoller(false, x, y);
                    int tong = (x + y + z);
                    if (3 <= (x + y + z) && (x + y + z) <= 10) {
                      player.inventory.ruby += xiuHongNgoc * 1.8;
                      Service.gI().sendMoney(player);
                      Service.gI()
                          .sendThongBaoOK(
                              player,
                              "Kết quả"
                                  + "\nSố hệ thống quay ra : "
                                  + x
                                  + " "
                                  + y
                                  + " "
                                  + z
                                  + "\nTổng là : "
                                  + tong
                                  + "\nBạn đã cược : "
                                  + xiuHongNgoc
                                  + " Hồng Ngọc vào Xỉu"
                                  + "\nKết quả : Xỉu"
                                  + "\n\nVề bờ");
                    } else if ((x + y + z) > 10 && (x + y + z) <= 18) {
                      Service.gI()
                          .sendThongBaoOK(
                              player,
                              "Kết quả"
                                  + "\nSố hệ thống quay ra là :"
                                  + " "
                                  + x
                                  + " "
                                  + y
                                  + " "
                                  + z
                                  + "\nTổng là : "
                                  + tong
                                  + "\nBạn đã cược : "
                                  + xiuHongNgoc
                                  + " Hồng Ngọc vào Xỉu"
                                  + "\nKết quả : Tài"
                                  + "\nCòn cái nịt.");
                    }
                  });
          threadXiuHN.start();
          break;
        case TAITV:
          int taiThoiVang = Integer.parseInt(text[0]);
          if (taiThoiVang > 50000) {
            Service.gI().sendThongBao(player, "Tối đa 50000 Thỏi vàng!!");
            return;
          }
          if (taiThoiVang <= 10) {
            Service.gI().sendThongBao(player, "Ít nhất 10 thỏi!!");
            return;
          }
          if (InventoryService.gI().getCountEmptyBag(player) <= 1) {
            Service.gI().sendThongBao(player, "Ít nhất 2 ô trống trong hành trang!!");
            return;
          }
          if (thoiVangInBag == null) {
            Service.gI().sendThongBao(player, "Không có thỏi vàng trong túi!!");
            return;
          }
          if (thoiVangInBag.quantity < taiThoiVang) {
            Service.gI().sendThongBao(player, "Không đủ thỏi vàng kìa ba!!");
            return;
          }
          InventoryService.gI().subQuantityItemsBag(player, thoiVangInBag, taiThoiVang);
          InventoryService.gI().sendItemBags(player);
          Thread threadTaiTV =
              new Thread(
                  () -> {
                    int timeSeconds = 10;
                    Service.gI()
                        .sendThongBao(player, "Chờ " + timeSeconds + " giây để biết kết quả.");
                    while (timeSeconds > 0) {
                      timeSeconds--;
                      try {
                        Thread.sleep(1000);
                      } catch (InterruptedException e) {
                        // Bỏ qua
                      }
                    }
                    int x = diceRoller(true);
                    int y = diceRoller(true, x);
                    int z = diceRoller(true, x, y);
                    int tong = (x + y + z);
                    if (3 <= tong && tong <= 10) {
                      Service.gI()
                          .sendThongBaoOK(
                              player,
                              "Kết quả"
                                  + "\nSố hệ thống quay ra là :"
                                  + " "
                                  + x
                                  + " "
                                  + y
                                  + " "
                                  + z
                                  + "\nTổng là : "
                                  + tong
                                  + "\nBạn đã cược : "
                                  + taiThoiVang
                                  + " Thỏi vàng vào Tài"
                                  + "\nKết quả : Xỉu"
                                  + "\nCòn cái nịt.");
                    } else if (tong > 10 && tong <= 18) {
                      Item tvthang = ItemService.gI().createNewItem((short) 457);
                      tvthang.quantity = (int) Math.round(taiThoiVang * 1.8);
                      InventoryService.gI().addItemBag(player, tvthang);
                      InventoryService.gI().sendItemBags(player);
                      Service.gI()
                          .sendThongBaoOK(
                              player,
                              "Kết quả"
                                  + "\nSố hệ thống quay ra : "
                                  + x
                                  + " "
                                  + y
                                  + " "
                                  + z
                                  + "\nTổng là : "
                                  + tong
                                  + "\nBạn đã cược : "
                                  + taiThoiVang
                                  + " Thỏi vàng vào Tài"
                                  + "\nKết quả : Tài"
                                  + "\n\nVề bờ");
                    }
                  });
          threadTaiTV.start();
          break;
        case XIUTV:
          int xiuThoiVang = Integer.parseInt(text[0]);
          if (xiuThoiVang > 50000) {
            Service.gI().sendThongBao(player, "Tối đa 50000 Thỏi vàng!!");
            return;
          }
          if (xiuThoiVang <= 10) {
            Service.gI().sendThongBao(player, "Ít nhất 10 thỏi!!");
            return;
          }
          if (InventoryService.gI().getCountEmptyBag(player) <= 1) {
            Service.gI().sendThongBao(player, "Ít nhất 2 ô trống trong hành trang!!");
            return;
          }
          if (thoiVangInBag == null) {
            Service.gI().sendThongBao(player, "Không có thỏi vàng trong túi!!");
            return;
          }
          if (thoiVangInBag.quantity < xiuThoiVang) {
            Service.gI().sendThongBao(player, "Không đủ thỏi vàng kìa ba!!");
            return;
          }
          InventoryService.gI().subQuantityItemsBag(player, thoiVangInBag, xiuThoiVang);
          InventoryService.gI().sendItemBags(player);
          Thread threadXiuTV =
              new Thread(
                  () -> {
                    int timeSeconds = 10;
                    Service.gI()
                        .sendThongBao(player, "Chờ " + timeSeconds + " giây để biết kết quả.");
                    while (timeSeconds > 0) {
                      timeSeconds--;
                      try {
                        Thread.sleep(1000);
                      } catch (InterruptedException e) {
                        // Bỏ qua
                      }
                    }
                    int x = diceRoller(false);
                    int y = diceRoller(false, x);
                    int z = diceRoller(false, x, y);
                    int tong = (x + y + z);
                    if (tong > 10 && tong <= 18) {
                      Service.gI()
                          .sendThongBaoOK(
                              player,
                              "Kết quả"
                                  + "\nSố hệ thống quay ra là :"
                                  + " "
                                  + x
                                  + " "
                                  + y
                                  + " "
                                  + z
                                  + "\nTổng là : "
                                  + tong
                                  + "\nBạn đã cược : "
                                  + xiuThoiVang
                                  + " Thỏi vàng vào Xỉu"
                                  + "\nKết quả : Tài"
                                  + "\nCòn cái nịt.");
                    } else if (3 <= tong && tong <= 10) {
                      Item tvthang = ItemService.gI().createNewItem((short) 457);
                      tvthang.quantity = (int) Math.round(xiuThoiVang * 1.8);
                      InventoryService.gI().addItemBag(player, tvthang);
                      InventoryService.gI().sendItemBags(player);
                      Service.gI()
                          .sendThongBaoOK(
                              player,
                              "Kết quả"
                                  + "\nSố hệ thống quay ra : "
                                  + x
                                  + " "
                                  + y
                                  + " "
                                  + z
                                  + "\nTổng là : "
                                  + tong
                                  + "\nBạn đã cược : "
                                  + xiuThoiVang
                                  + " Thỏi vàng vào Xỉu"
                                  + "\nKết quả : Xỉu"
                                  + "\n\nVề bờ");
                    }
                  });
          threadXiuTV.start();
          break;
        case CHOOSE_LEVEL_BDKB:
          int level = Integer.parseInt(text[0]);
          if (level >= 1 && level <= 110) {
            Npc npc = NpcManager.getByIdAndMap(ConstNpc.QUY_LAO_KAME, player.zone.map.mapId);
            if (npc != null) {
              npc.createOtherMenu(
                  player,
                  ConstNpc.MENU_ACCEPT_GO_TO_BDKB,
                  "Con có chắc chắn muốn tới bản đồ kho báu cấp độ " + level + "?",
                  new String[] {"Đồng ý", "Từ chối"},
                  level);
            }
          } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
          }
          break;
        case NAP_THE:
          NapThe.SendCard(player, LOAI_THE, MENH_GIA, text[0], text[1]);
          break;
        case DOI_RUONG_DONG_VANG:
          int slruongcandoi = Integer.parseInt(text[0]);
          int sldongxuvangbitru = slruongcandoi * 99;
          if (slruongcandoi > 100) {
            Service.gI().sendThongBao(player, "Tối đa 100 rương 1 lần!!");
            return;
          }
          if (slruongcandoi <= 0) {
            Service.gI().sendThongBao(player, "Số Lượng không hợp lệ!!");
            return;
          }
          Item dongxuvang = null;
          for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 1229) {
              dongxuvang = item;
              break;
            }
          }
          if (dongxuvang != null && dongxuvang.quantity >= sldongxuvangbitru) {
            InventoryService.gI().subQuantityItemsBag(player, dongxuvang, sldongxuvangbitru);
            Item ruongdongvang = ItemService.gI().createNewItem((short) 1230);
            ruongdongvang.quantity = slruongcandoi;
            InventoryService.gI().addItemBag(player, ruongdongvang);
            InventoryService.gI().sendItemBags(player);
            Service.gI()
                .sendThongBao(
                    player,
                    "Chúc Mừng Bạn Đổi x"
                        + slruongcandoi
                        + " "
                        + ruongdongvang.template.name
                        + " Thành Công !");
          } else {
            assert dongxuvang != null;
            Service.gI()
                .sendThongBao(
                    player,
                    "Không đủ Bông Hồng bạn còn thiếu "
                        + (sldongxuvangbitru - dongxuvang.quantity)
                        + " Đồng Xu Vàng nữa!");
          }
          break;
        case VND_TO_HONG_NGOC:
          int ratioGold = 1; // tỉ lệ đổi tv
          int coinGold = 1; // là cái loz
          int goldTrade = Integer.parseInt(text[0]);
          if (goldTrade <= 0 || goldTrade >= 50000000) {
            Service.gI().sendThongBao(player, "giới hạn");
          } else if (player.session.vnd >= goldTrade * coinGold) {
            PlayerDAO.subVND(player, goldTrade * coinGold);
            Item thoiVang = ItemService.gI().createNewItem((short) 861, goldTrade); // x3
            InventoryService.gI().addItemBag(player, thoiVang);
            InventoryService.gI().sendItemBags(player);
            Service.gI()
                .sendThongBao(
                    player,
                    "bạn nhận được " + goldTrade * ratioGold + " " + thoiVang.template.name);
          } else {
            Service.gI()
                .sendThongBao(
                    player,
                    "Số tiền của bạn là "
                        + player.session.vnd
                        + " không đủ để quy "
                        + " đổi "
                        + goldTrade
                        + " Hồng Ngọc "
                        + " "
                        + "bạn cần thêm"
                        + (player.session.vnd - goldTrade));
          }
          break;
        case VND_TO_THOI_VANG:
          int ratioGem = 4; // tỉ lệ đổi tv
          int coinGem = 1000; // là cái loz
          int gemTrade = Integer.parseInt(text[0]);
          if (gemTrade <= 0 || gemTrade >= 50000000) {
            Service.gI().sendThongBao(player, "giới hạn");
          } else if (player.session.vnd >= gemTrade * coinGem) {
            PlayerDAO.subVND(player, gemTrade * coinGem);
            Item thoiVang = ItemService.gI().createNewItem((short) 457, gemTrade * 4); // x4
            InventoryService.gI().addItemBag(player, thoiVang);
            InventoryService.gI().sendItemBags(player);
            Service.gI()
                .sendThongBao(
                    player, "bạn nhận được " + gemTrade * ratioGem + " " + thoiVang.template.name);
          } else {
            Service.gI()
                .sendThongBao(
                    player,
                    "Số tiền của bạn là "
                        + player.session.vnd
                        + " không đủ để quy "
                        + " đổi "
                        + gemTrade
                        + " Thỏi Vàng"
                        + " "
                        + "bạn cần thêm"
                        + (player.session.vnd - gemTrade));
          }
          break;
      }
    } catch (Exception e) {
      Logger.logException(Input.class, e);
    }
  }

  public void createForm(Player pl, int typeInput, String title, SubInput... subInputs) {
    pl.iDMark.setTypeInput(typeInput);
    Message msg;
    try {
      msg = new Message(-125);
      msg.writer().writeUTF(title);
      msg.writer().writeByte(subInputs.length);
      for (SubInput si : subInputs) {
        msg.writer().writeUTF(si.name);
        msg.writer().writeByte(si.typeInput);
      }
      pl.sendMessage(msg);
      msg.cleanup();
    } catch (Exception e) {
      Logger.logException(Input.class, e);
    }
  }

  public void createFormChangePassword(Player pl) {
    createForm(
        pl,
        CHANGE_PASSWORD,
        "Quên Mật Khẩu",
        new SubInput("Nhập mật khẩu đã quên", PASSWORD),
        new SubInput("Mật khẩu mới", PASSWORD),
        new SubInput("Nhập lại mật khẩu mới", PASSWORD));
  }

  public void createFormNapForAdmin(Player pl) {
    createForm(pl, NAP, "Nạp coin", new SubInput("Tên", ANY), new SubInput("Số lượng", ANY));
  }

  public void createFormSendRuby(Player pl) {
    createForm(
        pl, SEND_RUBY, "Gửi hồng ngọc", new SubInput("Tên", ANY), new SubInput("Số lượng", ANY));
  }

  public void createFormMTV(Player pl) {
    createForm(pl, MTV, "Mở thành viên", new SubInput("Tên", ANY));
  }

  public void createFormGiftCode(Player pl) {
    createForm(pl, GIFT_CODE, "Gift code ", new SubInput("Gift-code", ANY));
  }

  public void createFormFindPlayer(Player pl) {
    createForm(pl, FIND_PLAYER, "Tìm kiếm người chơi", new SubInput("Tên người chơi", ANY));
  }

  public void TAI(Player pl) {
    createForm(pl, TAIHN, "Chọn số hồng ngọc đặt tài", new SubInput("Số hồng ngọc", ANY));
  }

  public void XIU(Player pl) {
    createForm(pl, XIUHN, "Chọn số hồng ngọc đặt xỉu", new SubInput("Số hồng ngọc", ANY));
  }

  public void TAITV(Player pl) {
    createForm(pl, TAITV, "Chọn số thỏi vàng đặt tài", new SubInput("Số thỏi vàng", ANY));
  }

  public void XIUTV(Player pl) {
    createForm(pl, XIUTV, "Chọn số thỏi vàng đặt xỉu", new SubInput("Số thỏi vàng", ANY));
  }

  public void createFormQDHN(Player pl) {
    createForm(
        pl,
        VND_TO_HONG_NGOC,
        "Quy đổi Hồng Ngọc tỉ lệ 1-1"
            + "\n50.000 Vnd = 50.000 Hồng ngọc ",
        new SubInput("Nhập số lượng muốn đổi", NUMERIC));
  }

  public void createFormQDTV(Player pl) {
    createForm(
        pl,
        VND_TO_THOI_VANG,
        "Quy đổi Thỏi Vàng"
            + "\nNhập 10 Có nghĩa là  10.000đ"
            + "\nTỉ Lệ Quy Đổi 10.000đ = 40 Thỏi Vàng",
        new SubInput("Nhập số lượng muốn đổi", NUMERIC));
  }

  public void createFormChangeName(Player pl, Player plChanged) {
    PLAYER_ID_OBJECT.put((int) pl.id, plChanged);
    createForm(pl, CHANGE_NAME, "Đổi tên " + plChanged.name, new SubInput("Tên mới", ANY));
  }

  public void createFormChangeNameByItem(Player pl) {
    createForm(pl, CHANGE_NAME_BY_ITEM, "Đổi tên " + pl.name, new SubInput("Tên mới", ANY));
  }

  public void createFormChooseLevelBDKB(Player pl) {
    createForm(pl, CHOOSE_LEVEL_BDKB, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
  }

  public int diceRoller(boolean selected, int... oldDiceRoller) {
    int sum = Arrays.stream(oldDiceRoller).sum();
    if (selected && (sum > 6 && sum < 10)) {
      return Util.nextInt(1, 4);
    }
    if (!selected && (sum > 4 && sum < 8)) {
      return Util.nextInt(3, 6);
    }
    return Util.nextInt(1, 6);
  }

  public static class SubInput {

    private final String name;
    private final byte typeInput;

    public SubInput(String name, byte typeInput) {
      this.name = name;
      this.typeInput = typeInput;
    }
  }
}

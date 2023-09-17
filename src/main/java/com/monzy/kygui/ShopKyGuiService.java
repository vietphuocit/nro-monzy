/*
 * Dev By Duy
 */
package com.monzy.kygui;

import com.monzy.consts.ConstNpc;
import com.monzy.models.item.Item;
import com.monzy.models.item.Item.ItemOption;
import com.monzy.models.player.Player;
import com.monzy.services.InventoryService;
import com.monzy.services.ItemService;
import com.monzy.services.NpcService;
import com.monzy.services.Service;
import com.monzy.utils.Logger;
import com.network.io.Message;

import java.util.*;

public class ShopKyGuiService implements Runnable {

  private static ShopKyGuiService instance;
  private static long lastTimeUpdate;

  public static ShopKyGuiService gI() {
    if (instance == null) {
      instance = new ShopKyGuiService();
    }
    return instance;
  }

  @Override
  public void run() {
    while (true) {
      // Kiểm tra nếu đã trôi qua 15 phút kể từ lần cuối cùng thực hiện
      if (System.currentTimeMillis() - lastTimeUpdate >= 15 * 60 * 1000) {
        // Thực hiện đoạn mã ở đây
        ShopKyGuiManager.gI().save();
        // Cập nhật thời gian thực hiện cuối cùng
        lastTimeUpdate = System.currentTimeMillis();
      }
      // Tạm dừng 1 giây trước khi kiểm tra lại
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        // Bỏ qua
      }
    }
  }

  private List<ItemKyGui> getItemKyGui(Player pl, byte tab, byte... max) {
    List<ItemKyGui> its = new ArrayList<>();
    List<ItemKyGui> listSort = new ArrayList<>();
    List<ItemKyGui> listSort2 = new ArrayList<>();
    ShopKyGuiManager.gI().listItem.stream().filter((it) -> (it != null && it.tab == tab && !it.isBuy && it.player_sell != pl.id)).forEachOrdered(its::add);
    its.stream().filter(Objects::nonNull).sorted(Comparator.comparing(i -> i.isUpTop, Comparator.reverseOrder())).forEach(listSort::add);
    if (max.length == 2) {
      if (listSort.size() > max[1]) {
        for (int i = max[0]; i < max[1]; i++) {
          if (listSort.get(i) != null) {
            listSort2.add(listSort.get(i));
          }
        }
      } else {
        for (int i = max[0]; i <= max[0]; i++) {
          if (listSort.get(i) != null) {
            listSort2.add(listSort.get(i));
          }
        }
      }
      return listSort2;
    }
    if (max.length == 1 && listSort.size() > max[0]) {
      for (int i = 0; i < max[0]; i++) {
        if (listSort.get(i) != null) {
          listSort2.add(listSort.get(i));
        }
      }
      return listSort2;
    }
    return listSort;
  }

  private List<ItemKyGui> getItemKyGui() {
    List<ItemKyGui> its = new ArrayList<>();
    List<ItemKyGui> listSort = new ArrayList<>();
    ShopKyGuiManager.gI().listItem.stream().filter((it) -> (it != null && !it.isBuy)).forEachOrdered(its::add);
    its.stream().filter(Objects::nonNull).sorted(Comparator.comparing(i -> i.isUpTop, Comparator.reverseOrder())).forEach(listSort::add);
    return listSort;
  }

  public void buyItem(Player player, int id) {
    if (player.nPoint.power < 40000000000L) {
      Service.gI().sendThongBao(player, "Yêu cầu sức mạnh lớn hơn 40 tỷ");
      openShopKyGui(player);
      return;
    }
    ItemKyGui it = getItemBuy(id);
    if (it == null || it.isBuy) {
      Service.gI().sendThongBao(player, "Vật phẩm không tồn tại hoặc đã được bán");
      return;
    }
    if (it.player_sell == player.id) {
      Service.gI().sendThongBao(player, "Không thể mua vật phẩm bản thân đăng bán");
      openShopKyGui(player);
      return;
    }
    boolean isBuy = false;
    if (it.goldSell > 0) {
      Item thoiVang = InventoryService.gI().findItemBag(player, 457);
      if (thoiVang == null || thoiVang.quantity < it.goldSell) {
        Service.gI().sendThongBao(player, "Bạn Không Đủ Thỏi Vàng Để Mua Vật Phẩm");
      } else {
        InventoryService.gI().subQuantityItemsBag(player, thoiVang, it.goldSell);
        isBuy = true;
      }
    } else if (it.rubySell > 0) {
      if (player.inventory.ruby >= it.rubySell) {
        player.inventory.ruby -= it.rubySell;
        isBuy = true;
      } else {
        Service.gI().sendThongBao(player, "Bạn không đủ hồng ngọc để mua vật phẩm này!");
        isBuy = false;
      }
    }
    Service.gI().sendMoney(player);
    if (isBuy) {
      Item item = ItemService.gI().createNewItem(it.itemId);
      item.quantity = it.quantity;
      item.itemOptions.addAll(it.options);
      it.isBuy = true;
      it.player_buy = (int) player.id;
      InventoryService.gI().addItemBag(player, item);
      InventoryService.gI().sendItemBags(player);
      Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
      openShopKyGui(player);
    }
  }

  public ItemKyGui getItemBuy(int id) {
    for (ItemKyGui it : getItemKyGui()) {
      if (it != null && it.id == id) {
        return it;
      }
    }
    return null;
  }

  public ItemKyGui getItemBuy(Player pl, int id) {
    for (ItemKyGui it : ShopKyGuiManager.gI().listItem) {
      if (it != null && it.id == id && it.player_sell == pl.id) {
        return it;
      }
    }
    return null;
  }

  public void openShopKyGui(Player pl, byte index, int page) {
    if (page > getItemKyGui(pl, index).size()) {
      return;
    }
    Message msg = null;
    try {
      msg = new Message(-100);
      msg.writer().writeByte(index);
      List<ItemKyGui> items = getItemKyGui(pl, index);
      List<ItemKyGui> itemsSend = getItemKyGui(pl, index, (byte) (page * 20), (byte) (page * 20 + 20));
      byte tab = (byte) (items.size() / 20 > 0 ? items.size() / 20 : 1);
      msg.writer().writeByte(tab); // max page
      msg.writer().writeByte(page);
      msg.writer().writeByte(itemsSend.size());
      for (ItemKyGui itk : itemsSend) {
        Item it = ItemService.gI().createNewItem(itk.itemId);
        it.itemOptions.clear();
        if (itk.options.isEmpty()) {
          it.itemOptions.add(new ItemOption(73, 0));
        } else {
          it.itemOptions.addAll(itk.options);
        }
        msg.writer().writeShort(it.template.id);
        msg.writer().writeShort(itk.id);
        msg.writer().writeInt(itk.goldSell);
        msg.writer().writeInt(itk.rubySell);
        msg.writer().writeByte(0); // buy type
        if (pl.session.version >= 222) {
          msg.writer().writeInt(itk.quantity);
        } else {
          msg.writer().writeByte(itk.quantity);
        }
        msg.writer().writeByte(itk.player_sell == pl.id ? 1 : 0); // isMe
        msg.writer().writeByte(it.itemOptions.size());
        for (int a = 0; a < it.itemOptions.size(); a++) {
          msg.writer().writeByte(it.itemOptions.get(a).optionTemplate.id);
          msg.writer().writeShort(it.itemOptions.get(a).param);
        }
        msg.writer().writeByte(0);
        // msg.writer().writeByte(0);
      }
      pl.sendMessage(msg);
    } catch (Exception e) {
      Logger.logException(ShopKyGuiService.class, e);
    } finally {
      if (msg != null) {
        msg.cleanup();
      }
    }
  }

  public void upItemToTop(Player pl, int id) {
    ItemKyGui it = getItemBuy(id);
    if (it == null || it.isBuy) {
      Service.gI().sendThongBao(pl, "Vật phẩm không tồn tại hoặc đã được bán");
      return;
    }
    if (it.player_sell != pl.id) {
      Service.gI().sendThongBao(pl, "Vật phẩm không thuộc quyền sở hữu");
      openShopKyGui(pl);
      return;
    }
    pl.iDMark.setIdItemUpTop(id);
    NpcService.gI()
        .createMenuConMeo(
            pl,
            ConstNpc.UP_TOP_ITEM,
            -1,
            "Bạn có muốn đưa vật phẩm ['"
                + ItemService.gI().createNewItem(it.itemId).template.name
                + "'] của bản thân lên trang đầu?\nYêu cầu 50 hồng ngọc.",
            "Đồng ý",
            "Từ Chối");
  }

  public void claimOrDel(Player player, byte action, int id) {
    ItemKyGui it = getItemBuy(player, id);
    switch (action) {
      case 1: // hủy vật phẩm
        if (it == null || it.isBuy) {
          Service.gI().sendThongBao(player, "Vật phẩm không tồn tại hoặc đã được bán");
          return;
        }
        if (it.player_sell != player.id) {
          Service.gI().sendThongBao(player, "Vật phẩm không thuộc quyền sở hữu");
          openShopKyGui(player);
          return;
        }
        Item item = ItemService.gI().createNewItem(it.itemId);
        item.quantity = it.quantity;
        item.itemOptions.addAll(it.options);
        if (ShopKyGuiManager.gI().listItem.remove(it)) {
          InventoryService.gI().addItemBag(player, item);
          InventoryService.gI().sendItemBags(player);
          Service.gI().sendMoney(player);
          Service.gI().sendThongBao(player, "Hủy bán vật phẩm thành công");
          openShopKyGui(player);
        }
        break;
      case 2: // nhận tiền
        if (it == null || !it.isBuy) {
          Service.gI().sendThongBao(player, "Vật phẩm không tồn tại hoặc chưa được bán");
          return;
        }
        if (it.player_sell != player.id) {
          Service.gI().sendThongBao(player, "Vật phẩm không thuộc quyền sở hữu");
          openShopKyGui(player);
          return;
        }
        if (it.goldSell > 0) {
          Item thoiVang = ItemService.gI().createNewItem((short) 457);
          thoiVang.quantity = it.goldSell;
          InventoryService.gI().addItemBag(player, thoiVang);
          InventoryService.gI().sendItemBags(player);
        } else if (it.rubySell > 0) {
          player.inventory.ruby += it.rubySell - it.rubySell * 5 / 100;
        }
        if (ShopKyGuiManager.gI().listItem.remove(it)) {
          Service.gI().sendMoney(player);
          Service.gI().sendThongBao(player, "Bạn đã bán vật phẩm thành công");
          openShopKyGui(player);
        }
        break;
    }
  }

  public List<ItemKyGui> getItemCanKiGui(Player pl) {
    List<ItemKyGui> its = new ArrayList<>();
    ShopKyGuiManager.gI().listItem.stream()
        .filter((it) -> (it != null && it.player_sell == pl.id))
        .forEachOrdered(its::add);
    pl.inventory.itemsBag.stream()
        .filter(
            (it) -> (it.isNotNullItem()
                && ((it.template.type < 5 && it.template.type >= 0)
                || it.template.type == 12
                || it.template.type == 33
                || it.template.type == 29)))
        .forEachOrdered(
            (it) -> its.add(
                new ItemKyGui(
                    InventoryService.gI().getIndexBag(pl, it),
                    it.template.id,
                    (int) pl.id,
                    (byte) 4,
                    -1,
                    -1,
                    it.quantity,
                    (byte) -1,
                    it.itemOptions,
                    false,
                    -1)));
    return its;
  }

  public int getMaxId() {
    try {
      List<Integer> id = new ArrayList<>();
      ShopKyGuiManager.gI().listItem.stream()
          .filter(Objects::nonNull)
          .forEachOrdered((it) -> id.add(it.id));
      return Collections.max(id);
    } catch (Exception e) {
      return 0;
    }
  }

  public byte getTabKiGui(Item it) {
    if (it.template.type >= 0 && it.template.type <= 2) {
      return 0;
    } else if (it.template.type >= 3 && it.template.type <= 4) {
      return 1;
    } else if (it.template.type == 29) {
      return 2;
    } else {
      return 3;
    }
  }

  public void KiGui(Player pl, int id, int money, byte moneyType, int quantity) {
    try {
      if (pl.nPoint.power < 40000000000L) {
        Service.gI().sendThongBao(pl, "Yêu cầu sức mạnh lớn hơn 40 tỷ");
        openShopKyGui(pl);
        return;
      }
      if (pl.inventory.ruby < 5) {
        Service.gI().sendThongBao(pl, "Bạn cần có ít nhất 5 hồng ngọc để làm phí đăng bán");
        return;
      }
      Item it = ItemService.gI().copyItem(pl.inventory.itemsBag.get(id));
      if (money <= 0 || quantity > it.quantity) {
        Service.gI().sendThongBao(pl, "Có lỗi xảy ra");
        openShopKyGui(pl);
        return;
      }
      if (quantity > 99) {
        Service.gI().sendThongBao(pl, "Ký gửi tối đa x99");
        openShopKyGui(pl);
        return;
      }
      pl.inventory.ruby -= 5;
      switch (moneyType) {
        case 0: // vàng
          InventoryService.gI().subQuantityItemsBag(pl, pl.inventory.itemsBag.get(id), quantity);
          ShopKyGuiManager.gI().listItem.add(new ItemKyGui(getMaxId() + 1, it.template.id, (int) pl.id,
              getTabKiGui(it), money, -1, quantity, (byte) 0, it.itemOptions, false, -1));
          InventoryService.gI().sendItemBags(pl);
          openShopKyGui(pl);
          Service.gI().sendMoney(pl);
          Service.gI().sendThongBao(pl, "Đăng bán thành công");
          break;
        case 1: // hồng ngọc
          InventoryService.gI().subQuantityItemsBag(pl, pl.inventory.itemsBag.get(id), quantity);
          ShopKyGuiManager.gI().listItem.add(new ItemKyGui(getMaxId() + 1, it.template.id, (int) pl.id,
              getTabKiGui(it), -1, money, quantity, (byte) 0, it.itemOptions, false, -1));
          InventoryService.gI().sendItemBags(pl);
          openShopKyGui(pl);
          Service.gI().sendMoney(pl);
          Service.gI().sendThongBao(pl, "Đăng bán thành công");
          break;
        default:
          Service.gI().sendThongBao(pl, "Có lỗi xảy ra");
          openShopKyGui(pl);
          break;
      }
    } catch (Exception e) {
      Logger.logException(ShopKyGuiService.class, e);
    }
  }

  public void openShopKyGui(Player player) {
    if (player.nPoint.power < 40000000000L) {
      Service.gI().sendThongBao(player, "Yêu cầu sức mạnh lớn hơn 40 tỷ");
      return;
    }
    if (!player.session.actived) {
      Service.gI()
          .sendThongBaoFromAdmin(
              player, "|5|VUI LÒNG KÍCH HOẠT TÀI KHOẢN\n|5|ĐỂ MỞ KHÓA TÍNH NĂNG KÝ GỬI");
      return;
    }
    Message msg = null;
    try {
      msg = new Message(-44);
      msg.writer().writeByte(2);
      msg.writer().writeByte(5);
      for (byte i = 0; i < 5; i++) {
        if (i == 4) {
          List<ItemKyGui> itemKyGuis = getItemCanKiGui(player);
          msg.writer().writeUTF(ShopKyGuiManager.gI().tabName[i]);
          msg.writer().writeByte(0);
          msg.writer().writeByte(itemKyGuis.size());
          for (ItemKyGui itemKyGui : itemKyGuis) {
            if (itemKyGui == null)
              continue;
            Item item = ItemService.gI().createNewItem(itemKyGui.itemId);
            item.itemOptions.clear();
            if (itemKyGui.options.isEmpty()) {
              item.itemOptions.add(new ItemOption(73, 0));
            } else {
              item.itemOptions.addAll(itemKyGui.options);
            }
            msg.writer().writeShort(item.template.id);
            msg.writer().writeShort(itemKyGui.id);
            msg.writer().writeInt(itemKyGui.goldSell);
            msg.writer().writeInt(itemKyGui.rubySell);
            if (getItemBuy(player, itemKyGui.id) == null) {
              msg.writer().writeByte(0); // buy type
            } else if (itemKyGui.isBuy) {
              msg.writer().writeByte(2);
            } else {
              msg.writer().writeByte(1);
            }
            if (player.session.version >= 222) {
              msg.writer().writeInt(itemKyGui.quantity);
            } else {
              msg.writer().writeByte(itemKyGui.quantity);
            }
            msg.writer().writeByte(1); // isMe
            msg.writer().writeByte(item.itemOptions.size());
            for (int a = 0; a < item.itemOptions.size(); a++) {
              msg.writer().writeByte(item.itemOptions.get(a).optionTemplate.id);
              msg.writer().writeShort(item.itemOptions.get(a).param);
            }
            msg.writer().writeByte(0);
            msg.writer().writeByte(0);
          }
        } else {
          List<ItemKyGui> items = getItemKyGui(player, i);
          List<ItemKyGui> itemsSend = getItemKyGui(player, i, (byte) 20);
          msg.writer().writeUTF(ShopKyGuiManager.gI().tabName[i]);
          byte tab = (byte) (items.size() / 20 > 0 ? items.size() / 20 : 1);
          msg.writer().writeByte(tab); // max page
          msg.writer().writeByte(itemsSend.size());
          for (int j = 0; j < itemsSend.size(); j++) {
            ItemKyGui itk = itemsSend.get(j);
            Item it = ItemService.gI().createNewItem(itk.itemId);
            it.itemOptions.clear();
            if (itk.options.isEmpty()) {
              it.itemOptions.add(new ItemOption(73, 0));
            } else {
              it.itemOptions.addAll(itk.options);
            }
            msg.writer().writeShort(it.template.id);
            msg.writer().writeShort(itk.id);
            msg.writer().writeInt(itk.goldSell);
            msg.writer().writeInt(itk.rubySell);
            msg.writer().writeByte(0); // buy type
            if (player.session.version >= 222) {
              msg.writer().writeInt(itk.quantity);
            } else {
              msg.writer().writeByte(itk.quantity);
            }
            msg.writer().writeByte(itk.player_sell == player.id ? 1 : 0); // isMe
            msg.writer().writeByte(it.itemOptions.size());
            for (int a = 0; a < it.itemOptions.size(); a++) {
              msg.writer().writeByte(it.itemOptions.get(a).optionTemplate.id);
              msg.writer().writeShort(it.itemOptions.get(a).param);
            }
            msg.writer().writeByte(0);
            msg.writer().writeByte(0);
          }
        }
      }
      player.sendMessage(msg);
    } catch (Exception e) {
      Logger.logException(ShopKyGuiService.class, e);
    } finally {
      if (msg != null) {
        msg.cleanup();
      }
    }
  }
}

package com.monzy.services.func;

import com.monzy.jdbc.daos.HistoryTransactionDAO;
import com.monzy.models.item.Item;
import com.monzy.models.player.Inventory;
import com.monzy.models.player.Player;
import com.monzy.services.*;
import com.monzy.utils.Logger;
import com.monzy.utils.Util;
import com.network.io.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Trade {

  public static final int TIME_TRADE = 180000;
  public static final int QUANLITY_MAX = 20000;
  private static final byte SUCCESS = 0;
  private static final byte FAIL_MAX_GOLD_PLAYER1 = 1;
  private static final byte FAIL_MAX_GOLD_PLAYER2 = 2;
  private static final byte FAIL_NOT_ENOUGH_BAG_P1 = 3;
  private static final byte FAIL_NOT_ENOUGH_BAG_P2 = 4;
  private static final List<Integer> IDS_ITEM_CAN_NOT_TRADE =
      Arrays.asList(694, 696, 718, 987, 2006, 1131, 935);
  private final long gold1Before;
  private final long gold2Before;
  private final List<Item> bag1Before;
  private final List<Item> bag2Before;
  public byte accept;
  private Player player1;
  private Player player2;
  private List<Item> itemsBag1;
  private List<Item> itemsBag2;
  private List<Item> itemsTrade1;
  private List<Item> itemsTrade2;
  private int goldTrade1;
  private int goldTrade2;
  private long lastTimeStart;
  private boolean start;

  public Trade(Player pl1, Player pl2) {
    this.player1 = pl1;
    this.player2 = pl2;
    this.gold1Before = pl1.inventory.gold;
    this.gold2Before = pl2.inventory.gold;
    this.bag1Before = InventoryService.gI().copyItemsBag(player1);
    this.bag2Before = InventoryService.gI().copyItemsBag(player2);
    this.itemsBag1 = InventoryService.gI().copyItemsBag(player1);
    this.itemsBag2 = InventoryService.gI().copyItemsBag(player2);
    this.itemsTrade1 = new ArrayList<>();
    this.itemsTrade2 = new ArrayList<>();
    TransactionService.PLAYER_TRADE.put(pl1, this);
    TransactionService.PLAYER_TRADE.put(pl2, this);
  }

  public void openTabTrade() {
    this.lastTimeStart = System.currentTimeMillis();
    this.start = true;
    Message msg;
    try {
      msg = new Message(-86);
      msg.writer().writeByte(1);
      msg.writer().writeInt((int) player1.id);
      player2.sendMessage(msg);
      msg.cleanup();
      msg = new Message(-86);
      msg.writer().writeByte(1);
      msg.writer().writeInt((int) player2.id);
      player1.sendMessage(msg);
      msg.cleanup();
      Service.gI().hideWaitDialog(player1);
      Service.gI().hideWaitDialog(player2);
    } catch (Exception e) {
      Logger.logException(Trade.class, e);
    }
  }

  public void addItemTrade(Player pl, byte index, int quantity) {
    //        System.out.println("quantity: " + quantity);
    if (pl.session.actived) {
      //        if (true) {
      if (index == -1) {
        if (pl.equals(this.player1)) {
          goldTrade1 = quantity;
        } else {
          goldTrade2 = quantity;
        }
      } else {
        Item item;
        if (pl.equals(this.player1)) {
          item = itemsBag1.get(index);
        } else {
          item = itemsBag2.get(index);
        }
        if (quantity > item.quantity || quantity < 0) {
          return;
        }
        if (isItemCannotTran(item)) {
          removeItemTrade(pl, index);
        } else {
          if (quantity > 99) {
            int n = quantity / 99;
            int left = quantity % 99;
            for (int i = 0; i < n; i++) {
              Item itemTrade = ItemService.gI().copyItem(item);
              itemTrade.quantity = 99;
              if (pl.equals(this.player1)) {
                InventoryService.gI().subQuantityItem(itemsBag1, item, itemTrade.quantity);
                itemsTrade1.add(itemTrade);
              } else {
                InventoryService.gI().subQuantityItem(itemsBag2, item, itemTrade.quantity);
                itemsTrade2.add(itemTrade);
              }
            }
            if (left > 0) {
              Item itemTrade = ItemService.gI().copyItem(item);
              itemTrade.quantity = left;
              if (pl.equals(this.player1)) {
                InventoryService.gI().subQuantityItem(itemsBag1, item, itemTrade.quantity);
                itemsTrade1.add(itemTrade);
              } else {
                InventoryService.gI().subQuantityItem(itemsBag2, item, itemTrade.quantity);
                itemsTrade2.add(itemTrade);
              }
            }
          } else {
            Item itemTrade = ItemService.gI().copyItem(item);
            itemTrade.quantity = quantity != 0 ? quantity : 1;
            if (pl.equals(this.player1)) {
              InventoryService.gI().subQuantityItem(itemsBag1, item, itemTrade.quantity);
              itemsTrade1.add(itemTrade);
            } else {
              InventoryService.gI().subQuantityItem(itemsBag2, item, itemTrade.quantity);
              itemsTrade2.add(itemTrade);
            }
          }
        }
      }
    } else {
      Service.gI()
          .sendThongBaoFromAdmin(
              pl, "|5|VUI LÒNG KÍCH HOẠT TÀI KHOẢN\n|5|ĐỂ MỞ KHÓA TÍNH NĂNG GIAO DỊCH");
      removeItemTrade(pl, index);
    }
  }

  private void removeItemTrade(Player pl, byte index) {
    Message msg;
    try {
      msg = new Message(-86);
      msg.writer().writeByte(2);
      msg.writer().write(index);
      pl.sendMessage(msg);
      msg.cleanup();
      Service.gI().sendThongBao(pl, "Không thể giao dịch vật phẩm này");
    } catch (Exception e) {
      Logger.logException(Trade.class, e);
    }
  }

  private void removeItemTrade2(Player pl, byte index) {
    Message msg;
    try {
      msg = new Message(-86);
      msg.writer().writeByte(2);
      msg.writer().write(index);
      pl.sendMessage(msg);
      msg.cleanup();
    } catch (Exception e) {
      Logger.logException(Trade.class, e);
    }
  }

  private boolean isItemCannotTran(Item item) {
    for (Item.ItemOption io : item.itemOptions) {
      if (io.optionTemplate.id == 30) {
        return true;
      }
    }
    switch (item.template.type) {
      case 27:
        return IDS_ITEM_CAN_NOT_TRADE.contains((int) item.template.id);
      case 5:
      case 6:
      case 7:
      case 8:
      case 11:
      case 13:
      case 21:
      case 22:
      case 23:
      case 24:
      case 28:
      case 31:
      case 32:
      case 34:
      case 72:
      case 73:
        return true;
      default:
        return false;
    }
  }

  public void cancelTrade() {
    String notifiText = "Giao dịch bị hủy bỏ";
    Service.gI().sendThongBao(player1, notifiText);
    Service.gI().sendThongBao(player2, notifiText);
    closeTab();
    dispose();
  }

  private void closeTab() {
    Message msg;
    try {
      msg = new Message(-86);
      msg.writer().writeByte(7);
      player1.sendMessage(msg);
      player2.sendMessage(msg);
      msg.cleanup();
    } catch (Exception e) {
      Logger.logException(Trade.class, e);
    }
  }

  public void dispose() {
    player1.iDMark.setPlayerTradeId(-1);
    player2.iDMark.setPlayerTradeId(-1);
    TransactionService.PLAYER_TRADE.remove(player1);
    TransactionService.PLAYER_TRADE.remove(player2);
    this.player1 = null;
    this.player2 = null;
    this.itemsBag1 = null;
    this.itemsBag2 = null;
    this.itemsTrade1 = null;
    this.itemsTrade2 = null;
  }

  public void lockTran(Player pl) {
    Message msg;
    try {
      msg = new Message(-86);
      msg.writer().writeByte(6);
      if (pl.equals(player1)) {
        msg.writer().writeInt(goldTrade1);
        msg.writer().writeByte(itemsTrade1.size());
        for (Item item : itemsTrade1) {
          msg.writer().writeShort(item.template.id);
          msg.writer().writeInt(item.quantity);
          msg.writer().writeByte(item.itemOptions.size());
          for (Item.ItemOption io : item.itemOptions) {
            msg.writer().writeByte(io.optionTemplate.id);
            msg.writer().writeShort(io.param);
          }
        }
        player2.sendMessage(msg);
      } else {
        msg.writer().writeInt(goldTrade2);
        msg.writer().writeByte(itemsTrade2.size());
        for (Item item : itemsTrade2) {
          msg.writer().writeShort(item.template.id);
          msg.writer().writeInt(item.quantity);
          msg.writer().writeByte(item.itemOptions.size());
          for (Item.ItemOption io : item.itemOptions) {
            msg.writer().writeByte(io.optionTemplate.id);
            msg.writer().writeShort(io.param);
          }
        }
        player1.sendMessage(msg);
      }
      msg.cleanup();
    } catch (Exception e) {
      Logger.logException(Trade.class, e);
    }
  }

  public void acceptTrade() {
    this.accept++;
    if (this.accept == 2) {
      this.startTrade();
    }
  }

  private void startTrade() {
    byte tradeStatus = SUCCESS;
    if (player1.inventory.gold + goldTrade2 > Inventory.LIMIT_GOLD) {
      tradeStatus = FAIL_MAX_GOLD_PLAYER1;
    } else if (player2.inventory.gold + goldTrade1 > Inventory.LIMIT_GOLD) {
      tradeStatus = FAIL_MAX_GOLD_PLAYER2;
    }
    if (tradeStatus != SUCCESS) {
      sendNotifyTrade(tradeStatus);
    } else {
      for (Item item : itemsTrade1) {
        if (!InventoryService.gI().addItemList(itemsBag2, item)) {
          tradeStatus = FAIL_NOT_ENOUGH_BAG_P1;
          break;
        }
      }
      if (tradeStatus != SUCCESS) {
        sendNotifyTrade(tradeStatus);
      } else {
        for (Item item : itemsTrade2) {
          if (!InventoryService.gI().addItemList(itemsBag1, item)) {
            tradeStatus = FAIL_NOT_ENOUGH_BAG_P2;
            break;
          }
        }
        if (tradeStatus == SUCCESS) {
          player1.inventory.gold += goldTrade2;
          player2.inventory.gold += goldTrade1;
          player1.inventory.gold -= goldTrade1;
          player2.inventory.gold -= goldTrade2;
          player1.inventory.itemsBag = itemsBag1;
          player2.inventory.itemsBag = itemsBag2;
          InventoryService.gI().sendItemBags(player1);
          InventoryService.gI().sendItemBags(player2);
          PlayerService.gI().sendInfoHpMpMoney(player1);
          PlayerService.gI().sendInfoHpMpMoney(player2);
          HistoryTransactionDAO.insert(
              player1,
              player2,
              goldTrade1,
              goldTrade2,
              itemsTrade1,
              itemsTrade2,
              bag1Before,
              bag2Before,
              this.player1.inventory.itemsBag,
              this.player2.inventory.itemsBag,
              gold1Before,
              gold2Before,
              this.player1.inventory.gold,
              this.player2.inventory.gold);
        }
        sendNotifyTrade(tradeStatus);
      }
    }
  }

  private void sendNotifyTrade(byte status) {
    player1.iDMark.setLastTimeTrade(System.currentTimeMillis());
    player2.iDMark.setLastTimeTrade(System.currentTimeMillis());
    switch (status) {
      case SUCCESS:
        Service.gI().sendThongBao(player1, "Giao dịch thành công");
        Service.gI().sendThongBao(player2, "Giao dịch thành công");
        break;
      case FAIL_MAX_GOLD_PLAYER1:
        Service.gI()
            .sendThongBao(player1, "Giao dịch thất bại do số lượng vàng sau giao dịch vượt tối đa");
        Service.gI()
            .sendThongBao(
                player2,
                "Giao dịch thất bại do số lượng vàng "
                    + player1.name
                    + " sau giao dịch vượt tối đa");
        break;
      case FAIL_MAX_GOLD_PLAYER2:
        Service.gI()
            .sendThongBao(player2, "Giao dịch thất bại do số lượng vàng sau giao dịch vượt tối đa");
        Service.gI()
            .sendThongBao(
                player1,
                "Giao dịch thất bại do số lượng vàng "
                    + player2.name
                    + " sau giao dịch vượt tối đa");
        break;
      case FAIL_NOT_ENOUGH_BAG_P1:
      case FAIL_NOT_ENOUGH_BAG_P2:
        Service.gI()
            .sendThongBao(
                player1, "Giao dịch thất bại do 1 trong 2 không đủ ô trống trong hành trang");
        Service.gI()
            .sendThongBao(
                player2, "Giao dịch thất bại do 1 trong 2 không đủ ô trống trong hành trang");
        break;
    }
  }

  public void update() {
    if (this.start && Util.canDoWithTime(lastTimeStart, TIME_TRADE)) {
      this.cancelTrade();
    }
  }
}

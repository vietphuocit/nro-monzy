package com.monzy.services;

import com.monzy.consts.ConstNpc;
import com.monzy.models.intrinsic.Intrinsic;
import com.monzy.models.player.Player;
import com.monzy.server.Manager;
import com.monzy.utils.Util;
import com.network.io.Message;

import java.util.List;

public class IntrinsicService {

  private static final int[] COST_OPEN = {10, 20, 40, 80, 160, 320, 640, 1280};
  private static IntrinsicService I;

  public static IntrinsicService gI() {
    if (IntrinsicService.I == null) {
      IntrinsicService.I = new IntrinsicService();
    }
    return IntrinsicService.I;
  }

  public List<Intrinsic> getIntrinsics(byte playerGender) {
    switch (playerGender) {
      case 0:
        return Manager.INTRINSIC_TD;
      case 1:
        return Manager.INTRINSIC_NM;
      default:
        return Manager.INTRINSIC_XD;
    }
  }

  public Intrinsic getIntrinsicById(int id) {
    for (Intrinsic intrinsic : Manager.INTRINSICS) {
      if (intrinsic.id == id) {
        return new Intrinsic(intrinsic);
      }
    }
    return null;
  }

  public void sendInfoIntrinsic(Player player) {
    Message msg;
    try {
      msg = new Message(112);
      msg.writer().writeByte(0);
      msg.writer().writeShort(player.playerIntrinsic.intrinsic.icon);
      msg.writer().writeUTF(player.playerIntrinsic.intrinsic.getName());
      player.sendMessage(msg);
      msg.cleanup();
    } catch (Exception e) {
    }
  }

  public void showAllIntrinsic(Player player) {
    List<Intrinsic> listIntrinsic = getIntrinsics(player.gender);
    Message msg;
    try {
      msg = new Message(112);
      msg.writer().writeByte(1);
      msg.writer().writeByte(1); // count tab
      msg.writer().writeUTF("Nội tại");
      msg.writer().writeByte(listIntrinsic.size() - 1);
      for (int i = 1; i < listIntrinsic.size(); i++) {
        msg.writer().writeShort(listIntrinsic.get(i).icon);
        msg.writer().writeUTF(listIntrinsic.get(i).getDescription());
      }
      player.sendMessage(msg);
      msg.cleanup();
    } catch (Exception e) {
    }
  }

  public void showMenu(Player player) {
    NpcService.gI()
        .createMenuConMeo(
            player,
            ConstNpc.INTRINSIC,
            -1,
            "Nội tại là một kỹ năng bị động hỗ trợ đặc biệt\nBạn có muốn mở hoặc thay đổi nội tại không?",
            "Xem\ntất cả\nNội Tại",
            "Mở\nNội Tại",
            "Mở VIP",
            "Từ chối");
  }

  public void setTSTD(Player player) {
    NpcService.gI()
        .createMenuConMeo(
            player,
            ConstNpc.MENU_SKH_THIEN_SU_TD,
            -1,
            "chọn lẹ đi để tau đi chơi với ny",
            "Set\nTaiyoken",
            "Set\nGenki",
            "Set\nkamejoko",
            "Từ chối");
  }

  public void setTSNM(Player player) {
    NpcService.gI()
        .createMenuConMeo(
            player,
            ConstNpc.MENU_SKH_THIEN_SU_NM,
            -1,
            "chọn lẹ đi để tau đi chơi với ny",
            "Set\ngod ki",
            "Set\ngod dame",
            "Set\nsummon",
            "Từ chối");
  }

  public void setTSXD(Player player) {
    NpcService.gI()
        .createMenuConMeo(
            player,
            ConstNpc.MENU_SKH_THIEN_SU_XD,
            -1,
            "chọn lẹ đi để tau đi chơi với ny",
            "Set\ngod galick",
            "Set\nmonkey",
            "Set\ngod hp",
            "Từ chối");
  }

  public void showConfirmOpen(Player player) {
    NpcService.gI()
        .createMenuConMeo(
            player,
            ConstNpc.CONFIRM_OPEN_INTRINSIC,
            -1,
            "Bạn muốn đổi Nội Tại khác\nvới giá là "
                + COST_OPEN[player.playerIntrinsic.countOpen]
                + " Tr vàng ?",
            "Mở\nNội Tại",
            "Từ chối");
  }

  public void showConfirmOpenVip(Player player) {
    NpcService.gI()
        .createMenuConMeo(
            player,
            ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP,
            -1,
            "Bạn có muốn mở Nội Tại\nvới giá là 100 ngọc và\ntái lập giá vàng quay lại ban đầu không?",
            "Mở\nNội VIP",
            "Từ chối");
  }

  private void changeIntrinsic(Player player) {
    List<Intrinsic> listIntrinsic = getIntrinsics(player.gender);
    player.playerIntrinsic.intrinsic =
        new Intrinsic(listIntrinsic.get(Util.nextInt(1, listIntrinsic.size() - 1)));
    player.playerIntrinsic.intrinsic.param1 =
        (short)
            Util.nextInt(
                player.playerIntrinsic.intrinsic.paramFrom1,
                player.playerIntrinsic.intrinsic.paramTo1);
    player.playerIntrinsic.intrinsic.param2 =
        (short)
            Util.nextInt(
                player.playerIntrinsic.intrinsic.paramFrom2,
                player.playerIntrinsic.intrinsic.paramTo2);
    Service.gI()
        .sendThongBao(
            player,
            "Bạn nhận được Nội tại:\n"
                + player
                    .playerIntrinsic
                    .intrinsic
                    .getName()
                    .substring(0, player.playerIntrinsic.intrinsic.getName().indexOf(" [")));
    sendInfoIntrinsic(player);
  }

  public void open(Player player) {
    if (player.nPoint.power >= 10000000000L) {
      int goldRequire = COST_OPEN[player.playerIntrinsic.countOpen] * 1000000;
      if (player.inventory.gold >= goldRequire) {
        player.inventory.gold -= goldRequire;
        PlayerService.gI().sendInfoHpMpMoney(player);
        changeIntrinsic(player);
        player.playerIntrinsic.countOpen++;
      } else {
        Service.gI()
            .sendThongBao(
                player,
                "Bạn không đủ vàng, còn thiếu "
                    + Util.numberToMoney(goldRequire - player.inventory.gold)
                    + " vàng nữa");
      }
    } else {
      Service.gI().sendThongBao(player, "Yêu cầu sức mạnh tối thiểu 10 tỷ");
    }
  }

  public void openVip(Player player) {
    if (player.nPoint.power >= 10000000000L) {
      int gemRequire = 100;
      if (player.inventory.gem >= 100) {
        player.inventory.gem -= gemRequire;
        PlayerService.gI().sendInfoHpMpMoney(player);
        changeIntrinsic(player);
        player.playerIntrinsic.countOpen = 0;
      } else {
        Service.gI()
            .sendThongBao(
                player,
                "Bạn không có đủ ngọc, còn thiếu "
                    + (gemRequire - player.inventory.gem)
                    + " ngọc nữa");
      }
    } else {
      Service.gI().sendThongBao(player, "Yêu cầu sức mạnh tối thiểu 10 tỷ");
    }
  }
}

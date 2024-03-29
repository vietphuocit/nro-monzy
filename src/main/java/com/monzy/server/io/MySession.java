package com.monzy.server.io;

import com.monzy.data.DataGame;
import com.monzy.jdbc.daos.GodGK;
import com.monzy.models.item.Item;
import com.monzy.models.player.Player;
import com.monzy.server.Client;
import com.monzy.server.Controller;
import com.monzy.server.Maintenance;
import com.monzy.server.Manager;
import com.monzy.server.model.AntiLogin;
import com.monzy.services.ItemService;
import com.monzy.services.Service;
import com.monzy.utils.Logger;
import com.network.io.Message;
import com.network.session.Session;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySession extends Session {

  public static final byte[] KEYS = {0};
  private static final Map<String, AntiLogin> ANTILOGIN = new HashMap<>();
  public Player player;
  public byte timeWait = 100;
  public boolean sentKey;
  public String ipAddress;
  public boolean isAdmin;
  public boolean isMod;
  public int userId;
  public String uu;
  public String pp;
  public int typeClient;
  public byte zoomLevel;
  public long lastTimeLogout;
  public boolean joinedGame;
  public boolean actived;
  public int tongnap;
  public List<Item> itemsReward;
  public String dataReward;
  public int version;
  public int vnd;
  public String referralCode;

  public MySession(Socket socket) {
    super(socket);
    ipAddress = socket.getInetAddress().getHostAddress();
  }

  public void initItemsReward() {
    try {
      this.itemsReward = new ArrayList<>();
      String[] itemsReward = dataReward.split(";");
      for (String itemInfo : itemsReward) {
        if (itemInfo == null || itemInfo.equals("")) {
          continue;
        }
        String[] subItemInfo = itemInfo.replaceAll("[{}\\[\\]]", "").split("\\|");
        String[] baseInfo = subItemInfo[0].split(":");
        int itemId = Integer.parseInt(baseInfo[0]);
        int quantity = Integer.parseInt(baseInfo[1]);
        Item item = ItemService.gI().createNewItem((short) itemId, quantity);
        if (subItemInfo.length == 2) {
          String[] options = subItemInfo[1].split(",");
          for (String opt : options) {
            if (opt == null || opt.equals("")) {
              continue;
            }
            String[] optInfo = opt.split(":");
            int tempIdOption = Integer.parseInt(optInfo[0]);
            int param = Integer.parseInt(optInfo[1]);
            item.itemOptions.add(new Item.ItemOption(tempIdOption, param));
          }
        }
        this.itemsReward.add(item);
      }
    } catch (Exception e) {
      Logger.logException(MySession.class, e);
    }
  }

  @Override
  public void sendKey() throws Exception {
    super.sendKey();
    this.startSend();
  }

  public void sendSessionKey() {
    Message msg = new Message(-27);
    try {
      msg.writer().writeByte(KEYS.length);
      msg.writer().writeByte(KEYS[0]);
      for (int i = 1; i < KEYS.length; i++) {
        msg.writer().writeByte(KEYS[i] ^ KEYS[i - 1]);
      }
      this.sendMessage(msg);
      msg.cleanup();
      sentKey = true;
    } catch (Exception e) {
      Logger.logException(MySession.class, e);
    }
  }

  public void login(String username, String password) {
    AntiLogin al = ANTILOGIN.get(this.ipAddress);
    if (al == null) {
      al = new AntiLogin();
      ANTILOGIN.put(this.ipAddress, al);
    }
    if (!al.canLogin()) {
      Service.gI().sendThongBaoOK(this, al.getNotifyCannotLogin());
      return;
    }
    if (Manager.LOCAL) {
      Service.gI().sendThongBaoOK(this, "Server này chỉ để lưu dữ liệu\nVui lòng qua server khác");
      return;
    }
    if (Maintenance.isRunning) {
      Service.gI()
          .sendThongBaoOK(this, "Server đang trong thời gian bảo trì, vui lòng quay lại sau");
      return;
    }
    if (!this.isAdmin && Client.gI().getPlayers().size() >= Manager.MAX_PLAYER) {
      Service.gI()
          .sendThongBaoOK(
              this, "Máy chủ hiện đang quá tải, " + "cư dân vui lòng di chuyển sang máy chủ khác.");
      return;
    }
    if (this.player == null) {
      Player player = null;
      try {
        long st = System.currentTimeMillis();
        this.uu = username;
        this.pp = password;
        player = GodGK.login(this, al);
        if (player != null) {
          // -77 max small
          DataGame.sendSmallVersion(this);
          // -93 bgitem version
          Service.gI().sendMessage(this, -93, "1630679752231_-93_r");
          this.timeWait = 0;
          this.joinedGame = true;
          player.nPoint.calPoint();
          player.nPoint.setHp(player.nPoint.hp);
          player.nPoint.setMp(player.nPoint.mp);
          player.zone.addPlayer(player);
          if (player.pet != null) {
            player.pet.nPoint.calPoint();
            player.pet.nPoint.setHp(player.pet.nPoint.hp);
            player.pet.nPoint.setMp(player.pet.nPoint.mp);
          }
          player.setSession(this);
          Client.gI().put(player);
          this.player = player;
          // -28 -4 version data game
          DataGame.sendVersionGame(this);
          // -31 data item background
          DataGame.sendDataItemBG(this);
          Controller.getInstance().sendInfo(this);
          Logger.warning(
              "Login thành công player "
                  + this.player.name
                  + ": "
                  + (System.currentTimeMillis() - st)
                  + " ms");
        }
      } catch (Exception e) {
        if (player != null) {
          player.dispose();
        }
        Logger.logException(MySession.class, e);
      }
    }
  }
}

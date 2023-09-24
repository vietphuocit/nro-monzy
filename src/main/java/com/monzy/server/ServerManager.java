package com.monzy.server;

import com.monzy.giftcode.GiftCodeManager;
import com.monzy.kygui.ShopKyGuiManager;
import com.monzy.kygui.ShopKyGuiService;
import com.monzy.models.boss.BossManager;
import com.monzy.models.item.Item;
import com.monzy.models.map.dhvt.MartialCongressManager;
import com.monzy.models.matches.pvp.DaiHoiVoThuat;
import com.monzy.models.player.Player;
import com.monzy.server.io.MyKeyHandler;
import com.monzy.server.io.MySession;
import com.monzy.services.*;
import com.monzy.utils.Logger;
import com.monzy.utils.Util;
import com.network.example.MessageSendCollect;
import com.network.server.ISessionAcceptHandler;
import com.network.server.MonzyServer;
import com.network.session.ISession;

import java.net.ServerSocket;
import java.util.*;

public class ServerManager {

  public static final Map<Object, Object> CLIENTS = new HashMap<>();
  public static String timeStart;
  public static String NAME = "Girlkun75";
  public static int PORT = 14445;
  public static ServerSocket listenSocket;
  public static boolean isRunning;
  private static ServerManager instance;

  public static ServerManager gI() {
    if (instance == null) {
      instance = new ServerManager();
      Manager.gI();
    }
    return instance;
  }

  public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
    ServerManager.gI().run();
  }

  public void run() {
    isRunning = true;
//    activeCommandLine();
//    activeGame();
    activeServerSocket();
    //    NapThe.callbackAPI();
    new Thread(DaiHoiVoThuat.gI(), "Thread DHVT").start();
    //        ChonAiDay.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
    //        new Thread(ChonAiDay.gI(), "Thread CAD").start();
    NgocRongNamecService.gI().initNgocRongNamec((byte) 0);
    new Thread(NgocRongNamecService.gI(), "Thread NRNM").start();
    new Thread(new TopService(), "Thread Top").start();
    //    new Thread(new PaymentService(), "Thread Payment").start();
    new Thread(new ShopKyGuiService(), "Thread Shop Ky Gui").start();
    new Thread(new GiftService(), "Thread Gift Code").start();
    new Thread(new MartialCongressManager(), "Thread DHVT 23").start();
    BossManager.gI().loadBoss();
  }

  private void act() throws Exception {
    MonzyServer.gI()
        .init()
        .setAcceptHandler(
            new ISessionAcceptHandler() {
              @Override
              public void sessionInit(ISession is) {
                //                antiddos girlkun
                if (!canConnectWithIp(is.getIP())) {
                  is.disconnect();
                  return;
                }
                is.setMessageHandler(Controller.getInstance())
                    .setSendCollect(new MessageSendCollect())
                    .setKeyHandler(new MyKeyHandler())
                    .startCollect();
              }

              @Override
              public void sessionDisconnect(ISession session) {
                Client.gI().kickSession((MySession) session);
              }
            })
        .setTypeSessioClone(MySession.class)
        .setDoSomeThingWhenClose(
            () -> {
              Logger.info("SERVER CLOSE");
              System.exit(0);
            })
        .start(PORT);
  }

  private void activeServerSocket() {
    try {
      this.act();
    } catch (Exception e) {
      e.getStackTrace();
    }
  }

  private boolean canConnectWithIp(String ipAddress) {
    Object o = CLIENTS.get(ipAddress);
    if (o == null) {
      CLIENTS.put(ipAddress, 1);
      return true;
    } else {
      int n = Integer.parseInt(String.valueOf(o));
      if (n < Manager.MAX_PER_IP) {
        n++;
        CLIENTS.put(ipAddress, n);
        return true;
      } else {
        return false;
      }
    }
  }

  public void disconnect(MySession session) {
    Object o = CLIENTS.get(session.getIP());
    if (o != null) {
      int n = Integer.parseInt(String.valueOf(o));
      n--;
      if (n < 0) {
        n = 0;
      }
      CLIENTS.put(session.getIP(), n);
    }
  }

  private void activeCommandLine() {
    new Thread(
            () -> {
              Scanner sc = new Scanner(System.in);
              while (true) {
                String line = sc.nextLine();
                if (line.equals("baotri")) {
                  Maintenance.gI().start(60 * 2);
                } else if (line.equals("athread")) {
                  ServerNotify.gI().notify("Nro JIEN debug server: " + Thread.activeCount());
                } else if (line.equals("nplayer")) {
                  Logger.error("Player in game: " + Client.gI().getPlayers().size() + "\n");
                } else if (line.equals("admin")) {
                  new Thread(() -> Client.gI().close()).start();
                } else if (line.startsWith("bang")) {
                  new Thread(
                          () -> {
                            try {
                              ClanService.gI().close();
                              Logger.error("Save " + Manager.CLANS.size() + " bang");
                            } catch (Exception e) {
                              Logger.error("Lỗi save clan!...................................\n");
                            }
                          })
                      .start();
                } else if (line.startsWith("a")) {
                  String a = line.replace("a ", "");
                  Service.gI().sendThongBaoAllPlayer(a);
                } else if (line.startsWith("qua")) {
                  //                    =1-1-1-1=1-1-1-1=
                  //                     =playerId-quantily-itemId-sql=optioneId-pagram=
                  try {
                    List<Item.ItemOption> ios = new ArrayList<>();
                    String[] pagram1 = line.split("=")[1].split("-");
                    String[] pagram2 = line.split("=")[2].split("-");
                    if (pagram1.length == 4 && pagram2.length % 2 == 0) {
                      Player p = Client.gI().getPlayer(Integer.parseInt(pagram1[0]));
                      if (p != null) {
                        for (int i = 0; i < pagram2.length; i += 2) {
                          ios.add(
                              new Item.ItemOption(
                                  Integer.parseInt(pagram2[i]), Integer.parseInt(pagram2[i + 1])));
                        }
                        Item i =
                            Util.sendDo(
                                Integer.parseInt(pagram1[2]), Integer.parseInt(pagram1[3]), ios);
                        i.quantity = Integer.parseInt(pagram1[1]);
                        InventoryService.gI().addItemBag(p, i);
                        InventoryService.gI().sendItemBags(p);
                        Service.gI().sendThongBao(p, "Admin trả đồ. anh em thông cảm nhé...");
                      } else {
                        System.out.println("Người chơi không online");
                      }
                    }
                  } catch (Exception e) {
                    System.out.println("Lỗi quà");
                  }
                }
              }
            },
            "Active line")
        .start();
  }

  private void activeGame() {}

  public void close() {
    MonzyServer.gI().stopConnect();
    isRunning = false;
    try {
      ClanService.gI().close();
    } catch (Exception e) {
      Logger.error("Lỗi save clan!...................................\n");
    }
    Client.gI().close();
    ShopKyGuiManager.gI().save();
    GiftCodeManager.gI().saveGiftCode();
    Logger.success("SUCCESSFULLY MAINTENANCE!...................................\n");
    System.exit(0);
  }
}

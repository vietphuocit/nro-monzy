package com.monzy.services.func;

import com.database.Database;
import com.monzy.consts.ConstNpc;
import com.monzy.jdbc.daos.PlayerDAO;
import com.monzy.models.item.Item;
import com.monzy.models.map.Zone;
import com.monzy.models.npc.Npc;
import com.monzy.models.npc.NpcManager;
import com.monzy.models.player.Player;
import com.monzy.server.Client;
import com.monzy.services.*;
import com.monzy.utils.Util;
import com.network.io.Message;
import com.network.session.ISession;

import java.util.HashMap;
import java.util.Map;

public class Input {

    public static String LOAI_THE;
    public static String MENH_GIA;
    private static final Map<Integer, Object> PLAYER_ID_OBJECT = new HashMap<Integer, Object>();
    public static final int CHANGE_PASSWORD = 500;
    public static final int GIFT_CODE = 501;
    public static final int FIND_PLAYER = 502;
    public static final int CHANGE_NAME = 503;
    public static final int CHOOSE_LEVEL_BDKB = 504;
    public static final int NAP_THE = 505;
    public static final int CHANGE_NAME_BY_ITEM = 506;
    public static final int GIVE_IT = 507;
    public static final int QUY_DOI_COIN = 508;
    public static final int QUY_DOI_HONG_NGOC = 509;
    public static final int NAP = 516;
    public static final int TAIHN = 510;
    public static final int XIUHN = 511;
    public static final int TAITV = 512;
    public static final int XIUTV = 513;
    public static final int DOI_RUONG_DONG_VANG = 515;
    public static final byte NUMERIC = 0;
    public static final byte ANY = 1;
    public static final byte PASSWORD = 2;
    private static Input intance;

    private Input() {
    }

    public static Input gI() {
        if (intance == null) {
            intance = new Input();
        }
        return intance;
    }

    public void doInput(Player player, Message msg) {
        Item thoiVangInBag = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 457);
        try {
            String[] text = new String[msg.reader().readByte()];
            for (int i = 0; i < text.length; i++) {
                text[i] = msg.reader().readUTF();
            }
            switch (player.iDMark.getTypeInput()) {
                case GIVE_IT:
                    String name = text[0];
                    int id = Integer.valueOf(text[1]);
                    int q = Integer.valueOf(text[2]);
                    if (Client.gI().getPlayer(name) != null) {
                        Item item = ItemService.gI().createNewItem(((short) id));
                        item.quantity = q;
                        InventoryServiceNew.gI().addItemBag(Client.gI().getPlayer(name), item);
                        InventoryServiceNew.gI().sendItemBags(Client.gI().getPlayer(name));
                        Service.gI().sendThongBao(Client.gI().getPlayer(name), "Nhận " + item.template.name + " từ " + player.name);
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
                    String Name = text[0];
                    int coin = Integer.parseInt(text[1]);
                    if (Client.gI().getPlayer(Name) != null) {
                        PlayerDAO.addvnd(Client.gI().getPlayer(Name), coin);
                        PlayerDAO.addTongNap(Client.gI().getPlayer(Name), coin);
                        Service.gI().sendThongBao(player, Name + " nhận được " + coin + " vnd");
                    } else {
                        Service.gI().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                    }
                    break;
                case FIND_PLAYER:
                    Player pl = Client.gI().getPlayer(text[0]);
                    if (pl != null) {
                        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_FIND_PLAYER, -1, "Ngài muốn..?",
                                new String[]{"Đi tới\n" + pl.name, "Gọi " + pl.name + "\ntới đây", "Đổi tên", "Ban", "Kick"},
                                pl);
                    } else {
                        Service.gI().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                    }
                    break;
                case CHANGE_NAME: {
                    Player plChanged = (Player) PLAYER_ID_OBJECT.get((int) player.id);
                    if (plChanged != null) {
                        if (Database.executeQuery("select * from player where name = ?", text[0]).next()) {
                            Service.gI().sendThongBao(player, "Tên nhân vật đã tồn tại");
                        } else {
                            plChanged.name = text[0];
                            Database.executeUpdate("update player set name = ? where id = ?", plChanged.name, plChanged.id);
                            Service.gI().player(plChanged);
                            Service.gI().Send_Caitrang(plChanged);
                            Service.gI().sendFlagBag(plChanged);
                            Zone zone = plChanged.zone;
                            ChangeMapService.gI().changeMap(plChanged, zone, plChanged.location.x, plChanged.location.y);
                            Service.gI().sendThongBao(plChanged, "Chúc mừng bạn đã có cái tên mới đẹp đẽ hơn tên ban đầu");
                            Service.gI().sendThongBao(player, "Đổi tên người chơi thành công");
                        }
                    }
                }
                break;
                case CHANGE_NAME_BY_ITEM: {
                    if (player != null) {
                        if (Database.executeQuery("select * from player where name = ?", text[0]).next()) {
                            Service.gI().sendThongBao(player, "Tên nhân vật đã tồn tại");
                            createFormChangeNameByItem(player);
                        } else {
                            Item theDoiTen = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2006);
                            if (theDoiTen == null) {
                                Service.gI().sendThongBao(player, "Không tìm thấy thẻ đổi tên");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(player, theDoiTen, 1);
                                player.name = text[0];
                                Database.executeUpdate("update player set name = ? where id = ?", player.name, player.id);
                                Service.gI().player(player);
                                Service.gI().Send_Caitrang(player);
                                Service.gI().sendFlagBag(player);
                                Zone zone = player.zone;
                                ChangeMapService.gI().changeMap(player, zone, player.location.x, player.location.y);
                                Service.gI().sendThongBao(player, "Chúc mừng bạn đã có cái tên mới đẹp đẽ hơn tên ban đầu");
                            }
                        }
                    }
                }
                break;
                case TAIHN:
                    int taiHongNgoc = Integer.parseInt(text[0]);
                    if (taiHongNgoc > 500000) {
                        Service.getInstance().sendThongBao(player, "Tối đa 500000 Hồng Ngọc!!");
                        return;
                    }
                    if (taiHongNgoc <= 1000) {
                        Service.getInstance().sendThongBao(player, "Ít nhất 1000 hồng ngọc!!");
                        return;
                    }
                    player.inventory.ruby -= taiHongNgoc;
                    Service.gI().sendMoney(player);
                    Thread threadTaiHN = new Thread(() -> {
                        int timeSeconds = 10;
                        Service.getInstance().sendThongBao(player, "Chờ " + timeSeconds + " giây để biết kết quả.");
                        while (timeSeconds > 0) {
                            timeSeconds--;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        int x = Util.nextInt(1, 6);
                        int y = Util.nextInt(1, 6);
                        int z = Util.nextInt(1, 6);
                        int tong = (x + y + z);
                        if ((x + y + z) > 10 && (x + y + z) <= 18) {
                            player.inventory.ruby += taiHongNgoc * 1.8;
                            Service.gI().sendMoney(player);
                            Service.getInstance().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + x + " " +
                                    y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + taiHongNgoc +
                                    " Hồng Ngọc vào Tài" + "\nKết quả : Tài" + "\n\nVề bờ");
                        } else if (3 <= (x + y + z) && (x + y + z) <= 10) {
                            Service.getInstance().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra là :" +
                                    " " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : "
                                    + taiHongNgoc + " Hồng Ngọc vào Tài" + "\nKết quả : Xỉu" + "\nCòn cái nịt.");
                        }
                    });
                    threadTaiHN.start();
                    break;
                case XIUHN:
                    int xiuHongNgoc = Integer.parseInt(text[0]);
                    if (xiuHongNgoc > 500000) {
                        Service.getInstance().sendThongBao(player, "Tối đa 500000 Hồng Ngọc!!");
                        return;
                    }
                    if (xiuHongNgoc <= 1000) {
                        Service.getInstance().sendThongBao(player, "Ít nhất 1000 hồng ngọc!!");
                        return;
                    }
                    player.inventory.ruby -= xiuHongNgoc;
                    Service.gI().sendMoney(player);
                    Thread threadXiuHN = new Thread(() -> {
                        int timeSeconds = 10;
                        Service.getInstance().sendThongBao(player, "Chờ " + timeSeconds + " giây để biết kết quả.");
                        while (timeSeconds > 0) {
                            timeSeconds--;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        int x = Util.nextInt(1, 6);
                        int y = Util.nextInt(1, 6);
                        int z = Util.nextInt(1, 6);
                        int tong = (x + y + z);
                        if (3 <= (x + y + z) && (x + y + z) <= 10) {
                            player.inventory.ruby += xiuHongNgoc * 1.8;
                            Service.gI().sendMoney(player);
                            Service.getInstance().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + x + " " +
                                    y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + xiuHongNgoc +
                                    " Hồng Ngọc vào Xỉu" + "\nKết quả : Xỉu" + "\n\nVề bờ");
                        } else if ((x + y + z) > 10 && (x + y + z) <= 18) {
                            Service.getInstance().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra là :" +
                                    " " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : "
                                    + xiuHongNgoc + " Hồng Ngọc vào Xỉu" + "\nKết quả : Tài" + "\nCòn cái nịt.");
                        }
                    });
                    threadXiuHN.start();
                    break;
                case TAITV:
                    int taiThoiVang = Integer.parseInt(text[0]);
                    if (taiThoiVang > 50000) {
                        Service.getInstance().sendThongBao(player, "Tối đa 50000 Thỏi vàng!!");
                        return;
                    }
                    if (taiThoiVang <= 10) {
                        Service.getInstance().sendThongBao(player, "Ít nhất 10 thỏi!!");
                        return;
                    }
                    if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 1) {
                        Service.getInstance().sendThongBao(player, "Ít nhất 2 ô trống trong hành trang!!");
                        return;
                    }
                    if (thoiVangInBag == null) {
                        Service.getInstance().sendThongBao(player, "Không có thỏi vàng trong túi!!");
                        return;
                    }
                    if (thoiVangInBag.quantity < taiThoiVang) {
                        Service.getInstance().sendThongBao(player, "Không đủ thỏi vàng kìa ba!!");
                        return;
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, thoiVangInBag, taiThoiVang);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Thread threadTaiTV = new Thread(() -> {
                        int timeSeconds = 10;
                        Service.getInstance().sendThongBao(player, "Chờ " + timeSeconds + " giây để biết kết quả.");
                        while (timeSeconds > 0) {
                            timeSeconds--;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        int x = Util.nextInt(1, 6);
                        int y = Util.nextInt(1, 6);
                        int z = Util.nextInt(1, 6);
                        int tong = (x + y + z);
                        if (3 <= (x + y + z) && (x + y + z) <= 10) {
                            Service.getInstance().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra là :" +
                                    " " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : "
                                    + taiThoiVang + " Thỏi vàng vào Tài" + "\nKết quả : Xỉu" + "\nCòn cái nịt.");
                        } else if ((x + y + z) > 10 && (x + y + z) <= 18) {
                            Item tvthang = ItemService.gI().createNewItem((short) 457);
                            tvthang.quantity = (int) Math.round(taiThoiVang * 1.8);
                            InventoryServiceNew.gI().addItemBag(player, tvthang);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.getInstance().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + x + " " +
                                    y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + taiThoiVang +
                                    " Thỏi vàng vào Tài" + "\nKết quả : Tài" + "\n\nVề bờ");
                        }
                    });
                    threadTaiTV.start();
                    break;
                case XIUTV:
                    int xiuThoiVang = Integer.parseInt(text[0]);
                    if (xiuThoiVang > 50000) {
                        Service.getInstance().sendThongBao(player, "Tối đa 50000 Thỏi vàng!!");
                        return;
                    }
                    if (xiuThoiVang <= 10) {
                        Service.getInstance().sendThongBao(player, "Ít nhất 10 thỏi!!");
                        return;
                    }
                    if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 1) {
                        Service.getInstance().sendThongBao(player, "Ít nhất 2 ô trống trong hành trang!!");
                        return;
                    }
                    if (thoiVangInBag == null) {
                        Service.getInstance().sendThongBao(player, "Không có thỏi vàng trong túi!!");
                        return;
                    }
                    if (thoiVangInBag.quantity < xiuThoiVang) {
                        Service.getInstance().sendThongBao(player, "Không đủ thỏi vàng kìa ba!!");
                        return;
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, thoiVangInBag, xiuThoiVang);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Thread threadXiuTV = new Thread(() -> {
                        int timeSeconds = 10;
                        Service.getInstance().sendThongBao(player, "Chờ " + timeSeconds + " giây để biết kết quả.");
                        while (timeSeconds > 0) {
                            timeSeconds--;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        int x = Util.nextInt(1, 6);
                        int y = Util.nextInt(1, 6);
                        int z = Util.nextInt(1, 6);
                        int tong = (x + y + z);
                        if ((x + y + z) > 10 && (x + y + z) <= 18) {
                            Service.getInstance().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra là :" +
                                    " " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : "
                                    + xiuThoiVang + " Thỏi vàng vào Tài" + "\nKết quả : Tài" + "\nCòn cái nịt.");
                        } else if (3 <= (x + y + z) && (x + y + z) <= 10) {
                            Item tvthang = ItemService.gI().createNewItem((short) 457);
                            tvthang.quantity = (int) Math.round(xiuThoiVang * 1.8);
                            InventoryServiceNew.gI().addItemBag(player, tvthang);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.getInstance().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + x + " " +
                                    y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + xiuThoiVang +
                                    " Thỏi vàng vào Tài" + "\nKết quả : Xỉu" + "\n\nVề bờ");
                        }
                    });
                    threadXiuTV.start();
                    break;
                case CHOOSE_LEVEL_BDKB:
                    int level = Integer.parseInt(text[0]);
                    if (level >= 1 && level <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.QUY_LAO_KAME, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_BDKB,
                                    "Con có chắc chắn muốn tới bản đồ kho báu cấp độ " + level + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, level);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
//                    BanDoKhoBauService.gI().openBanDoKhoBau(player, (byte) );
                    break;
                case NAP_THE:
                    NapThe.SendCard(player, LOAI_THE, MENH_GIA, text[0], text[1]);
                    break;
                case DOI_RUONG_DONG_VANG:
                    int slruongcandoi = Integer.parseInt(text[0]);
                    int sldongxuvangbitru = slruongcandoi * 99;
                    if (slruongcandoi > 100) {
                        Service.getInstance().sendThongBao(player, "Tối đa 100 rương 1 lần!!");
                        return;
                    }
                    if (slruongcandoi <= 0) {
                        Service.getInstance().sendThongBao(player, "Số Lượng không hợp lệ!!");
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
                        InventoryServiceNew.gI().subQuantityItemsBag(player, dongxuvang, sldongxuvangbitru);
                        Item ruongdongvang = ItemService.gI().createNewItem((short) 1230);
                        ruongdongvang.quantity = slruongcandoi;
                        InventoryServiceNew.gI().addItemBag(player, ruongdongvang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "Chúc Mừng Bạn Đổi x" + slruongcandoi + " " + ruongdongvang.template.name + " Thành Công !");
                    } else {
                        Service.getInstance().sendThongBao(player, "Không đủ Bông Hồng bạn còn thiếu " + (sldongxuvangbitru - dongxuvang.quantity) + " Đồng Xu Vàng nữa!");
                    }
                    break;
                case QUY_DOI_COIN:
                    int ratioGold = 1; // tỉ lệ đổi tv
                    int coinGold = 1; // là cái loz
                    int goldTrade = Integer.parseInt(text[0]);
                    if (goldTrade <= 0 || goldTrade >= 50000000) {
                        Service.gI().sendThongBao(player, "giới hạn");
                    } else if (player.getSession().vnd >= goldTrade * coinGold) {
                        PlayerDAO.subvnd(player, goldTrade * coinGold);
                        Item thoiVang = ItemService.gI().createNewItem((short) 861, goldTrade * 1);// x3
                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được " + goldTrade * ratioGold
                                + " " + thoiVang.template.name);
                    } else {
                        Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().vnd + " không đủ để quy "
                                + " đổi " + goldTrade + " Hồng Ngọc " + " " + "bạn cần thêm" + (player.getSession().vnd - goldTrade));
                    }
                    break;
                case QUY_DOI_HONG_NGOC:
                    int ratioGem = 4; // tỉ lệ đổi tv
                    int coinGem = 1000; // là cái loz
                    int gemTrade = Integer.parseInt(text[0]);
                    if (gemTrade <= 0 || gemTrade >= 50000000) {
                        Service.gI().sendThongBao(player, "giới hạn");
                    } else if (player.getSession().vnd >= gemTrade * coinGem) {
                        PlayerDAO.subvnd(player, gemTrade * coinGem);
                        Item thoiVang = ItemService.gI().createNewItem((short) 457, gemTrade * 4);// x4
                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được " + gemTrade * ratioGem
                                + " " + thoiVang.template.name);
                    } else {
                        Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().vnd + " không đủ để quy "
                                + " đổi " + gemTrade + " Thỏi Vàng" + " " + "bạn cần thêm" + (player.getSession().vnd - gemTrade));
                    }
                    break;
            }
        } catch (Exception e) {
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
        }
    }

    public void createForm(ISession session, int typeInput, String title, SubInput... subInputs) {
        Message msg;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createFormChangePassword(Player pl) {
        createForm(pl, CHANGE_PASSWORD, "Quên Mật Khẩu", new SubInput("Nhập mật khẩu đã quên", PASSWORD),
                new SubInput("Mật khẩu mới", PASSWORD),
                new SubInput("Nhập lại mật khẩu mới", PASSWORD));
    }

    public void createFormGiveItem(Player pl) {
        createForm(pl, GIVE_IT, "Tặng vật phẩm", new SubInput("Tên", ANY), new SubInput("Id Item", ANY), new SubInput("Số lượng", ANY));
    }

    public void createFormNapCoin(Player pl) {
        createForm(pl, NAP, "Nạp coin", new SubInput("Tên", ANY), new SubInput("Số lượng", ANY));
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

    public void createFormNapThe(Player pl, String loaiThe, String menhGia) {
        LOAI_THE = loaiThe;
        MENH_GIA = menhGia;
        createForm(pl, NAP_THE, "Nạp thẻ", new SubInput("Số Seri", ANY), new SubInput("Mã thẻ", ANY));
    }

    public void createFormQDTV(Player pl) {
        createForm(pl, QUY_DOI_COIN, "Quy đổi Hồng Ngọc tỉ lệ 1-1"
                + "\n50.000 Vnd = 50.000 Hồng ngọc "
                + "\nNạp tiền Tại: MONZY ", new SubInput("Nhập số lượng muốn đổi", NUMERIC));
    }

    public void createFormQDHN(Player pl) {
        createForm(pl, QUY_DOI_HONG_NGOC, "Quy đổi Thỏi Vàng"
                + "\nNhập 10 Có nghĩa là  10.000đ"
                + "\nTỉ Lệ Quy Đổi 10.000đ = 40 Thỏi Vàng"
                + "\nNạp tiền Tại: MONZY ", new SubInput("Nhập số lượng muốn đổi", NUMERIC));
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

    public void createFormTradeRuongDongVang(Player pl) {
        createForm(pl, DOI_RUONG_DONG_VANG, "Nhập Số Lượng Muốn Đổi", new SubInput("Số Lượng", NUMERIC));
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

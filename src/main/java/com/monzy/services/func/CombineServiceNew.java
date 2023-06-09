package com.monzy.services.func;

import com.monzy.consts.ConstNpc;
import com.monzy.models.item.Item;
import com.monzy.models.item.Item.ItemOption;
import com.monzy.models.npc.Npc;
import com.monzy.models.npc.NpcManager;
import com.monzy.models.player.Player;
import com.monzy.server.Manager;
import com.monzy.server.ServerNotify;
import com.monzy.services.InventoryServiceNew;
import com.monzy.services.ItemService;
import com.monzy.services.RewardService;
import com.monzy.services.Service;
import com.monzy.utils.Util;
import com.network.io.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CombineServiceNew {

    private static final int COST_DOI_VE_DOI_DO_HUY_DIET = 500000000;
    private static final int COST_DAP_DO_KICH_HOAT = 500000000;
    private static final int COST_DOI_MANH_KICH_HOAT = 500000000;
    private static final int COST = 500000000;
    private static final int TIME_COMBINE = 1;
    public static final byte MAX_STAR_ITEM = 8;
    public static final byte MAX_LEVEL_ITEM = 8;
    private static final byte OPEN_TAB_COMBINE = 0;
    private static final byte REOPEN_TAB_COMBINE = 1;
    private static final byte COMBINE_SUCCESS = 2;
    private static final byte COMBINE_FAIL = 3;
    private static final byte COMBINE_CHANGE_OPTION = 4;
    private static final byte COMBINE_DRAGON_BALL = 5;
    public static final byte OPEN_ITEM = 6;
    public static final int EP_SAO_TRANG_BI = 500;
    public static final int PHA_LE_HOA_TRANG_BI = 501;
    public static final int CHUYEN_HOA_TRANG_BI = 502;
    public static final int NANG_CAP_VAT_PHAM = 510;
    public static final int NANG_CAP_BONG_TAI = 511;
    public static final int MO_CHI_SO_BONG_TAI = 512;
    public static final int NHAP_NGOC_RONG = 513;
    public static final int PHAN_RA_DO_THAN_LINH = 514;
    public static final int NANG_CAP_DO_TS = 515;
    public static final int NANG_CAP_DO_HD = 520;
    public static final int NANG_CAP_KHI = 516;
    public static final int NANG_CHIEN_LINH = 517;
    public static final int DOI_CHI_SO_AN_CHIEN_LINH = 518;
    public static final int NANG_CAP_SKH_VIP = 519;
    private static final int[] GOLD_NANG_BONG_TAI = {300_000_000, 500_000_000, 1_000_000_000};
    private static final int RUBY_NANG_BONG_TAI = 1000;
    private static final int GOLD_MOCS_BONG_TAI = 500_000_000;
    private static final int RUBY_MOCS_BONG_TAI = 1000;
    private static final int RATIO_MOCS_BONG_TAI = 1000;
    private static final int RATIO_NANG_CHIEN_LINH = 20;
    private static final int GOLD_NANG_CHIEN_LINH = 1_000_000_000;
    private static final int RUBY_NANG_CHIEN_LINH = 5000;
    private static final int RATIO_DOI_CHI_SO_AN_CHIEN_LINH = 50;
    private static final int GOLD_DOI_CHI_SO_AN_CHIEN_LINH = 500_000_000;
    private static final int RUBY_DOI_CHI_SO_AN_CHIEN_LINH = 1000;
    private static final int GOLD_NANG_KHI = 500_000_000;
    private static final int RUBY_NANG_KHI = 1000;
    private final Npc baHatMit;
    private final Npc npsthiensu64;
    private final Npc khidaumoi;
    private final Npc trungLinhThu;
    private static CombineServiceNew i;

    public CombineServiceNew() {
        this.baHatMit = NpcManager.getNpc(ConstNpc.BA_HAT_MIT);
        this.npsthiensu64 = NpcManager.getNpc(ConstNpc.NPC_64);
        this.khidaumoi = NpcManager.getNpc(ConstNpc.KHI_DAU_MOI);
        this.trungLinhThu = NpcManager.getNpc(ConstNpc.TRUNG_LINH_THU);
    }

    public static CombineServiceNew gI() {
        if (i == null) {
            i = new CombineServiceNew();
        }
        return i;
    }

    /**
     * Mở tab đập đồ
     *
     * @param player
     * @param type   kiểu đập đồ
     */
    public void openTabCombine(Player player, int type) {
        player.combineNew.setTypeCombine(type);
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_TAB_COMBINE);
            msg.writer().writeUTF(getTextInfoTabCombine(type));
            msg.writer().writeUTF(getTextTopTabCombine(type));
            if (player.iDMark.getNpcChose() != null) {
                msg.writer().writeShort(player.iDMark.getNpcChose().tempId);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiển thị thông tin đập đồ
     *
     * @param player
     */
    public void showInfoCombine(Player player, int[] index) {
        player.combineNew.clearItemCombine();
        if (index.length > 0) {
            for (int i = 0; i < index.length; i++) {
                player.combineNew.itemsCombine.add(player.inventory.itemsBag.get(index[i]));
            }
        }
        switch (player.combineNew.typeCombine) {
            case NANG_CHIEN_LINH:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item linhthu = null;
                    Item ttt = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.type == 72) {
                            linhthu = item;
                        } else if (item.template.id == 2031) {
                            ttt = item;
                        }
                    }
                    if (linhthu != null && ttt != null) {
                        player.combineNew.goldCombine = GOLD_NANG_CHIEN_LINH;
                        player.combineNew.rubyCombine = RUBY_NANG_CHIEN_LINH;
                        player.combineNew.ratioCombine = RATIO_NANG_CHIEN_LINH;
                        String npcSay = "Pet: " + linhthu.template.name + " \n|2|";
                        for (Item.ItemOption io : linhthu.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (ttt.quantity >= 99) {
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                if (player.combineNew.rubyCombine <= player.inventory.ruby) {
                                    npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                    trungLinhThu.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                            "Nâng cấp\ncần " + player.combineNew.rubyCombine + " hồng ngọc");
                                } else {
                                    npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.rubyCombine - player.inventory.ruby) + " hồng ngọc";
                                    trungLinhThu.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                                }
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                                trungLinhThu.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(99 - ttt.quantity) + " Thăng tinh thạch";
                            trungLinhThu.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.trungLinhThu.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Linh Thú và x99 Thăng tinh thạch", "Đóng");
                    }
                } else {
                    this.trungLinhThu.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Linh Thú và x99 Thăng tinh thạch", "Đóng");
                }
                break;
            case NANG_CAP_KHI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item ctkhi = null;
                    Item dns = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (checkctkhi(item)) {
                            ctkhi = item;
                        } else if (item.template.id == 674) {
                            dns = item;
                        }
                    }
                    if (ctkhi != null && dns != null) {
                        int lvkhi = lvkhi(ctkhi);
                        int countdns = getcountdnsnangkhi(lvkhi);
                        player.combineNew.goldCombine = getGoldnangkhi(lvkhi);
                        player.combineNew.rubyCombine = getRubydnangkhi(lvkhi);
                        player.combineNew.ratioCombine = getRatioNangkhi(lvkhi);
                        String npcSay = "Cải trang khỉ Cấp: " + lvkhi + " \n|2|";
                        for (Item.ItemOption io : ctkhi.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (dns.quantity >= countdns) {
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                if (player.combineNew.rubyCombine <= player.inventory.ruby) {
                                    npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                    khidaumoi.createOtherMenu(player, ConstNpc.MENU_NANG_KHI, npcSay,
                                            "Nâng cấp\ncần " + player.combineNew.rubyCombine + " hồng ngọc");
                                } else {
                                    npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.rubyCombine - player.inventory.ruby) + " hồng ngọc";
                                    khidaumoi.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                                }
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                                khidaumoi.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(countdns - dns.quantity) + " Đá Ngũ Sắc";
                            khidaumoi.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.khidaumoi.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Cải trang khỉ Cấp 1-7 và " + (10 + 10 * lvkhi(ctkhi)) + " Đá Ngũ Sắc", "Đóng");
                    }
                } else {
                    this.khidaumoi.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Cải trang khỉ Cấp 1-7 và 10 + 10*lvkhi Đá Ngũ Sắc", "Đóng");
                }
                break;
            case NANG_CAP_BONG_TAI:
                if (player.combineNew.itemsCombine.size() != 2) {
                    createErrorMessage(player, "Cần 1 Bông tai Porata cấp 1 hoặc 2 hoặc 3 và Mảnh vỡ bông tai");
                    return;
                }
                Item bongTaiNC = null;
                Item manhVoBTNC = null;
                for (Item item : player.combineNew.itemsCombine) {
                    if (checkBongTai(item)) {
                        bongTaiNC = item;
                    } else if (item.template.id == 933) {
                        manhVoBTNC = item;
                    }
                }
                if (bongTaiNC == null || manhVoBTNC == null) {
                    createErrorMessage(player, "Cần 1 Bông tai Porata cấp 1 hoặc 2 hoặc 3 và Mảnh vỡ bông tai");
                    return;
                }
                int levelBongTaiNC = levelBongTai(bongTaiNC);
                int getCountMVBT = getCountMVBTNangBT(levelBongTaiNC);
                player.combineNew.goldCombine = getGoldNangBT(levelBongTaiNC);
                player.combineNew.rubyCombine = getRubyNangBT(levelBongTaiNC);
                player.combineNew.ratioCombine = getRatioNangBT(levelBongTaiNC);
                if (manhVoBTNC.quantity < getCountMVBT) {
                    createErrorMessage(player, "Còn thiếu " + Util.numberToMoney(getCountMVBT - manhVoBTNC.quantity) + " Mảnh vỡ bông tai");
                    return;
                }
                if (player.combineNew.goldCombine > player.inventory.gold) {
                    createErrorMessage(player, "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng");
                    return;
                }
                if (player.combineNew.rubyCombine > player.inventory.ruby) {
                    createErrorMessage(player, "Còn thiếu " + Util.numberToMoney(player.combineNew.rubyCombine - player.inventory.ruby) + " hồng ngọc");
                    return;
                }
                String message = createSuccessMessage(bongTaiNC, player);
                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, message,
                        "Nâng cấp\ncần " + player.combineNew.rubyCombine + " hồng ngọc");
                break;
            case MO_CHI_SO_BONG_TAI:
                if (player.combineNew.itemsCombine.size() != 3) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 2, 3 hoặc 4\nMảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                    break;
                }
                Item bongTai = null;
                Item manhHon = null;
                Item daXanhLam = null;
                for (Item item : player.combineNew.itemsCombine) {
                    if (checkBongTaiMCS(item)) {
                        bongTai = item;
                    } else if (item.template.id == 934) {
                        manhHon = item;
                    } else if (item.template.id == 935) {
                        daXanhLam = item;
                    }
                }
                int levelBongTai = levelBongTai(bongTai);
                if (bongTai == null || manhHon == null || daXanhLam == null || manhHon.quantity < getCountMVBTNangBT(levelBongTai)) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 2, 3 hoặc 4 " + getCountMVBTNangBT(levelBongTai) + " Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                    break;
                }
                player.combineNew.goldCombine = getGoldMCSABT(levelBongTai);
                player.combineNew.rubyCombine = getRubyMSCABT(levelBongTai);
                player.combineNew.ratioCombine = getRatioMCSABT(levelBongTai);
                String npcSayMSCABT = bongTai.template.name + " \n";
                npcSayMSCABT += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                npcSayMSCABT += "|1|Cần " + getCountMVBTNangBT(levelBongTai) + " Mảnh hồn bông tai và 1 Đá xanh lam\n";
                if (player.combineNew.goldCombine <= player.inventory.gold) {
                    if (player.combineNew.rubyCombine <= player.inventory.ruby) {
                        npcSayMSCABT += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSayMSCABT,
                                "Nâng cấp\ncần " + player.combineNew.rubyCombine + " hồng ngọc");
                    } else {
                        npcSayMSCABT += "Còn thiếu " + Util.numberToMoney(player.combineNew.rubyCombine - player.inventory.ruby) + " hồng ngọc";
                        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSayMSCABT, "Đóng");
                    }
                } else {
                    npcSayMSCABT += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSayMSCABT, "Đóng");
                }
                break;
            case DOI_CHI_SO_AN_CHIEN_LINH:
                if (player.combineNew.itemsCombine.size() != 2) {
                    trungLinhThu.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Chiến Linh, X99 Đá ma thuật", "Đóng");
                    break;
                }
                Item chienLinh = null;
                Item daMaThuat = null;
                for (Item item : player.combineNew.itemsCombine) {
                    if (item.template.id >= 2038 && item.template.id <= 2042) {
                        chienLinh = item;
                    } else if (item.template.id == 2030) {
                        daMaThuat = item;
                    }
                }
                if (chienLinh == null || daMaThuat == null || daMaThuat.quantity < 99) {
                    trungLinhThu.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Chiến Linh, X99 Đá ma thuật", "Đóng");
                    break;
                }
                player.combineNew.goldCombine = GOLD_DOI_CHI_SO_AN_CHIEN_LINH;
                player.combineNew.rubyCombine = RUBY_DOI_CHI_SO_AN_CHIEN_LINH;
                player.combineNew.ratioCombine = RATIO_DOI_CHI_SO_AN_CHIEN_LINH;
                String npcSayChienLinh = "Chiến Linh\n|2|";
                for (Item.ItemOption io : chienLinh.itemOptions) {
                    npcSayChienLinh += io.getOptionString() + "\n";
                }
                npcSayChienLinh += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                if (player.combineNew.goldCombine > player.inventory.gold) {
                    npcSayChienLinh += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                    trungLinhThu.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSayChienLinh, "Đóng");
                    break;
                }
                if (player.combineNew.rubyCombine > player.inventory.ruby) {
                    npcSayChienLinh += "Còn thiếu " + Util.numberToMoney(player.combineNew.rubyCombine - player.inventory.ruby) + " hồng ngọc";
                    trungLinhThu.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSayChienLinh, "Đóng");
                    break;
                }
                npcSayChienLinh += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                trungLinhThu.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSayChienLinh,
                        "Nâng cấp\ncần " + player.combineNew.rubyCombine + " hồng ngọc");
                break;
            case EP_SAO_TRANG_BI:
                if (player.combineNew.itemsCombine.size() != 2) {
                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                    return;
                }
                Item trangBi = null;
                Item daPhaLe = null;
                for (Item item : player.combineNew.itemsCombine) {
                    if (isTrangBiPhaLeHoa(item)) {
                        trangBi = item;
                    } else if (isDaPhaLe(item)) {
                        daPhaLe = item;
                    }
                }
                if (trangBi == null || daPhaLe == null) {
                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                    return;
                }
                int star = 0; //sao pha lê đã ép
                int starEmpty = 0; //lỗ sao pha lê
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }
                if (star >= starEmpty) {
                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                    return;
                }
                player.combineNew.gemCombine = getGemEpSao(star);
                String npcSayEpSao = trangBi.template.name + "\n|2|";
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id != 102) {
                        npcSayEpSao += io.getOptionString() + "\n";
                    }
                }
                if (daPhaLe.template.type == 30) {
                    for (Item.ItemOption io : daPhaLe.itemOptions) {
                        npcSayEpSao += "|7|" + io.getOptionString() + "\n";
                    }
                } else {
                    npcSayEpSao += "|7|" + ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name.replaceAll("#", getParamDaPhaLe(daPhaLe) + "") + "\n";
                }
                npcSayEpSao += "|1|Cần " + Util.numberToMoney(player.combineNew.gemCombine) + " ngọc";
                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSayEpSao,
                        "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                break;
            case PHA_LE_HOA_TRANG_BI:
                if (player.combineNew.itemsCombine.size() != 1) {
                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa", "Đóng");
                    return;
                }
                Item itemPhaLeHoa = player.combineNew.itemsCombine.get(0);
                if (!isTrangBiPhaLeHoa(itemPhaLeHoa)) {
                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ", "Đóng");
                    return;
                }
                int starEpSao = 0;
                for (Item.ItemOption io : itemPhaLeHoa.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        starEpSao = io.param;
                        break;
                    }
                }
                if (starEpSao >= MAX_STAR_ITEM) {
                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                    return;
                }
                player.combineNew.goldCombine = getGoldPhaLeHoa(starEpSao);
                player.combineNew.gemCombine = getGemPhaLeHoa(starEpSao);
                player.combineNew.ratioCombine = getRatioPhaLeHoa(starEpSao);
                String npcSayPhaLeHoa = itemPhaLeHoa.template.name + "\n|2|";
                for (Item.ItemOption io : itemPhaLeHoa.itemOptions) {
                    if (io.optionTemplate.id != 102) {
                        npcSayPhaLeHoa += io.getOptionString() + "\n";
                    }
                }
                npcSayPhaLeHoa += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                if (player.combineNew.goldCombine <= player.inventory.gold) {
                    npcSayPhaLeHoa += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                    baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSayPhaLeHoa,
                            "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                } else {
                    npcSayPhaLeHoa += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSayPhaLeHoa, "Đóng");
                }
                break;
            case NHAP_NGOC_RONG:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                    break;
                }
                if (player.combineNew.itemsCombine.size() != 1) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                    break;
                }
                Item itemNhapNR = player.combineNew.itemsCombine.get(0);
                if (itemNhapNR == null || !itemNhapNR.isNotNullItem() || itemNhapNR.template.id <= 14 || itemNhapNR.template.id > 20 || itemNhapNR.quantity < 7) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                    break;
                }
                String npcSayNhapNR = "|2|Con có muốn biến 7 " + itemNhapNR.template.name + " thành\n"
                        + "1 viên " + ItemService.gI().getTemplate((short) (itemNhapNR.template.id - 1)).name + "\n"
                        + "|7|Cần 7 " + itemNhapNR.template.name;
                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSayNhapNR, "Làm phép", "Từ chối");
                break;
            case NANG_CAP_VAT_PHAM:
                if (player.combineNew.itemsCombine.size() < 2 || player.combineNew.itemsCombine.size() > 3) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                    break;
                }
                if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() < 1) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                    break;
                }
                if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() < 1) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá nâng cấp", "Đóng");
                    break;
                }
                if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                    break;
                }
                Item itemDo = null;
                Item itemDNC = null;
                Item itemDBV = null;
                for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                    Item currentItem = player.combineNew.itemsCombine.get(j);
                    if (currentItem.isNotNullItem()) {
                        if (player.combineNew.itemsCombine.size() == 3 && currentItem.template.id == 987) {
                            itemDBV = currentItem;
                            continue;
                        }
                        if (currentItem.template.type < 5) {
                            itemDo = currentItem;
                        } else {
                            itemDNC = currentItem;
                        }
                    }
                }
                if (!isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                    break;
                }
                int level = 0;
                for (Item.ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        break;
                    }
                }
                if (level >= MAX_LEVEL_ITEM) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                    break;
                }
                player.combineNew.goldCombine = getGoldNangCapDo(level);
                player.combineNew.ratioCombine = (float) getTileNangCapDo(level);
                player.combineNew.countDaNangCap = getCountDaNangCapDo(level);
                player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                String npcSayNCVP = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                for (Item.ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id != 72) {
                        npcSayNCVP += io.getOptionString() + "\n";
                    }
                }
                String option = null;
                int param = 0;
                for (Item.ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 47
                            || io.optionTemplate.id == 6
                            || io.optionTemplate.id == 0
                            || io.optionTemplate.id == 7
                            || io.optionTemplate.id == 14
                            || io.optionTemplate.id == 22
                            || io.optionTemplate.id == 23) {
                        option = io.optionTemplate.name;
                        param = io.param + (io.param * 10 / 100);
                        break;
                    }
                }
                npcSayNCVP += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                        + option.replaceAll("#", String.valueOf(param))
                        + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                        + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                        + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                        + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                        + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                if ((level == 2 || level == 4 || level == 6) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                    npcSayNCVP += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                }
                if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            npcSayNCVP, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                } else if (player.combineNew.goldCombine > player.inventory.gold) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            npcSayNCVP, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            npcSayNCVP, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSayNCVP, "Nâng cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                }
                break;
            case PHAN_RA_DO_THAN_LINH:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con hãy đưa ta đồ thần linh để phân rã", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 1) {
                    List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(562, 564, 566));
                    int couponAdd = 0;
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (item.isNotNullItem()) {
                        if (item.template.id >= 555 && item.template.id <= 567) {
                            couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 2 : item.template.id == 561 ? 3 : 1;
                        }
                    }
                    if (couponAdd == 0) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta chỉ có thể phân rã đồ thần linh thôi", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Sau khi phân rải vật phẩm\n|7|"
                            + "Bạn sẽ nhận được : " + couponAdd + " Điểm\n"
                            + (500000000 > player.inventory.gold ? "|7|" : "|1|")
                            + "Cần " + Util.numberToMoney(500000000) + " vàng";
                    if (player.inventory.gold < 500000000) {
                        this.baHatMit.npcChat(player, "Hết tiền rồi\nẢo ít thôi con");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_PHAN_RA_DO_THAN_LINH,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(500000000) + " vàng", "Từ chối");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta chỉ có thể phân rã 1 lần 1 món đồ thần linh", "Đóng");
                }
                break;
            case NANG_CAP_DO_TS:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 2 món Hủy Diệt bất kì và 1 món Thần Linh cùng loại", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thần linh", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() < 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ hủy diệt", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu mảnh thiên sứ", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isManhTS).findFirst().get().typeNameManh() + " thiên sứ tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(COST) + " vàng";
                    if (player.inventory.gold < COST) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_NANG_CAP_DO_TS,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_CAP_DO_HD:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 3 món Thần Linh bất kì ngươi sẽ nhận được đồ Hủy Diệt cùng loại", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() < 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thần linh", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isDTL).findFirst().get().typeName() + " hủy diệt tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(COST) + " vàng";
                    if (player.inventory.gold < COST) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_NANG_CAP_DO_HD,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_CAP_SKH_VIP:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món thiên sứ", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 1) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTS()).count() != 1) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thiên sứ", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isDTS).findFirst().get().typeName() + " kích hoạt VIP tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(COST) + " vàng";
                    if (player.inventory.gold < COST) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.MENU_NANG_DOI_SKH_VIP,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                } else {
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                    return;
                }
                break;
        }
    }

    /**
     * Bắt đầu đập đồ - điều hướng từng loại đập đồ
     *
     * @param player
     */
    public void startCombine(Player player) {
        switch (player.combineNew.typeCombine) {
            case EP_SAO_TRANG_BI:
                epSaoTrangBi(player);
                break;
            case PHA_LE_HOA_TRANG_BI:
                phaLeHoaTrangBi(player);
                break;
            case CHUYEN_HOA_TRANG_BI:
                break;
            case NHAP_NGOC_RONG:
                nhapNgocRong(player);
                break;
            case PHAN_RA_DO_THAN_LINH:
                phanRaDoThanLinh(player);
                break;
            case NANG_CAP_DO_TS:
                openDTS(player);
                break;
            case NANG_CAP_DO_HD:
                openDHD(player);
                break;
            case NANG_CAP_SKH_VIP:
                openSKHVIP(player);
                break;
            case NANG_CAP_VAT_PHAM:
                nangCapVatPham(player);
                break;
            case NANG_CAP_BONG_TAI:
                nangCapBongTai(player);
                break;
            case MO_CHI_SO_BONG_TAI:
                moChiSoBongTai(player);
                break;
            case NANG_CAP_KHI:
                nangCapKhi(player);
                break;
            case DOI_CHI_SO_AN_CHIEN_LINH:
                doiChiSoAnChienLinh(player);
                break;
            case NANG_CHIEN_LINH:
                nangCapChienLinh(player);
                break;
        }
        player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
        player.combineNew.clearParamCombine();
        player.combineNew.lastTimeCombine = System.currentTimeMillis();
    }

    public void GetTrangBiKichHoathuydiet(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{127, 128}, {130, 132}, {133, 135}};
        int[][] paramNormal = {{139, 140}, {142, 144}, {136, 138}};
        int[][] optionVIP = {{129}, {131}, {134}};
        int[][] paramVIP = {{141}, {143}, {137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(100);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(1500, 2000)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(22, Util.nextInt(100, 150)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(9000, 11000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(23, Util.nextInt(90, 150)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(15, 20)));
        }
        if (randomSkh <= 20) {//tile ra do kich hoat
            if (randomSkh <= 5) { // tile ra option vip
                item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            } else {//
                item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            }
        }
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void GetTrangBiKichHoatthiensu(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{127, 128}, {130, 132}, {133, 135}};
        int[][] paramNormal = {{139, 140}, {142, 144}, {136, 138}};
        int[][] optionVIP = {{129}, {131}, {134}};
        int[][] paramVIP = {{141}, {143}, {137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(100);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2000, 2500)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(22, Util.nextInt(150, 200)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(18000, 20000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(23, Util.nextInt(150, 200)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(20, 25)));
        }
        if (randomSkh <= 20) {//tile ra do kich hoat
            if (randomSkh <= 5) { // tile ra option vip
                item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            } else {//
                item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            }
        }
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void layChiSoCTKhi(Player player, Item ctkhi, int lvkhi) {
        ctkhi.itemOptions.add(new ItemOption(50, 15 + 5 * lvkhi));//sd
        ctkhi.itemOptions.add(new ItemOption(77, 15 + 5 * lvkhi));//hp
        ctkhi.itemOptions.add(new ItemOption(103, 15 + 5 * lvkhi));//ki
        ctkhi.itemOptions.add(new ItemOption(14, 10 + 2 * lvkhi));//cm
        ctkhi.itemOptions.add(new ItemOption(5, 10 + 2 * lvkhi));//sd cm
        ctkhi.itemOptions.add(new ItemOption(106, 0));
        ctkhi.itemOptions.add(new ItemOption(34, 0));
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void chiSoChienLinh(Player player, Item chienLinh) {
        chienLinh.itemOptions.add(new ItemOption(50, Util.nextInt(20, 20)));//sd
        chienLinh.itemOptions.add(new ItemOption(77, Util.nextInt(20, 20)));//hp
        chienLinh.itemOptions.add(new ItemOption(103, Util.nextInt(20, 20)));//ki
        randomCSA(chienLinh);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    private void randomCSA(Item chienLinh) {
        chienLinh.itemOptions.add(new Item.ItemOption(212, 0));
        int rdUp = Util.nextInt(0, 7);
        switch (rdUp) {
            case 0:
                chienLinh.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 15)));
                break;
            case 1:
                chienLinh.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 15)));
                break;
            case 2:
                chienLinh.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 15)));
                break;
            case 3:
                chienLinh.itemOptions.add(new Item.ItemOption(108, Util.nextInt(5, 15)));
                break;
            case 4:
                chienLinh.itemOptions.add(new Item.ItemOption(94, Util.nextInt(5, 15)));
                break;
            case 5:
                chienLinh.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 15)));
                break;
            case 6:
                chienLinh.itemOptions.add(new Item.ItemOption(80, Util.nextInt(5, 15)));
                break;
            case 7:
                chienLinh.itemOptions.add(new Item.ItemOption(81, Util.nextInt(5, 15)));
                break;
            default:
                break;
        }
    }

    private void doiKiemThan(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item keo = null, luoiKiem = null, chuoiKiem = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 2015) {
                    keo = it;
                } else if (it.template.id == 2016) {
                    chuoiKiem = it;
                } else if (it.template.id == 2017) {
                    luoiKiem = it;
                }
            }
            if (keo != null && keo.quantity >= 99 && luoiKiem != null && chuoiKiem != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2018);
                    item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(9, 15)));
                    item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(8, 15)));
                    item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(8, 15)));
                    if (Util.isTrue(80, 100)) {
                        item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 15)));
                    }
                    InventoryServiceNew.gI().addItemBag(player, item);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, keo, 99);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, luoiKiem, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, chuoiKiem, 1);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiChuoiKiem(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item manhNhua = player.combineNew.itemsCombine.get(0);
            if (manhNhua.template.id == 2014 && manhNhua.quantity >= 99) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2016);
                    InventoryServiceNew.gI().addItemBag(player, item);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhNhua, 99);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiLuoiKiem(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item manhSat = player.combineNew.itemsCombine.get(0);
            if (manhSat.template.id == 2013 && manhSat.quantity >= 99) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2017);
                    InventoryServiceNew.gI().addItemBag(player, item);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhSat, 99);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiManhKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 2 || player.combineNew.itemsCombine.size() == 3) {
            Item nr1s = null, doThan = null, buaBaoVe = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 14) {
                    nr1s = it;
                } else if (it.template.id == 2010) {
                    buaBaoVe = it;
                } else if (it.template.id >= 555 && it.template.id <= 567) {
                    doThan = it;
                }
            }
            if (nr1s != null && doThan != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DOI_MANH_KICH_HOAT) {
                    player.inventory.gold -= COST_DOI_MANH_KICH_HOAT;
                    int tiLe = buaBaoVe != null ? 100 : 50;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) 2009);
                        item.itemOptions.add(new Item.ItemOption(30, 0));
                        InventoryServiceNew.gI().addItemBag(player, item);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, nr1s, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, doThan, 1);
                    if (buaBaoVe != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, buaBaoVe, 1);
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị thần linh và 1 viên ngọc rồng 1 sao", "Đóng");
            }
        }
    }

    private void phanRaDoThanLinh(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            player.inventory.gold -= 500000000;
            List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(562, 564, 566));
            Item item = player.combineNew.itemsCombine.get(0);
            int couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 2 : item.template.id == 561 ? 3 : 1;
            sendEffectSuccessCombine(player);
            player.inventory.coupon += couponAdd;
            this.baHatMit.npcChat(player, "Con đã nhận được " + couponAdd + " điểm");
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            player.combineNew.itemsCombine.clear();
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            reOpenItemCombine(player);
        }
    }

    public void openDTS(Player player) {
        //check sl đồ tl, đồ hd
        // new update 2 mon huy diet + 1 mon than linh(skh theo style) +  5 manh bat ki
        if (player.combineNew.itemsCombine.size() != 4) {
            Service.gI().sendThongBao(player, "Thiếu đồ");
            return;
        }
        if (player.inventory.gold < COST) {
            Service.gI().sendThongBao(player, "Ảo ít thôi con...");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            return;
        }
        Item itemTL = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).findFirst().get();
        List<Item> itemHDs = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).collect(Collectors.toList());
        Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 5).findFirst().get();
        player.inventory.gold -= COST;
        sendEffectSuccessCombine(player);
        Item itemTS = ItemService.gI().randomCSDTS(Manager.IDS_DO_THIEN_SU[itemTL.template.gender > 2 ? player.gender : itemTL.template.gender][itemManh.typeIdManh()], itemTL.template.gender);
        InventoryServiceNew.gI().addItemBag(player, itemTS);
        InventoryServiceNew.gI().subQuantityItemsBag(player, itemTL, 1);
        InventoryServiceNew.gI().subQuantityItemsBag(player, itemManh, 5);
        itemHDs.forEach(item -> InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1));
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        Service.gI().sendThongBao(player, "Bạn đã nhận được " + itemTS.template.name);
        player.combineNew.itemsCombine.clear();
        reOpenItemCombine(player);
    }

    public void openDHD(Player player) {
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() != 3) {
            Service.gI().sendThongBao(player, "Thiếu đồ thần linh!");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < COST) {
                Service.gI().sendThongBao(player, "Con cần thêm vàng để đổi...");
                return;
            }
            player.inventory.gold -= COST;
            Item firstItemCombine = player.combineNew.itemsCombine.stream().filter(Item::isDTL).findFirst().get();
            List<Item> itemsCombine = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).collect(Collectors.toList());
            CombineServiceNew.gI().sendEffectOpenItem(player, firstItemCombine.template.iconID, firstItemCombine.template.iconID);
            short itemId;
            if (firstItemCombine.template.gender == 3 || firstItemCombine.template.type == 4) {
                itemId = Manager.ID_RADAR_HD;
            } else {
                itemId = Manager.IDS_DO_HUY_DIET[firstItemCombine.template.gender][firstItemCombine.template.type];
            }
            Item itemHD = null;
            if (new Item(itemId).isDHD()) {
                itemHD = ItemService.gI().randomCSDHD(itemId, player.gender);
            }
            InventoryServiceNew.gI().addItemBag(player, itemHD);
            InventoryServiceNew.gI().subQuantityItemsBag(player, firstItemCombine, 1);
            itemsCombine.forEach(i -> InventoryServiceNew.gI().subQuantityItemsBag(player, i, 1));
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void openSKHVIP(Player player) {
        if (player.combineNew.itemsCombine.size() != 1) {
            Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTS()).count() != 1) {
            Service.gI().sendThongBao(player, "Thiếu đồ thiên sứ");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < 1) {
                Service.gI().sendThongBao(player, "Con cần thêm vàng để đổi...");
                return;
            }
            player.inventory.gold -= COST;
            Item itemTS = player.combineNew.itemsCombine.stream().filter(Item::isDTS).findFirst().get();
            List<Item> itemSKH = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).collect(Collectors.toList());
            CombineServiceNew.gI().sendEffectOpenItem(player, itemTS.template.iconID, itemTS.template.iconID);
            short itemId;
            if (itemTS.template.gender == 3 || itemTS.template.type == 4) {
                itemId = Manager.IDS_RADAR[Util.nextInt(8, 11)];
            } else {
                itemId = Manager.IDS_TRANG_BI_SHOP[itemTS.template.gender][itemTS.template.type][Util.nextInt(8, 11)];
            }
            int skhId = ItemService.gI().randomSKHId(itemTS.template.gender);
            Item item = ItemService.gI().newItemSKH(itemId, skhId);
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTS, 1);
            itemSKH.forEach(i -> InventoryServiceNew.gI().subQuantityItemsBag(player, i, 1));
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    private void dapDoKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 1 || player.combineNew.itemsCombine.size() == 2) {
            Item dhd = null, dtl = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id >= 650 && item.template.id <= 662) {
                        dhd = item;
                    } else if (item.template.id >= 555 && item.template.id <= 567) {
                        dtl = item;
                    }
                }
            }
            if (dhd != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 //check chỗ trống hành trang
                        && player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                    player.inventory.gold -= COST_DAP_DO_KICH_HOAT;
                    int tiLe = dtl != null ? 100 : 50;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) getTempIdItemC0(dhd.template.gender, dhd.template.type));
                        RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                        RewardService.gI().initActivationOption(item.template.gender < 3 ? item.template.gender : player.gender, item.template.type, item.itemOptions);
                        InventoryServiceNew.gI().addItemBag(player, item);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, dhd, 1);
                    if (dtl != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, dtl, 1);
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiVeHuyDiet(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item item = player.combineNew.itemsCombine.get(0);
            if (item.isNotNullItem() && item.template.id >= 555 && item.template.id <= 567) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DOI_VE_DOI_DO_HUY_DIET) {
                    player.inventory.gold -= COST_DOI_VE_DOI_DO_HUY_DIET;
                    Item ticket = ItemService.gI().createNewItem((short) (2001 + item.template.type));
                    ticket.itemOptions.add(new Item.ItemOption(30, 0));
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                    InventoryServiceNew.gI().addItemBag(player, ticket);
                    sendEffectOpenItem(player, item.template.iconID, ticket.template.iconID);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void nangCapChienLinh(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int ruby = player.combineNew.rubyCombine;
            if (player.inventory.ruby < ruby) {
                Service.gI().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
                return;
            }
            Item linhthu = null;
            Item ttt = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.type == 72) {
                    linhthu = item;
                } else if (item.template.id == 2031) {
                    ttt = item;
                }
            }
            if (linhthu != null && ttt != null) {
                if (ttt.quantity < 99) {
                    Service.gI().sendThongBao(player, "Không đủ thăng tinh thạch");
                    return;
                }
                player.inventory.gold -= gold;
                player.inventory.ruby -= ruby;
                InventoryServiceNew.gI().subQuantityItemsBag(player, ttt, 99);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    short[] chienlinh = {2038, 2039, 2040, 2041, 2042};
                    linhthu.template = ItemService.gI().getTemplate(chienlinh[Util.nextInt(0, 4)]);
                    linhthu.itemOptions.clear();
                    chiSoChienLinh(player, linhthu);
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapKhi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int ruby = player.combineNew.rubyCombine;
            if (player.inventory.ruby < ruby) {
                Service.gI().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
                return;
            }
            Item ctkhi = null;
            Item dns = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (checkctkhi(item)) {
                    ctkhi = item;
                } else if (item.template.id == 674) {
                    dns = item;
                }
            }
            if (ctkhi != null && dns != null) {
                int lvkhi = lvkhi(ctkhi);
                int countdns = getcountdnsnangkhi(lvkhi);
                if (countdns > dns.quantity) {
                    Service.gI().sendThongBao(player, "Không đủ đá ngũ sắc");
                    return;
                }
                player.inventory.gold -= gold;
                player.inventory.ruby -= ruby;
                InventoryServiceNew.gI().subQuantityItemsBag(player, dns, countdns);
                if (Util.isTrue(player.combineNew.ratioCombine, 200)) {
                    short idctkhisaunc = getidctkhisaukhilencap(lvkhi);
                    ctkhi.template = ItemService.gI().getTemplate(idctkhisaunc);
                    ctkhi.itemOptions.clear();
                    ctkhi.itemOptions.add(new Item.ItemOption(72, lvkhi + 1));
                    layChiSoCTKhi(player, ctkhi, lvkhi);
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() != 2) {
            createErrorMessage(player, "Cần 1 Bông tai Porata cấp 1 hoặc 2 hoặc 3 và Mảnh vỡ bông tai");
            return;
        }
        int gold = player.combineNew.goldCombine;
        if (player.inventory.gold < gold) {
            Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
            return;
        }
        int ruby = player.combineNew.rubyCombine;
        if (player.inventory.ruby < ruby) {
            Service.gI().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
            return;
        }
        Item bongTaiNC = null;
        Item manhVoBTNC = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (checkBongTai(item)) {
                bongTaiNC = item;
            } else if (item.template.id == 933) {
                manhVoBTNC = item;
            }
        }
        if (bongTaiNC == null || manhVoBTNC == null) {
            createErrorMessage(player, "Cần 1 Bông tai Porata cấp 1 hoặc 2 hoặc 3 và Mảnh vỡ bông tai.");
            return;
        }
        int levelBongTai = levelBongTai(bongTaiNC);
        int getCountMVBT = getCountMVBTNangBT(levelBongTai);
        if (getCountMVBT > manhVoBTNC.quantity) {
            Service.gI().sendThongBao(player, "Không đủ Mảnh vỡ bông tai");
            return;
        }
        player.inventory.gold -= gold;
        player.inventory.ruby -= ruby;
        InventoryServiceNew.gI().subQuantityItemsBag(player, manhVoBTNC, getCountMVBT);
        if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
            bongTaiNC.template = ItemService.gI().getTemplate(getIDNewBongTai(levelBongTai));
            sendEffectSuccessCombine(player);
        } else {
            sendEffectFailCombine(player);
        }
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        reOpenItemCombine(player);
    }

    private void moChiSoBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() != 3) {
            return;
        }
        int gold = player.combineNew.goldCombine;
        int ruby = player.combineNew.rubyCombine;
        if (player.inventory.gold < gold) {
            Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
            return;
        }
        if (player.inventory.ruby < ruby) {
            Service.gI().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
            return;
        }
        Item bongTai = null;
        Item manhHon = null;
        Item daXanhLam = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (checkBongTaiMCS(item)) {
                bongTai = item;
            } else if (item.template.id == 934) {
                manhHon = item;
            } else if (item.template.id == 935) {
                daXanhLam = item;
            }
        }
        if (bongTai != null && daXanhLam != null && manhHon != null && manhHon.quantity >= getCountMHBTMCSABT(levelBongTai(bongTai))) {
            player.inventory.gold -= gold;
            player.inventory.ruby -= ruby;
            InventoryServiceNew.gI().subQuantityItemsBag(player, manhHon, getCountMHBTMCSABT(levelBongTai(bongTai)));
            InventoryServiceNew.gI().subQuantityItemsBag(player, daXanhLam, 1);
            if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                bongTai.itemOptions.clear();
                bongTai.itemOptions.add(new Item.ItemOption(Stream.of(50, 77, 103, 108, 94, 14, 80, 81).skip((long) (8 * Math.random())).findFirst().get(), Util.nextInt(5, levelBongTai(bongTai) * 5)));
                sendEffectSuccessCombine(player);
            } else {
                sendEffectFailCombine(player);
            }
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            reOpenItemCombine(player);
        }
    }

    private void doiChiSoAnChienLinh(Player player) {
        if (player.combineNew.itemsCombine.size() != 2) {
            return;
        }
        int gold = player.combineNew.goldCombine;
        if (player.inventory.gold < gold) {
            Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
            return;
        }
        int ruby = player.combineNew.rubyCombine;
        if (player.inventory.ruby < ruby) {
            Service.gI().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
            return;
        }
        Item chienLinh = null;
        Item daMaThuat = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.template.id >= 2038 && item.template.id <= 2042) {
                chienLinh = item;
            } else if (item.template.id == 2030) {
                daMaThuat = item;
            }
        }
        if (chienLinh == null || daMaThuat.quantity < 99) {
            return;
        }
        player.inventory.gold -= gold;
        player.inventory.ruby -= ruby;
        InventoryServiceNew.gI().subQuantityItemsBag(player, daMaThuat, 99);
        if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
            chienLinh.itemOptions.remove(chienLinh.itemOptions.size() - 2);
            chienLinh.itemOptions.remove(chienLinh.itemOptions.size() - 1);
            randomCSA(chienLinh);
            sendEffectSuccessCombine(player);
        } else {
            sendEffectFailCombine(player);
        }
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        reOpenItemCombine(player);
    }

    private void epSaoTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item trangBi = null;
            Item daPhaLe = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isTrangBiPhaLeHoa(item)) {
                    trangBi = item;
                } else if (isDaPhaLe(item)) {
                    daPhaLe = item;
                }
            }
            int star = 0; //sao pha lê đã ép
            int starEmpty = 0; //lỗ sao pha lê
            if (trangBi != null && daPhaLe != null) {
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }
                if (star < starEmpty) {
                    player.inventory.gem -= gem;
                    int optionId = getOptionDaPhaLe(daPhaLe);
                    int param = getParamDaPhaLe(daPhaLe);
                    Item.ItemOption option = null;
                    for (Item.ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += param;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(optionId, param));
                    }
                    if (optionStar != null) {
                        optionStar.param++;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(102, 1));
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, daPhaLe, 1);
                    sendEffectSuccessCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void phaLeHoaTrangBi(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            } else if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                int star = 0;
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.gem -= gem;
                    byte ratio = (optionStar != null && optionStar.param > 4) ? (byte) 2 : 1;
                    if (Util.isTrue(player.combineNew.ratioCombine, 100 * ratio)) {
                        if (optionStar == null) {
                            item.itemOptions.add(new Item.ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }
                        sendEffectSuccessCombine(player);
                        if (optionStar != null && optionStar.param >= 7) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                                    + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                        }
                    } else {
                        sendEffectFailCombine(player);
                    }
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nhapNgocRong(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem() && (item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
                    Item nr = ItemService.gI().createNewItem((short) (item.template.id - 1));
                    sendEffectSuccessCombine(player);
                    InventoryServiceNew.gI().addItemBag(player, nr);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 7);
                    InventoryServiceNew.gI().sendItemBags(player);
                    reOpenItemCombine(player);
//                    sendEffectCombineDB(player, item.template.iconID);
                }
            }
        }
    }

    private void nangCapVatPham(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                return;//admin
            }
            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;
                if (player.inventory.gold < gold) {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }
                if (itemDNC.quantity < countDaNangCap) {
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (Objects.isNull(itemDBV)) {
                        return;
                    }
                    if (itemDBV.quantity < countDaBaoVe) {
                        return;
                    }
                }
                int level = 0;
                Item.ItemOption optionLevel = null;
                for (Item.ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (level < MAX_LEVEL_ITEM) {
                    player.inventory.gold -= gold;
                    Item.ItemOption option = null;
                    Item.ItemOption option2 = null;
                    for (Item.ItemOption io : itemDo.itemOptions) {
                        if (io.optionTemplate.id == 47
                                || io.optionTemplate.id == 6
                                || io.optionTemplate.id == 0
                                || io.optionTemplate.id == 7
                                || io.optionTemplate.id == 14
                                || io.optionTemplate.id == 22
                                || io.optionTemplate.id == 23) {
                            option = io;
                        } else if (io.optionTemplate.id == 27
                                || io.optionTemplate.id == 28) {
                            option2 = io;
                        }
                    }
                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                        option.param += (option.param * 10 / 100);
                        if (option2 != null) {
                            option2.param += (option2.param * 10 / 100);
                        }
                        if (optionLevel == null) {
                            itemDo.itemOptions.add(new Item.ItemOption(72, 1));
                        } else {
                            optionLevel.param++;
                        }
//                        if (optionLevel != null && optionLevel.param >= 5) {
//                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp "
//                                    + "thành công " + trangBi.template.name + " lên +" + optionLevel.param);
//                        }
                        sendEffectSuccessCombine(player);
                    } else {
                        if ((level == 2 || level == 4 || level == 6) && (player.combineNew.itemsCombine.size() != 3)) {
                            option.param -= (option.param * 10 / 100);
                            if (option2 != null) {
                                option2.param -= (option2.param * 10 / 100);
                            }
                            optionLevel.param--;
                        }
                        if ((level == 7) && (player.combineNew.itemsCombine.size() != 3)) {
                            option.param -= (option.param * 10 / 100);
                            if (option2 != null) {
                                option2.param -= (option2.param * 10 / 100);
                            }
                        }
                        sendEffectFailCombine(player);
                    }
                    if (player.combineNew.itemsCombine.size() == 3) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }
    //--------------------------------------------------------------------------

    /**
     * Hiệu ứng mở item
     *
     * @param player
     */
    public void sendEffectOpenItem(Player player, short icon1, short icon2) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_ITEM);
            msg.writer().writeShort(icon1);
            msg.writer().writeShort(icon2);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thành công
     *
     * @param player
     */
    private void sendEffectSuccessCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_SUCCESS);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thất bại
     *
     * @param player
     */
    private void sendEffectFailCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_FAIL);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Gửi lại danh sách đồ trong tab combine
     *
     * @param player
     */
    private void reOpenItemCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(REOPEN_TAB_COMBINE);
            msg.writer().writeByte(player.combineNew.itemsCombine.size());
            for (Item it : player.combineNew.itemsCombine) {
                for (int j = 0; j < player.inventory.itemsBag.size(); j++) {
                    if (it == player.inventory.itemsBag.get(j)) {
                        msg.writer().writeByte(j);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng ghép ngọc rồng
     *
     * @param player
     * @param icon
     */
    private void sendEffectCombineDB(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_DRAGON_BALL);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //--------------------------------------------------------------------------Ratio, cost combine
    public boolean checkdamocsanchua(Item item) {
        for (Item.ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 212) {
                return true;
            }
        }
        return false;
    }

    private int getGoldPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 30000000;
            case 1:
                return 60000000;
            case 2:
                return 90000000;
            case 3:
                return 120000000;
            case 4:
                return 180000000;
            case 5:
                return 250000000;
            case 6:
                return 300000000;
            case 7:
                return 500000000;
            case 8:
                return 100000000;
            case 9:
                return 120000000;
            case 10:
                return 150000000;
            case 11:
                return 200000000;
            case 12:
                return 220000000;
        }
        return 0;
    }

    private float getRatioPhaLeHoa(int star) { //tile dap do chi hat mit
        switch (star) {
            case 0:
                return 50f;
            case 1:
                return 35f;
            case 2:
                return 25f;
            case 3:
                return 20f;
            case 4:
                return 10f;
            case 5:
                return 5f;
            case 6:
                return 1f;
            case 7:
                return 0.1f;
//            case 8:
//                return 2;    
//            case 9:
//                return 1.8f;    
//            case 10:
//                return 1.4f;
//            case 11:
//                return 1.5f;
//             case 12:
//                return 1.2f;    
        }
        return 0;
    }

    private float getRatioNangkhi(int lvkhi) { //tile dap do chi hat mit
        switch (lvkhi) {
            case 1:
                return 60f;
            case 2:
                return 50f;
            case 3:
                return 30f;
            case 4:
                return 15f;
            case 5:
                return 10f;
            case 6:
                return 5f;
            case 7:
                return 2f;
        }
        return 0;
    }

    // NANG BONG TAI
    private float getRatioNangBT(int lvbt) {
        switch (lvbt) {
            case 1:
                return 50f;
            case 2:
                return 30f;
            case 3:
                return 20f;
        }
        return 0;
    }

    private float getRatioMCSABT(int lvbt) {
        switch (lvbt) {
            case 2:
                return 70f;
            case 3:
                return 50f;
            case 4:
                return 30f;
        }
        return 0;
    }

    private int getGoldNangBT(int lvbt) {
        return GOLD_NANG_BONG_TAI[lvbt - 1];
    }

    private int getGoldMCSABT(int lvbt) {
        return GOLD_MOCS_BONG_TAI * lvbt;
    }

    private int getRubyNangBT(int lvbt) {
        return RUBY_NANG_BONG_TAI + 2000 * (lvbt - 1);
    }

    private int getRubyMSCABT(int lvbt) {
        return RUBY_NANG_BONG_TAI + 2000 * (lvbt - 2);
    }

    private int getCountMVBTNangBT(int lvbt) {
        return 100 + 50 * (lvbt - 1);
    }

    private int getCountMHBTMCSABT(int lvbt) {
        return 100 + 50 * (lvbt - 1);
    }

    private boolean checkBongTai(Item item) {
        return item.template.id == 454 || item.template.id == 921 || item.template.id == 1155;
    }

    private boolean checkBongTaiMCS(Item item) {
        return item.template.id == 921 || item.template.id == 1155 || item.template.id == 1156;
    }

    private int levelBongTai(Item bongtai) {
        switch (bongtai.template.id) {
            case 454:
                return 1;
            case 921:
                return 2;
            case 1155:
                return 3;
            case 1156:
                return 4;
        }
        return 0;
    }

    private short getIDNewBongTai(int lvbtcu) {
        switch (lvbtcu) {
            case 1:
                return 921;
            case 2:
                return 1155;
            case 3:
                return 1156;
        }
        return 0;
    }

    private String createSuccessMessage(Item bongTai, Player player) {
        int levelBongTai = levelBongTai(bongTai);
        String message = "Bông tai Porata Cấp: " + levelBongTai + " \n|2|\n";
        for (Item.ItemOption option : bongTai.itemOptions) {
            message += option.getOptionString() + "\n";
        }
        message += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
        message += "|1|Cần " + getCountMVBTNangBT(levelBongTai) + " Mảnh vỡ Bông Tai\n";
        message += "|1|và " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
        return message;
    }

    private void createErrorMessage(Player player, String message) {
        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, message, "Đóng");
    }

    private int getGoldnangkhi(int lvkhi) {
        return GOLD_NANG_KHI + 100000000 * lvkhi;
    }

    private int getRubydnangkhi(int lvkhi) {
        return RUBY_NANG_KHI + 1000 * lvkhi;
    }

    private int getcountdnsnangkhi(int lvkhi) {
        return 10 + 10 * lvkhi;
    }

    private boolean checkctkhi(Item item) {
        return (item.template.id >= 1136 && item.template.id <= 1140) || (item.template.id >= 1208 && item.template.id <= 1210);
    }

    private int lvkhi(Item ctkhi) {
        switch (ctkhi.template.id) {
            case 1137:
                return 1;
            case 1208:
                return 2;
            case 1209:
                return 3;
            case 1210:
                return 4;
            case 1138:
                return 5;
            case 1139:
                return 6;
            case 1140:
                return 7;
        }
        return 0;
    }

    private short getidctkhisaukhilencap(int lvkhicu) {
        switch (lvkhicu) {
            case 1:
                return 1208;
            case 2:
                return 1209;
            case 3:
                return 1210;
            case 4:
                return 1138;
            case 5:
                return 1139;
            case 6:
                return 1140;
            case 7:
                return 1136;
        }
        return 0;
    }

    private int getGemPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 10;
            case 1:
                return 20;
            case 2:
                return 30;
            case 3:
                return 40;
            case 4:
                return 50;
            case 5:
                return 60;
            case 6:
                return 70;
            case 7:
                return 80;
            case 8:
                return 90;
            case 9:
                return 100;
            case 10:
                return 150;
            case 11:
                return 170;
            case 12:
                return 200;
        }
        return 0;
    }

    private int getGemEpSao(int star) {
        switch (star) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 5;
            case 3:
                return 10;
            case 4:
                return 25;
            case 5:
                return 50;
            case 6:
                return 100;
            case 7:
                return 120;
            case 8:
                return 150;
            case 9:
            case 10:
            case 11:
            case 12:
        }
        return 0;
    }

    private double getTileNangCapDo(int level) {
        switch (level) {
            case 0:
                return 80;
            case 1:
                return 50;
            case 2:
                return 20;
            case 3:
                return 10;
            case 4:
                return 7;
            case 5:
                return 5;
            case 6:
                return 3;
            case 7: // 7 sao
                return 1;
            case 8:
                return 0.5;
            case 9:
                return 0.3;
            case 10:
                return 0.2;
            case 11:
                return 0.1;
            case 12:
                return 0.05;
        }
        return 0;
    }

    private int getCountDaNangCapDo(int level) {
        switch (level) {
            case 0:
                return 3;
            case 1:
                return 7;
            case 2:
                return 11;
            case 3:
                return 17;
            case 4:
                return 23;
            case 5:
                return 35;
            case 6:
                return 50;
            case 7:
                return 70;
            case 8:
                return 70;
            case 9:
                return 70;
            case 10:
                return 70;
            case 11:
                return 70;
            case 12:
                return 70;
        }
        return 0;
    }

    private int getCountDaBaoVe(int level) {
        return level + 1;
    }

    private int getGoldNangCapDo(int level) {
        switch (level) {
            case 0:
                return 10000;
            case 1:
                return 70000;
            case 2:
                return 300000;
            case 3:
                return 1500000;
            case 4:
                return 7000000;
            case 5:
                return 23000000;
            case 6:
                return 100000000;
            case 7:
                return 250000000;
        }
        return 0;
    }

    //--------------------------------------------------------------------------check
    private boolean isCoupleItemNangCap(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type < 5) {
                trangBi = item1;
            } else if (item1.template.type == 14) {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type < 5) {
                trangBi = item2;
            } else if (item2.template.type == 14) {
                daNangCap = item2;
            }
        }
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else return trangBi.template.type == 4 && daNangCap.template.id == 220;
        } else {
            return false;
        }
    }

    private boolean isCoupleItemNangCapCheck(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else return trangBi.template.type == 4 && daNangCap.template.id == 220;
        } else {
            return false;
        }
    }

    private boolean isDaPhaLe(Item item) {
        return item != null && (item.template.type == 30 || (item.template.id >= 14 && item.template.id <= 20));
    }

    private boolean isTrangBiPhaLeHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            return item.template.type < 5 || item.template.type == 32 || item.template.type == 23 || item.template.type == 24;
        } else {
            return false;
        }
    }

    private int getParamDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).param;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 5; // +5%hp
            case 19:
                return 5; // +5%ki
            case 18:
                return 5; // +5%hp/30s
            case 17:
                return 5; // +5%ki/30s
            case 16:
                return 3; // +3%sđ
            case 15:
                return 2; // +2%giáp
            case 14:
                return 5; // +5%né đòn
            default:
                return -1;
        }
    }

    private int getOptionDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).optionTemplate.id;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 77;
            case 19:
                return 103;
            case 18:
                return 80;
            case 17:
                return 81;
            case 16:
                return 50;
            case 15:
                return 94;
            case 14:
                return 108;
            default:
                return -1;
        }
    }

    /**
     * Trả về id item c0
     *
     * @param gender
     * @param type
     * @return
     */
    private int getTempIdItemC0(int gender, int type) {
        if (type == 4) {
            return 12;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 0;
                    case 1:
                        return 6;
                    case 2:
                        return 21;
                    case 3:
                        return 27;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 1;
                    case 1:
                        return 7;
                    case 2:
                        return 22;
                    case 3:
                        return 28;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 2;
                    case 1:
                        return 8;
                    case 2:
                        return 23;
                    case 3:
                        return 29;
                }
                break;
        }
        return -1;
    }

    //Trả về tên đồ c0
    private String getNameItemC0(int gender, int type) {
        if (type == 4) {
            return "Rada cấp 1";
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return "Áo vải 3 lỗ";
                    case 1:
                        return "Quần vải đen";
                    case 2:
                        return "Găng thun đen";
                    case 3:
                        return "Giầy nhựa";
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return "Áo sợi len";
                    case 1:
                        return "Quần sợi len";
                    case 2:
                        return "Găng sợi len";
                    case 3:
                        return "Giầy sợi len";
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return "Áo vải thô";
                    case 1:
                        return "Quần vải thô";
                    case 2:
                        return "Găng vải thô";
                    case 3:
                        return "Giầy vải thô";
                }
                break;
        }
        return "";
    }

    //--------------------------------------------------------------------------Text tab combine
    private String getTextTopTabCombine(int type) {
        switch (type) {
            case EP_SAO_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở lên mạnh mẽ";
            case PHA_LE_HOA_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case NHAP_NGOC_RONG:
                return "Ta sẽ phù phép\ncho 7 viên Ngọc Rồng\nthành 1 viên Ngọc Rồng cấp cao";
            case NANG_CAP_VAT_PHAM:
                return "Ta sẽ phù phép cho trang bị của ngươi trở lên mạnh mẽ";
            case PHAN_RA_DO_THAN_LINH:
                return "Ta sẽ phân rã \n  trang bị của người thành điểm!";
            case NANG_CAP_DO_TS:
                return "Ta sẽ nâng cấp \n  trang bị của người thành\n đồ thiên sứ!";
            case NANG_CAP_DO_HD:
                return "Ta sẽ nâng cấp \n trang bị của ngươi thành\n đồ hủy diệt!";
            case NANG_CAP_SKH_VIP:
                return "Thiên sứ nhờ ta nâng cấp \n  trang bị của người thành\n SKH VIP!";
            case NANG_CAP_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\n Tăng một cấp";
            case MO_CHI_SO_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata +2, +3 hoặc +4 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case DOI_CHI_SO_AN_CHIEN_LINH:
                return "Ta sẽ phù phép\ncho Chiến Linh của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case NANG_CAP_KHI:
                return "Ta sẽ phù phép\ncho Cải trang Khỉ của ngươi\nTăng một cấp!!";
            case NANG_CHIEN_LINH:
                return "Ta sẽ biến linh thú của ngươi \nThành Chiến Linh!!!";
            default:
                return "";
        }
    }

    private String getTextInfoTabCombine(int type) {
        switch (type) {
            case EP_SAO_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa) có ô đặt sao pha lê\nChọn loại sao pha lê\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'";
            case NHAP_NGOC_RONG:
                return "Vào hành trang\nChọn 7 viên ngọc cùng sao\nSau đó chọn 'Làm phép'";
            case NANG_CAP_VAT_PHAM:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHAN_RA_DO_THAN_LINH:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để phân rã\n"
                        + "Sau đó chọn 'Phân Rã'";
            case NANG_CAP_DO_TS:
                return "vào hành trang\nChọn 2 trang bị hủy diệt bất kì\nkèm 1 món đồ thần linh\n và 5 mảnh thiên sứ\n "
                        + "sẽ cho ra đồ thiên sứ từ 0-15% chỉ số"
                        + "Sau đó chọn 'Nâng Cấp'";
            case NANG_CAP_DO_HD:
                return "vào hành trang\nChọn 3 trang bị thần linh bất kì"
                        + "\nsẽ cho ra đồ hủy diệt từ 0-15% chỉ số"
                        + "\nSau đó chọn 'Nâng Cấp'";
            case NANG_CAP_SKH_VIP:
                return "Vào hành trang\nChọn 1 trang bị thiên sứ bất kì\nChọn 'Nâng Cấp'";
            case NANG_CAP_BONG_TAI:
                return "Vào hành trang\nChọn bông tai Porata\nChọn mảnh bông tai để nâng cấp \nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI:
                return "Vào hành trang\nChọn bông tai Porata +2, +3 hoặc +4\nChọn Mảnh hồn bông tai và Đá xanh lam\nSau đó chọn 'Nâng cấp'";
            case DOI_CHI_SO_AN_CHIEN_LINH:
                return "Vào hành trang\nChọn Chiến Linh\nChọn x99 Đá ma thuật\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_KHI:
                return "Vào hành trang\nChọn Cải trang Khỉ \nChọn Đá Ngũ Sắc để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case NANG_CHIEN_LINH:
                return "Vào hành trang\nChọn Linh Thú \nChọn x99 Thăng tinh thạch để nâng cấp\nSau đó chọn 'Nâng cấp'";
            default:
                return "";
        }
    }

}

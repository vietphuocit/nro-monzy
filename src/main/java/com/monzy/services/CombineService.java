package com.monzy.services;

import com.monzy.consts.ConstNpc;
import com.monzy.models.item.Item;
import com.monzy.models.item.Item.ItemOption;
import com.monzy.models.npc.Npc;
import com.monzy.models.npc.NpcManager;
import com.monzy.models.player.Player;
import com.monzy.server.Manager;
import com.monzy.server.ServerNotify;
import com.monzy.utils.Util;
import com.network.io.Message;
import org.apache.commons.lang3.text.WordUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CombineService {

    private static final int TIME_COMBINE = 1;
    // LIMIT STAR AND LEVEL ITEM
    public static final byte MAX_STAR_ITEM = 8;
    public static final byte MAX_LEVEL_ITEM = 8;
    // STATUS COMBINE
    private static final byte OPEN_TAB_COMBINE = 0;
    private static final byte REOPEN_TAB_COMBINE = 1;
    private static final byte COMBINE_SUCCESS = 2;
    private static final byte COMBINE_FAIL = 3;
    private static final byte COMBINE_CHANGE_OPTION = 4;
    private static final byte COMBINE_DRAGON_BALL = 5;
    public static final byte OPEN_ITEM = 6;
    // BA HIT MIT DAO KAME
    public static final int EP_SAO_TRANG_BI = 500;
    public static final int PHA_LE_HOA_TRANG_BI = 501;
    // BA HIT MIT VACH, SIEU THI
    public static final int NANG_CAP_VAT_PHAM = 503;
    public static final int NANG_CAP_BONG_TAI = 504;
    public static final int MO_CHI_SO_BONG_TAI = 505;
    public static final int NHAP_NGOC_RONG = 506;
    public static final int NANG_CAP_DO_HD = 507;
    public static final int NANG_CAP_DO_TS = 508;
    // KHI DAU MOI
    public static final int NANG_CAP_KHI = 509;
    // TRUNG LINH THU
    public static final int NANG_CHIEN_LINH = 510;
    public static final int DOI_CHI_SO_AN_CHIEN_LINH = 511;
    // WHIS
    public static final int NANG_CAP_SKH_VIP = 519;
    //
    public static final long GOLD_NANG_CAP_HUY_DIET = 10000000000L;
    public static final int RUBY_NANG_CAP_HUY_DIET = 5000;
    public static final long GOLD_NANG_CAP_THIEN_SU = 20000000000L;
    public static final int RUBY_NANG_CAP_THIEN_SU = 10000;
    public static final long GOLD_NANG_CAP_SKH_VIP = 30000000000L;
    public static final int RUBY_NANG_CAP_SKH_VIP = 20000;
    private static final int RATIO_NANG_CHIEN_LINH = 20;
    private static final int GOLD_NANG_CHIEN_LINH = 1_000_000_000;
    private static final int RUBY_NANG_CHIEN_LINH = 5000;
    private static final int RATIO_DOI_CHI_SO_AN_CHIEN_LINH = 50;
    private static final int GOLD_DOI_CHI_SO_AN_CHIEN_LINH = 500_000_000;
    private static final int RUBY_DOI_CHI_SO_AN_CHIEN_LINH = 1000;
    private final Npc baHatMit;
    private final Npc npsthiensu64;
    private final Npc khidaumoi;
    private final Npc trungLinhThu;
    private static CombineService i;

    public CombineService() {
        this.baHatMit = NpcManager.getNpc(ConstNpc.BA_HAT_MIT);
        this.npsthiensu64 = NpcManager.getNpc(ConstNpc.NPC_64);
        this.khidaumoi = NpcManager.getNpc(ConstNpc.KHI_DAU_MOI);
        this.trungLinhThu = NpcManager.getNpc(ConstNpc.TRUNG_LINH_THU);
    }

    public static CombineService gI() {
        if (i == null) {
            i = new CombineService();
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
        player.conbine.setTypeCombine(type);
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
        player.conbine.clearItemCombine();
        if (index.length > 0) {
            for (int i : index) {
                player.conbine.itemsCombine.add(player.inventory.itemsBag.get(i));
            }
        }
        switch (player.conbine.typeCombine) {
            case NANG_CAP_VAT_PHAM: {
                if (player.conbine.itemsCombine.size() < 2 || player.conbine.itemsCombine.size() > 3) {
                    Service.gI().sendThongBaoOK(player, "Hãy Chọn 1 Trang Bị Và Đúng Loại Đá Nâng Cấp.");
                    break;
                }
                Item trangBi = player.conbine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).findFirst().orElse(null);
                Item daNangCap = player.conbine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).findFirst().orElse(null);
                Item daBaoVe = player.conbine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).findFirst().orElse(null);
                if (trangBi == null) {
                    Service.gI().sendThongBaoOK(player, "Thiếu Trang Bị.");
                    break;
                }
                if (daNangCap == null) {
                    Service.gI().sendThongBaoOK(player, "Thiếu Đá Nâng Cấp.");
                    break;
                }
                if (player.conbine.itemsCombine.size() == 3 && daBaoVe == null) {
                    Service.gI().sendThongBaoOK(player, "Sai Nguyên Liệu Rồi.");
                    break;
                }
                if (isCoupleItemNangCapCheck(trangBi, daNangCap)) {
                    Service.gI().sendThongBaoOK(player, "Sai Đá Nâng Cấp Rồi");
                    break;
                }
                int level = trangBi.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 72).findFirst().orElse(new ItemOption(72, 0)).param;
                if (level >= MAX_LEVEL_ITEM) {
                    Service.gI().sendThongBaoOK(player, "Trang Bị Của Ngươi Đã Đạt Cấp Tối Đa");
                    break;
                }
                player.conbine.goldCombine = getGoldNangCapDo(level);
                player.conbine.ratioCombine = getTileNangCapDo(level);
                player.conbine.countDaNangCap = getCountDaNangCapDo(level);
                player.conbine.countDaBaoVe = (short) getCountDaBaoVe(level);
                if (checkInventory(player)) {
                    break;
                }
                if (player.conbine.countDaNangCap > daNangCap.quantity) {
                    Service.gI().sendThongBaoOK(player, "Còn Thiếu\n" + (player.conbine.countDaNangCap - daNangCap.quantity) + " " + daNangCap.template.name);
                    break;
                }
                if (player.conbine.itemsCombine.size() == 3 && daBaoVe != null && daBaoVe.quantity < player.conbine.countDaBaoVe) {
                    Service.gI().sendThongBaoOK(player, "Còn Thiếu" + (player.conbine.countDaBaoVe - daBaoVe.quantity) + " Đá Bảo Vệ");
                    break;
                }
                StringBuilder npcSay = new StringBuilder("|2|Hiện tại: " + trangBi.template.name + " (+" + level + ")\n|0|");
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id != 72) {
                        npcSay.append(io.getOptionString()).append("\n");
                    }
                }
                String option = null;
                int param = 0;
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 47 || io.optionTemplate.id == 6 || io.optionTemplate.id == 0 || io.optionTemplate.id == 7 || io.optionTemplate.id == 14 || io.optionTemplate.id == 22 || io.optionTemplate.id == 23) {
                        option = io.optionTemplate.name;
                        param = io.param + (io.param * 10 / 100);
                        break;
                    }
                }
                if (option == null) {
                    Service.gI().sendThongBaoOK(player, "Trang Bị Này Lỗi Rồi :(");
                    break;
                }
                npcSay.append("|2|Sau khi nâng cấp: ").append(trangBi.template.name).append(" (+").append(level + 1).append(")\n|1|");
                npcSay.append(option.replaceAll("#", String.valueOf(param)));
                npcSay.append("\n|7|Tỉ lệ thành công: ").append(player.conbine.ratioCombine).append("%.");
                npcSay.append("\n|1|Cần ").append(Util.numberToMoney(player.conbine.goldCombine)).append(" vàng");
                npcSay.append("\n|1|Cần ").append(player.conbine.countDaNangCap).append(" ").append(daNangCap.template.name);
                npcSay.append((player.conbine.itemsCombine.size() == 3 && daBaoVe != null) ? "\n|1|Cần " + player.conbine.countDaBaoVe + " đá bảo vệ" : "");
                if ((level == 2 || level == 4 || level == 6) && !(player.conbine.itemsCombine.size() == 3 && daBaoVe != null)) {
                    npcSay.append("\n|7|Nếu thất bại sẽ rớt xuống (+").append(level - 1).append(").");
                }
                if ((level == 7) && !(player.conbine.itemsCombine.size() == 3 && daBaoVe != null)) {
                    npcSay.append("\n|7|Nếu thất bại sẽ không giảm cấp nhưng sẽ bị giảm chỉ số.");
                }
                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, WordUtils.capitalize(npcSay.toString()), "Nâng cấp", "Từ chối");
                break;
            }
            case NHAP_NGOC_RONG: {
                if (checkInventory(player)) {
                    break;
                }
                if (player.conbine.itemsCombine.size() != 1) {
                    break;
                }
                Item itemNhapNR = player.conbine.itemsCombine.get(0);
                if (itemNhapNR == null || !itemNhapNR.isNotNullItem() || itemNhapNR.template.id <= 14 || itemNhapNR.template.id > 20 || itemNhapNR.quantity < 7) {
                    Service.gI().sendThongBaoOK(player, "Cần 7 Viên Ngọc Rồng 2 Sao Trở Lên");
                    break;
                }
                String npcSay = "|2|Con có muốn biến 7 " + itemNhapNR.template.name + " thành\n";
                npcSay += "1 viên " + ItemService.gI().getTemplate((short) (itemNhapNR.template.id - 1)).name;
                npcSay += "\n|7|Cần 7 " + itemNhapNR.template.name;
                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, WordUtils.capitalize(npcSay), "Làm phép", "Từ chối");
                break;
            }
            case NANG_CAP_BONG_TAI: {
                if (player.conbine.itemsCombine.size() != 2) {
                    Service.gI().sendThongBaoOK(player, "Cần 1 Bông Tai Porata Cấp 1, 2 Hoặc 3 Và Mảnh Vỡ Bông Tai");
                    break;
                }
                Item bongTaiNC = player.conbine.itemsCombine.stream().filter(this::checkBongTai).findFirst().orElse(null);
                Item manhVoBTNC = player.conbine.itemsCombine.stream().filter(item -> item.template.id == 933).findFirst().orElse(null);
                if (bongTaiNC == null) {
                    Service.gI().sendThongBaoOK(player, "Thiếu 1 Bông Tai Porata Cấp 1, 2 Hoặc 3");
                    break;
                }
                if (manhVoBTNC == null) {
                    Service.gI().sendThongBaoOK(player, "Thiếu Mảnh Vỡ Bông Tai");
                    break;
                }
                int levelBongTaiNC = levelBongTai(bongTaiNC);
                int getCountMVBT = getCountMVBTNangBT(levelBongTaiNC);
                player.conbine.goldCombine = getGoldNangBT(levelBongTaiNC);
                player.conbine.rubyCombine = getRubyNangBT(levelBongTaiNC);
                player.conbine.ratioCombine = getRatioNangBT(levelBongTaiNC);
                if (checkInventory(player)) {
                    break;
                }
                if (manhVoBTNC.quantity < getCountMVBT) {
                    Service.gI().sendThongBaoOK(player, "Còn Thiếu " + Util.numberToMoney(getCountMVBT - manhVoBTNC.quantity) + " Mảnh Vỡ Bông Tai");
                    break;
                }
                StringBuilder npcSay = new StringBuilder("|2|Hiện tại: Bông Tai Porata Cấp: " + levelBongTai(bongTaiNC) + "\n|2|");
                for (Item.ItemOption option : bongTaiNC.itemOptions) {
                    npcSay.append(option.getOptionString()).append("\n");
                }
                npcSay.append("|2|Sau khi nâng cấp: Bông Tai Porata Cấp: ").append(levelBongTai(bongTaiNC) + 1);
                npcSay.append("\n|7|Sau khi nâng cấp bạn sẽ mất chỉ số ẩn");
                npcSay.append("\n|7|Tỉ lệ thành công: ").append(player.conbine.ratioCombine).append("%");
                npcSay.append("\n|1|Cần ").append(Util.numberToMoney(player.conbine.goldCombine)).append(" vàng và ").append(player.conbine.rubyCombine).append(" hồng ngọc");
                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, WordUtils.capitalize(npcSay.toString()), "Nâng cấp");
                break;
            }
            case MO_CHI_SO_BONG_TAI: {
                if (player.conbine.itemsCombine.size() != 3) {
                    Service.gI().sendThongBaoOK(player, "Cần 1 Bông Tai Porata Cấp 2, 3 Hoặc 4\nMảnh Hồn Bông Tai Và 1 Đá Xanh Lam");
                    break;
                }
                Item bongTai = player.conbine.itemsCombine.stream().filter(this::checkBongTaiMCS).findFirst().orElse(null);
                Item manhHon = player.conbine.itemsCombine.stream().filter(item -> item.template.id == 934).findFirst().orElse(null);
                Item daXanhLam = player.conbine.itemsCombine.stream().filter(item -> item.template.id == 935).findFirst().orElse(null);
                if (bongTai == null) {
                    Service.gI().sendThongBaoOK(player, "Thiếu 1 Bông Tai Porata Cấp 2, 3 Hoặc 4");
                    break;
                }
                if (manhHon == null) {
                    Service.gI().sendThongBaoOK(player, "Thiếu Mảnh Hồn Bông Tai");
                    break;
                }
                if (daXanhLam == null) {
                    Service.gI().sendThongBaoOK(player, "Thiếu Đá Xanh Lam");
                    break;
                }
                int levelBongTai = levelBongTai(bongTai);
                int countMVBTNangBT = getCountMVBTNangBT(levelBongTai);
                player.conbine.goldCombine = getGoldMCSABT(levelBongTai);
                player.conbine.rubyCombine = getRubyMSCABT(levelBongTai);
                player.conbine.ratioCombine = getRatioMCSABT(levelBongTai);
                if (checkInventory(player)) {
                    break;
                }
                if (manhHon.quantity < countMVBTNangBT) {
                    Service.gI().sendThongBaoOK(player, "Thiếu " + (countMVBTNangBT - manhHon.quantity) + " Mảnh Hồn Bông Tai");
                    break;
                }
                String npcSay = bongTai.template.name;
                npcSay += "\n|7|Tỉ lệ thành công: " + player.conbine.ratioCombine + "%";
                npcSay += "\n|1|Cần " + countMVBTNangBT + " Mảnh hồn bông tai và 1 Đá xanh lam";
                npcSay += "\n|1|Cần " + Util.numberToMoney(player.conbine.goldCombine) + " vàng và " + player.conbine.rubyCombine + " hồng ngọc";
                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, WordUtils.capitalize(npcSay), "Nâng cấp");
                break;
            }
            case NANG_CAP_DO_HD: {
                if (player.conbine.itemsCombine.size() != 3) {
                    Service.gI().sendThongBaoOK(player, "Ta Chỉ Cần 3 Trang Bị Thần Linh");
                    break;
                }
                if (player.conbine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() < 3) {
                    Service.gI().sendThongBaoOK(player, "Thiếu Trang Bị Thần Linh");
                    break;
                }
                player.conbine.goldCombine = GOLD_NANG_CAP_HUY_DIET;
                player.conbine.rubyCombine = RUBY_NANG_CAP_HUY_DIET;
                if (checkInventory(player)) {
                    break;
                }
                Item firstItemCombine = player.conbine.itemsCombine.get(0);
                String npcSay = "|2|Con có muốn đổi các món sau:\n|7|"
                        + player.conbine.itemsCombine.stream().map(item -> item.template.name).collect(Collectors.joining("\n"))
                        + "\n|2|Lấy " + player.conbine.itemsCombine.stream().filter(Item::isDTL).findFirst().get().typeName() + " Hủy diệt " + (firstItemCombine.template.gender == 0 ? "Trái đất" : firstItemCombine.template.gender == 1 ? "Namec" : "Xayda")
                        + "\n|1|Cần " + Util.numberToMoney(player.conbine.goldCombine) + " vàng và " + player.conbine.rubyCombine + " hồng ngọc";
                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_NANG_CAP_DO_HD, WordUtils.capitalize(npcSay), "Nâng cấp", "Từ chối");
                break;
            }
            case NANG_CAP_DO_TS: {
                if (player.conbine.itemsCombine.size() != 4) {
                    Service.gI().sendThongBaoOK(player, "Ta Chỉ Cần 2 Trang Bị Hủy Diệt, 1 Trang Bị Thần Linh và 5 Mảnh Thiên Sứ");
                    return;
                }
                if (player.conbine.itemsCombine.stream().noneMatch(item -> item.isNotNullItem() && item.isDTL())) {
                    Service.gI().sendThongBaoOK(player, "Thiếu Trang Bị Thần Linh");
                    return;
                }
                if (player.conbine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() < 2) {
                    Service.gI().sendThongBaoOK(player, "Thiếu Trang Bị Hủy Diệt");
                    return;
                }
                if (player.conbine.itemsCombine.stream().noneMatch(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 5)) {
                    Service.gI().sendThongBaoOK(player, "Thiếu Mảnh Thiên Sứ");
                    return;
                }
                player.conbine.goldCombine = GOLD_NANG_CAP_THIEN_SU;
                player.conbine.rubyCombine = RUBY_NANG_CAP_THIEN_SU;
                if (checkInventory(player)) {
                    break;
                }
                Item itemThanLinh = player.conbine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).findFirst().get();
                String npcSay = "|2|Con Có Muốn Dổi Các Món Sau:\n|7|" + player.conbine.itemsCombine.stream().map(item -> item.template.name).collect(Collectors.joining("\n"))
                        + "\n|2|Lấy " + player.conbine.itemsCombine.stream().filter(item -> item.template.type == 27).findFirst().get().template.name.split("\\(")[1].split("\\)")[0] + " Thiên Sứ " + (itemThanLinh.template.gender == 0 ? "Trái Đất" : itemThanLinh.template.gender == 1 ? "Namec" : "Xayda")
                        + "\n|1|Cần " + Util.numberToMoney(player.conbine.goldCombine) + " vàng và " + player.conbine.rubyCombine + " hồng ngọc";
                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_NANG_CAP_DO_HD, WordUtils.capitalize(npcSay), "Nâng cấp", "Từ chối");
                break;
            }
            case NANG_CAP_KHI: {
                if (player.conbine.itemsCombine.size() != 2) {
                    Service.gI().sendThongBaoOK(player, "Cần 1 Cải Trang Khỉ Cấp 1-7 Và Đá Ngũ Sắc");
                    break;
                }
                Item caiTrangKhi = player.conbine.itemsCombine.stream().filter(this::checkCTKhi).findFirst().orElse(null);
                Item daNguSac = player.conbine.itemsCombine.stream().filter(item -> item.template.id == 674).findFirst().orElse(null);
                if (caiTrangKhi == null) {
                    Service.gI().sendThongBaoOK(player, "Thiếu Cải Trang Khỉ");
                    break;
                }
                int levelKhi = levelKhi(caiTrangKhi);
                if (daNguSac == null || daNguSac.quantity < getCountDNSNangKhi(levelKhi)) {
                    Service.gI().sendThongBaoOK(player, "Thiếu " + (getCountDNSNangKhi(levelKhi) - (daNguSac == null ? 0 : daNguSac.quantity)) + " Đá Ngũ Sắc");
                    break;
                }
                player.conbine.goldCombine = getGoldNangKhi(levelKhi);
                player.conbine.rubyCombine = getRubyNangKhi(levelKhi);
                player.conbine.ratioCombine = getRatioNangkhi(levelKhi);
                if (checkInventory(player)) {
                    break;
                }
                StringBuilder npcSay = new StringBuilder("|2|Cải trang khỉ Cấp: " + levelKhi + "\n");
                for (Item.ItemOption io : caiTrangKhi.itemOptions) {
                    npcSay.append(io.getOptionString()).append("\n");
                }
                npcSay.append("|7|Tỉ lệ thành công: ").append(player.conbine.ratioCombine).append("%").append("\n");
                npcSay.append("|1|Cần ").append(Util.numberToMoney(player.conbine.goldCombine)).append(" vàng và ").append(player.conbine.rubyCombine).append(" hồng ngọc.");
                khidaumoi.createOtherMenu(player, ConstNpc.MENU_NANG_KHI, WordUtils.capitalize(npcSay.toString()), "Nâng cấp");
                break;
            }
            case EP_SAO_TRANG_BI: {
                if (player.conbine.itemsCombine.size() != 2) {
                    Service.gI().sendThongBao(player, "Cần 1 Trang Bị Có Lỗ Sao Pha Lê Và 1 Loại Đá Pha Lê Để Ép Vào");
                    return;
                }
                Item trangBi = player.conbine.itemsCombine.stream().filter(this::isTrangBiPhaLeHoa).findFirst().orElse(null);
                Item daPhaLe = player.conbine.itemsCombine.stream().filter(this::isDaPhaLe).findFirst().orElse(null);
                if (trangBi == null || daPhaLe == null) {
                    Service.gI().sendThongBao(player, "Cần 1 Trang Bị Có Lỗ Sao Pha Lê Và 1 Loại Đá Pha Lê Để Ép Vào");
                    return;
                }
                int star = trangBi.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 102).findFirst().orElse(new ItemOption(102, 0)).param; //sao pha lê đã ép
                int starEmpty = trangBi.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 107).findFirst().orElse(new ItemOption(107, 0)).param; //lỗ sao pha lê
                if (star >= starEmpty) {
                    Service.gI().sendThongBao(player, "Cần 1 Trang Bị Có Lỗ Sao Pha Lê Và 1 Loại Đá Pha Lê Để Ép Vào");
                    return;
                }
                player.conbine.gemCombine = getGemEpSao(star);
                StringBuilder npcSay = new StringBuilder(trangBi.template.name + "\n|2|");
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id != 102) {
                        npcSay.append(io.getOptionString()).append("\n");
                    }
                }
                if (daPhaLe.template.type == 30) {
                    for (Item.ItemOption io : daPhaLe.itemOptions) {
                        npcSay.append("|7|").append(io.getOptionString()).append("\n");
                    }
                } else {
                    npcSay.append("|7|").append(ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name.replaceAll("#", getParamDaPhaLe(daPhaLe) + "")).append("\n");
                }
                npcSay.append("|1|Cần ").append(Util.numberToMoney(player.conbine.gemCombine)).append(" ngọc");
                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, WordUtils.capitalize(npcSay.toString()), "Nâng cấp");
                break;
            }
            case PHA_LE_HOA_TRANG_BI: {
                if (player.conbine.itemsCombine.size() != 1) {
                    Service.gI().sendThongBao(player, "Hãy Chọn 1 Vật Phẩm Để Pha Lê Hóa");
                    return;
                }
                Item itemPhaLeHoa = player.conbine.itemsCombine.get(0);
                if (!isTrangBiPhaLeHoa(itemPhaLeHoa)) {
                    Service.gI().sendThongBao(player, "Vật Phẩm Này Không Thể Đục Lỗ");
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
                    Service.gI().sendThongBao(player, "Vật Phẩm Đã Đạt Tối Đa Sao Pha Lê");
                    break;
                }
                player.conbine.goldCombine = getGoldPhaLeHoa(starEpSao);
                player.conbine.gemCombine = getGemPhaLeHoa(starEpSao);
                player.conbine.ratioCombine = getRatioPhaLeHoa(starEpSao);
                StringBuilder npcSay = new StringBuilder(itemPhaLeHoa.template.name + "\n|2|");
                for (Item.ItemOption io : itemPhaLeHoa.itemOptions) {
                    if (io.optionTemplate.id != 102) {
                        npcSay.append(io.getOptionString()).append("\n");
                    }
                }
                npcSay.append("|7|Tỉ lệ thành công: ").append(player.conbine.ratioCombine).append("%").append("\n");
                if (player.conbine.goldCombine <= player.inventory.gold) {
                    npcSay.append("|1|Cần ").append(Util.numberToMoney(player.conbine.goldCombine)).append(" vàng");
                    baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay.toString(), "Nâng cấp\ncần " + player.conbine.gemCombine + " ngọc");
                } else {
                    npcSay.append("Còn thiếu ").append(Util.numberToMoney(player.conbine.goldCombine - player.inventory.gold)).append(" vàng");
                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, WordUtils.capitalize(npcSay.toString()), "Đóng");
                }
                break;
            }
            case NANG_CHIEN_LINH: {
                if (player.conbine.itemsCombine.size() != 2) {
                    trungLinhThu.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Nguyên Liệu Rồi Con Ơi.", "Đóng");
                    break;
                }
                Item linhThu = player.conbine.itemsCombine.stream().filter(item -> item.template.type == 72).findFirst().orElse(null);
                Item thangTinhThach = player.conbine.itemsCombine.stream().filter(item -> item.template.id == 2031).findFirst().orElse(null);
                if (linhThu == null) {
                    trungLinhThu.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 1 Linh Thú mới có thể nâng.", "Đóng");
                    break;
                }
                if (thangTinhThach == null || thangTinhThach.quantity < 99) {
                    trungLinhThu.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu " + Util.numberToMoney(99 - ((thangTinhThach == null) ? 0 : thangTinhThach.quantity)) + " Thăng Tinh Thạch", "Đóng");
                    break;
                }
                player.conbine.goldCombine = GOLD_NANG_CHIEN_LINH;
                player.conbine.rubyCombine = RUBY_NANG_CHIEN_LINH;
                player.conbine.ratioCombine = RATIO_NANG_CHIEN_LINH;
                String npcSay = "Nâng cấp Linh Thú: " + linhThu.template.name + " lên chiến linh.\n";
                for (Item.ItemOption io : linhThu.itemOptions) {
                    npcSay += "|2|" + io.getOptionString() + "\n";
                }
                npcSay += "|7|Tỉ lệ thành công: " + player.conbine.ratioCombine + "%" + "\n";
                if (player.conbine.goldCombine > player.inventory.gold) {
                    npcSay += "Còn thiếu " + Util.numberToMoney(player.conbine.goldCombine - player.inventory.gold) + " vàng";
                    trungLinhThu.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                    break;
                }
                if (player.conbine.rubyCombine > player.inventory.ruby) {
                    npcSay += "Còn thiếu " + Util.numberToMoney(player.conbine.rubyCombine - player.inventory.ruby) + " hồng ngọc";
                    trungLinhThu.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                    break;
                }
                npcSay += "|1|Cần " + Util.numberToMoney(player.conbine.goldCombine) + " vàng và " + Util.numberToMoney(player.conbine.rubyCombine) + " hồng ngọc";
                trungLinhThu.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Nâng cấp");
                break;
            }
            case DOI_CHI_SO_AN_CHIEN_LINH: {
                if (player.conbine.itemsCombine.size() != 2) {
                    trungLinhThu.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 1 Chiến Linh, X99 Đá ma thuật", "Đóng");
                    break;
                }
                Item chienLinh = null;
                Item daMaThuat = null;
                for (Item item : player.conbine.itemsCombine) {
                    if (item.template.id >= 2038 && item.template.id <= 2042) {
                        chienLinh = item;
                    } else if (item.template.id == 2030) {
                        daMaThuat = item;
                    }
                }
                if (chienLinh == null || daMaThuat == null || daMaThuat.quantity < 99) {
                    trungLinhThu.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 1 Chiến Linh, X99 Đá ma thuật", "Đóng");
                    break;
                }
                player.conbine.goldCombine = GOLD_DOI_CHI_SO_AN_CHIEN_LINH;
                player.conbine.rubyCombine = RUBY_DOI_CHI_SO_AN_CHIEN_LINH;
                player.conbine.ratioCombine = RATIO_DOI_CHI_SO_AN_CHIEN_LINH;
                if (checkInventory(player)) {
                    break;
                }
                String npcSay = "Chiến Linh\n|2|";
                for (Item.ItemOption io : chienLinh.itemOptions) {
                    npcSay += io.getOptionString() + "\n";
                }
                npcSay += "|7|Tỉ lệ thành công: " + player.conbine.ratioCombine + "%" + "\n";
                npcSay += "|1|Cần " + Util.numberToMoney(player.conbine.goldCombine) + " vàng";
                trungLinhThu.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Nâng cấp\ncần " + player.conbine.rubyCombine + " hồng ngọc");
                break;
            }
            case NANG_CAP_SKH_VIP: {
                if (player.conbine.itemsCombine.size() != 1) {
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy Đưa Ta 1 Trang Bị Thiên Sứ", "Đóng");
                    return;
                }
                if (player.conbine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTS()).count() != 1) {
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Trang Bị Thiên Sứ", "Đóng");
                    return;
                }
                player.conbine.goldCombine = GOLD_NANG_CAP_SKH_VIP;
                player.conbine.rubyCombine = RUBY_NANG_CAP_SKH_VIP;
                if (checkInventory(player)) {
                    break;
                }
                String npcSay = "|2|Con có muốn nâng cấp\n|7|" + player.conbine.itemsCombine.get(0).template.name
                        + "\n|1|Thành " + player.conbine.itemsCombine.get(0).typeName() + " kích hoạt VIP tương ứng"
                        + "\n|1|Cần " + Util.numberToMoney(GOLD_NANG_CAP_SKH_VIP) + " vàng và " + RUBY_NANG_CAP_SKH_VIP + " hồng ngọc";
                this.npsthiensu64.createOtherMenu(player, ConstNpc.MENU_NANG_CAP_SKH_VIP, WordUtils.capitalize(npcSay), "Nâng cấp", "Từ chối");
                break;
            }
        }
    }

    /**
     * Bắt đầu đập đồ - điều hướng từng loại đập đồ
     *
     * @param player
     */
    public void startCombine(Player player) {
        switch (player.conbine.typeCombine) {
            case NANG_CAP_VAT_PHAM:
                nangCapVatPham(player);
                break;
            case NHAP_NGOC_RONG:
                nhapNgocRong(player);
                break;
            case NANG_CAP_BONG_TAI:
                nangCapBongTai(player);
                break;
            case MO_CHI_SO_BONG_TAI:
                moChiSoBongTai(player);
                break;
            case NANG_CAP_DO_HD:
                nangCapDHD(player);
                break;
            case NANG_CAP_DO_TS:
                nangCapDTS(player);
                break;
            case NANG_CAP_KHI:
                nangCapKhi(player);
                break;
            case EP_SAO_TRANG_BI:
                epSaoTrangBi(player);
                break;
            case PHA_LE_HOA_TRANG_BI:
                phaLeHoaTrangBi(player);
                break;
            case NANG_CHIEN_LINH:
                nangCapChienLinh(player);
                break;
            case DOI_CHI_SO_AN_CHIEN_LINH:
                doiChiSoAnChienLinh(player);
                break;
            case NANG_CAP_SKH_VIP:
                nangCapSKHVIP(player);
                break;
        }
        player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
        player.conbine.clearParamCombine();
        player.conbine.lastTimeCombine = System.currentTimeMillis();
    }

    /**
     * Nâng cấp vật phẩm
     *
     * @param player
     */
    private void nangCapVatPham(Player player) {
        if (player.conbine.itemsCombine.size() < 2 || player.conbine.itemsCombine.size() > 3) {
            return;
        }
        Item trangBi = player.conbine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).findFirst().orElse(null);
        Item daNangCap = player.conbine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).findFirst().orElse(null);
        Item daBaoVe = player.conbine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).findFirst().orElse(null);
        if (trangBi == null) {
            return;
        }
        if (daNangCap == null) {
            return;
        }
        if (player.conbine.itemsCombine.size() == 3 && daBaoVe == null) {
            return;
        }
        if (isCoupleItemNangCapCheck(trangBi, daNangCap)) {
            return;
        }
        if (player.inventory.gold < player.conbine.goldCombine) {
            return;
        }
        if (daNangCap.quantity < player.conbine.countDaNangCap) {
            return;
        }
        if (player.conbine.itemsCombine.size() == 3 && daBaoVe.quantity < player.conbine.countDaBaoVe) {
            return;
        }
        ItemOption optionLevel = trangBi.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 72).findFirst().orElse(new ItemOption(72, 0));
        if (optionLevel.param >= MAX_LEVEL_ITEM) {
            return;
        }
        player.inventory.gold -= player.conbine.goldCombine;
        ItemOption option = null;
        ItemOption option2 = null;
        for (ItemOption io : trangBi.itemOptions) {
            if (io.optionTemplate.id == 47 || io.optionTemplate.id == 6 || io.optionTemplate.id == 0 || io.optionTemplate.id == 7 || io.optionTemplate.id == 14 || io.optionTemplate.id == 22 || io.optionTemplate.id == 23) {
                option = io;
            } else if (io.optionTemplate.id == 27 || io.optionTemplate.id == 28) {
                option2 = io;
            }
        }
        if (option == null) {
            return;
        }
        if (Util.isTrue(player.conbine.ratioCombine, 100)) {
            option.param += (option.param * 10 / 100);
            if (option2 != null) {
                option2.param += (option2.param * 10 / 100);
            }
            optionLevel.param++;
            if(optionLevel.optionTemplate.id == 72 && optionLevel.param == 1) {
                trangBi.itemOptions.add(optionLevel);
            }
            sendEffectSuccessCombine(player);
        } else {
            if ((optionLevel.param == 2 || optionLevel.param == 4 || optionLevel.param == 6) && (player.conbine.itemsCombine.size() != 3)) {
                option.param -= (option.param * 10 / 100);
                if (option2 != null) {
                    option2.param -= (option2.param * 10 / 100);
                }
                optionLevel.param--;
            }
            if ((optionLevel.param == 7) && (player.conbine.itemsCombine.size() != 3)) {
                option.param -= (option.param * 10 / 100);
                if (option2 != null) {
                    option2.param -= (option2.param * 10 / 100);
                }
            }
            sendEffectFailCombine(player);
        }
        if (player.conbine.itemsCombine.size() == 3) {
            InventoryService.gI().subQuantityItemsBag(player, daBaoVe, player.conbine.countDaBaoVe);
        }
        InventoryService.gI().subQuantityItemsBag(player, daNangCap, player.conbine.countDaNangCap);
        InventoryService.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        reOpenItemCombine(player);
    }

    /**
     * Nhập ngọc rồng
     *
     * @param player
     */
    private void nhapNgocRong(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) == 0) {
            return;
        }
        if (player.conbine.itemsCombine.isEmpty()) {
            return;
        }
        Item item = player.conbine.itemsCombine.get(0);
        if (item != null && item.isNotNullItem() && (item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
            Item nr = ItemService.gI().createNewItem((short) (item.template.id - 1));
//                    sendEffectSuccessCombine(player);
            InventoryService.gI().addItemBag(player, nr);
            InventoryService.gI().subQuantityItemsBag(player, item, 7);
            InventoryService.gI().sendItemBags(player);
            reOpenItemCombine(player);
//                    sendEffectCombineDB(player, item.template.iconID);
        }
    }

    /**
     * Nâng cấp lên trang bị hủy diệt
     * Cần: 3 trang bị thần
     * 10 tỷ vàng
     * 5k hồng ngọc
     * Nhận: 1 Trang bị hủy diệt 'CÙNG LOẠI VÀ CÙNG HỆ VỚI TRANG BỊ THẦN ĐẦU TIÊN'
     *
     * @param player
     */
    public void nangCapDHD(Player player) {
        if (player.conbine.itemsCombine.size() != 3) {
            return;
        }
        if (player.conbine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() != 3) {
            return;
        }
        if (checkInventory(player)) {
            return;
        }
        player.inventory.gold -= player.conbine.goldCombine;
        player.inventory.ruby -= player.conbine.rubyCombine;
        Item firstItemCombine = player.conbine.itemsCombine.stream().filter(Item::isDTL).findFirst().get();
        CombineService.gI().sendEffectOpenItem(player, firstItemCombine.template.iconID, firstItemCombine.template.iconID);
        short itemId;
        if (firstItemCombine.template.gender == 3 || firstItemCombine.template.type == 4) {
            itemId = Manager.ID_RADAR_HD;
        } else {
            itemId = Manager.IDS_DO_HUY_DIET[firstItemCombine.template.gender][firstItemCombine.template.type];
        }
        Item itemHD = ItemService.gI().randomCSDHD(itemId, player.gender);
        player.conbine.itemsCombine.forEach(i -> InventoryService.gI().subQuantityItemsBag(player, i, 1));
        InventoryService.gI().addItemBag(player, itemHD);
        InventoryService.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemHD.template.name);
        sendEffectSuccessCombine(player);
        player.conbine.itemsCombine.clear();
        reOpenItemCombine(player);

    }

    /**
     * Nâng cấp lên trang bị thiên sứ
     * Cần: 2 trang bị hủy diệt
     * 1 trang bị thần
     * 5 mảnh thiên sứ
     * 20 tỷ vàng
     * 10k hồng ngọc
     * Nhận: 1 Trang bị thiên sứ 'CÙNG LOẠI VỚI MẢNH THIÊN SỨ VÀ CÙNG HỆ VỚI TRANG BỊ THẦN'
     *
     * @param player
     */
    public void nangCapDTS(Player player) {
        if (player.conbine.itemsCombine.size() != 4) {
            return;
        }
        if (checkInventory(player)) {
            return;
        }
        Item trangBiThanLinh = player.conbine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).findFirst().orElse(null);
        List<Item> listTrangBiHuyDiet = player.conbine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).collect(Collectors.toList());
        Item manhThienSu = player.conbine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 5).findFirst().orElse(null);
        if (trangBiThanLinh == null) {
            return;
        }
        if (listTrangBiHuyDiet.size() < 2) {
            return;
        }
        if (manhThienSu == null || manhThienSu.quantity < 5) {
            return;
        }
        player.inventory.gold -= player.conbine.goldCombine;
        player.inventory.ruby -= player.conbine.rubyCombine;
        Item trangBiThienSu = ItemService.gI().randomCSDTS(Manager.IDS_DO_THIEN_SU[trangBiThanLinh.template.gender > 2 ? player.gender : trangBiThanLinh.template.gender][manhThienSu.typeIdManh()], trangBiThanLinh.template.gender);
        InventoryService.gI().addItemBag(player, trangBiThienSu);
        InventoryService.gI().subQuantityItemsBag(player, trangBiThanLinh, 1);
        InventoryService.gI().subQuantityItemsBag(player, manhThienSu, 5);
        listTrangBiHuyDiet.forEach(item -> InventoryService.gI().subQuantityItemsBag(player, item, 1));
        InventoryService.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + trangBiThienSu.template.name);
        sendEffectSuccessCombine(player);
        player.conbine.itemsCombine.clear();
        reOpenItemCombine(player);
    }

    /**
     * Nâng cấp lên trang bị kích hoạt VIP
     * Cần: 1 trang bị thiên sứ
     * 30 tỷ vàng
     * 20k hồng ngọc
     * Nhận: Ngẫu nhiên 4 trang bị mạnh nhất trong shop 'CÙNG LOẠI VÀ CÙNG HỆ VỚI TRANG BỊ THIÊN SỨ'
     *
     * @param player
     */
    public void nangCapSKHVIP(Player player) {
        if (player.conbine.itemsCombine.size() != 1) {
            return;
        }
        if (player.conbine.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTS()).count() != 1) {
            return;
        }
        if (checkInventory(player)) {
            return;
        }
        player.inventory.gold -= GOLD_NANG_CAP_SKH_VIP;
        player.inventory.ruby -= RUBY_NANG_CAP_SKH_VIP;
        Item trangBiThienSu = player.conbine.itemsCombine.stream().filter(Item::isDTS).findFirst().get();
        short itemId;
        if (trangBiThienSu.template.gender == 3 || trangBiThienSu.template.type == 4) {
            itemId = Manager.IDS_RADAR[Util.nextInt(8, 11)];
        } else {
            itemId = Manager.IDS_TRANG_BI_SHOP[trangBiThienSu.template.gender][trangBiThienSu.template.type][Util.nextInt(8, 11)];
        }
        int skhId = ItemService.gI().randomSKHId(trangBiThienSu.template.gender);
        Item itemSKH = ItemService.gI().newItemSKH(itemId, skhId);
        InventoryService.gI().addItemBag(player, itemSKH);
        InventoryService.gI().subQuantityItemsBag(player, trangBiThienSu, 1);
        InventoryService.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        Service.gI().sendThongBao(player, "Bạn Đã Nhận Được " + itemSKH.template.name);
        sendEffectSuccessCombine(player);
        player.conbine.itemsCombine.clear();
        reOpenItemCombine(player);

    }

    public void chiSoCTKhi(Player player, Item ctkhi, int lvkhi) {
        ctkhi.itemOptions.add(new ItemOption(50, 15 + 5 * lvkhi));//sd
        ctkhi.itemOptions.add(new ItemOption(77, 15 + 5 * lvkhi));//hp
        ctkhi.itemOptions.add(new ItemOption(103, 15 + 5 * lvkhi));//ki
        ctkhi.itemOptions.add(new ItemOption(14, 10 + 2 * lvkhi));//cm
        ctkhi.itemOptions.add(new ItemOption(5, 10 + 2 * lvkhi));//sd cm
        ctkhi.itemOptions.add(new ItemOption(106, 0));
        ctkhi.itemOptions.add(new ItemOption(34, 0));
        InventoryService.gI().sendItemBags(player);
    }

    public void chiSoChienLinh(Player player, Item chienLinh) {
        chienLinh.itemOptions.add(new ItemOption(50, Util.nextInt(20, 20)));//sd
        chienLinh.itemOptions.add(new ItemOption(77, Util.nextInt(20, 20)));//hp
        chienLinh.itemOptions.add(new ItemOption(103, Util.nextInt(20, 20)));//ki
        randomCSA(chienLinh);
        InventoryService.gI().sendItemBags(player);
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

    private void nangCapChienLinh(Player player) {
        if (player.conbine.itemsCombine.size() == 2) {
            long gold = player.conbine.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int ruby = player.conbine.rubyCombine;
            if (player.inventory.ruby < ruby) {
                Service.gI().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
                return;
            }
            Item linhthu = null;
            Item ttt = null;
            for (Item item : player.conbine.itemsCombine) {
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
                InventoryService.gI().subQuantityItemsBag(player, ttt, 99);
                if (Util.isTrue(player.conbine.ratioCombine, 100)) {
                    short[] chienlinh = {2038, 2039, 2040, 2041, 2042};
                    linhthu.template = ItemService.gI().getTemplate(chienlinh[Util.nextInt(0, 4)]);
                    linhthu.itemOptions.clear();
                    chiSoChienLinh(player, linhthu);
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }


    private void nangCapBongTai(Player player) {
        if (player.conbine.itemsCombine.size() != 2) {
            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 1 Bông tai Porata cấp 1 hoặc 2 hoặc 3 và Mảnh vỡ bông tai", "Đóng");
            return;
        }
        long gold = player.conbine.goldCombine;
        if (player.inventory.gold < gold) {
            Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
            return;
        }
        int ruby = player.conbine.rubyCombine;
        if (player.inventory.ruby < ruby) {
            Service.gI().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
            return;
        }
        Item bongTaiNC = null;
        Item manhVoBTNC = null;
        for (Item item : player.conbine.itemsCombine) {
            if (checkBongTai(item)) {
                bongTaiNC = item;
            } else if (item.template.id == 933) {
                manhVoBTNC = item;
            }
        }
        if (bongTaiNC == null || manhVoBTNC == null) {
            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 1 Bông tai Porata cấp 1 hoặc 2 hoặc 3 và Mảnh vỡ bông tai.", "Đóng");
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
        InventoryService.gI().subQuantityItemsBag(player, manhVoBTNC, getCountMVBT);
        if (Util.isTrue(player.conbine.ratioCombine, 100)) {
            bongTaiNC.template = ItemService.gI().getTemplate(getIDNewBongTai(levelBongTai));
            sendEffectSuccessCombine(player);
        } else {
            sendEffectFailCombine(player);
        }
        InventoryService.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        reOpenItemCombine(player);
    }

    private void moChiSoBongTai(Player player) {
        if (player.conbine.itemsCombine.size() != 3) {
            return;
        }
        long gold = player.conbine.goldCombine;
        int ruby = player.conbine.rubyCombine;
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
        for (Item item : player.conbine.itemsCombine) {
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
            InventoryService.gI().subQuantityItemsBag(player, manhHon, getCountMHBTMCSABT(levelBongTai(bongTai)));
            InventoryService.gI().subQuantityItemsBag(player, daXanhLam, 1);
            if (Util.isTrue(player.conbine.ratioCombine, 100)) {
                bongTai.itemOptions.clear();
                bongTai.itemOptions.add(new Item.ItemOption(Stream.of(50, 77, 103, 108, 94, 14, 80, 81).skip((long) (8 * Math.random())).findFirst().get(), Util.nextInt(5, levelBongTai(bongTai) * 5)));
                sendEffectSuccessCombine(player);
            } else {
                sendEffectFailCombine(player);
            }
            InventoryService.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            reOpenItemCombine(player);
        }
    }

    // BEGIN --- Nâng cấp cải trang khỉ

    private int getGoldNangKhi(int levelKhi) {
        return 500000000 + 100000000 * levelKhi;
    }

    private int getRubyNangKhi(int levelKhi) {
        return 1000 + 1000 * levelKhi;
    }

    private int getCountDNSNangKhi(int levelKhi) {
        return 10 + 10 * levelKhi;
    }

    private boolean checkCTKhi(Item item) {
        return (item.template.id >= 1136 && item.template.id <= 1140) || (item.template.id >= 1208 && item.template.id <= 1210);
    }

    private int levelKhi(Item caiTrangKhi) {
        switch (caiTrangKhi.template.id) {
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

    private short getIDCTKhiSauNC(int lvkhicu) {
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

    private void nangCapKhi(Player player) {
        if (player.conbine.itemsCombine.size() != 2) {
            Service.gI().sendThongBaoOK(player, "Cần 1 Cải Trang Khỉ Cấp 1-7 Và Đá Ngũ Sắc");
            return;
        }
        Item caiTrangKhi = player.conbine.itemsCombine.stream().filter(this::checkCTKhi).findFirst().orElse(null);
        Item daNguSac = player.conbine.itemsCombine.stream().filter(item -> item.template.id == 674).findFirst().orElse(null);
        if (caiTrangKhi == null) {
            Service.gI().sendThongBaoOK(player, "Thiếu Cải Trang Khỉ");
            return;
        }
        int levelKhi = levelKhi(caiTrangKhi);
        if (daNguSac == null || daNguSac.quantity < getCountDNSNangKhi(levelKhi)) {
            Service.gI().sendThongBaoOK(player, "Thiếu " + (getCountDNSNangKhi(levelKhi) - (daNguSac == null ? 0 : daNguSac.quantity)) + " Đá Ngũ Sắc");
            return;
        }
        player.inventory.gold -= player.conbine.goldCombine;
        player.inventory.ruby -= player.conbine.rubyCombine;
        InventoryService.gI().subQuantityItemsBag(player, daNguSac, getCountDNSNangKhi(levelKhi));
        if (Util.isTrue(player.conbine.ratioCombine, 100)) {
            short idCTKhiSauNC = getIDCTKhiSauNC(levelKhi);
            caiTrangKhi.template = ItemService.gI().getTemplate(idCTKhiSauNC);
            caiTrangKhi.itemOptions.clear();
            caiTrangKhi.itemOptions.add(new ItemOption(72, levelKhi + 1));
            chiSoCTKhi(player, caiTrangKhi, levelKhi);
            sendEffectSuccessCombine(player);
        } else {
            sendEffectFailCombine(player);
        }
        InventoryService.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        reOpenItemCombine(player);
    }
    // END ----- Nâng cấp cải trang khỉ

    private void doiChiSoAnChienLinh(Player player) {
        if (player.conbine.itemsCombine.size() != 2) {
            return;
        }
        long gold = player.conbine.goldCombine;
        if (player.inventory.gold < gold) {
            Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
            return;
        }
        int ruby = player.conbine.rubyCombine;
        if (player.inventory.ruby < ruby) {
            Service.gI().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
            return;
        }
        Item chienLinh = null;
        Item daMaThuat = null;
        for (Item item : player.conbine.itemsCombine) {
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
        InventoryService.gI().subQuantityItemsBag(player, daMaThuat, 99);
        if (Util.isTrue(player.conbine.ratioCombine, 100)) {
            chienLinh.itemOptions.remove(chienLinh.itemOptions.size() - 2);
            chienLinh.itemOptions.remove(chienLinh.itemOptions.size() - 1);
            randomCSA(chienLinh);
            sendEffectSuccessCombine(player);
        } else {
            sendEffectFailCombine(player);
        }
        InventoryService.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        reOpenItemCombine(player);
    }

    private void epSaoTrangBi(Player player) {
        if (player.conbine.itemsCombine.size() != 2) {
            return;
        }
        int requiredGem = player.conbine.gemCombine;
        if (player.inventory.gem < requiredGem) {
            Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
            return;
        }
        Item trangBi = null;
        Item daPhaLe = null;
        for (Item item : player.conbine.itemsCombine) {
            if (isTrangBiPhaLeHoa(item)) {
                trangBi = item;
            } else if (isDaPhaLe(item)) {
                daPhaLe = item;
            }
        }
        if (trangBi == null || daPhaLe == null) {
            return;
        }
        int star = 0;
        int starEmpty = 0;
        for (Item.ItemOption io : trangBi.itemOptions) {
            if (io.optionTemplate.id == 102) {
                star = io.param;
            } else if (io.optionTemplate.id == 107) {
                starEmpty = io.param;
            }
        }
        if (star >= starEmpty) {
            return;
        }
        player.inventory.gem -= requiredGem;
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
        Item.ItemOption optionStar = null;
        for (Item.ItemOption io : trangBi.itemOptions) {
            if (io.optionTemplate.id == 102) {
                optionStar = io;
                break;
            }
        }
        if (optionStar != null) {
            optionStar.param++;
        } else {
            trangBi.itemOptions.add(new Item.ItemOption(102, 1));
        }
        InventoryService.gI().subQuantityItemsBag(player, daPhaLe, 1);
        sendEffectSuccessCombine(player);
        InventoryService.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        reOpenItemCombine(player);
    }

    private void phaLeHoaTrangBi(Player player) {
        if (!player.conbine.itemsCombine.isEmpty()) {
            long gold = player.conbine.goldCombine;
            int gem = player.conbine.gemCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            } else if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item item = player.conbine.itemsCombine.get(0);
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
                    if (Util.isTrue(player.conbine.ratioCombine, (star == 6 || star == 7) ? 2000 : 100)) {
                        if (optionStar == null) {
                            item.itemOptions.add(new Item.ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }
                        sendEffectSuccessCombine(player);
                        if (optionStar != null && optionStar.param >= 7) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa " + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                        }
                    } else {
                        sendEffectFailCombine(player);
                    }
                }
                InventoryService.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    //--- Ratio, cost combine
    private float getTileNangCapDo(int level) {
        switch (level) {
            case 0:
                return 80f;
            case 1:
                return 50f;
            case 2:
                return 20f;
            case 3:
                return 10f;
            case 4:
                return 7f;
            case 5:
                return 5f;
            case 6:
                return 3f;
            case 7:
                return 1f;
        }
        return 0;
    }

    private int getCountDaNangCapDo(int level) {
        switch (level) {
            case 0:
                return 5;
            case 1:
                return 10;
            case 2:
                return 15;
            case 3:
                return 20;
            case 4:
                return 25;
            case 5:
                return 50;
            case 6:
                return 75;
            case 7:
                return 99;
        }
        return 0;
    }

    private int getCountDaBaoVe(int level) {
        return level + 1;
    }

    private int getGoldNangCapDo(int level) {
        switch (level) {
            case 0:
                return 1000000;
            case 1:
                return 2500000;
            case 2:
                return 5000000;
            case 3:
                return 10000000;
            case 4:
                return 25000000;
            case 5:
                return 50000000;
            case 6:
                return 100000000;
            case 7:
                return 250000000;
        }
        return 0;
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
                return 1000000000;
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
                return 0.5f;
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

    private int getGoldNangBT(int levelBongTai) {
        switch (levelBongTai) {
            case 1:
                return 300000000;
            case 2:
                return 500000000;
            case 3:
                return 1000000000;
        }
        return 0;
    }

    private int getGoldMCSABT(int levelBongTai) {
        return 500000000 * levelBongTai;
    }

    private int getRubyNangBT(int levelBongTai) {
        return 1000 + 2000 * (levelBongTai - 1);
    }

    private int getRubyMSCABT(int levelBongTai) {
        return 1000 + 2000 * (levelBongTai - 2);
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
        }
        return 0;
    }
    //--------------------------------------------------------------------------check

    private boolean isCoupleItemNangCapCheck(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return false;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return false;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return false;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return false;
            } else return trangBi.template.type != 4 || daNangCap.template.id != 220;
        } else {
            return true;
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
                return "Ta sẽ phù phép cho trang bị của ngươi trở nên mạnh mẽ";
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
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa) có ô đặt sao pha lê\nChọn loại sao pha lê\n" + "Sau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'";
            case NHAP_NGOC_RONG:
                return "Vào hành trang\nChọn 7 viên ngọc cùng sao\nSau đó chọn 'Làm phép'";
            case NANG_CAP_VAT_PHAM:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để nâng cấp\n" + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_DO_TS:
                return "vào hành trang\nChọn 2 trang bị hủy diệt bất kì\nkèm 1 món đồ thần linh\n và 5 mảnh thiên sứ\n " + "sẽ cho ra đồ thiên sứ từ 0-15% chỉ số" + "Sau đó chọn 'Nâng Cấp'";
            case NANG_CAP_DO_HD:
                return "vào hành trang\nChọn 3 trang bị thần linh bất kì" + "\nsẽ cho ra đồ hủy diệt từ 0-15% chỉ số" + "\nSau đó chọn 'Nâng Cấp'";
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
    //---Effect---

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
            msg.writer().writeByte(player.conbine.itemsCombine.size());
            for (Item it : player.conbine.itemsCombine) {
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
        } catch (Exception ignored) {
        }
    }

    public boolean checkInventory(Player player) {
        if (player.conbine.goldCombine > player.inventory.gold) {
            Service.gI().sendThongBaoOK(player, "Còn Thiếu " + Util.numberToMoney(player.conbine.goldCombine - player.inventory.gold) + " Vàng");
            return true;
        }
        if (player.conbine.rubyCombine > player.inventory.ruby) {
            Service.gI().sendThongBaoOK(player, "Còn Thiếu " + Util.numberToMoney(player.conbine.rubyCombine - player.inventory.ruby) + " Hồng Ngọc");
            return true;
        }
        if (player.conbine.gemCombine > player.inventory.gem) {
            Service.gI().sendThongBaoOK(player, "Còn Thiếu " + Util.numberToMoney(player.conbine.gemCombine - player.inventory.gem) + " Ngọc");
            return true;
        }
        if (InventoryService.gI().getCountEmptyBag(player) == 0) {
            Service.gI().sendThongBaoOK(player, "Hành Trang Cần Ít Nhất 1 Chỗ Trống");
            return true;
        }
        return false;
    }

}

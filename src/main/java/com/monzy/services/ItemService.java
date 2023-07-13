package com.monzy.services;

import com.monzy.models.Template;
import com.monzy.models.Template.ItemOptionTemplate;
import com.monzy.models.item.Item;
import com.monzy.models.item.Item.ItemOption;
import com.monzy.models.map.ItemMap;
import com.monzy.models.player.Player;
import com.monzy.models.shop.ItemShop;
import com.monzy.server.Manager;
import com.monzy.utils.TimeUtil;
import com.monzy.utils.Util;

import java.util.*;
import java.util.stream.Collectors;

public class ItemService {

    private static ItemService i;
    public static int BOSS_DROP = 1;
    public static int MOB_DROP = 2;

    public static ItemService gI() {
        if (i == null) {
            i = new ItemService();
        }
        return i;
    }

    public short getItemIdByIcon(short IconID) {
        for (int i = 0; i < Manager.ITEM_TEMPLATES.size(); i++) {
            if (Manager.ITEM_TEMPLATES.get(i).iconID == IconID) {
                return Manager.ITEM_TEMPLATES.get(i).id;
            }
        }
        return -1;
    }

    public Item createItemNull() {
        Item item = new Item();
        return item;
    }

    public Item createItemFromItemShop(ItemShop itemShop) {
        Item item = new Item();
        item.template = itemShop.temp;
        item.quantity = 1;
        item.content = item.getContent();
        item.info = item.getInfo();
        for (Item.ItemOption io : itemShop.options) {
            item.itemOptions.add(new Item.ItemOption(io));
        }
        return item;
    }

    public Item copyItem(Item item) {
        Item it = new Item();
        it.itemOptions = new ArrayList<>();
        it.template = item.template;
        it.info = item.info;
        it.content = item.content;
        it.quantity = item.quantity;
        it.createTime = item.createTime;
        for (Item.ItemOption io : item.itemOptions) {
            it.itemOptions.add(new Item.ItemOption(io));
        }
        return it;
    }

    public Item createNewItem(short tempId) {
        return createNewItem(tempId, 1);
    }

    public Item createNewItem(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item createItemFromItemMap(ItemMap itemMap) {
        Item item = createNewItem(itemMap.itemTemplate.id, itemMap.quantity);
        item.itemOptions = itemMap.options;
        return item;
    }

    public ItemOptionTemplate getItemOptionTemplate(int id) {
        return Manager.ITEM_OPTION_TEMPLATES.get(id);
    }

    public Template.ItemTemplate getTemplate(int id) {
        return Manager.ITEM_TEMPLATES.get(id);
    }

    public int getPercentTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                    return 10;
                case 530:
                case 535:
                    return 20;
                case 531:
                case 536:
                    return 30;
                default:
                    return 0;
            }
        } else {
            return 0;
        }
    }

    public boolean isTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                case 530:
                case 535:
                case 531:
                case 536:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean isOutOfDateTime(Item item) {
        if (item != null) {
            for (Item.ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 93) {
                    int dayPass = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
                    if (dayPass != 0) {
                        io.param -= dayPass;
                        if (io.param <= 0) {
                            return true;
                        } else {
                            item.createTime = System.currentTimeMillis();
                        }
                    }
                }
            }
        }
        return false;
    }

    public int randomSKHId(byte gender) {
        if (gender == 3) gender = 2;
        int[][] options = {{127, 128, 129}, {130, 131, 132}, {133, 134, 135}};
        int skhId = Util.nextInt(0, 2);
        return options[gender][skhId];
    }

    public void sendDKH(Player player, int itemUseId, int select) {
        if (select < 0 || select > 4) return;
        Item itemUse = InventoryService.gI().findItem(player.inventory.itemsBag, itemUseId);
        int[][] items = {{0, 6, 21, 27, 12}, {1, 7, 22, 28, 12}, {2, 8, 23, 29, 12}};
        int[][] options = {{127, 128, 129}, {130, 131, 132}, {133, 134, 135}};
        int skhId = Util.nextInt(0, 2);
        Item item = null;
        switch (itemUseId) {
            case 2000:
                item = newItemSKH(items[0][select], options[0][skhId]);
                break;
            case 2001:
                item = newItemSKH(items[1][select], options[1][skhId]);
                break;
            case 2002:
                item = newItemSKH(items[2][select], options[2][skhId]);
                break;
        }
        if (item != null && InventoryService.gI().getCountEmptyBag(player) > 0) {
            InventoryService.gI().addItemBag(player, item);
            InventoryService.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            InventoryService.gI().subQuantityItemsBag(player, itemUse, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void sendDHD(Player player, int itemUseId, int select) {
        if (select < 0 || select > 4) return;
        Item itemUse = InventoryService.gI().findItem(player.inventory.itemsBag, itemUseId);
        int gender = -1;
        switch (itemUseId) {
            case 2003: //td
                gender = 0;
                break;
            case 2004: //xd
                gender = 2;
                break;
            case 2005: //nm
                gender = 1;
                break;
        }
        int[][] items = {{650, 651, 657, 658, 656}, {652, 653, 659, 660, 656}, {654, 655, 661, 662, 656}}; //td, namec,xd
        Item item = randomCSDHD(items[gender][select], gender);
        if (item != null && InventoryService.gI().getCountEmptyBag(player) > 0) {
            InventoryService.gI().addItemBag(player, item);
            InventoryService.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            InventoryService.gI().subQuantityItemsBag(player, itemUse, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void OpenItem736(Player player, Item itemUse) {
        try {
            if (InventoryService.gI().getCountEmptyBag(player) <= 1) {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 2 ô trống hành trang");
                return;
            }
            short[] icon = new short[2];
            int rd = Util.nextInt(1, 100);
            int rac = 50;
            int ruby = 20;
            int dbv = 10;
            int vb = 10;
            int bh = 5;
            int ct = 5;
            Item item = randomNgocRong();
            if (rd <= rac) {
                item = randomNgocRong();
            } else if (rd <= rac + ruby) {
                item = Manager.RUBY_REWARDS.get(Util.nextInt(0, Manager.RUBY_REWARDS.size() - 1));
            } else if (rd <= rac + ruby + dbv) {
                item = daBaoVe();
            } else if (rd <= rac + ruby + dbv + vb) {
                item = vanBay2011(true);
            } else if (rd <= rac + ruby + dbv + vb + bh) {
                item = phuKien2011(true);
            } else if (rd <= rac + ruby + dbv + vb + bh + ct) {
                item = caitrang2011(true);
            }
            if (item.template.id == 861) {
                item.quantity = Util.nextInt(10, 30);
            }
            icon[0] = itemUse.template.iconID;
            icon[1] = item.template.iconID;
            InventoryService.gI().subQuantityItemsBag(player, itemUse, 1);
            InventoryService.gI().addItemBag(player, item);
            InventoryService.gI().sendItemBags(player);
            player.inventory.event++;
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            CombineService.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendSetTSKichHoat(Player player, int idSKH) {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1105);
        Item ao = ItemService.gI().randomCSDTS(Manager.IDS_DO_THIEN_SU[player.gender][0], player.gender);
        Item quan = ItemService.gI().randomCSDTS(Manager.IDS_DO_THIEN_SU[player.gender][1], player.gender);
        Item gang = ItemService.gI().randomCSDTS(Manager.IDS_DO_THIEN_SU[player.gender][2], player.gender);
        Item giay = ItemService.gI().randomCSDTS(Manager.IDS_DO_THIEN_SU[player.gender][3], player.gender);
        Item nhan = ItemService.gI().randomCSDTS(Manager.IDS_DO_THIEN_SU[player.gender][4], player.gender);
        ao.itemOptions.add(new Item.ItemOption(idSKH, 0));
        quan.itemOptions.add(new Item.ItemOption(idSKH, 0));
        gang.itemOptions.add(new Item.ItemOption(idSKH, 0));
        giay.itemOptions.add(new Item.ItemOption(idSKH, 0));
        nhan.itemOptions.add(new Item.ItemOption(idSKH, 0));
        ao.itemOptions.add(new Item.ItemOption(idSKH + 9, 0));
        quan.itemOptions.add(new Item.ItemOption(idSKH + 9, 0));
        gang.itemOptions.add(new Item.ItemOption(idSKH + 9, 0));
        giay.itemOptions.add(new Item.ItemOption(idSKH + 9, 0));
        nhan.itemOptions.add(new Item.ItemOption(idSKH + 9, 0));
        ao.itemOptions = ao.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id != 30).collect(Collectors.toList());
        ao.itemOptions = ao.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id != 21).collect(Collectors.toList());
        ao.itemOptions.add(new Item.ItemOption(21, 120));
        ao.itemOptions.add(new Item.ItemOption(30, 0));
        quan.itemOptions = quan.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id != 30).collect(Collectors.toList());
        quan.itemOptions = quan.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id != 21).collect(Collectors.toList());
        quan.itemOptions.add(new Item.ItemOption(21, 120));
        quan.itemOptions.add(new Item.ItemOption(30, 0));
        gang.itemOptions = gang.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id != 30).collect(Collectors.toList());
        gang.itemOptions = gang.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id != 21).collect(Collectors.toList());
        gang.itemOptions.add(new Item.ItemOption(21, 120));
        gang.itemOptions.add(new Item.ItemOption(30, 0));
        giay.itemOptions = giay.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id != 30).collect(Collectors.toList());
        giay.itemOptions = giay.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id != 21).collect(Collectors.toList());
        giay.itemOptions.add(new Item.ItemOption(21, 120));
        giay.itemOptions.add(new Item.ItemOption(30, 0));
        nhan.itemOptions = nhan.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id != 30).collect(Collectors.toList());
        nhan.itemOptions = nhan.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id != 21).collect(Collectors.toList());
        nhan.itemOptions.add(new Item.ItemOption(21, 120));
        nhan.itemOptions.add(new Item.ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 5) {
            InventoryService.gI().addItemBag(player, ao);
            InventoryService.gI().addItemBag(player, quan);
            InventoryService.gI().addItemBag(player, gang);
            InventoryService.gI().addItemBag(player, giay);
            InventoryService.gI().addItemBag(player, nhan);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public Item newItemSKH(int itemId, int skhId) {
        Item item = createNewItem((short) itemId);
        item.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) itemId));
        item.itemOptions.add(new ItemOption(skhId, 1));
        item.itemOptions.add(new ItemOption(skhId + 9, 1));
        item.itemOptions.add(new ItemOption(30, 1));
        return item;
    }

    public int randomStarModReward() {
        return Util.nextInt(0, 3);
    }

    public int randomStarBossReward() {
        if (Util.isTrue(5, 100)) {
            return 6;
        } else if (Util.isTrue(10, 100)) {
            return Util.nextInt(4, 5);
        } else {
            return Util.nextInt(0, 3);
        }
    }

    public Item randomCSDTL(int itemId, int typeDrop) {
        Item item = createNewItem((short) itemId);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains(itemId)) {
            item.itemOptions.add(new Item.ItemOption(47, highlightsItem(item.template.gender == 2, Util.nextInt(1000, 1500))));
        }
        if (quan.contains(itemId)) {
            item.itemOptions.add(new Item.ItemOption(22, highlightsItem(item.template.gender == 0, Util.nextInt(45, 60))));
        }
        if (gang.contains(itemId)) {
            item.itemOptions.add(new Item.ItemOption(0, highlightsItem(item.template.gender == 2, Util.nextInt(3500, 4500))));
        }
        if (giay.contains(itemId)) {
            item.itemOptions.add(new Item.ItemOption(23, highlightsItem(item.template.gender == 1, Util.nextInt(45, 60))));
        }
        if (ntl == itemId) {
            item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(15, 18)));
        }
        if (typeDrop == BOSS_DROP)
            item.itemOptions.add(new Item.ItemOption(209, 1));
        item.itemOptions.add(new Item.ItemOption(21, 18));
        item.itemOptions.add(new Item.ItemOption(107, typeDrop == BOSS_DROP ? randomStarBossReward() : randomStarModReward()));
        return item;
    }

    public Item randomCSDHD(int itemId, int gender) {
        Item item = createNewItem((short) itemId);
        List<Integer> ao = Arrays.asList(650, 652, 654);
        List<Integer> quan = Arrays.asList(651, 653, 655);
        List<Integer> gang = Arrays.asList(657, 659, 661);
        List<Integer> giay = Arrays.asList(658, 660, 662);
        int nhd = 656;
        if (ao.contains(itemId)) {
            item.itemOptions.add(new Item.ItemOption(47, highlightsItem(gender == 2, Util.nextInt(2000, 2500))));
        }
        if (quan.contains(itemId)) {
            item.itemOptions.add(new Item.ItemOption(22, highlightsItem(gender == 0, Util.nextInt(75, 90))));
        }
        if (gang.contains(itemId)) {
            item.itemOptions.add(new Item.ItemOption(0, highlightsItem(gender == 2, Util.nextInt(5500, 6500))));
        }
        if (giay.contains(itemId)) {
            item.itemOptions.add(new Item.ItemOption(23, highlightsItem(gender == 1, Util.nextInt(75, 90))));
        }
        if (nhd == itemId) {
            item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(19, 22)));
        }
        item.itemOptions.add(new Item.ItemOption(21, 80));
        item.itemOptions.add(new Item.ItemOption(30, 1));
        return item;
    }

    public Item randomCSDTS(int itemId, int gender) {
        Item dots = createNewItem((short) itemId);
        List<Integer> ao = Arrays.asList(1048, 1049, 1050);
        List<Integer> quan = Arrays.asList(1051, 1052, 1053);
        List<Integer> gang = Arrays.asList(1054, 1055, 1056);
        List<Integer> giay = Arrays.asList(1057, 1058, 1059);
        List<Integer> nhan = Arrays.asList(1060, 1061, 1062);
        if (ao.contains(itemId)) {
            dots.itemOptions.add(new Item.ItemOption(47, highlightsItem(gender == 2, Util.nextInt(3000, 3500))));
        }
        if (quan.contains(itemId)) {
            dots.itemOptions.add(new Item.ItemOption(22, highlightsItem(gender == 0, Util.nextInt(105, 120))));
        }
        if (gang.contains(itemId)) {
            dots.itemOptions.add(new Item.ItemOption(0, highlightsItem(gender == 2, Util.nextInt(7500, 8500))));
        }
        if (giay.contains(itemId)) {
            dots.itemOptions.add(new Item.ItemOption(23, highlightsItem(gender == 1, Util.nextInt(105, 120))));
        }
        if (nhan.contains(itemId)) {
            dots.itemOptions.add(new Item.ItemOption(14, highlightsItem(gender == 1, Util.nextInt(23, 26))));
        }
        dots.itemOptions.add(new Item.ItemOption(21, 120));
        dots.itemOptions.add(new Item.ItemOption(30, 1));
        return dots;
    }

    //Cải trang sự kiện 20/11
    public Item caitrang2011(boolean rating) {
        Item item = createNewItem((short) 680);
        item.itemOptions.add(new Item.ItemOption(76, 1));//VIP
        item.itemOptions.add(new Item.ItemOption(77, 28));//hp 28%
        item.itemOptions.add(new Item.ItemOption(103, 25));//ki 25%
        item.itemOptions.add(new Item.ItemOption(147, 24));//sd 26%
        item.itemOptions.add(new Item.ItemOption(117, 18));//Đẹp + 18% sd
        if (Util.isTrue(995, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }

    //610 - bong hoa
    //Phụ kiện bó hoa 20/11
    public Item phuKien2011(boolean rating) {
        Item item = createNewItem((short) 954);
        item.itemOptions.add(new Item.ItemOption(77, new Random().nextInt(5) + 5));
        item.itemOptions.add(new Item.ItemOption(103, new Random().nextInt(5) + 5));
        item.itemOptions.add(new Item.ItemOption(147, new Random().nextInt(5) + 5));
        if (Util.isTrue(1, 100)) {
            item.itemOptions.get(Util.nextInt(item.itemOptions.size() - 1)).param = 10;
        }
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        if (Util.isTrue(995, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }

    public Item vanBay2011(boolean rating) {
        Item item = createNewItem((short) 795);
        item.itemOptions.add(new Item.ItemOption(89, 1));
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        if (Util.isTrue(950, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }

    public Item daBaoVe() {
        Item item = createNewItem((short) 987);
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        return item;
    }

    public Item randomNgocRong() {
        short[] racs = {20, 19, 18, 17};
        Item item = new Item(racs[Util.nextInt(racs.length - 1)]);
        if (optionDaNangCap(item.template.id) != 0) {
            item.itemOptions.add(new Item.ItemOption(optionDaNangCap(item.template.id), 1));
        }
        return item;
    }

    public byte optionDaNangCap(short itemId) {
        switch (itemId) {
            case 220:
                return 71;
            case 221:
                return 70;
            case 222:
                return 69;
            case 224:
                return 67;
            case 223:
                return 68;
            default:
                return 0;
        }
    }

    public void openBoxVip(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) <= 1) {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 2 ô trống hành trang");
            return;
        }
        if (player.inventory.event < 3000) {
            Service.gI().sendThongBao(player, "Bạn không đủ bông...");
            return;
        }
        Item item;
        if (Util.isTrue(45, 100)) {
            item = caitrang2011(false);
        } else {
            item = phuKien2011(false);
        }
        short[] icon = new short[2];
        icon[0] = 6983;
        icon[1] = item.template.iconID;
        InventoryService.gI().addItemBag(player, item);
        InventoryService.gI().sendItemBags(player);
        player.inventory.event -= 3000;
        Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
        CombineService.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    }

    public void giaobong(Player player, int quantity) {
        if (quantity > 10000) return;
        try {
            Item itemUse = InventoryService.gI().findItem(player.inventory.itemsBag, 610);
            if (itemUse.quantity < quantity) {
                Service.gI().sendThongBao(player, "Bạn không đủ bông...");
                return;
            }
            InventoryService.gI().subQuantityItemsBag(player, itemUse, quantity);
            Item item = createNewItem((short) 736);
            item.quantity = (quantity / 100);
            item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
            InventoryService.gI().addItemBag(player, item);
            InventoryService.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được x" + (quantity / 100) + " " + item.template.name);
        } catch (Exception e) {
            Service.gI().sendThongBao(player, "Bạn không đủ bông...");
        }
    }

    public Item PK_WC(int itemId) {
        Item phukien = createNewItem((short) itemId);
        int co = 983;
        int cup = 982;
        int bong = 966;
        if (cup == itemId) {
            phukien.itemOptions.add(new Item.ItemOption(77, new Random().nextInt(6) + 5)); // hp 5-10%
        }
        if (co == itemId) {
            phukien.itemOptions.add(new Item.ItemOption(103, new Random().nextInt(6) + 5)); // ki 5-10%
        }
        if (bong == itemId) {
            phukien.itemOptions.add(new Item.ItemOption(50, new Random().nextInt(6) + 5)); // sd 5- 10%
        }
        phukien.itemOptions.add(new Item.ItemOption(192, 1));//WORLDCUP
        phukien.itemOptions.add(new Item.ItemOption(193, 1));//(2 món kích hoạt ....)
        if (Util.isTrue(99, 100)) {// tỉ lệ ra hsd
            phukien.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(2) + 1));//hsd
        }
        return phukien;
    }

    //Cải trang Gohan WC
    public Item CT_WC(boolean rating) {
        Item caitrang = createNewItem((short) 883);
        caitrang.itemOptions.add(new Item.ItemOption(77, 30));// hp 30%
        caitrang.itemOptions.add(new Item.ItemOption(103, 15));// ki 15%
        caitrang.itemOptions.add(new Item.ItemOption(50, 20));// sd 20%
        caitrang.itemOptions.add(new Item.ItemOption(192, 1));//WORLDCUP
        caitrang.itemOptions.add(new Item.ItemOption(193, 1));//(2 món kích hoạt ....)
        if (Util.isTrue(99, 100) && rating) {// tỉ lệ ra hsd
            caitrang.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(2) + 1));//hsd
        }
        return caitrang;
    }

    public List<Item.ItemOption> getListOptionItemShop(short id) {
        List<Item.ItemOption> list = new ArrayList<>();
        Manager.SHOPS.forEach(shop -> shop.tabShops.forEach(tabShop -> tabShop.itemShops.forEach(itemShop -> {
            if (itemShop.temp.id == id && list.size() == 0) {
                list.addAll(itemShop.options);
            }
        })));
        return list;
    }

    public int highlightsItem(boolean highlights, int value) {
        double highlightsNumber = 1.1;
        return highlights ? (int) (value * highlightsNumber) : value;
    }

    // Random Sđ Hp Ki 10 - 15%
    public Item randomItemEvent(short idItem) {
        Item item = createNewItem(idItem);
        item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10, 15)));// sd 10 - 15%
        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 15)));// hp 10 - 15%
        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 15)));// ki 10 - 15%
        if (Util.isTrue(99, 100)) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(30) + 30));//hsd
        }
        return item;
    }
}

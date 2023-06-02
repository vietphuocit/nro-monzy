package com.monzy.models.mob;

import com.monzy.consts.ConstMap;
import com.monzy.consts.ConstMob;
import com.monzy.consts.ConstTask;
import com.monzy.models.item.Item;
import com.monzy.models.map.ItemMap;
import com.monzy.models.map.Zone;
import com.monzy.models.player.Location;
import com.monzy.models.player.Pet;
import com.monzy.models.player.Player;
import com.monzy.models.reward.ItemMobReward;
import com.monzy.models.reward.MobReward;
import com.monzy.models.shop.ItemShop;
import com.monzy.models.shop.Shop;
import com.monzy.models.shop.TabShop;
import com.monzy.server.Maintenance;
import com.monzy.server.Manager;
import com.monzy.services.*;
import com.monzy.utils.Util;
import com.network.io.Message;

import javax.swing.text.TabableView;
import java.util.*;
import java.util.stream.Stream;

public class Mob {

    public int id;
    public Zone zone;
    public int tempId;
    public String name;
    public byte level;
    public MobPoint point;
    public MobEffectSkill effectSkill;
    public Location location;
    public byte pDame;
    public int pTiemNang;
    private long maxTiemNang;
    public long lastTimeDie;
    public int lvMob = 0;
    public int status = 5;

    public Mob(Mob mob) {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
        this.id = mob.id;
        this.tempId = mob.tempId;
        this.level = mob.level;
        this.point.setHpFull(mob.point.getHpFull());
        this.point.sethp(this.point.getHpFull());
        this.location.x = mob.location.x;
        this.location.y = mob.location.y;
        this.pDame = mob.pDame;
        this.pTiemNang = mob.pTiemNang;
        this.setTiemNang();
    }

    public Mob() {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
    }

    public void setTiemNang() {
        this.maxTiemNang = (long) this.point.getHpFull() * (this.pTiemNang + Util.nextInt(-2, 2)) / 100;
    }

    private long lastTimeAttackPlayer;

    public boolean isDie() {
        return this.point.gethp() <= 0;
    }

    public synchronized void injured(Player plAtt, int damage, boolean dieWhenHpFull) {
        if (!this.isDie()) {
            if (damage >= this.point.hp) {
                damage = this.point.hp;
            }
            if (!dieWhenHpFull) {
                if (this.point.hp == this.point.maxHp && damage >= this.point.hp) {
                    damage = this.point.hp - 1;
                }
                if (this.tempId == 0 && damage > 10) {
                    damage = 10;
                }
            }
            this.point.hp -= damage;
            if (this.isDie()) {
                this.status = 0;
                this.sendMobDieAffterAttacked(plAtt, damage);
                TaskService.gI().checkDoneTaskKillMob(plAtt, this);
                TaskService.gI().checkDoneSideTaskKillMob(plAtt, this);
                this.lastTimeDie = System.currentTimeMillis();
            } else {
                this.sendMobStillAliveAffterAttacked(damage, plAtt != null && plAtt.nPoint.isCrit);
            }
            if (plAtt != null) {
                Service.gI().addSMTN(plAtt, (byte) 2, getTiemNangForPlayer(plAtt, damage), true);
            }
        }
    }

    public long getTiemNangForPlayer(Player pl, long dame) {
        if (dame == 0)
            return (int) pl.nPoint.calSucManhTiemNang(1);
        int levelPlayer = Service.gI().getCurrLevel(pl);
        int n = levelPlayer - this.level;
        long pDameHit = dame * 100 / point.getHpFull();
        long tiemNang = pDameHit * maxTiemNang / 100;
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        if (n >= 0) {
            for (int i = 0; i < n; i++) {
                long sub = tiemNang * 10 / 100;
                if (sub <= 0) {
                    sub = 1;
                }
                tiemNang -= sub;
            }
        } else {
            for (int i = 0; i < -n; i++) {
                long add = tiemNang * 10 / 100;
                if (add <= 0) {
                    add = 1;
                }
                tiemNang += add;
            }
        }
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        tiemNang = (int) pl.nPoint.calSucManhTiemNang(tiemNang);
        return tiemNang;
    }

    public void update() {
        if (this.tempId == 71) {
            try {
                Message msg = new Message(102);
                msg.writer().writeByte(5);
                msg.writer().writeShort(this.zone.getPlayers().get(0).location.x);
                Service.gI().sendMessAllPlayerInMap(zone, msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
        if (this.isDie() && !Maintenance.isRuning) {
            switch (zone.map.type) {
                case ConstMap.MAP_DOANH_TRAI:
                    break;
                default:
                    if (Util.canDoWithTime(lastTimeDie, 5000)) {
                        this.hoiSinh();
                        this.sendMobHoiSinh();
                    }
            }
        }
        effectSkill.update();
        attackPlayer();
    }

    private void attackPlayer() {
        if (!isDie() && !effectSkill.isHaveEffectSkill() && !(tempId == 0) && Util.canDoWithTime(lastTimeAttackPlayer, 2000)) {
            Player pl = getPlayerCanAttack();
            if (pl != null) {
//                MobService.gI().mobAttackPlayer(this, pl);
                this.mobAttackPlayer(pl);
            }
            this.lastTimeAttackPlayer = System.currentTimeMillis();
        }
    }

    private Player getPlayerCanAttack() {
        int distance = 100;
        Player plAttack = null;
        try {
            List<Player> players = this.zone.getNotBosses();
            for (Player pl : players) {
                if (!pl.isDie() && !pl.isBoss && !pl.effectSkin.isVoHinh && !pl.isNewPet) {
                    int dis = Util.getDistance(pl, this);
                    if (dis <= distance) {
                        plAttack = pl;
                        distance = dis;
                    }
                }
            }
        } catch (Exception e) {
        }
        return plAttack;
    }

    //**************************************************************************
    private void mobAttackPlayer(Player player) {
        int dameMob = this.point.getDameAttack();
        if (player.charms.tdDaTrau > System.currentTimeMillis()) {
            dameMob /= 2;
        }
        int dame = player.injured(null, dameMob, false, true);
        this.sendMobAttackMe(player, dame);
        this.sendMobAttackPlayer(player);
    }

    private void sendMobAttackMe(Player player, int dame) {
        if (!player.isPet && !player.isNewPet) {
            Message msg;
            try {
                msg = new Message(-11);
                msg.writer().writeByte(this.id);
                msg.writer().writeInt(dame); //dame
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    private void sendMobAttackPlayer(Player player) {
        Message msg;
        try {
            msg = new Message(-10);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeInt(player.nPoint.hp);
            Service.gI().sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void hoiSinh() {
        this.status = 5;
        this.point.hp = this.point.maxHp;
        this.setTiemNang();
    }

    private void sendMobHoiSinh() {
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(this.id);
            msg.writer().writeByte(this.tempId);
            msg.writer().writeByte(lvMob);
            msg.writer().writeInt(this.point.hp);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //**************************************************************************
    private void sendMobDieAffterAttacked(Player plKill, int dameHit) {
        Message msg;
        try {
            msg = new Message(-12);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(dameHit);
            msg.writer().writeBoolean(plKill.nPoint.isCrit); // crit
            List<ItemMap> items = mobReward(plKill, this.dropItemTask(plKill), msg);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
            hutItem(plKill, items);
        } catch (Exception e) {
        }
    }

    private void hutItem(Player player, List<ItemMap> items) {
        if (!player.isPet && !player.isNewPet) {
            if (player.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    if (item.itemTemplate.id != 590) {
                        ItemMapService.gI().pickItem(player, item.itemMapId, true);
                    }
                }
            }
        } else {
            if (((Pet) player).master.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    if (item.itemTemplate.id != 590) {
                        ItemMapService.gI().pickItem(((Pet) player).master, item.itemMapId, true);
                    }
                }
            }
        }
    }

    private List<ItemMap> mobReward(Player player, ItemMap itemTask, Message msg) {
//        nplayer
        List<ItemMap> itemReward = new ArrayList<>();
        try {
            if ((!player.isPet && player.getSession().actived && player.setClothes.setGOD == 5) || (player.isPet && ((Pet) player).master.getSession().actived && player.setClothes.setGOD == 5)) {
                short randomDoAn;
                randomDoAn = Manager.doAn[Util.nextInt(0, 4)];
                if (Util.nextInt(0, 100) < 7) {
                    Item DoAn = ItemService.gI().createNewItem(randomDoAn);
                    InventoryServiceNew.gI().addItemBag(player, DoAn);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendThongBao(player, "Bạn vừa nhận được " + DoAn.template.name);
                }
            }
            itemReward = this.getItemMobReward(player, this.location.x + Util.nextInt(-10, 10), this.zone.map.yPhysicInTop(this.location.x, this.location.y));
            if (itemTask != null) {
                itemReward.add(itemTask);
            }
            msg.writer().writeByte(itemReward.size()); //sl item roi
            for (ItemMap itemMap : itemReward) {
                msg.writer().writeShort(itemMap.itemMapId);// itemmapid
                msg.writer().writeShort(itemMap.itemTemplate.id); // id item
                msg.writer().writeShort(itemMap.x); // xend item
                msg.writer().writeShort(itemMap.y); // yend item
                msg.writer().writeInt((int) itemMap.playerId); // id nhan nat
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemReward;
    }

    public List<Integer> itemLevel1IDs = Arrays.asList(27, 28, 29, 6, 7, 8, 0, 1, 2, 12, 21, 22, 23, 30, 47, 55, 35, 43, 51, 33, 41, 49, 57, 24, 46, 53);

    public List<ItemShop> getIDsClothesBasic(int level, int gender) {
        if (level > 12)
            level = 12;
        List<ItemShop> itemShops = new ArrayList<>();
        int[] tabIDs = gender == 0 ? new int[]{1, 2} : gender == 1 ? new int[]{4, 5} : new int[]{7, 8};
        Shop shopClothes = Manager.SHOPS.stream().filter(shop -> shop.npcId == 7 + gender).findFirst().get();
        for (int i = 0; i < tabIDs.length; i++) {
            int finalI = i;
            TabShop tabShopClothes = shopClothes.tabShops.stream().filter(tabShop -> tabShop.id == tabIDs[finalI]).findFirst().get();
            itemShops.add(tabShopClothes.itemShops.get(-1 + level));
            itemShops.add(tabShopClothes.itemShops.get(11 + level));
            if (tabShopClothes.itemShops.size() > 24) {
                itemShops.add(tabShopClothes.itemShops.get(23 + level));
            }
        }
        return itemShops;
    }

    public ItemMap convertToItemMap(Zone zone, ItemShop itemShop, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, itemShop.temp.id, quantity, x, y, playerId);
        it.options = itemShop.options;
        it.options.add(new Item.ItemOption(107, Util.randomStar()));
        return it;
    }

    public List<ItemMap> getItemMobReward(Player player, int x, int yEnd) {
        List<ItemMap> list = new ArrayList<>();
        // đồ thường
        if (Util.isTrue(2, 100) && level < 19) {
            ItemShop itemShop = getIDsClothesBasic(level, player.gender).stream().skip((long) (5 * Math.random())).findFirst().get();
            list.add(convertToItemMap(zone, itemShop, 1, x, player.location.y, player.id));
        }
        // đồ thần linh
        if (MapService.gI().isMapCold(this.zone.map) && Util.isTrue(1, 2000)) {
            list.add(Util.randomClothesGod(zone, Manager.ID_CLOTHES_GOD[Util.nextInt(Manager.ID_CLOTHES_GOD.length)], 1, x, player.location.y, player.id, Util.MOB_DROP));
        }
        // thức ăn
        if (MapService.gI().isMapCold(this.zone.map) && player.setClothes.godClothes && Util.isTrue(5, 100)) {
            list.add(new ItemMap(zone, Util.nextInt(663, 667), 1, x, player.location.y, player.id));
        }
        // cskb
        if (player.itemTime.isUseMayDo && MapService.gI().isMapFuture(this.zone.map.mapId) && Util.isTrue(20, 100) && this.tempId > 57 && this.tempId < 66) {
            list.add(new ItemMap(zone, 380, 1, x, player.location.y, player.id));
        }
        //
        if (player.itemTime.isUseMayDo2 && Util.isTrue(10, 100) && this.tempId > 1 && this.tempId < 81) {
            list.add(new ItemMap(zone, 2036, 1, x, player.location.y, player.id));
        }
        // up hồng ngọc bdkb
        if (MapService.gI().isMapBanDoKhoBau(player.zone.map.mapId) && Util.isTrue(50, 100)) {
            list.add(new ItemMap(zone, 861, Util.nextInt(10, 100), x, player.location.y, player.id));
        }
        // vàng
        int gold = this.level * Util.nextInt(400, 600);
        gold = Math.min(gold + gold * (player.nPoint.getTlGold() / 100), 30000);
        if (Util.isTrue(50, 100)) {
            list.add(new ItemMap(zone, 190, gold, x, player.location.y, player.id));
        }
        // đá nâng cấp
        int dnc = Util.nextInt(220, 224);
        if (MapService.gI().isMapDoanhTrai(player.zone.map.mapId) && Util.isTrue(50, 100)) {
            list.add(new ItemMap(zone, dnc, Util.nextInt(100, 200), x, player.location.y, player.id));
        } else if (Util.isTrue(10, 100)) {
            list.add(new ItemMap(zone, dnc, 1, x, player.location.y, player.id));
        }
        // sao pha lê
        int spl = Util.nextInt(441, 447);
        if (Util.isTrue(10, 100)) {
            list.add(new ItemMap(zone, spl, 1, x, player.location.y, player.id));
        }
        // ngọc rồng
        int ngocRong = Util.nextInt(17, 20);
        if (Util.isTrue(10, 100)) {
            list.add(new ItemMap(zone, ngocRong, 1, x, player.location.y, player.id));
        }
        // up đồng xu vàng
        if (Util.isTrue(3, 100)) {
            list.add(new ItemMap(zone, 1229, 1, x, player.location.y, player.id));
        }
//        if (player.isPet && player.getSession().actived && Util.isTrue(15, 100)) {
//            list.add(new ItemMap(zone, 610, 1, x, player.location.y, player.id));
//        }
        return list;
    }

    private ItemMap dropItemTask(Player player) {
        ItemMap itemMap = null;
        switch (this.tempId) {
            case ConstMob.KHUNG_LONG:
            case ConstMob.LON_LOI:
            case ConstMob.QUY_DAT:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_2_0) {
                    itemMap = new ItemMap(this.zone, 73, 1, this.location.x, this.location.y, player.id);
                }
                break;
        }
        return itemMap;
    }

    private void sendMobStillAliveAffterAttacked(int dameHit, boolean crit) {
        Message msg;
        try {
            msg = new Message(-9);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(this.point.gethp());
            msg.writer().writeInt(dameHit);
            msg.writer().writeBoolean(crit); // chí mạng
            msg.writer().writeInt(-1);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

}

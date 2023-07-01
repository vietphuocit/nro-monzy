package com.monzy.models.player;

import com.monzy.card.Card;
import com.monzy.consts.ConstPlayer;
import com.monzy.consts.ConstTask;
import com.monzy.data.DataGame;
import com.monzy.models.clan.Clan;
import com.monzy.models.clan.ClanMember;
import com.monzy.models.intrinsic.IntrinsicPlayer;
import com.monzy.models.item.Item;
import com.monzy.models.item.ItemTime;
import com.monzy.models.map.MapMaBu.MapMaBu;
import com.monzy.models.map.TrapMap;
import com.monzy.models.map.Zone;
import com.monzy.models.map.bdkb.Bdkb;
import com.monzy.models.map.blackball.BlackBallWar;
import com.monzy.models.map.nguhanhson.nguhs;
import com.monzy.models.matches.IPVP;
import com.monzy.models.matches.TYPE_LOSE_PVP;
import com.monzy.models.mob.MobMe;
import com.monzy.models.npc.specialnpc.BillEgg;
import com.monzy.models.npc.specialnpc.MabuEgg;
import com.monzy.models.npc.specialnpc.MagicTree;
import com.monzy.models.skill.PlayerSkill;
import com.monzy.models.skill.Skill;
import com.monzy.models.task.TaskPlayer;
import com.monzy.server.Client;
import com.monzy.server.io.MySession;
import com.monzy.services.*;
import com.monzy.services.ChangeMapService;
import com.monzy.services.func.ChonAiDay;
import com.monzy.services.func.Conbine;
import com.monzy.utils.Logger;
import com.monzy.utils.Util;
import com.network.io.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Player {

    private MySession session;
    public boolean beforeDispose;
    public boolean banv = false;
    public boolean muav = false;
    public boolean isPet;
    public boolean isNewPet;
    //    public boolean isNewPet1;
    public boolean isBoss;
    public int event;
    public IPVP pvp;
    public int pointPvp;
    public byte maxTime = 30;
    public byte type = 0;
    public int mapIdBeforeLogout;
    public List<Zone> mapBlackBall;
    public List<Zone> mapMaBu;
    public Zone zone;
    public Zone mapBeforeCapsule;
    public List<Zone> mapCapsule;
    public Pet pet;
    public NewPet newpet;
    //    public NewPet newpet1;
    public MobMe mobMe;
    public Location location;
    public SetClothes setClothes;
    public EffectSkill effectSkill;
    public MabuEgg mabuEgg;
    public BillEgg billEgg;
    public TaskPlayer playerTask;
    public ItemTime itemTime;
    public Fusion fusion;
    public MagicTree magicTree;
    public IntrinsicPlayer playerIntrinsic;
    public Inventory inventory;
    public PlayerSkill playerSkill;
    public Conbine conbine;
    public IDMark iDMark;
    public Charms charms;
    public EffectSkin effectSkin;
    public Gift gift;
    public NPoint nPoint;
    public RewardBlackBall rewardBlackBall;
    public EffectFlagBag effectFlagBag;
    public FightMabu fightMabu;
    public Clan clan;
    public ClanMember clanMember;
    public List<Friend> friends;
    public List<Enemy> enemies;
    public long id;
    public String name;
    public byte gender;
    public boolean isNewMember;
    public short head;
    public byte typePk;
    public byte cFlag;
    public long timeudbv = 0;
    public long timeudmv = 0;
    public boolean haveTennisSpaceShip;
    public long lasttimebanv;
    public long lasttimemuav;
    public boolean justRevived;
    public long lastTimeRevived;
    public long timeChangeZone;
    public long lastTimeUseOption;
    public short idNRNM = -1;
    public short idGo = -1;
    public long lastTimePickNRNM;
    public int goldNormar;
    public int goldVIP;
    public long lastTimeWin;
    public boolean isWin;
    public List<Card> cards = new ArrayList<>();
    public short idAura = -1;

    public Player() {
        lastTimeUseOption = System.currentTimeMillis();
        location = new Location();
        nPoint = new NPoint(this);
        inventory = new Inventory();
        playerSkill = new PlayerSkill(this);
        setClothes = new SetClothes(this);
        effectSkill = new EffectSkill(this);
        fusion = new Fusion(this);
        playerIntrinsic = new IntrinsicPlayer();
        rewardBlackBall = new RewardBlackBall(this);
        effectFlagBag = new EffectFlagBag();
        fightMabu = new FightMabu(this);
        //----------------------------------------------------------------------
        iDMark = new IDMark();
        conbine = new Conbine();
        playerTask = new TaskPlayer();
        friends = new ArrayList<>();
        enemies = new ArrayList<>();
        itemTime = new ItemTime(this);
        charms = new Charms();
        gift = new Gift(this);
        effectSkin = new EffectSkin(this);
    }

    //--------------------------------------------------------------------------
    public boolean isDie() {
        if (this.nPoint != null) {
            return this.nPoint.hp <= 0;
        }
        return true;
    }

    //--------------------------------------------------------------------------
    public void setSession(MySession session) {
        this.session = session;
    }

    public void sendMessage(Message msg) {
        if (this.session != null) {
            session.sendMessage(msg);
        }
    }

    public MySession getSession() {
        return this.session;
    }

    public boolean isPl() {
        return !isPet && !isBoss && !isNewPet;
    }

    public void update() {
        if (!this.beforeDispose) {
            try {
                if (!iDMark.isBan()) {
                    if (nPoint != null) {
                        nPoint.update();
                    }
                    if (fusion != null) {
                        fusion.update();
                    }
                    if (effectSkin != null) {
                        effectSkill.update();
                    }
                    if (mobMe != null) {
                        mobMe.update();
                    }
                    if (effectSkin != null) {
                        effectSkin.update();
                    }
                    if (pet != null) {
                        pet.update();
                    }
                    if (newpet != null) {
                        newpet.update();
                    }
//                    if (newpet1 != null) {
//                        newpet1.update();
//                    }
                    if (magicTree != null) {
                        magicTree.update();
                    }
                    if (itemTime != null) {
                        itemTime.update();
                    }
                    long now = System.currentTimeMillis();
                    if (banv && this != null && Util.canDoWithTime(lasttimebanv, 1000) && (now >= timeudbv + 1000)) {
                        banv(this);
                        timeudbv = System.currentTimeMillis();
                    }
                    if (muav && this != null && Util.canDoWithTime(lasttimemuav, 2000) && (now >= timeudmv + 10000)) {
                        muav(this);
                        timeudmv = System.currentTimeMillis();
                    }
                    nguhs.gI().update(this);
                    Bdkb.gI().update(this);
                    BlackBallWar.gI().update(this);
                    MapMaBu.gI().update(this);
                    if (!isBoss && this.iDMark.isGotoFuture() && Util.canDoWithTime(this.iDMark.getLastTimeGoToFuture(), 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 102, -1, Util.nextInt(60, 200));
                        this.iDMark.setGotoFuture(false);
                    }
                    if (this.iDMark.isGoToBDKB() && Util.canDoWithTime(this.iDMark.getLastTimeGoToBDKB(), 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 135, -1, 35);
                        this.iDMark.setGoToBDKB(false);
                    }
                    if (this.zone != null) {
                        TrapMap trap = this.zone.isInTrap(this);
                        if (trap != null) {
                            trap.doPlayer(this);
                        }
                    }
                    if (this.isPl() && this.inventory.itemsBody.get(7) != null) {
                        Item it = this.inventory.itemsBody.get(7);
                        if (it != null && it.isNotNullItem() && this.newpet == null) {
                            PetService.Pet2(this, it.template.head, it.template.body, it.template.leg);
                            Service.getInstance().point(this);
                        }
                    } else if (this.isPl() && newpet != null && !this.inventory.itemsBody.get(7).isNotNullItem()) {
                        newpet.dispose();
                        newpet = null;
                    }
                    if (this.isPl() && isWin && this.zone.map.mapId == 51 && Util.canDoWithTime(lastTimeWin, 2000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 52, 0, -1);
                        isWin = false;
                    }
                    if (location.lastTimeplayerMove < System.currentTimeMillis() - 30 * 60 * 1000) {
                        Client.gI().kickSession(getSession());
                    }
                } else {
                    if (Util.canDoWithTime(iDMark.getLastTimeBan(), 5000)) {
                        Client.gI().kickSession(session);
                    }
                }
            } catch (Exception e) {
                e.getStackTrace();
                Logger.logException(Player.class, e, "Lỗi tại player: " + this.name);
            }
        }
    }

    //--------------------------------------------------------------------------
    /*
     * {380, 381, 382}: ht lưỡng long nhất thể xayda trái đất
     * {383, 384, 385}: ht porata xayda trái đất
     * {391, 392, 393}: ht namếc
     * {870, 871, 872}: ht c2 trái đất
     * {873, 874, 875}: ht c2 namếc
     * {867, 878, 869}: ht c2 xayda
     * {2033,2034,2035}: ht c3 td
     * {2030,2031,2032}: ht c3 nm
     * {2027,2028,2029}: ht c3 xd*/
    private static final short[][] idOutfitFusion = {
            {380, 381, 382},    // thuong xd, td
            {383, 384, 385},    // porata xd, td
            {391, 392, 393},    // thuong, porata namec
            {870, 871, 872},    // porata 2 td
            {873, 874, 875},    // porata 2 nm
            {867, 868, 869},    // porata 2 xd
            {2048, 2049, 2050}, // porata 3 td
            {2051, 2052, 2053}, // porata 3 nm
            {2054, 2055, 2056}, // porata 3 xd
            {2057, 2058, 2059}, // porata 4 td
            {2060, 2061, 2062}, // porata 4 nm
            {2063, 2064, 2065}, // porata 4 xd
            {1080, 1081, 1082}, // porata 5 td
            {1086, 1087, 1088}, // porata 5 nm
            {1083, 1084, 1085}, // porata 5 xd
    };

    // Sua id vat pham muon co aura lai
    public byte getAura() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        Item item = this.inventory.itemsBody.get(5);
        if (!item.isNotNullItem()) {
            return -1;
        }
        if (item.template.id == 1121) {
            return 10;
        } else {
            return -1;
        }
    }

    // hieu ung theo set
    public byte getEffFront() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }

        List<Item> items = this.inventory.itemsBody.subList(0, 5);
        List<Item.ItemOption> itemOptions = items.stream()
                .map(item -> item.itemOptions.stream()
                        .filter(io -> io.optionTemplate.id == 72)
                        .findFirst()
                        .orElse(null))
                .collect(Collectors.toList());

        if (itemOptions.stream().anyMatch(Objects::isNull)) {
            return -1;
        }

        int minLevel = itemOptions.stream()
                .mapToInt(io -> io.param)
                .min()
                .orElse(-1);

        if (minLevel >= 8) {
            return 8;
        } else if (minLevel == 7) {
            return 7;
        } else if (minLevel == 6) {
            return 6;
        } else if (minLevel == 5) {
            return 5;
        } else if (minLevel == 4) {
            return 4;
        } else {
            return -1;
        }
    }

    public short getHead() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        }
        if (effectSkill != null && effectSkill.isSocola) {
            return 412;
        }
        if (isFusion()) {
            if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
                Item skin = inventory.itemsBody.get(5);
                if (isSkinFusion(skin)) {
                    return (short) skin.template.head;
                }
            }
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][0];
            }
            if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][0];
            }
            if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                return idOutfitFusion[3 + this.gender][0];
            }
            if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                return idOutfitFusion[6 + this.gender][0];
            }
            if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                return idOutfitFusion[9 + this.gender][0];
            }
            if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
                return idOutfitFusion[12 + this.gender][0];
            }
        }
        if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            Item skin = inventory.itemsBody.get(5);
            if (isSkinFusion(skin)) {
                return this.head;
            }
            if (skin.template.head != -1) {
                return (short) skin.template.head;
            }
        }
        return this.head;
    }

    public short getBody() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 193;
        }
        if (effectSkill != null && effectSkill.isSocola) {
            return 413;
        }
        if (isFusion()) {
            if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
                Item skin = inventory.itemsBody.get(5);
                if (isSkinFusion(skin)) {
                    return (short) skin.template.body;
                }
            }
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][1];
            }
            if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][1];
            }
            if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                return idOutfitFusion[3 + this.gender][1];
            }
            if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                return idOutfitFusion[6 + this.gender][1];
            }
            if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                return idOutfitFusion[9 + this.gender][1];
            }
            if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
                return idOutfitFusion[12 + this.gender][1];
            }
        }
        if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            Item skin = inventory.itemsBody.get(5);
            if (isSkinFusion(skin)) {
                return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
            }
            if (skin.template.body != -1) {
                return (short) skin.template.body;
            }
        }
        if (inventory != null && inventory.itemsBody.get(0).isNotNullItem()) {
            return inventory.itemsBody.get(0).template.part;
        }
        return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
    }

    public short getLeg() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 194;
        }
        if (effectSkill != null && effectSkill.isSocola) {
            return 414;
        }
        if (isFusion()) {
            if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
                Item skin = inventory.itemsBody.get(5);
                if (isSkinFusion(skin)) {
                    return (short) skin.template.leg;
                }
            }
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][2];
            }
            if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][2];
            }
            if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                return idOutfitFusion[3 + this.gender][2];
            }
            if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                return idOutfitFusion[6 + this.gender][2];
            }
            if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                return idOutfitFusion[9 + this.gender][2];
            }
            if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
                return idOutfitFusion[12 + this.gender][2];
            }
        }
        if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            Item skin = inventory.itemsBody.get(5);
            if (isSkinFusion(skin)) {
                return (short) (gender == ConstPlayer.NAMEC ? 60 : 58);
            }
            if (skin.template.leg != -1) {
                return (short) skin.template.leg;
            }
        }
        if (inventory != null && inventory.itemsBody.get(1).isNotNullItem()) {
            return inventory.itemsBody.get(1).template.part;
        }
        return (short) (gender == 1 ? 60 : 58);
    }

    public short getFlagBag() {
        if (this.iDMark.isHoldBlackBall()) {
            return 31;
        }
        if (this.idNRNM >= 353 && this.idNRNM <= 359) {
            return 30;
        }
        if (TaskService.gI().getIdTask(this) == ConstTask.TASK_3_2) {
            return 28;
        }
        if (this.inventory.itemsBody.size() > 8) {
            Item flagBag = inventory.itemsBody.get(8);
            if (flagBag.isNotNullItem()) {
                return flagBag.template.part;
            }
        }
        if (this.clan != null) {
            return (short) this.clan.imgId;
        }
        return -1;
    }

    public short getMount() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        Item item = this.inventory.itemsBody.get(9);
        if (!item.isNotNullItem()) {
            return -1;
        }
        if (item.template.type == 24) {
            if (item.template.gender == 3 || item.template.gender == this.gender) {
                return item.template.id;
            } else {
                return -1;
            }
        } else {
            if (item.template.id < 500) {
                return item.template.id;
            } else {
                return (short) DataGame.MAP_MOUNT_NUM.get(String.valueOf(item.template.id));
            }
        }
    }

    //--------------------------------------------------------------------------
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                    case Skill.MASENKO:
                    case Skill.ANTOMIC:
                        if (this.nPoint.voHieuChuong > 0) {
                            com.monzy.services.PlayerService.gI().hoiPhuc(this, 0, damage * this.nPoint.voHieuChuong / 100);
                            return 0;
                        }
                }
            }
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (isMobAttack && this.charms.tdBatTu > System.currentTimeMillis() && damage >= this.nPoint.hp) {
                damage = this.nPoint.hp - 1;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                if (this.zone.map.mapId == 112) {
                    plAtt.pointPvp++;
                }
                setDie(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    protected void setDie(Player plAtt) {
        //xóa phù
        if (this.effectSkin.xHPKI > 1) {
            this.effectSkin.xHPKI = 1;
            Service.gI().point(this);
        }
        //xóa tụ skill đặc biệt
        this.playerSkill.prepareQCKK = false;
        this.playerSkill.prepareLaze = false;
        this.playerSkill.prepareTuSat = false;
        //xóa hiệu ứng skill
        this.effectSkill.removeSkillEffectWhenDie();
        //
        nPoint.setHp(0);
        nPoint.setMp(0);
        //xóa trứng
        if (this.mobMe != null) {
            this.mobMe.mobMeDie();
        }
        Service.gI().charDie(this);
        //add kẻ thù
        if (!this.isPet && !this.isNewPet && !this.isBoss && plAtt != null && !plAtt.isPet && !plAtt.isNewPet && !plAtt.isBoss) {
            if (!plAtt.itemTime.isUseAnDanh) {
                FriendAndEnemyService.gI().addEnemy(this, plAtt);
            }
        }
        //kết thúc pk
        if (this.pvp != null) {
            this.pvp.lose(this, TYPE_LOSE_PVP.DEAD);
        }
//        PVPServcice.gI().finishPVP(this, PVP.TYPE_DIE);
        BlackBallWar.gI().dropBlackBall(this);
    }

    //--------------------------------------------------------------------------
    public void setClanMember() {
        if (this.clanMember != null) {
            this.clanMember.powerPoint = this.nPoint.power;
            this.clanMember.head = this.getHead();
            this.clanMember.body = this.getBody();
            this.clanMember.leg = this.getLeg();
        }
    }

    public boolean isAdmin() {
        return this.session.isAdmin;
    }

    public boolean isMod() {
        return this.session.isMod;
    }

    public void setJustRevivaled() {
        this.justRevived = true;
        this.lastTimeRevived = System.currentTimeMillis();
    }

    public void preparedToDispose() {
    }

    public void dispose() {
        if (pet != null) {
            pet.dispose();
            pet = null;
        }
        if (newpet != null) {
            newpet.dispose();
            newpet = null;
        }
//        if (newpet1 != null) {
//            newpet1.dispose();
//            newpet1 = null;
//        }
        if (mapBlackBall != null) {
            mapBlackBall.clear();
            mapBlackBall = null;
        }
        zone = null;
        mapBeforeCapsule = null;
        if (mapMaBu != null) {
            mapMaBu.clear();
            mapMaBu = null;
        }
        if (billEgg != null) {
            billEgg.dispose();
            billEgg = null;
        }
        zone = null;
        mapBeforeCapsule = null;
        if (mapCapsule != null) {
            mapCapsule.clear();
            mapCapsule = null;
        }
        if (mobMe != null) {
            mobMe.dispose();
            mobMe = null;
        }
        location = null;
        if (setClothes != null) {
            setClothes.dispose();
            setClothes = null;
        }
        if (effectSkill != null) {
            effectSkill.dispose();
            effectSkill = null;
        }
        if (mabuEgg != null) {
            mabuEgg.dispose();
            mabuEgg = null;
        }
        if (playerTask != null) {
            playerTask.dispose();
            playerTask = null;
        }
        if (itemTime != null) {
            itemTime.dispose();
            itemTime = null;
        }
        if (fusion != null) {
            fusion.dispose();
            fusion = null;
        }
        if (magicTree != null) {
            magicTree.dispose();
            magicTree = null;
        }
        if (playerIntrinsic != null) {
            playerIntrinsic.dispose();
            playerIntrinsic = null;
        }
        if (inventory != null) {
            inventory.dispose();
            inventory = null;
        }
        if (playerSkill != null) {
            playerSkill.dispose();
            playerSkill = null;
        }
        if (conbine != null) {
            conbine.dispose();
            conbine = null;
        }
        if (iDMark != null) {
            iDMark.dispose();
            iDMark = null;
        }
        if (charms != null) {
            charms.dispose();
            charms = null;
        }
        if (effectSkin != null) {
            effectSkin.dispose();
            effectSkin = null;
        }
        if (gift != null) {
            gift.dispose();
            gift = null;
        }
        if (nPoint != null) {
            nPoint.dispose();
            nPoint = null;
        }
        if (rewardBlackBall != null) {
            rewardBlackBall.dispose();
            rewardBlackBall = null;
        }
        if (effectFlagBag != null) {
            effectFlagBag.dispose();
            effectFlagBag = null;
        }
        if (pvp != null) {
            pvp.dispose();
            pvp = null;
        }
        effectFlagBag = null;
        clan = null;
        clanMember = null;
        friends = null;
        enemies = null;
        session = null;
        name = null;
    }

    public void banv(Player player) {
        try {
            if (this.banv && player.inventory.gold <= 10000000000L) {
                Item tv = null;
                for (Item item : player.inventory.itemsBag) {
                    if (item.isNotNullItem() && item.template.id == 457) {
                        tv = item;
                        break;
                    }
                }
                if (tv != null && tv.quantity > 20) {
                    InventoryService.gI().subQuantityItemsBag(player, tv, 20);
                    player.inventory.gold += 10000000000L;
                    lasttimebanv = System.currentTimeMillis();
                    PlayerService.gI().sendInfoHpMpMoney(player);
                    InventoryService.gI().sendItemBags(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Hết thỏi vàng rồi, đã tắt lệnh bán vàng");
                    this.banv = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void muav(Player player) {
        try {
            if (this.muav && player != null) {
                if (InventoryService.gI().getCountEmptyBag(player) > 1) {
                    if (player.inventory.gold >= 500000000) {
                        player.inventory.gold -= 500000000;
                        Item tv = ItemService.gI().createNewItem((short) 457);
                        InventoryService.gI().addItemBag(player, tv);
                        lasttimemuav = System.currentTimeMillis();
                        PlayerService.gI().sendInfoHpMpMoney(player);
                        InventoryService.gI().sendItemBags(player);
                    }
                } else {
                    this.muav = false;
                    Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 2 ô trống hành trang, đã tắt tự mua tv");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String percentGold(int type) {
        try {
            if (type == 0) {
                double percent = ((double) this.goldNormar / ChonAiDay.gI().goldNormar) * 100;
                return String.valueOf(Math.ceil(percent));
            } else if (type == 1) {
                double percent = ((double) this.goldVIP / ChonAiDay.gI().goldVip) * 100;
                return String.valueOf(Math.ceil(percent));
            }
        } catch (ArithmeticException e) {
            return "0";
        }
        return "0";
    }

    public boolean isFusion() {
        return fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION;
    }

    public boolean isSkinFusion(Item item) {
        return item.template.id == 601
                || item.template.id == 602
                || item.template.id == 603
                || item.template.id == 639
                || item.template.id == 640
                || item.template.id == 641;
    }

}

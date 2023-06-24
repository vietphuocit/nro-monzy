package com.monzy.models.boss;

import com.monzy.consts.ConstPlayer;
import com.monzy.models.boss.iboss.IBossNew;
import com.monzy.models.boss.iboss.IBossOutfit;
import com.monzy.models.item.Item;
import com.monzy.models.map.ItemMap;
import com.monzy.models.map.Zone;
import com.monzy.models.player.Player;
import com.monzy.models.skill.Skill;
import com.monzy.server.Manager;
import com.monzy.server.ServerNotify;
import com.monzy.services.*;
import com.monzy.services.ChangeMapService;
import com.monzy.utils.SkillUtil;
import com.monzy.utils.Util;

import java.util.HashMap;
import java.util.Map;

public class Boss extends Player implements IBossNew, IBossOutfit {

    public int currentLevel = -1;
    protected final BossData[] data;
    public BossStatus bossStatus;
    protected Zone lastZone;
    protected long lastTimeRest;
    protected int secondsRest;
    protected long lastTimeChatS;
    protected int timeChatS;
    protected byte indexChatS;
    protected long lastTimeChatE;
    protected int timeChatE;
    protected byte indexChatE;
    protected long lastTimeChatM;
    protected int timeChatM;
    protected long lastTimeTargetPlayer;
    protected int timeTargetPlayer;
    protected Player playerTarget;
    protected Boss parentBoss;
    public Boss[][] bossAppearTogether;
    public Zone zoneFinal = null;

    public Boss(int id, BossData... data) throws Exception {
        this.id = id;
        this.isBoss = true;
        if (data == null || data.length == 0) {
            throw new Exception("Dữ liệu boss không hợp lệ");
        }
        this.data = data;
        this.secondsRest = this.data[0].getSecondsRest();
        this.bossStatus = BossStatus.REST;
        BossManager.gI().addBoss(this);
        this.bossAppearTogether = new Boss[this.data.length][];
        for (int i = 0; i < this.bossAppearTogether.length; i++) {
            if (this.data[i].getBossesAppearTogether() != null) {
                this.bossAppearTogether[i] = new Boss[this.data[i].getBossesAppearTogether().length];
                for (int j = 0; j < this.data[i].getBossesAppearTogether().length; j++) {
                    Boss boss = BossManager.gI().createBoss(this.data[i].getBossesAppearTogether()[j]);
                    if (boss != null) {
                        boss.parentBoss = this;
                        this.bossAppearTogether[i][j] = boss;
                    }
                }
            }
        }
    }

    @Override
    public void initBase() {
        BossData data = this.data[this.currentLevel];
        this.name = String.format(data.getName(), Util.nextInt(0, 100));
        this.gender = data.getGender();
        this.nPoint.mpg = 7_5_2002;
        this.nPoint.dameg = data.getDame();
        this.nPoint.hpg = data.getHp()[Util.nextInt(0, data.getHp().length - 1)];
        this.nPoint.hp = nPoint.hpg;
        this.nPoint.calPoint();
        this.initSkill();
        this.resetBase();
    }

    protected void initSkill() {
        for (Skill skill : this.playerSkill.skills) {
            skill.dispose();
        }
        this.playerSkill.skills.clear();
        this.playerSkill.skillSelect = null;
        int[][] skillTemp = data[this.currentLevel].getSkillTemp();
        for (int i = 0; i < skillTemp.length; i++) {
            Skill skill = SkillUtil.createSkill(skillTemp[i][0], skillTemp[i][1]);
            if (skillTemp[i].length == 3) {
                skill.coolDown = skillTemp[i][2];
            }
            this.playerSkill.skills.add(skill);
        }
    }

    protected void resetBase() {
        this.lastTimeChatS = 0;
        this.lastTimeChatE = 0;
        this.timeChatS = 0;
        this.timeChatE = 0;
        this.indexChatS = 0;
        this.indexChatE = 0;
    }

    @Override
    public short getHead() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        }
        return this.data[this.currentLevel].getOutfit()[0];
    }

    @Override
    public short getBody() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 193;
        }
        return this.data[this.currentLevel].getOutfit()[1];
    }

    @Override
    public short getLeg() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 194;
        }
        return this.data[this.currentLevel].getOutfit()[2];
    }

    @Override
    public short getFlagBag() {
        return this.data[this.currentLevel].getOutfit()[3];
    }

    @Override
    public byte getAura() {
        return (byte) this.data[this.currentLevel].getOutfit()[4];
    }

    @Override
    public byte getEffFront() {
        return (byte) this.data[this.currentLevel].getOutfit()[5];
    }

    public Zone getMapJoin() {
        int mapId = this.data[this.currentLevel].getMapJoin()[Util.nextInt(0, Math.min(10, this.data[this.currentLevel].getMapJoin().length - 1))];
        Zone map = MapService.gI().getMapWithRandZone(mapId);
        //to do: check boss in map
        return map;
    }

    @Override
    public void changeStatus(BossStatus status) {
        this.bossStatus = status;
    }

    @Override
    public Player getPlayerAttack() {
        if (this.playerTarget != null && (this.playerTarget.isDie() || !this.zone.equals(this.playerTarget.zone))) {
            this.playerTarget = null;
        }
        if (this.playerTarget == null || Util.canDoWithTime(this.lastTimeTargetPlayer, this.timeTargetPlayer)) {
            this.playerTarget = this.zone.getRandomPlayerInMap();
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(5000, 7000);
        }
        return this.playerTarget;
    }

    @Override
    public void changeToTypePK() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.PK_ALL);
    }

    @Override
    public void changeToTypeNonPK() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.NON_PK);
    }

    @Override
    public void update() {
        super.update();
        this.nPoint.mp = this.nPoint.mpg;
        if (this.effectSkill.isHaveEffectSkill()) {
            return;
        }
        switch (this.bossStatus) {
            case REST:
                this.rest();
                break;
            case RESPAWN:
                this.respawn();
                this.changeStatus(BossStatus.JOIN_MAP);
                break;
            case JOIN_MAP:
                this.joinMap();
                this.changeStatus(BossStatus.CHAT_S);
                break;
            case CHAT_S:
                if (chatS()) {
                    this.doneChatS();
                    this.lastTimeChatM = System.currentTimeMillis();
                    this.timeChatM = 5000;
                    this.changeStatus(BossStatus.ACTIVE);
                }
                break;
            case ACTIVE:
                if (this.effectSkill.isCharging || this.effectSkill.useTroi) {
                    return;
                }
                this.active();
                break;
            case DIE:
                this.changeStatus(BossStatus.CHAT_E);
                break;
            case CHAT_E:
                if (chatE()) {
                    this.doneChatE();
                    this.changeStatus(BossStatus.LEAVE_MAP);
                }
                break;
            case LEAVE_MAP:
                this.leaveMap();
                break;
        }
    }

    //loop
    @Override
    public void rest() {
        int nextLevel = this.currentLevel + 1;
        if (nextLevel >= this.data.length) {
            nextLevel = 0;
        }
        if (this.data[nextLevel].getTypeAppear() == TypeAppear.DEFAULT_APPEAR
                && Util.canDoWithTime(lastTimeRest, secondsRest * 1000L)) {
            this.changeStatus(BossStatus.RESPAWN);
        }
    }

    @Override
    public void respawn() {
        this.currentLevel++;
        if (this.currentLevel >= this.data.length) {
            this.currentLevel = 0;
        }
        this.initBase();
        this.changeToTypeNonPK();
    }

    @Override
    public void joinMap() {
        if (zoneFinal != null) {
            joinMapByZone(zoneFinal);
            this.notifyJoinMap();
            return;
        }
        if (this.zone == null) {
            if (this.parentBoss != null) {
                this.zone = parentBoss.zone;
            } else if (this.lastZone == null) {
                this.zone = getMapJoin();
            } else {
                this.zone = this.lastZone;
            }
        }
        if (this.zone != null) {
            if (this.currentLevel == 0) {
                if (this.parentBoss == null) {
                    ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, -1);
                } else {
                    ChangeMapService.gI().changeMapBySpaceShip(this, this.zone,
                            this.parentBoss.location.x + Util.nextInt(-100, 100));
                }
                this.wakeupAnotherBossWhenAppear();
            } else {
                ChangeMapService.gI().changeMap(this, this.zone, this.location.x, this.location.y);
            }
            Service.gI().sendFlagBag(this);
            this.notifyJoinMap();
        }
    }

    public void joinMapByZone(Player player) {
        if (player.zone != null) {
            this.zone = player.zone;
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, -1);
        }
    }

    public void joinMapByZone(Zone zone) {
        if (zone != null) {
            this.zone = zone;
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, -1);
        }
    }

    protected void notifyJoinMap() {
        if (this.id >= -22 && this.id <= -20) return;
        if (this.zone.map.mapId == 140 || MapService.gI().isMapMaBu(this.zone.map.mapId) || MapService.gI().isMapBlackBallWar(this.zone.map.mapId) || MapService.gI().isMapBanDoKhoBau(this.zone.map.mapId) || MapService.gI().isnguhs(this.zone.map.mapId))
            return;
        ServerNotify.gI().notify("BOSS " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
    }

    @Override
    public boolean chatS() {
        this.changeToTypeNonPK();
        if (Util.canDoWithTime(lastTimeChatS, 1000)) {
            if (this.indexChatS == this.data[this.currentLevel].getTextS().length) {
                return true;
            }
            String textChat = this.data[this.currentLevel].getTextS()[this.indexChatS];
            int prefix = Integer.parseInt(textChat.substring(1, textChat.lastIndexOf("|")));
            textChat = textChat.substring(textChat.lastIndexOf("|") + 1);
            if (!this.chat(prefix, textChat)) {
                return false;
            }
            this.lastTimeChatS = System.currentTimeMillis();
            this.indexChatS++;
        }
        return false;
    }

    @Override
    public void doneChatS() {
        this.changeToTypePK();
    }

    @Override
    public void chatM() {
        if (this.typePk == ConstPlayer.NON_PK) {
            return;
        }
        if (this.data[this.currentLevel].getTextM().length == 0) {
            return;
        }
        if (!Util.canDoWithTime(this.lastTimeChatM, this.timeChatM)) {
            return;
        }
        String textChat = this.data[this.currentLevel].getTextM()[Util.nextInt(0, this.data[this.currentLevel].getTextM().length - 1)];
        int prefix = Integer.parseInt(textChat.substring(1, textChat.lastIndexOf("|")));
        textChat = textChat.substring(textChat.lastIndexOf("|") + 1);
        this.chat(prefix, textChat);
        this.lastTimeChatM = System.currentTimeMillis();
        this.timeChatM = Util.nextInt(3000, 20000);
    }

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.attack();
    }

    protected long lastTimeAttack;

    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = this.zone.getPlayers().stream()
                        .skip((int) (this.zone.getPlayers().size() * Math.random()))
                        .findFirst()
                        .orElse(null);
                if (pl == null || pl.isDie() || pl.isNewPet) {
                    return;
                }
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                this.moveToPlayer(pl);
                SkillService.gI().useSkill(this, pl, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void die(Player plKill) {
        if (plKill != null) {
            reward(plKill);
            ServerNotify.gI().notify(plKill.name + " vừa tiêu diệt được " + this.name + ", ghê chưa ghê chưa..");
        }
        this.changeStatus(BossStatus.DIE);
    }

    @Override
    public void reward(Player plKill) {
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    public boolean chatE() {
        if (Util.canDoWithTime(lastTimeChatE, timeChatE)) {
            if (this.indexChatE == this.data[this.currentLevel].getTextE().length) {
                return true;
            }
            String textChat = this.data[this.currentLevel].getTextE()[this.indexChatE];
            int prefix = Integer.parseInt(textChat.substring(1, textChat.lastIndexOf("|")));
            textChat = textChat.substring(textChat.lastIndexOf("|") + 1);
            if (!this.chat(prefix, textChat)) {
                return false;
            }
            this.lastTimeChatE = System.currentTimeMillis();
            this.timeChatE = textChat.length() * 100;
            if (this.timeChatE > 2000) {
                this.timeChatE = 2000;
            }
            this.indexChatE++;
        }
        return false;
    }

    @Override
    public void doneChatE() {
    }

    @Override
    public void leaveMap() {
        if (this.currentLevel < this.data.length - 1) {
            this.lastZone = this.zone;
            this.changeStatus(BossStatus.RESPAWN);
        } else {
            ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.DEFAULT_SPACE_SHIP);
            ChangeMapService.gI().exitMap(this);
            this.lastZone = null;
            this.lastTimeRest = System.currentTimeMillis();
            this.changeStatus(BossStatus.REST);
        }
        this.wakeupAnotherBossWhenDisappear();
    }
    //end loop

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    @Override
    public void moveToPlayer(Player player) {
        this.moveTo(player.location.x, player.location.y);
    }

    @Override
    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(40, 60);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y);
    }

    @Override
    public void checkPlayerDie(Player player) {
    }

    public void chat(String text) {
        Service.gI().chat(this, text);
    }

    protected boolean chat(int prefix, String textChat) {
        if (prefix == -1) {
            this.chat(textChat);
        } else if (prefix == -2) {
            Player plMap = this.zone.getRandomPlayerInMap();
            if (plMap != null && !plMap.isDie() && Util.getDistance(this, plMap) <= 600) {
                Service.gI().chat(plMap, textChat);
            } else {
                return false;
            }
        } else if (prefix == -3) {
            if (this.parentBoss != null && !this.parentBoss.isDie()) {
                this.parentBoss.chat(textChat);
            }
        } else if (prefix >= 0) {
            if (this.bossAppearTogether != null && this.bossAppearTogether[this.currentLevel] != null) {
                Boss boss = this.bossAppearTogether[this.currentLevel][prefix];
                if (!boss.isDie()) {
                    boss.chat(textChat);
                }
            } else if (this.parentBoss != null && this.parentBoss.bossAppearTogether != null
                    && this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel] != null) {
                Boss boss = this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel][prefix];
                if (!boss.isDie()) {
                    boss.chat(textChat);
                }
            }
        }
        return true;
    }

    @Override
    public void wakeupAnotherBossWhenAppear() {
        if (!MapService.gI().isMapMaBu(this.zone.map.mapId) && !MapService.gI().isMapBlackBallWar(this.zone.map.mapId) && !MapService.gI().isMapBanDoKhoBau(this.zone.map.mapId) && !MapService.gI().isnguhs(this.zone.map.mapId)) {
            System.out.println("BOSS " + this.name + " : " + this.zone.map.mapName + " khu vực " + this.zone.zoneId + "(" + this.zone.map.mapId + ")");
        }
        if (this.bossAppearTogether == null || this.bossAppearTogether[this.currentLevel] == null) {
            return;
        }
        for (Boss boss : this.bossAppearTogether[this.currentLevel]) {
            int nextLevelBoss = boss.currentLevel + 1;
            if (nextLevelBoss >= boss.data.length) {
                nextLevelBoss = 0;
            }
            if (boss.data[nextLevelBoss].getTypeAppear() == TypeAppear.CALL_BY_ANOTHER) {
                if (boss.zone != null) {
                    boss.leaveMap();
                }
            }
            if (boss.data[nextLevelBoss].getTypeAppear() == TypeAppear.APPEAR_WITH_ANOTHER) {
                if (boss.zone != null) {
                    boss.leaveMap();
                }
                boss.changeStatus(BossStatus.RESPAWN);
            }
        }
    }

    @Override
    public void wakeupAnotherBossWhenDisappear() {
        System.out.println("Boss " + this.name + " vừa bị tiêu diệt");
    }

    public boolean rewardItem(Player plKill, int... ids) {
        for (int i = 0; i < ids.length; i++) {
            if (Util.isTrue(getRatioById(ids[i]), 100)) {
                ItemMap it = new ItemMap(this.zone, ids[i], 1, plKill.location.x, plKill.location.y, plKill.id);
                Service.gI().dropItemMap(this.zone, it);
                return true;
            }
        }
        return false;
    }

    public boolean rewardDTL(Player plKill) {
        int randomIdDoThan;
        if (Util.isTrue(10, 100)) {
            randomIdDoThan = Manager.IDS_DO_THAN[Util.nextInt(9, 12)];
        } else {
            randomIdDoThan = Manager.IDS_DO_THAN[Util.nextInt(0, 8)];
        }
        if (Util.isTrue(5, 100)) {
            Item item = ItemService.gI().randomCSDTL(randomIdDoThan, ItemService.BOSS_DROP);
            ItemMap itemMap = new ItemMap(zone, randomIdDoThan, 1, plKill.location.x, plKill.location.y, plKill.id);
            itemMap.options.clear();
            itemMap.options.addAll(item.itemOptions);
            Service.gI().dropItemMap(this.zone, itemMap);
            return true;
        }
        return false;
    }

    public boolean rewardManhThienSu(Player plKill) {
        for (Map.Entry<Integer, Integer> entry : MANH_THIEN_SU.entrySet()) {
            if (Util.isTrue(entry.getValue(), 100)) {
                ItemMap it = new ItemMap(this.zone, entry.getKey(), 1, plKill.location.x, plKill.location.y, plKill.id);
                Service.gI().dropItemMap(this.zone, it);
                return true;
            }
        }
        return false;
    }

    public int getRatioById(int id) {
        return ITEM_REWARD.getOrDefault(id, 0);
    }

    private final Map<Integer, Integer> ITEM_REWARD = new HashMap<Integer, Integer>() {{
        put(859, 10);
        put(956, 10);
        put(1142, 10);
        put(15, 5);
        put(16, 10);
    }};
    private final Map<Integer, Integer> MANH_THIEN_SU = new HashMap<Integer, Integer>() {{
        put(1066, 33);
        put(1067, 33);
        put(1068, 33);
        put(1069, 10);
        put(1070, 10);
    }};

}
/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */

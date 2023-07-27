package com.monzy.models.skill;

import com.monzy.models.mob.Mob;
import com.monzy.models.player.Player;
import com.monzy.services.SkillService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SkillSpecial {

    public static final int TIME_GONG = 2000;
    public static final int TIME_END_24_25 = 3000;
    public static final int TIME_END_26 = 11000;
    public Skill skillSpecial;
    public byte dir;
    public short _xPlayer;
    public short _yPlayer;
    public short _xObjTaget;
    public short _yObjTaget;
    public List<Player> playersTaget;
    public List<Mob> mobsTaget;
    public boolean isStartSkillSpecial;
    public byte stepSkillSpecial;
    public long lastTimeSkillSpecial;
    private Player player;
    private Timer timer;
    private TimerTask timerTask;
    private boolean isActive = false;

    public SkillSpecial(Player player) {
        this.player = player;
        this.playersTaget = new ArrayList<>();
        this.mobsTaget = new ArrayList<>();
    }

    private void update() {
        if (this.isStartSkillSpecial) {
            SkillService.gI().updateSkillSpecial(player);
        }
    }

    public void setSkillSpecial(byte dir, short _xPlayer, short _yPlayer, short _xObjTaget, short _yObjTaget) {
        this.skillSpecial = this.player.playerSkill.skillSelect;
        if (skillSpecial.currLevel < 1000) {
            skillSpecial.currLevel++;
            SkillService.gI().sendCurrLevelSpecial(player, skillSpecial);
        }
        this.dir = dir;
        this._xPlayer = _xPlayer;
        this._yPlayer = _yPlayer;
//        this._xObjTaget = _xObjTaget;
//        this._yObjTaget = _yObjTaget;
        this._xObjTaget = (short) (skillSpecial.dx + skillSpecial.point * 75); // skill dộ dài
        this._yObjTaget = (short) skillSpecial.dy;
        this.isStartSkillSpecial = true;
        this.stepSkillSpecial = 0;
        this.lastTimeSkillSpecial = System.currentTimeMillis();
        this.start(250); // nay delay lay bem dame á
    }

    public void closeSkillSpecial() {
        this.isStartSkillSpecial = false;
        this.stepSkillSpecial = 0;
        this.playersTaget.clear();
        this.mobsTaget.clear();
        this.close();
    }

    private void close() {
        try {
            this.isActive = false;
            this.timer.cancel();
            this.timerTask.cancel();
            this.timer = null;
            this.timerTask = null;
        } catch (Exception e) {
            this.timer = null;
            this.timerTask = null;
        }
    }

    public void start(int leep) {
        if (this.isActive == false) {
            this.isActive = true;
            this.timer = new Timer();
            this.timerTask = new TimerTask() {
                @Override
                public void run() {
                    SkillSpecial.this.update();
                }
            };
            this.timer.schedule(timerTask, leep, leep);
        }
    }

    public void dispose() {
        this.player = null;
        this.skillSpecial = null;
    }

}


package com.monzy.models.map.dhvt;

import com.monzy.utils.Logger;
import com.monzy.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class MartialCongressManager implements Runnable {

    private static MartialCongressManager i;
    private long lastUpdate;
    private static List<MartialCongress> list = new ArrayList<>();
    private static List<MartialCongress> toRemove = new ArrayList<>();

    public static MartialCongressManager gI() {
        if (i == null) {
            i = new MartialCongressManager();
        }
        return i;
    }

    @Override
    public void run() {
        int delay = 500;
        while (true) {
            try {
                long start = System.currentTimeMillis();
                this.update();
                long timeUpdate = System.currentTimeMillis() - start;
                if (timeUpdate < delay) {
                    Thread.sleep(delay - timeUpdate);
                }
            } catch (Exception ignored) {
            }
        }
    }

    public void update() {
        if (Util.canDoWithTime(lastUpdate, 1000)) {
            lastUpdate = System.currentTimeMillis();
            synchronized (list) {
                for (MartialCongress mc : list) {
                    try {
                        mc.update();
                    } catch (Exception e) {
                        Logger.logException(MartialCongressService.class, e);
                    }
                }
                list.removeAll(toRemove);
            }
        }
    }

    public void add(MartialCongress mc) {
        synchronized (list) {
            list.add(mc);
        }
    }

    public void remove(MartialCongress mc) {
        synchronized (toRemove) {
            toRemove.add(mc);
        }
    }

}

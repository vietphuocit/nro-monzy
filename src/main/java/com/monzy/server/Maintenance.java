package com.monzy.server;

import com.monzy.services.Service;
import com.monzy.utils.Logger;

public class Maintenance extends Thread {

    public static boolean isRuning = false;
    public boolean canUseCode;
    private static Maintenance i;
    private int min;

    private Maintenance() {
    }

    public static Maintenance gI() {
        if (i == null) {
            i = new Maintenance();
        }
        return i;
    }

    public void start(int min) {
        if (!isRuning) {
            isRuning = true;
            this.min = min;
            this.start();
        }
    }

    @Override
    public void run() {
        while (this.min > 0) {
            this.min--;
            Service.gI().sendThongBaoAllPlayer("Hệ thống sẽ bảo trì sau " + min
                    + " giây nữa, vui lòng thoát game để tránh mất vật phẩm");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
        Logger.error("BEGIN MAINTENANCE...............................\n");
        ServerManager.gI().close(100);
    }

}

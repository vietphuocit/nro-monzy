package com.monzy.services;

import com.monzy.server.Manager;
import com.monzy.utils.Logger;

public class TopService implements Runnable {

    private static long lastTimeUpdate;

    @Override
    public void run() {
        while (true) {
            try {// Kiểm tra nếu đã trôi qua 1 phút kể từ lần cuối cùng thực hiện
                if (System.currentTimeMillis() - lastTimeUpdate >= 60 * 1000) {
                    // Thực hiện đoạn mã ở đây
                    Manager.TOP_NV = Manager.readTop(Manager.QUERY_TOP_NV);
                    Manager.TOP_SM = Manager.readTop(Manager.QUERY_TOP_SM);
                    Manager.TOP_NAP = Manager.readTop(Manager.QUERY_TOP_NAP);
                    // Cập nhật thời gian thực hiện cuối cùng
                    lastTimeUpdate = System.currentTimeMillis();
                    System.err.println("Cập nhật bảng xếp hạng");
                }
                // Tạm dừng 1 giây trước khi kiểm tra lại
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception ignored) {
                Logger.error("Lỗi đọc bảng xếp hạng");
            }
        }
    }

}

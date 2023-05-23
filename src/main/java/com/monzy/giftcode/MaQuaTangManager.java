/*
 * Beo Sờ tu đi ô
 */
package com.monzy.giftcode;

import com.database.Database;
import com.monzy.models.item.Item.ItemOption;
import com.monzy.models.player.Player;
import com.monzy.services.NpcService;
import com.monzy.services.Service;
import com.monzy.utils.Logger;
import com.monzy.utils.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * @author Administrator
 */
public class MaQuaTangManager {

    public String name;
    public final ArrayList<MaQuaTang> listGiftCode = new ArrayList<>();
    private static MaQuaTangManager instance;

    public static MaQuaTangManager gI() {
        if (instance == null) {
            instance = new MaQuaTangManager();
        }
        return instance;
    }

    public void init() {
        try (Connection con = Database.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM giftcode", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MaQuaTang giftcode = new MaQuaTang();
                giftcode.code = rs.getString("code") + Util.nextInt(111, 999);
                giftcode.countLeft = rs.getInt("count_left");
                giftcode.datecreate = rs.getTimestamp("datecreate");
                giftcode.dateexpired = rs.getTimestamp("expired");
                JSONArray jar = (JSONArray) JSONValue.parse(rs.getString("detail"));
                if (jar != null) {
                    for (int i = 0; i < jar.size(); ++i) {
                        JSONObject jsonObj = (JSONObject) jar.get(i);
                        giftcode.detail.put(Integer.parseInt(jsonObj.get("id").toString()), Integer.parseInt(jsonObj.get("quantity").toString()));
                        jsonObj.clear();
                    }
                }
                JSONArray option = (JSONArray) JSONValue.parse(rs.getString("itemoption"));
                Logger.log("\nDone-------------------" + option.toString());
                if (option != null) {
                    for (int u = 0; u < option.size(); u++) {
                        JSONObject jsonobject = (JSONObject) option.get(u);
                        giftcode.option.add(new ItemOption(Integer.parseInt(jsonobject.get("id").toString()), Integer.parseInt(jsonobject.get("param").toString())));
                        jsonobject.clear();
                    }
                }
                listGiftCode.add(giftcode);
            }
            con.close();
        } catch (Exception erorlog) {
            erorlog.printStackTrace();
        }
    }

    public void sizeList(Player pl) {
        Service.gI().sendThongBao(pl, "" + MaQuaTang.class);
    }

    public MaQuaTang checkUseGiftCode(int idPlayer, String code) {
        for (MaQuaTang giftCode : listGiftCode) {
            if (giftCode.code.equals(code) && giftCode.countLeft > 0 && !giftCode.isUsedGiftCode(idPlayer)) {
                giftCode.countLeft -= 1;
                giftCode.addPlayerUsed(idPlayer);
                return giftCode;
            }
        }
        return null;
    }

    public void checkInfomationGiftCode(Player p) {
        StringBuilder sb = new StringBuilder();
        for (MaQuaTang giftCode : listGiftCode) {
            sb.append("Code: ").append(giftCode.code).append(", Số lượng: ").append(giftCode.countLeft).append("\b").append(", Ngày tạo: ")
                    .append(giftCode.datecreate).append("Ngày hết hạn").append(giftCode.dateexpired);
        }
        NpcService.gI().createTutorial(p, 5073, sb.toString());
    }

}

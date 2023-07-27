/*
 * Beo Sờ tu đi ô
 */
package com.monzy.giftcode;

import com.database.Database;
import com.monzy.models.item.Item.ItemOption;
import com.monzy.models.player.Player;
import com.monzy.services.NpcService;
import com.monzy.utils.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @author Administrator
 */
public class GiftCodeManager {

    private static GiftCodeManager instance;
    public final ArrayList<GiftCode> listGiftCode = new ArrayList<>();

    public GiftCodeManager() {
        init();
    }

    public static GiftCodeManager gI() {
        if (instance == null) {
            instance = new GiftCodeManager();
        }
        return instance;
    }

    public void init() {
        listGiftCode.clear();
        try (Connection con = Database.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM giftcode", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                GiftCode giftcode = new GiftCode();
                giftcode.code = rs.getString("code");
                giftcode.countLeft = rs.getInt("count_left");
                giftcode.dateCreate = rs.getTimestamp("date_create");
                giftcode.dateExpired = rs.getTimestamp("expired");
                JSONArray details = (JSONArray) JSONValue.parse(rs.getString("details"));
                if (details != null) {
                    for (Object o : details) {
                        JSONObject detail = (JSONObject) o;
                        giftcode.details.put(Integer.parseInt(detail.get("id").toString()), Integer.parseInt(detail.get("quantity").toString()));
                        detail.clear();
                    }
                }
                JSONArray option = (JSONArray) JSONValue.parse(rs.getString("item_option"));
                if (option != null) {
                    for (Object o : option) {
                        JSONObject jsonobject = (JSONObject) o;
                        giftcode.option.add(new ItemOption(Integer.parseInt(jsonobject.get("id").toString()), Integer.parseInt(jsonobject.get("param").toString())));
                        jsonobject.clear();
                    }
                }
                JSONArray players = (JSONArray) JSONValue.parse(rs.getString("players"));
                if (players != null) {
                    for (Object o : players) {
                        JSONObject jsonobject = (JSONObject) o;
                        giftcode.idsPlayer.add(Integer.parseInt(jsonobject.get("id").toString()));
                        jsonobject.clear();
                    }
                }
                listGiftCode.add(giftcode);
            }
        } catch (Exception e) {
            Logger.error(this.getClass().getName() + ": " + e.getMessage());
        }
    }

    public GiftCode checkUseGiftCode(int idPlayer, String code) {
        for (GiftCode giftCode : listGiftCode) {
            if (giftCode.code.equals(code) && giftCode.countLeft > 0 && !giftCode.isUsedGiftCode(idPlayer)) {
                giftCode.countLeft -= 1;
                giftCode.addPlayerUsed(idPlayer);
                return giftCode;
            }
        }
        return null;
    }

    public void checkInformationGiftCode(Player p) {
        StringBuilder sb = new StringBuilder();
        for (GiftCode giftCode : listGiftCode) {
            sb.append("Code: ").append(giftCode.code).append(", Số lượng: ").append(giftCode.countLeft).append("\bHạn sử dụng: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(giftCode.dateCreate)).append(" -> ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(giftCode.dateExpired)).append("\b");
        }
        NpcService.gI().createTutorial(p, 5073, sb.toString());
    }

    public void saveGiftCode() {
        try {
            for (GiftCode giftCode : listGiftCode) {
                JSONArray players = new JSONArray();
                for (Integer value : giftCode.idsPlayer) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", value);
                    players.add(jsonObject);
                }
                String query = "update giftcode\n" + "set count_left = ?, players = ?\n" + "WHERE code = ?";
                Database.executeUpdate(query, giftCode.countLeft, players.toJSONString(), giftCode.code);
            }
        } catch (Exception e) {
            Logger.logException(GiftCodeManager.class, e, "Lỗi save giftcode");
        }
    }

}

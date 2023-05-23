package com.monzy.services;

import com.monzy.giftcode.MaQuaTang;
import com.monzy.giftcode.MaQuaTangManager;
import com.monzy.models.item.Item;
import com.monzy.models.player.Player;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * @Stole By Arriety 💖
 */
public class GiftService {

    private static GiftService i;

    private GiftService() {
    }

    public String code;
    public int idGiftcode;
    public int gold;
    public int gem;
    public int dayexits;
    public Timestamp timecreate;
    public ArrayList<Item> listItem = new ArrayList<>();
    public static ArrayList<GiftService> gifts = new ArrayList<>();

    public static GiftService gI() {
        if (i == null) {
            i = new GiftService();
        }
        return i;
    }

    public void giftCode(Player player, String code) {
        MaQuaTang giftcode = MaQuaTangManager.gI().checkUseGiftCode((int) player.id, code);
        // if(!Maintenance.gI().canUseCode){Service.gI().sendThongBao(player, "Không thể thực hiện lúc này ");return;}
        if (giftcode == null) {
            Service.gI().sendThongBao(player, "Code đã được sử dụng, hoặc không tồn tại!");
        } else if (giftcode.timeCode()) {
            Service.gI().sendThongBao(player, "Code đã hết hạn");
        } else {
            InventoryServiceNew.gI().addItemGiftCodeToPlayer(player, giftcode);
        }
    }

}

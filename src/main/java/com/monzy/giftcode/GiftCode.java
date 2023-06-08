/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monzy.giftcode;

import com.monzy.models.item.Item.ItemOption;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Administrator
 */
public class GiftCode {

    String code;
    int countLeft;
    public HashMap<Integer, Integer> details = new HashMap<>();
    public ArrayList<Integer> idsPlayer = new ArrayList<>();
    public ArrayList<ItemOption> option = new ArrayList<>();
    Timestamp dateCreate;
    Timestamp dateExpired;

    public boolean isUsedGiftCode(int idPlayer) {
        return idsPlayer.contains(idPlayer);
    }

    public void addPlayerUsed(int idPlayer) {
        idsPlayer.add(idPlayer);
    }

    public boolean timeCode() {
        return this.dateCreate.getTime() > this.dateExpired.getTime();
    }

}

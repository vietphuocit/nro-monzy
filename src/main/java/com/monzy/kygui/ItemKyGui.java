/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monzy.kygui;

import com.monzy.models.item.Item.ItemOption;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class ItemKyGui {

    public int id;
    public short itemId;
    public int player_sell;
    public int tab;
    public int goldSell;
    public int rubySell;
    public int quantity;
    public byte isUpTop;
    public List<ItemOption> options = new ArrayList<>();
    public boolean isBuy;
    public int player_buy;

    public ItemKyGui() {
    }

    public ItemKyGui(int i, short id, int plId, int t, int gold, int ruby, int q, byte isUp, List<ItemOption> op, boolean b, int player_buy) {
        this.id = i;
        itemId = id;
        player_sell = plId;
        tab = t;
        goldSell = gold;
        rubySell = ruby;
        quantity = q;
        isUpTop = isUp;
        options = op;
        isBuy = b;
    }

}

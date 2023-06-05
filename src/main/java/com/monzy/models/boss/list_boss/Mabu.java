/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monzy.models.boss.list_boss;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;
import com.monzy.models.player.Player;
import com.monzy.services.InventoryServiceNew;
import com.monzy.services.ItemService;
import com.monzy.services.Service;

/**
 * @@Stole By Arriety
 */
public class Mabu extends Boss {

    public Mabu() throws Exception {
        super(BossID.MABU, BossesData.MABU);
    }

    @Override
    public void reward(Player plKill) {
        InventoryServiceNew.gI().addItemBag(plKill, ItemService.gI().createNewItem(((short) 568)));
        InventoryServiceNew.gI().sendItemBags(plKill);
        Service.gI().sendThongBao(plKill, "Bạn nhận được Trứng Bư!");
    }

}

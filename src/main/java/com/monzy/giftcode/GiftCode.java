/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monzy.giftcode;

import com.monzy.models.item.Item;

import java.sql.Timestamp;
import java.util.ArrayList;

public class GiftCode {

  public ArrayList<Item> details = new ArrayList<>();
  public ArrayList<Integer> idsPlayer = new ArrayList<>();
  String code;
  int countLeft;
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

package com.monzy.models.player;

public class Gift {

  public boolean goldTanThu;
  public boolean gemTanThu;
  private Player player;

  public Gift(Player player) {
    this.player = player;
  }

  public void dispose() {
    this.player = null;
  }
}

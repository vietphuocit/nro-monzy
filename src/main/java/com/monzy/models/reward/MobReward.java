package com.monzy.models.reward;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MobReward {

  private int mobId;
  private List<ItemMobReward> itemReward;

  public MobReward(int mobId) {
    this.mobId = mobId;
    this.itemReward = new ArrayList<>();
  }
}

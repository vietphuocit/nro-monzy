package com.monzy.models.boss.list_boss.fide;

import com.monzy.models.boss.Boss;
import com.monzy.models.boss.BossID;
import com.monzy.models.boss.BossesData;
import com.monzy.models.item.Item;
import com.monzy.models.map.ItemMap;
import com.monzy.models.player.Player;
import com.monzy.services.ItemService;
import com.monzy.services.Service;
import com.monzy.utils.Util;

public class FideGold extends Boss {

  public FideGold() throws Exception {
    super(BossID.FIDE_GOLD, BossesData.FIDE_GOLD);
  }

  @Override
  public void reward(Player plKill) {
    ItemMap it = new ItemMap(this.zone, 629, 1, plKill.location.x, plKill.location.y, plKill.id);
    it.options.addAll(caiTrangFideGold().itemOptions);
    if (Util.isTrue(50, 100)) {
      Service.gI().dropItemMap(this.zone, it);
    }
    rewardItem(plKill, 859, 956, 1142);
    super.reward(plKill);
  }

  @Override
  public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
    return super.injured(plAtt, (int) (damage * 0.5f), piercing, isMobAttack);
  }

  public Item caiTrangFideGold() {
    Item item = ItemService.gI().createNewItem((short) 629);
    item.itemOptions.add(new Item.ItemOption(147, 30)); // sd 50%
    item.itemOptions.add(new Item.ItemOption(77, 30)); // hp 50%
    item.itemOptions.add(new Item.ItemOption(103, 30)); // ki 50%
    item.itemOptions.add(new Item.ItemOption(106, 0)); // k ảnh hưởng bới cái lạnh
    if (Util.isTrue(995, 1000)) { // tỉ lệ ra hsd
      item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(4) + 3)); // hsd
    }
    return item;
  }
}

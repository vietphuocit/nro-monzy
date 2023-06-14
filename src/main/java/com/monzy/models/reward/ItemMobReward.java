package com.monzy.models.reward;

import com.monzy.models.Template;
import com.monzy.models.item.Item;
import com.monzy.models.map.ItemMap;
import com.monzy.models.map.Zone;
import com.monzy.models.player.Player;
import com.monzy.server.Manager;
import com.monzy.utils.Util;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class ItemMobReward {

    private Template.ItemTemplate temp;
    private int[] mapDrop;
    private int[] quantity;
    private int[] ratio;
    private int gender;
    private List<ItemOptionMobReward> option;

    public ItemMobReward(int tempId, int[] mapDrop, int[] quantity, int[] ratio, int gender) {
        this.temp = Manager.ITEM_TEMPLATES.get(tempId);
        this.mapDrop = mapDrop;
        this.quantity = quantity;
        if (this.quantity[0] < 0) {
            this.quantity[0] = -this.quantity[0];
        } else if (this.quantity[0] == 0) {
            this.quantity[0] = 1;
        }
        if (this.quantity[1] < 0) {
            this.quantity[1] = -this.quantity[1];
        } else if (this.quantity[1] == 0) {
            this.quantity[1] = 1;
        }
        if (this.quantity[0] > this.quantity[1]) {
            int tempSwap = this.quantity[0];
            this.quantity[0] = this.quantity[1];
            this.quantity[1] = tempSwap;
        }
        this.ratio = ratio;
        this.gender = gender;
        this.option = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ItemMobReward{" +
                "item=" + temp.name +
                ", ratio=" + Arrays.toString(ratio) +
                '}';
    }

}
/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - TiMi :)))
 */

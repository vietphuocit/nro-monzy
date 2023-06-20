package com.monzy.models.item;

import com.monzy.models.Template;
import com.monzy.models.Template.ItemTemplate;
import com.monzy.server.Manager;
import com.monzy.services.ItemService;
import com.monzy.utils.Util;

import java.util.*;

public class Item {

    public ItemTemplate template;
    public String info;
    public String content;
    public int quantity;
    public int quantityGD = 0;
    public List<ItemOption> itemOptions;
    public long createTime;

    public boolean isNotNullItem() {
        return this.template != null;
    }

    public Item() {
        this.itemOptions = new ArrayList<>();
        this.createTime = System.currentTimeMillis();
    }

    public Item(short itemId) {
        this.template = ItemService.gI().getTemplate(itemId);
        this.itemOptions = new ArrayList<>();
        this.createTime = System.currentTimeMillis();
    }

    public String getInfo() {
        String strInfo = "";
        for (ItemOption itemOption : itemOptions) {
            strInfo += itemOption.getOptionString();
        }
        return strInfo;
    }

    public String getContent() {
        return "Yêu cầu sức mạnh " + this.template.strRequire + " trở lên";
    }

    public void dispose() {
        this.template = null;
        this.info = null;
        this.content = null;
        if (this.itemOptions != null) {
            for (ItemOption io : this.itemOptions) {
                io.dispose();
            }
            this.itemOptions.clear();
        }
        this.itemOptions = null;
    }

    public static class ItemOption {

        private static final Map<String, String> OPTION_STRING = new HashMap<String, String>();
        public int param;
        public Template.ItemOptionTemplate optionTemplate;

        public ItemOption() {
        }

        public ItemOption(ItemOption io) {
            this.param = io.param;
            this.optionTemplate = io.optionTemplate;
        }

        public ItemOption(int tempId, int param) {
            this.optionTemplate = ItemService.gI().getItemOptionTemplate(tempId);
            this.param = param;
        }

        public ItemOption(Template.ItemOptionTemplate temp, int param) {
            this.optionTemplate = temp;
            this.param = param;
        }

        public String getOptionString() {
            return Util.replace(this.optionTemplate.name, "#", String.valueOf(this.param));
        }

        public void dispose() {
            this.optionTemplate = null;
        }

        @Override
        public String toString() {
            final String n = "\"";
            return "{"
                    + n + "id" + n + ":" + n + optionTemplate.id + n + ","
                    + n + "param" + n + ":" + n + param + n
                    + "}";
        }

    }

    public boolean isSKH() {
        for (ItemOption itemOption : itemOptions
        ) {
            if (itemOption.optionTemplate.id >= 127 && itemOption.optionTemplate.id <= 135) {
                return true;
            }
        }
        return false;
    }

    public boolean isDShop() {
        for (short[][] ids_by_gender : Manager.IDS_TRANG_BI_SHOP) {
            for (short[] ids_by_type : ids_by_gender) {
                for (short id : ids_by_type) {
                    if(id == template.id)
                        return true;
                }
            }
        }
        return false;
    }

    public boolean isDTS() {
        return this.template.id >= 1048 && this.template.id <= 1062;
    }

    public boolean isDTL() {
        return this.template.id >= 555 && this.template.id <= 567;
    }

    public boolean isDHD() {
        return this.template.id >= 650 && this.template.id <= 662;
    }

    public boolean isManhTS() {
        return this.template.id >= 1066 && this.template.id <= 1070;
    }

    public void isBugItem() {
        if (!isNotNullItem())
            return;
        isBugDTL();
        isBugDHD();
        isBugDTS();
        isBugDShop();
    }

    public void isBugDTL() {
        if (!isDTL() || itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 72).findFirst().orElse(null) != null) {
            return;
        }
        for (ItemOption option : itemOptions) {
            if (template.type == 0 && option.optionTemplate.id == 47 && option.param > 1500 * 1.1) {
                option.param = Util.nextInt(1000, 1500);
                return;
            }
            if (template.type == 1 && option.optionTemplate.id == 22 && option.param > 60 * 1.1) {
                option.param = Util.nextInt(45, 60);
                return;
            }
            if (template.type == 2 && option.optionTemplate.id == 0 && option.param > 4500 * 1.1) {
                option.param = Util.nextInt(3500, 4500);
                return;
            }
            if (template.type == 3 && option.optionTemplate.id == 23 && option.param > 60 * 1.1) {
                option.param = Util.nextInt(45, 60);
                return;
            }
            if (template.type == 4 && option.optionTemplate.id == 14 && option.param > 18 * 1.1) {
                option.param = Util.nextInt(16, 18);
                return;
            }
        }
    }

    public void isBugDHD() {
        if (!isDHD() || itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 72).findFirst().orElse(null) != null) {
            return;
        }
        for (ItemOption option : itemOptions) {
            if (template.type == 0 && option.optionTemplate.id == 47 && option.param > 2500 * 1.1) {
                option.param = Util.nextInt(2000, 2500);
                return;
            }
            if (template.type == 1 && option.optionTemplate.id == 22 && option.param > 90 * 1.1) {
                option.param = Util.nextInt(75, 90);
                return;
            }
            if (template.type == 2 && option.optionTemplate.id == 0 && option.param > 6500 * 1.1) {
                option.param = Util.nextInt(5500, 6500);
                return;
            }
            if (template.type == 3 && option.optionTemplate.id == 23 && option.param > 90 * 1.1) {
                option.param = Util.nextInt(75, 90);
                return;
            }
            if (template.type == 4 && option.optionTemplate.id == 14 && option.param > 22 * 1.1) {
                option.param = Util.nextInt(19, 22);
                return;
            }
        }
    }

    public void isBugDTS() {
        if (!isDTS() || itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 72).findFirst().orElse(null) != null) {
            return;
        }
        for (ItemOption option : itemOptions) {
            if (template.type == 0 && option.optionTemplate.id == 47 && option.param > 3500 * 1.1) {
                option.param = Util.nextInt(3000, 3500);
                return;
            }
            if (template.type == 1 && option.optionTemplate.id == 22 && option.param > 120 * 1.1) {
                option.param = Util.nextInt(105, 120);
                return;
            }
            if (template.type == 2 && option.optionTemplate.id == 0 && option.param > 8500 * 1.1) {
                option.param = Util.nextInt(7500, 8500);
                return;
            }
            if (template.type == 3 && option.optionTemplate.id == 23 && option.param > 120 * 1.1) {
                option.param = Util.nextInt(105, 120);
                return;
            }
            if (template.type == 4 && option.optionTemplate.id == 14 && option.param > 26 * 1.1) {
                option.param = Util.nextInt(23, 26);
                return;
            }
        }
    }

    public void isBugDShop() {
        if (!isDShop() || itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 72).findFirst().orElse(null) != null) {
            return;
        }
        for (ItemOption itemOption : ItemService.gI().getListOptionItemShop(template.id)) {
            ItemOption io = itemOptions.stream().filter(itemO -> itemO.optionTemplate.id == itemOption.optionTemplate.id).findFirst().orElse(null);
            if (io != null) {
                io.param = itemOption.param;
            }
        }
    }

    public String typeName() {
        switch (this.template.type) {
            case 0:
                return "Áo";
            case 1:
                return "Quần";
            case 2:
                return "Găng";
            case 3:
                return "Giày";
            case 4:
                return "Rada";
            default:
                return "";
        }
    }

    public byte typeIdManh() {
        if (!isManhTS()) return -1;
        switch (this.template.id) {
            case 1066:
                return 0;
            case 1067:
                return 1;
            case 1070:
                return 2;
            case 1068:
                return 3;
            case 1069:
                return 4;
            default:
                return -1;
        }
    }

    public String typeNameManh() {
        switch (this.template.id) {
            case 1066:
                return "Áo";
            case 1067:
                return "Quần";
            case 1070:
                return "Găng";
            case 1068:
                return "Giày";
            case 1069:
                return "Nhẫn";
            default:
                return "";
        }
    }

}

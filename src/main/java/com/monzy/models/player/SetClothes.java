package com.monzy.models.player;

import com.monzy.models.item.Item;

public class SetClothes {

    private Player player;

    public SetClothes(Player player) {
        this.player = player;
    }

    public byte krilin;
    public byte kaioken;
    public byte songoku;
    public byte picolo;
    public byte ocTieu;
    public byte pikkoroDaimao;
    public byte kakarot;
    public byte cadic;
    public byte nappa;
    public byte worldcup;
    public byte setDHD;
    public byte setGOD;
    public boolean godClothes;
    public int ctHaiTac = -1;

    public void setup() {
        setDefault();
        setupSKT();
        this.godClothes = true;
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id > 567 || item.template.id < 555) {
                    this.godClothes = false;
                    break;
                }
            } else {
                this.godClothes = false;
                break;
            }
        }
        Item ct = this.player.inventory.itemsBody.get(5);
        if (ct.isNotNullItem()) {
            switch (ct.template.id) {
                case 618:
                case 619:
                case 620:
                case 621:
                case 622:
                case 623:
                case 624:
                case 626:
                case 627:
                    this.ctHaiTac = ct.template.id;
                    break;
            }
        }
    }

    private void setupSKT() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 129:
                        case 138:
                            isActSet = true;
                            songoku++;
                            break;
                        case 128:
                        case 137:
                            isActSet = true;
                            kaioken++;
                            break;
                        case 127:
                        case 136:
                            isActSet = true;
                            krilin++;
                            break;
                        case 131:
                        case 140:
                            isActSet = true;
                            ocTieu++;
                            break;
                        case 132:
                        case 141:
                            isActSet = true;
                            pikkoroDaimao++;
                            break;
                        case 130:
                        case 139:
                            isActSet = true;
                            picolo++;
                            break;
                        case 135:
                        case 144:
                            isActSet = true;
                            nappa++;
                            break;
                        case 133:
                        case 142:
                            isActSet = true;
                            kakarot++;
                            break;
                        case 134:
                        case 143:
                            isActSet = true;
                            cadic++;
                            break;
                        case 21:
//                            if (io.param == 80) {
//                                setDHD++;
//                            } 
                            if (io.param == 18) {
                                setGOD++;
                            }
                            break;
                    }
                    if (isActSet) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    private void setDefault() {
        this.songoku = 0;
        this.kaioken = 0;
        this.krilin = 0;
        this.ocTieu = 0;
        this.pikkoroDaimao = 0;
        this.picolo = 0;
        this.kakarot = 0;
        this.cadic = 0;
        this.nappa = 0;
        this.setDHD = 0;
        this.setGOD = 0;
        this.worldcup = 0;
        this.godClothes = false;
        this.ctHaiTac = -1;
    }

    public void dispose() {
        this.player = null;
    }

}

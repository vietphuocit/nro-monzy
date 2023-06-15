package com.monzy.card;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dev Duy Peo
 */
public class Card {

    public short id;
    public byte amount;
    public byte maxAmount;
    public byte level;
    public byte used;
    public List<OptionCard> optionCards;

    public Card() {
        id = -1;
        amount = 0;
        maxAmount = 0;
        level = 0;
        used = 0;
        optionCards = new ArrayList<>();
    }

    public Card(byte m, List<OptionCard> o) {
        maxAmount = m;
        optionCards = o;
    }

    public Card(short i, byte a, byte ma, byte le, List<OptionCard> o) {
        id = i;
        amount = a;
        maxAmount = ma;
        level = le;
        optionCards = o;
    }

    public Card(short i, byte a, byte ma, byte le, List<OptionCard> o, byte u) {
        id = i;
        amount = a;
        maxAmount = ma;
        level = le;
        optionCards = o;
        used = u;
    }

    @Override
    public String toString() {
        final String n = "\"";
        return "{"
                + n + "id" + n + ":" + n + id + n + ","
                + n + "amount" + n + ":" + n + amount + n + ","
                + n + "max" + n + ":" + n + maxAmount + n + ","
                + n + "option" + n + ":" + optionCards + ","
                + n + "level" + n + ":" + n + level + n + ","
                + n + "used" + n + ":" + n + used + n
                + "}";
    }

}

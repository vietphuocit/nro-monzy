package com.monzy.models.npc;

import com.monzy.models.player.Player;

public interface IActionNpc {

    void openBaseMenu(Player player);

    void confirmMenu(Player player, int select);

}

package com.monzy.models.npc;

import com.monzy.consts.ConstNpc;
import com.monzy.consts.ConstTask;
import com.monzy.models.player.Player;
import com.monzy.server.Manager;
import com.monzy.services.TaskService;

import java.util.ArrayList;
import java.util.List;

public class NpcManager {

  public static Npc getByIdAndMap(int id, int mapId) {
    for (Npc npc : Manager.NPCS) {
      if (npc.tempId == id && npc.mapId == mapId) {
        return npc;
      }
    }
    return null;
  }

  public static Npc getNpc(byte tempId) {
    for (Npc npc : Manager.NPCS) {
      if (npc.tempId == tempId) {
        return npc;
      }
    }
    return null;
  }

  public static List<Npc> getNpcsByMapPlayer(Player player) {
    List<Npc> list = new ArrayList<>();
    if (player.zone != null) {
      for (Npc npc : player.zone.map.npcs) {
        if (npc.tempId == ConstNpc.QUA_TRUNG
            && player.mabuEgg == null
            && player.zone.map.mapId == (21 + player.gender)) {
          continue;
        } else if (npc.tempId == ConstNpc.QUA_TRUNG
            && player.billEgg == null
            && player.zone.map.mapId == 154) {
          continue;
        } else if (npc.tempId == ConstNpc.CALICK
            && TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
          continue;
        }
        list.add(npc);
      }
    }
    return list;
  }
}

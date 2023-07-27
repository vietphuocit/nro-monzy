package com.monzy.server;

import com.database.Database;
import com.monzy.jdbc.daos.PlayerDAO;
import com.monzy.models.map.ItemMap;
import com.monzy.models.matches.pvp.DaiHoiVoThuat;
import com.monzy.models.matches.pvp.DaiHoiVoThuatService;
import com.monzy.models.player.Player;
import com.monzy.server.io.MySession;
import com.monzy.services.ChangeMapService;
import com.monzy.services.NgocRongNamecService;
import com.monzy.services.Service;
import com.monzy.services.TransactionService;
import com.monzy.services.func.SummonDragon;
import com.monzy.utils.Logger;
import com.network.server.MonzySessionManager;
import com.network.session.ISession;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client implements Runnable {

    private static Client i;
    private final Map<Long, Player> players_id = new HashMap<Long, Player>();
    private final Map<Integer, Player> players_userId = new HashMap<Integer, Player>();
    private final Map<String, Player> players_name = new HashMap<String, Player>();
    private final List<Player> players = new ArrayList<>();
    private final boolean running = true;

    private Client() {
        new Thread(this).start();
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public static Client gI() {
        if (i == null) {
            i = new Client();
        }
        return i;
    }

    public void put(Player player) {
        if (!players_id.containsKey(player.id)) {
            this.players_id.put(player.id, player);
        }
        if (!players_name.containsValue(player)) {
            this.players_name.put(player.name, player);
        }
        if (!players_userId.containsValue(player)) {
            this.players_userId.put(player.getSession().userId, player);
        }
        if (!players.contains(player)) {
            this.players.add(player);
        }
    }

    private void remove(MySession session) {
        if (session.player != null) {
            this.remove(session.player);
            session.player.dispose();
        }
        if (session.joinedGame) {
            session.joinedGame = false;
            try {
                Database.executeUpdate("update account set last_time_logout = ? where id = ?", new Timestamp(System.currentTimeMillis()), session.userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ServerManager.gI().disconnect(session);
    }

    private void remove(Player player) {
        this.players_id.remove(player.id);
        this.players_name.remove(player.name);
        this.players_userId.remove(player.getSession().userId);
        this.players.remove(player);
        if (!player.beforeDispose) {
            DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).removePlayerWait(player);
            DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).removePlayer(player);
            player.beforeDispose = true;
            player.mapIdBeforeLogout = player.zone.map.mapId;
            if (player.idNRNM != -1) {
                ItemMap itemMap = new ItemMap(player.zone, player.idNRNM, 1, player.location.x, player.location.y, -1);
                Service.gI().dropItemMap(player.zone, itemMap);
                NgocRongNamecService.gI().pNrNamec[player.idNRNM - 353] = "";
                NgocRongNamecService.gI().idpNrNamec[player.idNRNM - 353] = -1;
                player.idNRNM = -1;
            }
            ChangeMapService.gI().exitMap(player);
            TransactionService.gI().cancelTrade(player);
            if (player.clan != null) {
                player.clan.removeMemberOnline(null, player);
            }
//            if (player.itemTime != null && player.itemTime.isUseTDLT) {
//                Item tdlt = null;
//                try {
//                    tdlt = InventoryService.gI().findItemBag(player, 521);
//                } catch (Exception ex) {
//                }
//                if (tdlt != null) {
//                    ItemTimeService.gI().turnOffTDLT(player, tdlt);
//                }
//            }
            if (SummonDragon.gI().playerSummonShenron != null
                    && SummonDragon.gI().playerSummonShenron.id == player.id) {
                SummonDragon.gI().isPlayerDisconnect = true;
            }
            if (player.mobMe != null) {
                player.mobMe.mobMeDie();
            }
            if (player.pet != null) {
                if (player.pet.mobMe != null) {
                    player.pet.mobMe.mobMeDie();
                }
                ChangeMapService.gI().exitMap(player.pet);
            }
        }
        PlayerDAO.updatePlayer(player);
    }

    public void kickSession(MySession session) {
        if (session != null) {
            this.remove(session);
            session.disconnect();
        }
    }

    public Player getPlayer(long playerId) {
        return this.players_id.get(playerId);
    }

    public Player getPlayerByUser(int userId) {
        return this.players_userId.get(userId);
    }

    public Player getPlayer(String name) {
        return this.players_name.get(name);
    }

    public void close() {
        Logger.error("BEGIN KICK OUT SESSION.............................." + players.size() + "\n");
        while (!players.isEmpty()) {
            this.kickSession(players.remove(0).getSession());
        }
        Logger.error("...........................................SUCCESSFUL\n");
    }

    public void cloneMySessionNotConnect() {
        Logger.error("BEGIN KICK OUT MySession Not Connect...............................\n");
        Logger.error("COUNT: " + MonzySessionManager.gI().getSessions().size());
        if (!MonzySessionManager.gI().getSessions().isEmpty()) {
            for (int j = 0; j < MonzySessionManager.gI().getSessions().size(); j++) {
                MySession m = (MySession) MonzySessionManager.gI().getSessions().get(j);
                if (m.player == null) {
                    this.kickSession((MySession) MonzySessionManager.gI().getSessions().remove(j));
                }
            }
        }
        Logger.error("..........................................................SUCCESSFUL\n");
    }

    private void update() {
        for (ISession s : MonzySessionManager.gI().getSessions()) {
            MySession session = (MySession) s;
            if (session.timeWait > 0) {
                session.timeWait--;
                if (session.timeWait == 0) {
                    kickSession(session);
                }
            }
        }
    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            try {
                long st = System.currentTimeMillis();
                update();
                Thread.sleep(800 - (System.currentTimeMillis() - st));
            } catch (Exception e) {
            }
        }
    }

    public void show(Player player) {
        String txt = "";
        txt += "sessions: " + MonzySessionManager.gI().getSessions().size() + "\n";
        txt += "players_id: " + players_id.size() + "\n";
        txt += "players_userId: " + players_userId.size() + "\n";
        txt += "players_name: " + players_name.size() + "\n";
        txt += "players: " + players.size() + "\n";
        Service.gI().sendThongBao(player, txt);
    }

}

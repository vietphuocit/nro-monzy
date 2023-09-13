package com.monzy.jdbc.daos;

import com.database.Database;
import com.monzy.models.item.Item;
import com.monzy.models.item.ItemTime;
import com.monzy.models.player.Friend;
import com.monzy.models.player.Fusion;
import com.monzy.models.player.Inventory;
import com.monzy.models.player.Player;
import com.monzy.models.skill.Skill;
import com.monzy.server.Manager;
import com.monzy.services.MapService;
import com.monzy.utils.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class PlayerDAO {

  public static boolean createNewPlayer(int userId, String name, byte gender, int hair) {
    try {
      JSONArray dataArray = new JSONArray();
      dataArray.add(50000000000L); // vàng
      dataArray.add(1000); // ngọc xanh
      dataArray.add(1000000); // hồng ngọc
      dataArray.add(0); // point
      dataArray.add(0); // event
      String inventory = dataArray.toJSONString();
      dataArray.clear();
      dataArray.add(21 + gender); // map
      dataArray.add(100); // x
      dataArray.add(384); // y
      String location = dataArray.toJSONString();
      dataArray.clear();
      dataArray.add(0); // giới hạn sức mạnh
      dataArray.add(1500000000); // sức mạnh
      dataArray.add(1500000000); // tiềm năng
      dataArray.add(1000); // thể lực
      dataArray.add(1000); // thể lực đầy
      dataArray.add(gender == 0 ? 200 : 100); // hp gốc
      dataArray.add(gender == 1 ? 200 : 100); // ki gốc
      dataArray.add(gender == 2 ? 15 : 10); // sức đánh gốc
      dataArray.add(0); // giáp gốc
      dataArray.add(0); // chí mạng gốc
      dataArray.add(0); // năng động
      dataArray.add(gender == 0 ? 200 : 100); // hp hiện tại
      dataArray.add(gender == 1 ? 200 : 100); // ki hiện tại
      String point = dataArray.toJSONString();
      dataArray.clear();
      dataArray.add(1); // level
      dataArray.add(5); // current pea
      dataArray.add(0); // is upgrade
      dataArray.add(new Date().getTime()); // last time harvest
      dataArray.add(new Date().getTime()); // last time upgrade
      String magicTree = dataArray.toJSONString();
      dataArray.clear();
      /**
       * [ {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
       * {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
       * {"temp_id":"-1","option":[],"create_time":"0""}, ... ]
       */
      int idAo = gender;
      int idQuan = gender + 6;
      int def = gender == 2 ? 3 : 2;
      int hp = gender == 0 ? 30 : 20;
      JSONArray item = new JSONArray();
      JSONArray options = new JSONArray();
      JSONArray opt = new JSONArray();
      for (int i = 0; i < 12; i++) {
        if (i == 0) { // áo
          opt.add(47); // id option
          opt.add(def); // param option
          item.add(idAo); // id item
          item.add(1); // số lượng
          options.add(opt.toJSONString());
          opt.clear();
        } else if (i == 1) { // quần
          opt.add(6); // id option
          opt.add(hp); // param option
          item.add(idQuan); // id item
          item.add(1); // số lượng
          options.add(opt.toJSONString());
          opt.clear();
        } else {
          item.add(-1); // id item
          item.add(0); // số lượng
        }
        item.add(options.toJSONString()); // full option item
        item.add(System.currentTimeMillis()); // thời gian item được tạo
        dataArray.add(item.toJSONString());
        options.clear();
        item.clear();
      }
      String itemsBody = dataArray.toJSONString();
      dataArray.clear();
      for (int i = 0; i < 20; i++) { // item tạo player
        if (i == 0) { // thỏi vàng
          opt.add(30); // id option cấm giao dịch
          opt.add(1); // param option
          item.add(457); // id item
          item.add(1000); // số lượng
          options.add(opt.toJSONString());
          opt.clear();
        } else {
          item.add(-1); // id item
          item.add(0); // số lượng
        }
//        item.add(-1); // id item
//        item.add(0); // số lượng
        item.add(options.toJSONString()); // full option item
        item.add(System.currentTimeMillis()); // thời gian item được tạo
        dataArray.add(item.toJSONString());
        options.clear();
        item.clear();
      }
      String itemsBag = dataArray.toJSONString();
      dataArray.clear();
      for (int i = 0; i < 20; i++) {
        if (i == 0) { // rada
          opt.add(14); // id option
          opt.add(1); // param option
          item.add(12); // id item
          item.add(1); // số lượng
          options.add(opt.toJSONString());
          opt.clear();
        } else {
          item.add(-1); // id item
          item.add(0); // số lượng
        }
        item.add(options.toJSONString()); // full option item
        item.add(System.currentTimeMillis()); // thời gian item được tạo
        dataArray.add(item.toJSONString());
        options.clear();
        item.clear();
      }
      String itemsBox = dataArray.toJSONString();
      dataArray.clear();
      for (int i = 0; i < 110; i++) {
        item.add(-1); // id item
        item.add(0); // số lượng
        item.add(options.toJSONString()); // full option item
        item.add(System.currentTimeMillis()); // thời gian item được tạo
        dataArray.add(item.toJSONString());
        options.clear();
        item.clear();
      }
      String itemsBoxLuckyRound = dataArray.toJSONString();
      dataArray.clear();
      String friends = dataArray.toJSONString();
      String enemies = dataArray.toJSONString();
      dataArray.add(0); // id nội tại
      dataArray.add(0); // chỉ số 1
      dataArray.add(0); // chỉ số 2
      dataArray.add(0); // số lần mở
      String intrinsic = dataArray.toJSONString();
      dataArray.clear();
      dataArray.add(0); // bổ huyết
      dataArray.add(0); // bổ khí
      dataArray.add(0); // giáp xên
      dataArray.add(0); // cuồng nộ
      dataArray.add(0); // ẩn danh
      dataArray.add(0); // mở giới hạn sức mạnh
      dataArray.add(0); // máy dò
      dataArray.add(0); // máy dò2
      dataArray.add(0); // thức ăn cold
      dataArray.add(0); // icon thức ăn cold
      dataArray.add(0); // tdlt
      String itemTime = dataArray.toJSONString();
      dataArray.clear();
      dataArray.add(0); // bổ huyết2
      dataArray.add(0); // bổ khí2
      dataArray.add(0); // giáp xên2
      dataArray.add(0); // cuồng nộ2
      dataArray.add(0); // ẩn danh2
      String itemTimeSC = dataArray.toJSONString();
      dataArray.clear();
      dataArray.add(1); // id nhiệm vụ
      dataArray.add(0); // index nhiệm vụ con
      dataArray.add(0); // số lượng đã làm
      String task = dataArray.toJSONString();
      dataArray.clear();
      String mabuEgg = dataArray.toJSONString();
      String billEgg = dataArray.toJSONString();
      dataArray.add(System.currentTimeMillis()); // bùa trí tuệ
      dataArray.add(System.currentTimeMillis()); // bùa mạnh mẽ
      dataArray.add(System.currentTimeMillis()); // bùa da trâu
      dataArray.add(System.currentTimeMillis()); // bùa oai hùng
      dataArray.add(System.currentTimeMillis()); // bùa bất tử
      dataArray.add(System.currentTimeMillis()); // bùa dẻo dai
      dataArray.add(System.currentTimeMillis()); // bùa thu hút
      dataArray.add(System.currentTimeMillis()); // bùa đệ tử
      dataArray.add(System.currentTimeMillis()); // bùa trí tuệ x3
      dataArray.add(System.currentTimeMillis()); // bùa trí tuệ x4
      String charms = dataArray.toJSONString();
      dataArray.clear();
      int[] skillsArr =
          gender == 0
              ? new int[] {0, 1, 6, 9, 10, 20, 22, 24, 19}
              : gender == 1
                  ? new int[] {2, 3, 7, 11, 12, 17, 18, 26, 19}
                  : new int[] {4, 5, 8, 13, 14, 21, 23, 25, 19};
      // [{"temp_id":"4","point":0,"last_time_use":0},]
      JSONArray skill = new JSONArray();
      for (int i = 0; i < skillsArr.length; i++) {
        skill.add(skillsArr[i]); // id skill
        if (i == 0) {
          skill.add(1); // level skill
        } else {
          skill.add(0); // level skill
        }
        skill.add(0); // thời gian sử dụng trước đó
        dataArray.add(skill.toString());
        skill.clear();
      }
      String skills = dataArray.toJSONString();
      dataArray.clear();
      dataArray.add(gender == 0 ? 0 : gender == 1 ? 2 : 4);
      dataArray.add(-1);
      dataArray.add(-1);
      dataArray.add(-1);
      dataArray.add(-1);
      String skillsShortcut = dataArray.toJSONString();
      dataArray.clear();
      String petData = dataArray.toJSONString();
      JSONArray blackBall = new JSONArray();
      for (int i = 1; i <= 7; i++) {
        blackBall.add(0);
        blackBall.add(0);
        blackBall.add(0);
        dataArray.add(blackBall.toJSONString());
        blackBall.clear();
      }
      String dataBlackBall = dataArray.toString();
      dataArray.clear();
      dataArray.add(-1); // id side task
      dataArray.add(0); // thời gian nhận
      dataArray.add(0); // số lượng đã làm
      dataArray.add(0); // số lượng cần làm
      dataArray.add(20); // số nhiệm vụ còn lại có thể nhận
      dataArray.add(0); // mức độ nhiệm vụ
      String dataSideTask = dataArray.toJSONString();
      dataArray.clear();
      String data_card = dataArray.toJSONString();
      String bill_data = dataArray.toJSONString();
      Database.executeUpdate(
          "insert into player"
              + "(account_id, name, head, gender, have_tennis_space_ship, clan_id_sv"
              + Manager.SERVER
              + ", "
              + "data_inventory, data_location, data_point, data_magic_tree, items_body, "
              + "items_bag, items_box, items_box_lucky_round, friends, enemies, data_intrinsic, data_item_time, data_item_time_sieu_cap,"
              + "data_task, data_mabu_egg, data_charm, skills, skills_shortcut, pet,"
              + "data_black_ball, data_side_task, data_card, bill_data) "
              + "values ()",
          userId,
          name,
          hair,
          gender,
          0,
          -1,
          inventory,
          location,
          point,
          magicTree,
          itemsBody,
          itemsBag,
          itemsBox,
          itemsBoxLuckyRound,
          friends,
          enemies,
          intrinsic,
          itemTime,
          itemTimeSC,
          task,
          mabuEgg,
          charms,
          skills,
          skillsShortcut,
          petData,
          dataBlackBall,
          dataSideTask,
          data_card,
          bill_data);
      Logger.success("Tạo player mới thành công!");
      return true;
    } catch (Exception e) {
      Logger.logException(PlayerDAO.class, e, "Lỗi tạo player mới");
      return false;
    }
  }

  public static void updatePlayer(Player player) {
    if (player.iDMark.isLoadedAllDataPlayer()) {
      long st = System.currentTimeMillis();
      try {
        JSONArray dataArray = new JSONArray();
        // data kim lượng
        dataArray.add(
            player.inventory.gold > Inventory.LIMIT_GOLD
                ? Inventory.LIMIT_GOLD
                : player.inventory.gold);
        dataArray.add(player.inventory.gem);
        dataArray.add(player.inventory.ruby);
        dataArray.add(player.inventory.coupon);
        dataArray.add(player.inventory.event);
        String inventory = dataArray.toJSONString();
        dataArray.clear();
        int mapId = -1;
        mapId = player.mapIdBeforeLogout;
        int x = player.location.x;
        int y = player.location.y;
        int hp = player.nPoint.hp;
        int mp = player.nPoint.mp;
        if (player.isDie()) {
          mapId = player.gender + 21;
          x = 300;
          y = 336;
          hp = 1;
          mp = 1;
        } else {
          if (MapService.gI().isMapDoanhTrai(mapId)
              || MapService.gI().isMapBlackBallWar(mapId)
              || MapService.gI().isMapBanDoKhoBau(mapId)
              || MapService.gI().isMapMaBu(mapId)
              || MapService.gI().isNguHS(mapId)) {
            mapId = player.gender + 21;
            x = 300;
            y = 336;
          }
        }
        // data vị trí
        dataArray.add(mapId);
        dataArray.add(x);
        dataArray.add(y);
        String location = dataArray.toJSONString();
        dataArray.clear();
        // data chỉ số
        dataArray.add(player.nPoint.limitPower);
        dataArray.add(player.nPoint.power);
        dataArray.add(player.nPoint.tiemNang);
        dataArray.add(player.nPoint.stamina);
        dataArray.add(player.nPoint.maxStamina);
        dataArray.add(player.nPoint.hpg);
        dataArray.add(player.nPoint.mpg);
        dataArray.add(player.nPoint.dameg);
        dataArray.add(player.nPoint.defg);
        dataArray.add(player.nPoint.critg);
        dataArray.add(0);
        dataArray.add(hp);
        dataArray.add(mp);
        String point = dataArray.toJSONString();
        dataArray.clear();
        // data đậu thần
        dataArray.add(player.magicTree.level);
        dataArray.add(player.magicTree.currPeas);
        dataArray.add(player.magicTree.isUpgrade ? 1 : 0);
        dataArray.add(player.magicTree.lastTimeHarvest);
        dataArray.add(player.magicTree.lastTimeUpgrade);
        String magicTree = dataArray.toJSONString();
        dataArray.clear();
        // data body
        JSONArray dataItem = new JSONArray();
        for (Item item : player.inventory.itemsBody) {
          JSONArray opt = new JSONArray();
          if (item.isNotNullItem()) {
            dataItem.add(item.template.id);
            dataItem.add(item.quantity);
            JSONArray options = new JSONArray();
            for (Item.ItemOption io : item.itemOptions) {
              opt.add(io.optionTemplate.id);
              opt.add(io.param);
              options.add(opt.toJSONString());
              opt.clear();
            }
            dataItem.add(options.toJSONString());
          } else {
            dataItem.add(-1);
            dataItem.add(0);
            dataItem.add(opt.toJSONString());
          }
          dataItem.add(item.createTime);
          dataArray.add(dataItem.toJSONString());
          dataItem.clear();
        }
        String itemsBody = dataArray.toJSONString();
        dataArray.clear();
        // data bag
        for (Item item : player.inventory.itemsBag) {
          JSONArray opt = new JSONArray();
          if (item.isNotNullItem()) {
            dataItem.add(item.template.id);
            dataItem.add(item.quantity);
            JSONArray options = new JSONArray();
            for (Item.ItemOption io : item.itemOptions) {
              opt.add(io.optionTemplate.id);
              opt.add(io.param);
              options.add(opt.toJSONString());
              opt.clear();
            }
            dataItem.add(options.toJSONString());
          } else {
            dataItem.add(-1);
            dataItem.add(0);
            dataItem.add(opt.toJSONString());
          }
          dataItem.add(item.createTime);
          dataArray.add(dataItem.toJSONString());
          dataItem.clear();
        }
        String itemsBag = dataArray.toJSONString();
        dataArray.clear();
        // data card
        // data box
        for (Item item : player.inventory.itemsBox) {
          JSONArray opt = new JSONArray();
          if (item.isNotNullItem()) {
            dataItem.add(item.template.id);
            dataItem.add(item.quantity);
            JSONArray options = new JSONArray();
            for (Item.ItemOption io : item.itemOptions) {
              opt.add(io.optionTemplate.id);
              opt.add(io.param);
              options.add(opt.toJSONString());
              opt.clear();
            }
            dataItem.add(options.toJSONString());
          } else {
            dataItem.add(-1);
            dataItem.add(0);
            dataItem.add(opt.toJSONString());
          }
          dataItem.add(item.createTime);
          dataArray.add(dataItem.toJSONString());
          dataItem.clear();
        }
        String itemsBox = dataArray.toJSONString();
        dataArray.clear();
        // data box crack ball
        for (Item item : player.inventory.itemsBoxCrackBall) {
          JSONArray opt = new JSONArray();
          if (item.isNotNullItem()) {
            dataItem.add(item.template.id);
            dataItem.add(item.quantity);
            JSONArray options = new JSONArray();
            for (Item.ItemOption io : item.itemOptions) {
              opt.add(io.optionTemplate.id);
              opt.add(io.param);
              options.add(opt.toJSONString());
              opt.clear();
            }
            dataItem.add(options.toJSONString());
          } else {
            dataItem.add(-1);
            dataItem.add(0);
            dataItem.add(opt.toJSONString());
          }
          dataItem.add(item.createTime);
          dataArray.add(dataItem.toJSONString());
          dataItem.clear();
        }
        String itemsBoxLuckyRound = dataArray.toJSONString();
        dataArray.clear();
        // data bạn bè
        JSONArray dataFE = new JSONArray();
        for (Friend f : player.friends) {
          dataFE.add(f.id);
          dataFE.add(f.name);
          dataFE.add(f.head);
          dataFE.add(f.body);
          dataFE.add(f.leg);
          dataFE.add(f.bag);
          dataFE.add(f.power);
          dataArray.add(dataFE.toJSONString());
          dataFE.clear();
        }
        String friend = dataArray.toJSONString();
        dataArray.clear();
        // data kẻ thù
        for (Friend e : player.enemies) {
          dataFE.add(e.id);
          dataFE.add(e.name);
          dataFE.add(e.head);
          dataFE.add(e.body);
          dataFE.add(e.leg);
          dataFE.add(e.bag);
          dataFE.add(e.power);
          dataArray.add(dataFE.toJSONString());
          dataFE.clear();
        }
        String enemy = dataArray.toJSONString();
        dataArray.clear();
        // data nội tại
        dataArray.add(player.playerIntrinsic.intrinsic.id);
        dataArray.add(player.playerIntrinsic.intrinsic.param1);
        dataArray.add(player.playerIntrinsic.intrinsic.param2);
        dataArray.add(player.playerIntrinsic.countOpen);
        String intrinsic = dataArray.toJSONString();
        dataArray.clear();
        // data item time
        dataArray.add(
            (player.itemTime.isUseBoHuyet
                ? (ItemTime.TIME_ITEM
                    - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet))
                : 0));
        dataArray.add(
            (player.itemTime.isUseBoKhi
                ? (ItemTime.TIME_ITEM
                    - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi))
                : 0));
        dataArray.add(
            (player.itemTime.isUseGiapXen
                ? (ItemTime.TIME_ITEM
                    - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen))
                : 0));
        dataArray.add(
            (player.itemTime.isUseCuongNo
                ? (ItemTime.TIME_ITEM
                    - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo))
                : 0));
        dataArray.add(
            (player.itemTime.isUseAnDanh
                ? (ItemTime.TIME_ITEM
                    - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh))
                : 0));
        dataArray.add(
            (player.itemTime.isOpenPower
                ? (ItemTime.TIME_OPEN_POWER
                    - (System.currentTimeMillis() - player.itemTime.lastTimeOpenPower))
                : 0));
        dataArray.add(
            (player.itemTime.isUseMayDo
                ? (ItemTime.TIME_MAY_DO
                    - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo))
                : 0));
        dataArray.add(
            (player.itemTime.isUseMayDo2
                ? (ItemTime.TIME_MAY_DO
                    - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo2))
                : 0));
        dataArray.add(
            (player.itemTime.isEatMeal
                ? (ItemTime.TIME_EAT_MEAL
                    - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal))
                : 0));
        dataArray.add(player.itemTime.iconMeal);
        dataArray.add(
            (player.itemTime.isUseTDLT
                ? ((player.itemTime.timeTDLT
                        - (System.currentTimeMillis() - player.itemTime.lastTimeUseTDLT))
                    / 60
                    / 1000)
                : 0));
        String itemTime = dataArray.toJSONString();
        dataArray.clear();
        // data item time sc
        dataArray.add(
            (player.itemTime.isUseBoHuyet2
                ? (ItemTime.TIME_ITEM
                    - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet2))
                : 0));
        dataArray.add(
            (player.itemTime.isUseBoKhi2
                ? (ItemTime.TIME_ITEM
                    - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi2))
                : 0));
        dataArray.add(
            (player.itemTime.isUseGiapXen2
                ? (ItemTime.TIME_ITEM
                    - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen2))
                : 0));
        dataArray.add(
            (player.itemTime.isUseCuongNo2
                ? (ItemTime.TIME_ITEM
                    - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo2))
                : 0));
        dataArray.add(
            (player.itemTime.isUseAnDanh2
                ? (ItemTime.TIME_ITEM
                    - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh2))
                : 0));
        String itemTimeSC = dataArray.toJSONString();
        dataArray.clear();
        // data nhiệm vụ
        dataArray.add(player.playerTask.taskMain.id);
        dataArray.add(player.playerTask.taskMain.index);
        dataArray.add(
            player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count);
        String task = dataArray.toJSONString();
        dataArray.clear();
        // data nhiệm vụ hàng ngày
        dataArray.add(
            player.playerTask.sideTask.template != null
                ? player.playerTask.sideTask.template.id
                : -1);
        dataArray.add(player.playerTask.sideTask.receivedTime);
        dataArray.add(player.playerTask.sideTask.count);
        dataArray.add(player.playerTask.sideTask.maxCount);
        dataArray.add(player.playerTask.sideTask.leftTask);
        dataArray.add(player.playerTask.sideTask.level);
        String sideTask = dataArray.toJSONString();
        dataArray.clear();
        // data trứng bư
        if (player.mabuEgg != null) {
          dataArray.add(player.mabuEgg.lastTimeCreate);
          dataArray.add(player.mabuEgg.timeDone);
        }
        String mabuEgg = dataArray.toJSONString();
        dataArray.clear();
        // data trứng bill
        if (player.billEgg != null) {
          dataArray.add(player.billEgg.lastTimeCreate);
          dataArray.add(player.billEgg.timeDone);
        }
        String billEgg = dataArray.toJSONString();
        dataArray.clear();
        // data bùa
        dataArray.add(player.charms.tdTriTue);
        dataArray.add(player.charms.tdManhMe);
        dataArray.add(player.charms.tdDaTrau);
        dataArray.add(player.charms.tdOaiHung);
        dataArray.add(player.charms.tdBatTu);
        dataArray.add(player.charms.tdDeoDai);
        dataArray.add(player.charms.tdThuHut);
        dataArray.add(player.charms.tdDeTu);
        dataArray.add(player.charms.tdTriTue3);
        dataArray.add(player.charms.tdTriTue4);
        String charm = dataArray.toJSONString();
        dataArray.clear();
        // data skill
        JSONArray dataSkill = new JSONArray();
        for (Skill skill : player.playerSkill.skills) {
          dataSkill.add(skill.template.id);
          dataSkill.add(skill.point);
          dataSkill.add(skill.lastTimeUseThisSkill);
          dataArray.add(dataSkill.toJSONString());
          dataSkill.clear();
        }
        String skills = dataArray.toJSONString();
        dataArray.clear();
        dataArray.clear();
        // data skill shortcut
        for (int skillId : player.playerSkill.skillShortCut) {
          dataArray.add(skillId);
        }
        String skillShortcut = dataArray.toJSONString();
        dataArray.clear();
        String pet = dataArray.toJSONString();
        String petInfo = dataArray.toJSONString();
        String petPoint = dataArray.toJSONString();
        String petBody = dataArray.toJSONString();
        String petSkill = dataArray.toJSONString();
        // data pet
        if (player.pet != null) {
          dataArray.add(player.pet.typePet);
          dataArray.add(player.pet.gender);
          dataArray.add(player.pet.name);
          dataArray.add(player.fusion.typeFusion);
          int timeLeftFusion =
              (int)
                  (Fusion.TIME_FUSION
                      - (System.currentTimeMillis() - player.fusion.lastTimeFusion));
          dataArray.add(timeLeftFusion < 0 ? 0 : timeLeftFusion);
          dataArray.add(player.pet.status);
          petInfo = dataArray.toJSONString();
          dataArray.clear();
          dataArray.add(player.pet.nPoint.limitPower);
          dataArray.add(player.pet.nPoint.power);
          dataArray.add(player.pet.nPoint.tiemNang);
          dataArray.add(player.pet.nPoint.stamina);
          dataArray.add(player.pet.nPoint.maxStamina);
          dataArray.add(player.pet.nPoint.hpg);
          dataArray.add(player.pet.nPoint.mpg);
          dataArray.add(player.pet.nPoint.dameg);
          dataArray.add(player.pet.nPoint.defg);
          dataArray.add(player.pet.nPoint.critg);
          dataArray.add(player.pet.nPoint.hp);
          dataArray.add(player.pet.nPoint.mp);
          petPoint = dataArray.toJSONString();
          dataArray.clear();
          JSONArray items = new JSONArray();
          JSONArray options = new JSONArray();
          JSONArray opt = new JSONArray();
          for (Item item : player.pet.inventory.itemsBody) {
            if (item.isNotNullItem()) {
              dataItem.add(item.template.id);
              dataItem.add(item.quantity);
              for (Item.ItemOption io : item.itemOptions) {
                opt.add(io.optionTemplate.id);
                opt.add(io.param);
                options.add(opt.toJSONString());
                opt.clear();
              }
              dataItem.add(options.toJSONString());
            } else {
              dataItem.add(-1);
              dataItem.add(0);
              dataItem.add(options.toJSONString());
            }
            dataItem.add(item.createTime);
            items.add(dataItem.toJSONString());
            dataItem.clear();
            options.clear();
          }
          petBody = items.toJSONString();
          JSONArray petSkills = new JSONArray();
          for (Skill s : player.pet.playerSkill.skills) {
            JSONArray pskill = new JSONArray();
            if (s.skillId != -1) {
              pskill.add(s.template.id);
              pskill.add(s.point);
            } else {
              pskill.add(-1);
              pskill.add(0);
            }
            petSkills.add(pskill.toJSONString());
          }
          petSkill = petSkills.toJSONString();
          dataArray.add(petInfo);
          dataArray.add(petPoint);
          dataArray.add(petBody);
          dataArray.add(petSkill);
          pet = dataArray.toJSONString();
        }
        dataArray.clear();
        // data thưởng ngọc rồng đen
        for (int i = 0; i < player.rewardBlackBall.timeOutOfDateReward.length; i++) {
          JSONArray dataBlackBall = new JSONArray();
          dataBlackBall.add(player.rewardBlackBall.timeOutOfDateReward[i]);
          dataBlackBall.add(player.rewardBlackBall.lastTimeGetReward[i]);
          dataBlackBall.add(player.rewardBlackBall.quantilyBlackBall[i]);
          dataArray.add(dataBlackBall.toJSONString());
          dataBlackBall.clear();
        }
        String dataBlackBall = dataArray.toJSONString();
        dataArray.clear();
        String query =
            " update player set head = ?, have_tennis_space_ship = ?,"
                + "clan_id_sv"
                + Manager.SERVER
                + " = ?, data_inventory = ?, data_location = ?, data_point = ?, data_magic_tree = ?,"
                + "items_body = ?, items_bag = ?, items_box = ?, items_box_lucky_round = ?, friends = ?,"
                + "enemies = ?, data_intrinsic = ?, data_item_time = ?, data_item_time_sieu_cap = ?, data_task = ?, data_mabu_egg = ?, pet = ?,"
                + "data_black_ball = ?, data_side_task = ?, data_charm = ?, skills = ?,"
                + " skills_shortcut = ?, pointPvp=?, event=?,data_card=?,bill_data =? where id = ?";
        Database.executeUpdate(
            query,
            player.head,
            player.haveTennisSpaceShip,
            (player.clan != null ? player.clan.id : -1),
            inventory,
            location,
            point,
            magicTree,
            itemsBody,
            itemsBag,
            itemsBox,
            itemsBoxLuckyRound,
            friend,
            enemy,
            intrinsic,
            itemTime,
            itemTimeSC,
            task,
            mabuEgg,
            pet,
            dataBlackBall,
            sideTask,
            charm,
            skills,
            skillShortcut,
            player.pointPvp,
            player.event,
            JSONValue.toJSONString(player.cards),
            billEgg,
            player.id);
        Logger.success(
            "Total time save player "
                + player.name
                + " thành công! "
                + (System.currentTimeMillis() - st));
      } catch (Exception e) {
        Logger.logException(PlayerDAO.class, e, "Lỗi save player " + player.name);
      }
    }
  }

  public static boolean subVND(Player player, int num) {
    PreparedStatement ps = null;
    try (Connection con = Database.getConnection()) {
      ps =
          con.prepareStatement(
              "update account set vnd = (vnd - ?), active = ? where id = ?",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      ps.setInt(1, num);
      ps.setInt(2, player.session.actived ? 1 : 0);
      ps.setInt(3, player.session.userId);
      ps.executeUpdate();
      ps.close();
      player.session.vnd -= num;
    } catch (Exception e) {
      Logger.logException(PlayerDAO.class, e, "Lỗi update Coin " + player.name);
      return false;
    }
    return true;
  }

  public static boolean addTongNap(Player player, int num) {
    PreparedStatement ps;
    try (Connection con = Database.getConnection()) {
      ps =
          con.prepareStatement(
              "update account set tongnap = (tongnap + ?), active = ? where id = ?",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      ps.setInt(1, num);
      ps.setInt(2, player.session.actived ? 1 : 0);
      ps.setInt(3, player.session.userId);
      ps.executeUpdate();
      ps.close();
      player.session.tongnap += num;
    } catch (Exception e) {
      Logger.logException(PlayerDAO.class, e, "Lỗi update Tổng nạp " + player.name);
      return false;
    }
    return true;
  }

  public static boolean addVND(Player player, int num) {
    PreparedStatement ps;
    try (Connection con = Database.getConnection()) {
      ps =
          con.prepareStatement(
              "update account set vnd = (vnd + ?), active = ? where id = ?",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      ps.setInt(1, num);
      ps.setInt(2, player.session.actived ? 1 : 0);
      ps.setInt(3, player.session.userId);
      ps.executeUpdate();
      ps.close();
      player.session.vnd += num;
    } catch (Exception e) {
      Logger.logException(PlayerDAO.class, e, "Lỗi update Coin " + player.name);
      return false;
    }
    return true;
  }

  public static boolean setIs_gift_box(Player player) {
    PreparedStatement ps = null;
    try (Connection con = Database.getConnection()) {
      ps =
          con.prepareStatement(
              "update account set is_gift_box = 0 where id = ?",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      ps.setInt(1, player.session.userId);
      ps.executeUpdate();
      ps.close();
    } catch (Exception e) {
      Logger.logException(PlayerDAO.class, e, "Lỗi update new_reg " + player.name);
      return false;
    }
    return true;
  }

  public static boolean checkLogout(Connection con, Player player) {
    long lastTimeLogout = 0;
    long lastTimeLogin = 0;
    try {
      PreparedStatement ps =
          con.prepareStatement(
              "select * from account where id = ? limit 1",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      ps.setInt(1, player.session.userId);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
        lastTimeLogin = rs.getTimestamp("last_time_login").getTime();
      }
      try {
        rs.close();
        ps.close();
      } catch (Exception ignored) {
      }
    } catch (Exception e) {
      return false;
    }
    return lastTimeLogout > lastTimeLogin;
  }

  public static void LogNapTien(
      String uid, String menhgia, String seri, String code, String tranid) {
    String UPDATE_PASS =
        "INSERT INTO naptien(uid,sotien,seri,code,loaithe,time,noidung,tinhtrang,tranid,magioithieu) VALUES(?,?,?,?,?,?,?,?,?,?)";
    try {
      Connection conn = Database.getConnection();
      PreparedStatement ps = null;
      // UPDATE NRSD,
      ps =
          conn.prepareStatement(
              UPDATE_PASS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      conn.setAutoCommit(false);
      // NGOC RONG SAO DEN
      ps.setString(1, uid);
      ps.setString(2, menhgia);
      ps.setString(3, seri);
      ps.setString(4, code);
      ps.setString(5, "VIETTEL");
      ps.setString(6, "123123123123");
      ps.setString(7, "dang nap the");
      ps.setString(8, "0");
      ps.setString(9, tranid);
      ps.setString(10, "0");
      if (ps.executeUpdate() == 1) {}
      conn.commit();
      // UPDATE NRSD
      conn.close();
    } catch (SQLException e) {
      Logger.logException(GodGK.class, e);
    }
  }

  public static boolean activedUser(Player player) {
    try (Connection con = Database.getConnection()) {
      PreparedStatement ps =
          con.prepareStatement(
              "update account set active = ? where id = ?",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      ps.setInt(1, player.session.actived ? 1 : 0);
      ps.setInt(2, player.session.userId);
      ps.executeUpdate();
      ps.close();
      return true;
    } catch (Exception e) {
      Logger.error("Lỗi active user: " + e.getMessage());
      return false;
    }
  }
}

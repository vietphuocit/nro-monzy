package com.monzy.server;

import com.database.Database;
import com.monzy.card.OptionCard;
import com.monzy.card.RadarCard;
import com.monzy.card.RadarService;
import com.monzy.consts.ConstMap;
import com.monzy.consts.ConstPlayer;
import com.monzy.data.DataGame;
import com.monzy.kygui.ItemKyGui;
import com.monzy.kygui.ShopKyGuiManager;
import com.monzy.models.Template.*;
import com.monzy.models.clan.Clan;
import com.monzy.models.clan.ClanMember;
import com.monzy.models.intrinsic.Intrinsic;
import com.monzy.models.item.Item;
import com.monzy.models.map.WayPoint;
import com.monzy.models.matches.TOP;
import com.monzy.models.matches.pvp.DaiHoiVoThuat;
import com.monzy.models.npc.Npc;
import com.monzy.models.npc.NpcFactory;
import com.monzy.models.shop.Shop;
import com.monzy.models.skill.NClass;
import com.monzy.models.skill.Skill;
import com.monzy.models.task.SideTaskTemplate;
import com.monzy.models.task.SubTaskMain;
import com.monzy.models.task.TaskMain;
import com.monzy.services.ItemService;
import com.monzy.services.MapService;
import com.monzy.utils.Logger;
import com.monzy.utils.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Manager {

  public static final List<com.monzy.models.map.Map> MAPS = new ArrayList<>();
  public static final List<ItemOptionTemplate> ITEM_OPTION_TEMPLATES = new ArrayList<>();
  public static final List<ItemLuckyRound> LUCKY_ROUND_REWARDS = new ArrayList<>();
  public static final Map<String, Byte> IMAGES_BY_NAME = new HashMap<String, Byte>();
  public static final List<ItemTemplate> ITEM_TEMPLATES = new ArrayList<>();
  public static final List<MobTemplate> MOB_TEMPLATES = new ArrayList<>();
  public static final List<NpcTemplate> NPC_TEMPLATES = new ArrayList<>();
  public static final List<String> CAPTIONS = new ArrayList<>();
  public static final List<TaskMain> TASKS = new ArrayList<>();
  public static final List<SideTaskTemplate> SIDE_TASKS_TEMPLATE = new ArrayList<>();
  public static final List<Intrinsic> INTRINSICS = new ArrayList<>();
  public static final List<Intrinsic> INTRINSIC_TD = new ArrayList<>();
  public static final List<Intrinsic> INTRINSIC_NM = new ArrayList<>();
  public static final List<Intrinsic> INTRINSIC_XD = new ArrayList<>();
  public static final List<HeadAvatar> HEAD_AVATARS = new ArrayList<>();
  public static final List<FlagBag> FLAGS_BAGS = new ArrayList<>();
  public static final List<NClass> NCLASS = new ArrayList<>();
  public static final List<Npc> NPCS = new ArrayList<>();
  public static final List<Clan> CLANS = new ArrayList<>();
  public static final List<String> NOTIFY = new ArrayList<>();
  public static final ArrayList<DaiHoiVoThuat> LIST_DHVT = new ArrayList<>();
  public static final List<Item> RUBY_REWARDS = new ArrayList<>();
  public static final String QUERY_TOP_SM =
      "SELECT player.id, CAST( split_str(data_point,',',2) AS UNSIGNED) AS sm\n"
          + "FROM player JOIN account ON player.account_id = account.id\n"
          + "WHERE account.create_time > TIMESTAMP('2023-7-13 00:00:00')\n"
          + "ORDER BY CAST( split_str(data_point,',',2)  AS UNSIGNED) DESC\n"
          + "LIMIT 20;";
  //    public static final String queryTopSD = "SELECT id, CAST( split_str(data_point,',',8) AS
  // UNSIGNED) AS sd FROM player ORDER BY CAST( split_str(data_point,',',8)  AS UNSIGNED) DESC LIMIT
  // 20;";
  //    public static final String queryTopHP = "SELECT id, CAST( split_str(data_point,',',6) AS
  // UNSIGNED) AS hp FROM player ORDER BY CAST( split_str(data_point,',',6)  AS UNSIGNED) DESC LIMIT
  // 20;";
  //    public static final String queryTopKI = "SELECT id, CAST( split_str(data_point,',',7) AS
  // UNSIGNED) AS ki FROM player ORDER BY CAST( split_str(data_point,',',7)  AS UNSIGNED) DESC LIMIT
  // 20;";
  public static final String QUERY_TOP_NV =
      "SELECT player.id, CAST( split_str(split_str(data_task,',',1),'[',2)  AS UNSIGNED) AS nv\n"
          + "FROM player JOIN account on player.account_id = account.id\n"
          + "WHERE account.create_time > TIMESTAMP('2023-7-13 00:00:00')\n"
          + "ORDER BY CAST( split_str(split_str(data_task,',',1),'[',2)  AS UNSIGNED) DESC, CAST(split_str(data_task,',',2)  AS UNSIGNED) DESC, CAST( split_str(data_point,',',2) AS UNSIGNED) DESC\n"
          + "LIMIT 20;";
  //    public static final String queryTopSK = "SELECT id, CAST( split_str( data_inventory,',',5)
  // AS UNSIGNED) AS event FROM player ORDER BY CAST( split_str( data_inventory,',',5)  AS UNSIGNED)
  // DESC LIMIT 20;";
  //    public static final String queryTopPVP = "SELECT id, CAST( pointPvp AS UNSIGNED) AS pointPvp
  // FROM player ORDER BY CAST( pointPvp AS UNSIGNED) DESC LIMIT 50;";
  //    public static final String queryTopNHS = "SELECT id, CAST( NguHanhSonPoint AS UNSIGNED) AS
  // nhs FROM player ORDER BY CAST( NguHanhSonPoint AS UNSIGNED) DESC LIMIT 20;";
  public static final String QUERY_TOP_NAP =
      "SELECT player_id as id, SUM(amount) as tong FROM tran_his\n"
          + "WHERE command = 'nap' AND create_at between TIMESTAMP('2023-7-13 00:00:00') and TIMESTAMP('2023-7-19 23:59:59')\n"
          + "GROUP BY player_id\n"
          + "ORDER BY SUM(amount) DESC \n"
          + "limit 10;";
  public static final byte[] itemIds_NR_SB = {16, 17, 15};
  public static final short[] itemDC12 = {
    233, 237, 241, 245, 249, 253, 257, 261, 265, 269, 273, 277
  };
  public static final short[] THUC_AN = {663, 664, 665, 666, 667};
  public static final short[] DA_NANG_CAP = {220, 221, 222, 223, 224};
  // id đồ shop
  public static final short[] IDS_AO_TD = {0, 33, 3, 34, 136, 137, 138, 139, 230, 231, 232, 233};
  public static final short[] IDS_QUAN_TD = {6, 35, 9, 36, 140, 141, 142, 143, 242, 243, 244, 245};
  public static final short[] IDS_GANG_TD = {
    21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257
  };
  public static final short[] IDS_GIAY_TD = {
    27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269
  };
  public static final short[] IDS_AO_XD = {2, 49, 5, 50, 168, 169, 170, 171, 238, 239, 240, 241};
  public static final short[] IDS_QUAN_XD = {8, 51, 11, 52, 172, 173, 174, 175, 250, 251, 252, 253};
  public static final short[] IDS_GANG_XD = {
    23, 53, 26, 54, 176, 177, 178, 179, 262, 263, 264, 265
  };
  public static final short[] IDS_GIAY_XD = {
    29, 55, 32, 56, 180, 181, 182, 183, 274, 275, 276, 277
  };
  public static final short[] IDS_AO_NM = {1, 41, 4, 42, 152, 153, 154, 155, 234, 235, 236, 237};
  public static final short[] IDS_QUAN_NM = {7, 43, 10, 44, 156, 157, 158, 159, 246, 247, 248, 249};
  public static final short[] IDS_GANG_NM = {
    22, 46, 25, 45, 160, 161, 162, 163, 258, 259, 260, 261
  };
  public static final short[] IDS_GIAY_NM = {
    28, 47, 31, 48, 164, 165, 166, 167, 270, 271, 272, 273
  };
  public static final short[] IDS_RADAR = {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281};
  public static final short[][][] IDS_TRANG_BI_SHOP = {
    {IDS_AO_TD, IDS_QUAN_TD, IDS_GANG_TD, IDS_GIAY_TD},
    {IDS_AO_NM, IDS_QUAN_NM, IDS_GANG_NM, IDS_GIAY_NM},
    {IDS_AO_XD, IDS_QUAN_XD, IDS_GANG_XD, IDS_GIAY_XD},
    {IDS_RADAR}
  };
  // id đồ thần
  public static final int[][] IDS_DO_THAN = {{555, 556, 562, 563, 561}, {557, 558, 564, 565, 561}, {559, 560, 566, 567, 561}};
  // id đồ hủy diệt
  public static final short ID_RADAR_HD = 656;
  public static final short[][] IDS_DO_HUY_DIET = {
    {650, 651, 657, 658}, {652, 653, 659, 660}, {654, 655, 661, 662}
  };
  // id đồ thiên sứ
  public static final short[][] IDS_DO_THIEN_SU = {
    {1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}
  };
  public static byte SERVER = 1;
  public static byte SECOND_WAIT_LOGIN = 5;
  public static int MAX_PER_IP = 5;
  public static int MAX_PLAYER = 1000;
  public static byte RATE_EXP = 1;
  public static byte RATE_PAY = 1;
  public static boolean LOCAL = false;
  //    public static byte RATE_EXP_SERVER = 1;// sau khi chinh
  public static MapTemplate[] MAP_TEMPLATES;
  public static List<Shop> SHOPS = new ArrayList<>();
  public static List<TOP> TOP_SM;
  //    public static List<TOP> topSD;
  //    public static List<TOP> topHP;
  //    public static List<TOP> topKI;
  public static List<TOP> TOP_NV;
  //    public static List<TOP> topSK;
  //    public static List<TOP> topPVP;
  //    public static List<TOP> topNHS;
  public static List<TOP> TOP_NAP;
  public static long TIME_READ_TOP = 1;
  private static Manager i;

  private Manager() {
    try {
      loadProperties();
    } catch (IOException ex) {
      Logger.logException(Manager.class, ex, "Lỗi load properites");
      System.exit(0);
    }
    this.loadDatabase();
    NpcFactory.createNpcConMeo();
    NpcFactory.createNpcRongThieng();
    this.initMap();
  }

  public static Manager gI() {
    if (i == null) {
      i = new Manager();
    }
    return i;
  }

  public static List<TOP> readTop(String query) {
    List<TOP> tops = new ArrayList<>();
    try (Connection con = Database.getConnection()) {
      PreparedStatement ps =
          con.prepareStatement(
              query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        TOP top = TOP.builder().id_player(rs.getInt("id")).build();
        switch (query) {
          case QUERY_TOP_SM:
            top.setInfo1(rs.getLong("sm") + "");
            top.setInfo2(rs.getLong("sm") + "");
            break;
          case QUERY_TOP_NV:
            top.setInfo1(rs.getByte("nv") + "");
            top.setInfo2(rs.getByte("nv") + "");
            break;
          case QUERY_TOP_NAP:
            top.setInfo1(rs.getInt("tong") + " vnd");
            top.setInfo2(rs.getInt("tong") + " vnd");
            break;
        }
        tops.add(top);
      }
    } catch (Exception e) {
      Logger.logException(Manager.class, e);
    }
    return tops;
  }

  public static void addClan(Clan clan) {
    CLANS.add(clan);
  }

  public static int getNumClan() {
    return CLANS.size();
  }

  public static MobTemplate getMobTemplateByTemp(int mobTempId) {
    for (MobTemplate mobTemp : MOB_TEMPLATES) {
      if (mobTemp.id == mobTempId) {
        return mobTemp;
      }
    }
    return null;
  }

  public static byte getNFrameImageByName(String name) {
    Object n = IMAGES_BY_NAME.get(name);
    if (n != null) {
      return Byte.parseByte(String.valueOf(n));
    } else {
      return 0;
    }
  }

  private void initMap() {
    int[][] tileTyleTop = MapService.gI().readTileIndexTileType(ConstMap.TILE_TOP);
    for (MapTemplate mapTemp : MAP_TEMPLATES) {
      int[][] tileMap = MapService.gI().readTileMap(mapTemp.id);
      int[] tileTop = tileTyleTop[mapTemp.tileId - 1];
      com.monzy.models.map.Map map =
          new com.monzy.models.map.Map(
              mapTemp.id,
              mapTemp.name,
              mapTemp.planetId,
              mapTemp.tileId,
              mapTemp.bgId,
              mapTemp.bgType,
              mapTemp.type,
              tileMap,
              tileTop,
              mapTemp.zones,
              mapTemp.maxPlayerPerZone,
              mapTemp.wayPoints);
      MAPS.add(map);
      map.initMob(mapTemp.mobTemp, mapTemp.mobLevel, mapTemp.mobHp, mapTemp.mobX, mapTemp.mobY);
      map.initNpc(mapTemp.npcId, mapTemp.npcX, mapTemp.npcY);
      new Thread(map, "Update map " + map.mapName).start();
    }
    Logger.info("Init map thành công!");
  }

  /** Load database */
  private void loadDatabase() {
    long st = System.currentTimeMillis();
    JSONArray dataArray;
    JSONObject dataObject = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try (Connection con = Database.getConnection()) {
      // load part
      ps =
          con.prepareStatement(
              "select * from part", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      List<Part> parts = new ArrayList<>();
      while (rs.next()) {
        Part part = new Part();
        part.id = rs.getShort("id");
        part.type = rs.getByte("type");
        dataArray = (JSONArray) JSONValue.parse(rs.getString("data").replaceAll("\\\"", ""));
        for (int j = 0; j < dataArray.size(); j++) {
          JSONArray pd = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(j)));
          part.partDetails.add(
              new PartDetail(
                  Short.parseShort(String.valueOf(pd.get(0))),
                  Byte.parseByte(String.valueOf(pd.get(1))),
                  Byte.parseByte(String.valueOf(pd.get(2)))));
          pd.clear();
        }
        parts.add(part);
        dataArray.clear();
      }
      DataOutputStream dos =
          new DataOutputStream(new FileOutputStream("data/monzy/update_data/part"));
      dos.writeShort(parts.size());
      for (Part part : parts) {
        dos.writeByte(part.type);
        for (PartDetail partDetail : part.partDetails) {
          dos.writeShort(partDetail.iconId);
          dos.writeByte(partDetail.dx);
          dos.writeByte(partDetail.dy);
        }
      }
      dos.flush();
      dos.close();
      Logger.info("Load part thành công (" + parts.size() + ")");
      // load small version
      ps =
          con.prepareStatement(
              "select count(id) from small_version",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      List<byte[]> smallVersion = new ArrayList<>();
      if (rs.first()) {
        int size = rs.getInt(1);
        for (int i = 0; i < 4; i++) {
          smallVersion.add(new byte[size]);
        }
      }
      ps =
          con.prepareStatement(
              "select * from small_version order by id",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      int index = 0;
      while (rs.next()) {
        smallVersion.get(0)[index] = rs.getByte("x1");
        smallVersion.get(1)[index] = rs.getByte("x2");
        smallVersion.get(2)[index] = rs.getByte("x3");
        smallVersion.get(3)[index] = rs.getByte("x4");
        index++;
      }
      for (int i = 0; i < 4; i++) {
        dos =
            new DataOutputStream(
                new FileOutputStream("data/monzy/data_img_version/x" + (i + 1) + "/img_version"));
        dos.writeShort(smallVersion.get(i).length);
        for (int j = 0; j < smallVersion.get(i).length; j++) {
          dos.writeByte(smallVersion.get(i)[j]);
        }
        dos.flush();
        dos.close();
      }
      // load clan
      ps =
          con.prepareStatement(
              "select * from clan_sv" + SERVER,
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      while (rs.next()) {
        Clan clan = new Clan();
        clan.id = rs.getInt("id");
        clan.name = rs.getString("name");
        clan.slogan = rs.getString("slogan");
        clan.imgId = rs.getByte("img_id");
        clan.powerPoint = rs.getLong("power_point");
        clan.maxMember = rs.getByte("max_member");
        clan.capsuleClan = rs.getInt("clan_point");
        clan.level = rs.getByte("level");
        clan.createTime = (int) (rs.getTimestamp("create_time").getTime() / 1000);
        clan.lastTimeOpenDTDN = rs.getLong("last_time_dt");
        dataArray = (JSONArray) JSONValue.parse(rs.getString("members"));
        for (int i = 0; i < dataArray.size(); i++) {
          dataObject = (JSONObject) JSONValue.parse(String.valueOf(dataArray.get(i)));
          ClanMember cm = new ClanMember();
          cm.clan = clan;
          cm.id = Integer.parseInt(String.valueOf(dataObject.get("id")));
          cm.name = String.valueOf(dataObject.get("name"));
          cm.head = Short.parseShort(String.valueOf(dataObject.get("head")));
          cm.body = Short.parseShort(String.valueOf(dataObject.get("body")));
          cm.leg = Short.parseShort(String.valueOf(dataObject.get("leg")));
          cm.role = Byte.parseByte(String.valueOf(dataObject.get("role")));
          cm.donate = Integer.parseInt(String.valueOf(dataObject.get("donate")));
          cm.receiveDonate = Integer.parseInt(String.valueOf(dataObject.get("receive_donate")));
          cm.memberPoint = Integer.parseInt(String.valueOf(dataObject.get("member_point")));
          cm.clanPoint = Integer.parseInt(String.valueOf(dataObject.get("clan_point")));
          cm.joinTime = Integer.parseInt(String.valueOf(dataObject.get("join_time")));
          cm.timeAskPea = Long.parseLong(String.valueOf(dataObject.get("ask_pea_time")));
          try {
            cm.powerPoint = Long.parseLong(String.valueOf(dataObject.get("power")));
          } catch (Exception e) {
            Logger.logException(Manager.class, e);
          }
          clan.addClanMember(cm);
        }
        CLANS.add(clan);
        dataArray.clear();
        dataObject.clear();
      }
      ps =
          con.prepareStatement(
              "select id from clan_sv" + SERVER + " order by id desc limit 1",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      if (rs.first()) {
        Clan.NEXT_ID = rs.getInt("id") + 1;
      }
      Logger.info(
          "Load clan thành công (" + CLANS.size() + "), clan next id: " + Clan.NEXT_ID);
      ps =
          con.prepareStatement(
              "select * from dhvt_template",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      if (rs.next()) {
        DaiHoiVoThuat dhvt = new DaiHoiVoThuat();
        dhvt.NameCup = rs.getString(2);
        dhvt.Time = rs.getString(3).split("\n");
        dhvt.gem = rs.getInt(4);
        dhvt.gold = rs.getInt(5);
        dhvt.min_start = rs.getInt(6);
        dhvt.min_start_temp = rs.getInt(6);
        dhvt.min_limit = rs.getInt(7);
        LIST_DHVT.add(dhvt);
      }
      Logger.info(
          "Load DHVT thành công (" + LIST_DHVT.size() + "), clan next id: " + Clan.NEXT_ID);
      // load skill
      ps =
          con.prepareStatement(
              "select * from skill_template order by nclass_id, slot",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      byte nClassId = -1;
      NClass nClass = null;
      while (rs.next()) {
        byte id = rs.getByte("nclass_id");
        if (id != nClassId) {
          nClassId = id;
          nClass = new NClass();
          nClass.name =
              id == ConstPlayer.TRAI_DAT ? "Trái Đất" : id == ConstPlayer.NAMEC ? "Namếc" : "Xayda";
          nClass.classId = nClassId;
          NCLASS.add(nClass);
        }
        SkillTemplate skillTemplate = new SkillTemplate();
        skillTemplate.classId = nClassId;
        skillTemplate.id = rs.getByte("id");
        skillTemplate.name = rs.getString("name");
        skillTemplate.maxPoint = rs.getByte("max_point");
        skillTemplate.manaUseType = rs.getByte("mana_use_type");
        skillTemplate.type = rs.getByte("type");
        skillTemplate.iconId = rs.getShort("icon_id");
        skillTemplate.damInfo = rs.getString("dam_info");
        nClass.skillTemplatess.add(skillTemplate);
        dataArray =
            (JSONArray)
                JSONValue.parse(
                    rs.getString("skills")
                        .replaceAll("\\[\"", "[")
                        .replaceAll("\"\\[", "[")
                        .replaceAll("\"\\]", "]")
                        .replaceAll("\\]\"", "]")
                        .replaceAll("\\}\",\"\\{", "},{"));
        for (int j = 0; j < dataArray.size(); j++) {
          JSONObject dts = (JSONObject) JSONValue.parse(String.valueOf(dataArray.get(j)));
          Skill skill = new Skill();
          skill.template = skillTemplate;
          skill.skillId = Short.parseShort(String.valueOf(dts.get("id")));
          skill.point = Byte.parseByte(String.valueOf(dts.get("point")));
          skill.powRequire = Long.parseLong(String.valueOf(dts.get("power_require")));
          skill.manaUse = Integer.parseInt(String.valueOf(dts.get("mana_use")));
          skill.coolDown = Integer.parseInt(String.valueOf(dts.get("cool_down")));
          skill.dx = Integer.parseInt(String.valueOf(dts.get("dx")));
          skill.dy = Integer.parseInt(String.valueOf(dts.get("dy")));
          skill.maxFight = Integer.parseInt(String.valueOf(dts.get("max_fight")));
          skill.damage = Short.parseShort(String.valueOf(dts.get("damage")));
          skill.price = Short.parseShort(String.valueOf(dts.get("price")));
          skill.moreInfo = String.valueOf(dts.get("info"));
          skillTemplate.skillss.add(skill);
        }
      }
      Logger.info("Load skill thành công (" + NCLASS.size() + ")");
      // load head avatar
      ps =
          con.prepareStatement(
              "select * from head_avatar",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      while (rs.next()) {
        HeadAvatar headAvatar = new HeadAvatar(rs.getInt("head_id"), rs.getInt("avatar_id"));
        HEAD_AVATARS.add(headAvatar);
      }
      Logger.info("Load head avatar thành công (" + HEAD_AVATARS.size() + ")");
      // load flag bag
      ps =
          con.prepareStatement(
              "select * from flag_bag",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      while (rs.next()) {
        FlagBag flagBag = new FlagBag();
        flagBag.id = rs.getByte("id");
        flagBag.name = rs.getString("name");
        flagBag.gold = rs.getInt("gold");
        flagBag.gem = rs.getInt("gem");
        flagBag.iconId = rs.getShort("icon_id");
        String[] iconData = rs.getString("icon_data").split(",");
        flagBag.iconEffect = new short[iconData.length];
        for (int j = 0; j < iconData.length; j++) {
          flagBag.iconEffect[j] = Short.parseShort(iconData[j].trim());
        }
        FLAGS_BAGS.add(flagBag);
      }
      Logger.info("Load flag bag thành công (" + FLAGS_BAGS.size() + ")");
      // load intrinsic
      ps =
          con.prepareStatement(
              "select * from intrinsic",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      while (rs.next()) {
        Intrinsic intrinsic = new Intrinsic();
        intrinsic.id = rs.getByte("id");
        intrinsic.name = rs.getString("name");
        intrinsic.paramFrom1 = rs.getShort("param_from_1");
        intrinsic.paramTo1 = rs.getShort("param_to_1");
        intrinsic.paramFrom2 = rs.getShort("param_from_2");
        intrinsic.paramTo2 = rs.getShort("param_to_2");
        intrinsic.icon = rs.getShort("icon");
        intrinsic.gender = rs.getByte("gender");
        switch (intrinsic.gender) {
          case ConstPlayer.TRAI_DAT:
            INTRINSIC_TD.add(intrinsic);
            break;
          case ConstPlayer.NAMEC:
            INTRINSIC_NM.add(intrinsic);
            break;
          case ConstPlayer.XAYDA:
            INTRINSIC_XD.add(intrinsic);
            break;
          default:
            INTRINSIC_TD.add(intrinsic);
            INTRINSIC_NM.add(intrinsic);
            INTRINSIC_XD.add(intrinsic);
        }
        INTRINSICS.add(intrinsic);
      }
      Logger.info("Load intrinsic thành công (" + INTRINSICS.size() + ")");
      // load task
      ps =
          con.prepareStatement(
              "SELECT id, task_main_template.name, detail, "
                  + "task_sub_template.name AS 'sub_name', max_count, notify, npc_id, map "
                  + "FROM task_main_template JOIN task_sub_template ON task_main_template.id = "
                  + "task_sub_template.task_main_id");
      rs = ps.executeQuery();
      int taskId = -1;
      TaskMain task = null;
      while (rs.next()) {
        int id = rs.getInt("id");
        if (id != taskId) {
          taskId = id;
          task = new TaskMain();
          task.id = taskId;
          task.name = rs.getString("name");
          task.detail = rs.getString("detail");
          TASKS.add(task);
        }
        SubTaskMain subTask = new SubTaskMain();
        subTask.name = rs.getString("sub_name");
        subTask.maxCount = rs.getShort("max_count");
        subTask.notify = rs.getString("notify");
        subTask.npcId = rs.getByte("npc_id");
        subTask.mapId = rs.getShort("map");
        task.subTasks.add(subTask);
      }
      Logger.info("Load task thành công (" + TASKS.size() + ")");
      // load side task
      ps =
          con.prepareStatement(
              "select * from side_task_template",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      while (rs.next()) {
        SideTaskTemplate sideTask = new SideTaskTemplate();
        sideTask.id = rs.getInt("id");
        sideTask.name = rs.getString("name");
        String[] mc1 = rs.getString("max_count_lv1").split("-");
        String[] mc2 = rs.getString("max_count_lv2").split("-");
        String[] mc3 = rs.getString("max_count_lv3").split("-");
        String[] mc4 = rs.getString("max_count_lv4").split("-");
        String[] mc5 = rs.getString("max_count_lv5").split("-");
        sideTask.count[0][0] = Integer.parseInt(mc1[0]);
        sideTask.count[0][1] = Integer.parseInt(mc1[1]);
        sideTask.count[1][0] = Integer.parseInt(mc2[0]);
        sideTask.count[1][1] = Integer.parseInt(mc2[1]);
        sideTask.count[2][0] = Integer.parseInt(mc3[0]);
        sideTask.count[2][1] = Integer.parseInt(mc3[1]);
        sideTask.count[3][0] = Integer.parseInt(mc4[0]);
        sideTask.count[3][1] = Integer.parseInt(mc4[1]);
        sideTask.count[4][0] = Integer.parseInt(mc5[0]);
        sideTask.count[4][1] = Integer.parseInt(mc5[1]);
        SIDE_TASKS_TEMPLATE.add(sideTask);
      }
      Logger.info("Load side task thành công (" + SIDE_TASKS_TEMPLATE.size() + ")");
      // load item template
      ps =
          con.prepareStatement(
              "select * from item_template",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      while (rs.next()) {
        ItemTemplate itemTemp = new ItemTemplate();
        itemTemp.id = rs.getShort("id");
        itemTemp.type = rs.getByte("type");
        itemTemp.gender = rs.getByte("gender");
        itemTemp.name = rs.getString("name");
        itemTemp.description = rs.getString("description");
        itemTemp.iconID = rs.getShort("icon_id");
        itemTemp.part = rs.getShort("part");
        itemTemp.isUpToUp = rs.getBoolean("is_up_to_up");
        itemTemp.strRequire = rs.getInt("power_require");
        itemTemp.gold = rs.getInt("gold");
        itemTemp.gem = rs.getInt("gem");
        itemTemp.head = rs.getInt("head");
        itemTemp.body = rs.getInt("body");
        itemTemp.leg = rs.getInt("leg");
        ITEM_TEMPLATES.add(itemTemp);
      }
      Logger.info("Load map item template thành công (" + ITEM_TEMPLATES.size() + ")");
      // load item option template
      ps =
          con.prepareStatement(
              "select id, name from item_option_template",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      while (rs.next()) {
        ItemOptionTemplate optionTemp = new ItemOptionTemplate();
        optionTemp.id = rs.getInt("id");
        optionTemp.name = rs.getString("name");
        ITEM_OPTION_TEMPLATES.add(optionTemp);
      }
      Logger.info(
          "Load map item option template thành công (" + ITEM_OPTION_TEMPLATES.size() + ")");
      // load shop
//      SHOPS = ShopDAO.getShops(con);
//      Logger.info("Load shop thành công (" + SHOPS.size() + ")");
      // load reward lucky round
      File folder = new File("data/monzy/data_lucky_round_reward");
      for (File fileEntry : folder.listFiles()) {
        if (!fileEntry.isDirectory()) {
          BufferedReader br = new BufferedReader(new FileReader(fileEntry));
          String line = null;
          while ((line = br.readLine()) != null) {
            line = line.replaceAll("[{}\\[\\]]", "");
            String[] arrSub = line.split("\\|");
            String[] data1 = arrSub[0].split(":");
            ItemLuckyRound item = new ItemLuckyRound();
            item.temp = ItemService.gI().getTemplate(Integer.parseInt(data1[0]));
            item.ratio = Integer.parseInt(data1[1]);
            item.typeRatio = Integer.parseInt(data1[2]);
            if (arrSub.length > 1) {
              String[] data2 = arrSub[1].split(";");
              for (String str : data2) {
                String[] data = str.split(":");
                ItemOptionLuckyRound io = new ItemOptionLuckyRound();
                Item.ItemOption itemOption = new Item.ItemOption(Integer.parseInt(data[0]), 0);
                io.itemOption = itemOption;
                String[] param = data[1].split("-");
                io.param1 = Integer.parseInt(param[0]);
                if (param.length == 2) {
                  io.param2 = Integer.parseInt(param[1]);
                }
                item.itemOptions.add(io);
              }
            }
            LUCKY_ROUND_REWARDS.add(item);
          }
        }
      }
      Logger.info("Load reward lucky round thành công (" + LUCKY_ROUND_REWARDS.size() + ")");
      // load notify
      folder = new File("data/monzy/notify");
      for (File fileEntry : folder.listFiles()) {
        if (!fileEntry.isDirectory()) {
          StringBuffer notify =
              new StringBuffer(
                      fileEntry.getName().substring(0, fileEntry.getName().lastIndexOf(".")))
                  .append("<>");
          BufferedReader br = new BufferedReader(new FileReader(fileEntry));
          String line = null;
          while ((line = br.readLine()) != null) {
            notify.append(line + "\n");
          }
          NOTIFY.add(notify.toString());
        }
      }
      Logger.info("Load notify thành công (" + NOTIFY.size() + ")");
      // load caption
      ps =
          con.prepareStatement(
              "select * from caption",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      while (rs.next()) {
        CAPTIONS.add(rs.getString("name"));
      }
      Logger.info("Load caption thành công (" + CAPTIONS.size() + ")");
      // load image by name
      ps =
          con.prepareStatement(
              "select name, n_frame from img_by_name",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      while (rs.next()) {
        IMAGES_BY_NAME.put(rs.getString("name"), rs.getByte("n_frame"));
      }
      Logger.info("Load images by name thành công (" + IMAGES_BY_NAME.size() + ")");
      // load mob template
      ps =
          con.prepareStatement(
              "select * from mob_template",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      while (rs.next()) {
        MobTemplate mobTemp = new MobTemplate();
        mobTemp.id = rs.getByte("id");
        mobTemp.type = rs.getByte("type");
        mobTemp.name = rs.getString("name");
        mobTemp.hp = rs.getInt("hp");
        mobTemp.rangeMove = rs.getByte("range_move");
        mobTemp.speed = rs.getByte("speed");
        mobTemp.dartType = rs.getByte("dart_type");
        mobTemp.percentDame = rs.getByte("percent_dame");
        mobTemp.percentTiemNang = rs.getByte("percent_tiem_nang");
        MOB_TEMPLATES.add(mobTemp);
      }
      Logger.info("Load mob template thành công (" + MOB_TEMPLATES.size() + ")");
      // load npc template
      ps =
          con.prepareStatement(
              "select * from npc_template",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      while (rs.next()) {
        NpcTemplate npcTemp = new NpcTemplate();
        npcTemp.id = rs.getByte("id");
        npcTemp.name = rs.getString("name");
        npcTemp.head = rs.getShort("head");
        npcTemp.body = rs.getShort("body");
        npcTemp.leg = rs.getShort("leg");
        npcTemp.avatar = rs.getInt("avatar");
        NPC_TEMPLATES.add(npcTemp);
      }
      Logger.info("Load npc template thành công (" + NPC_TEMPLATES.size() + ")");
      // load map template
      ps =
          con.prepareStatement(
              "select count(id) from map_template",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      if (rs.first()) {
        int countRow = rs.getShort(1);
        MAP_TEMPLATES = new MapTemplate[countRow];
        ps =
            con.prepareStatement(
                "select * from map_template",
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        rs = ps.executeQuery();
        short i = 0;
        while (rs.next()) {
          MapTemplate mapTemplate = new MapTemplate();
          int mapId = rs.getInt("id");
          String mapName = rs.getString("name");
          mapTemplate.id = mapId;
          mapTemplate.name = mapName;
          // load data
          //                    dataArray = (JSONArray) jv.parse(rs.getString("data"));
          //                    mapTemplate.type = Byte.parseByte(String.valueOf(dataArray.get(0)));
          //                    mapTemplate.planetId =
          // Byte.parseByte(String.valueOf(dataArray.get(1)));
          //                    mapTemplate.bgType =
          // Byte.parseByte(String.valueOf(dataArray.get(2)));
          //                    mapTemplate.tileId =
          // Byte.parseByte(String.valueOf(dataArray.get(3)));
          //                    mapTemplate.bgId = Byte.parseByte(String.valueOf(dataArray.get(4)));
          //                    dataArray.clear();
          mapTemplate.type = rs.getByte("type");
          mapTemplate.planetId = rs.getByte("planet_id");
          mapTemplate.bgType = rs.getByte("bg_type");
          mapTemplate.tileId = rs.getByte("tile_id");
          mapTemplate.bgId = rs.getByte("bg_id");
          mapTemplate.zones = rs.getByte("zones");
          mapTemplate.maxPlayerPerZone = rs.getByte("max_player");
          // load waypoints
          dataArray =
              (JSONArray)
                  JSONValue.parse(
                      rs.getString("waypoints")
                          .replaceAll("\\[\"\\[", "[[")
                          .replaceAll("\\]\"\\]", "]]")
                          .replaceAll("\",\"", ","));
          for (int j = 0; j < dataArray.size(); j++) {
            WayPoint wp = new WayPoint();
            JSONArray dtwp = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(j)));
            wp.name = String.valueOf(dtwp.get(0));
            wp.minX = Short.parseShort(String.valueOf(dtwp.get(1)));
            wp.minY = Short.parseShort(String.valueOf(dtwp.get(2)));
            wp.maxX = Short.parseShort(String.valueOf(dtwp.get(3)));
            wp.maxY = Short.parseShort(String.valueOf(dtwp.get(4)));
            wp.isEnter = Byte.parseByte(String.valueOf(dtwp.get(5))) == 1;
            wp.isOffline = Byte.parseByte(String.valueOf(dtwp.get(6))) == 1;
            wp.goMap = Short.parseShort(String.valueOf(dtwp.get(7)));
            wp.goX = Short.parseShort(String.valueOf(dtwp.get(8)));
            wp.goY = Short.parseShort(String.valueOf(dtwp.get(9)));
            mapTemplate.wayPoints.add(wp);
            dtwp.clear();
          }
          dataArray.clear();
          // load mobs
          dataArray = (JSONArray) JSONValue.parse(rs.getString("mobs").replaceAll("\\\"", ""));
          mapTemplate.mobTemp = new byte[dataArray.size()];
          mapTemplate.mobLevel = new byte[dataArray.size()];
          mapTemplate.mobHp = new int[dataArray.size()];
          mapTemplate.mobX = new short[dataArray.size()];
          mapTemplate.mobY = new short[dataArray.size()];
          for (int j = 0; j < dataArray.size(); j++) {
            JSONArray dtm = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(j)));
            mapTemplate.mobTemp[j] = Byte.parseByte(String.valueOf(dtm.get(0)));
            mapTemplate.mobLevel[j] = Byte.parseByte(String.valueOf(dtm.get(1)));
            mapTemplate.mobHp[j] = Integer.parseInt(String.valueOf(dtm.get(2)));
            mapTemplate.mobX[j] = Short.parseShort(String.valueOf(dtm.get(3)));
            mapTemplate.mobY[j] = Short.parseShort(String.valueOf(dtm.get(4)));
            dtm.clear();
          }
          dataArray.clear();
          // load npcs
          dataArray = (JSONArray) JSONValue.parse(rs.getString("npcs").replaceAll("\\\"", ""));
          mapTemplate.npcId = new byte[dataArray.size()];
          mapTemplate.npcX = new short[dataArray.size()];
          mapTemplate.npcY = new short[dataArray.size()];
          for (int j = 0; j < dataArray.size(); j++) {
            JSONArray dtn = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(j)));
            mapTemplate.npcId[j] = Byte.parseByte(String.valueOf(dtn.get(0)));
            mapTemplate.npcX[j] = Short.parseShort(String.valueOf(dtn.get(1)));
            mapTemplate.npcY[j] = Short.parseShort(String.valueOf(dtn.get(2)));
            dtn.clear();
          }
          dataArray.clear();
          MAP_TEMPLATES[i++] = mapTemplate;
        }
        Logger.info("Load map template thành công (" + MAP_TEMPLATES.length + ")");
        RUBY_REWARDS.add(Util.sendDo(861, 0, new ArrayList<>()));
      }
      ps =
          con.prepareStatement(
              "SELECT * FROM shop_ky_gui",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      while (rs.next()) {
        int i = rs.getInt("id");
        int idPl = rs.getInt("player_id");
        int tab = rs.getInt("tab");
        short itemId = rs.getShort("item_id");
        int gold = rs.getInt("gold");
        int gem = rs.getInt("gem");
        int quantity = rs.getInt("quantity");
        byte isUp = rs.getByte("isUpTop");
        boolean isBuy = rs.getByte("isBuy") == 1;
        int idPlBuy = rs.getInt("player_buy");
        List<Item.ItemOption> op = new ArrayList<>();
        JSONArray jsa2 = (JSONArray) JSONValue.parse(rs.getString("itemOption"));
        for (int j = 0; j < jsa2.size(); ++j) {
          JSONObject jso2 = (JSONObject) jsa2.get(j);
          int idOptions = Integer.parseInt(jso2.get("id").toString());
          int param = Integer.parseInt(jso2.get("param").toString());
          op.add(new Item.ItemOption(idOptions, param));
        }
        ShopKyGuiManager.gI()
            .listItem
            .add(
                new ItemKyGui(i, itemId, idPl, tab, gold, gem, quantity, isUp, op, isBuy, idPlBuy));
      }
      ps =
          con.prepareStatement(
              "select * from radar", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      while (rs.next()) {
        RadarCard rd = new RadarCard();
        rd.Id = rs.getShort("id");
        rd.IconId = rs.getShort("iconId");
        rd.Rank = rs.getByte("rank");
        rd.Max = rs.getByte("max");
        rd.Type = rs.getByte("type");
        rd.Template = rs.getShort("template");
        rd.Name = rs.getString("name");
        rd.Info = rs.getString("info");
        JSONArray arr = (JSONArray) JSONValue.parse(rs.getString("body"));
        for (int i = 0; i < arr.size(); i++) {
          JSONObject ob = (JSONObject) arr.get(i);
          if (ob != null) {
            rd.Head = Short.parseShort(ob.get("head").toString());
            rd.Body = Short.parseShort(ob.get("body").toString());
            rd.Leg = Short.parseShort(ob.get("leg").toString());
            rd.Bag = Short.parseShort(ob.get("bag").toString());
          }
        }
        rd.Options.clear();
        arr = (JSONArray) JSONValue.parse(rs.getString("options"));
        for (int i = 0; i < arr.size(); i++) {
          JSONObject ob = (JSONObject) arr.get(i);
          if (ob != null) {
            rd.Options.add(
                new OptionCard(
                    Integer.parseInt(ob.get("id").toString()),
                    Short.parseShort(ob.get("param").toString()),
                    Byte.parseByte(ob.get("activeCard").toString())));
          }
        }
        rd.Require = rs.getShort("require");
        rd.RequireLevel = rs.getShort("require_level");
        rd.AuraId = rs.getShort("aura_id");
        RadarService.gI().RADAR_TEMPLATE.add(rd);
      }
      Logger.info(
          "Load radar template thành công (" + RadarService.gI().RADAR_TEMPLATE.size() + ")");
      rs.close();
      ps.close();
    } catch (Exception e) {
      Logger.logException(Manager.class, e, "Lỗi load database");
      System.exit(0);
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (ps != null) {
          ps.close();
        }
      } catch (SQLException e) {
        Logger.logException(Manager.class, e);
      }
    }
    Logger.log(
        Logger.GREEN_BOLD_BRIGHT,
        "Tổng thời gian load database: " + (System.currentTimeMillis() - st) + "(ms)");
  }

  public void loadProperties() throws IOException {
    Properties properties = new Properties();
    properties.load(new FileInputStream("data/monzy/monzy.properties"));
    Object value;
    // Config sv
    if ((value = properties.get("server.monzy.port")) != null) {
      ServerManager.PORT = Integer.parseInt(String.valueOf(value));
    }
    if ((value = properties.get("server.monzy.name")) != null) {
      ServerManager.NAME = String.valueOf(value);
    }
    if ((value = properties.get("server.monzy.sv")) != null) {
      SERVER = Byte.parseByte(String.valueOf(value));
    }
    String linkServer = "";
    for (int i = 1; i <= 10; i++) {
      value = properties.get("server.monzy.sv" + i);
      if (value != null) {
        linkServer += value + ":0,";
      }
    }
    DataGame.LINK_IP_PORT = linkServer.substring(0, linkServer.length() - 1);
    if ((value = properties.get("server.monzy.waitlogin")) != null) {
      SECOND_WAIT_LOGIN = Byte.parseByte(String.valueOf(value));
    }
    if ((value = properties.get("server.monzy.maxperip")) != null) {
      MAX_PER_IP = Integer.parseInt(String.valueOf(value));
    }
    if ((value = properties.get("server.monzy.maxplayer")) != null) {
      MAX_PLAYER = Integer.parseInt(String.valueOf(value));
    }
    if ((value = properties.get("server.monzy.rateExp")) != null) {
      RATE_EXP = Byte.parseByte(String.valueOf(value));
    }
    if ((value = properties.get("server.monzy.ratePay")) != null) {
      RATE_PAY = Byte.parseByte(String.valueOf(value));
    }
    if ((value = properties.get("server.monzy.local")) != null) {
      LOCAL = String.valueOf(value).equalsIgnoreCase("true");
    }
  }
}

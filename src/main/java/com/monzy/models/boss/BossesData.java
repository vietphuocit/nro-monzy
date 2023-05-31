package com.monzy.models.boss;

import com.monzy.models.skill.Skill;
import com.monzy.consts.*;
import com.monzy.utils.Util;

public class BossesData {

    /**
     * Prefix text chat |-1| Boss chat |-2| Player in map chat |-3| Parent chat
     * |0|,|1|,|n| Index boss in list chat
     */
    private static final int[][] FULL_DRAGON = new int[][]{{Skill.DRAGON, 1}, {Skill.DRAGON, 2}, {Skill.DRAGON, 3}, {Skill.DRAGON, 4}, {Skill.DRAGON, 5}, {Skill.DRAGON, 6}, {Skill.DRAGON, 7}};
    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};
    private static final int[][] FULL_GALICK = new int[][]{{Skill.GALICK, 1}, {Skill.GALICK, 2}, {Skill.GALICK, 3}, {Skill.GALICK, 4}, {Skill.GALICK, 5}, {Skill.GALICK, 6}, {Skill.GALICK, 7}};
    private static final int[][] FULL_KAMEJOKO = new int[][]{{Skill.KAMEJOKO, 1}, {Skill.KAMEJOKO, 2}, {Skill.KAMEJOKO, 3}, {Skill.KAMEJOKO, 4}, {Skill.KAMEJOKO, 5}, {Skill.KAMEJOKO, 6}, {Skill.KAMEJOKO, 7}};
    private static final int[][] FULL_MASENKO = new int[][]{{Skill.MASENKO, 1}, {Skill.MASENKO, 2}, {Skill.MASENKO, 3}, {Skill.MASENKO, 4}, {Skill.MASENKO, 5}, {Skill.MASENKO, 6}, {Skill.MASENKO, 7}};
    private static final int[][] FULL_ANTOMIC = new int[][]{{Skill.ANTOMIC, 1}, {Skill.ANTOMIC, 2}, {Skill.ANTOMIC, 3}, {Skill.ANTOMIC, 4}, {Skill.ANTOMIC, 5}, {Skill.ANTOMIC, 6}, {Skill.ANTOMIC, 7}};
    private static final int[][] FULL_LIENHOAN = new int[][]{{Skill.LIEN_HOAN, 1}, {Skill.LIEN_HOAN, 2}, {Skill.LIEN_HOAN, 3}, {Skill.LIEN_HOAN, 4}, {Skill.LIEN_HOAN, 5}, {Skill.LIEN_HOAN, 6}, {Skill.LIEN_HOAN, 7}};
    private static final int[][] FULL_TDHS = new int[][]{{Skill.THAI_DUONG_HA_SAN, 1}, {Skill.THAI_DUONG_HA_SAN, 2}, {Skill.THAI_DUONG_HA_SAN, 3}, {Skill.THAI_DUONG_HA_SAN, 4}, {Skill.THAI_DUONG_HA_SAN, 5}, {Skill.THAI_DUONG_HA_SAN, 6}, {Skill.THAI_DUONG_HA_SAN, 7}};
    private static final int REST_1_S = 1;
    private static final int REST_2_S = 2;
    private static final int REST_5_S = 5;
    private static final int REST_10_S = 10;
    private static final int REST_20_S = 20;
    private static final int REST_30_S = 30;
    private static final int REST_1_M = 60;
    private static final int REST_2_M = 120;
    private static final int REST_5_M = 300;
    private static final int REST_10_M = 600;
    private static final int REST_15_M = 900;
    private static final int REST_30_M = 1800;
    private static final int REST_24_H = 86400000;
    /**
     * Boss nappa
     */
    public static final BossData KUKU = new BossData(
            "Kuku", //name
            ConstPlayer.XAYDA, //gender
            new short[]{159, 160, 161, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            20000, //dame
            new int[]{5000000}, //hp
            new int[]{68, 69, 70, 71, 72, 64, 65, 66, 67}, //map join
            new int[][]{
                    {Skill.GALICK, 7, 1000},
                    {Skill.ANTOMIC, 7, 10000}},
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_15_M //second rest
    );
    public static final BossData MAP_DAU_DINH = new BossData(
            "Mập Đầu Đinh", //name
            ConstPlayer.XAYDA, //gender
            new short[]{165, 166, 167, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            30000, //dame
            new int[]{7000000}, //hp
            new int[]{68, 69, 70, 71, 72, 64, 65, 66, 67}, //map join
            new int[][]{
                    {Skill.GALICK, 7, 1000},
                    {Skill.ANTOMIC, 7, 10000}},
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_15_M //second rest
    );
    public static final BossData RAMBO = new BossData(
            "Rambo", //name
            ConstPlayer.XAYDA, //gender
            new short[]{162, 163, 164, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            40000, //dame
            new int[]{10000000}, //hp
            new int[]{68, 69, 70, 71, 72, 64, 65, 66, 67}, //map join
            new int[][]{
                    {Skill.GALICK, 7, 1000},
                    {Skill.ANTOMIC, 7, 10000}},
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_15_M //second rest
    );
    /**
     * Boss tiểu đội sát thủ
     */
    public static final BossData SO_4 = new BossData(
            "Số 4 Guldo", //name
            ConstPlayer.XAYDA, //gender
            new short[]{168, 169, 170, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            100000, //dame
            new int[]{10000000}, //hp
            new int[]{79, 82, 83}, //map join
            new int[][]{
                    {Skill.GALICK, 7, 1000},
                    {Skill.ANTOMIC, 7, 1000}
            },
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_15_M
    );
    public static final BossData SO_3 = new BossData(
            "Số 3 Recome", //name
            ConstPlayer.XAYDA, //gender
            new short[]{174, 175, 176, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            110000, //dame
            new int[]{12000000}, //hp
            new int[]{79, 82, 83}, //map join
            new int[][]{
                    {Skill.GALICK, 7, 1000},
                    {Skill.ANTOMIC, 7, 1000}
            },//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER //type appear
    );
    public static final BossData SO_2 = new BossData(
            "Số 2 Jeice", //name
            ConstPlayer.XAYDA, //gender
            new short[]{171, 172, 173, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            120000, //dame
            new int[]{14000000}, //hp
            new int[]{79, 82, 83}, //map join
            new int[][]{
                    {Skill.GALICK, 7, 1000},
                    {Skill.ANTOMIC, 7, 1000}
            },//skill//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER //type appear
    );
    public static final BossData SO_1 = new BossData(
            "Số 1 Burter", //name
            ConstPlayer.XAYDA, //gender
            new short[]{177, 178, 179, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            130000, //dame
            new int[]{16000000}, //hp
            new int[]{79, 82, 83}, //map join
            new int[][]{
                    {Skill.GALICK, 7, 1000},
                    {Skill.ANTOMIC, 7, 1000}
            },//skill//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER //type appear
    );
    public static final BossData TIEU_DOI_TRUONG = new BossData(
            "Tiểu đội trưởng Ginyu", //name
            ConstPlayer.XAYDA, //gender
            new short[]{180, 181, 182, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            140000, //dame
            new int[]{20000000}, //hp
            new int[]{79, 82, 83}, //map join
            new int[][]{
                    {Skill.SOCOLA, 7, 1000},
                    {Skill.GALICK, 7, 1000},
                    {Skill.ANTOMIC, 7, 1000}
            },//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER //type appear
    );
    /**
     * Boss tiểu đội sát thủ NAMEC
     */
    public static final BossData SO_4_1 = new BossData(
            "Số 4 Guldo", //name
            ConstPlayer.XAYDA, //gender
            new short[]{168, 169, 170, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            100000, //dame
            new int[]{10000000}, //hp
            new int[]{31, 32, 33, 34, 9, 10, 11, 12, 13}, //map join
            new int[][]{
                    {Skill.GALICK, 7, 1000},
                    {Skill.ANTOMIC, 7, 1000}
            },
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_15_M
    );
    public static final BossData SO_3_1 = new BossData(
            "Số 3 Recome", //name
            ConstPlayer.XAYDA, //gender
            new short[]{174, 175, 176, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            110000, //dame
            new int[]{12000000}, //hp
            new int[]{31, 32, 33, 34, 9, 10, 11, 12, 13}, //map join
            new int[][]{
                    {Skill.GALICK, 7, 1000},
                    {Skill.ANTOMIC, 7, 1000}
            },//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER //type appear
    );
    public static final BossData SO_2_1 = new BossData(
            "Số 2 Jeice", //name
            ConstPlayer.XAYDA, //gender
            new short[]{171, 172, 173, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            120000, //dame
            new int[]{14000000}, //hp
            new int[]{31, 32, 33, 34, 9, 10, 11, 12, 13}, //map join
            new int[][]{
                    {Skill.GALICK, 7, 1000},
                    {Skill.ANTOMIC, 7, 1000}
            },//skill//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER //type appear
    );
    public static final BossData SO_1_1 = new BossData(
            "Số 1 Burter", //name
            ConstPlayer.XAYDA, //gender
            new short[]{177, 178, 179, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            130000, //dame
            new int[]{16000000}, //hp
            new int[]{31, 32, 33, 34, 9, 10, 11, 12, 13}, //map join
            new int[][]{
                    {Skill.GALICK, 7, 1000},
                    {Skill.ANTOMIC, 7, 1000}
            }, //skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER //type appear
    );
    public static final BossData TIEU_DOI_TRUONG_1 = new BossData(
            "Tiểu đội trưởng Ginyu", //name
            ConstPlayer.XAYDA, //gender
            new short[]{180, 181, 182, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            140000, //dame
            new int[]{20000000}, //hp
            new int[]{31, 32, 33, 34, 9, 10, 11, 12, 13}, //map join
            new int[][]{
                    {Skill.SOCOLA, 7, 1000},
                    {Skill.GALICK, 7, 1000},
                    {Skill.ANTOMIC, 7, 1000}
            },//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER //type appear
    );
    /**
     * Boss Fide đại ca
     */
    public static final BossData FIDE_DAI_CA_1 = new BossData(
            "Fide đại ca 1", //name
            ConstPlayer.XAYDA, //gender
            new short[]{183, 184, 185, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            150000, //dame
            new int[]{10000000}, //hp
            new int[]{80}, //map join
            new int[][]{
                    {Skill.GALICK, 7, 1000},
                    {Skill.ANTOMIC, 7, 1000}
            }, //skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_15_M //second rest
    );
    public static final BossData FIDE_DAI_CA_2 = new BossData(
            "Fide đại ca 2", //name
            ConstPlayer.XAYDA, //gender
            new short[]{186, 187, 188, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            170000, //dame
            new int[]{15000000}, //hp
            new int[]{80}, //map join
            new int[][]{
                    {Skill.GALICK, 7, 1000},
                    {Skill.ANTOMIC, 7, 1000}
            }, //skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.ANOTHER_LEVEL //type appear
    );
    public static final BossData FIDE_DAI_CA_3 = new BossData(
            "Fide đại ca 3", //name
            ConstPlayer.XAYDA, //gender
            new short[]{189, 190, 191, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            190000, //dame
            new int[]{25000000}, //hp
            new int[]{80}, //map join
            new int[][]{
                    {Skill.GALICK, 7, 1000},
                    {Skill.ANTOMIC, 7, 1000}
            }, //skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.ANOTHER_LEVEL //type appear
    );
    /**
     * Boss Android 13, 14, 15
     */
    public static final BossData ANDROID_13 = new BossData(
            "Android 13", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{252, 253, 254, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            200000, //dame
            new int[]{20000000}, //hp
            new int[]{84, 104}, //map join
            new int[][]{
                    {Skill.KAMEJOKO, 7, 10000},
                    {Skill.LIEN_HOAN, 7, 1000}},//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.CALL_BY_ANOTHER
    );
    public static final BossData ANDROID_14 = new BossData(
            "Android 14", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{246, 247, 248, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            200000, //dame
            new int[]{25000000}, //hp
            new int[]{84, 104}, //map join
            new int[][]{
                    {Skill.KAMEJOKO, 7, 10000},
                    {Skill.LIEN_HOAN, 7, 1000}},//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_15_M,
            new int[]{BossID.ANDROID_13, BossID.ANDROID_15}
    );
    public static final BossData ANDROID_15 = new BossData(
            "Android 15", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{261, 262, 263, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            200000, //dame
            new int[]{30000000}, //hp
            new int[]{84, 104}, //map join
            new int[][]{
                    {Skill.KAMEJOKO, 7, 10000},
                    {Skill.LIEN_HOAN, 7, 1000}},//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );
    /**
     * Boss Android PIC, POC, KING KONG
     */
    public static final BossData PIC = new BossData(
            "Số 17 Pic", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{237, 238, 239, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            200000, //dame
            new int[]{30000000}, //hp
            new int[]{97, 98, 99}, //map join
            new int[][]{
                    {Skill.KAMEJOKO, 7, 10000},
                    {Skill.LIEN_HOAN, 7, 1000}},//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );
    public static final BossData POC = new BossData(
            "Số 18 Poc", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{240, 241, 242, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            200000, //dame
            new int[]{35000000}, //hp
            new int[]{97, 98, 99}, //map join
            new int[][]{
                    {Skill.KAMEJOKO, 7, 10000},
                    {Skill.LIEN_HOAN, 7, 1000}},//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );
    public static final BossData KING_KONG = new BossData(
            "Số 16 King Kong", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{243, 244, 245, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            200000, //dame
            new int[]{40000000}, //hp
            new int[]{97, 98, 99}, //map join
            new int[][]{
                    {Skill.KAMEJOKO, 7, 10000},
                    {Skill.LIEN_HOAN, 7, 1000}},//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_15_M,
            new int[]{BossID.PIC, BossID.POC}
    );
    /**
     * Boss Android 19, 20
     */
    public static final BossData ANDROID_19 = new BossData(
            "Android 19", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{249, 250, 251, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            200000, //dame
            new int[]{40000000}, //hp
            new int[]{96, 94, 93}, //map join
            new int[][]{
                    {Skill.KAMEJOKO, 7, 10000},
                    {Skill.LIEN_HOAN, 7, 1000}},//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );
    public static final BossData DR_KORE = new BossData(
            "Dr.Kôrê", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{255, 256, 257, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            200000, //dame
            new int[]{50000000}, //hp
            new int[]{96, 94, 93}, //map join
            new int[][]{
                    {Skill.KAMEJOKO, 7, 10000},
                    {Skill.LIEN_HOAN, 7, 1000}},//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_15_M, //second rest
            new int[]{BossID.ANDROID_19}
    );
    /**
     * Boss Xên bọ hung
     */
    public static final BossData XEN_BO_HUNG_1 = new BossData(
            "Xên bọ hung 1",
            ConstPlayer.XAYDA,
            new short[]{228, 229, 230, -1, -1, -1},
            300000,
            new int[]{100000000},
            new int[]{100},
            new int[][]{
                    {Skill.KAMEJOKO, 7, 1000},
                    {Skill.LIEN_HOAN, 7, 1000}
            },
            // skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_15_M
    );
    public static final BossData XEN_BO_HUNG_2 = new BossData(
            "Xên bọ hung 2",
            ConstPlayer.XAYDA,
            new short[]{231, 232, 233, -1, -1, -1},
            300000,
            new int[]{100000000},
            new int[]{100},
            new int[][]{
                    {Skill.KAMEJOKO, 7, 1000},
                    {Skill.LIEN_HOAN, 7, 1000}
            },
            // skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.ANOTHER_LEVEL
    );
    public static final BossData XEN_BO_HUNG_3 = new BossData(
            "Xên hoàn thiện",
            ConstPlayer.XAYDA,
            new short[]{234, 235, 236, -1, -1, -1},
            350000,
            new int[]{150000000},
            new int[]{100},
            new int[][]{
                    {Skill.KAMEJOKO, 7, 1000},
                    {Skill.LIEN_HOAN, 7, 1000}
            },
            //skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.ANOTHER_LEVEL
    );
    /**
     * Boss Xên Con, Siêu bọ hung
     */
    public static final BossData XEN_CON = new BossData(
            "Xên con", //name
            ConstPlayer.XAYDA, //gender
            new short[]{264, 265, 266, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            300000, //dame
            new int[]{200000000}, //hp
            new int[]{103}, //map join
            new int[][]{
                    {Skill.KAMEJOKO, 7, 1000},
                    {Skill.LIEN_HOAN, 7, 1000}
            },
            //skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_15_M, //second rest
            new int[]{BossID.XEN_CON_1, BossID.XEN_CON_2, BossID.XEN_CON_3, BossID.XEN_CON_4}
    );
    public static final BossData XEN_CON_1 = new BossData(
            "Xên con 1", //name
            ConstPlayer.XAYDA, //gender
            new short[]{264, 265, 266, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            300000, //dame
            new int[]{200000000}, //hp
            new int[]{96, 94, 93}, //map join
            new int[][]{
                    {Skill.KAMEJOKO, 7, 10000},
                    {Skill.LIEN_HOAN, 7, 1000}},//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );
    public static final BossData XEN_CON_2 = new BossData(
            "Xên con 2", //name
            ConstPlayer.XAYDA, //gender
            new short[]{264, 265, 266, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            300000, //dame
            new int[]{200000000}, //hp
            new int[]{96, 94, 93}, //map join
            new int[][]{
                    {Skill.KAMEJOKO, 7, 10000},
                    {Skill.LIEN_HOAN, 7, 1000}},//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );
    public static final BossData XEN_CON_3 = new BossData(
            "Xên con 3", //name
            ConstPlayer.XAYDA, //gender
            new short[]{264, 265, 266, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            300000, //dame
            new int[]{200000000}, //hp
            new int[]{96, 94, 93}, //map join
            new int[][]{
                    {Skill.KAMEJOKO, 7, 10000},
                    {Skill.LIEN_HOAN, 7, 1000}},//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );
    public static final BossData XEN_CON_4 = new BossData(
            "Xên con 4", //name
            ConstPlayer.XAYDA, //gender
            new short[]{264, 265, 266, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            300000, //dame
            new int[]{200000000}, //hp
            new int[]{96, 94, 93}, //map join
            new int[][]{
                    {Skill.KAMEJOKO, 7, 10000},
                    {Skill.LIEN_HOAN, 7, 1000}},//skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );
    /**
     * Boss Siêu bọ hung
     */
    public static final BossData SIEU_BO_HUNG_1 = new BossData(
            "Siêu bọ hung",
            ConstPlayer.XAYDA,
            new short[]{234, 235, 236, -1, -1, -1},
            300000,
            new int[]{300000000},
            new int[]{103},
            new int[][]{
                    {Skill.KAMEJOKO, 7, 1000},
                    {Skill.LIEN_HOAN, 7, 1000}
            },
            //skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_30_M
    );
    public static final BossData SIEU_BO_HUNG_2 = new BossData(
            "Siêu bọ hung 2",
            ConstPlayer.XAYDA,
            new short[]{234, 235, 236, -1, -1, -1},
            300000,
            new int[]{400000000},
            new int[]{103},
            new int[][]{
                    {Skill.KAMEJOKO, 7, 1000},
                    {Skill.LIEN_HOAN, 7, 1000}
            },
            //skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.ANOTHER_LEVEL
    );
    public static final BossData SIEU_BO_HUNG_3 = new BossData(
            "Siêu bọ hung 3",
            ConstPlayer.XAYDA,
            new short[]{234, 235, 236, -1, -1, -1},
            300000,
            new int[]{500000000},
            new int[]{103},
            new int[][]{
                    {Skill.KAMEJOKO, 7, 1000},
                    {Skill.LIEN_HOAN, 7, 1000}
            },
            //skill
            new String[]{"|-1|3", "|-1|2", "|-1|1", "|-1|COMBAT"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.ANOTHER_LEVEL
    );
    //**************************************************************************Boss doanh trại
    public static final BossData TRUNG_UY_TRANG = new BossData(
            "Trung úy trắng", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{141, 142, 143, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1200, //dame
            new int[]{150000}, //hp
            new int[]{62}, //map join
            (int[][]) Util.addArray(FULL_DEMON), //skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Nhóc con"}, //text chat 2
            new String[]{}, //text chat 3
            REST_10_S
    );
    //**************************************************************************
    public static final BossData COOLER_GOLD = new BossData(
            "Cooler Vàng", //name
            ConstPlayer.XAYDA, //gender
            new short[]{709, 710, 711, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            2000000, //dame
            new int[]{1000000000}, //hp
            new int[]{155}, //map join
            new int[][]{
                    {Skill.THOI_MIEN, 7, 100000},
                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 3000},
                    {Skill.KAMEJOKO, 7, 10000},
                    {Skill.LIEN_HOAN, 4, 1000},
                    {Skill.TAI_TAO_NANG_LUONG, 7, 50000},
                    {Skill.MASENKO, 7, 10000}}, //skill
            new String[]{"|-1|Hello cục cưng",
                    "|-1|Mày có biết tao là ai không?",
                    "|-2|Tao không cần biết mày là ai, mày nghĩ mày dọa được tao à?",
                    "|-1|Thôi không nói nhiều nữa,giờ tao cho mày biết tao là ai."
            }, //text chat 1
            new String[]{"|-1|Tao hơn hẳn mày, mày nên cầu cho may mắn ở phía mày đi",
                    "|-1|Ghê chưa ghê chưa!",
                    "|-1|Tao có rất nhiều vật phẩm quý giá,nhưng với mày thì có cái..nịt",
                    "|-1|Đánh tao à,lo mà luyện tập thêm đi",
                    "|-1|Nói cho mày biết,tao là anh trai của Fide",
                    "|-1|trạng thái Goldend Meta Cooler sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Đêm qua em đẹp lắm!"}, //text chat 3
            REST_15_M //second rest
    );
    public static final BossData CUMBER = new BossData(
            "Sayan Tà Ác Cumber", //name
            ConstPlayer.XAYDA, //gender
            new short[]{1207, 1208, 1209, -1, 0, -1}, //outfit {head, body, leg, bag, aura, eff}
            2000000, //dame
            new int[]{1000000000}, //hp
            new int[]{155}, //map join
            new int[][]{
                    {Skill.LIEN_HOAN, 7, 1000},
                    {Skill.ANTOMIC, 7, 1000},
                    {Skill.TAI_TAO_NANG_LUONG, 7, 1000},
                    {Skill.BIEN_KHI, 5, 10000}}, //skill
            new String[]{"|-1|Gaaaaaa !!!!!!!!",
                    "|-2|Tên Sayan kia là ai vậy",
                    "|-1|Sức mạnh tà ác !"
            }, //text chat 1
            new String[]{"|-1|Ta muốn tìm một đối thủ xứng tầm",
                    "|-1|Đi chết đi!",
                    "|-1|Các ngươi không phải đối thủ của ta đâu",
                    "|-1|trạng thái Tà Ác sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Tên đó mạnh thật!"}, //text chat 3
            REST_15_M //second rest
    );
    //**************************************************************************
    //**************************************************************************
    //**************************************************************************cha con fide
    public static final BossData VUA_COLD = new BossData(
            "Thống Chế King COLD",
            ConstPlayer.XAYDA,
            new short[]{712, 713, 714, -1, -1, -1},
            3000000,
            new int[]{2000000000},
            new int[]{131, 132, 133},
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_LIENHOAN), //skill
            new String[]{"|-2|Hắn ta là ai vậy?"}, //text chat 1
            new String[]{"|-1|Thì ra đây là trái đất",
                    "|-1|Hành tinh này bán đi chắc cũng kha khá đó!",
                    "|-2|Ngươi làm ta khó chịu rồi đấy",
                    "|-1|Ngươi sẽ không bao giờ thắng được đâu!!",
                    "|-2|Tên này mạnh quá",}, //text chat 2
            new String[]{"|-1|Xin hãy tha cho ta !",
                    "|-1|Ta sẽ cho ngươi nửa số hành tinh ta đang giữ!",
                    "|-1|Đừng màaa!"}, //text chat 3
            REST_15_M, //second rest
            new int[]{BossID.FIDE_ROBOT}
    );
    public static final BossData FIDE_ROBOT = new BossData(
            "Fide người máy", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{715, 716, 717, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            12200, //dame
            new int[]{150000000}, //hp
            new int[]{131, 132, 133}, //map join
            (int[][]) Util.addArray(FULL_GALICK, FULL_ANTOMIC), //skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Haaaahaa",
                    "|-1|Chúng ta sẽ hủy diệt hành tinh này",
                    "|-1|Tên Sôn gô ku mãi vẫn chưa tới",
                    "|-1|Ngươi sẽ không bao giờ thắng được đâu!!",
                    "|-2|Để xem ai mới là người chiến thắng!!",}, //text chat 2
            new String[]{"|-1|Ta thua rồi sao? Khôngggggggg!"}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );
    //**************************************************************************
    //**************************************************************************
    public static final BossData XUKA = new BossData(
            "Cô Bé Shizuka", //name
            ConstPlayer.XAYDA, //gender
            new short[]{802, 803, 804, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1111111, //dame
            new int[]{15000000}, //hp
            new int[]{19}, //map join
            new int[][]{
                    {Skill.MASENKO, 7, 1000},
                    {Skill.DE_TRUNG, 7, 1000},
            },//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                    "|-1|Chán", "|-1|Đại ca Khoa có nhầm không nhỉ",
                    "|-1|Các ngươi không nhúc nhích được sao?",
                    "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER //type appear
    );
    public static final BossData XEKO = new BossData(
            "Mõm Nhọn Suneo", //name
            ConstPlayer.XAYDA, //gender
            new short[]{850, 851, 852, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1111111, //dame
            new int[]{10000000}, //hp
            new int[]{19}, //map join
            new int[][]{
                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 10000},
                    {Skill.KAMEJOKO, 7, 1000},
            },//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                    "|-1|Chán", "|-1|Đại ca có nhầm không nhỉ",
                    "|-1|Một mình tao chấp hết tụi bây",
                    "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER //type appear
    );
    public static final BossData CHAIEN = new BossData(
            "Khỉ Đột Chaien", //name
            ConstPlayer.XAYDA, //gender
            new short[]{847, 848, 849, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1111111, //dame
            new int[]{20000000}, //hp
            new int[]{19}, //map join
            new int[][]{
                    {Skill.GALICK, 7, 10000},
                    {Skill.KAMEJOKO, 7, 10000},
                    {Skill.BIEN_KHI, 1, 600000},
            },//skill//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                    "|-1|Chán", "|-1|Đại ca có nhầm không nhỉ",
                    "|-1|Một mình tao chấp hết tụi bây",
                    "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER //type appear
    );
    public static final BossData NOBITA = new BossData(
            "Chú Bé Đần Nobita", //name
            ConstPlayer.XAYDA, //gender
            new short[]{844, 845, 846, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1111110, //dame
            new int[]{10000000}, //hp
            new int[]{19}, //map join
            new int[][]{
                    {Skill.MASENKO, 7, 1000},
                    {Skill.ANTOMIC, 7, 10000},
            },//skill//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                    "|-1|Chán", "|-1|Đại ca Doraemon có nhầm không nhỉ",
                    "|-1|Một mình tao chấp hết tụi bây",
                    "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER //type appear
    );
    public static final BossData DORAEMON = new BossData(
            "Người Máy Doraemon",
            ConstPlayer.XAYDA,
            new short[]{790, 791, 792, -1, -1, -1},
            1000000,
            new int[]{10000000},
            new int[]{19},
//            new int[]{14},
            new int[][]{
                    {Skill.DRAGON, 7, 1000},
                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},
            }, //skill//skill
            new String[]{"|-2|Á đù, Doraemon !!!!!"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_15_M, //second rest
            new int[]{BossID.CHAIEN, BossID.XEKO, BossID.NOBITA, BossID.XUKA} //boss join map together
    );
    //**************************************************************************
    //**************************************************************************
    public static final BossData BLACK_GOKU_BASE = new BossData(
            "Black Goku", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{550, 880, 881, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            8000000, //dame
            new int[]{1000000000}, //hp
            new int[]{0, 1, 2, 3, 4, 5, 6, 27, 28, 29}, //map join
            (int[][]) Util.addArray(FULL_GALICK, FULL_KAMEJOKO, FULL_LIENHOAN), //skill
            new String[]{"|-1|Ta là Sôn Gô Ku",
                    "|-1|Cơ thể này,sức mạnh này",
                    "|-1|Ta khá thích việc loại bỏ các ngươi",
                    "|-1|Mau chấp nhận số phận đi lũ sâu bọ"
            }, //text chat 1
            new String[]{"|-1|Các ngươi chỉ có vậy thôi sao?",
                    "|-1|Đúng là loài người thấp kém",
                    "|-2|Ngươi nói như thể ngươi không phải con người vậy?",
                    "|-2|Chiếc nhẫn kia lẽ nào ngươi là một Kaioshin?!",
                    "|-1|Các ngươi không nên biết quá nhiều",
                    "|-2|Xem đòn đánh của ta đây !",
                    "|-1|Được thôi, nếu muốn chết đến vậy, ta rất vui lòng!!"
            }, //text chat 2
            new String[]{"|-1|Biến hình! Super Sayan Rose"}, //text chat 3
            REST_5_M //second rest
    );
    //**************************************************************************
    public static final BossData BLACK_GOKU = new BossData(
            "Black Goku", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{550, 880, 881, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            100000, //dame
            new int[]{1000000000}, //hp
            new int[]{102, 92, 93, 94, 96, 97, 98, 99, 100}, //map join
            (int[][]) Util.addArray(FULL_GALICK, FULL_KAMEJOKO), //skill
            new String[]{"|-1|Ta là Sôn Gô Ku",
                    "|-1|Cơ thể này,sức mạnh này",
                    "|-1|Ta khá thích việc loại bỏ các ngươi",
                    "|-1|Mau chấp nhận số phận đi lũ sâu bọ"
            }, //text chat 1
            new String[]{"|-1|Các ngươi chỉ có vậy thôi sao?",
                    "|-1|Đúng là loài người thấp kém",
                    "|-2|Ngươi nói như thể ngươi không phải con người vậy?",
                    "|-2|Chiếc nhẫn kia lẽ nào ngươi là một Kaioshin?!",
                    "|-1|Các ngươi không nên biết quá nhiều",
                    "|-2|Xem đòn đánh của ta đây !",
                    "|-1|Được thôi, nếu muốn chết đến vậy, ta rất vui lòng!!"
            }, //text chat 2
            new String[]{"|-1|Biến hình! Super Sayan Rose"}, //text chat 3
            REST_5_M //second rest
    );
    public static final BossData SUPER_BLACK_GOKU = new BossData(
            "Super Black Goku Rose", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{553, 880, 881, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            200000, //dame
            new int[]{2000000000}, //hp
            new int[]{102, 92, 93, 94, 96, 97, 98, 99, 100}, //map join
            new int[][]{
                    {Skill.THAI_DUONG_HA_SAN, 7, 30000},
                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 10000},
                    {Skill.KAMEJOKO, 7, 10000},
                    {Skill.LIEN_HOAN, 7, 10000}},
            //skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Ta chính là người mang thân thể của Songoku",
                    "|-1|Sức mạnh của ta là không có giới hạn",
                    "|-1|Ta sẽ thống trị vũ trụ",
                    "|-1|Để ta nói cho nghe,người Sayan sau khi hồi phục sức mạnh sẽ tăng lên rất nhiều",
                    "|-2|Tại sao ngươi lại lấy thân thể của songoku chứ?"
            }, //text chat 2
            new String[]{"|-1|Chúng ta sẽ gặp lại nhau sớm thôi",
                    "|-2|Ngươi nói gì chứ?"}, //text chat 3
            TypeAppear.ANOTHER_LEVEL //type appear
    );
    //-------------------------------------------------------------------
    public static final BossData SUPER_BLACK_GOKU_2 = new BossData(
            "Super Black Goku Rose", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{553, 880, 881, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            10000000, //dame
            new int[]{2000000000}, //hp
            new int[]{102, 92, 93, 94, 96, 97, 98, 99, 100}, //map join
//            new int[]{14}, //map join
            new int[][]{
                    {Skill.KHIEN_NANG_LUONG, 7, 10000},
                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 10000},
                    {Skill.KAMEJOKO, 7, 1000},
                    {Skill.ANTOMIC, 7, 10000}},
            //skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Ta chính là người mang thân thể của Songoku",
                    "|-1|Sức mạnh của ta là không có giới hạn",
                    "|-1|Ta sẽ thống trị vũ trụ",
                    "|-1|Để ta nói cho nghe,người Sayan sau khi hồi phục sức mạnh sẽ tăng lên rất nhiều",
                    "|-2|Tại sao ngươi lại lấy thân thể của songoku chứ?"
            }, //text chat 2
            new String[]{"|-1|Hẹn gặp lại",
                    "|-2|Không tiễn"}, //text chat 3
            REST_15_M, //second rest
            new int[]{BossID.ZAMASZIN}
    );
    public static final BossData ZAMAS = new BossData(
            "Kaioshin Zamas", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{433, 904, 905, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            4550500, //dame
            new int[]{450000000}, //hp
            new int[]{102, 92, 93, 94, 96, 97, 98, 99, 100}, //map join
//            new int[]{14}, //map join
            new int[][]{
                    {Skill.GALICK, 7, 1000},
                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 10000},
                    {Skill.TAI_TAO_NANG_LUONG, 4, 1000},
                    {Skill.KAMEJOKO, 7, 30000}}, //skill
            new String[]{"|-1|Kia là một con người sao?",
                    "|-3|Ủa tên kia là ai vậy?",
                    "|-2|Lẽ nào đúng như chúng ta đã nghĩ",
                    "|-1|Lũ con người không đủ tư cách để nói chuyện với ta",
                    "|-2|Zamas! Tại sao chứ !",
                    "|-1|Ta sẽ cho người biết sức mạnh của một vị thần là như thế nào !"
            }, //text chat 1
            new String[]{"|-1|Ta là kaioshin của vũ trụ thứ 10 ",
                    "|-1|Tên của ta là Zamas, ta sẽ thay đổi thế giới này",
                    "|-1|Lũ con người các ngươi là những thứ ta cần loại bỏ đầu tiên",
                    "|-2|Tại sao các ngươi lại nhắm tới con người bọn ta chứ?",
                    "|-1|Bởi vì ta muốn thực hiện kế hoạch đưa con người về số 0 !",
                    "|-1|Lần này ta không nương tay đâu!",
                    "|-2|Ngươi thực sự rất mạnh. Nhưng chưa đủ thực lực đâu!!",
                    "|-1|Cái gì!? Đó là điều ngu ngốc nhất ta từng nghe! Mau biến đi",
                    "|-1|Hắn thực sự rất mạnh, đúng là cuộc chiến hay",
                    "|-3|Không lí nào ta lại run sợ bọn con người sao"
            }, //text chat 2
            new String[]{"|-1|Chỉ còn một cách duy nhất mà thôi",
                    "|-1|Bông tai Porata!"}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );
    //-------------------------------------------------------------------
    public static final BossData THANZM2 = new BossData(
            "Thần Zamas Tối Thượng", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{903, 904, 905, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            13000000, //dame
            new int[]{1700000000}, //hp
            new int[]{102, 92, 93, 94, 96, 97, 98, 99, 100}, //map join
            new int[][]{
                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 5000},
                    {Skill.KAMEJOKO, 5, 3000},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 10000},
                    {Skill.QUA_CAU_KENH_KHI, 7, 120000}},
            //skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Ta chính là thế giới",
                    "|-1|Ta chính là công lí",
                    "|-1|Hãy chiêm ngưỡng vẻ đẹp của ta !Hỡi con người",
                    "|-1|Sức mạnh to lớn nằm trong cơ thể bất tử",
                    "|-1|Ta sẽ đem công lí tới toàn bộ vũ trụ này", "|-2|Ngươi cứ lải nhải hoài 2 chữ công lí vậy?", "|-1|Lũ các ngươi làm ta thấy đau rồi ấy haha"
            }, //text chat 2
            new String[]{}, //text chat 3
            REST_15_M
    );
    //**************************************************************************
    public static final BossData MABU = new BossData(
            "Mabư",
            ConstPlayer.XAYDA,
            new short[]{297, 298, 299, -1, -1, -1},
            500000,
            new int[]{1500000000},
            new int[]{52},
            new int[][]{
                    {Skill.KAMEJOKO, 3, 5000},
                    {Skill.LIEN_HOAN, 7, 100},
                    {Skill.SOCOLA, 7, 1000}},
            new String[]{"|-2|Ma nhân Bư đã xuất hiện rồi"}, //text chat 1
            new String[]{"|-1|Thấy ảo chưa nè!"}, //text chat 2
            new String[]{"|-1|Nhớ mặt tao đấy",
                    "|-1|Tobe continue.."}, //text chat 3
            REST_10_M
    );
    //*******************
    //**************************************************************************
    public static final BossData BROLY_1 = new BossData(
            "Broly Base", //name
            ConstPlayer.XAYDA, //gender
            new short[]{1080, 1084, 1085, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1200000, //dame
            new int[]{1500000000}, //hp
            new int[]{5}, //map join
            new int[][]{
                    {Skill.KHIEN_NANG_LUONG, 7, 10000},
                    {Skill.ANTOMIC, 7, 500}}, //skill
            new String[]{
                    "|-1|Tuy không biết các ngươi là ai, nhưng ta rất ấn tượng đấy!",
                    "|-2|Tới đây đi!"
            }, //text chat 1
            new String[]{"|-1|Các ngươi tới số rồi mới gặp phải ta",
                    "|-1|Gaaaaaa",
                    "|-2|Không..thể..nào!!",
                    "|-2|Không ngờ..Hắn mạnh cỡ này sao..!!"
            }, //text chat 2
            new String[]{"|-1|Gaaaaaaaa!!!"}, //text chat 3
            REST_10_M //second rest
    );
    public static final BossData BROLY_2 = new BossData(
            "Super Broly", //name
            ConstPlayer.XAYDA, //gender
            new short[]{1083, 1084, 1085, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            2400000, //dame
            new int[]{500000000}, //hp
            new int[]{5}, //map join
            new int[][]{
                    {Skill.THAI_DUONG_HA_SAN, 5, 10000},
                    {Skill.ANTOMIC, 3, 1000}}, //skill
            new String[]{
                    "|-1|Gaaaaaa",
                    "|-2|Tới đây đi!"
            }, //text chat 1
            new String[]{"|-1|Các ngươi tới số rồi mới gặp phải ta",
                    "|-1|Gaaaaaa",
                    "|-2|Không..thể..nào!!",
                    "|-2|Không ngờ..Hắn mạnh cỡ này sao..!!"
            }, //text chat 2
            new String[]{"|-1|Gaaaaaaaa!!!"}, //text chat 3
            TypeAppear.ANOTHER_LEVEL //type appear
    );
    public static final BossData BROLY_3 = new BossData(
            "Super Sayan Huyền Thoại", //name
            ConstPlayer.XAYDA, //gender
            new short[]{294, 295, 1085, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            7020000, //dame
            new int[]{1000000000}, //hp
            new int[]{5}, //map join
            new int[][]{
                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 1000},
                    {Skill.KAMEJOKO, 7, 1000}}, //skill
            new String[]{
                    "|-1|Tuy không biết các ngươi là ai, nhưng ta rất ấn tượng đấy!",
                    "|-2|Ta cũng cảm thấy phấn khích lắm!"
            }, //text chat 1
            new String[]{"|-1|Các ngươi tới số rồi mới gặp phải ta",
                    "|-1|Gaaaaaa",
                    "|-2|Không..thể..nào!!",
                    "|-2|Tên này điên thật rồi!!",
                    "|-1|Được thôi, nếu muốn chết đến vậy, ta rất vui lòng!!"
            }, //text chat 2
            new String[]{"|-1|Khôngggggggg!!"}, //text chat 3
            TypeAppear.ANOTHER_LEVEL //type appear
    );
    public static final BossData BROLY_4 = new BossData(
            "Super Sayan GOD", //name
            ConstPlayer.XAYDA, //gender
            new short[]{390, 295, 296, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            19220000, //dame
            new int[]{1900000000}, //hp
            new int[]{5}, //map join
            new int[][]{
                    {Skill.SOCOLA, 7, 6000},
                    {Skill.DE_TRUNG, 7, 6000},
                    {Skill.TROI, 7, 6000},
                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 6000},
                    {Skill.LIEN_HOAN, 7, 100},
                    {Skill.KAMEJOKO, 7, 1000}}, //skill
            new String[]{
                    "|-1|Tuy không biết các ngươi là ai, nhưng ta rất ấn tượng đấy!",
                    "|-2|Ta cũng cảm thấy phấn khích lắm!"
            }, //text chat 1
            new String[]{"|-1|Các ngươi tới số rồi mới gặp phải ta",
                    "|-1|Gaaaaaa",
                    "|-2|Không..thể..nào!!",
                    "|-2|Tên này điên thật rồi!!",
                    "|-1|Được thôi, nếu muốn chết đến vậy, ta rất vui lòng!!"
            }, //text chat 2
            new String[]{"|-1|Khôngggggggg!!"}, //text chat 3
            TypeAppear.ANOTHER_LEVEL //type appear
    );
    //************************************************************************** Boss hủy diệt
    public static final BossData THIEN_SU_WHIS = new BossData(
            "Thiên sứ Whis", //name
            ConstPlayer.NAMEC, //gender
            new short[]{838, 839, 840, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            100000000, //dame
            new int[]{150}, //hp
            new int[]{146, 147, 148}, //map join
            new int[][]{
                    {Skill.KHIEN_NANG_LUONG, 7, 100000},
                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 5000},},//skill
            new String[]{"|-2|Kia là ai thế",
                    "|-1|Xin chào! Chúng tôi tới đây để tìm người!",
                    "|-1|Ngài Berrus! Có vẻ hắn ta không có ở đây",
                    "|-1|Ta có thể hỏi các ngươi",
                    "|-2|Chuyện gì vậy?",
                    "|-1|Các ngươi có biết tên Super Sayan God ở đâu không?",
                    "|-2|Super Sayan God? đó là gì vậy?",
                    "|-1|Có vẻ giấc mơ của ngài Berrus là bịp rồi! Hô Hô",
                    "|0|Nếu các ngươi không biết hắn ở đâu",
                    "|0|Chắc ta phải phá hủy hành tinh này vậy"
            }, //text chat 1
            new String[]{"|-1|Ta có thể ngồi ăn một chút được rồi!",
                    "|-1|Các ngươi vẫn yếu vẫn như mọi khi",
                    "|0|Thật là bực mình!",
                    "|-2|Đây là sức mạnh của một thiên sứ sao?",
                    "|-1|Hô Hô",
                    "|-1|Các ngươi không đánh bại được ta đâu!",
                    "|-2|Không thể nào",
                    "|-2|Tại sao lại vậy chứ !",}, //text chat 2
            new String[]{"|-1|Ta đi về đây!Cảm ơn vì món ăn"}, //text chat 3
            REST_10_M, //second rest
            new int[]{BossID.THAN_HUY_DIET}
    );
    public static final BossData THAN_HUY_DIET = new BossData(
            "Thần Hủy Diệt Berrus", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{508, 509, 510, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            2000000, //dame
            new int[]{2000000000}, //hp
            new int[]{146, 147, 148}, //map join
            new int[][]{
                    {Skill.MASENKO, 7, 200},
                    {Skill.LIEN_HOAN, 7, 300},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Các ngươi thật là yếu ớt",
                    "|-1|Ta sẽ phá hủy hành tinh này",
                    "|-1|Chán quá!",
                    "|-1|Ta vẫn chưa dùng hết sức đâu!",
                    "|-2|Hắn ta không cần phòng thủ luôn!",}, //text chat 2
            new String[]{"|-1|Ta buồn ngủ quá!"}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );
    public static final BossData THIEN_SU_VADOS = new BossData(
            "Thiên sứ Vados", //name
            ConstPlayer.NAMEC, //gender
            new short[]{530, 531, 532, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            100000000, //dame
            new int[]{150}, //hp
            new int[]{146, 147, 148}, //map join
            new int[][]{
                    {Skill.KHIEN_NANG_LUONG, 7, 100000},
                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 50000},},//skill
            new String[]{"|-2|Kia là ai thế",
                    "|-1|Ta là Vados",
                    "|-1|Ta tới đây để kiếm một thứ",
                    "|-1|Đó chính là Ngọc Rồng Siêu Cấp",
                    "|0|Bọn này ta cho 1 búng là bay !"
            }, //text chat 1
            new String[]{"|-1|Ồ",
                    "|-1|Ta làm vậy có hơi quá không?",
                    "|0|Thật là bực mình!",
                    "|-2|Sao ông ta lại mạnh tới vậy ?",
                    "|-1|Hô Hô",
                    "|-1|Các ngươi muốn đánh bại một Thiên Sứ sao?",
                    "|-2|Khốn khiếp!",
                    "|-2|Tại sao lại vậy chứ !",}, //text chat 2
            new String[]{"|-1|Hẹn gặp lại,ta rất hài lòng về cuộc chiến"}, //text chat 3
            REST_15_M, //second rest
            new int[]{BossID.THAN_HUY_DIET_CHAMPA}
    );
    public static final BossData THAN_HUY_DIET_CHAMPA = new BossData(
            "Thần Hủy Diệt Champa", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{511, 512, 513, -1, -1, 77}, //outfit {head, body, leg, bag, aura, eff}
            200000, //dame
            new int[]{2000000000}, //hp
            new int[]{146, 147, 148}, //map join
            new int[][]{
                    {Skill.MASENKO, 3, 400},
                    {Skill.LIEN_HOAN, 7, 300},},//skill//skill
            new String[]{"|-1|Các ngươi có biết",
                    "|-1|Ngọc Rồng Siêu Cấp đang ở đâu hay không?",
                    "|-2|Ai mà biết được",}, //text chat 1
            new String[]{"|-1|Một lũ yếu ớt",
                    "|-1|Ta sẽ phá hủy hành tinh này",
                    "|-1|Chán quá!",
                    "|-1|Không có ai đủ mạnh để đấu với ta sao?",
                    "|-2|Hắn ta không cần phòng thủ luôn!",}, //text chat 2
            new String[]{"|-1|Chết tiệt!"}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );
    //**************************************************************************
    //************************************************************************** Boss goku
    public static final BossData SONGOKU_TA_AC = new BossData(
            "Siêu Goku Tà Ác", //name
            ConstPlayer.XAYDA, //gender
            new short[]{543, 57, 999, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            2100000, //dame
            new int[]{2000000000}, //hp
            new int[]{155}, //map join
            new int[][]{
                    {Skill.THAI_DUONG_HA_SAN, 7, 100000},
                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 1000},
                    {Skill.KAMEJOKO, 7, 10000},
                    {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{"|-1|Ta bị sao thế này !",
                    "|-2|Chú Goku",
                    "|-2|Đó không còn là chú Goku nữa rồi",
                    "|-1|GAAAAAAAAAAAAAA!."
            }, //text chat 1
            new String[]{"|-2|Tỉnh lại đi chú Goku",
                    "|-2|Đừng để bị hắn chi phối!",
                    "|-1|Định chạy trốn hả?",
                    "|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                    "|-2|Không được rồi!",
                    "|-2|Phải cố hết sức thôi"
            }, //text chat 2
            new String[]{"|-2|Mau nghỉ ngơi nào chú Goku"}, //text chat 3
            REST_15_M //second rest
    );
    //************************************************************************** Boss nrd
    public static final BossData Rong_1Sao = new BossData(
            "Rồng Syn 1 Sao", //name
            ConstPlayer.XAYDA, //gender
            new short[]{204, 205, 206, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1000000, //dame
            new int[]{200000000}, //hp
            new int[]{85}, //map join
            new int[][]{
                    {Skill.LIEN_HOAN, 7, 10000},
                    {Skill.KAMEJOKO, 5, 10000}}, //skill
            new String[]{"|-1|Gaaaaaa !!!!!!!!",
                    "|-2|Tên kia là ai vậy",
                    "|-1|Sức mạnh tà ác !"
            }, //text chat 1
            new String[]{"|-1|Ta muốn tìm một đối thủ xứng tầm",
                    "|-1|Đi chết đi!",
                    "|-1|Các ngươi không phải đối thủ của ta đâu",
                    "|-1|trạng thái Tà Ác sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Tên đó mạnh thật!"}, //text chat 3
            REST_24_H //second rest
    );
    public static final BossData Rong_2Sao = new BossData(
            "Rồng Haze 2 Sao", //name
            ConstPlayer.XAYDA, //gender
            new short[]{219, 220, 221, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1000000, //dame
            new int[]{20000000}, //hp
            new int[]{86}, //map join
            new int[][]{
                    {Skill.LIEN_HOAN, 7, 1000},
                    {Skill.KAMEJOKO, 5, 1000}}, //skill
            new String[]{"|-1|Gaaaaaa !!!!!!!!",
                    "|-2|Tên kia là ai vậy",
                    "|-1|Sức mạnh tà ác !"
            }, //text chat 1
            new String[]{"|-1|Ta muốn tìm một đối thủ xứng tầm",
                    "|-1|Đi chết đi!",
                    "|-1|Các ngươi không phải đối thủ của ta đâu",
                    "|-1|trạng thái Tà Ác sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Tên đó mạnh thật!"}, //text chat 3
            REST_24_H //second rest
    );
    public static final BossData Rong_3Sao = new BossData(
            "Rồng Eis 3 Sao", //name
            ConstPlayer.XAYDA, //gender
            new short[]{207, 208, 209, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1200000, //dame
            new int[]{40000000}, //hp
            new int[]{87}, //map join
            new int[][]{
                    {Skill.THOI_MIEN, 4, 125000},
                    {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 10000}}, //skill
            new String[]{"|-1|Gaaaaaa !!!!!!!!",
                    "|-2|Tên kia là ai vậy",
                    "|-1|Sức mạnh tà ác !"
            }, //text chat 1
            new String[]{"|-1|Ta muốn tìm một đối thủ xứng tầm",
                    "|-1|Đi chết đi!",
                    "|-1|Các ngươi không phải đối thủ của ta đâu",
                    "|-1|trạng thái Tà Ác sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Tên đó mạnh thật!"}, //text chat 3
            REST_24_H //second rest
    );
    public static final BossData Rong_4Sao = new BossData(
            "Rồng Nuova 4 Sao", //name
            ConstPlayer.XAYDA, //gender
            new short[]{210, 211, 212, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1000000, //dame
            new int[]{180000000}, //hp
            new int[]{88}, //map join
            new int[][]{
                    {Skill.LIEN_HOAN, 7, 10000},
                    {Skill.TROI, 7, 100000},
                    {Skill.KAMEJOKO, 5, 10000}}, //skill
            new String[]{"|-1|Gaaaaaa !!!!!!!!",
                    "|-2|Tên kia là ai vậy",
                    "|-1|Sức mạnh tà ác !"
            }, //text chat 1
            new String[]{"|-1|Ta muốn tìm một đối thủ xứng tầm",
                    "|-1|Đi chết đi!",
                    "|-1|Các ngươi không phải đối thủ của ta đâu",
                    "|-1|trạng thái Tà Ác sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Tên đó mạnh thật!"}, //text chat 3
            REST_24_H //second rest
    );
    public static final BossData Rong_5Sao = new BossData(
            "Rồng Rage 5 Sao", //name
            ConstPlayer.XAYDA, //gender
            new short[]{213, 214, 215, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1000000, //dame
            new int[]{200000000}, //hp
            new int[]{89}, //map join
            new int[][]{
                    {Skill.KAMEJOKO, 5, 10000}}, //skill
            new String[]{"|-1|Gaaaaaa !!!!!!!!",
                    "|-2|Tên kia là ai vậy",
                    "|-1|Sức mạnh tà ác !"
            }, //text chat 1
            new String[]{"|-1|Ta muốn tìm một đối thủ xứng tầm",
                    "|-1|Đi chết đi!",
                    "|-1|Các ngươi không phải đối thủ của ta đâu",
                    "|-1|trạng thái Tà Ác sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Tên đó mạnh thật!"}, //text chat 3
            REST_24_H //second rest
    );
    public static final BossData Rong_6Sao = new BossData(
            "Rồng Oceanus 6 Sao", //name
            ConstPlayer.XAYDA, //gender
            new short[]{222, 223, 224, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            3000000, //dame
            new int[]{200000000}, //hp
            new int[]{90}, //map join
            new int[][]{
                    {Skill.LIEN_HOAN, 7, 4000},
                    {Skill.KAMEJOKO, 5, 10000}}, //skill
            new String[]{"|-1|Gaaaaaa !!!!!!!!",
                    "|-2|Tên kia là ai vậy",
                    "|-1|Sức mạnh tà ác !"
            }, //text chat 1
            new String[]{"|-1|Ta muốn tìm một đối thủ xứng tầm",
                    "|-1|Đi chết đi!",
                    "|-1|Các ngươi không phải đối thủ của ta đâu",
                    "|-1|trạng thái Tà Ác sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Tên đó mạnh thật!"}, //text chat 3
            REST_24_H //second rest
    );
    public static final BossData Rong_7Sao = new BossData(
            "Rồng Naturon 7 Sao", //name
            ConstPlayer.XAYDA, //gender
            new short[]{216, 217, 218, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1000000, //dame
            new int[]{200000000}, //hp
            new int[]{91}, //map join
            new int[][]{
                    {Skill.KAMEJOKO, 5, 1000}}, //skill
            new String[]{"|-1|Gaaaaaa !!!!!!!!",
                    "|-2|Tên kia là ai vậy",
                    "|-1|Sức mạnh tà ác !"
            }, //text chat 1
            new String[]{"|-1|Ta muốn tìm một đối thủ xứng tầm",
                    "|-1|Đi chết đi!",
                    "|-1|Các ngươi không phải đối thủ của ta đâu",
                    "|-1|trạng thái Tà Ác sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Tên đó mạnh thật!"}, //text chat 3
            REST_24_H //second rest
    );
    //**************************************************************************Team Mabu 12h
    public static final BossData MABU_12H = new BossData(
            "Mabư",
            ConstPlayer.XAYDA,
            new short[]{297, 298, 299, -1, -1, -1},
            1000000,
            new int[]{2000000000},
            new int[]{120},
            new int[][]{
                    {Skill.KHIEN_NANG_LUONG, 7, 50000},
                    {Skill.THAI_DUONG_HA_SAN, 7, 70000},
                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 1000},
                    {Skill.DE_TRUNG, 7, 1000},
                    {Skill.KAMEJOKO, 7, 100}},
            new String[]{"|-2|Ma nhân Bư đã xuất hiện rồi"}, //text chat 1
            new String[]{"|-1|Thấy ảo chưa nè!"}, //text chat 2
            new String[]{"|-1|Nhớ mặt tao đấy",
                    "|-1|Tobe continue.."}, //text chat 3
            REST_15_M
    );
    public static final BossData DRABURA = new BossData(
            "Ma Vương Dabura",
            ConstPlayer.XAYDA,
            new short[]{418, 419, 420, -1, -1, -1},
            100000,
            new int[]{50000000},
            new int[]{114},
            new int[][]{
                    {Skill.LIEN_HOAN, 7, 1000},
                    {Skill.DEMON, 7, 10000}},
            new String[]{"|-2|Ma nhân Bư đã xuất hiện rồi"}, //text chat 1
            new String[]{"|-1|Thấy ảo chưa nè!"}, //text chat 2
            new String[]{"|-1|Nhớ mặt tao đấy",
                    "|-1|Tobe continue.."}, //text chat 3
            REST_1_M
    );
    public static final BossData DRABURA_2 = new BossData(
            "Ma Vương Dabura",
            ConstPlayer.XAYDA,
            new short[]{418, 419, 420, -1, -1, -1},
            200000,
            new int[]{50000000},
            new int[]{115},
            new int[][]{
                    {Skill.LIEN_HOAN, 7, 1000},
                    {Skill.DEMON, 7, 10000}},
            new String[]{"|-2|Ma nhân Bư đã xuất hiện rồi"}, //text chat 1
            new String[]{"|-1|Thấy ảo chưa nè!"}, //text chat 2
            new String[]{"|-1|Nhớ mặt tao đấy",
                    "|-1|Tobe continue.."}, //text chat 3
            REST_1_M
    );
    public static final BossData BUI_BUI = new BossData(
            "Bui Bui",
            ConstPlayer.XAYDA,
            new short[]{451, 452, 453, -1, -1, -1},
            200000,
            new int[]{300000000},
            new int[]{117},
            new int[][]{
                    {Skill.LIEN_HOAN, 7, 1000},
                    {Skill.DEMON, 7, 10000}},
            new String[]{"|-2|Ma nhân Bư đã xuất hiện rồi"}, //text chat 1
            new String[]{"|-1|Thấy ảo chưa nè!"}, //text chat 2
            new String[]{"|-1|Nhớ mặt tao đấy",
                    "|-1|Tobe continue.."}, //text chat 3
            REST_1_M
    );
    public static final BossData BUI_BUI_2 = new BossData(
            "Bui Bui",
            ConstPlayer.XAYDA,
            new short[]{451, 452, 453, -1, -1, -1},
            200000,
            new int[]{500000000},
            new int[]{118},
            new int[][]{
                    {Skill.LIEN_HOAN, 7, 1000},
                    {Skill.DEMON, 7, 10000}},
            new String[]{"|-2|Ma nhân Bư đã xuất hiện rồi"}, //text chat 1
            new String[]{"|-1|Thấy ảo chưa nè!"}, //text chat 2
            new String[]{"|-1|Nhớ mặt tao đấy",
                    "|-1|Tobe continue.."}, //text chat 3
            REST_1_M
    );
    public static final BossData YACON = new BossData(
            "Yacôn",
            ConstPlayer.XAYDA,
            new short[]{415, 416, 417, -1, -1, -1},
            200000,
            new int[]{500000000},
            new int[]{119},
            new int[][]{
                    {Skill.DEMON, 7, 100}},
            new String[]{"|-2|Ma nhân Bư đã xuất hiện rồi"}, //text chat 1
            new String[]{"|-1|Thấy ảo chưa nè!"}, //text chat 2
            new String[]{"|-1|Nhớ mặt tao đấy",
                    "|-1|Tobe continue.."}, //text chat 3
            REST_1_M
    );
    public static final BossData COOLER_1 = new BossData(
            "Cooler", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{508, 509, 510, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1, //dame
            new int[]{1000000}, //hp
            new int[]{14}, //map join
            new int[][]{
                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 1000},
                    {Skill.DE_TRUNG, 7, 1000},
                    {Skill.LIEN_HOAN, 1, 400},
                    {Skill.MASENKO, 2, 300},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Các ngươi thật là yếu ớt",
                    "|-1|Chán quá!",
                    "|-1|Không có ai đủ mạnh để đấu với ta sao?",
                    "|-1|Ta sẽ cho ngươi cái nịt!",
            }, //text chat 2
            new String[]{"|-1|Biến hình !!!!!!!!!!!!"}, //text chat 3
            REST_30_M
    );
    public static final BossData COOLER_2 = new BossData(
            "Cooler 2", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{508, 509, 510, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1, //dame
            new int[]{1000000}, //hp
            new int[]{14}, //map join
            new int[][]{
                    {Skill.DE_TRUNG, 7, 1000},
                    {Skill.KHIEN_NANG_LUONG, 7, 100000},
                    {Skill.KAMEJOKO, 1, 400},
                    {Skill.LIEN_HOAN, 2, 300},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Các ngươi thật là yếu ớt",
                    "|-1|Ta sẽ phá hủy hành tinh này",
                    "|-1|Chán quá!",
                    "|-1|Không có ai đủ mạnh để đấu với ta sao?",
                    "|-1|Ta nghèo lắm!Đừng săn ta nữa",}, //text chat 2
            new String[]{"|-1|Đen lắm em trai !"}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );
    public static final BossData SUPER_ANDROID_17 = new BossData(
            "Super SooMe", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{636, 637, 638, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            5500000, //dame
            new int[]{2000000000}, //hp
            new int[]{98, 99, 100, 96, 92, 93}, //map join
            new int[][]{
                    {Skill.LIEN_HOAN, 1, 300}, {Skill.LIEN_HOAN, 2, 400}, {Skill.LIEN_HOAN, 3, 500}, {Skill.LIEN_HOAN, 4, 600}, {Skill.LIEN_HOAN, 5, 700}, {Skill.LIEN_HOAN, 6, 800}, {Skill.LIEN_HOAN, 7, 900},
                    {Skill.KAMEJOKO, 7, 600}, {Skill.KAMEJOKO, 6, 700}, {Skill.KAMEJOKO, 5, 800}, {Skill.KAMEJOKO, 4, 900}, {Skill.KAMEJOKO, 3, 1000}, {Skill.KAMEJOKO, 2, 1100}, {Skill.KAMEJOKO, 1, 1002},
                    {Skill.ANTOMIC, 1, 130}, {Skill.ANTOMIC, 2, 140}, {Skill.ANTOMIC, 3, 150}, {Skill.ANTOMIC, 4, 160}, {Skill.ANTOMIC, 5, 170}, {Skill.ANTOMIC, 6, 190}, {Skill.ANTOMIC, 7, 200},
                    {Skill.MASENKO, 1, 210}, {Skill.MASENKO, 5, 220}, {Skill.MASENKO, 6, 230},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 7, 15000},
                    {Skill.KHIEN_NANG_LUONG, 7, 50000},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_15_M
    );
    public static final BossData SUPER_XEN = new BossData(
            "Super Xên SooMe", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{2000, 2001, 2002, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            70000000, //dame
            new int[]{1500000000}, //hp
            new int[]{103}, //map join
            new int[][]{
                    {Skill.LIEN_HOAN, 1, 300}, {Skill.LIEN_HOAN, 2, 400}, {Skill.LIEN_HOAN, 3, 500}, {Skill.LIEN_HOAN, 4, 600}, {Skill.LIEN_HOAN, 5, 700}, {Skill.LIEN_HOAN, 6, 800}, {Skill.LIEN_HOAN, 7, 900},
                    {Skill.KAMEJOKO, 7, 600}, {Skill.KAMEJOKO, 6, 700}, {Skill.KAMEJOKO, 5, 800}, {Skill.KAMEJOKO, 4, 900}, {Skill.KAMEJOKO, 3, 1000}, {Skill.KAMEJOKO, 2, 1100}, {Skill.KAMEJOKO, 1, 1002},
                    {Skill.ANTOMIC, 1, 130}, {Skill.ANTOMIC, 2, 140}, {Skill.ANTOMIC, 3, 150}, {Skill.ANTOMIC, 4, 160}, {Skill.ANTOMIC, 5, 170}, {Skill.ANTOMIC, 6, 190}, {Skill.ANTOMIC, 7, 200},
                    {Skill.MASENKO, 1, 210}, {Skill.MASENKO, 5, 220}, {Skill.MASENKO, 6, 230},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 7, 15000},
                    {Skill.KHIEN_NANG_LUONG, 7, 50000},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_30_M
    );
    public static final BossData KAMIRIN = new BossData(
            "Super Super SooMe", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{356, 357, 358, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            5205500, //dame
            new int[]{100000000}, //hp
            new int[]{38}, //map join
            (int[][]) Util.addArray(FULL_GALICK, FULL_KAMEJOKO, FULL_LIENHOAN, FULL_ANTOMIC, FULL_DEMON, FULL_MASENKO, FULL_DRAGON), //skill
            new String[]{"|-1|Sôn..gôku",
                    "|-2|Lại là Soome à.. rốt cuộc ông ta đã tạo ra bao nhiêu Anhrin nhân tạo thế nhỉ?",
                    "|-1|Bọn ta là rôbốt sát thủ, sinh ra từ máy tính ngài Soome,..",
                    "|-1|..cho một mục tiêu duy nhất là giết Sôngôku!",
                    "|-2|Máy tính? Để giết Gôku sao?",
                    "|-1|Mong muốn trả thù Gôku của ngài Soome đã được lưu hết vào máy tính..",
                    "|-1|.., Bọn ta sinh ra từ lòng căm thù ngày một tăng bên trong chiếc máy tính có chứa mong muốn trả thù",
                    "|-1|Mục tiêu của bọn ta chỉ là Gôku, nhưng mà.. nếu ngươi mà cản đường thì là chuyện khác!",}, //text chat 1
            new String[]{"|-1|Sao thế hả? Ta mới chỉ khởi động thôi mà!",
                    "|-2|Ngươi đánh giá thấp bọn ta quá đấy!",
                    "|-2|Đừng có tưởng bở, lũ sâu bọ member!",
                    "|-1|Nếu có ý định gây trở ngại cho cuộc chiến giữa ta và Sôngôku, thì ta cũng sẽ giết ngươi ngay lập tức",
                    "|-2|Ngươi tưởng ta để cho ngươi giết được ta ngay à?",
                    "|-2|Đúng là mạnh mồm thật đấy!",
                    "|-2|Đỡ này",}, //text chat 2
            new String[]{"|-1|Sô..Sông...gôku....."}, //text chat 3
            TypeAppear.CALL_BY_ANOTHER
    );
    public static final BossData KAMILOC = new BossData(
            "Super Super Super SooMe", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{397, 398, 399, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1200000, //dame
            new int[]{1500000000}, //hp
            new int[]{38}, //map join
            new int[][]{
                    {Skill.TAI_TAO_NANG_LUONG, 7, 10000},
                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 10000},
                    {Skill.DE_TRUNG, 7, 1000},
                    {Skill.THAI_DUONG_HA_SAN, 7, 120000},
                    {Skill.LIEN_HOAN, 7, 100},},//skill
            new String[]{"|-2|Các ngươi là ai?",
                    "|-2|Ta không thể cảm nhận được khí của các ngươi, các ngươi không phải là con người đúng chứ?",
                    "|-2|Ta hiểu rồi, các ngươi là rôbốt sát thủ do tiến sĩ Kôrê tạo ra chứ gì?"
            }, //text chat 1
            new String[]{}, //text chat 2
            new String[]{"|0| Soome  tiêu tùng cả rồi à?"}, //text chat 3
            REST_30_M,
            new int[]{BossID.KAMIRIN, BossID.KAMI_SOOME}
    );
    public static final BossData KAMI_SOOME = new BossData(
            "Super Super Super Super SooMe", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{409, 410, 411, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            5200000, //dame
            new int[]{2000000000}, //hp
            new int[]{38}, //map join
            new int[][]{
                    {Skill.LIEN_HOAN, 1, 300}, {Skill.LIEN_HOAN, 2, 400}, {Skill.LIEN_HOAN, 3, 500}, {Skill.LIEN_HOAN, 4, 600}, {Skill.LIEN_HOAN, 5, 700}, {Skill.LIEN_HOAN, 6, 800}, {Skill.LIEN_HOAN, 7, 900},
                    {Skill.KAMEJOKO, 7, 600}, {Skill.KAMEJOKO, 6, 700}, {Skill.KAMEJOKO, 5, 800}, {Skill.KAMEJOKO, 4, 900}, {Skill.KAMEJOKO, 3, 1000}, {Skill.KAMEJOKO, 2, 1100}, {Skill.KAMEJOKO, 1, 1002},
                    {Skill.ANTOMIC, 1, 130}, {Skill.ANTOMIC, 2, 140}, {Skill.ANTOMIC, 3, 150}, {Skill.ANTOMIC, 4, 160}, {Skill.ANTOMIC, 5, 170}, {Skill.ANTOMIC, 6, 190}, {Skill.ANTOMIC, 7, 200},
                    {Skill.MASENKO, 1, 210}, {Skill.MASENKO, 5, 220}, {Skill.MASENKO, 6, 230},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 7, 15000},
                    {Skill.KHIEN_NANG_LUONG, 7, 50000},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{"|-2|Thì ra vẫn chỉ là một đống sắt vụn!"}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );
    public static final BossData CUMBERBLACK = new BossData(
            "CumBer SooMe Black", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{907, 908, 909, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            5500000, //dame
            new int[]{2000000000}, //hp
            new int[]{98, 99, 100, 96, 92, 93}, //map join
            new int[][]{
                    {Skill.LIEN_HOAN, 1, 300}, {Skill.LIEN_HOAN, 2, 400}, {Skill.LIEN_HOAN, 3, 500}, {Skill.LIEN_HOAN, 4, 600}, {Skill.LIEN_HOAN, 5, 700}, {Skill.LIEN_HOAN, 6, 800}, {Skill.LIEN_HOAN, 7, 900},
                    {Skill.KAMEJOKO, 7, 600}, {Skill.KAMEJOKO, 6, 700}, {Skill.KAMEJOKO, 5, 800}, {Skill.KAMEJOKO, 4, 900}, {Skill.KAMEJOKO, 3, 1000}, {Skill.KAMEJOKO, 2, 1100}, {Skill.KAMEJOKO, 1, 1002},
                    {Skill.ANTOMIC, 1, 130}, {Skill.ANTOMIC, 2, 140}, {Skill.ANTOMIC, 3, 150}, {Skill.ANTOMIC, 4, 160}, {Skill.ANTOMIC, 5, 170}, {Skill.ANTOMIC, 6, 190}, {Skill.ANTOMIC, 7, 200},
                    {Skill.MASENKO, 1, 210}, {Skill.MASENKO, 5, 220}, {Skill.MASENKO, 6, 230},
                    {Skill.DE_TRUNG, 7, 1000},
                    {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 7, 15000},
                    {Skill.KHIEN_NANG_LUONG, 7, 50000},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_30_M,
            new int[]{BossID.CUMBERYELLOW}
    );
    public static final BossData CUMBERYELLOW = new BossData(
            "Mai iu idol IU", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{888, 889, 890, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            70000000, //dame
            new int[]{2000000000}, //hp
            new int[]{98, 99, 100, 96, 92, 93}, //map join
            new int[][]{
                    {Skill.LIEN_HOAN, 1, 300}, {Skill.LIEN_HOAN, 2, 400}, {Skill.LIEN_HOAN, 3, 500}, {Skill.LIEN_HOAN, 4, 600}, {Skill.LIEN_HOAN, 5, 700}, {Skill.LIEN_HOAN, 6, 800}, {Skill.LIEN_HOAN, 7, 900},
                    {Skill.KAMEJOKO, 7, 600}, {Skill.KAMEJOKO, 6, 700}, {Skill.KAMEJOKO, 5, 800}, {Skill.KAMEJOKO, 4, 900}, {Skill.KAMEJOKO, 3, 1000}, {Skill.KAMEJOKO, 2, 1100}, {Skill.KAMEJOKO, 1, 1002},
                    {Skill.ANTOMIC, 1, 130}, {Skill.ANTOMIC, 2, 140}, {Skill.ANTOMIC, 3, 150}, {Skill.ANTOMIC, 4, 160}, {Skill.ANTOMIC, 5, 170}, {Skill.ANTOMIC, 6, 190}, {Skill.ANTOMIC, 7, 200},
                    {Skill.MASENKO, 1, 210}, {Skill.MASENKO, 5, 220}, {Skill.MASENKO, 6, 230},
                    {Skill.DE_TRUNG, 7, 1000},
                    {Skill.THAI_DUONG_HA_SAN, 5, 100000},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 7, 15000},
                    {Skill.KHIEN_NANG_LUONG, 7, 50000},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            TypeAppear.APPEAR_WITH_ANOTHER
    );

}

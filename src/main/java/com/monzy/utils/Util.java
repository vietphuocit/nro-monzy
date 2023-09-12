package com.monzy.utils;

import com.monzy.models.boss.BossManager;
import com.monzy.models.item.Item;
import com.monzy.models.mob.Mob;
import com.monzy.models.npc.Npc;
import com.monzy.models.player.Player;
import com.monzy.server.Client;
import com.monzy.services.ItemService;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Util {

  private static final Random rand;
  private static final char[] SOURCE_CHARACTERS = {
    'À', 'Á', 'Â', 'Ã', 'È', 'É', 'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
    'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý', 'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ',
    'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ', 'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ',
    'ậ', 'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ', 'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế',
    'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ', 'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ',
    'ồ', 'Ổ', 'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ', 'Ợ', 'ợ', 'Ụ', 'ụ',
    'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ', 'ữ', 'Ự', 'ự',
  };
  private static final char[] DESTINATION_CHARACTERS = {
    'A', 'A', 'A', 'A', 'E', 'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a', 'a',
    'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u', 'y', 'A', 'a', 'D', 'd', 'I', 'i',
    'U', 'u', 'O', 'o', 'U', 'u', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
    'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e',
    'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
    'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'U', 'u',
    'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
  };

  static {
    rand = new Random();
  }

  public static String md5(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] messageDigest = md.digest(input.getBytes());
      BigInteger no = new BigInteger(1, messageDigest);
      String hashText = no.toString(16);
      while (hashText.length() < 32) {
        hashText = "0" + hashText;
      }
      return hashText;
    } catch (Exception e) {
    }
    return "";
  }

  public static int createIdBossClone(int idPlayer) {
    return -idPlayer - 100_000_000;
  }

  public static boolean contains(String[] arr, String key) {
    return Arrays.toString(arr).contains(key);
  }

  public static String numberToMoney(long power) {
    Locale locale = new Locale("vi", "VN");
    NumberFormat num = NumberFormat.getInstance(locale);
    num.setMaximumFractionDigits(1);
    if (power >= 1000000000) {
      return num.format((double) power / 1000000000) + " Tỷ";
    } else if (power >= 1000000) {
      return num.format((double) power / 1000000) + " Tr";
    } else if (power >= 1000) {
      return num.format((double) power / 1000) + " k";
    } else {
      return num.format(power);
    }
  }

  public static String powerToString(long power) {
    Locale locale = new Locale("vi", "VN");
    NumberFormat num = NumberFormat.getInstance(locale);
    num.setMaximumFractionDigits(1);
    if (power >= 1000000000) {
      return num.format((double) power / 1000000000) + " Tỷ";
    } else if (power >= 1000000) {
      return num.format((double) power / 1000000) + " Tr";
    } else if (power >= 1000) {
      return num.format((double) power / 1000) + " k";
    } else {
      return num.format(power);
    }
  }

  public static int getDistance(int x1, int y1, int x2, int y2) {
    return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
  }

  public static int getDistance(Player pl1, Player pl2) {
    return getDistance(pl1.location.x, pl1.location.y, pl2.location.x, pl2.location.y);
  }

  public static int getDistance(Player pl, Npc npc) {
    return getDistance(pl.location.x, pl.location.y, npc.cx, npc.cy);
  }

  public static int getDistance(Player pl, Mob mob) {
    return getDistance(pl.location.x, pl.location.y, mob.location.x, mob.location.y);
  }

  public static int getDistance(Mob mob1, Mob mob2) {
    return getDistance(mob1.location.x, mob1.location.y, mob2.location.x, mob2.location.y);
  }

  /**
   * @param from
   * @param to
   * @return [form, to]
   */
  public static int nextInt(int from, int to) {
    return from + rand.nextInt(to - from + 1);
  }

  /**
   * @param max
   * @return [0, max-1]
   */
  public static int nextInt(int max) {
    return rand.nextInt(max);
  }

  /**
   * @param ratio
   * @param typeRatio
   * @return true or false :v
   */
  public static boolean isTrue(int ratio, int typeRatio) {
    int num = nextInt(1, typeRatio);
    return num <= ratio;
  }

  /**
   * @param ratio
   * @param typeRatio
   * @return true or false :v
   */
  public static boolean isTrue(float ratio, int typeRatio) {
    if (ratio < 1) return isTrue(1, (int) (typeRatio * (1 / ratio)));
    return isTrue(1, (int) (typeRatio / ratio));
  }

  public static int getOne(int n1, int n2) {
    return rand.nextInt() % 2 == 0 ? n1 : n2;
  }

  public static int currentTimeSec() {
    return (int) System.currentTimeMillis() / 1000;
  }

  public static String replace(String text, String regex, String replacement) {
    return text.replace(regex, replacement);
  }

  public static boolean haveSpecialCharacter(String text) {
    Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(text);
    boolean b = m.find();
    return b || text.contains(" ");
  }

  public static boolean canDoWithTime(long lastTime, long miniTimeTarget) {
    return System.currentTimeMillis() - lastTime > miniTimeTarget;
  }

  public static char removeAccent(char ch) {
    int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
    if (index >= 0) {
      ch = DESTINATION_CHARACTERS[index];
    }
    return ch;
  }

  public static String removeAccent(String str) {
    StringBuilder sb = new StringBuilder(str);
    for (int i = 0; i < sb.length(); i++) {
      sb.setCharAt(i, removeAccent(sb.charAt(i)));
    }
    return sb.toString();
  }

  public static String generateRandomText(int len) {
    String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk" + "lmnopqrstuvwxyz!@#$%&";
    Random rnd = new Random();
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
      sb.append(chars.charAt(rnd.nextInt(chars.length())));
    }
    return sb.toString();
  }

  public static Object[] addArray(Object[]... arrays) {
    if (arrays == null || arrays.length == 0) {
      return null;
    }
    if (arrays.length == 1) {
      return arrays[0];
    }
    Object[] arr0 = arrays[0];
    for (int i = 1; i < arrays.length; i++) {
      arr0 = ArrayUtils.addAll(arr0, arrays[i]);
    }
    return arr0;
  }

  public static Item sendDo(int itemId, int sql, List<Item.ItemOption> ios) {
    //        InventoryServiceNew.gI().addItemBag(player,
    // ItemService.gI().createItemFromItemShop(is));
    //        InventoryServiceNew.gI().sendItemBags(player);
    Item item = ItemService.gI().createNewItem((short) itemId);
    item.itemOptions.addAll(ios);
    item.itemOptions.add(new Item.ItemOption(107, sql));
    return item;
  }

  public static boolean checkDo(Item.ItemOption itemOption) {
    switch (itemOption.optionTemplate.id) {
      case 0: // tấn công
        if (itemOption.param > 12000) {
          return false;
        }
        break;
      case 14: // chí mạng
        if (itemOption.param > 30) {
          return false;
        }
        break;
      case 107: // spl
      case 102: // spl
        if (itemOption.param > 8) {
          return false;
        }
        break;
      case 77:
      case 103:
      case 95:
      case 96:
        if (itemOption.param > 41) {
          return false;
        }
        break;
      case 50: // sd 3%
        if (itemOption.param > 24) {
          return false;
        }
        break;
      case 6: // hp
      case 7: // ki
        if (itemOption.param > 120000) {
          return false;
        }
        break;
      case 47: // giáp
        if (itemOption.param > 3500) {
          return false;
        }
        break;
    }
    return true;
  }

  public static void useCheckDo(Player player, Item item, String position) {
    try {
      if (item.template != null) {
        if (item.template.id >= 381 && item.template.id <= 385) {
          return;
        }
        if (item.template.id >= 66 && item.template.id <= 135) {
          return;
        }
        if (item.template.id >= 474 && item.template.id <= 515) {
          return;
        }
        item.itemOptions.forEach(
            itemOption -> {
              if (!Util.checkDo(itemOption)) {
                Logger.error(player.name + "-" + item.template.name + "-" + position + "\n");
              }
            });
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String phanthuong(int i) {
    switch (i) {
      case 1:
        return "5tr";
      case 2:
        return "3tr";
      case 3:
        return "1tr";
      default:
        return "100k";
    }
  }

  public static int randomBossId() {
    int bossId = Util.nextInt(10000);
    while (BossManager.gI().getBossById(bossId) != null) {
      bossId = Util.nextInt(10000);
    }
    return bossId;
  }

  public static long tinhLuyThua(int coSo, int soMu) {
    long ketQua = 1;
    for (int i = 0; i < soMu; i++) {
      ketQua *= coSo;
    }
    return ketQua;
  }

  public static void checkPlayer(Player player) {
    new Thread(
            () -> {
              List<Player> list =
                  Client.gI().getPlayers().stream()
                      .filter(
                          p ->
                              !p.isPet
                                  && !p.isNewPet
                                  && p.session.userId == player.session.userId)
                      .collect(Collectors.toList());
              if (list.size() > 1) {
                list.forEach(pp -> Client.gI().kickSession(pp.session));
                list.clear();
              }
            })
        .start();
  }

  public static int[] pickNRandInArr(int[] array, int n) {
    List<Integer> list = new ArrayList<Integer>(array.length);
    for (int i : array)
      list.add(i);
    Collections.shuffle(list);
    int[] answer = new int[n];
    for (int i = 0; i < n; i++)
      answer[i] = list.get(i);
    Arrays.sort(answer);
    return answer;
  }
}

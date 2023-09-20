package com.monzy.models.player;

import com.monzy.card.Card;
import com.monzy.card.OptionCard;
import com.monzy.consts.ConstPlayer;
import com.monzy.consts.ConstRatio;
import com.monzy.models.intrinsic.Intrinsic;
import com.monzy.models.item.Item;
import com.monzy.models.skill.Skill;
import com.monzy.server.Manager;
import com.monzy.services.*;
import com.monzy.utils.Logger;
import com.monzy.utils.SkillUtil;
import com.monzy.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class NPoint {

  public static final byte MAX_LIMIT = 9;
  public boolean isCrit;
  public boolean isCrit100;
  public int dameAfter;
  /*-----------------------Chỉ số cơ bản------------------------------------*/
  public byte numAttack;
  public short stamina, maxStamina;
  public byte limitPower;
  public long power;
  public long tiemNang;
  public int hp, hpMax, hpg;
  public int mp, mpMax, mpg;
  public int dame, dameg;
  public int def, defg;
  public int crit, critg;
  public byte speed = 8;
  public boolean teleport;
  public boolean khangTDHS;
  /** Chỉ số cộng thêm */
  public int hpAdd, mpAdd, dameAdd, defAdd, critAdd, hpHoiAdd, mpHoiAdd;
  /** //+#% sức đánh chí mạng */
  public List<Integer> tlDameCrit;
  /** Tỉ lệ hp, mp cộng thêm */
  public List<Integer> tlHp, tlMp;
  /** Tỉ lệ giáp cộng thêm */
  public List<Integer> tlDef;
  /** Tỉ lệ sức đánh/ sức đánh khi đánh quái */
  public List<Integer> tlDame, tlDameAttMob;
  /** Lượng hp, mp hồi mỗi 30s, mp hồi cho người khác */
  public int hpHoi, mpHoi, mpHoiCute;
  /** Tỉ lệ hp, mp hồi cộng thêm */
  public short tlHpHoi, tlMpHoi;
  /** Tỉ lệ hp, mp hồi bản thân và đồng đội cộng thêm */
  public short tlHpHoiBanThanVaDongDoi, tlMpHoiBanThanVaDongDoi;
  /** Tỉ lệ hút hp, mp khi đánh, hp khi đánh quái */
  public short tlHutHp, tlHutMp, tlHutHpMob;
  /** Tỉ lệ hút hp, mp xung quanh mỗi 5s */
  public short tlHutHpMpXQ;
  /** Tỉ lệ phản sát thương */
  public short tlPST;
  /** Tỉ lệ tiềm năng sức mạnh */
  public List<Integer> tlTNSM;
  /** Tỉ lệ vàng cộng thêm */
  public short tlGold;
  /** Tỉ lệ né đòn */
  public short tlNeDon;
  /** Tỉ lệ sức đánh đẹp cộng thêm cho bản thân và người xung quanh */
  public List<Integer> tlSDDep;
  /** Tỉ lệ giảm sức đánh */
  public short tlSubSD;

  public int voHieuChuong;
  /*------------------------Effect skin-------------------------------------*/
  public Item trainArmor;
  public boolean wornTrainArmor;
  public boolean wearingTrainArmor;
  public boolean wearingVoHinh;
  public boolean isKhongLanh;
  public short tlHpGiamODo;
  private Player player;
  private Intrinsic intrinsic;
  // --------------------------------------------------------------------------
  private long lastTimeHoiPhuc;
  /*-------------------------------------------------------------------------*/
  private long lastTimeHoiStamina;

  public NPoint(Player player) {
    this.player = player;
    this.tlHp = new ArrayList<>();
    this.tlMp = new ArrayList<>();
    this.tlDef = new ArrayList<>();
    this.tlDame = new ArrayList<>();
    this.tlDameAttMob = new ArrayList<>();
    this.tlSDDep = new ArrayList<>();
    this.tlTNSM = new ArrayList<>();
    this.tlDameCrit = new ArrayList<>();
  }

  /** Tính toán mọi chỉ số sau khi có thay đổi */
  public void calPoint() {
    if (this.player.pet != null) {
      this.player.pet.nPoint.setPointWhenWearClothes();
    }
    this.setPointWhenWearClothes();
  }

  private void setPointWhenWearClothes() {
    resetPoint();
    int idbt = 454;
    int countbt = 0;
    for (Item item : this.player.inventory.itemsBag) {
      if (countbt >= 1) {
        // Đã đạt đến mục tiêu, dừng vòng lặp
        break;
      }
      if (item.isNotNullItem()) {
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
          idbt = 921;
        }
        if (item.template.id == idbt) {
          for (Item.ItemOption io : item.itemOptions) {
            switch (io.optionTemplate.id) {
              case 50: // Sức đánh+#%
                this.tlDame.add(io.param);
                break;
              case 77: // HP+#%
                this.tlHp.add(io.param);
                break;
              case 103: // KI +#%
                this.tlMp.add(io.param);
                break;
              case 108: // #% Né đòn
                this.tlNeDon += (short) io.param;
                break;
              case 94: // Giáp #%
                this.tlDef.add(io.param);
                break;
              case 14: // +#% Chí mạng
                this.critAdd += io.param;
                break;
              case 80: // HP+#%/30s
                this.tlHpHoi += (short) io.param;
                break;
              case 81: // MP+#%/30s
                this.tlMpHoi += (short) io.param;
                break;
            }
          }
          countbt++;
        }
      }
    }
    Card card = player.cards.stream().filter(r -> r != null && r.used == 1).findFirst().orElse(null);
    if (card != null) {
      OptionCard optionCard = card.optionCards.get(Math.max(0, card.level));
      switch (optionCard.id) {
        case 50: // Sức đánh+#%
          this.tlDame.add(optionCard.param);
          break;
        case 77: // HP+#%
          this.tlHp.add(optionCard.param);
          break;
        case 103: // KI +#%
          this.tlMp.add(optionCard.param);
          break;
      }
    }
    this.player.setClothes.worldcup = 0;
    for (Item item : this.player.inventory.itemsBody) {
      if (item.isNotNullItem()) {
        switch (item.template.id) {
          case 883:
          case 904:
          case 966:
          case 982:
          case 983:
            player.setClothes.worldcup++;
        }
        if (item.template.id >= 592 && item.template.id <= 594) {
          this.teleport = true;
        }
        for (Item.ItemOption io : item.itemOptions) {
          if (player.isSkinFusion(item) && !player.isFusion()) {
            break;
          }
          switch (io.optionTemplate.id) {
            case 0: // Tấn công +#
              this.dameAdd += io.param;
              break;
            case 2: // HP, KI+#000
              this.hpAdd += io.param * 1000;
              this.mpAdd += io.param * 1000;
              break;
            case 3: // fake
              this.voHieuChuong += io.param;
              break;
            case 5: // +#% sức đánh chí mạng
              this.tlDameCrit.add(io.param);
              break;
            case 6: // HP+#
              this.hpAdd += io.param;
              break;
            case 7: // KI+#
              this.mpAdd += io.param;
              break;
            case 8: // Hút #% HP, KI xung quanh mỗi 5 giây
              this.tlHutHpMpXQ += (short) io.param;
              break;
            case 14: // Chí mạng+#%
              this.critAdd += io.param;
              break;
            case 19: // Tấn công+#% khi đánh quái
              this.tlDameAttMob.add(io.param);
              break;
            case 22: // HP+#K
              this.hpAdd += io.param * 1000;
              break;
            case 23: // MP+#K
              this.mpAdd += io.param * 1000;
              break;
            case 27: // +# HP/30s
              this.hpHoiAdd += io.param;
              break;
            case 28: // +# KI/30s
              this.mpHoiAdd += io.param;
              break;
            case 33: // dịch chuyển tức thời
              this.teleport = true;
              break;
            case 47: // Giáp+#
              this.defAdd += io.param;
              break;
            case 48: // HP/KI+#
              this.hpAdd += io.param;
              this.mpAdd += io.param;
              break;
            case 49: // Tấn công+#%
            case 50: // Sức đánh+#%
            case 147: // +#% sức đánh
              this.tlDame.add(io.param);
              break;
            case 77: // HP+#%
              this.tlHp.add(io.param);
              break;
            case 80: // HP+#%/30s
              this.tlHpHoi += (short) io.param;
              break;
            case 81: // MP+#%/30s
              this.tlMpHoi += (short) io.param;
              break;
            case 88: // Cộng #% exp khi đánh quái
            case 101: // +#% TN,SM
              this.tlTNSM.add(io.param);
              break;
            case 94: // Giáp #%
              this.tlDef.add(io.param);
              break;
            case 95: // Biến #% tấn công thành HP
              this.tlHutHp += (short) io.param;
              break;
            case 96: // Biến #% tấn công thành MP
              this.tlHutMp += (short) io.param;
              break;
            case 97: // Phản #% sát thương
              this.tlPST += (short) io.param;
              break;
            case 100: // +#% vàng từ quái
              this.tlGold += (short) io.param;
              break;
            case 103: // KI +#%
              this.tlMp.add(io.param);
              break;
            case 104: // Biến #% tấn công quái thành HP
              this.tlHutHpMob += (short) io.param;
              break;
            case 105: // Vô hình khi không đánh quái và boss
              this.wearingVoHinh = true;
              break;
            case 106: // Không ảnh hưởng bởi cái lạnh
              this.isKhongLanh = true;
              break;
            case 108: // #% Né đòn
              this.tlNeDon += (short) io.param; // đối nghịch
              break;
            case 109: // Hôi, giảm #% HP
              this.tlHpGiamODo += (short) io.param;
              break;
            case 116: // Kháng thái dương hạ san
              this.khangTDHS = true;
              break;
            case 117: // Đẹp +#% SĐ cho mình và người xung quanh
              this.tlSDDep.add(io.param);
              break;
            case 75: // Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
              this.tlSubSD += 50;
              this.tlTNSM.add(io.param);
              this.tlGold += (short) io.param;
              break;
            case 162: // Cute hồi #% KI/s bản thân và xung quanh
              this.mpHoiCute += io.param;
              break;
            case 173: // Phục hồi #% HP và KI cho đồng đội
              this.tlHpHoiBanThanVaDongDoi += (short) io.param;
              this.tlMpHoiBanThanVaDongDoi += (short) io.param;
              break;
          }
        }
      }
    }
    setDameTrainArmor();
    setBasePoint();
  }

  private void setDameTrainArmor() {
    if (!this.player.isPet && !this.player.isBoss) {
      if (this.player.inventory.itemsBody.size() < 7) {
        return;
      }
      try {
        Item gtl = this.player.inventory.itemsBody.get(6);
        if (gtl.isNotNullItem()) {
          this.wearingTrainArmor = true;
          this.wornTrainArmor = true;
          this.player.inventory.trainArmor = gtl;
          this.tlSubSD += (short) ItemService.gI().getPercentTrainArmor(gtl);
        } else {
          if (this.wornTrainArmor) {
            this.wearingTrainArmor = false;
            for (Item.ItemOption io : this.player.inventory.trainArmor.itemOptions) {
              if (io.optionTemplate.id == 9 && io.param > 0) {
                this.tlDame.add(ItemService.gI().getPercentTrainArmor(this.player.inventory.trainArmor));
                break;
              }
            }
          }
        }
      } catch (Exception e) {
        Logger.error("Lỗi get giáp tập luyện " + this.player.name + "\n");
      }
    }
  }

  public void setBasePoint() {
    setHpMax();
    setHp();
    setMpMax();
    setMp();
    setDame();
    setDef();
    setCrit();
    setHpHoi();
    setMpHoi();
    setNeDon();
    settlGold();
  }

  private void setNeDon() {
    // ngọc rồng đen 4 sao
    if (this.player.rewardBlackBall.rewardsExpire[3] > System.currentTimeMillis()) {
      this.tlNeDon += RewardBlackBall.R4S_1;
    }
  }

  private void setHpHoi() {
    this.hpHoi = this.hpMax / 100;
    this.hpHoi += this.hpHoiAdd;
    this.hpHoi += (int) (this.hpMax * (this.tlHpHoi / 100f));
    this.hpHoi += (int) (this.hpMax * (this.tlHpHoiBanThanVaDongDoi / 100f));
  }

  private void setMpHoi() {
    this.mpHoi = this.mpMax / 100;
    this.mpHoi += this.mpHoiAdd;
    this.mpHoi += (int)( this.mpMax * (this.tlMpHoi / 100f));
    this.mpHoi += (int)( this.mpMax * (this.tlMpHoiBanThanVaDongDoi / 100f));
  }

  private void setHpMax() {
    this.hpMax = this.hpg;
    this.hpMax += this.hpAdd;
    //  item
    for (int tl : this.tlHp) {
      this.hpMax += (int) (this.hpMax * (tl / 100f));
    }
    //  set nappa
    if (this.player.setClothes.nappa == 5) {
      this.hpMax = (int) (this.hpMax * 1.8f);
    }
    //  set world cup
    if (this.player.setClothes.worldcup == 2) {
      this.hpMax += (int) (this.hpMax * (10 / 100f));
    }
    //  ngọc rồng đen 2 sao
    if (this.player.rewardBlackBall.rewardsExpire[1] > System.currentTimeMillis()) {
      this.hpMax += (int) (this.hpMax * (RewardBlackBall.R2S_1 / 100f));
    }
    //  khỉ
    if (this.player.effectSkill.isMonkey) {
      if (!this.player.isPet || ((Pet) this.player).status != Pet.FUSION) {
        int percent = SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
        this.hpMax += (int) (this.hpMax * (percent / 100f));
      }
    }
    //  pet mabư
    if (this.player.isPet
        && ((Pet) this.player).typePet == 1
        && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
        || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
      this.hpMax += (int) (this.hpMax * (10 / 100f));
    }
    //  pet pic
    if (this.player.isPet
        && ((Pet) this.player).typePet == 3
        && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
        || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
      this.hpMax += (int) (this.hpMax * (20 / 100f));
    }
    //  pet berus
    if (this.player.isPet
        && ((Pet) this.player).typePet == 2
        && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
        || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
      this.hpMax += (int) (this.hpMax * (30 / 100f));
    }
    // phù
    if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
      this.hpMax *= this.player.effectSkin.xHPKI;
    }
    // +HP đệ
    if (this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
      this.hpMax += this.player.pet.nPoint.hpMax;
    }
    // btc +2
    if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
      this.hpMax = (int) (this.hpMax * 1.1f);
    }
    // huýt sáo
    if (!this.player.isPet || ((Pet) this.player).status != Pet.FUSION) {
      if (this.player.effectSkill.tiLeHPHuytSao != 0) {
        this.hpMax += (int) (this.hpMax * (this.player.effectSkill.tiLeHPHuytSao / 100f));
      }
    }
    // bổ huyết
    if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet) {
      this.hpMax *= 2;
    }
    // coller
    if (this.player.zone != null
        && MapService.gI().isMapCold(this.player.zone.map)
        && !this.isKhongLanh) {
      this.hpMax /= 2;
    }
  }

  private void setHp() {
    if (this.hp > this.hpMax) {
      this.hp = this.hpMax;
    }
  }

  private void setMpMax() {
    this.mpMax = this.mpg;
    this.mpMax += this.mpAdd;
    // đồ
    for (Integer tl : this.tlMp) {
      this.mpMax += (int) (this.mpMax * (tl / 100f));
    }
    // ngọc rồng đen 2 sao
    if (this.player.rewardBlackBall.rewardsExpire[1] > System.currentTimeMillis()) {
      this.mpMax += (int) (this.mpMax * (RewardBlackBall.R3S_1 / 100f));
    }
    // set worldcup
    if (this.player.setClothes.worldcup == 2) {
      this.mpMax += (int) (this.mpMax * (10 / 100f));
    }
    // pet mabư
    if (this.player.isPet
        && ((Pet) this.player).typePet == 1
        && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
        || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
      this.mpMax += (int) (this.mpMax * (10 / 100f));
      ;
    }
    // pet pic
    if (this.player.isPet
        && ((Pet) this.player).typePet == 3
        && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
        || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
      this.mpMax += (int) (this.mpMax * (20 / 100f));
    }
    // pet berus
    if (this.player.isPet
        && ((Pet) this.player).typePet == 2
        && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
        || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
      this.mpMax += (int) (this.mpMax * (30 / 100f));
    }
    // phù
    if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
      this.mpMax *= this.player.effectSkin.xHPKI;
    }
    // +MP đệ
    if (this.player.fusion.typeFusion != 0) {
      this.mpMax += this.player.pet.nPoint.mpMax;
    }
    // btc2
    if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
      this.mpMax = (int) (this.mpMax * 1.1f);
    }
    // bổ khí
    if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi) {
      this.mpMax *= 2;
    }
  }

  private void setMp() {
    if (this.mp > this.mpMax) {
      this.mp = this.mpMax;
    }
  }

  private void setDame() {
    this.dame = this.dameg;
    this.dame += this.dameAdd;
    // đồ
    for (Integer tl : this.tlDame) {
      this.dame += (int) (this.dame * (tl / 100f));
    }
    for (Integer tl : this.tlSDDep) {
      this.dame += (int) (this.dame * (tl / 100f));
    }
    // pet mabư
    if (this.player.isPet
        && ((Pet) this.player).typePet == 1
        && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
        || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
      this.dame += (int) (this.dame * (10 / 100f));
    }
    // pet pic
    if (this.player.isPet
        && ((Pet) this.player).typePet == 3
        && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
        || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
      this.dame += (int) (this.dame * (20 / 100f));
    }
    // pet berus
    if (this.player.isPet
        && ((Pet) this.player).typePet == 2
        && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
        || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
      this.dame += (int) (this.dame * (30 / 100f));
    }
    // thức ăn
    if (!this.player.isPet && this.player.itemTime.isEatMeal && this.player.itemTime.isMealCooler()
        || this.player.isPet
            && ((Pet) this.player).master.itemTime.isEatMeal
            && ((Pet) this.player).master.itemTime.isMealCooler()) {
      this.dame += (int) (this.dame * (10 / 100f));
    }
    // +SĐ đệ
    if (this.player.fusion.typeFusion != 0) {
      this.dame += this.player.pet.nPoint.dame;
    }
    // btc 2
    if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
      this.dame = (int) (this.dame * 1.1);
    }
    // cuồng nộ
    if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo) {
      this.dame *= 2;
    }
    // giảm dame
    this.dame -= (int) (this.dame * (this.tlSubSD / 100f));
    // map cold
    if (this.player.zone != null
        && MapService.gI().isMapCold(this.player.zone.map)
        && !this.isKhongLanh) {
      this.dame /= 2;
    }
    // ngọc rồng đen 1 sao
    if (this.player.rewardBlackBall.rewardsExpire[0] > System.currentTimeMillis()) {
      this.dame += (int) (this.dame * (RewardBlackBall.R1S_1 / 100f));
    }
    // khỉ
    if (this.player.effectSkill.isMonkey) {
      if (!this.player.isPet || ((Pet) this.player).status != Pet.FUSION) {
        int percent = SkillUtil.getPercentDameMonkey(player.effectSkill.levelMonkey);
        this.dame += (int) (this.dame * percent / 100f);
      }
    }
  }

  private void setDef() {
    this.def = this.defg * 4;
    this.def += this.defAdd;
    // đồ
    for (Integer tl : this.tlDef) {
      this.def += (this.def * tl / 100);
    }
    // ngọc rồng đen 3 sao
    if (this.player.rewardBlackBall.rewardsExpire[2] > System.currentTimeMillis()) {
      this.def += RewardBlackBall.R3S_1;
    }
  }

  private void setCrit() {
    this.crit = this.critg;
    this.crit += this.critAdd;
    // ngọc rồng đen 5 sao
    if (this.player.rewardBlackBall.rewardsExpire[4] > System.currentTimeMillis()) {
      this.crit += RewardBlackBall.R5S_1;
    }
    // biến khỉ
    if (this.player.effectSkill.isMonkey) {
      this.crit = 110;
    }
  }

  private void resetPoint() {
    this.voHieuChuong = 0;
    this.hpAdd = 0;
    this.mpAdd = 0;
    this.dameAdd = 0;
    this.defAdd = 0;
    this.critAdd = 0;
    this.tlHp.clear();
    this.tlMp.clear();
    this.tlDef.clear();
    this.tlDame.clear();
    this.tlDameCrit.clear();
    this.tlDameAttMob.clear();
    this.tlGold = 0;
    this.tlHpHoiBanThanVaDongDoi = 0;
    this.tlMpHoiBanThanVaDongDoi = 0;
    this.hpHoi = 0;
    this.mpHoi = 0;
    this.mpHoiCute = 0;
    this.tlHpHoi = 0;
    this.tlMpHoi = 0;
    this.tlHutHp = 0;
    this.tlHutMp = 0;
    this.tlHutHpMob = 0;
    this.tlHutHpMpXQ = 0;
    this.tlPST = 0;
    this.tlTNSM.clear();
    this.tlDameAttMob.clear();
    this.tlNeDon = 0;
    this.tlSDDep.clear();
    this.tlSubSD = 0;
    this.tlHpGiamODo = 0;
    this.teleport = false;
    this.wearingVoHinh = false;
    this.isKhongLanh = false;
    this.khangTDHS = false;
  }

  public void addHp(int hp) {
    this.hp += hp;
    if (this.hp > this.hpMax) {
      this.hp = this.hpMax;
    }
  }

  public void addMp(int mp) {
    this.mp += mp;
    if (this.mp > this.mpMax) {
      this.mp = this.mpMax;
    }
  }

  public void setHp(long hp) {
    if (hp > this.hpMax) {
      this.hp = this.hpMax;
    } else {
      this.hp = (int) hp;
    }
  }

  public void setMp(long mp) {
    if (mp > this.mpMax) {
      this.mp = this.mpMax;
    } else {
      this.mp = (int) mp;
    }
  }

  public void settlGold() {
    if (intrinsic != null && intrinsic.id == 23) {
      this.tlGold += intrinsic.param1;
    }
  }

  private void setIsCrit() {
    if (intrinsic != null && intrinsic.id == 25 && this.getCurrPercentHP() <= intrinsic.param1) {
      isCrit = true;
    } else if (isCrit100) {
      isCrit100 = false;
      isCrit = true;
    } else {
      isCrit = Util.isTrue(this.crit, ConstRatio.PER100);
    }
  }

  public int getDameAttack(boolean isAttackMob) {
    setIsCrit();
    long dameAttack = this.dame;
    intrinsic = this.player.playerIntrinsic.intrinsic;
    int percentDameIntrinsic = 0;
    int percentDameSkill = 0;
    byte percentXDame = 0;
    Skill skillSelect = player.playerSkill.skillSelect;
    switch (skillSelect.template.id) {
      case Skill.DRAGON:
        if (intrinsic.id == 1) {
          percentDameIntrinsic = intrinsic.param1;
        }
        percentDameSkill = skillSelect.damage;
        break;
      case Skill.KAMEJOKO:
        if (intrinsic.id == 2) {
          percentDameIntrinsic = intrinsic.param1;
        }
        //                int dameX4 =  player.inventory.getParam(player.inventory.itemsBody.get(5),
        // 159);
        //                if (dameX4 > 0) {
        //                dameAttack *= dameX4;
        //                }
        percentDameSkill = skillSelect.damage;
        if (this.player.setClothes.songoku == 5) {
          percentXDame = 100;
        }
        break;
      case Skill.GALICK:
        if (intrinsic.id == 16) {
          percentDameIntrinsic = intrinsic.param1;
        }
        percentDameSkill = skillSelect.damage;
        if (this.player.setClothes.kakarot == 5) {
          percentXDame = 100;
        }
        break;
      case Skill.ANTOMIC:
        if (intrinsic.id == 17) {
          percentDameIntrinsic = intrinsic.param1;
        }
        percentDameSkill = skillSelect.damage;
        break;
      case Skill.DEMON:
        if (intrinsic.id == 8) {
          percentDameIntrinsic = intrinsic.param1;
        }
        percentDameSkill = skillSelect.damage;
        break;
      case Skill.MASENKO:
        if (intrinsic.id == 9) {
          percentDameIntrinsic = intrinsic.param1;
        }
        percentDameSkill = skillSelect.damage;
        break;
      case Skill.KAIOKEN:
        if (intrinsic.id == 26) {
          percentDameIntrinsic = intrinsic.param1;
        }
        percentDameSkill = skillSelect.damage;
        if (this.player.setClothes.kaioken == 5) {
          percentXDame = 100;
        }
        break;
      case Skill.LIEN_HOAN:
        if (intrinsic.id == 13) {
          percentDameIntrinsic = intrinsic.param1;
        }
        percentDameSkill = skillSelect.damage;
        if (this.player.setClothes.ocTieu == 5) {
          percentXDame = 50;
        }
        break;
      case Skill.DICH_CHUYEN_TUC_THOI:
        dameAttack *= 2;
        dameAttack =
            Util.nextInt(
                (int) (dameAttack - (dameAttack * 5 / 100)),
                (int) (dameAttack + (dameAttack * 5 / 100)));
        return (int) dameAttack;
      case Skill.MAKANKOSAPPO:
        this.isCrit = false;
        percentDameSkill = skillSelect.damage;
        int dameSkill = (int) (this.mpMax * (percentDameSkill / 100f));
        if (this.player.setClothes.picolo == 5) {
          dameSkill = (int) (dameSkill * 1.5f);
        }
        return dameSkill;
      case Skill.QUA_CAU_KENH_KHI:
        this.isCrit = false;
        percentDameSkill = skillSelect.damage;
        int tlDame = (int) (this.player.zone.getPlayers().stream().filter(pl -> Util.getDistance(this.player, pl) < 150).count() * 5);
        int dame = (int) (this.dame * (1f + (tlDame / 100f)) * (percentDameSkill / 100f));
        if (this.player.setClothes.krilin == 5) {
          dame *= 2;
        }
        return dame;
    }
    if (intrinsic.id == 18 && this.player.effectSkill.isMonkey) {
      percentDameIntrinsic = intrinsic.param1;
    }
    if (percentDameSkill != 0) {
      dameAttack = dameAttack * percentDameSkill / 100;
    }
    dameAttack += (dameAttack * percentDameIntrinsic / 100);
    dameAttack += (dameAttack * dameAfter / 100);
    if (isAttackMob) {
      for (Integer tl : this.tlDameAttMob) {
        dameAttack += (dameAttack * tl / 100);
      }
    }
    dameAfter = 0;
    if (this.player.isPet
        && ((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
      dameAttack *= 2;
    }
    if (isCrit) {
      dameAttack *= 2;
      for (Integer tl : this.tlDameCrit) {
        dameAttack += (dameAttack * tl / 100);
      }
    }
    dameAttack += (dameAttack * percentXDame / 100);
    dameAttack =
        Util.nextInt(
            (int) (dameAttack - (dameAttack * 5 / 100)),
            (int) (dameAttack + (dameAttack * 5 / 100)));
    if (player.isPl()) {
      if (player.inventory.haveOption(player.inventory.itemsBody, 5, 159)) {
        if (Util.canDoWithTime(player.lastTimeUseOption, 60000)
            && (player.playerSkill.skillSelect.skillId == Skill.KAMEJOKO
                || player.playerSkill.skillSelect.skillId == Skill.ANTOMIC
                || player.playerSkill.skillSelect.skillId == Skill.MASENKO)) {
          dameAttack *= player.inventory.getParam(player.inventory.itemsBody.get(5), 159);
          player.lastTimeUseOption = System.currentTimeMillis();
        }
      }
    }
    // check activation set
    return (int) dameAttack;
  }

  public int getCurrPercentHP() {
    if (this.hpMax == 0) {
      return 100;
    }
    return (int) ((long) this.hp * 100 / this.hpMax);
  }

  public int getCurrPercentMP() {
    return (int) ((long) this.mp * 100 / this.mpMax);
  }

  public void setFullHpMp() {
    this.hp = this.hpMax;
    this.mp = this.mpMax;
  }

  public void subHP(int sub) {
    this.hp -= sub;
    if (this.hp < 0) {
      this.hp = 0;
    }
  }

  public void subMP(int sub) {
    this.mp -= sub;
    if (this.mp < 0) {
      this.mp = 0;
    }
  }

  public long calSucManhTiemNang(long tiemNang) {
    if (power < getPowerLimit()) {
      int tlTNSM = 0;
      for (Integer tl : this.tlTNSM) {
        tlTNSM += tl;
      }
      tiemNang += (tiemNang * tlTNSM / 100);
      if (this.player.cFlag != 0) {
        if (this.player.cFlag == 8) {
          tiemNang += (tiemNang * 10 / 100);
        } else {
          tiemNang += (tiemNang * 5 / 100);
        }
      }
      tiemNang *= Manager.RATE_EXP;
      long tn = tiemNang;
      if (this.player.charms.tdTriTue > System.currentTimeMillis()) {
        tiemNang += tn;
      }
      if (this.player.charms.tdTriTue3 > System.currentTimeMillis()) {
        tiemNang += tn * 2;
      }
      if (this.player.charms.tdTriTue4 > System.currentTimeMillis()) {
        tiemNang += tn * 3;
      }
      if (this.intrinsic != null && this.intrinsic.id == 24) {
        tiemNang += tiemNang * this.intrinsic.param1 / 100;
      }
      if (this.player.isPet) {
        if (((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
          tiemNang += tn * 2;
        }
      }
      tiemNang = calSubTNSM(tiemNang);
      if (tiemNang <= 0) {
        tiemNang = 1;
      }
    } else {
      tiemNang = 0;
    }
    return tiemNang;
  }

  public long calSubTNSM(long tiemNang) {
    switch (this.limitPower) {
      case 8:
        return tiemNang % 10000;
      case 9:
        return tiemNang % 2000;
    }
    return tiemNang;
  }

  public short getTileHutHp(boolean isMob) {
    if (isMob) {
      return (short) (this.tlHutHp + this.tlHutHpMob);
    } else {
      return this.tlHutHp;
    }
  }

  public short getTiLeHutMp() {
    return this.tlHutMp;
  }

  public int subDameInjureWithDef(int dame) {
    int def = this.def;
    dame -= def;
    if (this.player.itemTime.isUseGiapXen) {
      dame /= 2;
    }
    if (dame < 0) {
      dame = 1;
    }
    return dame;
  }

  public int subDameInjureWithTLDef(int dame) {
    int sum = tlDef.stream().mapToInt(Integer::intValue).sum();
    dame -= dame * sum / 100;
    if (dame < 0) {
      dame = 1;
    }
    return dame;
  }

  public boolean canOpenPower() {
    return this.power < getPowerLimit();
  }

  public long getPowerLimit() {
    switch (limitPower) {
      case 0:
        return 17999999999L;
      case 1:
        return 18999999999L;
      case 2:
        return 20999999999L;
      case 3:
        return 24999999999L;
      case 4:
        return 30999999999L;
      case 5:
        return 40999999999L;
      case 6:
        return 60999999999L;
      case 7:
        return 80999999999L;
      case 8:
        return 120999999999L;
      case 9:
        return 180999999999L;
      default:
        return 0;
    }
  }

  public long getPowerNextLimit() {
    switch (limitPower + 1) {
      case 0:
        return 17999999999L;
      case 1:
        return 18999999999L;
      case 2:
        return 20999999999L;
      case 3:
        return 24999999999L;
      case 4:
        return 30999999999L;
      case 5:
        return 40999999999L;
      case 6:
        return 60999999999L;
      case 7:
        return 80999999999L;
      case 8:
        return 2000999999999L;
      case 9:
        return 2500999999999L;
      default:
        return 0;
    }
  }

  public int getHpMpLimit() {
    if (limitPower == 0) {
      return 220000;
    }
    if (limitPower == 1) {
      return 240000;
    }
    if (limitPower == 2) {
      return 300000;
    }
    if (limitPower == 3) {
      return 350000;
    }
    if (limitPower == 4) {
      return 400000;
    }
    if (limitPower == 5) {
      return 450000;
    }
    if (limitPower == 6) {
      return 500000;
    }
    if (limitPower == 7) {
      return 550000;
    }
    if (limitPower == 8) {
      return 560000;
    }
    if (limitPower == 9) {
      return 600000;
    }
    return 0;
  }

  public int getDameLimit() {
    if (limitPower == 0) {
      return 11000;
    }
    if (limitPower == 1) {
      return 12000;
    }
    if (limitPower == 2) {
      return 15000;
    }
    if (limitPower == 3) {
      return 18000;
    }
    if (limitPower == 4) {
      return 20000;
    }
    if (limitPower == 5) {
      return 22000;
    }
    if (limitPower == 6) {
      return 25000;
    }
    if (limitPower == 7) {
      return 30000;
    }
    if (limitPower == 8) {
      return 31000;
    }
    if (limitPower == 9) {
      return 32000;
    }
    return 0;
  }

  public short getDefLimit() {
    if (limitPower == 0) {
      return 550;
    }
    if (limitPower == 1) {
      return 600;
    }
    if (limitPower == 2) {
      return 700;
    }
    if (limitPower == 3) {
      return 800;
    }
    if (limitPower == 4) {
      return 1000;
    }
    if (limitPower == 5) {
      return 1200;
    }
    if (limitPower == 6) {
      return 1400;
    }
    if (limitPower == 7) {
      return 1600;
    }
    if (limitPower == 8) {
      return 1700;
    }
    if (limitPower == 9) {
      return 1800;
    }
    return 0;
  }

  public byte getCritLimit() {
    if (limitPower == 0) {
      return 5;
    }
    if (limitPower == 1) {
      return 6;
    }
    if (limitPower == 2) {
      return 7;
    }
    if (limitPower == 3) {
      return 8;
    }
    if (limitPower == 4) {
      return 9;
    }
    if (limitPower == 5) {
      return 10;
    }
    if (limitPower == 6) {
      return 10;
    }
    if (limitPower == 7) {
      return 10;
    }
    if (limitPower == 8) {
      return 10;
    }
    if (limitPower == 9) {
      return 10;
    }
    return 0;
  }

  // **************************************************************************
  // POWER - TIEM NANG
  public void powerUp(long power) {
    this.power += power;
    TaskService.gI().checkDoneTaskPower(player, this.power);
  }

  public void tiemNangUp(long tiemNang) {
    this.tiemNang += tiemNang;
  }

  public void increasePoint(byte type, short point) {
    if (point <= 0 || point > 100) {
      return;
    }
    long tiemNangUse;
    if (type == 0) {
      int pointHp = point * 20;
      tiemNangUse = point * (2L * (this.hpg + 1000) + pointHp - 20) / 2;
      if ((this.hpg + pointHp) <= getHpMpLimit()) {
        if (doUseTiemNang(tiemNangUse)) {
          hpg += pointHp;
        }
      } else {
        Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
        return;
      }
    }
    if (type == 1) {
      int pointMp = point * 20;
      tiemNangUse = point * (2L * (this.mpg + 1000) + pointMp - 20) / 2;
      if ((this.mpg + pointMp) <= getHpMpLimit()) {
        if (doUseTiemNang(tiemNangUse)) {
          mpg += pointMp;
        }
      } else {
        Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
        return;
      }
    }
    if (type == 2) {
      tiemNangUse = point * (2L * this.dameg + point - 1) / 2 * 100;
      if ((this.dameg + point) <= getDameLimit()) {
        if (doUseTiemNang(tiemNangUse)) {
          dameg += point;
        }
      } else {
        Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
        return;
      }
    }
    if (type == 3) {
      tiemNangUse = 2L * (this.defg + 5) / 2 * 100000;
      if ((this.defg + point) <= getDefLimit()) {
        if (doUseTiemNang(tiemNangUse)) {
          defg += point;
        }
      } else {
        Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
        return;
      }
    }
    if (type == 4) {
      tiemNangUse = 50000000L;
      for (int i = 0; i < this.critg; i++) {
        tiemNangUse *= 5L;
      }
      if ((this.critg + point) <= getCritLimit()) {
        if (doUseTiemNang(tiemNangUse)) {
          critg += point;
        }
      } else {
        Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
        return;
      }
    }
    Service.gI().point(player);
  }

  private boolean doUseTiemNang(long tiemNang) {
    if (this.tiemNang < tiemNang) {
      Service.gI().sendThongBaoOK(player, "Bạn không đủ tiềm năng");
      return false;
    }
    if (this.tiemNang - tiemNang >= 0) {
      this.tiemNang -= tiemNang;
      TaskService.gI().checkDoneTaskUseTiemNang(player);
      return true;
    }
    return false;
  }

  public void update() {
    if (player != null && player.effectSkill != null) {
      if (player.effectSkill.isCharging && player.effectSkill.countCharging < 10) {
        int tiLeHoiPhuc = SkillUtil.getPercentCharge(player.playerSkill.skillSelect.point);
        if (!player.isDie()
            && !player.effectSkill.isHaveEffectSkill()
            && (hp < hpMax || mp < mpMax)) {
          PlayerService.gI().hoiPhuc(player, (int) (hpMax * (tiLeHoiPhuc / 100f)), (int) (mpMax * (tiLeHoiPhuc / 100f)));
          if (player.effectSkill.countCharging % 3 == 0) {
            Service.gI().chat(player, "Phục hồi năng lượng " + getCurrPercentHP() + "%");
          }
        } else {
          EffectSkillService.gI().stopCharge(player);
        }
        if (++player.effectSkill.countCharging >= 10) {
          EffectSkillService.gI().stopCharge(player);
        }
      }
      if (Util.canDoWithTime(lastTimeHoiPhuc, 30000)) {
        PlayerService.gI().hoiPhuc(this.player, hpHoi, mpHoi);
        this.lastTimeHoiPhuc = System.currentTimeMillis();
      }
      if (Util.canDoWithTime(lastTimeHoiStamina, 60000) && this.stamina < this.maxStamina) {
        this.stamina++;
        this.lastTimeHoiStamina = System.currentTimeMillis();
        if (!this.player.isBoss && !this.player.isPet) {
          PlayerService.gI().sendCurrentStamina(this.player);
        }
      }
    }
    // hồi phục 30s
    // hồi phục thể lực
  }

  public void dispose() {
    this.intrinsic = null;
    this.player = null;
    this.tlHp = null;
    this.tlMp = null;
    this.tlDef = null;
    this.tlDame = null;
    this.tlDameAttMob = null;
    this.tlSDDep = null;
    this.tlTNSM = null;
  }
}

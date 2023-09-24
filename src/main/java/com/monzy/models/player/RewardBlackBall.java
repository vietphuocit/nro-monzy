package com.monzy.models.player;

import com.monzy.models.map.blackball.BlackBallWar;
import com.monzy.services.Service;
import com.monzy.utils.TimeUtil;
import com.monzy.utils.Util;

import java.util.Date;

public class RewardBlackBall {

  public static final int R1S_1 = 20;
  public static final int R2S_1 = 15;
  public static final int R3S_1 = 20;
  public static final int R4S_1 = 10;
  public static final int R5S_1 = 20;
	public static final int R6S_1 = 300_000_000;
	public static final int R7S_1 = 500;
	public static final int TIME_WAIT = 22 * 60 * 60 * 1000; // Đợi 1 tiếng để được nhận quà
	private static final int TIME_REWARD = 22 * 60 * 60 * 1000; // Tồn tại 22 tiếng
	public long[] rewardsExpire;
	public int[] quantityBlackBall;
  public long[] lastTimeGetReward;
  private Player player;

  public RewardBlackBall(Player player) {
    this.player = player;
	  this.rewardsExpire = new long[7];
    this.lastTimeGetReward = new long[7];
	  this.quantityBlackBall = new int[7];
  }

  public void reward(byte star) {
	  if (this.rewardsExpire[star - 1] > BlackBallWar.TIME_OPEN) {
		  quantityBlackBall[star - 1]++;
    }
	  this.rewardsExpire[star - 1] = System.currentTimeMillis() + TIME_REWARD;
    Service.gI().point(player);
  }

  public void getRewardSelect(byte select) {
    int index = 0;
	  for (int i = 0; i < rewardsExpire.length; i++) {
		  if (rewardsExpire[i] > System.currentTimeMillis()) {
        index++;
        if (index == select + 1) {
          getReward(i + 1);
          break;
        }
      }
    }
  }

  private void getReward(int star) {
	  if (rewardsExpire[star - 1] > System.currentTimeMillis()
        && Util.canDoWithTime(lastTimeGetReward[star - 1], TIME_WAIT)) {
      switch (star) {
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
	        Service.gI().sendThongBao(player, "Phần thưởng chỉ số tự động nhận");
	        return;
        case 6:
	        this.player.inventory.gold += R6S_1;
	        Service.gI().sendMoney(this.player);
	        Service.gI().sendThongBao(this.player, "Bạn vừa nhận được 20tr vàng");
	        return;
        case 7:
	        this.player.inventory.ruby += R7S_1;
	        Service.gI().sendMoney(this.player);
	        Service.gI().sendThongBao(this.player, "Bạn vừa nhận được 100 hồng ngọc");
      }
    } else {
		  Service.gI().sendThongBao(player,
				  "Chưa thể nhận phần quà ngay lúc này, vui lòng đợi "
						  + TimeUtil.diffDate(new Date(lastTimeGetReward[star - 1]), new Date(lastTimeGetReward[star - 1] + TIME_WAIT), TimeUtil.MINUTE)
						  + " phút nữa");
    }
  }

  public void dispose() {
    this.player = null;
  }
}

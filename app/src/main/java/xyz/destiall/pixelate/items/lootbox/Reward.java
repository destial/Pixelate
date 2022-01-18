package xyz.destiall.pixelate.items.lootbox;

import android.graphics.Bitmap;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.states.StateGame;

/**
 * Written By Yong Hong
 */
public class Reward {

    public enum RewardType {
        SKIN,
    }

    Bitmap rewardImage;
    final int rewardCount;
    final String rewardName;
    final RewardType rewardType;

    public Reward(Bitmap map, int rewardCount, RewardType rewardType, String rewardName)
    {
        this.rewardImage = map;
        this.rewardCount = rewardCount;
        this.rewardName = rewardName;
        this.rewardType = rewardType;
    }

    public Bitmap getRewardImage()
    {
        return rewardImage;
    }

    public String getRewardName()
    {
        return rewardName;
    }

    public int getRewardCount()
    {
        return rewardCount;
    }

    public void applyRewards() {
        if (rewardType == RewardType.SKIN) {
            EntityPlayer player = ((StateGame) Pixelate.getGSM().getState("Game")).getPlayer();
            switch (rewardName) {
                case "Brown Skin":
                    player.setSkin(ResourceManager.getBitmap(R.drawable.playerbrown));
                    break;
                case "Red Skin":
                    player.setSkin(ResourceManager.getBitmap(R.drawable.playerred));
                    break;
                case "White Skin":
                    player.setSkin(ResourceManager.getBitmap(R.drawable.playerwhite));
                    break;
            }
        }
    }
}

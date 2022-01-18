package xyz.destiall.pixelate.items.lootbox.crates;

import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.items.lootbox.Reward;
import xyz.destiall.pixelate.position.Vector2;

/**
 * Written By Yong Hong
 */
public class SkinCrate extends Crate {

    public SkinCrate(Vector2 screenOrigin) {
        super("Skin Crate", screenOrigin);
        crateImage = ResourceManager.getBitmap(R.drawable.crate1);
        crateImage = Imageable.resizeImage(crateImage, 0.2f);

        cost = 1;
        costType = CostType.DIAMOND;

        rewardsTable.addReward(new Reward(ResourceManager.getBitmap(R.drawable.cosmetic_playerbrownskin), 1, Reward.RewardType.SKIN, "Brown Skin"),0.5);
        rewardsTable.addReward(new Reward(ResourceManager.getBitmap(R.drawable.cosmetic_playerredskin), 1, Reward.RewardType.SKIN, "Red Skin"),0.12);
        rewardsTable.addReward(new Reward(ResourceManager.getBitmap(R.drawable.cosmetic_playerwhiteskin), 1, Reward.RewardType.SKIN, "White Skin"),0.04);
    }
}

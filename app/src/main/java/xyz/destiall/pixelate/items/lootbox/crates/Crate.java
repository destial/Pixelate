package xyz.destiall.pixelate.items.lootbox.crates;

import android.graphics.Bitmap;
import android.graphics.Color;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.items.lootbox.Reward;
import xyz.destiall.pixelate.items.lootbox.RewardTable;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.timer.Timer;

/**
 * Written By Yong Hong
 */
public class Crate implements Renderable, Updateable {
    public enum CostType {
        EXP,
        DIAMOND,
        PIXELPOINT,
    }
    protected final Reward[] rollingAnimation = new Reward[5];
    protected final Vector2 screenOrigin;
    protected final String crateName;

    protected RewardTable rewardsTable;
    protected Bitmap crateImage;
    protected boolean currentlyRolling;
    protected float animDuration;
    protected float animTimeLeft;
    protected float animSpeed;
    protected float animNextCD;
    protected float animDurationDrag;
    protected float animDurationDragTimeLeft;
    public int cost;
    public CostType costType;

    protected Crate(String crateName, Vector2 screenOrigin) {
        this.crateName = crateName;
        this.rewardsTable = new RewardTable();
        this.screenOrigin = screenOrigin;

        this.currentlyRolling = false;

        this.animDuration = 6.5f;
        this.animDurationDrag = 4.2f;
        this.animSpeed = 0.2f;

        this.animTimeLeft = 0.f;
        this.animDurationDragTimeLeft = 0.f;
        this.animNextCD = 0.f;

        cost = 1;
    }

    @Override
    public void render(Screen screen) {
        if (currentlyRolling) {
            double startX = Pixelate.WIDTH * -0.05;
            double startY = Pixelate.HEIGHT * 0.35;
            for (int i = 0; i < 5; ++i) {
                startX += Pixelate.WIDTH * 0.15;
                if (animTimeLeft <= 0) {
                    if (animDurationDragTimeLeft < animDurationDrag * 0.66) {
                        if (i != 2) continue;
                    } else
                    {
                        if (i == 0 || i == 4) continue;
                    }
                }
                Reward r = rollingAnimation[i];

                int border = Color.GRAY;
                double chance = rewardsTable.getDistributionOf(r);
                double weightage = chance / rewardsTable.getSumOfEntryChances();
                if (weightage <= 0.1)
                    border = Color.YELLOW;
                else if (weightage <= 0.2)
                    border = Color.BLUE;

                //Border
                screen.quad(startX, startY, Pixelate.WIDTH * 0.12, Pixelate.HEIGHT * 0.3, border);
                screen.quad(startX + Pixelate.WIDTH * 0.005, startY - Pixelate.HEIGHT * 0.01, Pixelate.WIDTH * 0.11, Pixelate.HEIGHT * 0.29, Color.argb(155, 80, 80, 80));
                screen.draw(r.getRewardImage(), startX + Pixelate.WIDTH * 0.01, startY);
                if(animTimeLeft <= 0)
                {
                    screen.text(r.getRewardName(), startX, startY + Pixelate.HEIGHT * 0.4, 60, Color.WHITE);
                }
            }
        } else {
            screen.draw(crateImage, Pixelate.WIDTH * 0.5 - crateImage.getWidth() / 2, Pixelate.HEIGHT * 0.5 - crateImage.getHeight() / 2);
            screen.text(crateName, Pixelate.WIDTH * 0.5 - (crateName.length()) * 15, Pixelate.HEIGHT * 0.68, 60, Color.WHITE);
        }
    }

    @Override
    public void update() {
        if (currentlyRolling) {
            double dt = Timer.getDeltaTime();
            animNextCD -= dt;
            if (animNextCD <= 0 && animTimeLeft > 0) {
                animNextCD = animSpeed;
                for (int i = 1; i < 5; ++i) {
                    rollingAnimation[i - 1] = rollingAnimation[i];
                }
                rollingAnimation[4] = rewardsTable.generateReward();
            }

            animTimeLeft -= dt;
            if (animTimeLeft <= 0) {
                animDurationDragTimeLeft -= dt;
                if (animDurationDragTimeLeft <= 0) {
                    //End here
                    currentlyRolling = false;
                    Reward finalReward = rollingAnimation[2];
                    finalReward.applyRewards();
                }
            }
        }
    }

    public Vector2 getScreenOrigin() {
        return screenOrigin;
    }

    public Bitmap getCrateImage() {
        return crateImage;
    }

    public String crateName() {
        return crateName;
    }

    public int getCost() {
        return cost;
    }

    public boolean openCrate(EntityPlayer player) {
        System.out.println("Reached1");
        if (rewardsTable.getRewards().size() < 1) return false;
        if (!currentlyRolling) {
            System.out.println("Reached2");
            currentlyRolling = true;
            animTimeLeft = animDuration;
            animDurationDragTimeLeft = animDurationDrag;
            this.animNextCD = this.animSpeed;
            for (int i = 0; i < 5; ++i) {
                System.out.println("Reached3");
                rollingAnimation[i] = rewardsTable.generateReward();
            }
            return true;
        }
        return false;
    }
}

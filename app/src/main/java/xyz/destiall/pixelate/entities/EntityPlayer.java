package xyz.destiall.pixelate.entities;

import android.graphics.Color;

import java.util.List;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.events.EventJoystick;
import xyz.destiall.pixelate.events.EventMining;
import xyz.destiall.pixelate.events.EventOpenInventory;
import xyz.destiall.pixelate.events.EventPlace;
import xyz.destiall.pixelate.events.EventPlayerMineAnimation;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.gui.HUD;
import xyz.destiall.pixelate.items.Inventory;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.timer.Timer;

public class EntityPlayer extends EntityLiving implements Listener {
    public EntityPlayer() {
        super(ResourceManager.getBitmap(R.drawable.player), 6, 3);
        location = new Location((int) (Pixelate.WIDTH * 0.5), (int) (Pixelate.HEIGHT * 0.5));
        spriteSheet.addSprite("LOOK RIGHT", createAnimation(0));
        spriteSheet.addSprite("LOOK LEFT", createAnimation(1));
        spriteSheet.addSprite("WALK RIGHT", createAnimation(2));
        spriteSheet.addSprite("WALK LEFT", createAnimation(3));
        spriteSheet.setCurrentSprite("LOOK RIGHT");
        scale = 0.5f;
        collision = new AABB(location.getX(), location.getY(), location.getX() + Tile.SIZE - 10, location.getY() + Tile.SIZE - 10);
        inventory = new Inventory(this, 27);
        HUD.INSTANCE.setHotbar(inventory);
        Pixelate.HANDLER.registerListener(this);
    }

    @Override
    public void updateSprite() {
        if (velocity.getX() > 0) facing = Direction.RIGHT;
        else if (velocity.getX() < 0) facing = Direction.LEFT;
        String anim = (velocity.isZero() ? "LOOK " : "WALK ") + facing.name();
        spriteSheet.setCurrentSprite(anim);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render(Screen screen) {
        super.render(screen);
        Vector2 vector = Screen.convert(location);
        vector.add(Tile.SIZE * 0.5 + target.getVector().getX() * Tile.SIZE, Tile.SIZE * 0.5 + target.getVector().getY() * Tile.SIZE);
        screen.circle(vector.getX(), vector.getY(), 5, Color.RED);
    }

    @EventHandler
    private void onJoystickEvent(EventJoystick e) {
        velocity.setX(e.getOffsetX());
        velocity.setY(e.getOffsetY());
        velocity.multiply((Tile.SIZE - 10) / 5f);
    }

    @EventHandler
    private void onMine(EventMining e) {

    }

    @EventHandler
    private void onAnimationMine(EventPlayerMineAnimation e)
    {
        if (location.getWorld() == null) return;
        List<Tile> currentTiles = location.getWorld().findTiles(collision);
        Location newLoc = location.clone().add(Tile.SIZE * 0.5 + target.getVector().getX() * Tile.SIZE, Tile.SIZE * 0.5 + target.getVector().getY() * Tile.SIZE);
        Tile tile = newLoc.getTile();
        if (tile == null) return;
        if (currentTiles.contains(tile)) return;
        if(tile.getTileType() != Tile.TileType.FOREGROUND) return;

        float bbProgress = tile.getBlockBreakProgress(); //Out of 100.0
        float bbDuration = tile.getMaterial().getRequiredMineDuration(getItemInHand().getType());
        float timeRelative = bbProgress / 100.0f * bbDuration;
        timeRelative += Timer.getDeltaTime();
        float bbProgressDiff = timeRelative / bbDuration * 100 - bbProgress;
        tile.addBlockBreakProgression(bbProgressDiff);

        if (tile.getBlockBreakProgress() >= 100) {
            ItemStack stack = location.getWorld().breakTile(newLoc);
            inventory.addItem(stack);
        }
    }

    @EventHandler
    private void onPlace(EventPlace e) {
        if (location.getWorld() == null) return;
        Location newLoc = location.clone().add(Tile.SIZE * 0.5 + target.getVector().getX() * Tile.SIZE, Tile.SIZE * 0.5 + target.getVector().getY() * Tile.SIZE);
        Tile tile = newLoc.getTile();
        if (tile != null && tile.getTileType() != Tile.TileType.FOREGROUND) {
            ItemStack current = inventory.getItem(HUD.INSTANCE.getHotbar().getCurrentSlot());
            if (current != null) {
                tile.setMaterial(current.getType());
                tile.addBlockBreakProgression(-100.f); //reset to 0
                current.setAmount(current.getAmount() - 1);
            }
        }
    }

    @EventHandler
    private void onOpenInventory(EventOpenInventory e) {
        HUD.INSTANCE.setInventory(inventory);
    }

    public ItemStack getItemInHand()
    {
        return inventory.getItem(HUD.INSTANCE.getHotbar().getCurrentSlot());
    }

}

package xyz.destiall.pixelate.entities;

import android.graphics.BitmapFactory;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.GameSurface;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.events.EventJoystick;
import xyz.destiall.pixelate.events.EventMining;
import xyz.destiall.pixelate.events.EventOpenInventory;
import xyz.destiall.pixelate.gui.HUD;
import xyz.destiall.pixelate.items.Inventory;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;

public class EntityPlayer extends EntityLiving implements Listener {
    public EntityPlayer(GameSurface gameSurface) {
        super(BitmapFactory.decodeResource(gameSurface.getResources(), R.drawable.player), 8, 3);
        location = new Location((int) (Game.WIDTH * 0.5), (int) (Game.HEIGHT * 0.5));
        spriteSheet.addSprite("LOOK RIGHT", createAnimation(0));
        spriteSheet.addSprite("LOOK LEFT", createAnimation(1));
        spriteSheet.addSprite("WALK RIGHT", createAnimation(2));
        spriteSheet.addSprite("WALK LEFT", createAnimation(3));
        spriteSheet.setCurrentSprite("LOOK RIGHT");
        scale = 0.5f;
        health = 20f;
        collision = new AABB(location.getX(), location.getY(), location.getX() + Tile.SIZE - 10, location.getY() + Tile.SIZE - 10);
        inventory = new Inventory(this, 27);
        HUD.INSTANCE.setHotbar(inventory);
        Game.HANDLER.registerListener(this);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @EventHandler
    private void onJoystickEvent(EventJoystick e) {
        velocity.setX(e.getOffsetX());
        velocity.setY(e.getOffsetY());
        velocity.multiply((Tile.SIZE - 10) / 5f);
    }

    @EventHandler
    private void onMine(EventMining e) {
        if (location.getWorld() == null) return;
        updateAABB();
        Location newLoc = location.clone().add(facing.getVector().multiply((Tile.SIZE - 10)));
        AABB front = new AABB(newLoc.getX(), newLoc.getY(), newLoc.getX() + Tile.SIZE - 10, newLoc.getY() + Tile.SIZE - 10);
        if (location.getWorld().isForegroundTile(front)) {
            newLoc.add((Tile.SIZE - 10) / 2f, (Tile.SIZE - 10) / 2f);
            Tile tile = location.getWorld().breakTile(newLoc);
        }
    }

    @EventHandler
    private void onOpenInventory(EventOpenInventory e) {
        HUD.INSTANCE.setInventory(inventory);
    }
}

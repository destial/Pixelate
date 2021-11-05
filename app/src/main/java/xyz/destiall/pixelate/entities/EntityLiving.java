package xyz.destiall.pixelate.entities;

import android.graphics.Bitmap;

import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.items.Inventory;
import xyz.destiall.pixelate.items.InventoryHolder;

public abstract class EntityLiving extends Entity implements InventoryHolder {
    protected float health;
    protected float speed;
    protected float armor;
    protected Inventory inventory;
    public EntityLiving(Bitmap image, int rows, int columns) {
        super(image, rows, columns);
    }

    public float getHealth() {
        return health;
    }

    public float getSpeed() {
        return speed;
    }

    public float getArmor() {
        return armor;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void damage(float damage) {
        if (health > 0) {
            health -= damage;
            if (health < 0) health = 0;
        }
    }

    @Override
    public void update() {
        super.update();
        if (!velocity.isZero()) {
            if (velocity.getX() > 0) {
                facing = Direction.RIGHT;
            } else if (velocity.getX() < 0) {
                facing = Direction.LEFT;
            }
        }
        location.add(velocity);
        constraint();
        updateAABB();
        if (location.getWorld().isForegroundTile(collision)) {
            location.subtract(velocity);
            location.add(velocity.getX(), 0);
            updateAABB();
            if (location.getWorld().isForegroundTile(collision)) {
                location.subtract(velocity.getX(), 0);
                location.add(0, velocity.getY());
            }
            location.add(0, velocity.getY());
            updateAABB();
            if (location.getWorld().isForegroundTile(collision)) {
                location.subtract(0, velocity.getY());
                location.add(velocity.getX(), 0);
            }
            updateAABB();
            if (location.getWorld().isForegroundTile(collision)) {
                location.subtract(velocity);
            }
        }
        String anim = (velocity.isZero() ? "LOOK " : "WALK ") + facing.name();
        spriteSheet.setCurrentSprite(anim);
    }

    protected void updateAABB() {
        Bitmap map = spriteSheet.getCurrentAnimation();
        if (scale != 1 && scale > 0) {
            map = Bitmap.createScaledBitmap(map, (int)(map.getWidth() * scale), (int)(map.getHeight() * scale), false);
        }
        collision.getMin().setX(location.getX());
        collision.getMin().setY(location.getY());
        collision.getMax().setX(location.getX() + map.getWidth());
        collision.getMax().setY(location.getY() + map.getHeight());
    }

    protected void constraint() {
        if (location.getX() < -Game.WIDTH) {
            location.set(-Game.WIDTH, location.getY());
        }
        if (location.getY() < -Game.HEIGHT) {
            location.set(location.getX(), -Game.HEIGHT);
        }
        if (location.getX() > Game.WIDTH) {
            location.set(Game.WIDTH, location.getY());
        }
        if (location.getY() > Game.HEIGHT) {
            location.set(location.getX(), Game.HEIGHT);
        }
    }
}

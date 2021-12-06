package xyz.destiall.pixelate.entities;

import android.graphics.Bitmap;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.items.Inventory;
import xyz.destiall.pixelate.items.InventoryHolder;
import xyz.destiall.pixelate.timer.Timer;

public abstract class EntityLiving extends Entity implements InventoryHolder {
    protected float health;
    protected float speed;
    protected float armor;
    protected float damageDelay;
    protected Inventory inventory;
    public EntityLiving(Bitmap image, int rows, int columns) {
        super(image, rows, columns);
        health = 20f;
        speed = 1f;
        armor = 0f;
        damageDelay = 0f;
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
        if (damageDelay != 0) return;
        damageDelay = (float) Timer.getDeltaTime();
        if (health > 0) {
            health -= damage;
            if (health < 0) health = 0;
        }
    }

    @Override
    public void update() {
        super.update();
        updateDirection();
        updateSprite();
        collide();
        if (damageDelay != 0f) {
            damageDelay += Timer.getDeltaTime();
            if (damageDelay >= 1.f) {
                damageDelay = 0.f;
            }
        }
    }

    public void updateSprite() {
        // Set animation sprite for entity based on velocity
        String anim = (velocity.isZero() ? "LOOK " : "WALK ") + facing.name();
        if (spriteSheet.hasAnimation(anim)) spriteSheet.setCurrentSprite(anim);
    }

    public void updateDirection() {
        // Set direction of entity based on velocity
        if (!velocity.isZero()) {
            if (velocity.getY() > 0 && Math.abs(velocity.getY()) > Math.abs(velocity.getX())) {
                facing = target = Direction.DOWN;
            } else if (velocity.getY() < 0 && Math.abs(velocity.getY()) > Math.abs(velocity.getX())) {
                facing = target = Direction.UP;
            } else if (velocity.getX() > 0) {
                facing = target = Direction.RIGHT;
            } else if (velocity.getX() < 0) {
                facing = target = Direction.LEFT;
            }
        }
    }

    public void collide() {
        // Move entity
        location.add(velocity);

        // Constraint with in world borders
        constraint();

        // Update collision model
        updateAABB();

        // Only update collision if entity is moving and is in a valid world
        if (!velocity.isZero() && location.getWorld() != null) {

            // If the tile stepping into is a collidable tile
            if (location.getWorld().isForegroundTile(collision)) {

                // Revert position
                location.subtract(velocity);

                // Check x direction
                location.add(velocity.getX(), 0);
                updateAABB();

                // If the tile in the x direction is also collidable
                if (location.getWorld().isForegroundTile(collision)) {

                    // Revert position
                    location.subtract(velocity.getX(), 0 );
                }

                // Check y direction
                location.add(0, velocity.getY());
                updateAABB();

                // If the tile in the y direction is also collidable
                if (location.getWorld().isForegroundTile(collision)) {

                    // Revert position
                    location.subtract(0, velocity.getY());
                }
            }
        }
    }

    public void updateAABB() {
        // Get the scale and size of the entity based on the animation image (lazy hack)
        Bitmap map = spriteSheet.getCurrentAnimation();
        if (scale == 0) scale = 1;

        // Set collision bounds based on image
        collision.setMin(location.getX(), location.getY());
        collision.setMax(location.getX() + (int)(map.getWidth() * scale), location.getY() + (int)(map.getHeight() * scale));
    }

    public void constraint() {
        if (location.getX() < -Pixelate.WIDTH) {
            location.set(-Pixelate.WIDTH, location.getY());
        }
        if (location.getY() < -Pixelate.HEIGHT) {
            location.set(location.getX(), -Pixelate.HEIGHT);
        }
        if (location.getX() > Pixelate.WIDTH) {
            location.set(Pixelate.WIDTH, location.getY());
        }
        if (location.getY() > Pixelate.HEIGHT) {
            location.set(location.getX(), Pixelate.HEIGHT);
        }
    }
}

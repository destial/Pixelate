package xyz.destiall.pixelate.entities;

import android.graphics.Bitmap;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.events.EventEntityDamage;
import xyz.destiall.pixelate.events.EventEntityDamageByEntity;
import xyz.destiall.pixelate.items.InventoryHolder;
import xyz.destiall.pixelate.items.inventory.EntityInventory;
import xyz.destiall.pixelate.items.inventory.Inventory;
import xyz.destiall.pixelate.timer.Timer;

/**
 * Written by Rance
 */
public abstract class EntityLiving extends Entity implements InventoryHolder {
    protected transient float damageDelay;

    protected Inventory inventory;
    protected float health;
    protected float maxHealth;
    protected float speed;
    protected float armor;

    public EntityLiving() {
        //super(image, rows, columns);
        health = maxHealth = 20f;
        speed = 1f;
        armor = 0f;
        damageDelay = 0f;
    }

    @Override
    public void refresh() {
        super.refresh();
        getInventory().setHolder(this);
    }

    /**
     * Set the max health of this entity
     * @param maxHealth The requested max health
     */
    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**
     * Get the max health of this entity
     * @return The max health
     */
    public float getMaxHealth() {
        return maxHealth;
    }

    /**
     * Get the current health of this entity
     * @return The current health
     */
    public float getHealth() {
        return health;
    }

    /**
     * Get the current speed of this entity
     * @return The current speed
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Get the current armor strength of this entity
     * @return The current armor strength
     */
    public float getArmor() {
        return armor;
    }

    @Override
    public EntityInventory getInventory() {
        return (EntityInventory) inventory;
    }

    /**
     * Damage this entity. Removes this entity from the world if health is <= 0
     * @param damage The damage to deal
     */
    public void damage(float damage) {
        if (damageDelay != 0) return;
        damageDelay = (float) Timer.getDeltaTime();
        if (health > 0) {
            EventEntityDamage e = new EventEntityDamage(this, damage);
            Pixelate.HANDLER.call(e);
            if (e.isCancelled()) return;
            health -= e.getDamage();
            if (health < 0) {
                health = 0;
            }
            if (health <= 0) {
                remove();
            }
        }
    }

    /**
     * Damage this entity. Removes this entity from the world if health is <= 0
     * @param damager The Entity that damaged this entity
     * @param damage The damage to deal
     */
    public void damage(Entity damager, float damage) {
        if (damageDelay != 0) return;
        damageDelay = (float) Timer.getDeltaTime();
        if (health > 0) {
            EventEntityDamage e = new EventEntityDamageByEntity(this, damager, damage);
            Pixelate.HANDLER.call(e);
            if (e.isCancelled()) return;
            health -= e.getDamage();
            if (health < 0) {
                health = 0;
            }
            if (health <= 0) {
                remove();
            }
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

    protected void updateSprite() {
        // Set animation sprite for entity based on velocity
        String anim = (velocity.isZero() ? "LOOK " : "WALK ") + facing.name();
        if (spriteSheet.hasAnimation(anim)) spriteSheet.setCurrentAnimation(anim);
    }

    protected void updateDirection() {
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

    protected void collide() {
        // Move entity
        location.add(velocity);

        // Constraint with in world borders
        constraint();

        // Update collision model
        updateAABB();

        // Only update collision if entity is moving and is in a valid world
        World w;
        if (!velocity.isZero() && (w = location.getWorld()) != null) {

            // If the tile stepping into is a collidable tile
            if (w.isForegroundTile(collision)) {

                // Revert position
                location.subtract(velocity);

                // Check x direction
                location.add(velocity.getX(), 0);
                updateAABB();

                // If the tile in the x direction is also collidable
                if (w.isForegroundTile(collision)) {

                    // Revert position
                    location.subtract(velocity.getX(), 0);
                }

                // Check y direction
                location.add(0, velocity.getY());
                updateAABB();

                // If the tile in the y direction is also collidable
                if (w.isForegroundTile(collision)) {

                    // Revert position
                    location.subtract(0, velocity.getY());
                    updateAABB();
                }
            }
        }
    }

    protected void updateAABB() {
        // Get the scale and size of the entity based on the animation image (lazy hack)
        Bitmap map = spriteSheet.getCurrentSprite();
        if (scale == 0) scale = 1;

        // Set collision bounds based on image
        collision.setMin(location.getX(), location.getY());
        collision.setMax(location.getX() + (int)(map.getWidth() * scale), location.getY() + (int)(map.getHeight() * scale));
    }

    protected void constraint() {
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

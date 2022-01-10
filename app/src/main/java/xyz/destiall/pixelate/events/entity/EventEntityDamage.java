package xyz.destiall.pixelate.events.entity;

import xyz.destiall.java.events.Cancellable;
import xyz.destiall.java.events.Event;
import xyz.destiall.pixelate.entities.EntityLiving;

/**
 * Called when an EntityLiving gets damaged
 */
public class EventEntityDamage extends Event implements Cancellable {
    private final EntityLiving damaged;
    private float damage;
    private boolean cancelled = false;

    public EventEntityDamage(EntityLiving damaged, float damage) {
        this.damaged = damaged;
        this.damage = damage;
    }

    /**
     * Get the damage dealt
     * @return The current damage
     */
    public float getDamage() {
        return damage;
    }

    /**
     * Set the damage dealt
     * @param damage The damage to deal
     */
    public void setDamage(float damage) {
        this.damage = damage;
    }

    /**
     * Get the EntityLiving that was damaged
     * @return The entity
     */
    public EntityLiving getDamaged() {
        return damaged;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}

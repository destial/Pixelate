package xyz.destiall.pixelate.events.entity;

import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.entities.EntityLiving;

/**
 * Called when an EntityLiving gets damaged by another Entity
 */
public class EventEntityDamageByEntity extends EventEntityDamage {
    private final Entity damager;

    public EventEntityDamageByEntity(EntityLiving damaged, Entity damager, float damage) {
        super(damaged, damage);
        this.damager = damager;
    }

    /**
     * Get the Entity that damaged this entity
     * @return The damager
     */
    public Entity getDamager() {
        return damager;
    }
}

package xyz.destiall.pixelate.events;

import xyz.destiall.java.events.Cancellable;
import xyz.destiall.java.events.Event;
import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.environment.World;

/**
 * Called when an entity is about to spawn in the world
 */
public class EventSpawnEntity extends Event implements Cancellable {
    private final Entity entity;
    private final World world;
    private boolean cancelled = false;
    public EventSpawnEntity(Entity entity, World world) {
        this.entity = entity;
        this.world = world;
    }

    /**
     * Get the world that this entity is spawning in
     * @return The world
     */
    public World getWorld() {
        return world;
    }

    /**
     * Get the entity that spawned
     * @return The entity
     */
    public Entity getEntity() {
        return entity;
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

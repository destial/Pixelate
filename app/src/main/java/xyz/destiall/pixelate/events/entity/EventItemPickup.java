package xyz.destiall.pixelate.events.entity;

import xyz.destiall.utility.java.events.Cancellable;
import xyz.destiall.utility.java.events.Event;
import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.entities.EntityItem;

/**
 * Called when an item is picked up by the player
 */
public class EventItemPickup extends Event implements Cancellable {
    private final EntityItem item;
    private final Entity entity;
    private boolean cancelled = false;

    public EventItemPickup(Entity entity, EntityItem item) {
        this.entity = entity;
        this.item = item;
    }

    /**
     * Get the item that was picked up
     * @return The item
     */
    public EntityItem getItem() {
        return item;
    }

    /**
     * Get the entity that picked this item up
     * @return The picker
     */
    public Entity getPicker() {
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

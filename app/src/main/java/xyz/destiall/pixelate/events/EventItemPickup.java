package xyz.destiall.pixelate.events;

import xyz.destiall.java.events.Cancellable;
import xyz.destiall.java.events.Event;
import xyz.destiall.pixelate.entities.EntityItem;

/**
 * Called when an item is picked up by the player
 */
public class EventItemPickup extends Event implements Cancellable {
    private final EntityItem item;
    private boolean cancelled = false;
    public EventItemPickup(EntityItem item) {
        this.item = item;
    }

    /**
     * Get the item that was picked up
     * @return The item
     */
    public EntityItem getItem() {
        return item;
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

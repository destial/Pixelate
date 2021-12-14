package xyz.destiall.pixelate.events;

import xyz.destiall.java.events.Cancellable;
import xyz.destiall.java.events.Event;
import xyz.destiall.pixelate.entities.EntityItem;

public class EventItemPickup extends Event implements Cancellable {
    private final EntityItem item;
    private boolean cancelled = false;
    public EventItemPickup(EntityItem item) {
        this.item = item;
    }

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

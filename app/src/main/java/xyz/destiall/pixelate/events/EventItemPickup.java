package xyz.destiall.pixelate.events;

import xyz.destiall.java.events.Cancellable;
import xyz.destiall.java.events.Event;
import xyz.destiall.pixelate.entities.ItemDrop;

public class EventItemPickup extends Event implements Cancellable {
    private final ItemDrop item;
    private boolean cancelled = false;
    public EventItemPickup(ItemDrop item) {
        this.item = item;
    }

    public ItemDrop getItem() {
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

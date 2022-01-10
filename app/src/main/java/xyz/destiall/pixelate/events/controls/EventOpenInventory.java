package xyz.destiall.pixelate.events.controls;

import xyz.destiall.java.events.Cancellable;
import xyz.destiall.java.events.Event;

/**
 * Called when the player opens their inventory
 */
public class EventOpenInventory extends Event implements Cancellable {
    private boolean cancelled = false;

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}

package xyz.destiall.pixelate.events.tile;

import xyz.destiall.utility.java.events.Cancellable;
import xyz.destiall.utility.java.events.Event;
import xyz.destiall.pixelate.position.Location;

/**
 * Called when a TNT is ignited
 */
public class EventIgniteTNT extends Event implements Cancellable {
    private final Location location;
    private boolean cancelled = false;

    public EventIgniteTNT(Location location) {
        this.location = location;
    }

    /**
     * Get the location of this TNT
     * @return A mutable location
     */
    public Location getLocation() {
        return location;
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

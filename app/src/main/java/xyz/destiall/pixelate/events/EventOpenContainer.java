package xyz.destiall.pixelate.events;

import xyz.destiall.java.events.Cancellable;
import xyz.destiall.java.events.Event;
import xyz.destiall.pixelate.environment.tiles.containers.ContainerTile;

/**
 * Called when the player opens a container tile
 */
public class EventOpenContainer extends Event implements Cancellable {
    private final ContainerTile tile;
    private boolean cancelled = false;

    public EventOpenContainer(ContainerTile tile) {
        this.tile = tile;
    }

    /**
     * Get the container tile that was opened
     * @return The container
     */
    public ContainerTile getContainer() {
        return this.tile;
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

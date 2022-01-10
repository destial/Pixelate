package xyz.destiall.pixelate.events.tile;

import xyz.destiall.java.events.Cancellable;
import xyz.destiall.java.events.Event;
import xyz.destiall.pixelate.environment.tiles.containers.ContainerTile;
import xyz.destiall.pixelate.items.inventory.Inventory;

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
        return tile;
    }

    /**
     * Get the container inventory that was opened
     * @return The container inventory
     */
    public Inventory getInventory() {
        return getContainer().getInventory();
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}

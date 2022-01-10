package xyz.destiall.pixelate.events.tile;

import xyz.destiall.java.events.Cancellable;
import xyz.destiall.java.events.Event;
import xyz.destiall.pixelate.environment.tiles.Tile;

/**
 * Called when a tile is broken
 */
public class EventTileBreak extends Event implements Cancellable {
    private final Tile tile;
    private boolean cancelled = false;

    public EventTileBreak(Tile tile) {
        this.tile = tile;
    }

    /**
     * Get the tile that was broken
     * @return The broken tile
     */
    public Tile getTile() {
        return tile;
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

package xyz.destiall.pixelate.events.tile;

import xyz.destiall.utility.java.events.Cancellable;
import xyz.destiall.utility.java.events.Event;
import xyz.destiall.pixelate.environment.tiles.Tile;

/**
 * Called when a tile is about to be replaced with another tile
 */
public class EventTileReplace extends Event implements Cancellable {
    private final Tile oldTile;
    private final Tile newTile;
    private boolean cancelled = false;

    public EventTileReplace(Tile oldTile, Tile newTile) {
        this.oldTile = oldTile;
        this.newTile = newTile;
    }

    /**
     * Get the tile to be replaced
     * @return The tile before replaced
     */
    public Tile getOldTile() {
        return oldTile;
    }

    /**
     * Get the tile that it is being replaced with
     * @return The replacing material
     */
    public Tile getNewTile() {
        return newTile;
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

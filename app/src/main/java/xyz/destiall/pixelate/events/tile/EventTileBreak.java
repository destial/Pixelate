package xyz.destiall.pixelate.events.tile;

import java.util.List;

import xyz.destiall.utility.java.events.Cancellable;
import xyz.destiall.utility.java.events.Event;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.items.ItemStack;

/**
 * Called when a tile is broken
 */
public class EventTileBreak extends Event implements Cancellable {
    private final Tile tile;
    private final List<ItemStack> drops;
    private boolean cancelled = false;

    public EventTileBreak(Tile tile, List<ItemStack> drops) {
        this.drops = drops;
        this.tile = tile;
    }

    /**
     * Get the drops of this tile
     * @return The drops of this tile
     */
    public List<ItemStack> getDrops() {
        return drops;
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

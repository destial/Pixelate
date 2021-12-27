package xyz.destiall.pixelate.events;

import xyz.destiall.java.events.Cancellable;
import xyz.destiall.java.events.Event;
import xyz.destiall.pixelate.entities.EntityLiving;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.tiles.Tile;

/**
 * Called when a tile is placed
 */
public class EventTilePlace extends Event implements Cancellable {
    private final Tile tile;
    private final Material material;
    private final EntityLiving entity;
    private boolean cancelled = false;

    public EventTilePlace(EntityLiving entity, Tile tile, Material material) {
        this.entity = entity;
        this.tile = tile;
        this.material = material;
    }

    /**
     * Get the entity that will place this tile
     * @return The entity
     */
    public EntityLiving getEntity() {
        return entity;
    }

    /**
     * Get the tile to be placed
     * @return The tile before placed
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Get the material that it is being replaced with
     * @return The replacing material
     */
    public Material getReplaced() {
        return material;
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

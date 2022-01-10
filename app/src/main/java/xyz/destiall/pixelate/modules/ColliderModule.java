package xyz.destiall.pixelate.modules;

import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.modular.Component;
import xyz.destiall.pixelate.position.AABB;

/**
 * Written by Rance
 */
public class ColliderModule implements Component<Entity> {
    private final AABB bounds;
    private transient Entity entity;

    public ColliderModule(Entity entity) {
        bounds = new AABB(0, 0, Tile.SIZE, Tile.SIZE);
        this.entity = entity;
    }

    /**
     * Get the bounds of this collider
     * @return The bounds
     */
    public AABB getBounds() {
        return bounds;
    }

    /**
     * Set the bounds of this collider
     * @param x The top left x
     * @param y The top left y
     * @param width The width
     * @param height The height
     */
    public void setBounds(float x, float y, float width, float height) {
        bounds.setMin(x, y);
        bounds.setMax(x + width, y + height);
    }

    /**
     * Check with another ColliderModule if both of them have collided
     * @param other The other ColliderModule
     * @return true if collided, otherwise false
     */
    public boolean hasCollided(ColliderModule other) {
        return bounds.isOverlap(other.bounds);
    }

    @Override
    public void update() {}

    @Override
    public void destroy() {}

    @Override
    public Entity getParent() {
        return entity;
    }

    @Override
    public void setParent(Entity entity) {
        this.entity = entity;
    }
}

package xyz.destiall.pixelate.modules;

import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.modular.Module;
import xyz.destiall.pixelate.position.AABB;

public class ColliderModule implements Module {
    private final AABB bounds;
    public ColliderModule() {
        bounds = new AABB(0, 0, Tile.SIZE, Tile.SIZE);
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
}

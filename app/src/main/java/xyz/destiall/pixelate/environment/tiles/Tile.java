package xyz.destiall.pixelate.environment.tiles;

import android.graphics.Bitmap;

import java.util.Objects;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.position.Vector2;

public class Tile extends Imageable implements Updateable,Renderable {
    public static final long SIZE = Pixelate.getTileMap().getWidth() / Material.getColumns();

    protected Vector2 location;
    protected Material material;
    protected World world;
    protected TileType tileType;

    protected float brokenProgression;

    public Tile(int x, int y, Material material, World world, TileType type) {
        super(Pixelate.getTileMap(), Material.getRows(), Material.getColumns());
        this.material = material;
        this.world = world;
        location = new Vector2(x, y);
        tileType = type;
        if (!material.isBlock()) {
            this.material = Material.STONE;
        }
        image = createSubImageAt(this.material.getRow(), this.material.getColumn());
        brokenProgression = 0.f;
    }

    /**
     * Get the location of this tile
     * @return An immutable location
     */
    public Vector2 getLocation() {
        return location.clone();
    }

    /**
     * Get the tile type of this tile
     * @return The tile type
     */
    public TileType getTileType() {
        return tileType;
    }

    /**
     * Get the material of this tile
     * @return The material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Set the material of this tile.
     * Will replace the tile if its of different category
     * @param mat The material to set
     * @return The tile after being set
     */
    public Tile setMaterial(Material mat) {
        if (mat != material && mat.isContainer()) {
            Tile t = TileFactory.createTile(mat, (int) location.getX(), (int) location.getY(), world);
            world.replaceTile(this, TileFactory.createTile(mat, (int) location.getX(), (int) location.getY(), world));
            return t;
        }
        material = mat;
        image = Bitmap.createBitmap(Pixelate.getTileMap(), material.getColumn() * (int) width, material.getRow() * (int) height, (int) Tile.SIZE, (int) Tile.SIZE);
        tileType = mat.getTileType();
        brokenProgression = 0;
        return this;
    }

    @Override
    public void update() {}

    public enum TileType {
        BACKGROUND,
        FOREGROUND,
        UNKNOWN
    }

    /**
     * Get the block break progression of this tile
     * @return The progression (between 0 and 100)
     */
    public float getBlockBreakProgress() {
        return this.brokenProgression;
    }

    /**
     * Add a block break progression to this tile
     * @param progression The progress to add
     */
    public void addBlockBreakProgression(float progression) {
        brokenProgression += progression;
        if (brokenProgression < 0) brokenProgression = 0;
        if (brokenProgression > 100) brokenProgression = 100;
    }

    /**
     * Check if this tile is ready to be broken
     * @return true if ready, otherwise false
     */
    public boolean readyToBreak() {
        return brokenProgression >= 100;
    }

    @Override
    public void render(Screen screen) {
        Vector2 offset = Screen.convert(location);
        if (offset.getX() + Tile.SIZE < 0 || offset.getX() > Pixelate.WIDTH || offset.getY() + Tile.SIZE < 0 || offset.getY() > Pixelate.HEIGHT) return;
        screen.draw(image, offset.getX(), offset.getY());
        if (brokenProgression < 100 && brokenProgression > 0)
            screen.draw(TileBreakProgression.getInstance().getTileBreakProgression((int) Math.floor(brokenProgression * 0.1)), offset.getX(), offset.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return location.equals(tile.getLocation()) && material == tile.material && Objects.equals(world, tile.world) && tileType == tile.tileType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, material, world, tileType);
    }
}

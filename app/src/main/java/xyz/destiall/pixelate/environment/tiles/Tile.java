package xyz.destiall.pixelate.environment.tiles;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;

public class Tile implements Updateable, Renderable {
    public static final long SIZE = Pixelate.getTileMap().getWidth() / Material.getColumns();
    protected transient Bitmap image;

    protected Location location;
    protected Material material;
    protected TileType tileType;
    protected float brokenProgression;

    @SerializedName("type")
    private final String typeName;

    protected Tile() {
        typeName = getClass().getName();
    }

    public Tile(int x, int y, Material material, World world, TileType type) {
        typeName = getClass().getName();
        location = new Location(x, y, world);
        location.setWorld(world);
        tileType = type;
        this.material = material;
        if (!material.isBlock()) {
            this.material = Material.STONE;
        }
        image = material.getImage();
        brokenProgression = 0.f;
    }

    /**
     * Set the world of this tile
     * @param world The world
     */
    public void setWorld(World world) {
        location.setWorld(world);
    }

    /**
     * Get the location vector of this tile
     * @return An immutable vector
     */
    public Vector2 getVector() {
        return location.toVector().clone();
    }

    /**
     * Get the location of this tile
     * @return An immutable location
     */
    public Location getLocation() {
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
        if (!mat.isBlock()) return this;
        if (mat != material && mat.isContainer()) {
            World world = location.getWorld();
            if (world == null) return this;
            Tile t = TileFactory.createTile(mat, location.getX(), location.getY(), world);
            world.replaceTile(this, t);
            material = mat;
            tileType = mat.getTileType();
            brokenProgression = 0;
            return t;
        }
        material = mat;
        image = mat.getImage();
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
        if (image == null) image = material.getImage();
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
        return location.equals(tile.getLocation()) && material == tile.material && tileType == tile.tileType;
    }
}

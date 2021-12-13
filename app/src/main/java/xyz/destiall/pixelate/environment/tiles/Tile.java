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

    public Vector2 getLocation() {
        return location;
    }

    public TileType getTileType() {
        return tileType;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material mat) {
        if (mat != material && mat.isContainer()) {
            world.replaceTile(this, TileFactory.createTile(mat, (int) location.getX(), (int) location.getY(), world));
            return;
        }
        material = mat;
        image = Bitmap.createBitmap(Pixelate.getTileMap(), material.getColumn() * (int) width, material.getRow() * (int) height, (int) Tile.SIZE, (int) Tile.SIZE);
        tileType = mat.getTileType();
        brokenProgression = 0;
    }

    @Override
    public void update() {

    }

    public enum TileType {
        BACKGROUND,
        FOREGROUND,
        UNKNOWN
    }

    public float getBlockBreakProgress() {
        return this.brokenProgression;
    }

    public void addBlockBreakProgression(float progression) {
        brokenProgression += progression;
        if (brokenProgression < 0) brokenProgression = 0;
        if (brokenProgression > 100) brokenProgression = 100;
    }

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

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
        material = mat;
        image = Bitmap.createBitmap(Pixelate.getTileMap(), material.getColumn() * width, material.getRow() * height, width, height);
        tileType = mat.getTileType();
    }

    @Override
    public void update() {

    }

    public enum TileType {
        BACKGROUND,
        FOREGROUND,
        UNKNOWN
    }

    public float getBlockBreakProgress()
    {
        return this.brokenProgression;
    }

    public void addBlockBreakProgression(float progression)
    {
        this.brokenProgression += progression;
        if(this.brokenProgression < 0) this.brokenProgression = 0;
        if(this.brokenProgression > 100) this.brokenProgression = 100;
    }

    public boolean readyToBreak()
    {
        return (this.brokenProgression >= 100);
    }

    @Override
    public void render(Screen screen) {
        Vector2 offset = screen.convert(location);
        if (offset.getX() + Tile.SIZE < 0 || offset.getX() > Pixelate.WIDTH || offset.getY() + Tile.SIZE < 0 || offset.getY() > Pixelate.HEIGHT) return;
        screen.draw(image, offset.getX(), offset.getY());
        if(this.brokenProgression < 100 && this.brokenProgression > 0)
            screen.draw(TileBreakProgression.getInstance().getTileBreakProgression((int)Math.floor(this.brokenProgression * 0.1)), offset.getX(), offset.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return Objects.equals(location, tile.location) && material == tile.material && Objects.equals(world, tile.world) && tileType == tile.tileType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, material, world, tileType);
    }
}

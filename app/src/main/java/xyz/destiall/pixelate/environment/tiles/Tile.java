package xyz.destiall.pixelate.environment.tiles;

import android.graphics.Bitmap;

import java.util.Objects;

import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.position.Vector2;

public class Tile extends Imageable implements Renderable {
    public static final long SIZE = Game.getTileMap().getWidth() / Material.getColumns();

    protected Vector2 location;
    protected Material material;
    protected World world;
    protected TILE_TYPE tileType;

    public Tile(int x, int y, Material material, World world, TILE_TYPE type) {
        super(Game.getTileMap(), Material.getRows(), Material.getColumns());
        this.material = material;
        this.world = world;
        location = new Vector2(x, y);
        tileType = type;
        if (!material.isBlock()) {
            this.material = Material.STONE;
        }
        image = createSubImageAt(this.material.getRow(), this.material.getColumn());
    }

    public Vector2 getLocation() {
        return location;
    }

    public TILE_TYPE getTileType() {
        return tileType;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material mat) {
        material = mat;
        image = Bitmap.createBitmap(Game.getTileMap(), material.getColumn() * width, material.getRow() * height, width, height);
        this.tileType = mat.getTileType();
    }

    public enum TILE_TYPE {
        BACKGROUND,
        FOREGROUND,
        UNKNOWN
    }

    @Override
    public void render(Screen screen) {
        Vector2 offset = screen.convert(location);
        if (offset.getX() + Tile.SIZE < 0 || offset.getX() > Game.WIDTH || offset.getY() + Tile.SIZE < 0 || offset.getY() > Game.HEIGHT) return;
        screen.getCanvas().drawBitmap(image, (int) offset.getX(), (int) offset.getY(), null);
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

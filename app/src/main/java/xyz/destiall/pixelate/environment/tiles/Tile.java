package xyz.destiall.pixelate.environment.tiles;

import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.position.Vector2;

public abstract class Tile extends Imageable implements Renderable, Comparable<Tile> {
    protected final int id;
    protected Vector2 location;
    protected Material material;
    protected World world;
    protected Type tileType;
    public static final int SIZE = 100;
    public Tile(int id, int x, int y, Material material, World world, Type type) {
        super(Game.getTileMap(), 1, 2);
        this.material = material;
        this.id = id;
        this.world = world;
        location = new Vector2(x, y);
        tileType = type;
        if (!material.isBlock()) {
            this.material = Material.STONE;
        }
    }

    public int getId() {
        return id;
    }

    public Vector2 getLocation() {
        return location;
    }

    public Type getTileType() {
        return tileType;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public int compareTo(Tile o) {
        return Integer.compare(id, o.id);
    }

    public enum Type {
        BACKGROUND,
        FOREGROUND
    }

    @Override
    public void render(Screen screen) {
        //if (screen.isOutOfBounds(location)) return;
        Vector2 offset = screen.convert(location);
        screen.getCanvas().drawBitmap(createSubImageAt(material.getRow(), material.getColumn()), (int) offset.getX(), (int) offset.getY(), null);
    }


}

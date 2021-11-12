package xyz.destiall.pixelate.environment.tiles;

import android.graphics.Bitmap;

import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.position.Vector2;

public class Tile extends Imageable implements Renderable {
    //protected final int id; no longer needed
    protected Vector2 location;
    protected Material material;
    protected World world;
    protected TILE_TYPE tileType;
    public static final long SIZE = Game.getTileMap().getWidth() / Material.getColumns() + 1;
    public Tile(int x, int y, Material material, World world, TILE_TYPE type) {
        super(Game.getTileMap(), Material.getRows() + 1, Material.getColumns() + 1);
        this.material = material;
        //this.id = id;
        this.world = world;
        location = new Vector2(x, y);
        tileType = type;
        if (!material.isBlock()) {
            this.material = Material.STONE;
        }
        image = createSubImageAt(material.getRow(), material.getColumn());
    }

    /*public int getId() {
        return id;
    }*/

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
        this.material = mat;
        image = Bitmap.createBitmap(Game.getTileMap(), material.getColumn() * width, material.getRow() * height, width, height);
        this.tileType = TileFactory.materialTileType.getOrDefault(mat,TILE_TYPE.BACKGROUND);
    }



    public enum TILE_TYPE {
        BACKGROUND,
        FOREGROUND
    }

    @Override
    public void render(Screen screen) {
        Vector2 offset = screen.convert(location);
        if (offset.getX() + Tile.SIZE < 0 || offset.getX() > Game.WIDTH || offset.getY() + Tile.SIZE < 0 || offset.getY() > Game.HEIGHT) return;
        screen.getCanvas().drawBitmap(image, (int) offset.getX(), (int) offset.getY(), null);
    }
}

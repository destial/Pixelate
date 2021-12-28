package xyz.destiall.pixelate.environment.tiles;

import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.tiles.containers.ChestTile;
import xyz.destiall.pixelate.environment.tiles.containers.FurnanceTile;

public class TileFactory {

    /**
     * Create a tile based on the given Material
     * @param mat The material
     * @param x The top left x
     * @param y The top left y
     * @param world The world
     * @return The newly created tile
     */
    public static Tile createTile(Material mat, int x, int y, World world) {
        Tile t;
        if (mat == Material.FURNACE) {
            t = new FurnanceTile(x, y, world);
            t.setWorld(world);
            return t;
        } else if (mat == Material.CHEST) {
            t = new ChestTile(x, y, world);
            t.setWorld(world);
            return t;
        }
        t = new Tile(x, y, mat, world, mat.getTileType());
        t.setWorld(world);
        return t;
    }
}

package xyz.destiall.pixelate.environment.tiles;

import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.tiles.containers.ChestTile;
import xyz.destiall.pixelate.environment.tiles.containers.FurnanceTile;

public class TileFactory {
    public static Tile createTile(Material mat, int x, int y, World world) {
        if (mat == Material.FURNACE) {
            return new FurnanceTile(x, y, world);
        } else if (mat == Material.CHEST) {
            return new ChestTile(x, y, world);
        }
        return new Tile(x, y, mat, world, mat.getTileType());
    }
}

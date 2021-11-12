package xyz.destiall.pixelate.environment.tiles;

import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;

public class TileFactory {

    public static Tile createTile(Material mat, int x, int y, World world) {
        return new Tile(x, y, mat, world, mat.getTileType());
    }
}

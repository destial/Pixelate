package xyz.destiall.pixelate.environment.tiles;

import java.util.HashMap;

import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;

public class TileFactory {

    public static HashMap<Material, Tile.TILE_TYPE> materialTileType = new HashMap<Material, Tile.TILE_TYPE>();
    static {
        materialTileType.put(Material.GRASS, Tile.TILE_TYPE.BACKGROUND);
        materialTileType.put(Material.STONE, Tile.TILE_TYPE.BACKGROUND);
        materialTileType.put(Material.PLANKS, Tile.TILE_TYPE.FOREGROUND);
        materialTileType.put(Material.WOOD, Tile.TILE_TYPE.FOREGROUND);
        materialTileType.put(Material.COAL_ORE, Tile.TILE_TYPE.FOREGROUND);
    }

    public static Tile createTile(Material mat, int x, int y, World world)
    {
        return new Tile(x, y, mat, world, materialTileType.getOrDefault(mat, Tile.TILE_TYPE.BACKGROUND));
    }
}

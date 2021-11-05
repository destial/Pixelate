package xyz.destiall.pixelate.environment.tiles;

import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;

public class TileCoalOre extends Tile {
    public TileCoalOre(int id, int x, int y, World world) {
        super(id, x, y, Material.COAL_ORE, world, Type.FOREGROUND);
    }
}
package xyz.destiall.pixelate.environment.tiles;

import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;

public class TileWood extends Tile {
    public TileWood(int id, int x, int y, World world) {
        super(id, x, y, Material.WOOD, world, Type.FOREGROUND);
    }
}

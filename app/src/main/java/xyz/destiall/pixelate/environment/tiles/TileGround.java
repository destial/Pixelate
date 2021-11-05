package xyz.destiall.pixelate.environment.tiles;

import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;

public class TileGround extends Tile {
    public TileGround(int id, int x, int y, World world) {
        super(id, x, y, Material.STONE, world, Type.BACKGROUND);
    }
}

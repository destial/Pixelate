package xyz.destiall.pixelate.environment.tiles;

import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;

public class TileGrass extends Tile {
    public TileGrass(int id, int x, int y, World world) {
        super(id, x, y, Material.GRASS, world, Type.FOREGROUND);
    }
}
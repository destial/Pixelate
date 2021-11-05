package xyz.destiall.pixelate.environment.generator;

import java.util.Collection;

import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.tiles.Tile;

public interface Generator {
    void generate(World world, Collection<Tile> tiles);
    void generate(int seed, World world, Collection<Tile> tiles);
}

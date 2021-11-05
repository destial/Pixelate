package xyz.destiall.pixelate.environment.generator;

import java.util.Collection;

import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.noise.PerlinNoise;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.environment.tiles.TileGround;
import xyz.destiall.pixelate.environment.tiles.TileWood;
import xyz.destiall.pixelate.position.Location;

public class GeneratorBasic implements Generator {
    @Override
    public void generate(World world, Collection<Tile> tiles) {
        generate(0, world, tiles);
    }

    private float generationScale = 0.05f;

    @Override
    public void generate(int seed, World world, Collection<Tile> tiles) {
        int i = 0;
        double divisor = 1.0/Tile.SIZE;

        double min = 1, max = 0;
        for (int x = -Game.WIDTH; x <= Game.WIDTH; x+=Tile.SIZE) {
            for (int y = -Game.HEIGHT; y <= Game.HEIGHT; y+=Tile.SIZE) {
                Tile tile;

                double noiseValue = PerlinNoise.noise(x*divisor*generationScale,y*divisor*generationScale);

                if(noiseValue < min) min = noiseValue;
                if(noiseValue > max) max = noiseValue;

                if (noiseValue > 0.4) {
                    tile = new TileWood(i++, (int) x, (int) y, world);
                } else {
                    tile = new TileGround(i++, (int) x, (int) y, world);
                }
                if (Math.random() > 0.98) world.spawnEntity(new Location(x, y, world), Entity.Type.ZOMBIE);
                tiles.add(tile);
            }
        }

        //System.out.println("Noise minmax: " + min + " , " + max);
    }
}

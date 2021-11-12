package xyz.destiall.pixelate.environment.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.noise.PerlinNoise;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.environment.tiles.TileFactory;
import xyz.destiall.pixelate.position.Location;

public class GeneratorBasic implements Generator {
    @Override
    public void generate(World world, Collection<Tile> tiles) {
        generate(0, world, tiles);
    }

    private float generationScale = 0.05f;
    private float oreGenerationScale = 0.3f;

    @Override
    public void generate(int seed, World world, Collection<Tile> tiles) {
        int i = 0;
        double divisor = 1.0/Tile.SIZE;

        double min = 1, max = 0;
        List<Location> orePopulationZones = new ArrayList<Location>();

        //Main terrain generation
        for (int x = -Game.WIDTH; x <= Game.WIDTH; x+=Tile.SIZE) {
            for (int y = -Game.HEIGHT; y <= Game.HEIGHT; y+=Tile.SIZE) {
                Tile tile = null;

                double noiseValue = PerlinNoise.noise(x*divisor*generationScale,y*divisor*generationScale);

                if(noiseValue < min) min = noiseValue;
                if(noiseValue > max) max = noiseValue;

                if (noiseValue > 0.59) {
                    if(noiseValue > 0.7 && noiseValue < 0.8)
                    {
                        //Ore Spawning Algorithm
                        if(Math.random() < 0.1) //Chance to populate area with ores
                        {
                            orePopulationZones.add(new Location(x*Tile.SIZE, y*Tile.SIZE, world));
                        }
                        else
                        {
                            tile = TileFactory.createTile(Material.WOOD, x,y,world);
                        }

                    }


                    else
                        tile = TileFactory.createTile(Material.WOOD, x,y,world);

                } else if (noiseValue < 0.35) {
                    tile = TileFactory.createTile(Material.GRASS, x,y,world);
                }

                if(tile == null)
                    tile = TileFactory.createTile(Material.STONE, x,y,world);
                if (Math.random() > 0.98) world.spawnEntity(new Location(x, y, world), Entity.Type.ZOMBIE);
                tiles.add(tile);
            }
        }

        //Populative ore generation
        Material oreCanvas = Material.WOOD;
        for(Location loc : orePopulationZones)
        {
            //Max Radius 3x3
            for(int x = loc.getX()-(int)Tile.SIZE*2; x<loc.getX()+(int)Tile.SIZE; ++x)
            {
                for(int y = loc.getY()-(int)Tile.SIZE*2; y<loc.getY()+(int)Tile.SIZE; ++y)
                {
                    double noiseValue = PerlinNoise.noise(x*divisor*generationScale,y*divisor*generationScale);
                    if(noiseValue > 0.4 && noiseValue < 0.55)
                    {
                        Tile tile = world.findTile(new Location(x,y,world));

                    }
                }
            }
        }


        System.out.println("Noise minmax: " + min + " , " + max);
    }
}

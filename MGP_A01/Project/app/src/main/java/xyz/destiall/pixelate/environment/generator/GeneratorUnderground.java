package xyz.destiall.pixelate.environment.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SplittableRandom;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.noise.PerlinNoise;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.environment.tiles.TileFactory;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;

public class GeneratorUnderground implements Generator {


    @Override
    public void generate(World world, Collection<Tile> tiles) {
        generate(0, world, tiles);
    }
    public static SplittableRandom ran = new SplittableRandom();

    private float generationScale = 0.05f;
    private float oreGenerationScale = 0.3f;

    @Override
    public void generate(int seed, World world, Collection<Tile> tiles) {
        int i = 0;
        double divisor = 1.0/Tile.SIZE;

        double min = 1, max = 0;
        List<Location> orePopulationZones = new ArrayList<Location>();

        PerlinNoise.reshufflePermutation(); //Reshuffle Permutation

        //Main terrain generation
        for (int x = -Pixelate.WIDTH; x <= Pixelate.WIDTH; x+=Tile.SIZE) {
            for (int y = -Pixelate.HEIGHT; y <= Pixelate.HEIGHT; y+=Tile.SIZE) {
                Tile tile = null;

                double noiseValue = PerlinNoise.noise(x*divisor*generationScale,y*divisor*generationScale);

                if(noiseValue < min) min = noiseValue;
                if(noiseValue > max) max = noiseValue;

                if (noiseValue > 0.59) {
                    if(noiseValue > 0.7 && noiseValue < 0.8) {
                        //Ore Spawning Algorithm
                        if (Math.random() < 0.05) //Chance to populate area with ores
                        {
                            orePopulationZones.add(new Location(x, y, world));
                        }
                    }
                    tile = TileFactory.createTile(Material.COBBLESTONE, x, y, world);

                } else if (noiseValue < 0.35) {
                    tile = TileFactory.createTile(Material.MOSSY_COBBLESTONE, x,y,world);
                }

                if(tile == null)
                    tile = TileFactory.createTile(Material.STONE, x,y,world);

                if (Math.random() > 0.98) world.spawnMonster(new Location(x, y, world), Entity.Type.ZOMBIE);
                tiles.add(tile);
            }
        }

        //Populative ore generation
        Material oreCanvas = Material.COBBLESTONE;
        for(Location loc : orePopulationZones)
        {
            Material oreLoot = Material.COAL_ORE;
            switch(ran.nextInt(0,5))
            {
                case 0:
                    oreLoot = Material.IRON_ORE;
                    break;
                case 1:
                    oreLoot = Material.GOLD_ORE;
                    break;
                case 2:
                    oreLoot = Material.LAPIS_ORE;
                    break;
                case 3:
                    oreLoot = Material.DIAMOND_ORE;
                    break;
                case 4:
                    oreLoot = Material.EMERALD_ORE;
                    break;
            }
            //Max Radius 3x3
            for(int x = loc.getX()-(int)Tile.SIZE*2; x<loc.getX()+(int)Tile.SIZE; x+=Tile.SIZE)
            {
                for(int y = loc.getY()-(int)Tile.SIZE*2; y<loc.getY()+(int)Tile.SIZE; y+=Tile.SIZE)
                {
                    double noiseValue = PerlinNoise.noise(x*divisor*oreGenerationScale,y*divisor*oreGenerationScale);
                    if(noiseValue > 0.4 && noiseValue < 0.6)
                    {
                        Location location = new Location(x,y,world);
                        location.add((Tile.SIZE - 10) / 2f, (Tile.SIZE - 10) / 2f);
                        Tile tile = tiles.stream()
                                .filter(t -> AABB.isOverlap(location, t))
                                .findFirst().orElse(null);
                        if(tile != null && tile.getMaterial() == oreCanvas)
                        {
                            tile.setMaterial(oreLoot);
                        }

                    }
                }
            }
        }


    }
}
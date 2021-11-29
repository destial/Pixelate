package xyz.destiall.pixelate.environment;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.entities.EntityMonster;
import xyz.destiall.pixelate.environment.generator.Generator;
import xyz.destiall.pixelate.environment.generator.GeneratorBasic;
import xyz.destiall.pixelate.environment.generator.GeneratorUnderground;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;

public class World implements Updateable, Renderable {
    private final List<Entity> entities;
    // TODO: Maybe split tiles into chunks?
    private final Set<Tile> tiles;
    private final Generator generator;
    private Environment worldType;

    public World() {
        this(new GeneratorBasic());
    }

    public World(Generator generator) {
        entities = new LinkedList<>();
        tiles = new HashSet<>();
        this.generator = generator;
        if (generator instanceof GeneratorBasic) {
            worldType = Environment.OVERWORLD;
        } else if (generator instanceof GeneratorUnderground) {
            worldType = Environment.CAVE;
        }
    }

    public Environment getEnvironment() {
        return worldType;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public boolean isForegroundTile(AABB aabb) {
        return tiles.stream().anyMatch(t -> aabb.isAABB(t) && t.getTileType() == Tile.TileType.FOREGROUND);
    }

    public void generateWorld(int seed, boolean force) {
        if (tiles.size() == 0 || force) {
            generator.generate(seed, this, tiles);
        }
    }

    public Location getNearestEmpty(Location requestedLocation) {
        //TODO a better location finding alogrithm based on recursive function that searches surrounding locations)
        while (requestedLocation.getTile().getTileType() != Tile.TileType.BACKGROUND) {
            requestedLocation.add(Tile.SIZE, Tile.SIZE);
        }
        return requestedLocation;
    }

    public EntityMonster spawnEntity(Location location, Entity.Type type) {
        EntityMonster monster = new EntityMonster(type);
        monster.teleport(location);
        entities.add(monster);
        return monster;
    }

    public Tile findTile(Location location) {
        return tiles.stream()
                .filter(t -> AABB.isAABB(location, t))
                .findFirst().orElse(null);
    }

    public ItemStack breakTile(Location location) {
        Tile tile = findTile(location);
        if (tile == null || tile.getTileType() != Tile.TileType.FOREGROUND) return null;
        Material prev = tile.getMaterial();
        tile.setMaterial(Material.STONE);
        return new ItemStack(prev);
    }

    @Override
    public void update() {
        for (Entity entity : entities) {
            entity.update();
        }
    }

    @Override
    public void tick() {
        for (Entity entity : entities) {
            entity.tick();
        }
    }

    @Override
    public void render(Screen canvas) {
        for (Tile tile : tiles) {
            tile.render(canvas);
        }
        for (Entity entity : entities) {
            entity.render(canvas);
        }
    }
}

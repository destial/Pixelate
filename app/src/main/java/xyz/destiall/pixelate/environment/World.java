package xyz.destiall.pixelate.environment;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.entities.EntityMonster;
import xyz.destiall.pixelate.environment.generator.Generator;
import xyz.destiall.pixelate.environment.generator.GeneratorBasic;
import xyz.destiall.pixelate.environment.generator.GeneratorUnderground;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;

public class World implements Updateable, Renderable {
    private final List<Entity> entities;
    // TODO: Maybe split tiles into chunks?
    private final List<Tile> tiles;
    private final Generator generator;
    private Environment worldType;

    public World() {
        this(new GeneratorBasic());
    }

    public World(Generator generator) {
        entities = new LinkedList<>();
        tiles = new LinkedList<>();
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
        return tiles.stream().anyMatch(t -> aabb.isOverlap(t) && t.getTileType() == Tile.TileType.FOREGROUND);
    }

    public void generateWorld(int seed, boolean force) {
        if (tiles.size() == 0 || force) {
            generator.generate(seed, this, tiles);
        }
    }

    public List<Entity> getNearestEntities(Location requestedLocation, double radius) {
        return entities.stream().filter(e -> e.getLocation().distance(requestedLocation) <= radius).collect(Collectors.toList());
    }

    public Location getNearestEmpty(Location requestedLocation) {
        //TODO a better location finding alogrithm based on recursive function that searches surrounding locations)
        while (requestedLocation.getTile().getTileType() != Tile.TileType.BACKGROUND) {
            requestedLocation.add(Tile.SIZE, Tile.SIZE);
        }
        return requestedLocation;
    }

    public EntityMonster spawnMonster(Location location, Entity.Type type) {
        EntityMonster monster = new EntityMonster(type);
        monster.teleport(location);
        entities.add(monster);
        return monster;
    }

    public Tile findTile(Location location) {
        return tiles.stream()
                .filter(t -> AABB.isOverlap(location, t))
                .findFirst().orElse(null);
    }

    public void breakTile(Location location) {
        Tile tile = findTile(location);
        if (tile == null || tile.getTileType() != Tile.TileType.FOREGROUND) return;
        tile.setMaterial(Material.STONE);
    }

    public void replaceTile(Tile oldTile, Tile newTile) {
        int index = tiles.indexOf(oldTile);
        tiles.set(index, newTile);
    }

    public List<Tile> findTiles(AABB aabb) {
        return tiles.stream().filter(aabb::isOverlap).collect(Collectors.toList());
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    @Override
    public void update() {
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).update();
        }
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).update();
        }
    }

    @Override
    public void render(Screen canvas) {
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).render(canvas);
        }
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).render(canvas);
        }
    }
}

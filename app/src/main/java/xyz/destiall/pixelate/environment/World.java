package xyz.destiall.pixelate.environment;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.entities.EntityItem;
import xyz.destiall.pixelate.entities.EntityMonster;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.entities.EntityPrimedTNT;
import xyz.destiall.pixelate.environment.generator.Generator;
import xyz.destiall.pixelate.environment.generator.GeneratorBasic;
import xyz.destiall.pixelate.environment.generator.GeneratorUnderground;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.environment.tiles.containers.ContainerTile;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.LootTable;
import xyz.destiall.pixelate.modular.Module;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;

public class World implements Updateable, Renderable, Module {
    private final List<Entity> entities;
    // TODO: Maybe split tiles into chunks?
    private final List<Tile> tiles;
    private final Generator generator;
    private Environment environment;

    public World() {
        this(new GeneratorBasic());
    }

    public World(Generator generator) {
        entities = new LinkedList<>();
        tiles = new LinkedList<>();
        this.generator = generator;
        if (generator instanceof GeneratorBasic) {
            environment = Environment.OVERWORLD;
        } else if (generator instanceof GeneratorUnderground) {
            environment = Environment.CAVE;
        }
    }

    /**
     * Get this world's environment
     * @return The environment
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Get this world's entities
     * @return The entities (raw pointer)
     */
    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * If any of the tiles in the boundary can be collided
     * @param aabb The boundary to search
     * @return true if there is, otherwise false
     */
    public boolean isForegroundTile(AABB aabb) {
        return tiles.stream().anyMatch(t -> aabb.isOverlap(t) && t.getTileType() == Tile.TileType.FOREGROUND);
    }

    /**
     * Generate this world using the given generator and seed
     * @param seed The seed to use
     * @param force Regenerate if world is already generated
     */
    public void generateWorld(int seed, boolean force) {
        if (tiles.size() == 0 || force) {
            if (force) tiles.clear();
            generator.generate(seed, this, tiles);
        }
    }

    /**
     * Get the nearest entities at the requested location
     * @param requestedLocation The requested location
     * @param radius Radius to search
     * @return List of entities around the location, can be empty but never null
     */
    public List<Entity> getNearestEntities(Location requestedLocation, double radius) {
        return entities.stream().filter(e -> e.getLocation().distanceSquared(requestedLocation) <= radius * radius).collect(Collectors.toList());
    }

    /**
     * Get the nearest spawnable location at the requested location
     * @param location The requested location
     * @return An empty location, never null
     */
    public Location getNearestEmpty(Location location) {
        return getNearestEmpty(location.getX(), location.getY());
    }

    /**
     * Get the nearest spawnable location at the requested location
     * @param x World x
     * @param y World y
     * @return An empty location, never null
     */
    public Location getNearestEmpty(double x, double y) {
        Location loc = new Location(x, y, this);
        // TODO: a better location finding alogrithm based on recursive function that searches surrounding locations)
        while (loc.getTile().getTileType() != Tile.TileType.BACKGROUND) {
            loc.add(Tile.SIZE, Tile.SIZE);
        }
        return loc;
    }

    /**
     * Spawn a monster at the requested location
     * @param location The requested location
     * @param type Type of monster
     * @return The spawned monster, never null
     */
    public EntityMonster spawnMonster(Location location, Entity.Type type) {
        EntityMonster monster = new EntityMonster(type);
        monster.teleport(location);
        entities.add(monster);
        return monster;
    }

    /**
     * Spawn a generic entity at the requested location
     * @param clazz The entity class
     * @param location The requested location
     * @return The spawned entity, or null if invalid entity class
     */
    public <E> E spawnEntity(Class<? extends Entity> clazz, Location location) {
        if (clazz == EntityPrimedTNT.class) {
            Entity e = new EntityPrimedTNT(location.getX(), location.getY(), this);
            entities.add(e);
            return (E) e;
        } else if (clazz == EntityMonster.class) {
            return (E) spawnMonster(location, Entity.Type.ZOMBIE);
        } else if (clazz == EntityPlayer.class) {
            if (entities.stream().anyMatch(e -> e instanceof EntityPlayer)) {
                return null;
            }
            Entity e = new EntityPlayer();
            entities.add(e);
            return (E) e;
        }
        return null;
    }

    /**
     * Get the tile at the requested location
     * @param location The requested location
     * @return The tile, or null if not found or if not same the world
     */
    public Tile findTile(Location location) {
        if (location.getWorld() != this) return null;
        return tiles.stream()
                .filter(t -> AABB.isOverlap(location, t))
                .findFirst().orElse(null);
    }

    /**
     * Drop an item at the requested location
     * @param item The item to drop
     * @param vector The requested location
     * @return The dropped item entity, never null
     */
    public EntityItem dropItem(ItemStack item, Vector2 vector) {
        EntityItem drop = new EntityItem(item);
        drop.teleport(new Location(vector.getX(), vector.getY(), this));
        entities.add(drop);
        return drop;
    }

    /**
     * Drop an item at the requested location
     * @param item The item to drop
     * @param location The requested location
     * @return The dropped item entity, never null
     */
    public EntityItem dropItem(ItemStack item, Location location) {
        return dropItem(item, location.toVector());
    }

    /**
     * Break a tile at the requested location
     * @param location The requested location
     * @return List of drops, or null if can't be broken
     */
    public List<ItemStack> breakTile(Location location) {
        return breakTile(findTile(location));
    }

    /**
     * Break the requested tile
     * @param tile The requested tile
     * @return List of drops, or null if can't be broken
     */
    public List<ItemStack> breakTile(Tile tile) {
        if (tile == null || tile.getTileType() != Tile.TileType.FOREGROUND) return null;
        List<ItemStack> drops = LootTable.getInstance().getDrops(tile.getMaterial(), 0);
        if (tile instanceof ContainerTile) {
            ContainerTile containerTile = (ContainerTile) tile;
            drops.addAll(Arrays.stream(containerTile.getInventory().getItems()).filter(Objects::nonNull).collect(Collectors.toList()));
        }
        tile.setMaterial(Material.STONE);
        return drops;
    }

    /**
     * Replace a tile with another tile
     * @param oldTile The old tile to find
     * @param newTile The new tile to replace
     */
    public void replaceTile(Tile oldTile, Tile newTile) {
        int index = tiles.indexOf(oldTile);
        if (index == -1) return;
        tiles.set(index, newTile);
    }

    /**
     * Get the list of tiles overlapping this boundary
     * @param aabb The boundary to search
     * @return List of tiles in this boundary, can be empty but never null
     */
    public List<Tile> findTiles(AABB aabb) {
        return tiles.stream().filter(aabb::isOverlap).collect(Collectors.toList());
    }


    /**
     * Remove an entity from this world.
     * (Deprecated) Use Entity.remove() for better checking
     * @param entity The entity to remove
     */
    @Deprecated
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

    @Override
    public void destroy() {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (e instanceof Listener) {
                Pixelate.HANDLER.unregisterListener((Listener) e);
            }
        }
        entities.clear();
        tiles.clear();
    }
}

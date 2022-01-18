package xyz.destiall.pixelate.environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import xyz.destiall.java.events.Cancellable;
import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.entities.EntityItem;
import xyz.destiall.pixelate.entities.EntityMonster;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.entities.EntityPrimedTNT;
import xyz.destiall.pixelate.environment.effects.Effect;
import xyz.destiall.pixelate.environment.generator.Generator;
import xyz.destiall.pixelate.environment.generator.GeneratorBasic;
import xyz.destiall.pixelate.environment.generator.GeneratorUnderground;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.environment.sounds.Sound;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.environment.tiles.containers.ContainerTile;
import xyz.destiall.pixelate.events.entity.EventSpawnEntity;
import xyz.destiall.pixelate.events.tile.EventTileBreak;
import xyz.destiall.pixelate.events.tile.EventTileReplace;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.LootTable;
import xyz.destiall.pixelate.items.meta.Enchantment;
import xyz.destiall.pixelate.items.meta.ItemMeta;
import xyz.destiall.pixelate.modular.Modular;
import xyz.destiall.pixelate.modular.Module;
import xyz.destiall.pixelate.modules.EffectsModule;
import xyz.destiall.pixelate.modules.SoundsModule;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;

/**
 * Written by Rance & Yong Hong
 */
public class World implements Updateable, Renderable, Module, Modular {
    protected transient final HashMap<Class<? extends Module>, Module> modules;
    private transient final Generator generator;
    private final List<Entity> entities;

    // TODO: Maybe split tiles into chunks?
    private final List<Tile> tiles;
    private Environment environment;
    private String name;

    public World() {
        this(new GeneratorBasic());
    }

    public World(Generator generator) {
        entities = new LinkedList<>();
        tiles = new LinkedList<>();
        modules = new HashMap<>();
        this.generator = generator;
        environment = Environment.OVERWORLD;
        if (generator instanceof GeneratorUnderground) {
            environment = Environment.CAVE;
        }
        addModule(new EffectsModule().setWorld(this));
        addModule(new SoundsModule().setWorld(this));
    }

    /**
     * Get the tiles of this world
     * @return The tiles
     */
    public List<Tile> getTiles() {
        return tiles;
    }

    /**
     * Set the name of this world
     * @param name The world name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the name of this world
     * @return The world name
     */
    public String getName() {
        return name;
    }

    /**
     * Play a particle effect at the requested location
     * @param type The particle type
     * @param location The requested location
     * @return The effect that is playing
     */
    public Effect playEffect(Effect.EffectType type, Location location) {
        if (!hasModule(EffectsModule.class)) return null;
        return getModule(EffectsModule.class).spawnEffect(type, location);
    }

    /**
     * Play a sound at the requested location
     * @param sound The sound to play
     * @param location The requested location
     * @param volume The volume of the sound
     * @return The sound that is playing
     */
    public Sound playSound(Sound.SoundType sound, Location location, float volume) {
        if (!hasModule(SoundsModule.class)) return null;
        return getModule(SoundsModule.class).playSound(sound, location, volume);
    }

    /**
     * Generate this world using the given generator and seed
     * @param seed The seed to use
     * @param force Regenerate if world is already generated
     */
    public void generateWorld(int seed, boolean force) {
        if (tiles.size() == 0 || force) {
            tiles.clear();
            generator.generate(seed, this, tiles);
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
    public EntityMonster spawnMonster(Entity.Type type, Location location) {
        EntityMonster monster = new EntityMonster(type);
        monster.teleport(location);
        Cancellable cancel = (Cancellable) Pixelate.HANDLER.call(new EventSpawnEntity(monster, this));
        if (cancel.isCancelled()) return null;
        entities.add(monster);
        return monster;
    }

    /**
     * Spawn a generic entity at the requested location
     * @param clazz The entity class
     * @param location The requested location
     * @return The spawned entity, or null if invalid entity class
     */
    public <E extends Entity> E spawnEntity(Class<E> clazz, Location location) {
        if (clazz == EntityPrimedTNT.class) {
            Entity e = new EntityPrimedTNT(location.getX(), location.getY(), this);
            Cancellable cancel = (Cancellable) Pixelate.HANDLER.call(new EventSpawnEntity(e, this));
            if (cancel.isCancelled()) return null;
            entities.add(e);
            return clazz.cast(e);
        } else if (clazz == EntityMonster.class) {
            return clazz.cast(spawnMonster(Entity.Type.ZOMBIE, location));
        } else if (clazz == EntityPlayer.class) {
            if (entities.stream().anyMatch(e -> e instanceof EntityPlayer)) {
                return null;
            }
            Entity e = new EntityPlayer();
            Cancellable cancel = (Cancellable) Pixelate.HANDLER.call(new EventSpawnEntity(e, this));
            if (cancel.isCancelled()) return null;
            entities.add(e);
            return clazz.cast(e);
        } else if (clazz == EntityItem.class) {
            return clazz.cast(dropItem(new ItemStack(Material.WOOD), location));
        }
        return null;
    }

    /**
     * Get the tile at the requested location
     * @param location The requested location
     * @return The tile, or null if not found or if not same the world
     */
    public Tile findTile(Location location) {
        World w;
        if ((w = location.getWorld()) == null) return null;
        if (w != this) return null;
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
        return breakTile(tile, null);
    }

    /**
     * Break the requested tile
     * @param tile The requested tile
     * @param item The item that broke the tile
     * @return List of drops, or null if can't be broken or if the event is cancelled
     */
    public List<ItemStack> breakTile(Tile tile, ItemStack item) {
        if (tile == null || tile.getTileType() != Tile.TileType.FOREGROUND) return null;
        int luck = 0;
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            luck = meta.hasEnchantment(Enchantment.FORTUNE) ? meta.getEnchantLevel(Enchantment.FORTUNE) : luck;
        }
        Material tileMat = tile.getMaterial();
        List<ItemStack> drops = LootTable.getInstance().getDrops(tile.getMaterial(), luck);
        if (tile instanceof ContainerTile) {
            ContainerTile containerTile = (ContainerTile) tile;
            drops.addAll(Arrays.stream(containerTile.getInventory().getItems()).filter(Objects::nonNull).collect(Collectors.toList()));
        }
        EventTileBreak ev = new EventTileBreak(tile, drops);
        Pixelate.HANDLER.call(ev);
        if (ev.isCancelled()) return null;

        Location tileLoc = tile.getLocation();
        dropItems(drops, tileLoc);

        Sound.SoundType sound;
        switch (tileMat) {
            case PLANKS:
            case CHEST:
            case WORKBENCH:
            case WOOD:
                sound = Sound.SoundType.BLOCK_BREAK_WOOD;
                break;
            case SAND:
                sound = Sound.SoundType.BLOCK_BREAK_SAND;
                break;
            default:
                sound = Sound.SoundType.BLOCK_BREAK_STONE;
        }
        playSound(sound, tile.getLocation(), 1.0f);

        tile.setMaterial(Material.STONE);
        return drops;
    }

    /**
     * Drop multiple items at the requested location
     * @param drops The items to drop
     * @param center The center of the requested location
     * @return The list of dropped item entities, never null
     */
    public List<EntityItem> dropItems(List<ItemStack> drops, Location center) {
        Location loc = center.clone();
        List<EntityItem> droppedEntities = new ArrayList<>();
        for (double rad = -Math.PI, i = 0; rad <= Math.PI && i < drops.size(); rad += Math.PI / drops.size(), i++) {
            ItemStack drop = drops.get((int) i);
            double x = Math.cos(i) * Tile.SIZE * 0.3;
            double y = Math.sin(i) * Tile.SIZE * 0.3;
            droppedEntities.add(dropItem(drop, loc.add(x, y)));
            loc.subtract(x, y);
        }
        return droppedEntities;
    }

    /**
     * Replace a tile with another tile
     * @param oldTile The old tile to find
     * @param newTile The new tile to replace
     */
    public void replaceTile(Tile oldTile, Tile newTile) {
        int index = tiles.indexOf(oldTile);
        if (index == -1) return;
        EventTileReplace replace = new EventTileReplace(oldTile, newTile);
        Pixelate.HANDLER.call(replace);
        if (replace.isCancelled()) return;
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
        for (Module m : modules.values()) {
            m.update();
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
        for (Module m : modules.values()) {
            if (m instanceof Renderable) {
                ((Renderable) m).render(canvas);
            }
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
        for (Module m : modules.values()) {
            m.destroy();
        }
        modules.clear();
    }

    @Override
    public <N extends Module> N getModule(Class<N> clazz) {
        if (!hasModule(clazz)) return null;
        return clazz.cast(modules.get(clazz));
    }

    @Override
    public void addModule(Module module) {
        modules.putIfAbsent(module.getClass(), module);
    }

    @Override
    public <N extends Module> boolean hasModule(Class<N> clazz) {
        return modules.containsKey(clazz);
    }

    @Override
    public <N extends Module> N removeModule(Class<N> clazz) {
        N module = getModule(clazz);
        if (module != null) {
            module.destroy();
            modules.remove(clazz);
        }
        return module;
    }
}

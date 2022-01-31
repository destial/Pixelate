package xyz.destiall.pixelate.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import xyz.destiall.java.events.Cancellable;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.entities.EntityItem;
import xyz.destiall.pixelate.entities.EntityMonster;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.entities.EntityPrimedTNT;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.events.entity.EventSpawnEntity;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.modular.Component;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.settings.Settings;
import xyz.destiall.pixelate.timer.Timer;

public class SpawnModule implements Component<World> {
    private transient final Random random = new Random();
    private transient World world;
    private int seconds;

    public SpawnModule() {
        seconds = 0;
    }

    @Override
    public void update() {
        if (!Settings.SPAWNING) return;
        if (Timer.isSecond()) {
            seconds++;
        }
        if (seconds >= 60) {
            seconds = 0;
            if (world.getEntities().size() > 10) return;

            for (Tile tile : world.getTiles()) {
                int next = random.nextInt(500);
                if (tile.getMaterial() == Material.STONE) {
                    if (next >= 499) {
                        spawnMonster(Entity.Type.ZOMBIE, tile.getLocation());
                    } else if (next <= 1) {
                        spawnMonster(Entity.Type.SKELETON, tile.getLocation());
                    }
                }
            }
        }
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
        Cancellable cancel = (Cancellable) Pixelate.HANDLER.call(new EventSpawnEntity(monster, world));
        if (cancel.isCancelled()) return null;
        world.getEntities().add(monster);
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
            Entity e = new EntityPrimedTNT(location.getX(), location.getY(), world);
            Cancellable cancel = (Cancellable) Pixelate.HANDLER.call(new EventSpawnEntity(e, world));
            if (cancel.isCancelled()) return null;
            world.getEntities().add(e);
            return clazz.cast(e);
        } else if (clazz == EntityMonster.class) {
            return clazz.cast(spawnMonster(Entity.Type.ZOMBIE, location));
        } else if (clazz == EntityPlayer.class) {
            if (world.getEntities().stream().anyMatch(e -> e instanceof EntityPlayer)) {
                return null;
            }
            Entity e = new EntityPlayer();
            Cancellable cancel = (Cancellable) Pixelate.HANDLER.call(new EventSpawnEntity(e, world));
            if (cancel.isCancelled()) return null;
            world.getEntities().add(e);
            return clazz.cast(e);
        } else if (clazz == EntityItem.class) {
            return clazz.cast(dropItem(new ItemStack(Material.WOOD), location));
        }
        return null;
    }

    /**
     * Drop an item at the requested location
     * @param item The item to drop
     * @param vector The requested location
     * @return The dropped item entity, never null
     */
    public EntityItem dropItem(ItemStack item, Vector2 vector) {
        EntityItem drop = new EntityItem(item);
        drop.teleport(new Location(vector.getX(), vector.getY(), world));
        world.getEntities().add(drop);
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

    @Override
    public void destroy() {

    }

    @Override
    public World getParent() {
        return world;
    }

    @Override
    public SpawnModule setParent(World world) {
        this.world = world;
        return this;
    }
}

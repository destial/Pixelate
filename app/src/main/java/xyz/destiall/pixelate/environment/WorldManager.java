package xyz.destiall.pixelate.environment;

import java.util.HashMap;
import java.util.Map;

import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.modular.Module;

/**
 * Written by Yong Hong
 */
public class WorldManager implements Updateable, Renderable, Module {
    private transient boolean loaded;
    private Map<String, World> worlds;
    private String activeWorld;

    public WorldManager() {
        worlds = new HashMap<>();
        loaded = false;
    }

    /**
     * Load this world manager from another instance.
     * Used when loading from a save file
     * @param wm The loaded world manager
     */
    public void load(WorldManager wm) {
        if (loaded) return;
        loaded = true;
        worlds = wm.worlds;
        for (World world : worlds.values()) {
            for (Tile tile : world.getTiles()) {
                tile.setWorld(world);
            }
            for (Entity entity : world.getEntities()) {
                entity.teleport(entity.getLocation(true).setWorld(world));
            }
        }
        activeWorld = wm.activeWorld;
    }

    /**
     * Add a world to this manager
     * @param name The name of the world
     * @param world The world
     * @return true if successfully added, otherwise false
     */
    public boolean addWorld(String name, World world) {
        if (!worlds.containsKey(name)) {
            worlds.put(name, world);
            world.setName(name);
            if (worlds.size() == 1) return setActive(name);
            return true;
        }
        return false;
    }

    /**
     * Set the current active world to use
     * @param name The name of the world
     * @return true if successfully activated, otherwise false
     */
    public boolean setActive(String name) {
        if (worlds.containsKey(name)) {
            activeWorld = name;
            return true;
        }
        return false;
    }

    /**
     * Get the current active world
     * @return The active world
     */
    public World getCurrentWorld()
    {
        return worlds.get(activeWorld);
    }

    /**
     * Get the name of the current active world
     * @return The active world name
     */
    public String getCurrentWorldName()
    {
        return activeWorld;
    }

    /**
     * Get all the worlds in this manager
     * @return The worlds mapped by their name
     */
    public Map<String, World> getWorlds() {
        return worlds;
    }

    /**
     * Check if this world is active
     * @param name The name of the world
     * @return true if active, otherwise false
     */
    public boolean isWorldActive(String name) {
        return worlds.containsKey(name) && activeWorld.equals(name);
    }

    /**
     * Check if this world name is a world
     * @param name The name of the world
     * @return true if is registered, otherwise false
     */
    public boolean isAWorld(String name)
    {
        return worlds.containsKey(name);
    }

    @Override
    public void render(Screen screen) {
        getCurrentWorld().render(screen);
    }

    @Override
    public void update() {
        getCurrentWorld().update();
    }

    @Override
    public void destroy() {
        for (World world : worlds.values()) {
            world.destroy();
        }
        worlds.clear();
    }
}

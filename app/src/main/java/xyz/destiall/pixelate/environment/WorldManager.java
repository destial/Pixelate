package xyz.destiall.pixelate.environment;

import java.util.HashMap;
import java.util.Map;

import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.modular.Module;

public class WorldManager implements Updateable, Renderable, Module {
    private Map<String, World> worlds;
    private String activeWorld;

    public WorldManager() {
        worlds = new HashMap<>();
    }

    public void load(WorldManager wm) {
        worlds = wm.worlds;
        for (World world : worlds.values()) {
            for (Tile tile : world.getTiles()) {
                tile.setWorld(world);
            }
            for (Entity entity : world.getEntities()) {
                entity.teleport(entity.getLocation().setWorld(world));
            }
        }
        activeWorld = wm.activeWorld;
    }

    public boolean addWorld(String name, World world) {
        if (!worlds.containsKey(name)) {
            worlds.put(name, world);
            world.setName(name);
            if (worlds.size() == 1) return setActive(name);
            return true;
        }
        return false;
    }

    public boolean setActive(String name) {
        if (worlds.containsKey(name)) {
            activeWorld = name;
            return true;
        }
        return false;
    }

    public World getCurrentWorld()
    {
        return worlds.get(activeWorld);
    }

    public String getCurrentWorldName()
    {
        return activeWorld;
    }

    public Map<String, World> getWorlds() {
        return worlds;
    }

    public boolean isWorldActive(String name) {
        return worlds.containsKey(name) && activeWorld.equals(name);
    }

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

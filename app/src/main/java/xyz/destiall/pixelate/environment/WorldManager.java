package xyz.destiall.pixelate.environment;

import java.util.HashMap;
import java.util.Set;

import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;

public class WorldManager implements Updateable, Renderable {
    private final HashMap<String, World> worlds;
    private String activeWorld;

    public WorldManager() {
        worlds = new HashMap<>();
    }

    public boolean addWorld(String name, World world) {
        if (!worlds.containsKey(name)) {
            worlds.put(name, world);
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

    public Set<String> getWorlds()
    {
        return worlds.keySet();
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
}

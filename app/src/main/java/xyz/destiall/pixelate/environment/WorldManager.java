package xyz.destiall.pixelate.environment;

import java.util.HashMap;
import java.util.Set;

public class WorldManager {

    HashMap<String, World> worlds;
    String activeWorld;

    public WorldManager()
    {
        worlds = new HashMap<String, World>();
    }

    public boolean addWorld(String name, World world)
    {
        if(!worlds.containsKey(name))
        {
            worlds.put(name, world);
            if(worlds.size() == 1) activeWorld = name;
            return true;
        }
        return false;
    }

    public boolean setActive(String name)
    {
        if(worlds.containsKey(name))
        {
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

    public boolean isWorldActive(String name)
    {
        if(worlds.containsKey(name) && activeWorld.equals(name))
            return true;
        return false;
    }

    public boolean isAWorld(String name)
    {
        return worlds.containsKey(name);
    }
}

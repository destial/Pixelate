package xyz.destiall.pixelate.events;

import xyz.destiall.java.events.Event;
import xyz.destiall.pixelate.environment.tiles.containers.FurnanceTile;

public class EventFurnaceInventory extends Event {
    FurnanceTile tile;

    EventFurnaceInventory(FurnanceTile tile)
    {
        this.tile = tile;
    }

    public FurnanceTile getFurnace()
    {
        return this.tile;
    }
}

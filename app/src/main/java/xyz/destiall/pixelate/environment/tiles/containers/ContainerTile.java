package xyz.destiall.pixelate.environment.tiles.containers;

import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.items.inventory.Inventory;

public abstract class ContainerTile extends Tile {
    protected Inventory tileInventory;

    protected ContainerTile() {}

    public ContainerTile(int x, int y, Material material, World world, TileType type, Inventory inventory) {
        super(x, y, material, world, type);
        this.tileInventory = inventory;
    }

    /**
     * Get the inventory related to this container tile
     * @return The inventory
     */
    public abstract Inventory getInventory();
}

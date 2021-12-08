package xyz.destiall.pixelate.environment.tiles;

import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.items.inventory.Inventory;

public abstract class ContainerTile extends Tile {

    protected Inventory tileInventory;

    public ContainerTile(int x, int y, Material material, World world, TileType type, Inventory inventory) {
        super(x, y, material, world, type);
        this.tileInventory = inventory;
    }


}

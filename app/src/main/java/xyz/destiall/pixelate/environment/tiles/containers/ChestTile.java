package xyz.destiall.pixelate.environment.tiles.containers;

import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.items.inventory.ChestInventory;

/**
 * Written by Rance
 */
public class ChestTile extends ContainerTile {
    protected ChestTile() {}

    public ChestTile(int x, int y, World world) {
        super(x, y, Material.CHEST, world, Material.CHEST.getTileType(), new ChestInventory(3 * 9));
    }

    @Override
    public ChestInventory getInventory() {
        return (ChestInventory) this.tileInventory;
    }
}

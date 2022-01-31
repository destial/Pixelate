package xyz.destiall.pixelate.environment.tiles.containers;

import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.items.inventory.EnchantTableInventory;

public class EnchantTableTile extends ContainerTile {

    protected EnchantTableTile() {}
    public EnchantTableTile(int x, int y, World world) {
        super(x, y, Material.ENCHANT_TABLE, world, Material.ENCHANT_TABLE.getTileType(), new EnchantTableInventory());
    }

    @Override
    public EnchantTableInventory getInventory() {
        return (EnchantTableInventory) tileInventory;
    }
}

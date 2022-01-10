package xyz.destiall.pixelate.items;

import xyz.destiall.pixelate.items.inventory.Inventory;

/**
 * Written by Rance
 */
public interface InventoryHolder {
    /**
     * Get the inventory of this holder
     * @return The inventory
     */
    Inventory getInventory();
}

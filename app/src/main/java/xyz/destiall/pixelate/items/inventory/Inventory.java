package xyz.destiall.pixelate.items.inventory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

import xyz.destiall.pixelate.items.ItemStack;

public abstract class Inventory {
    protected ItemStack[] items;
    private static Field inventoryFieldItemStack;

    static {
        try {
            inventoryFieldItemStack = ItemStack.class.getDeclaredField("inventory");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove an item from this inventory
     * @param item The item to remove
     */
    public abstract void removeItem(ItemStack item);

    /**
     * Add an item to this inventory
     * @param item The item to add
     * @return true if succeeded, otherwise false
     */
    public abstract boolean addItem(ItemStack item);

    /**
     * Clear all the items from this inventory
     */
    public abstract void clear();

    /**
     * Check if this inventory is full and can no longer accept any new items
     * @return true if full, otherwise false
     */
    public boolean isFull() {
        return Arrays.stream(items).allMatch(Objects::nonNull);
    }

    /**
     * Get all the items in this inventory
     * @return The current items
     */
    public ItemStack[] getItems() {
        return items;
    }

    protected static void setItemStackInventory(ItemStack item, Inventory inventory) {
        try {
            if (item == null) return;
            inventoryFieldItemStack.setAccessible(true);
            inventoryFieldItemStack.set(item, inventory);
            inventoryFieldItemStack.setAccessible(false);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }
}

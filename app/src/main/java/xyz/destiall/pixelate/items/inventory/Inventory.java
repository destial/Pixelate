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

    public abstract void removeItem(ItemStack item);
    public abstract boolean addItem(ItemStack item);
    public abstract void clear();

    public boolean isFull() {
        return Arrays.stream(items).allMatch(Objects::nonNull);
    }

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

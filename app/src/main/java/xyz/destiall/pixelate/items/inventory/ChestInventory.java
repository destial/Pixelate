package xyz.destiall.pixelate.items.inventory;

import java.util.Arrays;

import xyz.destiall.pixelate.items.ItemStack;

public class ChestInventory extends Inventory {
    private int size;
    protected ChestInventory() {}

    public ChestInventory(int size) {
        this.size = size;
        items = new ItemStack[size];
    }

    @Override
    public boolean addItem(ItemStack itemStack) {
        if (isFull()) return false;
        for (int i = 0; i < size; i++) {
            if (items[i] != null) {
                if (items[i].similar(itemStack)) {
                    items[i].addAmount(1);
                    return true;
                }
                continue;
            }
            items[i] = itemStack;
            setItemStackInventory(itemStack, this);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        for (ItemStack i : items) {
            setItemStackInventory(i, null);
        }
        Arrays.fill(items, null);
    }

    @Override
    public void removeItem(ItemStack item) {
        if (item == null) return;
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i] == item) {
                setItemStackInventory(items[i], null);
                items[i] = null;
                setItemStackInventory(item, null);
                break;
            }
        }
    }

    /**
     * Get the size of this inventory
     * @return The size
     */
    public int getSize() {
        return size;
    }

    /**
     * Set the slot of this item in this inventory
     * @param index The slot to set (between 0 and getSize() - 1)
     * @param itemStack The item to set
     * @return The previous item in the slot, or null if none
     */
    public ItemStack setItem(int index, ItemStack itemStack) {
        ItemStack prev = items[index];
        items[index] = itemStack;
        if (itemStack != null) {
            setItemStackInventory(itemStack, this);
        }
        setItemStackInventory(prev, null);
        return prev;
    }

    /**
     * Get the item in this slot
     * @param index The slot
     * @return The item, null if none
     */
    public ItemStack getItem(int index) {
        return items[index];
    }

    /**
     * Get the slot of this item
     * @param itemStack The item to find
     * @return The slot if found, otherwise -1
     */
    public int getSlot(ItemStack itemStack) {
        for (int i = 0; i < size; i++) {
            if (items[i] == itemStack) return i;
        }
        return -1;
    }
}

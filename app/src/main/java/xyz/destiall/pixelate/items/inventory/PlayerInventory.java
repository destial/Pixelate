package xyz.destiall.pixelate.items.inventory;

import java.util.Arrays;

import xyz.destiall.pixelate.items.InventoryHolder;
import xyz.destiall.pixelate.items.ItemStack;

public class PlayerInventory extends EntityInventory {
    private ItemStack[] crafting;

    protected PlayerInventory() {}

    public PlayerInventory(InventoryHolder holder, int size) {
        super(holder, size);
        items = new ItemStack[size];
        crafting = new ItemStack[4];
    }

    /**
     * Set the crafting item slot in this inventory
     * @param index The index of the slot (between 0 and 3)
     * @param itemStack The item to set
     * @return The previous item, null if none
     */
    public ItemStack setCrafting(int index, ItemStack itemStack) {
        ItemStack prev = crafting[index];
        crafting[index] = itemStack;
        if (itemStack != null) {
            setItemStackInventory(itemStack, this);
        }
        setItemStackInventory(prev, null);
        return prev;
    }

    @Override
    public boolean addItem(ItemStack itemStack) {
        if (isFull()) return false;
        for (int i = 0; i < size; i++) {
            if (items[i] != null) {
                if (items[i].similar(itemStack)) {
                    items[i].addAmount(itemStack.getAmount());
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
        for (int i = 0; i < crafting.length; i++) {
            if (crafting[i] != null && crafting[i] == item) {
                setItemStackInventory(crafting[i], null);
                crafting[i] = null;
                setItemStackInventory(item, null);
                break;
            }
        }
    }

    /**
     * Clear the current items in the crafting slots
     */
    public void clearCrafting() {
        for (ItemStack c : crafting) {
            setItemStackInventory(c, null);
        }
        Arrays.fill(crafting, null);
    }

    /**
     * Get the item in this crafting slot
     * @param index The crafting slot index (between 0 and 3)
     * @return The item in this slot, null if none
     */
    public ItemStack getCraftingItem(int index) {
        return crafting[index];
    }

    /**
     * Get all the items in this crafting
     * @return The crafting items
     */
    public ItemStack[] getCrafting() {
        return crafting;
    }

    /**
     * Get the crafting slot that this item is in
     * @param itemStack The item to find
     * @return The slot (between 0 and 3) if found, otherwise -1
     */
    public int getCraftingSlot(ItemStack itemStack) {
        for (int i = 0; i < crafting.length; i++) {
            if (crafting[i] == itemStack) return i;
        }
        return -1;
    }
}

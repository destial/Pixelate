package xyz.destiall.pixelate.items;

import java.util.Arrays;
import java.util.Objects;

public class Inventory {
    private final InventoryHolder holder;
    private final int size;
    private final ItemStack[] items;
    private final ItemStack[] crafting;

    public Inventory(InventoryHolder holder, int size) {
        this.holder = holder;
        this.size = size;
        items = new ItemStack[size];
        crafting = new ItemStack[4];
    }

    public InventoryHolder getHolder() {
        return holder;
    }

    public int getSize() {
        return size;
    }

    public ItemStack[] getItems() {
        return items;
    }

    public ItemStack setItem(int index, ItemStack itemStack) {
        ItemStack prev = items[index];
        items[index] = itemStack;
        return prev;
    }

    public ItemStack setCrafting(int index, ItemStack itemStack) {
        ItemStack prev = crafting[index];
        crafting[index] = itemStack;
        return prev;
    }

    public boolean addItem(ItemStack itemStack) {
        if (isFull()) return false;
        for (int i = 0; i < size; i++) {
            if (items[i] != null) {
                if (items[i].getMaterial() == itemStack.getMaterial()) {
                    items[i].addAmount(1);
                    break;
                }
                continue;
            }
            items[i] = itemStack;
            return true;
        }
        return false;
    }

    public void clearCrafting() {
        Arrays.fill(crafting, null);
    }

    public ItemStack getItem(int index) {
        return items[index];
    }

    public ItemStack getCraftingItem(int index) {
        return crafting[index];
    }

    public ItemStack[] getCrafting() {
        return crafting;
    }

    public int getSlot(ItemStack itemStack) {
        for (int i = 0; i < size; i++) {
            if (items[i] == itemStack) return i;
        }
        return -1;
    }

    public int getCraftingSlot(ItemStack itemStack) {
        for (int i = 0; i < crafting.length; i++) {
            if (crafting[i] == itemStack) return i;
        }
        return -1;
    }

    public boolean isFull() {
        return Arrays.stream(items).allMatch(Objects::nonNull);
    }
}

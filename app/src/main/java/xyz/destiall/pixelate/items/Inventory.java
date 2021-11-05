package xyz.destiall.pixelate.items;

import java.util.Arrays;
import java.util.Objects;

public class Inventory {
    private final InventoryHolder holder;
    private final int size;
    private final ItemStack[] items;

    public Inventory(InventoryHolder holder, int size) {
        this.holder = holder;
        this.size = size;
        items = new ItemStack[size];
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

    public boolean addItem(ItemStack itemStack) {
        if (isFull()) return false;
        for (int i = 0; i < size; i++) {
            if (items[i] != null) {
                if (items[i].equals(itemStack)) {
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

    public ItemStack getItem(int index) {
        return items[index];
    }

    public int getSlot(ItemStack itemStack) {
        for (int i = 0; i < size; i++) {
            if (items[i] == itemStack) return i;
        }
        return -1;
    }

    public boolean isFull() {
        return Arrays.stream(items).allMatch(Objects::nonNull);
    }
}

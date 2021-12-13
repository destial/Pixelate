package xyz.destiall.pixelate.items.inventory;

import java.util.Arrays;
import java.util.Objects;

import xyz.destiall.pixelate.items.ItemStack;

public class ChestInventory extends Inventory {
    private final int size;
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

    public boolean isFull() {
        return Arrays.stream(items).allMatch(Objects::nonNull);
    }

    public int getSize() {
        return size;
    }

    public ItemStack setItem(int index, ItemStack itemStack) {
        ItemStack prev = items[index];
        items[index] = itemStack;
        if (itemStack != null) {
            setItemStackInventory(itemStack, this);
        }
        setItemStackInventory(prev, null);
        return prev;
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
}

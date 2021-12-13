package xyz.destiall.pixelate.items.inventory;

import java.util.Arrays;
import java.util.Objects;

import xyz.destiall.pixelate.items.InventoryHolder;
import xyz.destiall.pixelate.items.ItemStack;

public class PlayerInventory extends Inventory {
    private final InventoryHolder holder;
    private final ItemStack[] crafting;
    private final int size;

    public PlayerInventory(InventoryHolder holder, int size) {
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

    public ItemStack setItem(int index, ItemStack itemStack) {
        ItemStack prev = items[index];
        items[index] = itemStack;
        if (itemStack != null) {
            setItemStackInventory(itemStack, this);
        }
        setItemStackInventory(prev, null);
        return prev;
    }

    public ItemStack setCrafting(int index, ItemStack itemStack) {
        ItemStack prev = crafting[index];
        crafting[index] = itemStack;
        if (itemStack != null) {
            setItemStackInventory(itemStack, this);
        }
        setItemStackInventory(prev, null);
        return prev;
    }

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

    public void clear() {
        for (ItemStack i : items) {
            setItemStackInventory(i, null);
        }
        Arrays.fill(items, null);
    }

    public void clearCrafting() {
        for (ItemStack c : crafting) {
            setItemStackInventory(c, null);
        }
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

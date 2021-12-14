package xyz.destiall.pixelate.items.inventory;

import java.util.Arrays;

import xyz.destiall.pixelate.items.InventoryHolder;
import xyz.destiall.pixelate.items.ItemStack;

public class PlayerInventory extends EntityInventory {
    private final ItemStack[] crafting;

    public PlayerInventory(InventoryHolder holder, int size) {
        super(holder, size);
        items = new ItemStack[size];
        crafting = new ItemStack[4];
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

    public void clearCrafting() {
        for (ItemStack c : crafting) {
            setItemStackInventory(c, null);
        }
        Arrays.fill(crafting, null);
    }

    public ItemStack getCraftingItem(int index) {
        return crafting[index];
    }

    public ItemStack[] getCrafting() {
        return crafting;
    }

    public int getCraftingSlot(ItemStack itemStack) {
        for (int i = 0; i < crafting.length; i++) {
            if (crafting[i] == itemStack) return i;
        }
        return -1;
    }
}

package xyz.destiall.pixelate.items.inventory;

import xyz.destiall.pixelate.items.ItemStack;

public class FurnaceInventory extends Inventory {
    private ItemStack toSmelt, burner, processed;

    public FurnaceInventory() {}

    @Override
    public void removeItem(ItemStack item) {}

    @Override
    public boolean addItem(ItemStack item) {
        return false;
    }

    @Override
    public void clear() {
        toSmelt = null;
        burner = null;
        processed = null;
    }

    @Override
    public ItemStack[] getItems() {
        return new ItemStack[] { toSmelt, burner, processed };
    }

    public ItemStack getBurnerSlot() { return burner; }
    public ItemStack getToSmeltSlot() { return toSmelt; }
    public ItemStack getProcessedSlot() { return processed; }

    public void setBurnerSlot(ItemStack item) { burner = item; }
    public void setToSmeltSlot(ItemStack item) { toSmelt = item; }
    public void setProcessedSlot(ItemStack item) { processed = item; }
}

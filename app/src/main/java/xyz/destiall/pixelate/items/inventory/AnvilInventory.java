package xyz.destiall.pixelate.items.inventory;

import xyz.destiall.pixelate.items.ItemStack;

/**
 * Written by Yong Hong
 */
public class AnvilInventory extends Inventory {
    private ItemStack repairItem, additive, result;

    public AnvilInventory() {}

    //Can be redone to figure out material type and whether it can be removed from burner or processor slot
    @Override
    public void removeItem(ItemStack item) {}

    //Can be redone to figure out material type and whether it can fit into burner or processor slot
    @Override
    public boolean addItem(ItemStack item) {
        return false;
    }

    @Override
    public void clear() {
        repairItem = null;
        additive = null;
        result = null;
    }

    @Override
    public ItemStack[] getItems() {
        return new ItemStack[] { repairItem, additive, result };
    }

    public ItemStack getRepairItemSlot() { return repairItem; }
    public ItemStack getAdditiveSlot() { return additive; }
    public ItemStack getResultSlot() { return result; }

    public void setRepairItemSlot(ItemStack item) {
        if (item != null)
            setItemStackInventory(item, this);
        repairItem = item;
    }

    public void setAdditive(ItemStack item) {
        if (item != null)
            setItemStackInventory(item, this);
        additive = item;
    }

    public void setResultSlot(ItemStack item) {
        if (item != null)
            setItemStackInventory(item, this);
        result = item;
    }
}

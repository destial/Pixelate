package xyz.destiall.pixelate.items.inventory;

import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.items.ItemStack;

public class CreativeInventory extends EntityInventory {
    public CreativeInventory() {
        super(null, 3 * 9);
        for (Material m : Material.values()) {
            addItem(new ItemStack(m));
        }
    }

    @Override
    public void removeItem(ItemStack item) {}

    @Override
    public boolean addItem(ItemStack item) {
        if (isFull()) return false;
        for (int i = 0; i < size; i++) {
            if (items[i] != null) continue;
            items[i] = item;
            return true;
        }
        return false;
    }

    @Override
    public ItemStack setItem(int index, ItemStack itemStack) {
        return itemStack;
    }

    @Override
    public ItemStack[] getItems() {
        return super.getItems().clone();
    }
}

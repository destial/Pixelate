package xyz.destiall.pixelate.items.inventory;

import xyz.destiall.pixelate.items.ItemStack;

public abstract class Inventory {
    protected ItemStack[] items;
    public Inventory() {};

    abstract public void removeItem(ItemStack item);
    abstract public boolean addItem(ItemStack item);
}

package xyz.destiall.pixelate.items;

import android.graphics.Bitmap;
import android.graphics.Color;

import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Glint;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.items.inventory.Inventory;
import xyz.destiall.pixelate.items.meta.DefaultItemMeta;
import xyz.destiall.pixelate.items.meta.ItemFlag;
import xyz.destiall.pixelate.items.meta.ItemMeta;

/**
 * Written by Rance
 */
public class ItemStack {
    private transient Inventory inventory;
    private Material material;
    private ItemMeta meta;
    private int amount;

    private ItemStack() {}

    public ItemStack(Material material) {
        this(material, 1);
    }

    public ItemStack(Material material, int amount) {
        setType(material);
        setAmount(amount);
        meta = new DefaultItemMeta();
    }

    /**
     * Set the type of this item
     * @param material The material to set
     */
    public void setType(Material material) {
        this.material = material;
    }

    /**
     * Get the image that this item represents
     * @return The Bitmap image
     */
    public Bitmap getImage() {
        return material.getImage();
    }

    /**
     * Add an amount to this item
     * @param amount The amount to add
     */
    public void addAmount(int amount) {
        this.amount += amount;
    }

    /**
     * Remove an amount from this item
     * Removes itself from the current inventory if amount is <= 0
     * @param amount The amount to remove
     */
    public void removeAmount(int amount) {
        if (amount == 0) return;
        this.amount -= amount;
        if (this.amount <= 0) removeFromInventory();
    }

    /**
     * Sets the amount of this item
     * Removes itself from the current inventory if amount is <= 0
     * @param amount The amount to set
     */
    public void setAmount(int amount) {
        this.amount = amount;
        if (amount == 0) {
            removeFromInventory();
        }
    }

    private void removeFromInventory() {
        this.amount = 0;
        if (inventory != null) {
            inventory.removeItem(this);
        }
    }

    /**
     * Get the material type of this item
     * @return The material
     */
    public Material getType() {
        return material;
    }

    /**
     * Get the amount of this item
     * @return The amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Create an item based on this tile.
     * (Deprecated) Use LootTable instead
     * @param tile The tile to create
     * @return The created item
     */
    @Deprecated
    public static ItemStack of(Tile tile) {
        return new ItemStack(tile.getMaterial(), 1);
    }

    @Override
    public ItemStack clone() {
        ItemStack clone = new ItemStack(material, amount);
        clone.meta = meta.clone();
        return clone;
    }

    /**
     * Clone this item, including the inventory that this item is in
     * @return The cloned item
     */
    public ItemStack cloneItem() {
        ItemStack clone = clone();
        clone.inventory = inventory;
        return clone;
    }

    /**
     * Check if this item is similar in material and amount
     * @param other The other item to check
     * @return true if same, otherwise false
     */
    public boolean equals(ItemStack other) {
        return similar(other) && other.amount == amount;
    }

    /**
     * Check if this item is similar in material
     * @param other The other item to check
     * @return true if same, otherwise false
     */
    public boolean similar(ItemStack other) {
        return other.material == material && other.meta.equals(meta);
    }

    /**
     * Get the item meta of this item
     * @return The item meta
     */
    public ItemMeta getItemMeta() {
        return meta;
    }

    /**
     * Set the item meta of this item
     * @param meta The new item meta
     */
    public void setItemMeta(ItemMeta meta) {
        if (meta.getClass().equals(this.meta.getClass())) {
            this.meta = meta;
            return;
        }
        throw new ClassCastException("Trying to set item meta with invalid class: " + meta.getClass() + ", expected " + this.meta.getClass());
    }

    private static final Bitmap image = ResourceManager.getBitmap(R.drawable.hotbar);

    /**
     * Render an item meta in an inventory
     * @param screen The screen to render to
     * @param item The item to render
     * @param x Top left x
     * @param y Top left y
     */
    public static void renderInventory(Screen screen, ItemStack item, int x, int y) {
        ItemMeta meta = item.getItemMeta();
        if (item.getType().isTool()) {
            screen.bar(x + 7, y + (int) (image.getWidth() * 0.6), image.getWidth() - 40, 10, Color.GREEN, Color.RED, meta.getDurability() / (float) item.getType().getMaxDurability());
        }
        if (meta.isEnchanted() && !meta.hasItemFlag(ItemFlag.HIDE_ENCHANT)) {
            Glint.INSTANCE.renderInventory(screen, x, y);
        }
    }
}

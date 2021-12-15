package xyz.destiall.pixelate.items;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.items.inventory.Inventory;

@SuppressWarnings("all")
public class ItemStack extends Imageable {
    private final Material material;
    private Inventory inventory;
    private int amount;

    public ItemStack(Material material) {
        this(material, 1);
    }

    public ItemStack(Material material, int amount) {
        super(Pixelate.getTileMap(), Material.getRows(), Material.getColumns());
        this.material = material;
        this.amount = amount;
        if (!material.isBlock()) {
            image = ResourceManager.getBitmap(material.getDrawable());
        } else {
            image = createSubImageAt(material.getRow(), material.getColumn());
        }
        height = image.getHeight();
        width = image.getWidth();
        inventory = null;
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
        return new ItemStack(material, amount);
    }

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
        return other.material == material && other.amount == amount;
    }

    /**
     * Check if this item is similar in material
     * @param other The other item to check
     * @return true if same, otherwise false
     */
    public boolean similar(ItemStack other) {
        return other.material == material;
    }
}

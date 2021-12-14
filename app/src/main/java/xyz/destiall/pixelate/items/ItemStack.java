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

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void removeAmount(int amount) {
        if (amount == 0) return;
        this.amount -= amount;
        if (this.amount <= 0) removeFromInventory();
    }

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

    public Material getType() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

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

    public boolean equals(ItemStack other) {
        return other.material == material && other.amount == amount;
    }

    public boolean similar(ItemStack other) {
        return other.material == material;
    }
}

package xyz.destiall.pixelate.items;

import android.graphics.BitmapFactory;

import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Imageable;

public class ItemStack extends Imageable {
    private final Material material;
    private int amount;

    public ItemStack(Material material) {
        this(material, 1);
    }

    public ItemStack(Material material, int amount) {
        super(Game.getTileMap(), Material.getRows(), Material.getColumns());
        this.material = material;
        this.amount = amount;
        if (!material.isBlock()) {
            image = BitmapFactory.decodeResource(Game.getResources(), material.getDrawable());
        } else {
            image = createSubImageAt(material.getRow(), material.getColumn());
        }
        height = image.getHeight();
        width = image.getWidth();
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void removeAmount(int amount) {
        if (amount == 0) return;
        this.amount -= amount;
        if (this.amount < 0) this.amount = 0;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Material getMaterial() {
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

    public boolean equalsWithAmount(ItemStack other) {
        return other.material == material && other.amount == amount;
    }
}

package xyz.destiall.pixelate.items;

import android.graphics.BitmapFactory;

import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Imageable;

public class ItemStack extends Imageable {
    private final int amount;
    private final Material material;

    public ItemStack(Material material) {
        this(material, 1);
    }

    public ItemStack(Material material, int amount) {
        super(Game.getTileMap(), 1, 2);
        this.material = material;
        this.amount = amount;
        if (!material.isBlock()) {
            image = BitmapFactory.decodeResource(Game.getResources(), material.getDrawable());
            height = image.getHeight();
            width = image.getWidth();
        }
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
}

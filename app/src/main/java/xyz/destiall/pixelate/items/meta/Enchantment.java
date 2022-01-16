package xyz.destiall.pixelate.items.meta;

import java.util.Arrays;

import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.environment.materials.ToolType;
import xyz.destiall.pixelate.items.ItemStack;

/**
 * Written by Rance
 */
public enum Enchantment {
    DAMAGE_ALL("Sharpness",5, ToolType.AXE, ToolType.SWORD),
    DIG_SPEED("Efficiency",5, ToolType.AXE, ToolType.PICKAXE, ToolType.SHOVEL),
    DURABILITY("Durability",3, ToolType.SHEAR, ToolType.AXE, ToolType.PICKAXE, ToolType.SHOVEL),
    FORTUNE("Fortune",3, ToolType.AXE, ToolType.PICKAXE, ToolType.SHOVEL)

    ;

    private final String enchantName;
    private final int max;
    private final ToolType[] types;

    Enchantment(String enchantName, int max, ToolType... types) {
        this.enchantName = enchantName;
        this.max = max;
        this.types = types;
    }

    /**
     * Get the enchant name of this enchantment
     * @return The beauty enchantment name
     */
    public String getEnchantName()
    {
        return enchantName;
    }

    /**
     * Get the max level of this enchantment
     * @return The max level
     */
    public int getMaxLevel() {
        return max;
    }

    /**
     * If this material can be enchanted with this enchantment
     * @param material The material to check
     * @return true if can be enchanted, otherwise false
     */
    public boolean canEnchant(Material material) {
        return Arrays.stream(types).anyMatch(t -> t == material.getToolType());
    }

    /**
     * If this item can be enchanted with this enchantment
     * @param item The item to check
     * @return true if can be enchanted, otherwise false
     */
    public boolean canEnchantItem(ItemStack item) {
        return canEnchant(item.getType());
    }
}

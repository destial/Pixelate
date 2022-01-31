package xyz.destiall.pixelate.items.inventory;

import java.util.HashMap;
import java.util.Map;

import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.environment.materials.ToolType;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.meta.Enchantment;
import xyz.destiall.pixelate.position.Location;

public class EnchantTableInventory extends Inventory {
    private final Map<ItemStack, Map<Enchantment, Integer>> generatedEnchantments;
    private ItemStack enchantSlot;
    private ItemStack lapisSlot;

    public EnchantTableInventory() {
        generatedEnchantments = new HashMap<>();
        items = new ItemStack[0];
    }

    @Override
    public void removeItem(ItemStack item) {
        if (item == null) return;
        if (item == enchantSlot) {
            enchantSlot = null;
        }
        if (item == lapisSlot) {
            lapisSlot = null;
        }
    }

    public Map<Enchantment, Integer> generateEnchantments(Tile tile) {
        if (enchantSlot == null || lapisSlot == null) return null;
        if (enchantSlot.getItemMeta().isEnchanted()) return null;
        if (generatedEnchantments.containsKey(enchantSlot)) return generatedEnchantments.get(enchantSlot);
        int bookshelves = 0;
        Location location = tile.getLocation();
        World w;
        if ((w = location.getWorld()) == null) return null;
        int x = location.getX() - 2 * Tile.SIZE;
        int y = location.getY() - 2 * Tile.SIZE;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (location.distance(x, y) <= Tile.SIZE) continue;
                Tile t;
                if ((t = w.findTile(new Location(x, y, w))) == null) continue;
                bookshelves += t.getMaterial() == Material.BOOKSHELF ? 1 : 0;
            }
        }
        Map<Enchantment, Integer> enchantment = new HashMap<>();
        if (enchantSlot.getType().getToolType() == ToolType.AXE) {
            enchantment.put(Enchantment.DIG_SPEED, Math.min(bookshelves + 1, Enchantment.DIG_SPEED.getMaxLevel()));
            enchantment.put(Enchantment.DURABILITY, Math.min(bookshelves + 1, Enchantment.DURABILITY.getMaxLevel()));
            enchantment.put(Enchantment.DAMAGE_ALL, Math.min(bookshelves + 1, Enchantment.DAMAGE_ALL.getMaxLevel()));
        } else if (enchantSlot.getType().getToolType() == ToolType.PICKAXE) {
            enchantment.put(Enchantment.DIG_SPEED, Math.min(bookshelves + 1, Enchantment.DIG_SPEED.getMaxLevel()));
            enchantment.put(Enchantment.DURABILITY, Math.min(bookshelves + 1, Enchantment.DURABILITY.getMaxLevel()));
            enchantment.put(Enchantment.FORTUNE, Math.min(bookshelves + 1, Enchantment.FORTUNE.getMaxLevel()));
        } else if (enchantSlot.getType().getToolType() == ToolType.SWORD) {
            enchantment.put(Enchantment.DURABILITY, Math.min(bookshelves + 1, Enchantment.DURABILITY.getMaxLevel()));
            enchantment.put(Enchantment.DAMAGE_ALL, Math.min(bookshelves + 1, Enchantment.DAMAGE_ALL.getMaxLevel()));
        } else if (enchantSlot.getType().getToolType() == ToolType.SHOVEL) {
            enchantment.put(Enchantment.DIG_SPEED, Math.min(bookshelves + 1, Enchantment.DIG_SPEED.getMaxLevel()));
            enchantment.put(Enchantment.DURABILITY, Math.min(bookshelves + 1, Enchantment.DURABILITY.getMaxLevel()));;
        } else {
            return null;
        }
        generatedEnchantments.put(enchantSlot, enchantment);
        return enchantment;
    }

    public void enchant(Map.Entry<Enchantment, Integer> enchantment) {
        if (this.enchantSlot == null || this.lapisSlot == null) return;
        enchantSlot.getItemMeta().addEnchantment(enchantment.getKey(), enchantment.getValue());
        lapisSlot.removeAmount(1);
        generatedEnchantments.remove(enchantSlot);
    }

    public void setEnchantSlot(ItemStack slot) {
        enchantSlot = slot;
        setItemStackInventory(enchantSlot, this);
    }

    public void setLapisSlot(ItemStack slot) {
        this.lapisSlot = slot;
        setItemStackInventory(lapisSlot, this);
    }

    public ItemStack getEnchantSlot() {
        return enchantSlot;
    }

    public ItemStack getLapisSlot() {
        return lapisSlot;
    }

    @Override
    public boolean addItem(ItemStack item) {
        return false;
    }

    @Override
    public void clear() {
        enchantSlot = null;
        lapisSlot = null;
    }
}

package xyz.destiall.pixelate.items.meta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DefaultItemMeta implements ItemMeta {
    private final Map<Enchantment, Integer> enchants = new HashMap<>();
    private final List<ItemFlag> flags = new ArrayList<>();
    private int durability;
    private String display;
    private boolean unbreakable;

    @Override
    public void setDisplayName(String name) {
        this.display = name;
    }

    @Override
    public String getDisplayName() {
        return display;
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    @Override
    public boolean isUnbreakable() {
        return unbreakable;
    }

    @Override
    public boolean hasDisplayName() {
        return display != null;
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        return enchants;
    }

    @Override
    public void addEnchantment(Enchantment enchant, int level) {
        if (level > enchant.getMaxLevel()) return;
        if (level <= 0) {
            enchants.remove(enchant);
        }
        enchants.put(enchant, level);
    }

    @Override
    public boolean removeEnchantment(Enchantment enchant) {
        return enchants.remove(enchant) != null;
    }

    @Override
    public boolean hasEnchantment(Enchantment enchant) {
        return enchants.containsKey(enchant);
    }

    @Override
    @SuppressWarnings("all")
    public int getEnchantLevel(Enchantment enchant) {
        return hasEnchantment(enchant) ? enchants.get(enchant) : -1;
    }

    @Override
    public boolean isEnchanted() {
        return enchants.size() != 0;
    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public void setDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public void addItemFlag(ItemFlag... flags) {
        this.flags.addAll(Arrays.asList(flags));
        this.flags.sort(Comparator.comparing(Enum::name));
    }

    @Override
    public boolean hasItemFlag(ItemFlag flag) {
        return flags.contains(flag);
    }

    @Override
    public void removeItemFlag(ItemFlag flag) {
        flags.remove(flag);
        flags.sort(Comparator.comparing(Enum::name));
    }

    @Override
    public ItemMeta clone() {
        DefaultItemMeta meta = new DefaultItemMeta();
        meta.display = display;
        for (Map.Entry<Enchantment, Integer> en : enchants.entrySet()) {
            meta.enchants.put(en.getKey(), en.getValue());
        }
        meta.unbreakable = unbreakable;
        return meta;
    }

    @Override
    public boolean equals(ItemMeta other) {
        if (!other.getClass().equals(getClass())) return false;
        DefaultItemMeta meta = (DefaultItemMeta) other;
        return Objects.equals(meta.display, display) &&
                meta.durability == durability &&
                meta.unbreakable == unbreakable &&
                Arrays.equals(meta.flags.toArray(), flags.toArray()) &&
                meta.enchants.equals(enchants);
    }
}

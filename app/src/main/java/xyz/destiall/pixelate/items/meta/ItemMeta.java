package xyz.destiall.pixelate.items.meta;

import java.util.Map;

public interface ItemMeta {

    /**
     * Set the display name of this item
     * @param name The display name
     */
    void setDisplayName(String name);

    /**
     * Get the display name of this item
     * @return The display name
     */
    String getDisplayName();

    /**
     * If this item has a display name set
     * @return true if not null, otherwise false
     */
    boolean hasDisplayName();

    /**
     * Set this item to be unbreakable
     * @param unbreakable If unbreakable
     */
    void setUnbreakable(boolean unbreakable);

    /**
     * If this item is unbreakable
     * @return true if unbreakable, otherwise false
     */
    boolean isUnbreakable();

    /**
     * Get the enchantment mappings of this item
     * @return The enchantments
     */
    Map<Enchantment, Integer> getEnchantments();

    /**
     * Add an enchantment to this item.
     * Will fail if level is greater than enchantment max level
     * @param enchant The enchant to add
     * @param level The level
     */
    void addEnchantment(Enchantment enchant, int level);

    /**
     * Remove this enchantment from this item
     * @param enchant The enchantment to remove
     * @return true if successful, otherwise false
     */
    boolean removeEnchantment(Enchantment enchant);

    /**
     * If this item has this enchantment
     * @param enchant The enchantment to check
     * @return true if it has, otherwise false
     */
    boolean hasEnchantment(Enchantment enchant);

    /**
     * Get the enchantment level of this enchant
     * @param enchant The enchantment to check
     * @return The enchantment level or -1 if not found
     */
    int getEnchantLevel(Enchantment enchant);

    /**
     * If this item has enchantments
     * @return true if it has, otherwise false
     */
    boolean isEnchanted();

    /**
     * Get this item's durability
     * @return The durability
     */
    int getDurability();

    /**
     * Set this item's durability
     * @param durability The new durability
     */
    void setDurability(int durability);

    /**
     * Add flags to this item
     * @param flags The item flags to add
     */
    void addItemFlag(ItemFlag... flags);

    /**
     * If this item has this flag
     * @param flag The flag to check
     * @return true if it has, otherwise false
     */
    boolean hasItemFlag(ItemFlag flag);

    /**
     * Remove a flag from this item
     * @param flag The flag to remove
     */
    void removeItemFlag(ItemFlag flag);

    /**
     * Clone this item meta
     * @return An immutable clone
     */
    ItemMeta clone();

    /**
     * If this item meta is similar to another
     * @param other The other item meta
     * @return true if similar, otherwise false
     */
    boolean equals(ItemMeta other);
}

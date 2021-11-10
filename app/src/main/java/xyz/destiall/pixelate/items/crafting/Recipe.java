package xyz.destiall.pixelate.items.crafting;

import java.util.HashMap;
import java.util.Map;

import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.items.Inventory;
import xyz.destiall.pixelate.items.ItemStack;

public class Recipe {
    private final String key;
    private final ItemStack item;
    private Map<Integer, String> format;
    private Material[] recipe;

    public Recipe(String key, ItemStack item) {
        this.key = key;
        this.item = item;

    }

    public String getKey() {
        return key;
    }

    // TODO: Make crafting recipes fulfill if they are just in the correct order, regardless of position
    public boolean isFulfilled(Inventory inventory) {
        ItemStack[] crafting = inventory.getCrafting();
        int i = 0;
        for (ItemStack stack : crafting) {
            if (stack != null) {
                if (i >= recipe.length) return false;
                if (stack.getMaterial() == recipe[i]) return true;
            }
            i++;
        }
        return false;
    }

    public void setShape(String... args) {
        recipe = new Material[9];
        format = new HashMap<>(args.length);
        int i = 0;
        for (String s : args) {
            format.put(i++, s);
        }
    }

    public void setIngredient(String element, Material material) {
        format.entrySet().stream().filter(en -> en.getValue().equals(element)).forEach(en -> recipe[en.getKey()] = material);
    }

    public ItemStack getItem() {
        return item;
    }
}
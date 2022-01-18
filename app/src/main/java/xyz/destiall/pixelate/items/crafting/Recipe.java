package xyz.destiall.pixelate.items.crafting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.items.ItemStack;

/**
 * Written by Rance
 */
public class Recipe {
    private final String key;
    private final ItemStack item;
    private Map<Integer, String> format;
    private Material[] recipe;
    private final Map<Material, Integer> ingredients;

    public Recipe(String key, ItemStack item) {
        this.key = key;
        this.item = item;
        ingredients = new HashMap<>();
    }

    public Map<Material, Integer> getIngredients() {
        return ingredients;
    }

    /**
     * The key name of this recipe
     * @return The name
     */
    public String getKey() {
        return key;
    }

    /**
     * Check if this current crafting array fulfills this recipe
     * @param crafting The crafting array
     * @return true if fulfilled, otherwise false
     */
    // TODO: Make crafting recipes fulfill if they are just in the correct order, regardless of position
    public boolean isFulfilled(ItemStack[] crafting) {
        Material[] format = {
          crafting[0] == null ? null : crafting[0].getType(), crafting[1] == null ? null : crafting[1].getType(), crafting[2] == null ? null : crafting[2].getType(),
          crafting[3] == null ? null : crafting[3].getType(), crafting[4] == null ? null : crafting[4].getType(), crafting[5] == null ? null : crafting[5].getType(),
          crafting[6] == null ? null : crafting[6].getType(), crafting[7] == null ? null : crafting[7].getType(), crafting[8] == null ? null : crafting[8].getType()
        };
        return Arrays.equals(recipe, format);
    }

    /**
     * Set the shape of the crafting recipe.
     * This is the args order:
     * { 0, 1, 2 }
     * { 3, 4, 5 }
     * { 6, 7, 8 }
     * If there is an empty spot between elements, use null
     * @param args The shape
     */
    public void setShape(String... args) {
        recipe = new Material[9];
        format = new HashMap<>(args.length);
        int i = 0;
        for (String s : args) {
            format.put(i++, s);
        }
    }

    /**
     * Set the ingredient of the arguments in the shape
     * @param element The element to find
     * @param material The material to replace
     */
    public void setIngredient(String element, Material material) {
        format.entrySet().stream().filter(en -> en.getValue() != null && en.getValue().equals(element)).forEach(en -> {
            recipe[en.getKey()] = material;
            ingredients.put(material, ingredients.getOrDefault(material, 0) + 1);
        });
    }

    public ItemStack getItem() {
        return item.clone();
    }
}

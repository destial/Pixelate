package xyz.destiall.pixelate.items.crafting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Written by Rance
 */
public class RecipeManager {
    private static final Map<String, Recipe> recipeMap = new HashMap<>();

    /**
     * Add a recipe to this manager to search
     * @param recipe The recipe
     */
    public static void addRecipe(Recipe recipe) {
        recipeMap.put(recipe.getKey(), recipe);
    }

    /**
     * Get all the recipes
     * @return List of recipes
     */
    public static Collection<Recipe> getRecipes() {
        return recipeMap.values();
    }
}

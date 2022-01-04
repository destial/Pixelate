package xyz.destiall.pixelate.items.crafting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RecipeManager {
    private static final Map<String, Recipe> recipeMap = new HashMap<>();

    public static void addRecipe(Recipe recipe) {
        recipeMap.put(recipe.getKey(), recipe);
    }

    public static Collection<Recipe> getRecipes() {
        return recipeMap.values();
    }

}

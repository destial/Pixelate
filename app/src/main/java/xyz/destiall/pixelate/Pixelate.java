package xyz.destiall.pixelate;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import xyz.destiall.java.events.EventHandling;
import xyz.destiall.pixelate.activities.GameActivity;
import xyz.destiall.pixelate.commands.CommandGraph;
import xyz.destiall.pixelate.commands.executors.EnchantCommand;
import xyz.destiall.pixelate.commands.executors.GamemodeCommand;
import xyz.destiall.pixelate.commands.executors.ItemCommand;
import xyz.destiall.pixelate.commands.executors.SettingsCommand;
import xyz.destiall.pixelate.commands.executors.SpawnCommand;
import xyz.destiall.pixelate.commands.executors.SummonCommand;
import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.WorldManager;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.gui.HUD;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.crafting.Recipe;
import xyz.destiall.pixelate.items.crafting.RecipeManager;
import xyz.destiall.pixelate.items.inventory.Inventory;
import xyz.destiall.pixelate.items.meta.ItemMeta;
import xyz.destiall.pixelate.modular.Module;
import xyz.destiall.pixelate.serialize.EntitySerializer;
import xyz.destiall.pixelate.serialize.InventorySerializer;
import xyz.destiall.pixelate.serialize.ItemMetaSerializer;
import xyz.destiall.pixelate.serialize.ModuleSerializer;
import xyz.destiall.pixelate.serialize.TileSerializer;
import xyz.destiall.pixelate.states.GSM;
import xyz.destiall.pixelate.states.StateGame;
import xyz.destiall.pixelate.timer.Timer;

/**
 * Written by Rance
 */
public class Pixelate extends Thread {
    public static final EventHandling HANDLER = new EventHandling();
    public static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setLenient()
            .setPrettyPrinting()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(Entity.class, new EntitySerializer())
            .registerTypeAdapter(Module.class, new ModuleSerializer())
            .registerTypeAdapter(Inventory.class, new InventorySerializer())
            .registerTypeAdapter(ItemMeta.class, new ItemMetaSerializer())
            .registerTypeAdapter(Tile.class, new TileSerializer())
            .create();

    public static int HEIGHT;
    public static int WIDTH;

    private static GameSurface gameSurface;
    private static SurfaceHolder surfaceHolder;
    private static Canvas canvas;
    private static Bitmap tileMap;
    private static Bitmap blockBreakAnimationMap;
    private static GSM stateManager;
    public static boolean PAUSED = false;
    private final Timer timer;
    private static RecipeManager recipeManager;
    private static CommandGraph commandGraph;
    private static HUD hud;
    private boolean running;

    public Pixelate(GameSurface gameSurface, SurfaceHolder surfaceHolder)  {
        super();
        Pixelate.gameSurface = gameSurface;
        Pixelate.surfaceHolder = surfaceHolder;
        tileMap = ResourceManager.getBitmap(R.drawable.tilemap);
        tileMap = Imageable.resizeImage(tileMap, 1.52f);
        blockBreakAnimationMap = ResourceManager.getBitmap(R.drawable.blockbreakanimation);
        blockBreakAnimationMap = Imageable.resizeImage(blockBreakAnimationMap, 2.12f);
        HEIGHT = gameSurface.getHeight();
        WIDTH = gameSurface.getWidth();
        timer = new Timer();
        stateManager = new GSM();
        stateManager.addState("Game", new StateGame(gameSurface));
        commandGraph = new CommandGraph();
        recipeManager = new RecipeManager();
        hud = new HUD();
        setupCommands();
        setupRecipes();
    }

    @Override
    public void run() {
        String savePath = "game.json";

        stateManager.setState("Game");
        if (!stateManager.getCurrentState().load(savePath)) {
            stateManager.getCurrentState().reset();
        }
        while (running)  {
            try {
                canvas = surfaceHolder.lockCanvas();
                if (canvas == null) continue;
                HEIGHT = canvas.getHeight();
                WIDTH = canvas.getWidth();
                update();
                render(canvas);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null)  {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    canvas = null;
                }
            }
        }
        stateManager.getState("Game").save(savePath);
        stateManager.destroy();
        hud.destroy();
    }

    public void setRunning(boolean running)  {
        this.running = running;
    }

    private void update() {
        timer.update();
        stateManager.update();
    }

    private void render(Canvas canvas) {
        canvas.drawRGB(0, 0, 0);
        stateManager.render(canvas);
    }

    public static boolean setWorld(String world) {
        StateGame gameState = ((StateGame) stateManager.getState("Game"));
        WorldManager wm = gameState.getWorldManager();
        if (wm.isAWorld(world) && !wm.isWorldActive(world)) {
            // Remove player from current world
            World current = wm.getCurrentWorld();
            current.getEntities().remove(gameState.getPlayer());

            //Set new active world
            wm.setActive(world);

            // Teleport player to new world
            World next = wm.getCurrentWorld();
            next.getEntities().add(gameState.getPlayer());
            gameState.getPlayer().teleport(next.getNearestEmpty(0,0));
            return true;
        }
        return false;
    }

    public static Bitmap getTileMap() {
        return tileMap;
    }

    public static Bitmap getBlockBreakAnimationMap() { return blockBreakAnimationMap; }

    public static Resources getResources() {
        return getGameSurface().getResources();
    }

    public static GSM getGSM() {
        return stateManager;
    }

    public static GameSurface getGameSurface() {
        return gameSurface;
    }

    public static GameActivity getContext() {
        return (GameActivity) getGameSurface().getContext();
    }

    public static RecipeManager getRecipeManager() {
        return recipeManager;
    }

    public static CommandGraph getCommands() {
        return commandGraph;
    }

    public static HUD getHud() {
        return hud;
    }

    private static void setupCommands() {
        commandGraph.registerCommand("spawn", new SpawnCommand());
        commandGraph.registerCommand("summon", new SummonCommand());
        commandGraph.registerCommand("gamemode", new GamemodeCommand());
        commandGraph.registerCommand("settings", new SettingsCommand());
        commandGraph.registerCommand("item", new ItemCommand());
        commandGraph.registerCommand("enchant", new EnchantCommand());
    }

    private static void setupRecipes() {
        Recipe plankRecipe = new Recipe("plank1", new ItemStack(Material.PLANKS, 4));
        plankRecipe.setShape("W");
        plankRecipe.setIngredient("W", Material.WOOD);
        recipeManager.addRecipe(plankRecipe);

        plankRecipe = new Recipe("plank2", new ItemStack(Material.PLANKS, 4));
        plankRecipe.setShape(null, "W");
        plankRecipe.setIngredient("W", Material.WOOD);
        recipeManager.addRecipe(plankRecipe);

        plankRecipe = new Recipe("plank3", new ItemStack(Material.PLANKS, 4));
        plankRecipe.setShape(null, null, null, "W");
        plankRecipe.setIngredient("W", Material.WOOD);
        recipeManager.addRecipe(plankRecipe);

        Recipe stickRecipe = new Recipe("stick1", new ItemStack(Material.STICK, 4));
        stickRecipe.setShape("P", null, null,
                             "P");
        stickRecipe.setIngredient("P", Material.PLANKS);
        recipeManager.addRecipe(stickRecipe);

        stickRecipe = new Recipe("stick2", new ItemStack(Material.STICK, 4));
        stickRecipe.setShape(null, "P", null,
                             null, "P");
        stickRecipe.setIngredient("P", Material.PLANKS);
        recipeManager.addRecipe(stickRecipe);

        Recipe workBenchRecipe = new Recipe("workbench", new ItemStack(Material.WORKBENCH));
        workBenchRecipe.setShape("P", "P", null,
                                 "P", "P");
        workBenchRecipe.setIngredient("P", Material.PLANKS);
        recipeManager.addRecipe(workBenchRecipe);

        Recipe furnaceRecipe = new Recipe("furnace", new ItemStack(Material.FURNACE));
        furnaceRecipe.setShape("C", "C" , "C",
                               "C", null, "C",
                               "C", "C" , "C");

        furnaceRecipe.setIngredient("C", Material.COBBLESTONE);
        recipeManager.addRecipe(furnaceRecipe);

        Recipe tnt = new Recipe("tnt", new ItemStack(Material.TNT));
        tnt.setShape("G", "S", "G",
                     "S", "G", "S",
                     "G", "S", "G");
        tnt.setIngredient("G", Material.GUNPOWDER);
        tnt.setIngredient("S", Material.SAND);
        recipeManager.addRecipe(tnt);

        // WOOD TOOLS
        Recipe woodPickaxe = new Recipe("wood_pickaxe", new ItemStack(Material.WOODEN_PICKAXE));
        woodPickaxe.setShape("P", "P", "P",
                        null, "S", null,
                        null, "S", null);
        woodPickaxe.setIngredient("P", Material.PLANKS);
        woodPickaxe.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(woodPickaxe);

        Recipe woodAxe = new Recipe("wood_axe1", new ItemStack(Material.WOODEN_AXE));
        woodAxe.setShape("P", "P", null,
                         "P", "S", null,
                        null, "S");
        woodAxe.setIngredient("P", Material.PLANKS);
        woodAxe.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(woodAxe);

        woodAxe = new Recipe("wood_axe2", new ItemStack(Material.WOODEN_AXE));
        woodAxe.setShape(null, "P", "P",
                         null, "S", "P",
                         null, "S");
        woodAxe.setIngredient("P", Material.PLANKS);
        woodAxe.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(woodAxe);

        Recipe woodSword = new Recipe("wood_sword1", new ItemStack(Material.WOODEN_SWORD));
        woodSword.setShape("P", null, null,
                           "P", null, null,
                           "S");
        woodSword.setIngredient("P", Material.PLANKS);
        woodSword.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(woodSword);

        woodSword = new Recipe("wood_sword2", new ItemStack(Material.WOODEN_SWORD));
        woodSword.setShape(null, "P", null,
                           null, "P", null,
                           null, "S");
        woodSword.setIngredient("P", Material.PLANKS);
        woodSword.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(woodSword);

        woodSword = new Recipe("wood_sword3", new ItemStack(Material.WOODEN_SWORD));
        woodSword.setShape(null, null, "P",
                           null, null, "P",
                           null, null, "S");
        woodSword.setIngredient("P", Material.PLANKS);
        woodSword.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(woodSword);

        // STONE TOOLS
        Recipe stonePickaxe = new Recipe("stone_pickaxe", new ItemStack(Material.STONE_PICKAXE));
        stonePickaxe.setShape("P", "P", "P",
                null, "S", null,
                null, "S", null);
        stonePickaxe.setIngredient("P", Material.STONE);
        stonePickaxe.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(stonePickaxe);

        Recipe stoneAxe = new Recipe("stone_axe1", new ItemStack(Material.STONE_AXE));
        stoneAxe.setShape("P", "P", null,
                "P", "S", null,
                null, "S");
        stoneAxe.setIngredient("P", Material.STONE);
        stoneAxe.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(stoneAxe);

        stoneAxe = new Recipe("stone_axe2", new ItemStack(Material.STONE_AXE));
        stoneAxe.setShape(null, "P", "P",
                null, "S", "P",
                null, "S");
        stoneAxe.setIngredient("P", Material.STONE);
        stoneAxe.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(stoneAxe);

        Recipe stoneSword = new Recipe("stone_sword1", new ItemStack(Material.STONE_SWORD));
        stoneSword.setShape("P", null, null,
                "P", null, null,
                "S");
        stoneSword.setIngredient("P", Material.STONE);
        stoneSword.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(stoneSword);

        stoneSword = new Recipe("stone_sword2", new ItemStack(Material.STONE_SWORD));
        stoneSword.setShape(null, "P", null,
                null, "P", null,
                null, "S");
        stoneSword.setIngredient("P", Material.STONE);
        stoneSword.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(stoneSword);

        stoneSword = new Recipe("stone_sword3", new ItemStack(Material.STONE_SWORD));
        stoneSword.setShape(null, null, "P",
                null, null, "P",
                null, null, "S");
        stoneSword.setIngredient("P", Material.STONE);
        stoneSword.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(stoneSword);

        // IRON TOOLS
        Recipe ironPickaxe = new Recipe("iron_pickaxe", new ItemStack(Material.IRON_PICKAXE));
        ironPickaxe.setShape("P", "P", "P",
                null, "S", null,
                null, "S", null);
        ironPickaxe.setIngredient("P", Material.IRON_INGOT);
        ironPickaxe.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(ironPickaxe);

        Recipe ironAxe = new Recipe("iron_axe1", new ItemStack(Material.IRON_AXE));
        ironAxe.setShape("P", "P", null,
                "P", "S", null,
                null, "S");
        ironAxe.setIngredient("P", Material.IRON_INGOT);
        ironAxe.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(ironAxe);

        ironAxe = new Recipe("iron_axe2", new ItemStack(Material.IRON_AXE));
        ironAxe.setShape(null, "P", "P",
                null, "S", "P",
                null, "S");
        ironAxe.setIngredient("P", Material.IRON_INGOT);
        ironAxe.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(ironAxe);

        Recipe ironSword = new Recipe("iron_sword1", new ItemStack(Material.IRON_SWORD));
        ironSword.setShape("P", null, null,
                "P", null, null,
                "S");
        ironSword.setIngredient("P", Material.IRON_INGOT);
        ironSword.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(ironSword);

        ironSword = new Recipe("iron_sword2", new ItemStack(Material.IRON_SWORD));
        ironSword.setShape(null, "P", null,
                null, "P", null,
                null, "S");
        ironSword.setIngredient("P", Material.IRON_INGOT);
        ironSword.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(ironSword);

        ironSword = new Recipe("iron_sword3", new ItemStack(Material.IRON_SWORD));
        ironSword.setShape(null, null, "P",
                null, null, "P",
                null, null, "S");
        ironSword.setIngredient("P", Material.IRON_INGOT);
        ironSword.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(ironSword);

        // GOLD TOOLS
        Recipe goldPickaxe = new Recipe("gold_pickaxe", new ItemStack(Material.GOLD_PICKAXE));
        goldPickaxe.setShape("P", "P", "P",
                null, "S", null,
                null, "S", null);
        goldPickaxe.setIngredient("P", Material.GOLD_INGOT);
        goldPickaxe.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(goldPickaxe);

        Recipe goldAxe = new Recipe("gold_axe1", new ItemStack(Material.GOLD_AXE));
        goldAxe.setShape("P", "P", null,
                "P", "S", null,
                null, "S");
        goldAxe.setIngredient("P", Material.GOLD_INGOT);
        goldAxe.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(goldAxe);

        goldAxe = new Recipe("gold_axe2", new ItemStack(Material.GOLD_AXE));
        goldAxe.setShape(null, "P", "P",
                null, "S", "P",
                null, "S");
        goldAxe.setIngredient("P", Material.GOLD_INGOT);
        goldAxe.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(goldAxe);

        Recipe goldSword = new Recipe("gold_sword1", new ItemStack(Material.GOLD_SWORD));
        goldSword.setShape("P", null, null,
                "P", null, null,
                "S");
        goldSword.setIngredient("P", Material.GOLD_INGOT);
        goldSword.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(goldSword);

        goldSword = new Recipe("gold_sword2", new ItemStack(Material.GOLD_SWORD));
        goldSword.setShape(null, "P", null,
                null, "P", null,
                null, "S");
        goldSword.setIngredient("P", Material.GOLD_INGOT);
        goldSword.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(goldSword);

        goldSword = new Recipe("gold_sword3", new ItemStack(Material.GOLD_SWORD));
        goldSword.setShape(null, null, "P",
                null, null, "P",
                null, null, "S");
        goldSword.setIngredient("P", Material.GOLD_INGOT);
        goldSword.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(goldSword);

        // DIAMOND TOOLS
        Recipe diamondPickaxe = new Recipe("diamond_pickaxe", new ItemStack(Material.DIAMOND_PICKAXE));
        diamondPickaxe.setShape("P", "P", "P",
                null, "S", null,
                null, "S", null);
        diamondPickaxe.setIngredient("P", Material.DIAMOND);
        diamondPickaxe.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(diamondPickaxe);

        Recipe diamondAxe = new Recipe("diamond_axe1", new ItemStack(Material.DIAMOND_AXE));
        diamondAxe.setShape("P", "P", null,
                "P", "S", null,
                null, "S");
        diamondAxe.setIngredient("P", Material.DIAMOND);
        diamondAxe.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(diamondAxe);

        diamondAxe = new Recipe("diamond_axe2", new ItemStack(Material.DIAMOND_AXE));
        diamondAxe.setShape(null, "P", "P",
                null, "S", "P",
                null, "S");
        diamondAxe.setIngredient("P", Material.DIAMOND);
        diamondAxe.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(diamondAxe);

        Recipe diamondSword = new Recipe("diamond_sword1", new ItemStack(Material.DIAMOND_SWORD));
        diamondSword.setShape("P", null, null,
                "P", null, null,
                "S");
        diamondSword.setIngredient("P", Material.DIAMOND);
        diamondSword.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(diamondSword);

        diamondSword = new Recipe("diamond_sword2", new ItemStack(Material.DIAMOND_SWORD));
        diamondSword.setShape(null, "P", null,
                null, "P", null,
                null, "S");
        diamondSword.setIngredient("P", Material.DIAMOND);
        diamondSword.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(diamondSword);

        diamondSword = new Recipe("diamond_sword3", new ItemStack(Material.DIAMOND_SWORD));
        diamondSword.setShape(null, null, "P",
                null, null, "P",
                null, null, "S");
        diamondSword.setIngredient("P", Material.DIAMOND);
        diamondSword.setIngredient("S", Material.STICK);
        recipeManager.addRecipe(diamondSword);
    }
}

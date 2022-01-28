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
    private boolean running;

    public Pixelate(GameSurface gameSurface, SurfaceHolder surfaceHolder)  {
        super();
        Pixelate.gameSurface = gameSurface;
        Pixelate.surfaceHolder = surfaceHolder;
        Pixelate.tileMap = ResourceManager.getBitmap(R.drawable.tilemap);
        Pixelate.tileMap = Imageable.resizeImage(tileMap, 1.52f);
        Pixelate.blockBreakAnimationMap = ResourceManager.getBitmap(R.drawable.blockbreakanimation);
        Pixelate.blockBreakAnimationMap = Imageable.resizeImage(blockBreakAnimationMap, 2.12f);
        Pixelate.HEIGHT = gameSurface.getHeight();
        Pixelate.WIDTH = gameSurface.getWidth();
        timer = new Timer();
        Pixelate.stateManager = new GSM();
        Pixelate.stateManager.addState("Game", new StateGame(gameSurface));
        commandGraph = new CommandGraph();
        recipeManager = new RecipeManager();
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
        // { W  , null
        // null, null }
        plankRecipe.setIngredient("W", Material.WOOD);
        recipeManager.addRecipe(plankRecipe);

        plankRecipe = new Recipe("plank2", new ItemStack(Material.PLANKS, 4));
        plankRecipe.setShape(null, "W");
        plankRecipe.setIngredient("W", Material.WOOD);
        recipeManager.addRecipe(plankRecipe);

        plankRecipe = new Recipe("plank3", new ItemStack(Material.PLANKS, 4));
        plankRecipe.setShape(null, null, "W");
        plankRecipe.setIngredient("W", Material.WOOD);
        recipeManager.addRecipe(plankRecipe);

        plankRecipe = new Recipe("plank4", new ItemStack(Material.PLANKS, 4));
        plankRecipe.setShape(null, null, null, "W");
        plankRecipe.setIngredient("W", Material.WOOD);
        recipeManager.addRecipe(plankRecipe);

        Recipe stickRecipe = new Recipe("stick1", new ItemStack(Material.STICK, 4));
        stickRecipe.setShape("P", null, "P");
        stickRecipe.setIngredient("P", Material.PLANKS);
        recipeManager.addRecipe(stickRecipe);

        stickRecipe = new Recipe("stick2", new ItemStack(Material.STICK, 4));
        stickRecipe.setShape(null, "P", null, "P");
        stickRecipe.setIngredient("P", Material.PLANKS);
        recipeManager.addRecipe(stickRecipe);
    }
}

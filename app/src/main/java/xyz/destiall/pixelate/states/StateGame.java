package xyz.destiall.pixelate.states;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xyz.destiall.pixelate.GameSurface;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.entities.EntityPrimedTNT;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.WorldManager;
import xyz.destiall.pixelate.environment.generator.GeneratorUnderground;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.gui.HUD;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.crafting.Recipe;
import xyz.destiall.pixelate.items.crafting.RecipeManager;
import xyz.destiall.pixelate.modular.Modular;
import xyz.destiall.pixelate.modular.Module;
import xyz.destiall.pixelate.position.Location;

public class StateGame extends State implements Modular {
    private final HashMap<Class<? extends Module>, Module> modules;
    private final EntityPlayer player;
    private final List<Object> allObjects;
    private final Screen screen;

    public StateGame(GameSurface gameSurface) {
        super(gameSurface);
        allObjects = new ArrayList<>();
        modules = new HashMap<>();

        World world = new World();
        world.generateWorld(0, true);

        World cave = new World(new GeneratorUnderground());
        cave.generateWorld(0, true);

        WorldManager worldManager = new WorldManager();

        worldManager.addWorld("Overworld", world);
        worldManager.addWorld("Cave", cave);
        worldManager.setActive("Overworld");
        allObjects.add(worldManager);

        player = new EntityPlayer();
        Location location = new Location(0, 0, worldManager.getCurrentWorld());

        //Adding example items
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemStack d_axe = new ItemStack(Material.DIAMOND_AXE, 1);
        ItemStack d_pickaxe = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemStack furnace = new ItemStack(Material.FURNACE, 1);
        ItemStack chest = new ItemStack(Material.CHEST, 1);
        player.getInventory().addItem(sword);
        player.getInventory().addItem(d_axe);
        player.getInventory().addItem(d_pickaxe);
        player.getInventory().addItem(furnace);
        player.getInventory().addItem(chest);

        Location loc = worldManager.getCurrentWorld().getNearestEmpty(location);
        world.dropItem(chest, loc.add(Tile.SIZE, Tile.SIZE));
        player.teleport(loc.subtract(Tile.SIZE, Tile.SIZE));
        worldManager.getCurrentWorld().getEntities().add(player);

        world.spawnEntity(EntityPrimedTNT.class, loc);

        allObjects.add(HUD.INSTANCE);
        screen = new Screen(null, player, Pixelate.WIDTH, Pixelate.HEIGHT);

        setupRecipes();
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    @Override
    public void update() {
        for (Object o : allObjects) {
            if (o instanceof Updateable) {
                ((Updateable) o).update();
            }
        }
        for (Module m : modules.values()) {
            m.update();
        }
    }

    @Override
    public void render(Canvas canvas) {
        screen.update(canvas, player, Pixelate.WIDTH, Pixelate.HEIGHT);
        for (Object o : allObjects) {
            if (o instanceof Renderable) {
                ((Renderable) o).render(screen);
            }
        }
        for (Module m : modules.values()) {
            if (m instanceof Renderable) {
                ((Renderable) m).render(screen);
            }
        }
    }

    @Override
    public void destroy() {
        for (Object o : allObjects) {
            if (o instanceof Module) {
                ((Module) o).destroy();
            }
        }
        for (Module m : modules.values()) {
            m.destroy();
        }
        allObjects.clear();
        modules.clear();
    }

    public <N> N getObject(Class<N> clazz) {
        return (N) allObjects.stream().filter(o -> o.getClass().isAssignableFrom(clazz)).findFirst().orElse(null);
    }

    @Override
    public <N extends Module> N getModule(Class<N> clazz) {
        return (N) modules.get(clazz);
    }

    @Override
    public  void addModule(Module module) {
        modules.putIfAbsent(module.getClass(), module);
    }

    @Override
    public <N extends Module> boolean hasModule(Class<N> clazz) {
        return modules.containsKey(clazz);
    }

    @Override
    public <N extends Module> N removeModule(Class<N> clazz) {
        N module = getModule(clazz);
        if (module != null) {
            module.destroy();
            modules.remove(clazz);
        }
        return module;
    }

    private void setupRecipes() {

        Recipe plankRecipe = new Recipe("plank1", new ItemStack(Material.PLANKS, 4));
        plankRecipe.setShape("W");
        // { W  , null
        // null, null }
        plankRecipe.setIngredient("W", Material.WOOD);
        RecipeManager.addRecipe(plankRecipe);

        plankRecipe = new Recipe("plank2", new ItemStack(Material.PLANKS, 4));
        plankRecipe.setShape(null, "W");
        plankRecipe.setIngredient("W", Material.WOOD);
        RecipeManager.addRecipe(plankRecipe);

        plankRecipe = new Recipe("plank3", new ItemStack(Material.PLANKS, 4));
        plankRecipe.setShape(null, null, "W");
        plankRecipe.setIngredient("W", Material.WOOD);
        RecipeManager.addRecipe(plankRecipe);

        plankRecipe = new Recipe("plank4", new ItemStack(Material.PLANKS, 4));
        plankRecipe.setShape(null, null, null, "W");
        plankRecipe.setIngredient("W", Material.WOOD);
        RecipeManager.addRecipe(plankRecipe);

        Recipe stickRecipe = new Recipe("stick1", new ItemStack(Material.STICK, 4));
        stickRecipe.setShape("P", null, "P");
        stickRecipe.setIngredient("P", Material.PLANKS);
        RecipeManager.addRecipe(stickRecipe);

        stickRecipe = new Recipe("stick2", new ItemStack(Material.STICK, 4));
        stickRecipe.setShape(null, "P", null, "P");
        stickRecipe.setIngredient("P", Material.PLANKS);
        RecipeManager.addRecipe(stickRecipe);
    }
}

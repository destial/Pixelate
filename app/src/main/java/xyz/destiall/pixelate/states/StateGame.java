package xyz.destiall.pixelate.states;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.GameSurface;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.WorldManager;
import xyz.destiall.pixelate.environment.generator.GeneratorBasic;
import xyz.destiall.pixelate.environment.generator.GeneratorUnderground;
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

        World world = new World(new GeneratorBasic());
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

        player.teleport(worldManager.getCurrentWorld().getNearestEmpty(location));
        worldManager.getCurrentWorld().getEntities().add(player);

        allObjects.add(HUD.INSTANCE);
        screen = new Screen(null, player, Game.WIDTH, Game.HEIGHT);

        Recipe plankRecipe = new Recipe("plank1", new ItemStack(Material.PLANKS));
        plankRecipe.setShape("W"); // { W  , null
                                    // null, null }
        plankRecipe.setIngredient("W", Material.WOOD);
        RecipeManager.addRecipe(plankRecipe);

        plankRecipe = new Recipe("plank2", new ItemStack(Material.PLANKS));
        plankRecipe.setShape(null, "W");
        plankRecipe.setIngredient("W", Material.WOOD);
        RecipeManager.addRecipe(plankRecipe);

        plankRecipe = new Recipe("plank3", new ItemStack(Material.PLANKS));
        plankRecipe.setShape(null, null, "W");
        plankRecipe.setIngredient("W", Material.WOOD);
        RecipeManager.addRecipe(plankRecipe);

        plankRecipe = new Recipe("plank4", new ItemStack(Material.PLANKS));
        plankRecipe.setShape(null, null, null, "W");
        plankRecipe.setIngredient("W", Material.WOOD);
        RecipeManager.addRecipe(plankRecipe);
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
    public void tick() {
        for (Object o : allObjects) {
            if (o instanceof Updateable) {
                ((Updateable) o).tick();
            }
        }
        for (Module m : modules.values()) {
            m.tick();
        }
    }

    @Override
    public void render(Canvas canvas) {
        screen.update(canvas, player, Game.WIDTH, Game.HEIGHT);
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

    public <N> N getObject(Class<N> clazz) {
        return (N) allObjects.stream().filter(o -> o.getClass().isAssignableFrom(clazz)).findFirst().orElse(null);
    }

    @Override
    public <N extends Module> N getModule(Class<N> clazz) {
        return (N) modules.get(clazz);
    }

    @Override
    public <N extends Module> void addModule(N module) {
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
}

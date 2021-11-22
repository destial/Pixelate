package xyz.destiall.pixelate.states;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.GameSurface;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.WorldManager;
import xyz.destiall.pixelate.environment.generator.GeneratorBasic;
import xyz.destiall.pixelate.environment.generator.GeneratorUnderground;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.gui.HUD;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.crafting.Recipe;
import xyz.destiall.pixelate.position.Location;

public class StateGame extends State {
    private final EntityPlayer player;
    private List<Object> objects;
    private WorldManager worldsManagement;
    private final Screen screen;


    public StateGame(GameSurface gameSurface) {
        super(gameSurface);
        objects = new ArrayList<>();

        World world = new World(new GeneratorBasic());
        world.generateWorld(0, true);

        World cave = new World(new GeneratorUnderground());
        cave.generateWorld(0, true);

        worldsManagement = new WorldManager();

        worldsManagement.addWorld("Overworld", world);
        worldsManagement.addWorld("Cave", cave);

        player = new EntityPlayer(gameSurface);
        Location location = new Location(0, 0, worldsManagement.getCurrentWorld());

        player.teleport(worldsManagement.getCurrentWorld().useBestEmptyLocation(location));
        worldsManagement.getCurrentWorld().getEntities().add(player);

        objects.add(HUD.INSTANCE);
        screen = new Screen(null, player, Game.WIDTH, Game.HEIGHT);

        Recipe plankRecipe = new Recipe("plank1", new ItemStack(Material.PLANKS));
        plankRecipe.setShape("W");
        plankRecipe.setIngredient("W", Material.WOOD);
        Game.addRecipe(plankRecipe);

        plankRecipe = new Recipe("plank2", new ItemStack(Material.PLANKS));
        plankRecipe.setShape(null, "W");
        plankRecipe.setIngredient("W", Material.WOOD);
        Game.addRecipe(plankRecipe);

        plankRecipe = new Recipe("plank3", new ItemStack(Material.PLANKS));
        plankRecipe.setShape(null, null, "W");
        plankRecipe.setIngredient("W", Material.WOOD);
        Game.addRecipe(plankRecipe);

        plankRecipe = new Recipe("plank4", new ItemStack(Material.PLANKS));
        plankRecipe.setShape(null, null, null, "W");
        plankRecipe.setIngredient("W", Material.WOOD);
        Game.addRecipe(plankRecipe);
    }

    public EntityPlayer getPlayer() { return player; }
    public WorldManager getWorldManager() { return worldsManagement; }

    @Override
    public void update() {
        worldsManagement.getCurrentWorld().update();
        for (Object o : objects) {
            if (o instanceof Updateable) {
                ((Updateable) o).update();
            }
        }
    }

    @Override
    public void tick() {
        worldsManagement.getCurrentWorld().tick();
        for (Object o : objects) {
            if (o instanceof Updateable) {
                ((Updateable) o).tick();
            }
        }
    }

    @Override
    public void render(Canvas canvas) {
        screen.update(canvas, player, Game.WIDTH, Game.HEIGHT);
        worldsManagement.getCurrentWorld().render(screen);
        for (Object o : objects) {
            if (o instanceof Renderable) {
                ((Renderable) o).render(screen);
            }
        }
    }
}

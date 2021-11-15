package xyz.destiall.pixelate.states;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.GameSurface;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;
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
    private final List<Object> objects;
    private final Screen screen;

    public StateGame(GameSurface gameSurface) {
        super(gameSurface);
        objects = new ArrayList<>();
        World world = new World();
        player = new EntityPlayer(gameSurface);
        world.generateWorld(0, true);
        Location location = new Location(0, 0, world);
        while (location.getTile().getTileType() != Tile.TILE_TYPE.BACKGROUND) {
            location.add(Tile.SIZE, Tile.SIZE);
        }
        player.teleport(location);
        world.getEntities().add(player);
        objects.add(world);
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

    @Override
    public void update() {
        for (Object o : objects) {
            if (o instanceof Updateable) {
                ((Updateable) o).update();
            }
        }
    }

    @Override
    public void tick() {
        for (Object o : objects) {
            if (o instanceof Updateable) {
                ((Updateable) o).tick();
            }
        }
    }

    @Override
    public void render(Canvas canvas) {
        screen.update(canvas, player, Game.WIDTH, Game.HEIGHT);
        for (Object o : objects) {
            if (o instanceof Renderable) {
                ((Renderable) o).render(screen);
            }
        }
    }
}

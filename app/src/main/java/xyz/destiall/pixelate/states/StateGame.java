package xyz.destiall.pixelate.states;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.GameSurface;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.gui.HUD;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.position.Location;

public class StateGame implements State {
    private final EntityPlayer player;
    private final List<Object> objects;

    public StateGame(GameSurface gameSurface) {
        objects = new ArrayList<>();
        World world = new World();
        player = new EntityPlayer(gameSurface);
        player.teleport(new Location(Game.WIDTH / 2f, Game.HEIGHT / 2f, world));
        if (player.getInventory().addItem(new ItemStack(Material.SWORD))) {
            System.out.println("Added item");
        }
        world.getEntities().add(player);
        objects.add(world);
        objects.add(HUD.INSTANCE);
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
        Screen screen = new Screen(canvas, player, Game.WIDTH, Game.HEIGHT);
        for (Object o : objects) {
            if (o instanceof Renderable) {
                ((Renderable) o).render(screen);
            }
        }
    }
}

package xyz.destiall.pixelate.states;

import android.graphics.Canvas;
import android.graphics.Color;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import xyz.destiall.pixelate.GameSurface;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.WorldManager;
import xyz.destiall.pixelate.environment.generator.GeneratorUnderground;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.modular.Modular;
import xyz.destiall.pixelate.modular.Module;
import xyz.destiall.pixelate.settings.Settings;
import xyz.destiall.pixelate.timer.Timer;

/**
 * Written by Rance
 */
public class StateGame extends State implements Modular {
    private final HashMap<Class<? extends Module>, Module> modules;
    private EntityPlayer player;
    private WorldManager worldManager;
    private Screen screen;

    public StateGame(GameSurface surface) {
        modules = new HashMap<>();
        setSurface(surface);
    }

    @Override
    public boolean load(String path) {
        File level = new File(surface.getContext().getFilesDir().getPath() + File.separator + path);
        if (!level.exists()) return false;
        try {
            worldManager = new WorldManager();
            worldManager.load(Pixelate.GSON.fromJson(new FileReader(level), WorldManager.class));
            player = (EntityPlayer) worldManager.getCurrentWorld().getEntities().stream().filter(e -> e instanceof EntityPlayer).findFirst().orElse(new EntityPlayer());
            initialize();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void reset() {
        World world = new World();
        World cave = new World(new GeneratorUnderground());

        worldManager = new WorldManager();
        worldManager.addWorld("Overworld", world);
        worldManager.addWorld("Cave", cave);
        world.generateWorld(0, true);
        cave.generateWorld(0, true);
        worldManager.setActive("Overworld");

        player = new EntityPlayer();

        player.teleport(worldManager.getCurrentWorld().getNearestEmpty(0, 0));
        worldManager.getCurrentWorld().getEntities().add(player);

        initialize();
    }

    @Override
    public void save(String path) {
        String json = Pixelate.GSON.toJson(worldManager);
        try {
            File level = new File(surface.getContext().getFilesDir().getPath() + File.separator + path);
            if (!level.exists()) level.createNewFile();
            System.out.println(level.getAbsolutePath());
            FileWriter writer = new FileWriter(level.getPath());
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        Pixelate.HANDLER.registerListener(player);
        Pixelate.getHud().setHotbar(player.getInventory());
        screen = new Screen(null, player, Pixelate.WIDTH, Pixelate.HEIGHT);
    }

    /**
     * Get the main player of this state
     * @return The player
     */
    public EntityPlayer getPlayer() {
        return player;
    }

    /**
     * Get the WorldManager of this state
     * @return The world manager
     */
    public WorldManager getWorldManager() {
        return worldManager;
    }

    @Override
    public void update() {
        worldManager.update();
        Pixelate.getHud().update();
        for (Module m : modules.values()) {
            m.update();
        }
    }

    @Override
    public void render(Canvas canvas) {
        screen.update(canvas, player, Pixelate.WIDTH, Pixelate.HEIGHT);
        worldManager.render(screen);
        Pixelate.getHud().render(screen);
        for (Module m : modules.values()) {
            if (m instanceof Renderable) {
                ((Renderable) m).render(screen);
            }
        }
        if (Settings.DEBUG) {
            screen.text("FPS: " + Timer.getFPS(), 10, 50, 60, Color.WHITE);
            screen.text("Delta: " + Timer.getDeltaTime() + "ms", 10, 110, 60, Color.WHITE);
            screen.text("Draw Calls: " + screen.getDrawCalls(), 10, 170, 60, Color.WHITE);
        }
    }

    @Override
    public void destroy() {
        worldManager.destroy();
        for (Module m : modules.values()) {
            m.destroy();
        }
        modules.clear();
    }

    @Override
    public <N extends Module> N getModule(Class<N> clazz) {
        return clazz.cast(modules.get(clazz));
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

    @Override
    public Collection<Module> getModules() {
        return modules.values();
    }
}

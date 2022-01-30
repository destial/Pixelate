package xyz.destiall.pixelate.states;

import android.graphics.Canvas;
import android.graphics.Color;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xyz.destiall.pixelate.GameSurface;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.WorldManager;
import xyz.destiall.pixelate.environment.generator.GeneratorUnderground;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.gui.HUD;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.meta.Enchantment;
import xyz.destiall.pixelate.items.meta.ItemMeta;
import xyz.destiall.pixelate.modular.Modular;
import xyz.destiall.pixelate.modular.Module;
import xyz.destiall.pixelate.position.Location;
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
            Pixelate.HANDLER.registerListener(player);
            HUD.INSTANCE.setHotbar(player.getInventory());
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
        Location location = new Location(0, 0, worldManager.getCurrentWorld());

        //Adding example items
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.getItemMeta().addEnchantment(Enchantment.DAMAGE_ALL, 1);
        ItemStack d_axe = new ItemStack(Material.WOODEN_AXE, 1);
        d_axe.getItemMeta().addEnchantment(Enchantment.DIG_SPEED, 5);

        ItemStack d_pickaxe = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        {
            ItemMeta meta = d_pickaxe.getItemMeta();
            List<String> lore = new ArrayList<String>();
            lore.add("Pick me up like u do with dat axe");
            lore.add("<33333333333333333333 ~pickaxe");
            meta.setLore(lore);
            if(meta.hasLore())
                System.out.println("Meta las lore");
            else
                System.out.println("No lore?");
            d_pickaxe.setItemMeta(meta);
        }

        ItemStack furnace = new ItemStack(Material.FURNACE, 1);
        ItemStack chest = new ItemStack(Material.CHEST, 1);
        ItemStack anvil = new ItemStack(Material.ANVIL, 1);
        ItemStack tnt = new ItemStack(Material.TNT, 4);
        player.getInventory().addItem(sword);
        player.getInventory().addItem(d_axe);
        player.getInventory().addItem(d_pickaxe);
        player.getInventory().addItem(furnace);
        player.getInventory().addItem(chest);
        player.getInventory().addItem(tnt);
        player.getInventory().addItem(anvil);

        player.getInventory().addItem(new ItemStack(Material.COAL_ORE,1));

        Location loc = worldManager.getCurrentWorld().getNearestEmpty(location);
        world.dropItem(chest, loc.add(Tile.SIZE, Tile.SIZE));
        player.teleport(loc.subtract(Tile.SIZE, Tile.SIZE));
        worldManager.getCurrentWorld().getEntities().add(player);
        Pixelate.HANDLER.registerListener(player);
        HUD.INSTANCE.setHotbar(player.getInventory());

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
        HUD.INSTANCE.update();
        for (Module m : modules.values()) {
            m.update();
        }
    }

    @Override
    public void render(Canvas canvas) {
        screen.update(canvas, player, Pixelate.WIDTH, Pixelate.HEIGHT);
        worldManager.render(screen);
        HUD.INSTANCE.render(screen);
        for (Module m : modules.values()) {
            if (m instanceof Renderable) {
                ((Renderable) m).render(screen);
            }
        }
        screen.text("FPS: " + Timer.getFPS(), 10, 50, 60, Color.WHITE);
        screen.text("Delta: " + Timer.getDeltaTime() + "ms", 10, 110, 60, Color.WHITE);
        screen.text("Draw Calls: " + screen.getDrawCalls(), 10, 170, 60, Color.WHITE);
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
}

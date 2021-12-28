package xyz.destiall.pixelate;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import xyz.destiall.java.events.EventHandling;
import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.WorldManager;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.items.inventory.Inventory;
import xyz.destiall.pixelate.modular.Module;
import xyz.destiall.pixelate.serialize.EntitySerializer;
import xyz.destiall.pixelate.serialize.InventorySerializer;
import xyz.destiall.pixelate.serialize.ModuleSerializer;
import xyz.destiall.pixelate.serialize.TileSerializer;
import xyz.destiall.pixelate.states.GSM;
import xyz.destiall.pixelate.states.StateGame;
import xyz.destiall.pixelate.timer.Timer;

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
}

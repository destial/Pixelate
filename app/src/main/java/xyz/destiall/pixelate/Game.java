package xyz.destiall.pixelate;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import java.util.HashMap;
import java.util.Map;

import xyz.destiall.java.events.EventHandling;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.WorldManager;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.items.crafting.Recipe;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.states.GSM;
import xyz.destiall.pixelate.states.StateGame;
import xyz.destiall.pixelate.states.StatePauseMenu;
import xyz.destiall.pixelate.timer.Timer;

public class Game extends Thread {
    public static final EventHandling HANDLER = new EventHandling();
    private static final Map<String, Recipe> recipeMap = new HashMap<>();
    public static int HEIGHT;
    public static int WIDTH;

    private static GameSurface gameSurface;
    private static SurfaceHolder surfaceHolder;
    private static Canvas canvas;
    private static Bitmap tileMap;
    private static GSM stateManager;
    public static boolean paused = false;
    private final Timer timer;
    private boolean running;

    public Game(GameSurface gameSurface, SurfaceHolder surfaceHolder)  {
        super();
        Game.gameSurface = gameSurface;
        Game.surfaceHolder = surfaceHolder;
        tileMap = ResourceManager.getBitmap(R.drawable.tilemap);
        tileMap = Bitmap.createScaledBitmap(tileMap, (int) (tileMap.getWidth() * 1.52), (int) (tileMap.getHeight() * 1.52), false);
        HEIGHT = gameSurface.getHeight();
        WIDTH = gameSurface.getWidth();
        timer = new Timer();
        stateManager = new GSM();
        stateManager.addState("Game", new StateGame(gameSurface));
        stateManager.addState("PauseMenu", new StatePauseMenu(gameSurface));
        stateManager.setState("Game");
    }

    @Override
    public void run() {
        long nsPerTick = 1000000000 / 60;
        long lastTime = System.nanoTime();
        long unprocessed = 0;
        while (running && !paused)  {
            try {
                canvas = surfaceHolder.lockCanvas();
                if(!paused)
                {
                    HEIGHT = canvas.getHeight();
                    WIDTH = canvas.getWidth();
                    update();
                    unprocessed += (Timer.getLastNanoTime() - lastTime) / nsPerTick;
                    lastTime = Timer.getLastNanoTime();
                    while (unprocessed >= 1) {
                        tick();
                        unprocessed -= 1;
                    }
                }
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
    }

    public void setRunning(boolean running)  {
        this.running = running;
    }

    private void update() {
        if(!paused)
        {
            timer.update();
            stateManager.update();
        }
    }

    private void render(Canvas canvas) {
        if(!paused)
        {
            canvas.drawRGB(0, 0, 0);
            stateManager.render(canvas);
        }
    }

    private void tick() {
        if(!paused)
        {
            stateManager.tick();
            timer.tick();
        }
    }

    public static boolean setWorld(String world) {
        StateGame gameState = ((StateGame) stateManager.getState("Game"));
        WorldManager wm = gameState.getWorldManager();
        if (wm.isAWorld(world) && !wm.isWorldActive(world)) {
            World current = wm.getCurrentWorld();
            current.getEntities().remove(gameState.getPlayer());

            //Set new active world
            wm.setActive(world);

            //Player codes
            World next = wm.getCurrentWorld();
            System.out.println("Current world: " + wm.getCurrentWorldName());
            next.getEntities().add(gameState.getPlayer());
            gameState.getPlayer().teleport(wm.getCurrentWorld().getNearestEmpty(new Location(0,0, wm.getCurrentWorld())));

            return true;
        }
        return false;
    }

    public static Bitmap getTileMap() {
        return tileMap;
    }

    public static Resources getResources() {
        return gameSurface.getResources();
    }

    public static void addRecipe(Recipe recipe) {
        recipeMap.put(recipe.getKey(), recipe);
    }

    public static Map<String, Recipe> getRecipes() {
        return recipeMap;
    }

    public static GSM getGSM() {
        return stateManager;
    }

    public static GameSurface getGameSurface() {
        return gameSurface;
    }
}
